package com.shike.baselibrary;

import android.app.Activity;
import android.app.Application;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by snoopy on 2018/5/30.
 */

public class App extends Application {

    private List<Activity> mList = new LinkedList<Activity>();
    public static App getInstance() {
        return instance;
    }

    private static App instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    // add Activity
    public void addActivity(Activity activity) {
        mList.add(activity);
    }

    public void exit() {
        try {
            for (Activity activity : mList) {
                if (activity != null)
                    activity.finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            System.exit(0);
        }
    }
}
