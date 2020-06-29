package com.libs.core.business.http.proxy;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.libs.core.business.http.HttpCommonParamsProxy;
import com.libs.core.business.http.HttpURL;
import com.libs.core.common.manager.UserManager;
import com.libs.core.common.utils.LogUtils;

import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;
import retrofit2.http.PUT;

public class CommonInterceptor implements Interceptor {

    private String url;
    private String version;
    private String channel;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private Gson mGson;
    public static final String TAG = "CommonInterceptor";

    public CommonInterceptor(String url, String version, String channel) {
        this.url = url;
        this.version = version;
        this.channel = channel;
        mGson = new Gson();
    }

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request oldRequest = chain.request();
        String method = oldRequest.method();
        try {
            if (!TextUtils.isEmpty(url) && oldRequest.url().toString().startsWith(url)) {
                Map<String, String> commonParams = getCommonParamsMap();

                if ("GET".equals(method)) {
                    HttpUrl mHttpUrl = oldRequest.url();
                    Set<String> paramNames = mHttpUrl.queryParameterNames();
                    for (String key : paramNames) { //追加已有参数
                        String value = mHttpUrl.queryParameter(key);
                        commonParams.put(key, TextUtils.isEmpty(value) ? "" : value);
                    }
                    HttpUrl.Builder newHttpUrlBuilder = mHttpUrl.newBuilder();
                    for (Map.Entry<String, String> entry : commonParams.entrySet()) {
                        newHttpUrlBuilder.setQueryParameter(entry.getKey(), entry.getValue());
                    }
                    Request newRequest = oldRequest.newBuilder()
                            .url(newHttpUrlBuilder.build())
                            .build();
                    return chain.proceed(newRequest);

                } else if ("POST".equals(method)) {
                    RequestBody body = oldRequest.body();
                    if (body != null) {
                        if (body instanceof FormBody) {
                            for (int i = 0; i < ((FormBody) body).size(); i++) {
                                commonParams.put(((FormBody) body).name(i), ((FormBody) body).value(i));
                            }
                            FormBody.Builder formBodyBuilder = new FormBody.Builder();
                            for (Map.Entry<String, String> entry : commonParams.entrySet()) {
                                formBodyBuilder.add(entry.getKey(), entry.getValue());
                            }
                            Request newRequest = oldRequest.newBuilder()
                                    .post(formBodyBuilder.build())
                                    .build();
                            return chain.proceed(newRequest);

                        } else {
                            Buffer buffer = new Buffer();
                            try {
                                body.writeTo(buffer);
                                Charset charset = StandardCharsets.UTF_8;
                                String json = buffer.readString(charset);
                                JSONObject jsonObject = new JSONObject(json);
                                Iterator<String> iterator = jsonObject.keys();
                                while (iterator.hasNext()) {
                                    String key = iterator.next();
                                    String params = jsonObject.optString(key);
                                    commonParams.put(key, params);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            } finally {
                                buffer.close();
                            }
                            Request newRequest = oldRequest.newBuilder()
                                    .post(RequestBody.create(body.contentType(), mGson.toJson(commonParams)))
                                    .build();
                            return chain.proceed(newRequest);
                        }
                    }
                }


           /* // 添加新的参数
            HttpUrl.Builder authorizedUrlBuilder = oldRequest.url()
                    .newBuilder()
                    .scheme(oldRequest.url().scheme())
                    .host(oldRequest.url().host())
                    .addQueryParameter("version", version)
                    .addQueryParameter("channel", channel)
                    .addQueryParameter("ukey", TextUtils.isEmpty(UserManager.getInstance().getToken()) ? "" : UserManager.getInstance().getToken());
            // 新的请求
            Request newRequest = oldRequest.newBuilder()
                    .method(oldRequest.method(), oldRequest.body())
                    .url(authorizedUrlBuilder.build())
                    .build();

            return chain.proceed(newRequest);*/
            }
        } catch (Exception e) {
            LogUtils.e(TAG, "添加公共参数出错 = " + e.getMessage());
        }
        return chain.proceed(chain.request());
    }

    //获取公参
    public Map<String, String> getCommonParamsMap() {
        Map<String, String> params = new HashMap<>();
        params.put("version", HttpCommonParamsProxy.getInstance().getAppVersion());
        params.put("channel", HttpCommonParamsProxy.getInstance().getAppChannel());
        params.put("ukey", TextUtils.isEmpty(UserManager.getInstance().getToken()) ? "" : UserManager.getInstance().getToken());
        params.put("androidid", HttpCommonParamsProxy.getInstance().getAppAndroidId());
        params.put("imei", HttpCommonParamsProxy.getInstance().getAppImei());
        params.put("oaid", HttpCommonParamsProxy.getInstance().getAppOAId());
        return params;
    }

    public boolean isJsonStr(String params) {
        if (!TextUtils.isEmpty(params) && params.indexOf('{') > -1) {
            return true;
        }
        return false;
    }
}
