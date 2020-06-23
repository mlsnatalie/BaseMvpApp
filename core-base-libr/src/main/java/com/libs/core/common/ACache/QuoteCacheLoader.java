package com.libs.core.common.ACache;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.v4.util.LruCache;
import android.text.TextUtils;

import com.libs.core.business.events.SaveQuoteCacheEvent;
import com.libs.core.common.utils.LogUtils;
import com.libs.core.common.utils.MyUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by zoubangyue on 2016/3/9.
 * <p/>
 * 行情数据缓存。可清空。
 * 采用内存和磁盘的二级缓存。
 * <p/>
 * 内存缓存使用LruCache.
 * 磁盘缓存使用ASimpleCache(增加文件超时控制).
 * <p/>
 * 缓存的目标：
 * 1.节约流量
 * 2.节约cpu/内存
 * 3.降低磁盘IO
 * 4.通过以上方式实现行情详情页的快速响应
 */
public class QuoteCacheLoader {
    private String TAG = "QuoteCacheLoader";

    /*内存缓存。Lru latest recently user 简单说就是喜新厌旧式缓存。*/
    private LruCache<String, Object> mMemoryCache;

    /*磁盘缓存管理器。ASimpleCache框架。*/
    private ACache mDiskCache;

    /*加载数据线程池*/
    private ExecutorService mLoadThreadPool = null;

    private Map<String, Integer> mExpireMap = new HashMap<>();

    /*单例缓存加载器*/
    private static QuoteCacheLoader loader;

    private boolean mIsClearing;

    private Handler mScheduledHandler;

    public static QuoteCacheLoader getInstance(Context context) {
        if (loader == null) {
            synchronized (QuoteCacheLoader.class) {
                if (loader == null) {
                    loader = new QuoteCacheLoader(context);
                }
            }
        }
        return loader;
    }

    private QuoteCacheLoader(final Context context) {
        //获取系统分配给每个应用程序的最大内存，每个应用系统分配32M
//        int maxMemory = (int) Runtime.getRuntime().maxMemory();
//        int mCacheSize = maxMemory / 8;
        //给LruCache分配1/8 4M
        mMemoryCache = new LruCache<String, Object>(3 * 1024 * 1024) {

            @Override
            protected int sizeOf(String key, Object value) {
                // 因为无法计算java对象的大小，所以设置400kb
                //kline 300根大概60k
                //min/premin 最大大概180k
                if (key.contains("min"))
                    return 1024 * 200;
                else if (key.contains("kline"))
                    return 1024 * 60;
                return 1024 * 50;
            }

            /**
             * 记录被移除时的回调。
             * 1.空间不够需要移除
             * 2.新元素替换老元素
             * 注意：新增一个新的不会走这个回调
             * @param evicted
             * @param key
             * @param oldValue
             * @param newValue
             */
            @Override
            protected void entryRemoved(boolean evicted, String key, Object oldValue, Object newValue) {
                super.entryRemoved(evicted, key, oldValue, newValue);
                try {
                    //正在清空缓存和磁盘则不写入
                    if (mIsClearing) {
                        LogUtils.d(TAG, "entryRemoved but clearing cache and disk");
                        return;
                    }
                    if (evicted) {
                        // 释放空间时，持久化到磁盘
                        Integer expire = mExpireMap.remove(key);

                        //说明已经持久化过了。为保险起见，无论如何再持久化一次
                        //if (expire == null) return;

                        if (expire != null && expire.intValue() > 0) {
                            putToDeviceCache(key, oldValue, expire);
                            LogUtils.d(TAG, "lru release space, persistenting[" + key + "][" + expire + "] to disk.");
                        } else {
                            putToDeviceCache(key, oldValue);
                            LogUtils.d(TAG, "lru release space, persistenting[" + key + "][-1] to disk.");
                        }
                    } else {
                        // 添加实体
                        if (newValue != null) {
                            Integer expire = mExpireMap.get(key);
                            LogUtils.d(TAG, "lru lazy putting[" + key + "]expire[" + expire + "] entry.");
                            //lazy persistent 加入缓存后进行延迟持久化
                            mScheduledHandler.postDelayed(new LazyPersistentRunnable(key), 10 * 1000);
                            //RxBus.getInstance().post(new SaveQuoteCacheEvent(key));
                        } else {
                            //移除实体
                            mExpireMap.remove(key);
                            try {
                                mDiskCache.remove(key);
                                //mDiskCache.clear();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            LogUtils.d(TAG, "lru removing[" + key + "] entry.");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        try {
            mDiskCache = ACache.get(MyUtils.getInstance().getCache(context, "quotecache"));
        } catch (Exception e) {
            e.printStackTrace();
            try {
                mDiskCache = ACache.get(context, "quotecache");
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }

        HandlerThread handlerThread = new HandlerThread("CacheHandlerThread");
        handlerThread.start();
        mScheduledHandler = new Handler(handlerThread.getLooper());
    }

    /**
     * 延迟持久化执行。
     *
     * @param saveQuoteCacheEvents
     */
    public void lazyPersistent(List<SaveQuoteCacheEvent> saveQuoteCacheEvents) {
        try {
            LogUtils.d(TAG, "lru lazy persistenting run in " + Thread.currentThread().getName());
            //过滤重复的数据到set中
            Set<SaveQuoteCacheEvent> set = new HashSet<>(saveQuoteCacheEvents);
            for (SaveQuoteCacheEvent temp : set) {
                String key = temp.getKey();
                Integer expire = mExpireMap.remove(key);
                if (expire == null) continue;

                Object value = mMemoryCache.get(key);
                if (value == null) continue;

                if (expire > 0)
                    putToDeviceCache(key, value, expire);
                else
                    putToDeviceCache(key, value);
                LogUtils.d(TAG, "lru lazy persistenting key[" + key + "]expire[" + expire + "] to disk.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取线程池的方法，因为涉及到并发的问题，我们加上同步锁
     *
     * @return
     */
    private ExecutorService getThreadPool() {
        if (mLoadThreadPool == null) {
            synchronized (ExecutorService.class) {
                if (mLoadThreadPool == null) {
                    //为了下载图片更加的流畅，我们用了2个线程来下载图片
                    mLoadThreadPool = Executors.newFixedThreadPool(2);
                }
            }
        }
        return mLoadThreadPool;
    }

    /**
     * 添加数据到内存缓存。
     * 替换老数据。
     * <p/>
     * 当首次放入内存时，不会调用entryRemoved回调，所以此处需要直接持久化
     *
     * @param key
     * @param data
     */
    private void putToMemoryCache(String key, Object data) {
        if (!TextUtils.isEmpty(key) && data != null) {
            Object previous = mMemoryCache.put(key, data);
            LogUtils.d(TAG, "lru putting to memcache[" + key + "] entry.");
            if (previous == null) {
                Integer expire = mExpireMap.remove(key);
                if (expire == null) expire = -1;
                putToDeviceCache(key, data, expire);
            }
        }
    }

    /**
     * 添加数据到内存缓存。
     * 替换老数据。
     *
     * @param key
     * @param data
     * @param withoutToDisk 只放入缓存，不入磁盘
     */
    private void putToMemoryCache(String key, Object data, boolean withoutToDisk) {
        if (!TextUtils.isEmpty(key) && data != null) {
            mMemoryCache.put(key, data);
            LogUtils.d(TAG, "lru putting to memcache[" + key + "] entry.");
        }
    }

    /**
     * 添加数据到磁盘缓存。
     * 替换老数据。
     *
     * @param key
     * @param data
     * @return 是否保存成功
     */
    private boolean putToDeviceCache(String key, Object data) {
        return putToDeviceCache(key, data, -1);
    }

    /**
     * 添加数据到磁盘缓存。
     * 替换老数据。
     *
     * @param key
     * @param data
     * @return 是否保存成功
     */
    private boolean putToDeviceCache(String key, Object data, int expire) {
        //判断key是否为空,数据是否序列化
        if (TextUtils.isEmpty(key))
            return false;
        if (data instanceof Serializable) {
            try {
                //是否是ArrayList
                if (data instanceof ArrayList<?>) {
                    ArrayList<?> dd = (ArrayList<?>) data;
                    int len = dd.size();
                    if (len > 1500) {
                        //超过600，入磁盘的时候进行截断，防止数据过多
                        LogUtils.d(TAG, "lru data count tolarge[" + len + "], cut it[" + key + "].");
                        LogUtils.d(TAG, "lru putting to disk[" + key + "] entry.");
                        mDiskCache.put(key, new ArrayList<>(dd.subList(len - 1000, len)));
                        return true;
                    }
                }
                LogUtils.d(TAG, "lru putting to disk[" + key + "] entry.");
                mDiskCache.put(key, (Serializable) data, expire);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 添加数据到缓存。
     * 替换老数据。
     *
     * @param key
     * @param data
     */
    public void put(String key, Object data) {
        mExpireMap.put(key, -1);
        putToMemoryCache(key, data);
    }

    /**
     * 添加数据到缓存。
     * 替换老数据。
     *
     * @param key
     * @param data
     */
    public void put(String key, Object data, int expire) {
        mExpireMap.put(key, expire);
        putToMemoryCache(key, data);
    }

    /**
     * 立马存入内存和磁盘
     *
     * @param key
     * @param obj
     * @param putToDisk
     */
    public void put(String key, Object obj, boolean putToDisk) {
        put(key, obj);
        if (putToDisk) {
            putToDeviceCache(key, obj);
        }
    }

    /**
     * 移除内存数据，同时也会持久化到磁盘。
     *
     * @param key
     */
    public void remove(String key) {
        if (!TextUtils.isEmpty(key)) {
            mMemoryCache.remove(key);
        }
    }

//    /**
//     * 从内存和磁盘删除
//     *
//     * @param key
//     * @param immediately
//     */
//    public void remove(String key, boolean immediately) {
//        try {
//            remove(key);
//            mDiskCache.remove(key);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    /**
     * 从内存缓存中获取数据。
     *
     * @param key
     * @return
     */
    private Object getDataFromMemCache(String key) {
        return mMemoryCache.get(key);
    }

    /**
     * 从磁盘缓存中获取数据。
     *
     * @param key
     * @return
     */
    private Object getDataFromDeviceCache(String key) {
        Object data = mDiskCache.getAsObject(key);
        //将磁盘获取到的数据，放入内存
        putToMemoryCache(key, data, true);
        return data;
    }

    /**
     * 同步加载对象缓存。
     *
     * @param key
     * @return
     */
    public Object loadData(final String key) {
        return loadData(key, (onDataLoaderListener) null);
    }

    /**
     * 同步加载对象缓存。
     *
     * @param key
     * @return
     */
    public Object loadData(final String key, Class<?> clazz) {
        Object object = loadData(key, (onDataLoaderListener) null);
        if (object != null && object.getClass() == clazz) {
            return object;
        }
        return null;
    }

//    /**
//     * 同步加载protobuf缓存。
//     * 在内存时pb对象的形式，在磁盘中是byte[]，见 putToDeviceCache()
//     *
//     * @param key
//     * @param builder
//     * @return
//     */
//    public Object loadPbData(final String key, MessageLite.Builder builder) {
//        if (key == null) {
//            LogUtils.d(TAG, "get pb key cannot be null");
//            return null;
//        }
//        Object data = getDataFromMemCache(key);
//        if (data != null) {
//            // 从内存获取到数据
//            LogUtils.d(TAG, "geted pb from memcache[" + key + "]");
//            return data;
//        } else {
//            try {
//                byte[] bytes = mDiskCache.getAsBinary(key);
//                if (bytes != null) {
//                    data = builder.mergeFrom(bytes).build();
//                    putToMemoryCache(key, data);
//                }
//            } catch (InvalidProtocolBufferException e) {
//                e.printStackTrace();
//            } catch (Exception e) {
//                LogUtils.e(TAG, "", e);
//            }
//        }
//        return data;
//    }

    /**
     * 获取缓存，若加回调则内存没获取到就开始通过异步进行加载。
     *
     * @param key
     * @param listener
     * @return
     */
    public Object loadData2(final String key, final onFileLoaderListener listener) {
        Object data = showCacheData(key);

        if (data != null) {
            return data;
        }

        if (listener != null) {
            final Handler handler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    listener.onFileLoader(msg.obj, key);
                }
            };

            getThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    // 从磁盘加载
                    Object data = null;
                    try {
                        data = getDataFromDeviceCache(key);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    //通知回调处理
                    Message msg = handler.obtainMessage();
                    msg.obj = data;
                    handler.sendMessage(msg);

                    // 加入内存缓存
                    putToMemoryCache(key, data);
                }
            });
        }

        return null;
    }

    public Object loadData(final String key, final onDataLoaderListener listener) {
        Object data = showCacheData(key);

        if (data != null) {
            return data;
        }

        if (listener != null) {
            final Handler handler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    listener.onDataLoader(msg.obj, key);
                }
            };

            getThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    // 从磁盘加载
                    Object data = null;
                    try {
                        data = getDataFromDeviceCache(key);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    //通知回调处理
                    Message msg = handler.obtainMessage();
                    msg.obj = data;
                    handler.sendMessage(msg);

                    // 加入内存缓存
                    putToMemoryCache(key, data);
                }
            });
        }

        return null;
    }

    /**
     * 获取数据, 内存中没有就去手机或者sd卡中获取，这一步在getView中会调用，比较关键的一步
     *
     * @param key
     * @return
     */
    private Object showCacheData(String key) {
        Object data = getDataFromMemCache(key);
        if (data != null) {
            // 从内存获取到数据
            LogUtils.d(TAG, "geted from memcache[" + key + "]");
            return data;
        } else {
            //从磁盘获取缓存数据
            try {
                data = getDataFromDeviceCache(key);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (data != null)
                LogUtils.d(TAG, "geted from diskcache[" + key + "]");
            else
                LogUtils.d(TAG, "no data from memcache and diskcache[" + key + "]");

            return data;
        }
    }

    /**
     * 取消正在下载的任务
     */
    public synchronized void cancelTask() {
        if (mLoadThreadPool != null) {
            mLoadThreadPool.shutdownNow();
            mLoadThreadPool = null;
        }
    }

    /**
     * 异步加载缓存数据。
     */
    public interface onDataLoaderListener {
        void onDataLoader(Object data, String key);
    }

    public interface onFileLoaderListener {
        void onFileLoader(Object data, String key);
    }

    class LazyPersistentRunnable implements Runnable {
        private String key;

        public LazyPersistentRunnable(String key) {
            this.key = key;
        }

        @Override
        public void run() {
            Integer expire = mExpireMap.remove(key);
            if (expire == null) return;

            Object value = mMemoryCache.get(key);
            if (value == null) return;

            if (expire > 0)
                putToDeviceCache(key, value, expire);
            else
                putToDeviceCache(key, value);
            LogUtils.d(TAG, "lru lazy persisenting key[" + key + "]expire[" + expire + "] to disk.");
        }
    }

    public void clear() {
        try {
            LogUtils.d(TAG, "clear cache and disk...");
            mIsClearing = true;
            mExpireMap.clear();
            mMemoryCache.evictAll();
            mDiskCache.clear();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mIsClearing = false;
        }
    }
}
