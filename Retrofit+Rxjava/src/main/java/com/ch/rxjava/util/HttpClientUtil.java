package com.ch.rxjava.util;


import android.util.Log;

import com.ch.rxjava.base.ActivityLifeCycleEvent;
import com.ch.rxjava.base.BaseActivity;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;

public class HttpClientUtil {


    private HttpRequestUtil request;
    private String baseUrl = "http://www.shwdztc.com/api/p2p/";

    /**
     * 在访问HttpMethods时创建单例
     */
    private static class SingletonHolder {
        private static final HttpClientUtil INSTANCE = new HttpClientUtil();
    }

    /**
     * 获取单例
     */
    public static HttpClientUtil getInstance() {
        return SingletonHolder.INSTANCE;
    }

    /**
     * 构造方法私有
     */
    private HttpClientUtil() {

//       HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
//       interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new LoggingInterceptor())
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();

        request = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(client)
                .build().create(HttpRequestUtil.class);

    }


    private class LoggingInterceptor implements Interceptor {
        @Override
        public okhttp3.Response intercept(Chain chain) throws IOException {

            Request request = chain.request();
            Log.e("requestUrl==========>", "请求的url: " + request.url() + "---->" + chain.connection() + "----请求头--->" + request.headers());
            okhttp3.Response response = chain.proceed(request);
            Log.e("responseUrl==========>", "响应的url: " + response.request().url() + "---响应头--->" + response.headers());
            return response;
        }
    }


//

    /**
     * 添加线程管理并订阅
     *
     * @param subscriber
     * @param event            Activity 生命周期
     * @param lifecycleSubject
     */
    public void sendHttpRequset(String requestType, String url, Map<String, String> params, final ProgressSubscriber subscriber, final ActivityLifeCycleEvent event, final PublishSubject<ActivityLifeCycleEvent> lifecycleSubject) {
        //数据预处理
        Observable.Transformer<Object, Object> result = BaseActivity.bindUntilEvent(event, lifecycleSubject);

        Observable<String> observable = null;
        if (requestType.equals("get")) {
            observable = request.getRequest(url, params);
        } else {
            observable = request.postRequest(url, params);
        }

        observable.compose(result).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        //显示Dialog和一些其他操作
                        subscriber.showProgressDialog();
                    }
                })
                //保证doOnSubscribe是在主线程执行
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }


}
