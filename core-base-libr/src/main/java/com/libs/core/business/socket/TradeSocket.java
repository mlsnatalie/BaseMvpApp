package com.libs.core.business.socket;

import android.text.TextUtils;

import com.libs.core.business.events.LiveEvent;
import com.libs.core.business.vo.ChatVo;
import com.libs.core.common.encrypt.MD5Utils;
import com.libs.core.common.rxbus.RxBus;
import com.libs.core.common.utils.GsonUtils;
import com.libs.core.common.utils.LogUtils;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by blazers on 2017/8/4.
 * 用于处理交易的长连接
 */

public class TradeSocket {

    /**
     * 连接与断开连接的锁 同一时间只能有连接或断开连接 否则会造成已经刚刚新建的mInstance被关闭的情况
     */
    private static final Object sConnectShutdownLock = new Object();

    /**
     * 自动重连次数
     */
    private static final int AUTO_RECONNECT_MAX_TIME = 3;

    /**
     * 自动重连尝试间隔
     */
    private static final long AUTO_RECONNECT_INTERVAL = 5 * 1000;

    /**
     * 尝试连接的最大次数 避免由于连接而造成的锁死与资源浪费
     */
    private static final int TRY_CONNECT_MAX_TIME = 5;

    /**
     * 尝试连接的间隔
     */
    private static final long TRY_CONNECT_INTERVAL = 3 * 1000;

    /**
     * Socket读取超时
     */
    private static final int READ_TIME_OUT = 40 * 1000;

    /**
     * 心跳包间隔
     */
    private static final long HEART_BEAT_INTERVAL = 10 * 1000;

    /**
     * 最大错误次数
     */
    private static final int MAX_ERROR_TIME = 3;


    /**
     * 初始化一个ChatVo对象
     **/
    private ChatVo chatVo;
    /**
     * 心跳包定义字段
     */
    private static final String HEART_BEAT_PACKET = "wd_heartbeat";
    private static final String CHAT_DATA = "wd_chat";
    private static final String CHAT_LIKE = "wd_like";
    private static final String CHAT_PEOPLE = "wd_room";

    /**
     * 验签字段
     */
    private static final String AUTH1 = "auth1";

    /**
     * 验签返回
     */
    private static final String AUTH2 = "auth2";

    /**
     * 后台验签密钥
     */
    private static final String SECREATKEY = "6PIRqVw3cRm84dKVgPlp";

//    private static final int HEART_BEAT_SIZE = HEART_BEAT_PACKET.getSerializedSize();

    /**
     * 单例对象
     */
    private static volatile TradeSocket mInstance;

    // Vars - 会被创建多次的对象

    /**
     * Socket对象
     */
    private Socket mSocket;

    /**
     * 线程池
     */
    private ExecutorService mExecutorService;

    // Vars - 仅创建一次的对象

    /**
     * 运行状态
     */
    private boolean mConnectOrShutdownFlag;

    /**
     * 连接状态
     */
    private boolean mIsConnected;

    /**
     * 发送队列
     */
    private Queue<byte[]> mWriteQueue;

    /**
     * 发生错误的次数
     */
    private AtomicInteger mCurrentConnectionErrorOccurTimes;

    /**
     * 自动重连次数
     */
    private AtomicInteger mCurrentInstanceReconnectTime;

    /**
     * 当前尝试连接的次数
     */
    private int mCurrentConnectionTryConnectTime;
    /**
     * 当前尝试连接的房间ID
     */
    private String roomId;

    private String userId;

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * 初始化线程池 开启各个任务线程
     */
    private TradeSocket() {
        mWriteQueue = new LinkedBlockingQueue<>(256);
        mCurrentInstanceReconnectTime = new AtomicInteger(0);
        mCurrentConnectionErrorOccurTimes = new AtomicInteger(0);
    }

    /**
     * 获取单例 并初始化
     *
     * @return
     */
    public static TradeSocket getInstance() {
        if (mInstance == null) {
            synchronized (TradeSocket.class) {
                mInstance = new TradeSocket();
            }
        }
        return mInstance;
    }

    /**
     * 初始化链接，第一次链接
     **/
    public void startCoon() {
        if (mInstance != null) {
            // 尝试一次连接
            mInstance.initialize();
        }
    }

    /**
     * 关闭连接 线程安全
     */
    public static void shutdown(boolean reconnect) {
        if (mInstance == null || mInstance.mSocket == null || mInstance.mSocket.isClosed()) {
            return;
        }
        synchronized (sConnectShutdownLock) {
            if (mInstance == null || !mInstance.mConnectOrShutdownFlag) {
                return;
            }
            mInstance.mIsConnected = false;
            LogUtils.d("====", "关闭连接 @ " + Thread.currentThread().getName());
            mInstance.mConnectOrShutdownFlag = false; // Flag置为False
            mInstance.mWriteQueue.clear(); // 清空发送队列
            try {
                mInstance.mSocket.close(); // 关闭Socket
            } catch (IOException e) {
                e.printStackTrace();
            }
            mInstance.mExecutorService.shutdownNow(); // 立即关闭executor
            // 是否需要重连
            if (reconnect) {
                if (mInstance.mCurrentInstanceReconnectTime.incrementAndGet() <= AUTO_RECONNECT_MAX_TIME) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            if (mInstance != null)
                                LogUtils.d("====", "目前已自动重连次数: " + mInstance.mCurrentInstanceReconnectTime + "  " + AUTO_RECONNECT_MAX_TIME / 1000 + " 秒后尝试重新连接");
                            try {
                                Thread.sleep(AUTO_RECONNECT_INTERVAL);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            if (mInstance != null)
                                mInstance.initialize();
                        }
                    }).start();
                } else {
                    LogUtils.d("====", "重连已达到最大次数");
                    mInstance = null;
                }
            } else {
                LogUtils.d("====", "连接已完全关闭");
                mInstance = null;
            }
        }
    }

    private void initialize() {
        synchronized (sConnectShutdownLock) {
            if (mConnectOrShutdownFlag) {
                return;
            }
            LogUtils.d("====", "<Constructor> " + Thread.currentThread().getName() + " 目前重连次数: " + mCurrentInstanceReconnectTime.get());
            // 开启Executors 开启 连接 发送 读取 心跳维持 线程
            mConnectOrShutdownFlag = true;
            mExecutorService = Executors.newFixedThreadPool(3);
            mExecutorService.execute(new ConnectTaskThread());
        }
    }

    /**
     * 发送数据
     *
     * @param baseMsg
     */
    public void send(byte[] baseMsg) {
        mWriteQueue.add(baseMsg);
        if (!mConnectOrShutdownFlag)
            initialize();
    }

    /**
     * 删除待发送数据 若有
     *
     * @param baseMsg
     */
    public void remove(byte[] baseMsg) {
        mWriteQueue.remove(baseMsg);
    }

    /**
     * 是否关闭连接 TODO 处理自动重连？自动重传数据？
     */
    private void judgeForgiveOrNot() {
        int errorTimes = mCurrentConnectionErrorOccurTimes.incrementAndGet();
        if (errorTimes > MAX_ERROR_TIME) {
            LogUtils.d("====", "接受与发送过程中错误次数过多，即将关闭连接，是否尝试自动重连： true");
            shutdown(true);
        }
    }

    /**
     * 连接线程
     */
    private class ConnectTaskThread extends Thread {
        @Override
        public void run() {
            super.run();
            mSocket = new Socket();
            mIsConnected = false; // 初始化连接状态
            mCurrentConnectionTryConnectTime = 0; // 初始化连接次数
            mCurrentConnectionErrorOccurTimes.set(0);
            try {
                mSocket.setTcpNoDelay(true);
                mSocket.setTrafficClass(0x04 | 0x10);
                mSocket.setSoTimeout(READ_TIME_OUT);
                mSocket.setSoLinger(true, 0);
                mSocket.setKeepAlive(true);
            } catch (SocketException e) {
                e.printStackTrace();
            }
            while (!mIsConnected && mConnectOrShutdownFlag) {
                LogUtils.d("====", "<Try Connecting>");
                try {
                    // 获取IP 端口
                    String ip = ChatSocketDefine.getChatIp();
                    int port = ChatSocketDefine.getChatPort();
                    LogUtils.d("====", "ip = " + ip + " ,port = " + port);
                    mSocket.connect(new InetSocketAddress(ip, port));
                    LogUtils.d("====", "<Connected>");
                    mIsConnected = true; // 连接状态改变
                    mCurrentInstanceReconnectTime.set(0); // 连接成功 则重置已经自动重连的次数为0 使其仍可自动重连
                    mExecutorService.execute(new SendTaskThread());
                    mExecutorService.execute(new ReadTaskThread());
                } catch (IOException e) {
                    if (++mCurrentConnectionTryConnectTime <= TRY_CONNECT_MAX_TIME) {
                        LogUtils.e("====", "Connect to x x failed! Will Retry in " + TRY_CONNECT_INTERVAL + "  " + e.toString());
                        try {
                            Thread.sleep(TRY_CONNECT_INTERVAL);
                        } catch (InterruptedException e1) {
                            e1.printStackTrace();
                        }
                    } else {
                        LogUtils.d("====", "尝试连接已达到最大次数， 即将关闭");
                        shutdown(false);
                    }
                }
            }
            LogUtils.d("====", "<ConnectTask> thread done!");
        }
    }

    /**
     * 发送数据线程
     */
    private class SendTaskThread extends Thread {

        //发送心跳的时间戳
        long heartBeatTime = 0;

        @Override
        public void run() {
            super.run();
            while (true) {
                try {
                    if (!mIsConnected)
                        break;
                    if (mWriteQueue.isEmpty()) {
                        long currentTime = System.currentTimeMillis();
                        if (currentTime - heartBeatTime > HEART_BEAT_INTERVAL) {
                            //时间间隔大于30秒，则发送心跳
                            heartBeatTime = currentTime;
                            // 写消息头
                            chatVo = new ChatVo();
                            chatVo.setCmd(HEART_BEAT_PACKET);
                            String chatJson = GsonUtils.toJson(chatVo);
                            int length = chatJson.getBytes().length;
                            mSocket.getOutputStream().write(SocketUtils.intToByte(length, 4));
                            // 写消息体
                            mSocket.getOutputStream().write(chatJson.getBytes());
                            mSocket.getOutputStream().flush();
                            LogUtils.d("====", "发送心跳包");
                        }
                        Thread.sleep(1200);
                        continue;
                    }
                    // 取消息
                    byte[] baseMsg = mWriteQueue.poll();
                    int length = baseMsg.length;
                    // 写消息头
                    mSocket.getOutputStream().write(SocketUtils.intToByte(length, 4));
                    // 写消息体
                    mSocket.getOutputStream().write(baseMsg);
                    mSocket.getOutputStream().flush();
//                    LogUtils.d("====", "Send: ReqId -> " + baseMsg.getHead().getReqID());
                    Thread.sleep(1200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (SocketTimeoutException e) {
                    // Socket 读超时 错误次数来处理
                    LogUtils.e("==== Send Time Out", e.toString());
                    judgeForgiveOrNot();
                } catch (Exception e) {
                    LogUtils.e("==== Send IO || EXC", e.toString());
                    judgeForgiveOrNot();
                }
            }
            LogUtils.d("====", "<SendTaskThread> thread done!");
        }
    }

    /**
     * 接收数据线程
     */
    private class ReadTaskThread extends Thread {

        @Override
        public void run() {
            super.run();
            while (true) {
                if (!mIsConnected)
                    break;
                // 处理数据
                try {
                    byte[] head = new byte[4];
                    int read = mSocket.getInputStream().read(head);
                    if (read == -1) {
                        throw new IOException("End of stream");
                    }
                    LogUtils.d("====", "Received");
                    if (read != 4) {
                        continue;
                    }
                    int bodyLength = SocketUtils.bytesToInt(head);
                    if (bodyLength > 0) {
                        if (bodyLength > 8388608)
                            throw new RuntimeException("数据长度超过8M，发送传输异常，重启交易长连接");
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        int offset = 0;
                        byte[] bytes = new byte[bodyLength];
                        while (offset < bodyLength) {
                            int size = mSocket.getInputStream().read(bytes, offset, bodyLength - offset);
                            if (size > 0) {
                                offset += size;
                                baos.write(bytes, offset - size, size);
                            } else {
                                break;
                            }
                        }
                        baos.flush();
                        baos.close();
                        LogUtils.d("====", "收到直播间聊天数据" + baos.toString());
//                        Gson gson = new Gson();
//                        ChatVo chatVo = gson.fromJson(baos.toString(), ChatVo.class);
                        JSONObject jsonObject = new JSONObject(baos.toString());
                        String cmd = jsonObject.getString("cmd");
                        LogUtils.d("====", "Receive cmd:  -> " + cmd);
                        if (TextUtils.equals(HEART_BEAT_PACKET, cmd)) {
                            // 不需要处理
                        } else if (TextUtils.equals(AUTH1, cmd)) {
                            JSONObject resultJson = jsonObject.getJSONObject("result");
                            String seed = resultJson.getString("seed");
                            ;
//                            String seed = chatVo.getResult().getSeed();
                            String auth2 = MD5Utils.encrypt((seed + SECREATKEY).getBytes());
                            ChatVo newChatVo = new ChatVo();
                            newChatVo.setCmd(AUTH2);
                            newChatVo.setAuthCode(auth2);
                            if (!TextUtils.isEmpty(roomId)) {
                                newChatVo.setRoom_id(roomId);
                            }

                            if (!TextUtils.isEmpty(userId)) {
                                newChatVo.setUserId(userId);
                            }
                            String chatJson = GsonUtils.toJson(newChatVo);
                            LogUtils.d("====", "向后台传输数据" + chatJson);
                            send(chatJson.getBytes());
                        } else if (TextUtils.equals(CHAT_PEOPLE, cmd)) {
                            JSONObject resultJson = jsonObject.getJSONObject("result");
                            int num = resultJson.getInt("persons");
//                            int num = chatVo.getResult().getPersons();
                            LogUtils.d("====", "发送广播数据-真实人数:" + num);
//                            if (num < 500) {
//                                num = num * 20 + new Random().nextInt(10);
//                            }
//                            LogUtils.d("====", "发送广播数据-虚构人数:" + num);
//                            PreferenceManager.putInt(PreferConst.LAST_LIVE_NUM, num);
                            LiveEvent liveEvent1 = new LiveEvent(LiveEvent.LIVE_CHAT_PEOPLE);
                            liveEvent1.putExtra(LiveEvent.CHAT_PEOPLE, num);
                            RxBus.getInstance().post(liveEvent1);
                        } else if (TextUtils.equals(CHAT_DATA, cmd) || TextUtils.equals(CHAT_LIKE, cmd)) {
                            LogUtils.d("====", "发送广播数据" + baos.toString());

                            JSONObject resultJson = jsonObject.getJSONObject("result");
                            JSONObject messageJson = resultJson.getJSONObject("message");
                            JSONObject userJson = resultJson.getJSONObject("user");
                            int msgType = messageJson.getInt("msgType");
                            if (msgType == 1) {
                                ChatVo receiverChatVo = new ChatVo();
                                receiverChatVo.setCmd(jsonObject.getString("cmd"));
                                receiverChatVo.setRoom_id(roomId);
                                ChatVo.ResultBean resultBean = new ChatVo.ResultBean();
                                resultBean.setMessage(messageJson.getString("content"));
                                resultBean.setSendUserId(messageJson.getString("sendUserId"));

                                ChatVo.ResultBean.UserBean userBean = new ChatVo.ResultBean.UserBean();
                                userBean.setUid(userJson.getString("uid"));
                                userBean.setIcon(userJson.getString("icon"));
                                userBean.setNickname(userJson.getString("nickname"));

                                int isTeacher = messageJson.getInt("isTeacher");
                                if (isTeacher == 1) {
                                    int teacherIdentity = messageJson.getInt("teacherIdentity");
                                    if (teacherIdentity == 1) {//老师
                                        userBean.setUserType("4");
                                    } else if (teacherIdentity == 2) {//助理
                                        userBean.setUserType("2");
                                    } else {//主持人
                                        userBean.setUserType("3");
                                    }
                                } else {//观众
                                    userBean.setUserType("1");
                                }

                                resultBean.setUser(userBean);
                                receiverChatVo.setResult(resultBean);
                                LiveEvent liveEvent = new LiveEvent(LiveEvent.LIVE_CHAT_DATA);
                                liveEvent.putExtra(LiveEvent.CHAT_DATA, receiverChatVo);
                                RxBus.getInstance().post(liveEvent);
                            }
                        }
                    }
                } catch (SocketTimeoutException e) {
                    // Socket 读超时 错误次数来处理
                    LogUtils.e("==== Read Time Out", e.toString());
                    judgeForgiveOrNot();
                } catch (Exception e) {
                    LogUtils.e("==== Read IO || EXC", e.toString());
                    judgeForgiveOrNot();
                }
            }
            LogUtils.d("====", "<ReadTaskThread> thread done!");
        }
    }
}
