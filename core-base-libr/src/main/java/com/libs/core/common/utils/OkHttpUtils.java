package com.libs.core.common.utils;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.internal.$Gson$Types;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * OKHttp辅助类
 * <p>
 * 1.多路复用,请求优化
 * 2.支持服务器推送技术
 * 3.SPDY压缩了HTTP头
 * 4.强制使用SSL传输协议
 * 5.多文件上传和下载更加简单
 *
 * @author zhang.zheng
 * @version 2018-05-19
 */
public class OkHttpUtils {

    /**
     * 文件下载常量
     */
    public static final String FILE_SIZE = "file_size";// 文件大小
    public static final String DOWN_SIZE = "down_size";// 下载长度
    public static final String SAVE_PATH = "save_path";// 保存路径

    private Gson mGson;
    private Handler mHandler;
    private OkHttpClient mOkHttpClient;
    private static volatile OkHttpUtils mOkHttpUtils;


    private OkHttpUtils() {
        mGson = new Gson();
        mOkHttpClient = new OkHttpClient();
        mHandler = new Handler(Looper.getMainLooper());
    }

    public static OkHttpUtils getInstance() {
        if (mOkHttpUtils == null) {
            synchronized (OkHttpUtils.class) {
                if (mOkHttpUtils == null) {
                    mOkHttpUtils = new OkHttpUtils();
                }
            }
        }
        return mOkHttpUtils;
    }

    /******************************************Get请求*********************************************/

    /**
     * Get请求
     */
    public void asynGet(String url, Map<String, String> params, final ResultCallback callback) {
        Request request = buildGetRequest(url, params);
        deliveryResult(callback, request);
    }


    private Request buildGetRequest(String url, Map<String, String> params) {
        StringBuilder sb = new StringBuilder(url);
        if (params != null && !params.isEmpty()) {
            Set<Map.Entry<String, String>> entries = params.entrySet();
            sb.append("?");
            for (Map.Entry<String, String> entry : entries) {
                sb.append(entry.getKey() + "=" + entry.getValue() + "&");
            }
            sb.deleteCharAt(sb.lastIndexOf("&"));
        }

        LogUtils.e(this, "newUrl:" + sb.toString());
        return new Request.Builder()
                .url(sb.toString())
                .build();
    }

    /*****************************************Post请求*********************************************/

    /**
     * Post请求
     */
    public void asynPost(String url, Map<String, String> params, final ResultCallback callback) {
        Request request = buildPostRequest(url, params);
        deliveryResult(callback, request);
    }


    private Request buildPostRequest(String url, Map<String, String> params) {
        FormBody.Builder body = new FormBody.Builder();
        if (params != null && !params.isEmpty()) {
            Set<Map.Entry<String, String>> entries = params.entrySet();
            for (Map.Entry<String, String> entry : entries) {
                body.add(entry.getKey(), entry.getValue());
            }
        }
        return new Request.Builder()
                .url(url)
                .post(body.build())
                .build();
    }

    /******************************************文件上传*********************************************/

    /**
     * 文件上传（单文件）
     */
    public void asynPostFile(String url, File file, String fileKey, Map<String, String> params,
                             ResultCallback callback) {
        Request request = buildMultipartFormRequest(url, new File[]{file}, new String[]{fileKey}, params);
        deliveryResult(callback, request);
    }

    /**
     * 文件上传（多文件）
     */
    public void asynPostFile(String url, File[] files, String[] fileKeys, Map<String, String> params,
                             ResultCallback callback) {
        Request request = buildMultipartFormRequest(url, files, fileKeys, params);
        deliveryResult(callback, request);
    }



    /**
     * 构建表单请求
     */
    private Request buildMultipartFormRequest(String url, File[] files, String[] fileKeys, Map<String, String> params) {
        // 表单请求体
        MultipartBody.Builder body = new MultipartBody.Builder();
        body.setType(MultipartBody.FORM);
        // 拼接请求参数
        if (params != null && !params.isEmpty()) {
            Set<Map.Entry<String, String>> entries = params.entrySet();
            for (Map.Entry<String, String> entry : entries) {
                body.addPart(Headers.of("Content-Disposition", "form-data; type=\"" + entry.getKey() + "\""),
                        RequestBody.create(null, entry.getValue()));
            }
        }
        // 拼接文件数据
        if (files != null && files.length > 0) {
            RequestBody fileBody;
            for (int i = 0; i < files.length; i++) {
                String fileName = files[i].getName();
                // 根据文件名设置contentType
                fileBody = RequestBody.create(MediaType.parse(parseFileMimeType(fileName)), files[i]);
                body.addPart(Headers.of("Content-Disposition", "form-data; type=\"" + fileKeys[i] + "\"; filename=\"" + fileName + "\""),
                        fileBody);
            }
        }

        return new Request.Builder()
                .url(url)
                .post(body.build())
                .build();
    }

    /**
     * 解析文件Mime类型
     */
    private String parseFileMimeType(String filePath) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentType = fileNameMap.getContentTypeFor(filePath);
        if (contentType == null) {
            contentType = "application/octet-stream";
        }
        return contentType;
    }

    /********************************************文件下载*******************************************/

    /**
     * 下载文件
     *
     * @param url      下载链接
     * @param saveDir  保存目录
     * @param callback 回调函数
     */
    public void asynFileDownload(final String url, final String saveDir, final ResultCallback callback) {
        final Request request = new Request.Builder().url(url).build();

        mOkHttpClient.newCall(request).enqueue(new Callback() {

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                InputStream inputStream = null;
                FileOutputStream outputStream = null;
                byte[] buf = new byte[2048];
                long downSize = 0;
                try {
                    inputStream = response.body().byteStream();
                    File file = new File(saveDir, parseFileName(url));
                    outputStream = new FileOutputStream(file);

                    // 保存下载信息
                    Map<String, Object> downInfo = new HashMap<>();
                    downInfo.put(FILE_SIZE, response.body().contentLength());
                    downInfo.put(SAVE_PATH, file.getAbsolutePath());
                    int length;
                    while ((length = inputStream.read(buf)) != -1) {
                        // 读写文件
                        outputStream.write(buf, 0, length);
                        // 已下载长度
                        downSize += length;
                        downInfo.put(DOWN_SIZE, downSize);
                        // 发送通知更新UI
                        sendSuccessResultCallback(downInfo, callback);
                    }
                    outputStream.flush();
                } catch (Exception e) {
                    sendFailedStringCallback(response.request(), e, callback);
                } finally {
                    try {
                        if (inputStream != null) {
                            inputStream.close();
                        }
                        if (outputStream != null) {
                            outputStream.close();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call call, @Nullable IOException e) {
                sendFailedStringCallback(request, e, callback);
            }
        });
    }

    /**
     * 解析文件名称
     *
     * @param path 文件路径
     */
    private String parseFileName(String path) {
        String fileName;
        int separatorIndex = path.lastIndexOf("/");
        if (separatorIndex < 0) {
            fileName = path;
        } else {
            fileName = System.currentTimeMillis() + path.substring(separatorIndex + 1, path.length());
        }
        return fileName;
    }


    /**
     * 处理请求回调
     */
    private void deliveryResult(final ResultCallback callback, final Request request) {
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @Nullable IOException e) {
                sendFailedStringCallback(request, e, callback);
            }

            @Override
            public void onResponse(@Nullable Call call, @NonNull Response response) {
                try {
                    final String string = response.body().string();
                    if (callback.mType == String.class) {
                        sendSuccessResultCallback(string, callback);
                    } else {
                        Object obj = mGson.fromJson(string, callback.mType);
                        sendSuccessResultCallback(obj, callback);
                    }
                } catch (Exception e) {
                    sendFailedStringCallback(response.request(), e, callback);
                }
            }
        });

    }

    /**
     * 发送成功回调
     */
    @SuppressWarnings("unchecked")
    private void sendSuccessResultCallback(final Object object, final ResultCallback callback) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (callback != null) {
                    callback.onResponse(object);
                }
            }
        });
    }

    /**
     * 发送失败回调
     */
    private void sendFailedStringCallback(final Request request, final Exception e, final ResultCallback callback) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (callback != null) {
                    callback.onError(request, e);
                }
            }
        });
    }

    /**
     * 结果回调类
     */
    public static abstract class ResultCallback<T> {
        Type mType;

        protected ResultCallback() {
            mType = getSuperclassTypeParameter(getClass());
        }

        static Type getSuperclassTypeParameter(Class<?> subclass) {
            Type superclass = subclass.getGenericSuperclass();
            if (superclass instanceof Class) {
                throw new RuntimeException("Missing type parameter.");
            }
            ParameterizedType parameterized = (ParameterizedType) superclass;
            return $Gson$Types.canonicalize(parameterized.getActualTypeArguments()[0]);
        }

        public abstract void onResponse(T response);

        public abstract void onError(Request request, Exception e);
    }


}
