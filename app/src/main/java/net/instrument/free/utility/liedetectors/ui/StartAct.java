package net.instrument.free.utility.liedetectors.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;

import com.tencent.mmkv.MMKV;

import net.instrument.free.utility.liedetectors.R;

public class StartAct extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_start);
        MMKV.initialize(this);

        CountDownTimer countDownTimer = new CountDownTimer(3000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                if (MMKV.defaultMMKV().decodeString("gj")==null) {
                    startActivity(new Intent(StartAct.this, SelectAct.class));
                    finish();
                }else if(MMKV.defaultMMKV().decodeString("qx")==null) {
                    startActivity(new Intent(StartAct.this, CheckPermissions.class));
                    finish();
                }else {
                    startActivity(new Intent(StartAct.this,MainAct.class));
                    finish();
                }
            }
        }.start();
    }
}