package com.shike.shikeframe.camera;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Environment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shike.baselibrary.activity.BaseActivity;
import com.shike.baselibrary.utils.camera.CameraActivity;
import com.shike.shikeframe.R;

import java.io.ByteArrayOutputStream;
import java.io.File;

/**
 * Created by snoopy on 2018/6/20.
 */

public abstract class UseCameraActivity extends BaseActivity {

    private static final int RESULT_LOAD_IMAGE = 1;
    private static final int TAKE_PICTURE = 0;
    private static final int CUT_PHOTO_REQUEST_CODE = 2;
    public String path = "";
    public Uri photoUri;

    // 照片
    public ImageView my_info_head;

    private Callback callBack;

    public void setCallback(Callback callBack) {
        this.callBack = callBack;
    }

    public void comfireImgSelection(ImageView my_info, int lId) {

        my_info_head = my_info;
        final Dialog dlg = new Dialog(this, R.style.ActionSheet);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout layout = (LinearLayout) inflater.inflate(lId, null);
        final int cFullFillWidth = 10000;
        layout.setMinimumWidth(cFullFillWidth);

        TextView pop_cancel = layout.findViewById(R.id.pop_cancel);
        TextView takephoto = layout.findViewById(R.id.takephoto);

        pop_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dlg.dismiss();

            }
        });
        takephoto.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dlg.dismiss();
                // s startTakePhoto(context);
                photo();
            }
        });
        Window w = dlg.getWindow();
        WindowManager.LayoutParams lp = w.getAttributes();
        lp.x = 0;
        final int cMakeBottom = -1000;
        lp.y = cMakeBottom;
        lp.gravity = Gravity.BOTTOM;
        dlg.onWindowAttributesChanged(lp);
        dlg.setCanceledOnTouchOutside(true);
        dlg.setContentView(layout);
        dlg.show();
    }

    // 拍照
    public void comfireImgSelection(ImageView my_info) {
        comfireImgSelection(my_info, R.layout.camera);
    }

    private void photo() {
        String sdcardState = Environment.getExternalStorageState();
        String sdcardPathDir = FileUtils.SDPATH;
        File file = null;
        if (Environment.MEDIA_MOUNTED.equals(sdcardState)) {
            // 有sd卡，是否有myImage文件夹
            File fileDir = new File(sdcardPathDir);
            if (!fileDir.exists()) {
                fileDir.mkdirs();
            }
            // 是否有headImg文件
            file = new File(sdcardPathDir + System.currentTimeMillis() + ".JPEG");
        }

        if (file != null) {
            path = file.getPath();
            Intent intent = new Intent(this, CameraActivity.class);
            intent.putExtra("url", path);
            startActivityForResult(intent, 100);
        }
    }


    public interface Callback {
        public void success(String path, Bitmap bitmap);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100) {
            if (path != null && resultCode == 1) {
                Bitmap bitmap = BitmapFactory.decodeFile(path);
                if (my_info_head != null) {
                    my_info_head.setImageBitmap(bitmap);
                }
                if (callBack != null) {
                    callBack.success(path, bitmap);
                }
            } else if (resultCode == 1000){
                showToast("图片保存失败！请检查存储权限是否打开");
            } else if (resultCode == 1001) {
                showToast("相机异常，请确定权限后重试！");
            }
        } else {

        }
    }

    /**
     * 图片 压缩 转字节流
     *
     * @param bm
     * @return
     */
    public byte[] Bitmap2Bytes(Bitmap bm) {
        int i = bm.getWidth() * bm.getHeight();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 80, baos);
        return baos.toByteArray();
    }

    /**
     * 图片 不压缩 转字节流
     *
     * @param bm
     * @return
     */
    public byte[] Bitmap2BytesUnCom(Bitmap bm) {
        int i = bm.getWidth() * bm.getHeight();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

}
