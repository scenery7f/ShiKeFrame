package com.shike.baselibrary.utils.camera;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.shike.baselibrary.R;
import com.shike.baselibrary.utils.dialog.CustomProgressDialog;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class CameraActivity extends AppCompatActivity implements View.OnClickListener {

    public static int SCREEN_WIDTH, SCREEN_HEIGHT;

    private Camera mCamera;
    private SurfaceView surfaceView;

    private ImageView flashAction;
    private ImageView cameraAction;
    private ImageView changeCameraAction;
    private ImageView focus_img;
    private ImageView camera_pic;

    private CustomProgressDialog dialogg;

    private RelativeLayout bottom_paizhao,bottom_chuli;

    private float mStartX = 0;
    private float mStartY = 0;


    /**
     * 图片相关
     */
    private String imgUrl;
    private byte[] imageBytes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        if (getIntent().hasExtra("url"))
            imgUrl = getIntent().getStringExtra("url");
        else {
            Log.e(getClass().getName(),"请指定图片存储位置！");
            finish();
        }

        screenSize();

        initView();
        initListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            mCamera.openCamera();
        } catch (Exception e) {
            e.printStackTrace();
            setResult(1001); // 相机异常
            finish();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            mCamera.cancle();
        } catch (Exception e) {
            e.printStackTrace();
            setResult(1001); // 相机异常
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initView() {
        dialogg = new CustomProgressDialog(this);

        surfaceView = findViewById(R.id.show_camera);

        mCamera = new Camera(this);
        mCamera.setmHolder(surfaceView.getHolder());

        flashAction = findViewById(R.id.flash_btn);
        cameraAction = findViewById(R.id.camera_btn);
        changeCameraAction = findViewById(R.id.change_camera);
        focus_img = findViewById(R.id.focus_img);
        camera_pic = findViewById(R.id.camera_pic);

        bottom_paizhao = findViewById(R.id.bottom_paizhao);
        bottom_chuli = findViewById(R.id.bottom_chuli);
    }

    private void initListener() {

        flashAction.setOnClickListener(this);
        cameraAction.setOnClickListener(this);
        changeCameraAction.setOnClickListener(this);

        mCamera.setCallback(new MyAutoFocusCallback());
        mCamera.setListener(new CameraListener() {
            @Override
            public void getPicture(Bitmap bitmap, byte[] date) {
                focus_img.setVisibility(View.VISIBLE);

                imageBytes = date;
                createFileWithByte(date,imgUrl);

                offFlash();

                dialogg.cancel();
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (flashAction == view) { // 控制闪光灯
            if (flashAction.getTag().toString().equals("off")) {
                onFlash();
            } else {
                offFlash();
            }
        } else if (cameraAction == view) { // 拍照
            mCamera.getPicture(camera_pic);
            startAnimationCamera();

            dialogg.show();

            bottom_paizhao.setVisibility(View.GONE);
            bottom_chuli.setVisibility(View.VISIBLE);
            flashAction.setVisibility(View.GONE);

        } else if (changeCameraAction == view) { // 切换摄像头
            offFlash();
            mCamera.cameraChange();
        }
    }

    /**
     * 关闭闪光灯
     */
    private void offFlash() {
        if (mCamera.setFlashModel(false)) {
            flashAction.setTag("off");
            flashAction.setImageResource(R.drawable.ic_action_flash_off);
        }
    }

    /**
     * 打开闪光灯
     */
    private void onFlash() {
        if (mCamera.setFlashModel(true)) {
            flashAction.setTag("on");
            flashAction.setImageResource(R.drawable.ic_action_flash_on);
        }
    }

    /**
     * 重新拍照
     *
     * @param view
     */
    public void anew(View view) {
        try {
            mCamera.openCamera();
        } catch (Exception e) {
            e.printStackTrace();
            setResult(1001); // 相机异常
            finish();
        }
        camera_pic.setVisibility(View.GONE);
        bottom_paizhao.setVisibility(View.VISIBLE);
        bottom_chuli.setVisibility(View.GONE);
        flashAction.setVisibility(View.VISIBLE);
    }

    /**
     * 使用图片
     *
     * @param view
     */
    public void save(View view) {

        File file = new File(imgUrl);

        if (file.exists()) {//createFileWithByte(imageBytes,imgUrl)
            setResult(1); // 保存图片成功返回
        } else {
            setResult(1000); // 保存图片失败返回
        }
        finish();
    }

    /**
     * 触摸监听
     *
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (MotionEvent.ACTION_DOWN == event.getAction()) {
            if (mCamera.mCamera != null) {
                mCamera.onFocus(event.getX(), event.getY());
                changeLocal(event);
                mStartX = event.getRawX();
                mStartY = event.getRawY();
            }
        }

        return super.onTouchEvent(event);
    }

    /**
     * 对焦监听
     */
    private class MyAutoFocusCallback implements android.hardware.Camera.AutoFocusCallback {

        @Override
        public void onAutoFocus(boolean b, android.hardware.Camera camera) {
            if (b) {
                mCamera.setFocusModel();
            } else {

            }
        }

    }

    private void changeLocal(MotionEvent event) {

        focus_img.setImageResource(R.drawable.ic_action_focus_start);

        int baseInt = dip2px(20);
        focus_img.layout((int) event.getRawX() - baseInt, (int) event.getRawY() - baseInt, (int) event.getRawX() + baseInt, (int) event.getRawY() + baseInt);

        startAnimationFocus();

    }

    /**
     * 保存图片
     *
     * @param bytes
     * @param fileName
     * @return
     */
    private boolean createFileWithByte(byte[] bytes, String fileName) {
        boolean save = true;
        /**
         * 创建File对象，其中包含文件所在的目录以及文件的命名
         */
        File file = new File(fileName);

        // 判断是否有路径
        File fParent = file.getParentFile();
        if (!fParent.exists()) {
            fParent.mkdirs();
        }

        // 创建FileOutputStream对象
        FileOutputStream outputStream = null;
        // 创建BufferedOutputStream对象
        BufferedOutputStream bufferedOutputStream = null;
        try {
            // 如果文件存在则删除
            if (file.exists()) {
                file.delete();
            }
            // 在文件系统中根据路径创建一个新的空文件
            file.createNewFile();
            // 获取FileOutputStream对象
            outputStream = new FileOutputStream(file);
            // 获取BufferedOutputStream对象
            bufferedOutputStream = new BufferedOutputStream(outputStream);
            // 往文件所在的缓冲输出流中写byte数据
            bufferedOutputStream.write(bytes);
            // 刷出缓冲输出流，该步很关键，要是不执行flush()方法，那么文件的内容是空的。
            bufferedOutputStream.flush();
        } catch (Exception e) {
            // 打印异常信息
            e.printStackTrace();

            save = false;
        } finally {
            // 关闭创建的流对象
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bufferedOutputStream != null) {
                try {
                    bufferedOutputStream.close();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        }

        return save;
    }


    /**
     * 返回按钮
     *
     * @param view
     */
    public void onBackAction(View view) {
        setResult(0);
        finish();
    }

    /**
     * 拍照按钮动画
     */
    private void startAnimationCamera() {
        CustomRotateAnim rotateAnim = CustomRotateAnim.getCustomRotateAnim();
        // 一次动画执行1秒
        rotateAnim.setDuration(100);
        // 设置为循环播放
        rotateAnim.setRepeatCount(0);
        // 设置为匀速
        rotateAnim.setInterpolator(new AccelerateInterpolator());

        rotateAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                cameraAction.setImageResource(R.drawable.ic_action_camera_pas);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                cameraAction.setImageResource(R.drawable.ic_action_camera);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        cameraAction.startAnimation(rotateAnim);//开始动画
    }

    /**
     * 对焦动画
     */
    private void startAnimationFocus() {

        Animation animation = new ScaleAnimation(2.0f, 1.0f, 2.0f, 1.0f, Animation.RELATIVE_TO_SELF,0.5f, Animation.RELATIVE_TO_SELF,0.5f);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                focus_img.setImageResource(R.drawable.ic_action_focus_end);
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            sleep(1000);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    focus_img.layout(0, 0, 0, 0);
                                }
                            });
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
        animation.setDuration(500);//动画时间
        animation.setRepeatCount(0);//动画的重复次数
        focus_img.clearAnimation();
        focus_img.startAnimation(animation);//开始动画

    }

    /**
     * 获取屏幕尺寸
     */
    private void screenSize() {
        Resources resources = this.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        float density = dm.density;
        SCREEN_WIDTH = dm.widthPixels;
        SCREEN_HEIGHT = dm.heightPixels;
    }

    private float density = -1F;

    public int dip2px(float pxValue) {
        return (int) (pxValue * getDensity() + 0.5F);
    }

    public float getDensity() {
        if (density <= 0F) {
            density = getResources().getDisplayMetrics().density;
        }
        return density;
    }
}
