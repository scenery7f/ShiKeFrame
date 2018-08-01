package com.shike.baselibrary.utils.camera;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.util.Log;
import android.view.SurfaceHolder;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by snoopy on 2018/6/14.
 */

public class Camera implements SurfaceHolder.Callback ,SensorEventListener {//, android.hardware.Camera.PreviewCallback

    protected android.hardware.Camera mCamera;
    private Context mContext;
    private boolean hasCamera;

    private SurfaceHolder mHolder;

    private CameraListener listener;
    public void setListener(CameraListener listener) {
        this.listener = listener;
    }

    private int orientation = 0;// 0 竖屏，1横屏，2倒屏，3反向横屏

    // 获取感应管理工具
    private SensorManager mSManager;
    private Sensor sensor;

    public void setmHolder(SurfaceHolder mHolder) {
        this.mHolder = mHolder;
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mHolder.addCallback(this);
    }

    private android.hardware.Camera.AutoFocusCallback callback;

    public void setCallback(android.hardware.Camera.AutoFocusCallback callback) {
        this.callback = callback;
    }

    public int cameraId = -100;

    public Camera(Context mContext) {
        this.mContext = mContext;
        mSManager = (SensorManager) mContext.getSystemService(mContext.SENSOR_SERVICE);
        sensor = mSManager.getDefaultSensor
                (Sensor.TYPE_ACCELEROMETER);
    }

    /**
     * 打开摄像头
     */
    public void openCamera() throws Exception {
        if (cameraId == -100)
            cameraId = android.hardware.Camera.CameraInfo.CAMERA_FACING_BACK;
        mCamera = android.hardware.Camera.open(cameraId);
        showInSurfaceView();

        //注册listener，第三个参数是检测的精确度
        mSManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_GAME);
    }

    /**
     * 关闭摄像头
     */
    public void cancle() throws Exception {
        if (mCamera == null)
            return;
        mCamera.setPreviewCallback(null);
        mCamera.stopPreview();
        mCamera.release();
        mCamera = null;

        mSManager.unregisterListener(this);
    }

    /**
     * 切换摄像头
     */
    public void cameraChange() {
        if (cameraId == android.hardware.Camera.CameraInfo.CAMERA_FACING_FRONT) {
            cameraId = android.hardware.Camera.CameraInfo.CAMERA_FACING_BACK;
        } else {
            cameraId = android.hardware.Camera.CameraInfo.CAMERA_FACING_FRONT;
        }
        mCamera.setPreviewCallback(null);
        mCamera.stopPreview();
        mCamera.release();
        mCamera = null;

        mCamera = android.hardware.Camera.open(cameraId);
        showInSurfaceView();
    }

    /**
     * 修正显示图像
     *
     * @param context
     * @param camera
     */
    public void followScreenOrientation(Context context, android.hardware.Camera camera) {
        final int orientation = context.getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            camera.setDisplayOrientation(0);
        } else if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            camera.setDisplayOrientation(90);
        }
    }

    /**
     * 设置自动对焦
     */
    public void setFocusModel() {
        android.hardware.Camera.Parameters params = mCamera.getParameters();
        List<String> focusModes = params.getSupportedFocusModes();
        if (focusModes.contains(android.hardware.Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
            params.setFocusMode(android.hardware.Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        }

        try {
            mCamera.setParameters(params);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 触摸对焦
     *
     * @param x
     * @param y
     */
    public void onFocus(float x, float y) {

        try {
            if (mCamera == null)
                return;

            android.hardware.Camera.Parameters params = mCamera.getParameters();

            mCamera.cancelAutoFocus();
            List<android.hardware.Camera.Area> areas = new ArrayList<>();
            List<android.hardware.Camera.Area> areasMetrix = new ArrayList<>();
            android.hardware.Camera.Size previewSize = params.getPreviewSize();
            Rect focusRect = calculateTapArea(x, y, 1.0f, previewSize);
            Rect metrixRect = calculateTapArea(x, y, 1.5f, previewSize);
            areas.add(new android.hardware.Camera.Area(focusRect, 1000));
            areasMetrix.add(new android.hardware.Camera.Area(metrixRect, 1000));
            params.setMeteringAreas(areasMetrix);
            params.setFocusMode(android.hardware.Camera.Parameters.FOCUS_MODE_AUTO);
            params.setFocusAreas(areas);
            try {
                mCamera.setParameters(params);
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
            mCamera.autoFocus(callback);
        } catch (Exception e) {

        }

    }

    /**
     * 预览、成像等固定参数
     */
    private void setParams() {

        List<android.hardware.Camera.Size> sizes = new ArrayList<>();

        android.hardware.Camera.Parameters params = mCamera.getParameters();

        android.hardware.Camera.Size maxSize = null;
        // 设置最好的预览尺寸
        List<android.hardware.Camera.Size> previewSizes = params.getSupportedPreviewSizes();
        for (android.hardware.Camera.Size size : previewSizes) {
            float a = (float) size.height / (float) size.width;
            if (a == 0.75) {
                sizes.add(size);
            }
        }

        setPreviewSize(sizes);
    }

    private void setPreviewSize(List<android.hardware.Camera.Size> sizes) {

        if (sizes.size() > 0) {
            if (sizes.get(0).height < sizes.get(sizes.size() - 1).height) {
                Collections.reverse(sizes);
            }
        }

        for (android.hardware.Camera.Size size : sizes) {
            try {
                android.hardware.Camera.Parameters params = mCamera.getParameters();
                // 设置预览尺寸
                params.setPreviewSize(size.width, size.height);
                // 设置成像尺寸
                params.setPictureSize(size.width, size.height);

                mCamera.setParameters(params);

                return;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 设置闪光灯
     */
    public boolean setFlashModel(boolean on) {

        if (cameraId == android.hardware.Camera.CameraInfo.CAMERA_FACING_FRONT) {
            return false;
        }

        android.hardware.Camera.Parameters params = mCamera.getParameters();
        if (on) {
            if (params.getFlashMode() != android.hardware.Camera.Parameters.FLASH_MODE_TORCH)
                params.setFlashMode(android.hardware.Camera.Parameters.FLASH_MODE_TORCH);//开启
        } else {
            if (params.getFlashMode() != android.hardware.Camera.Parameters.FLASH_MODE_OFF)
                params.setFlashMode(android.hardware.Camera.Parameters.FLASH_MODE_OFF);//关闭
        }

        try {
            mCamera.setParameters(params);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    /**
     * 显示摄像头实时图像到SurfaceView
     */
    public void showInSurfaceView() {

        if (!surfaceIsCreated) {
            return;
        }

        if (mHolder.getSurface() == null)
            return;

        if (mCamera != null) {
            mCamera.stopPreview();

            followScreenOrientation(mContext, mCamera);

            setFocusModel();// 自动对焦
            setParams();// 设置图像参数

            try {
                mCamera.setPreviewDisplay(mHolder);
            } catch (IOException e) {
                e.printStackTrace();
            }
            mCamera.startPreview();

            if (callback != null)
                mCamera.autoFocus(callback);

        }
    }


    /**
     * 拍照
     *
     * @param imageView
     */
    public void getPicture(final ImageView imageView) {
        mCamera.takePicture(null, null,
                new android.hardware.Camera.PictureCallback() {
                    @Override
                    public void onPictureTaken(byte[] date, android.hardware.Camera camera) {

                        Bitmap bm = BitmapFactory.decodeByteArray(date, 0, date.length);

                        int angle = 0;
                        if (cameraId == android.hardware.Camera.CameraInfo.CAMERA_FACING_BACK) {// 后置摄像头，旋转角度
                            switch (orientation) {
                                case 0:
                                    angle = 90;
                                    break;
                                case 2:
                                    angle = -90;
                                    break;
                                case 3:
                                    angle = 180;
                                    break;
                            }
                        } else { // 前置摄像头旋转角度
                            switch (orientation) {
                                case 0:
                                    angle = -90;
                                    break;
                                case 3:
                                    angle = 180;
                                    break;
                            }
                        }

                        bm = rotaingImageView(angle,bm);

                        date = Bitmap2BytesUnCom(bm);

                        imageView.setImageBitmap(bm);

                        if (listener != null) {
                            listener.getPicture(bm, date);
                        }
                        try {
                            cancle();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }


    /**
     * 获取触摸位置
     *
     * @param x
     * @param y
     * @param coefficient
     * @param previewSize
     * @return
     */
    private Rect calculateTapArea(float x, float y, float coefficient, android.hardware.Camera.Size previewSize) {
        float focusAreaSize = 300;
        int areaSize = Float.valueOf(focusAreaSize * coefficient).intValue();
        int centerY = 0;
        int centerX = 0;
        centerY = (int) (x / CameraActivity.SCREEN_WIDTH * 2000 - 1000);
        centerX = (int) (y / CameraActivity.SCREEN_HEIGHT * 2000 - 1000);
        int left = clamp(centerX - areaSize / 2, -1000, 1000);
        int top = clamp(centerY - areaSize / 2, -1000, 1000);

        RectF rectF = new RectF(left, top, left + areaSize, top + areaSize);
        return new Rect(Math.round(rectF.left), Math.round(rectF.top), Math.round(rectF.right), Math.round(rectF.bottom));
    }

    private static int clamp(int x, int min, int max) {
        if (x > max) {
            return max;
        }
        if (x < min) {
            return min;
        }
        return x;
    }

    /**
     * 判断前置摄像头
     *
     * @return
     */
    public boolean hasFrontCamera() {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.GINGERBREAD && android.hardware.Camera.getNumberOfCameras() > 0) {
            PackageManager pm = mContext.getPackageManager();
            if (pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断后置摄像头
     *
     * @return
     */
    public boolean hasBackCamera() {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.GINGERBREAD && android.hardware.Camera.getNumberOfCameras() > 0) {
            PackageManager pm = mContext.getPackageManager();
            if (pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)) {
                return true;
            }
        }
        return false;
    }

    private boolean surfaceIsCreated = false;

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        surfaceIsCreated = true;
        showInSurfaceView();
        Log.d(getClass().getName(), "surfaceCreated");
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        Log.d(getClass().getName(), "surfaceChanged");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        surfaceIsCreated = false;
        Log.d(getClass().getName(), "surfaceDestroyed");
    }

    /**
     * 感应监听
     */
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        float x = sensorEvent.values[SensorManager.DATA_X];
        float y = sensorEvent.values[SensorManager.DATA_Y];
//        float z = sensorEvent.values[SensorManager.DATA_Z];
//        Logger.d("x="+(int)x+","+"y="+(int)y+","+"z="+(int)z);
        if (x > 5) {// 横屏 正常
            orientation = 1;
        } else if (x < -5){
            orientation = 3;
        } else if (y < 0){
            orientation = 2;
        } else {
            orientation = 0;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

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

    /**
     * 旋转图片
     *
     * @param angle
     * @param bitmap
     * @return Bitmap
     */
    public Bitmap rotaingImageView(int angle, Bitmap bitmap) {
        //旋转图片 动作
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        System.out.println("angle2=" + angle);
        // 创建新的图片
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return resizedBitmap;
    }

}
