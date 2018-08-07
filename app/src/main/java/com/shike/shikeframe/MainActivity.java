package com.shike.shikeframe;

import com.shike.baselibrary.activity.BaseActivity;
import com.shike.baselibrary.utils.xListView.XListView;
import com.shike.shikeframe.adapter.Adapter;

public class MainActivity extends BaseActivity {

    private XListView xListView;

    @Override
    public int getContentView() {
        return R.layout.activity_main;
    }

    @Override
    public void initBindWidget() {
        showToast("");

        xListView = getView(R.id.listview);
        xListView.setPullLoadEnable(true);
        xListView.setPullRefreshEnable(true);

        xListView.setAdapter(new Adapter(this));
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {
    }
}
