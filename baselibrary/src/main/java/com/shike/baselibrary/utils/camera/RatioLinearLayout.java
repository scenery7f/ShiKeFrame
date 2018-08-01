package com.shike.baselibrary.utils.camera;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.shike.baselibrary.R;

/**
 * Created by Administrator on 2017/8/25.
 */

public class RatioLinearLayout extends LinearLayout {

    private float ratiol;
    private boolean forHeight = false;

    public RatioLinearLayout(Context context) {
        super(context);
    }

    public RatioLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.RatioLinearLayout);
        ratiol = array.getFloat(R.styleable.RatioLinearLayout_ratio,0);
        forHeight = array.getBoolean(R.styleable.RatioLinearLayout_forHeight,false);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        if (forHeight) {
            int heightMode = MeasureSpec.getMode(heightMeasureSpec);
            // 宽大小
            int heightSize = MeasureSpec.getSize(heightMeasureSpec);
            // 高大小
            int widthSize;
            // 只有宽的值是精确的才对高做精确的比例校对
            if (heightMode == MeasureSpec.EXACTLY && ratiol > 0) {
                widthSize = (int) (heightSize * ratiol + 0.5f);
                widthMeasureSpec = MeasureSpec.makeMeasureSpec(widthSize,
                        MeasureSpec.EXACTLY);
            }

            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        } else {
            // 宽模式
            int widthMode = MeasureSpec.getMode(widthMeasureSpec);
            // 宽大小
            int widthSize = MeasureSpec.getSize(widthMeasureSpec);
            // 高大小
            int heightSize;
            // 只有宽的值是精确的才对高做精确的比例校对
            if (widthMode == MeasureSpec.EXACTLY && ratiol > 0) {
                heightSize = (int) (widthSize / ratiol + 0.5f);
                heightMeasureSpec = MeasureSpec.makeMeasureSpec(heightSize,
                        MeasureSpec.EXACTLY);
            }

            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }

    }
}
