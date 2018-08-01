package com.shike.baselibrary.okhttp.response;

import android.os.Handler;
import android.os.Looper;

import com.shike.baselibrary.okhttp.ResponseEntityToModule;
import com.shike.baselibrary.okhttp.exception.OkHttpException;
import com.shike.baselibrary.okhttp.listener.DisposeDataHandle;
import com.shike.baselibrary.okhttp.listener.DisposeDataListener;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


/**
 * @author vision
 * @function 专门处理JSON的回调
 */
public class CommonJsonCallback implements Callback {

    /**
     * the logic layer exception, may alter in different app
     */
    protected final String RESULT_CODE = "resultCode"; // 有返回则对于http请求来说是成功的，但还有可能是业务逻辑上的错误
    protected final int RESULT_CODE_VALUE = 0;
    protected final String EMPTY_MSG = "服务器数据为空！";

    /**
     * the java layer exception, do not same to the logic error
     */
    protected final int NETWORK_ERROR = -1; // the network relative error
    protected final int JSON_ERROR = -2; // the JSON relative error
    protected final int OTHER_ERROR = -3; // the unknow error

    /**
     * 将其它线程的数据转发到UI线程
     */
    private Handler mDeliveryHandler;
    private DisposeDataListener mListener;
    private Class<?> mClass;

    public CommonJsonCallback(DisposeDataHandle handle) {
        this.mListener = handle.mListener;
        this.mClass = handle.mClass;
        this.mDeliveryHandler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void onFailure(final Call call, final IOException ioexception) {
        /**
         * 此时还在非UI线程，因此要转发
         */
        mDeliveryHandler.post(new Runnable() {
            @Override
            public void run() {
                mListener.onFailure(new OkHttpException(NETWORK_ERROR, ioexception));
            }
        });
    }

    @Override
    public void onResponse(final Call call, final Response response) throws IOException {
        final String result = response.body().string();
        mDeliveryHandler.post(new Runnable() {
            @Override
            public void run() {
                handleResponse(result);
            }
        });

    }

//处理服务器返回的数据

    private void handleResponse(Object responseObj) {
          if (responseObj == null || responseObj.toString().trim().equals("")) {
            mListener.onFailure(new OkHttpException(NETWORK_ERROR, EMPTY_MSG));
            return;
        }

        try {
            /**
             * 协议确定后看这里如何修改
             */
            JSONObject result = new JSONObject(responseObj.toString());
            if (result.has(RESULT_CODE)) {
//                从json 对象中取出响应码，若为0则正确
//                if (result.getInt(RESULT_CODE) == RESULT_CODE_VALUE) {
                if (mClass == null) {
                    mListener.onSuccess(result);
                } else {
//                        将我们的json对象转化为实体对象
                    Object obj = ResponseEntityToModule.parseJsonObjectToModule(result, mClass);
                    if (obj != null) {
                        mListener.onSuccess(obj);
                    } else {
                        mListener.onFailure(new OkHttpException(JSON_ERROR, EMPTY_MSG));
                    }
                }
            } else {
                Object obj = ResponseEntityToModule.parseJsonObjectToModule(result, mClass);
                if (obj != null) {
                    mListener.onSuccess(obj);
                } else {
                    mListener.onFailure(new OkHttpException(JSON_ERROR, EMPTY_MSG));
                }
            }
        } catch (Exception e) {
            mListener.onFailure(new OkHttpException(OTHER_ERROR, e.getMessage()));
            e.printStackTrace();
        }
    }
}