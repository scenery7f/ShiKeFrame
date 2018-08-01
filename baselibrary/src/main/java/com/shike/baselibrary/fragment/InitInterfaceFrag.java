package com.shike.baselibrary.fragment;

import android.os.Bundle;

/**
 * Created by Administrator on 2017-7-7.
 */

public interface  InitInterfaceFrag {
    // 1、从父类获得数据
    public void initGetDataFromParent(Bundle savedInstanceState);

    // 2、加载布局文件,返回布局文件id
    public int initSetContentView();

    // 3、绑定控件
    public void initBindWidget(Bundle savedInstanceState);

    // 4、给控件绑定事件
    public void initSetListener();

    // 5、初始化数据
    public void initSetData();

}
