package net.instrument.free.utility.liedetectors.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.tencent.mmkv.MMKV;

import net.instrument.free.utility.liedetectors.R;

import java.util.ArrayList;
import java.util.List;

public class CheckPermissions extends AppCompatActivity implements View.OnClickListener{

    private ImageView bt_camera;
    private ImageView bt_record;
    private ImageView ic_next;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_permissions);

        initUI();
        initListener();
        initDate();
    }
    public void initUI(){
        bt_camera = findViewById(R.id.bt_camera);
        bt_record = findViewById(R.id.bt_record);
        ic_next = findViewById(R.id.ic_next);
        ic_next.setClickable(false);
        ic_next.setOnClickListener(this);
    }
    public void initListener(){}
    public void initDate(){
        request_permissions();
    }

    private void request_permissions() {
        // 创建一个权限列表，把需要使用而没用授权的的权限存放在这里
        List<String> permissionList = new ArrayList<>();

        // 判断权限是否已经授予，没有就把该权限添加到列表中

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.CAMERA);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.RECORD_AUDIO);
        }
        // 如果列表为空，就是全部权限都获取了，不用再次获取了。不为空就去申请权限
        if (!permissionList.isEmpty()) {
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);

            ActivityCompat.requestPermissions(this, permissions, 1002);
        } else {
            //所有权限都以获取
            bt_record.setImageResource(R.drawable.open);
            bt_camera.setImageResource(R.drawable.open);
            ic_next.setImageResource(R.drawable.ic_next);
            ic_next.setClickable(true);
        }
    }

    // 请求权限回调方法
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1002:
                // 1002请求码对应的是申请多个权限
                if (grantResults.length > 0) {
                    List<String> list = new ArrayList<>();
                    // 因为是多个权限，所以需要一个循环获取每个权限的获取情况
                    for (int i = 0; i < grantResults.length; i++) {
                        // PERMISSION_DENIED 这个值代表是没有授权，我们可以把被拒绝授权的权限显示出来
                        if (grantResults[i] == PackageManager.PERMISSION_DENIED){
                            String permission = permissions[i];
                            System.out.println("permission:"+permission);
                            list.add(permission);
                            // Toast.makeText(Start.this, permissions[i] + "权限被拒绝了,请手动打开权限", Toast.LENGTH_SHORT).show();
                            // getAppDetailSettingIntent(Start.this);
                        }
                    }
                    if (list.isEmpty()){
                        //权限都已获取
                        bt_record.setImageResource(R.drawable.open);
                        bt_camera.setImageResource(R.drawable.open);
                        ic_next.setImageResource(R.drawable.ic_next);
                        ic_next.setClickable(true);


                    }else if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)&&!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECORD_AUDIO)){
                        System.out.println("用户选择始终拒绝不再弹出");
                        Toast.makeText(CheckPermissions.this,"Be sure to grant location permissions, otherwise it won't work!",Toast.LENGTH_LONG).show();
                        getAppDetailSettingIntent(CheckPermissions.this);
                    }else{
                        //Toast.makeText(StartActivity.this,"Please give permission to locate",Toast.LENGTH_SHORT).show();

                        AlertDialog alertDialog = new AlertDialog.Builder(this)
                                .setTitle("Permission settings")
                                .setMessage("Be sure to grant location permissions, otherwise it won't work!")
                                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        request_permissions();
                                    }
                                }).create();
                        alertDialog.show();
                    }
                }
        }
    }


    /**
     * 跳转到权限设置界面
     */
    private void getAppDetailSettingIntent(Context context){
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
        intent.setData(Uri.fromParts("package", getPackageName(), null));
        startActivity(intent);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ic_next) {
            MMKV.defaultMMKV().encode("qx","yes");
            startActivity(new Intent(CheckPermissions.this,MainAct.class));
            finish();
        }

    }
}