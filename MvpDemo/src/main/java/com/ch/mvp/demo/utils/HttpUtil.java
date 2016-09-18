package com.ch.mvp.demo.utils;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class HttpUtil {


    public static String URL = "http://120.24.220.249:13000/api/p2p/";

    // 实例话对象
    private static AsyncHttpClient client = new AsyncHttpClient();

    private HttpUtil() {
        // 设置链接超时，如果不设置，默认为10s
        client.setTimeout(5000);
    }

    private synchronized static AsyncHttpClient getInstance() {
        if (client == null) {
            client = new AsyncHttpClient();
        }
        return client;
    }

    public void getDataByHttp(String type) {

    }

    /**
     * get请求
     *
     * @param urlString
     * @param params
     * @param handler
     */
    public static void sendHttpByGet(String urlString, RequestParams params,
                                     AsyncHttpResponseHandler handler) {
        // url里面带参数
        getInstance().get(urlString, params, handler);
    }

    /**
     * post请求
     *
     * @param urlString
     * @param params
     * @param res
     */
    public static void sendHttpByPost(String urlString, RequestParams params,
                                      AsyncHttpResponseHandler res) {
        // url里面带参数
        getInstance().post(urlString, params, res);
    }


}
