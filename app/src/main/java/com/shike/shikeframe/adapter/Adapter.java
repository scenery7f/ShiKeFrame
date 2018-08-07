package com.shike.shikeframe.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.shike.shikeframe.R;

/**
 * Created by snoopy on 2018/8/7.
 */

public class Adapter extends BaseAdapter {

    private Context mContext;

    public Adapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return 100;
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.list_item,null);
        }

        return view;
    }
}
