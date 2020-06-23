package com.libs.core.business.http.proxy;

import com.libs.core.business.http.vo.HttpResultVo;
import com.libs.core.common.utils.LogUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


@SuppressWarnings("unchecked")
public class RetrofitProxy<T> implements InvocationHandler {

    private static final String TAG = RetrofitProxy.class.getSimpleName();

    private Class clazz;

    private String baseUrl;


    /**
     * 绑定委托对象并返回一个代理类
     */
    public <T> T create(Class<T> clazz, String baseUrl) {
        this.clazz = clazz;
        this.baseUrl = baseUrl;
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[]{clazz}, this);
    }

    @Override
    public Observable<?> invoke(Object proxy, Method method, final Object[] args) {
        LogUtils.d(TAG, "retrofit proxy start");
        Observable<?> observable = null;
        try {
            LogUtils.d(TAG, "target:[" + clazz.getName() + "],method:[" + method.getName() + "],params:" + (args != null ? Arrays.toString(args) : ""));
            observable = ((Observable<?>) method
                    .invoke(ApiRetrofit.getInstance().getRetrofit(baseUrl)
                            .create((Class<T>) clazz), args))
                    .compose(mObservableTransformer);
        } catch (Exception e) {
            e.printStackTrace();
        }
        LogUtils.d(TAG, "retrofit proxy end");
        return observable;
    }


    private ObservableTransformer mObservableTransformer = new ObservableTransformer<T, T>() {
        @Override
        public ObservableSource<T> apply(Observable<T> upstream) {
            return upstream
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnError(new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            LogUtils.d(TAG, "error:" + throwable.toString());
                        }
                    })
                    .flatMap(new Function<T, Observable<? extends T>>() {
                        @Override
                        public Observable<? extends T> apply(T httpResult) throws Exception {
                            if (httpResult != null && httpResult instanceof HttpResultVo) {
                                HttpResultVo ret = (HttpResultVo) httpResult;
                                LogUtils.d(TAG, "code:" + ret.getCode() + " ,message:" + ret.getMessage());
                                LogUtils.d(TAG, "code:" + httpResult.toString());
                            } else
                                LogUtils.d(TAG, httpResult != null ? httpResult.toString() : "no ret");
                            return Observable.just(httpResult);
                        }
                    });
        }
    };


//    private class ApiTransformer implements Observable.Transformer<T, T> {
//
//
//
//        @Override
//        public Observable<T> call(Observable<T> webGsonResultObservable) {
//            return webGsonResultObservable.subscribeOn(Schedulers.io())
//                    .observeOn(isCallbackMainThread ? AndroidSchedulers.mainThread() : Schedulers.io())
//                    .doOnError(new Action1<Throwable>() {
//                        @Override
//                        public void call(Throwable throwable) {
//                            try {
//                                if (!NetUtils.isNetworkConnected(ApplicationProxy.getApp(), false)) {
//                                    LogUtils.e(TAG, "未连接到网络！", throwable);
//                                }
//                            } catch (Exception e) {
//                                LogUtils.e(TAG, e.getMessage() + "", e);
//                            }
//                            if (throwable instanceof SocketTimeoutException) {
//                                LogUtils.e(TAG, "连接到网络，但请求超时！", throwable);
//                            } else
//                                LogUtils.e(TAG, throwable.getMessage() + "", throwable);
//                        }
//                    })
//                    .flatMap(new Func1<T, Observable<? extends T>>() {
//                        @Override
//                        public Observable<? extends T> call(final T webGsonResult) {
//                            // 可以在此处对返回结果进行统一拦截处理
//                            if (webGsonResult != null && webGsonResult instanceof WebGsonResult) {
//                                WebGsonResult ret = (WebGsonResult) webGsonResult;
//                                LogUtils.d(TAG, "code:" + ret.getCode() + " ,message:" + ret.getMessage());
//                                LogUtils.d(TAG, "code:" + webGsonResult.toString());
//                            } else
//                                LogUtils.d(TAG, webGsonResult != null ? webGsonResult.toString() : "no ret");
//                            return Observable.just(webGsonResult);
//                        }
//                    });
//        }
//    }
}
