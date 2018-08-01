package com.shike.baselibrary.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Administrator on 2017/8/9.
 */

public class TestUtil {

    public static boolean showLog = true;

    public static void showToast(Context context, String str) {
        Toast.makeText(context,str, Toast.LENGTH_SHORT).show();
    }

    public static void log(Object str) {
        if (showLog)
            Log.v("TAG","test-log:"+str);
    }

    public static void log(Class c, String str) {
        if (showLog)
            Log.v(c.getName(),"test-log:"+str);
    }

}
