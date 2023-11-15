package net.instrument.free.utility.liedetectors.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import net.instrument.free.utility.liedetectors.R;
import net.instrument.free.utility.liedetectors.fragment.CameraFragment;
import net.instrument.free.utility.liedetectors.fragment.SensorFragment;
import net.instrument.free.utility.liedetectors.fragment.SpeakFragment;

public class MainAct extends AppCompatActivity {

    private FragmentManager mFragmentManager;
    private Fragment[] fragments;
    private int lastFragment;
    private BottomNavigationView mNavigationView;

    private SensorFragment sensorFragment;
    private SpeakFragment speakFragment;
    private CameraFragment cameraFragment;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);
        initUI();
        initFragment();
        initListener();

    }

    public void initUI(){
        mNavigationView = findViewById(R.id.main_navigation_bar);
    }

    private void initFragment() {
        sensorFragment = new SensorFragment(this);
        speakFragment = new SpeakFragment(this);
        cameraFragment = new CameraFragment(this);
        fragments = new Fragment[]{sensorFragment, cameraFragment, speakFragment};
        mFragmentManager = getSupportFragmentManager();
        //默认显示HomeFragment
        mFragmentManager.beginTransaction()
                .replace(R.id.main_page_controller, sensorFragment)
                .show(sensorFragment)
                .commit();
    }

    private void initListener() {
        mNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.Fingerprint){
                    if (lastFragment != 0) {
                        MainAct.this.switchFragment(lastFragment, 0);
                        lastFragment = 0;
                    }
                    return true;
                }
                if (item.getItemId() == R.id.Eye){
                    if (lastFragment != 1) {
                        MainAct.this.switchFragment(lastFragment, 1);
                        lastFragment = 1;
                    }
                    return true;
                }
                if (item.getItemId() == R.id.Voice){
                    if (lastFragment != 2) {
                        MainAct.this.switchFragment(lastFragment, 2);
                        lastFragment = 2;
                    }
                    return true;
                }

                return false;
            }
        });
    }

    private void switchFragment(int lastFragment, int index) {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.hide(fragments[lastFragment]);
        if (!fragments[index].isAdded()){
            transaction.add(R.id.main_page_controller,fragments[index]);
        }
        transaction.show(fragments[index]).commitAllowingStateLoss();
    }

    @Override
    protected void onResume() {
        super.onResume();
       // sensorFragment.updateData();
    }
}