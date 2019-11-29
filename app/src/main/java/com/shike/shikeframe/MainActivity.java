package com.shike.shikeframe;

import com.shike.baselibrary.activity.BaseActivity;
import com.shike.baselibrary.okhttp.listener.DisposeDataListener;
import com.shike.baselibrary.utils.xListView.XListView;
import com.shike.shikeframe.adapter.Adapter;
import com.shike.shikeframe.http.RequestCenter;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;

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

        RequestCenter.getGradeList("50", "003", new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) throws UnsupportedEncodingException, ParseException {

            }

            @Override
            public void onFailure(Object reasonObj) {

            }
        });

    }
}
