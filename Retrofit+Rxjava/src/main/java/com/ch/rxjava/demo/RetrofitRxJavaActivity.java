package com.ch.rxjava.demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * http://www.2cto.com/kf/201605/510999.html
 */
public class RetrofitRxJavaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrofit_rx_java);

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://www.shwdztc.com/api/p2p/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(client)
                .build();

        Map<String, String> params = new HashMap<>();
        params.put("tokenId", "ce301015-303b-432c-9fbe-ff12167d01c1");

        ApiService service = retrofit.create(ApiService.class);
        service.queryMap("GetRecentnewRecord", params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {


                    }

                    @Override
                    public void onError(Throwable e) {

                        Log.e("----erro----", e.getMessage());

                    }

                    @Override
                    public void onNext(String s) {

                        Log.e("-----success---", s);

                    }
                });

    }


    private interface ApiService {


        /**
         * 如果直接多参数 @QueryMap
         */
        @GET("{url}")
        Observable<String> queryMap(@Path("url") String url, @QueryMap Map<String, String> maps);


    }


    private class LoggingInterceptor implements Interceptor {
        @Override
        public okhttp3.Response intercept(Chain chain) throws IOException {

            Request request = chain.request();

            Log.e("requestUrl==========>", "intercept:" + request.url() + "---->" + chain.connection() + "---->" + request.headers());


            okhttp3.Response response = chain.proceed(request);


            System.out.println("------info-------->" + response.request().url() + "------>" + response.headers());
            return response;
        }
    }

}
