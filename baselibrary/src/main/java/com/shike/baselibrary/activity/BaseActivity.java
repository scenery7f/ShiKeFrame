package com.shike.baselibrary.activity;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.shike.baselibrary.R;

/**
 * Created by snoopy on 2017/12/10.
 */

public abstract class BaseActivity extends AppCompatActivity implements InitListener{

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());

        initBindWidget();

        initListener();

        initData();
    }

    /**
     * 设置顶部导航栏
     * @param title
     * @param dhaue
     * @return
     */
    public Toolbar setToolBar(String title, boolean dhaue) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setTitle(title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(dhaue);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        return toolbar;
    }


    /**
     * 设置导航栏 及 侧边抽屉
     *
     * @param title
     * @param drawer
     */
    public void setTopAndDrawer(String title,int drawer) {

        Toolbar toolbar = setToolBar(title,false);

        // 设置抽屉
        mDrawerLayout = (DrawerLayout) findViewById(drawer);
        // 實作 drawer toggle 並放入 toolbar
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        mDrawerToggle.syncState();

        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }


    /**
     * 向Activity中发送消息
     *
     * @param msgType
     * @param objects
     * @return
     */
    public Object hasMessage(int msgType,Object... objects) {
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

            return (E) findViewById(id);

        } catch (Exception e) {

            e.printStackTrace();

            return null;

        }
    }

    protected void showToast(String toast) {
        Toast.makeText(this, toast, Toast.LENGTH_SHORT).show();
    }

}
