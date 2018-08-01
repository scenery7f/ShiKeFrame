package com.shike.baselibrary.fragment;


import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Administrator on 2017-7-6.
 */

public abstract class BaseFragment extends Fragment implements InitInterfaceFrag {

    public View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            /**
             * 1、加载布局文件
             */
            initGetDataFromParent(savedInstanceState);
            /**
             * 2、加载布局文件,返回布局文件id
             */
            int layoutId = initSetContentView();
            rootView = inflater.inflate(layoutId, container, false);
            /**
             * 3、绑定控件
             */
            initBindWidget(savedInstanceState);
            /**
             * 4、给控件绑定事件
             */
            initSetListener();
            /**
             * 5、初始化数据
             */
            initSetData();
        } else {
            // 缓存的rootView需要判断是否已经被加过parent，
            // 如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
            }
        }

        return rootView;
    }


    /**
     * 申请指定的权限.
     */
    public void requestPermission(int code, String... permissions) {

        ActivityCompat.requestPermissions(getActivity(), permissions, code);
    }

    /**
     * 判断是否有指定的权限
     */
    public boolean hasPermission(String... permissions) {

        for (String permisson : permissions) {
            if (ContextCompat.checkSelfPermission(getActivity(), permisson)
                    != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * 向fragment中发送消息
     *
     * @param msgType
     * @param object
     * @return
     */
    public Object hasMessage(int msgType, Object... object) {
        return getClass().getName();
    }


    /**
     * 绑定控件
     * @param id
     * @param <E>
     * @return
     */
    public final <E extends View> E getView(int id) {
        try {

            return (E) rootView.findViewById(id);

        } catch (Exception e) {

            e.printStackTrace();

            return null;

        }
    }

}
