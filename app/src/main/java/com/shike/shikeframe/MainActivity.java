package com.shike.shikeframe;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.shike.baselibrary.okhttp.listener.DisposeDataListener;
import com.shike.baselibrary.utils.TestUtil;
import com.shike.shikeframe.http.RequestCenter;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        RequestCenter.login("13363307219", "123456", new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) throws UnsupportedEncodingException, ParseException {
                TestUtil.log("aa"+responseObj);
            }

            @Override
            public void onFailure(Object reasonObj) {
                TestUtil.log("aa"+reasonObj);
            }
        });
    }
}
