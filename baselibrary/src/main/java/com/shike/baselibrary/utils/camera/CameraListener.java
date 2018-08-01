package com.shike.baselibrary.utils.camera;

import android.graphics.Bitmap;

/**
 * Created by snoopy on 2018/6/18.
 */

public interface CameraListener {
    void getPicture(Bitmap bitmap, byte[] date);
}
