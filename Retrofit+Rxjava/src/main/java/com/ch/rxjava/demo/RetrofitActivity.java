package com.ch.rxjava.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * 相关文章：http://www.jianshu.com/p/16994e49e2f6
 */
public class RetrofitActivity extends AppCompatActivity {

    private String url = "http://120.24.220.249:13000/api/p2p/GetRecentnewRecord?tokenId=ce301015-303b-432c-9fbe-ff12167d01c1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrofit);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new LoggingInterceptor())
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://www.shwdztc.com/api/p2p/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .client(client)
                .build();

        Map<String, String> params = new HashMap<>();
        params.put("tokenId", "ce301015-303b-432c-9fbe-ff12167d01c1");
        ApiService apiService = retrofit.create(ApiService.class);
        Call<String> call = apiService.queryMap("GetRecentnewRecord", params);
//        Call<String> call = apiService.getData("ce301015-303b-432c-9fbe-ff12167d01c1");
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                System.out.println("-success-->" + response.body().toString());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                System.out.println("--failure--->" + t.toString());
            }
        });
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


    private interface ApiService {

        /**
         * 这里主要用注解@get @post 设置请求方式，后面“GetRecentnewRecord”是方法Url
         */
        @GET("GetRecentnewRecord")
        Call<String> getData(@Query("tokenId") String id);

        /**
         * 如果想用表单 @FieldMap
         */
        @FormUrlEncoded
        @POST("/url")
        Call<ResponseBody> postForm(@FieldMap Map<String, Object> maps);

        /**
         * 如果直接用对象 @Body
         */
        @POST("url")
        Call<ResponseBody> PostBody(@Body Objects objects);

        /**
         * 如果直接多参数 @QueryMap
         */
        @GET("{url}")
        Call<String> queryMap(@Path("url") String url, @QueryMap Map<String, String> maps);

        /**
         * 如果上传文件 @Part
         */
        @Multipart
        @POST("/url")
        Call<ResponseBody> uploadFlie(@Part("description") RequestBody description, @Part("files") MultipartBody.Part file);

        /**
         * 如果多文件上传 @PartMap()
         */
        @Multipart
        @POST("{url}")
        Call<ResponseBody> uploadFiles(@Path("url") String url, @PartMap() Map<String, RequestBody> maps);
    }
}
