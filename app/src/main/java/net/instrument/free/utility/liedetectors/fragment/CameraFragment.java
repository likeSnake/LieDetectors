package net.instrument.free.utility.liedetectors.fragment;

import android.Manifest;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import net.instrument.free.utility.liedetectors.R;
import net.instrument.free.utility.liedetectors.ui.ResultAct;

import java.util.Arrays;

public class CameraFragment extends Fragment implements View.OnClickListener,SurfaceHolder.Callback{
    private Context context;

    private static final String TAG = "CameraPreview";
    private static final int REQUEST_CAMERA_PERMISSION = 1;

    private CameraManager mCameraManager;
    private String mCameraId;
    private CameraDevice mCameraDevice;
    private CameraCaptureSession mCaptureSession;
    private CaptureRequest.Builder mPreviewRequestBuilder;
    private Button start;
    private SurfaceView mPreview;
    private Surface mSurface;
    private ImageView eye_left;
    private ImageView eye_right;
    private Boolean isStart = true;
    private AnimatorSet animatorSet;
    private AnimatorSet animatorSet2;
    private boolean isFirst = true;


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            // Fragment 不可见
            // 在这里执行你需要的操作
            System.out.println("关闭相机");
            closeCamera();
        } else {
            // Fragment 可见
            // 在这里执行你需要的操作
            if (!isFirst) {
                openCamera();
            }
        }
    }

    public CameraFragment(Context context){
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_camera, container, false);
        initView(rootView);
        initDate();
        initListener();
        return rootView;
    }

    public void initView(View rootView){

        mPreview = rootView.findViewById(R.id.camera_preview);
        eye_left = rootView.findViewById(R.id.eye_left);
        eye_right = rootView.findViewById(R.id.eye_right);
        start = rootView.findViewById(R.id.start);
    }

    public void startAnm(){

        // 创建第一个旋转动画（从0度到360度）
        ObjectAnimator rotationAnimator1 = ObjectAnimator.ofFloat(eye_left, "rotation", 0f, 360f);
        rotationAnimator1.setDuration(1000); // 设置动画持续时间（以毫秒为单位）

// 创建第二个旋转动画（从360度到0度）
        ObjectAnimator rotationAnimator2 = ObjectAnimator.ofFloat(eye_left, "rotation", 360f, 0f);
        rotationAnimator2.setDuration(1000); // 设置动画持续时间（以毫秒为单位）

        // 创建第一个旋转动画（从0度到360度）
        ObjectAnimator rotationAnimator3 = ObjectAnimator.ofFloat(eye_right, "rotation", 0f, 360f);
        rotationAnimator3.setDuration(1000); // 设置动画持续时间（以毫秒为单位）

// 创建第二个旋转动画（从360度到0度）
        ObjectAnimator rotationAnimator4 = ObjectAnimator.ofFloat(eye_right, "rotation", 360f, 0f);
        rotationAnimator4.setDuration(1000); // 设置动画持续时间（以毫秒为单位）

// 创建AnimatorSet并组合两个旋转动画
         animatorSet = new AnimatorSet();
         animatorSet2 = new AnimatorSet();

        animatorSet.playSequentially(rotationAnimator1, rotationAnimator2);
        animatorSet2.playSequentially(rotationAnimator3, rotationAnimator4);

// 设置循环模式为重复
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (!isStart) {
                    animatorSet.start(); // 在动画结束时重新启动动画
                }
            }
        });

        animatorSet2.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (!isStart) {
                    animatorSet2.start(); // 在动画结束时重新启动动画
                }
            }
        });

// 开始动画
        animatorSet.start();
        animatorSet2.start();
    }
    public void initDate(){

        // 初始化相机预览视图
        mPreview.getHolder().addCallback(this);

        // 初始化 CameraManager
        mCameraManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
        try {
            // 获取可用的相机列表
            String[] cameraIds = mCameraManager.getCameraIdList();
            if (cameraIds.length > 0) {
                // 选择第一个相机
                mCameraId = cameraIds[0];
            }
        } catch (CameraAccessException e) {
            Log.e(TAG, "Failed to access camera: " + e);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    public void initListener(){
        start.setOnClickListener(this);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.start){
            if (isStart) {
                isStart = false;
                start.setText("Finish");
                startAnm();
            }else {
                isStart = true;
                start.setText("Start");
                animatorSet.cancel();
                animatorSet2.cancel();
                startTanChuang();
            }
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        if (isFirst) {
            openCamera();
            isFirst = false;
        }
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
       // openCamera();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
        // 设置预览的 Surface
        mSurface = holder.getSurface();
     //   startPreview();

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
        closeCamera();
    }

    private void openCamera() {
        try {
            // 请求相机权限
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            // 打开相机
            mCameraManager.openCamera(mCameraId, mCameraStateCallback, null);
        } catch (CameraAccessException e) {
            Log.e(TAG, "Failed to open camera: " + e);
        }
    }

    private void closeCamera() {
        if (mCaptureSession != null) {
            mCaptureSession.close();
            mCaptureSession = null;
        }
        if (mCameraDevice != null) {
            mCameraDevice.close();
            mCameraDevice = null;
        }
    }

    private void startPreview() {
        try {
            mPreviewRequestBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            mPreviewRequestBuilder.addTarget(mSurface);

            mCameraDevice.createCaptureSession(Arrays.asList(mSurface), mCaptureSessionStateCallback, null);
        } catch (CameraAccessException e) {
            Log.e(TAG, "Failed to start preview: " + e);
        }
    }

    private final CameraDevice.StateCallback mCameraStateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(@NonNull CameraDevice camera) {
            mCameraDevice = camera;
            startPreview();
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice camera) {
            closeCamera();
        }

        @Override
        public void onError(@NonNull CameraDevice camera, int error){
            closeCamera();
        }
    };

    private final CameraCaptureSession.StateCallback mCaptureSessionStateCallback = new CameraCaptureSession.StateCallback() {
        @Override
        public void onConfigured(@NonNull CameraCaptureSession session) {
            mCaptureSession = session;
            try {
                mCaptureSession.setRepeatingRequest(mPreviewRequestBuilder.build(), null, null);
            } catch (CameraAccessException e) {
                Log.e(TAG, "Failed to start camera capture session: " + e);
            }
        }

        @Override
        public void onConfigureFailed(@NonNull CameraCaptureSession session) {
            Log.e(TAG, "Camera capture session configuration failed");
        }
    };


    public void startTanChuang() {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog);
        dialog.getWindow().setBackgroundDrawableResource(R.color.my_colors);
        dialog.setCancelable(false);
        dialog.findViewById(R.id.dialog_close_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                context.startActivity(new Intent(context, ResultAct.class));
                isFirst = true;
            }
        });
        dialog.show();
    }





}