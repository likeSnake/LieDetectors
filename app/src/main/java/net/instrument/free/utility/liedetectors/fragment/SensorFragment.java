package net.instrument.free.utility.liedetectors.fragment;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import net.instrument.free.utility.liedetectors.R;
import net.instrument.free.utility.liedetectors.ui.ResultAct;
import net.instrument.free.utility.liedetectors.ui.SelectAct;
import net.instrument.free.utility.liedetectors.ui.StartAct;

public class SensorFragment extends Fragment implements View.OnClickListener{
    private Context context;
    private RelativeLayout zhiwen_layout;
    private Vibrator vibrator;
    private ImageView image_yuan;
    private ImageView ic_scan;
    private ImageView bt_zhiwen;
    private ObjectAnimator animator;
    private CountDownTimer countDownTimer;
    private TextView title_text;
    private AnimatorSet animatorSet;
    private boolean isReading = false;
    private ValueAnimator colorAnimator;

    public SensorFragment(Context context){
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sensor, container, false);
        initView(rootView);
        initTimer();
        initDate();
        initListener();
        return rootView;
    }

    public void initView(View rootView){
        zhiwen_layout = rootView.findViewById(R.id.zhiwen_layout);
        image_yuan = rootView.findViewById(R.id.image_yuan);
        ic_scan = rootView.findViewById(R.id.ic_scan);
        bt_zhiwen = rootView.findViewById(R.id.bt_zhiwen);
        title_text = rootView.findViewById(R.id.title_text);

    }

    public void initDate(){
         vibrator = (Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);

    }


    @SuppressLint("ClickableViewAccessibility")
    public void initListener(){

        // 手势操作处理
        bt_zhiwen.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        System.out.println("---*----开始长按");
                        image_yuan.setVisibility(View.VISIBLE);
                        regulateIndex2();
                        vibrator.vibrate(10000);
                        title_text.setText("Please Hold For More Than 3 Seconds");
                        countDownTimer.start();
                        break;
                    case MotionEvent.ACTION_UP:
                        System.out.println("--放开长按");
                        // 放开处理
                        colorAnimator.cancel();
                        image_yuan.setVisibility(View.GONE);
                        vibrator.cancel();
                        animatorSet.cancel();
                        countDownTimer.cancel();
                        ic_scan.setVisibility(View.GONE);
                        if (isReading){
                            isReading = false;
                            startDialog();
                        }
                        break;
                }
                return true;
            }
        });
    }

    public void startDialog() {
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

            }
        });
        dialog.show();
    }

    public void initTimer(){
         countDownTimer = new CountDownTimer(3000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                title_text.setText("In The Analysis");
                isReading = true;
            }
        };
    }

    public void regulateIndex2() {

        // 创建属性动画
        ObjectAnimator moveUp = ObjectAnimator.ofFloat(ic_scan, "translationY", 0, -bt_zhiwen.getHeight());
        moveUp.setDuration(1500); // 设置动画持续时间为1秒

        ObjectAnimator moveDown = ObjectAnimator.ofFloat(ic_scan, "translationY", -bt_zhiwen.getHeight(), 0);
        moveDown.setDuration(1500); // 设置动画持续时间为1秒

        // 创建动画集合
         animatorSet = new AnimatorSet();
        animatorSet.playSequentially(moveUp, moveDown); // 依次执行上移和下移动画
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                // 动画开始时的回调
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                // 动画结束时的回调，可以在此处再次启动动画以实现循环效果
                animatorSet.start();
            }

            @Override
            public void onAnimationCancel(Animator animator) {
                // 动画被取消时的回调
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
                // 动画重复时的回调
            }
        });

        // 启动动画
        animatorSet.start();

        startYanSe();

     //   ic_scan.startAnimation(animationSet);
        ic_scan.setVisibility(View.VISIBLE);
    }

    public void startYanSe(){
        // 定义颜色变化的起始颜色和结束颜色
        int startColor = Color.YELLOW;
        int endColor = Color.DKGRAY;

        // 创建颜色过渡的动画对象
         colorAnimator = ValueAnimator.ofObject(new ColorEvaluator(), startColor, endColor);
        colorAnimator.setDuration(1500); // 设置动画时长为3秒
        colorAnimator.setRepeatCount(ValueAnimator.INFINITE); // 设置重复次数为无限
        colorAnimator.setRepeatMode(ValueAnimator.REVERSE); // 设置重复模式为反向

        // 添加动画更新监听器，将过渡的颜色应用到ImageView
        colorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                int animatedValue = (int) animator.getAnimatedValue();
                image_yuan.setColorFilter(new PorterDuffColorFilter(animatedValue, PorterDuff.Mode.SRC_IN));
                image_yuan.invalidate();
            }
        });

        // 启动动画
        colorAnimator.start();
    }

    public void startAnimator(){
        // 创建动画对象，沿Y轴平移
         animator = ObjectAnimator.ofFloat(
                ic_scan, "translationY", 0f, -bt_zhiwen.getHeight());

        // 设置动画属性
        animator.setDuration(1500);  // 动画持续时间，单位毫秒
        animator.setRepeatMode(ObjectAnimator.REVERSE);  // 反向重复
        animator.setRepeatCount(ObjectAnimator.INFINITE);  // 无限重复

        // 启动动画
        animator.start();

        ic_scan.setVisibility(View.VISIBLE);

        // 定义颜色变化的起始颜色和结束颜色
        int startColor = Color.YELLOW;
        int endColor = Color.DKGRAY;

        // 创建颜色过渡的动画对象
        ValueAnimator colorAnimator = ValueAnimator.ofObject(new ColorEvaluator(), startColor, endColor);
        colorAnimator.setDuration(1500); // 设置动画时长为3秒
        colorAnimator.setRepeatCount(ValueAnimator.INFINITE); // 设置重复次数为无限
        colorAnimator.setRepeatMode(ValueAnimator.REVERSE); // 设置重复模式为反向

        // 添加动画更新监听器，将过渡的颜色应用到ImageView
        colorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                int animatedValue = (int) animator.getAnimatedValue();
                image_yuan.setColorFilter(new PorterDuffColorFilter(animatedValue, PorterDuff.Mode.SRC_IN));
                image_yuan.invalidate();
            }
        });

        // 启动动画
        colorAnimator.start();
    }

    // 自定义颜色过渡的评估器
    private static class ColorEvaluator implements TypeEvaluator<Integer> {
        @Override
        public Integer evaluate(float fraction, Integer startValue, Integer endValue) {
            int startColor = startValue;
            int endColor = endValue;

            // 提取起始颜色的RGB分量
            int startAlpha = Color.alpha(startColor);
            int startRed = Color.red(startColor);
            int startGreen = Color.green(startColor);
            int startBlue = Color.blue(startColor);

            // 提取结束颜色的RGB分量
            int endAlpha = Color.alpha(endColor);
            int endRed = Color.red(endColor);
            int endGreen = Color.green(endColor);
            int endBlue = Color.blue(endColor);

            // 计算过渡颜色的RGB分量
            int currentAlpha = (int) (startAlpha + fraction * (endAlpha - startAlpha));
            int currentRed = (int) (startRed + fraction * (endRed - startRed));
            int currentGreen = (int) (startGreen + fraction * (endGreen - startGreen));
            int currentBlue = (int) (startBlue + fraction * (endBlue - startBlue));

            // 组合过渡颜色的ARGB值
            return Color.argb(currentAlpha, currentRed, currentGreen, currentBlue);
        }
    }


    @Override
    public void onClick(View v) {

    }

}