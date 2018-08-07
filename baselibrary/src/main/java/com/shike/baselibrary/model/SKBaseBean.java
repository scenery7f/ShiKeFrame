package com.shike.baselibrary.model;

import android.content.Context;

import com.google.gson.Gson;
import com.shike.baselibrary.okhttp.ResponseEntityToModule;
import com.shike.baselibrary.utils.SharepreferenceUtils;

import java.lang.reflect.Field;

/**
 * Created by snoopy on 2017/11/13.
 */

public class SKBaseBean {

    public void saveJson(Context context) {
        SharepreferenceUtils.saveString(context,getKey(), new Gson().toJson(this));
    }

    public Boolean loadBean(Context context) {

        String data = SharepreferenceUtils.getString(context,getKey(),null);

        if (data != null) {
            copy(ResponseEntityToModule.parseJsonToModule(data,this.getClass()));
            return true;
        }
        return false;
    }

    /**
     * @param context
     * @param key
     */
    public void saveJson(Context context, String key) {
        SharepreferenceUtils.saveString(context,getKey()+key, new Gson().toJson(this));
    }

    /**
     * @param context
     * @param key
     * @return
     */
    public Boolean loadBean(Context context, String key) {
        String data = SharepreferenceUtils.getString(context,getKey()+key,null);

        if (data != null) {
            try {
                copy(ResponseEntityToModule.parseJsonToModule(data,this.getClass()));
                return true;
            } catch (Exception e) {
                e.getMessage();
            }
        }
        return false;
    }

    private String getKey() {
        String key = getClass().getName();

        return key;
    }

    public void copy(Object object) {
        Field[] tFields = this.getClass().getDeclaredFields();
        Field[] oFields = object.getClass().getDeclaredFields();

        for (int i=0;i<tFields.length;i++) {
            Field tF = tFields[i];
            Field oF = oFields[i];
            tF.setAccessible(true);
            oF.setAccessible(true);
            try {

                tF.set(this,oF.get(object));

            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

}
