package com.ch.rxjava.demo;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.ch.rxjava.base.ActivityLifeCycleEvent;
import com.ch.rxjava.base.BaseActivity;
import com.ch.rxjava.util.*;
import com.ch.rxjava.util.HttpRequestUtil;
import com.ch.rxjava.util.ProgressSubscriber;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * http://www.2cto.com/kf/201605/510999.html
 */
public class RetrofitRxJavaActivity extends BaseActivity {


    private Context context = RetrofitRxJavaActivity.this;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrofit_rx_java);
        sendHttpRequest();
    }

    private void sendHttpRequest() {

        Map<String, String> params = new HashMap<>();
        params.put("tokenId", "ce301015-303b-432c-9fbe-ff12167d01c1");

        HttpClientUtil.getInstance(context).sendHttpRequset("get", "GetRecentnewRecord", params, new ProgressSubscriber(this) {
            @Override
            protected void _onNext(Object o) {
                System.out.println("请求成功======>" + o.toString());
            }

            @Override
            protected void _onError(String message) {
                System.out.println("失败======>" + message);
            }
        }, ActivityLifeCycleEvent.DESTROY, lifecycleSubject);

    }


}
