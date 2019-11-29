package com.shike.shikeframe.http;

import com.shike.baselibrary.model.SKBaseBean;
import com.shike.baselibrary.okhttp.CommonOkHttpClient;
import com.shike.baselibrary.okhttp.listener.DisposeDataHandle;
import com.shike.baselibrary.okhttp.listener.DisposeDataListener;
import com.shike.baselibrary.okhttp.request.CommonRequest;
import com.shike.baselibrary.okhttp.request.RequestParams;
import com.shike.shikeframe.web.JavaBean;

/**
 * Created by snoopy on 2018/8/1.
 */

public class RequestCenter {

    /**
     * 根据参数生成body访问方式
     *
     * @param url
     * @param params
     * @param listener
     * @param clazz
     */
    public static void postRequest(String url, RequestParams params, DisposeDataListener listener, Class<?> clazz) {
        CommonOkHttpClient.post(CommonRequest.
                createPostRequest(url, params), new DisposeDataHandle(listener, clazz));
    }

    //根据参数发送所有get请求
    public static void getRequest(String url, RequestParams params, DisposeDataListener listener, Class<?> clazz) {
        CommonOkHttpClient.post(CommonRequest.
                createGetRequest(url, params), new DisposeDataHandle(listener, clazz));
    }

    public static void login(String name, String psd, DisposeDataListener listener) {
        RequestParams params = new RequestParams();
        params.put("name", name);
        params.put("psd", psd);

        postRequest(HttpConstants.LOGIN, params, listener, JavaBean.class);
    }

    public static void getGradeList(String schoolCode, String schoolId, DisposeDataListener listener) {
        RequestParams params = new RequestParams();
        params.put("schoolcode", schoolCode);
        params.put("schoolid", schoolId);

        RequestCenter.postRequest(HttpConstants.GETGRADELIST, params, listener, SKBaseBean.class);
    }
}
