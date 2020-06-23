package com.libs.core.business.http.proxy;
/*
 * Created by zhangxiaowei on 2018/9/11 0011.
 */

import android.annotation.SuppressLint;

import com.libs.core.common.utils.ListUtils;
import com.libs.core.common.utils.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.http.HttpHeaders;
import okio.Buffer;
import okio.BufferedSource;

public class LogInterceptor implements Interceptor {
    static final String TAG = "LogInterceptor";

    @SuppressLint("DefaultLocale")
    @Override
    public Response intercept(Chain chain) throws IOException {
        //===============
        Request request = chain.request();
        String requestMethod = request.method();
        HttpUrl httpUrl = request.url();
        RequestBody requestBody = request.body();
        Response response = null;
        StringBuilder requestBuilder = null;
        StringBuilder responseBuilder = null;
        try {
            //String startTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss SSS").format(new Date());
            requestBuilder = new StringBuilder();
            requestBuilder.append("发送请求 : ");
            /*Headers headers = request.headers();
            if (headers != null) {
                for (int i = 0, count = headers.size(); i < count; i++) {
                    String name = headers.name(i);
                    // Skip headers from the request body as they are explicitly logged above.
                    if (!"Content-Type".equalsIgnoreCase(name) && !"Content-Length".equalsIgnoreCase(name)) {
                        requestBuilder.append(name);
                        requestBuilder.append(" : ");
                        requestBuilder.append(headers.value(i));
                        requestBuilder.append("\n");
                    }
                }
            }*/
            requestBuilder.append("\nurl : ");
            requestBuilder.append(request.url());
            requestBuilder.append("\nmethod : ");
            requestBuilder.append(requestMethod);
            requestBuilder.append("\nparams : \n {\n");
            if ("GET".equals(requestMethod)) {
                Set<String> paramNames = httpUrl.queryParameterNames();
                if (paramNames != null && !paramNames.isEmpty()) {
                    for (String key : paramNames) { //追加已有参数
                        requestBuilder.append(key);
                        requestBuilder.append(" : ");
                        requestBuilder.append(httpUrl.queryParameter(key));
                        requestBuilder.append("\n");
                    }
                }
            } else if ("POST".equals(requestMethod)) {
                if (requestBody != null) {
                    Charset charset = StandardCharsets.UTF_8;
                    MediaType contentType = requestBody.contentType();
                    if (contentType != null) {
                        charset = contentType.charset(StandardCharsets.UTF_8);
                    }
                    /*requestBuilder.append("\ncontent-Type: ");
                    requestBuilder.append(requestBody.contentType());
                    requestBuilder.append("\n");*/
                    if (requestBody instanceof FormBody) {
                        for (int i = 0; i < ((FormBody) requestBody).size(); i++) {
                            requestBuilder.append(((FormBody) requestBody).name(i));
                            requestBuilder.append(" : ");
                            requestBuilder.append(((FormBody) requestBody).value(i));
//                            requestBuilder.append(URLDecoder.decode(((FormBody) requestBody).value(i),"utf-8"));
                            requestBuilder.append("\n");
                        }
                    } else {
                        Buffer buffer = new Buffer();
                        try {
                            requestBody.writeTo(buffer);
                            String json = buffer.readString(charset);
                            JSONObject jsonObject = new JSONObject(json);
                            Iterator<String> iterator = jsonObject.keys();
                            while (iterator.hasNext()) {
                                String key = iterator.next();
                                String params = jsonObject.optString(key);
                                requestBuilder.append(key);
                                requestBuilder.append(" : ");
                                requestBuilder.append(params);
                                requestBuilder.append("\n");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            buffer.close();
                        }
                    }
                }
            }
            requestBuilder.append("\n}");

            //============response ==================
            long startNs = System.nanoTime();
            response = chain.proceed(request);
            long tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs);
            responseBuilder = new StringBuilder("请求结果 : ");
            responseBuilder.append("\n");
            responseBuilder.append("请求耗时 : ");
            responseBuilder.append(tookMs);
            responseBuilder.append(" ms \n");
            if (response != null) {
                responseBuilder.append("response method : ");
                responseBuilder.append(response.request().method());
                responseBuilder.append("\n");
                responseBuilder.append("response url : ");
                responseBuilder.append(response.request().url());
                responseBuilder.append("\n");
                responseBuilder.append("response code : ");
                responseBuilder.append(response.code());
                responseBuilder.append("\n");
                responseBuilder.append("response message : ");
                responseBuilder.append(response.message());
                responseBuilder.append("\n");

                /*Headers resHeaders = response.headers();
                for (int i = 0, count = resHeaders.size(); i < count; i++) {
                    responseBuilder.append(resHeaders.name(i));
                    responseBuilder.append(" : ");
                    responseBuilder.append(resHeaders.value(i));
                    responseBuilder.append("\n");
                }*/

                if (HttpHeaders.hasBody(response)) {
                    try {
                        ResponseBody responseBody = response.body();
                        BufferedSource source = responseBody.source();
                        source.request(Long.MAX_VALUE); // Buffer the entire body.
                        Buffer buffer = source.buffer();
                        Charset resCharset = StandardCharsets.UTF_8;
                        MediaType resContentType = responseBody.contentType();
                        if (resContentType != null) {
                            resCharset = resContentType.charset(StandardCharsets.UTF_8);
                        }
                        String bodyStr = buffer.clone().readString(resCharset);
                        responseBuilder.append("response result : ");
                        responseBuilder.append("\n");
                        responseBuilder.append(formatJson(bodyStr));
                        responseBuilder.append("\n");
                    } catch (Exception e) {
                        LogUtils.e(TAG, "response exception: " + e.getMessage());
                    }
                }
            }
            //String endTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss SSS").format(new Date());

        } catch (Exception e) {
            LogUtils.e(TAG, "request exception: " + e.getMessage());
        } finally {
            if (requestBuilder != null) {
                LogUtils.d(TAG, requestBuilder.toString());
            }
            if (responseBuilder != null) {
                LogUtils.d(TAG, responseBuilder.toString());
            }
        }
        return response;
        //=================


        //这个chain里面包含了request和response，所以你要什么都可以从这里拿
        /*Request request = chain.request();

        long t1 = System.currentTimeMillis();//请求发起的时间
        try {
            StringBuilder sb = new StringBuilder();
            if (request.body() instanceof FormBody) {
                FormBody body = (FormBody) request.body();
                for (int i = 0; i < body.size(); i++) {
                    sb.append(body.encodedName(i) + "=" + URLDecoder.decode(body.encodedValue(i), "utf-8") + " ;\n ");
                }
                sb.delete(sb.length() - 1, sb.length());
                LogUtils.d("LogInterceptor", "发送请求:" + request.url() + "\n{" + sb.toString() + "}");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Response response = chain.proceed(request);
        long t2 = System.currentTimeMillis();//收到响应的时间

        //这里不能直接使用response.body().string()的方式输出日志
        //因为response.body().string()之后，response中的流会被关闭，程序会报错，我们需要创建出一
        //个新的response给应用层处理
        ResponseBody responseBody = response.peekBody(1024 * 1024);
        String result = responseBody.string();

        try {


            result = result.replaceAll("%(?![0-9a-fA-F]{2})", "%25");
            result = result.replaceAll("\\+", "%2B");
            result = URLDecoder.decode(result, "utf-8");
            LogUtils.d("LogInterceptor", String.format("接收响应: %s毫秒 URL = [%s] \n %s ",
                    (t2 - t1),
                    response.request().url(),
                    result
            ));

//            String s = response.request().url().toString();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;*/
    }

    /**
     * 格式化json
     *
     * @param content
     * @return
     */
    public static String formatJson(String content) {

        StringBuffer sb = new StringBuffer();
        int index = 0;
        int count = 0;
        while (index < content.length()) {
            char ch = content.charAt(index);
            if (ch == '{' || ch == '[') {
                sb.append(ch);
                sb.append('\n');
                count++;
                for (int i = 0; i < count; i++) {
                    sb.append('\t');
                }
            } else if (ch == '}' || ch == ']') {
                sb.append('\n');
                count--;
                for (int i = 0; i < count; i++) {
                    sb.append('\t');
                }
                sb.append(ch);
            } else if (ch == ',') {
                sb.append(ch);
                sb.append('\n');
                for (int i = 0; i < count; i++) {
                    sb.append('\t');
                }
            } else {
                sb.append(ch);
            }
            index++;
        }
        return sb.toString();
    }


 /*   result = result.replaceAll("%(?![0-9a-fA-F]{2})", "%25");
    result = result.replaceAll("\\+", "%2B");
    result = URLDecoder.decode(result, "utf-8");*/
}
