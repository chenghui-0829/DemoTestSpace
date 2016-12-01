package com.ch.rxjava.util;

import java.util.Map;

import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by CH on 2016/12/1.
 */
public interface HttpRequestUtil {

    /**
     * get请求
     */
    @GET("{url}")
    Observable<String> getRequest(@Path("url") String url, @QueryMap Map<String, String> maps);

    /**
     * post请求
     */
    @POST("{url}")
    Observable<String> postRequest(@Path("url") String url, @QueryMap Map<String, String> maps);

}
