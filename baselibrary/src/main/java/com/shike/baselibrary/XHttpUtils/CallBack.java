package com.shike.baselibrary.XHttpUtils;


import com.shike.baselibrary.utils.ResponseEntityToModule.ResponseEntityToModule;
import com.shike.baselibrary.utils.TestUtil;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;

/**
 * Created by duanyucong on 2017/6/5.
 */

public class CallBack implements Callback.CommonCallback<JSONObject> {

    private CommonCallback commonCallback;
    private Class beanClass;

    public CallBack(CommonCallback commonCallback,Class beanClass) {
        this.commonCallback = commonCallback;
        this.beanClass = beanClass;
    }

    @Override
    public void onSuccess(JSONObject result) {

        TestUtil.log("返回JSON："+result);

        commonCallback.onFinish();

        try {
            if (result.getInt("code") == 1) {

                commonCallback.onSuccess(ResponseEntityToModule.parseJsonObjectToModule(result,beanClass));

            } else {
                commonCallback.onError(result.getString("msg"),result.getInt("code"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            commonCallback.onError("网络异常-JSON格式错误",-100);
        }
    }

    @Override
    public void onError(Throwable ex, boolean isOnCallback) {
        commonCallback.onFinish();
        commonCallback.onError("网络访问失败！",-99);
    }

    @Override
    public void onCancelled(CancelledException cex) {
        commonCallback.onFinish();
    }

    @Override
    public void onFinished() {
        commonCallback.onFinish();
    }

    public interface CommonCallback {
        public void onSuccess(Object result) throws JSONException;
        public void onError(String msg, int code);
        public void onFinish();
    }

}
