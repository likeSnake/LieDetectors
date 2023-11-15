package net.instrument.free.utility.liedetectors.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.tencent.mmkv.MMKV;

import net.instrument.free.utility.liedetectors.R;
import net.instrument.free.utility.liedetectors.adapter.CountAdapter;
import net.instrument.free.utility.liedetectors.pojo.Country;

import java.util.ArrayList;

public class SelectAct extends AppCompatActivity implements View.OnClickListener{

    private RecyclerView recyclerView;
    private ArrayList<Country> list = new ArrayList<>();
    private CountAdapter countAdapter;
    private ImageView ic_next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_select);

        initUI();
        initListener();
        initDate();
    }
    public void initUI(){
        recyclerView = findViewById(R.id.recyclerView);
        ic_next = findViewById(R.id.ic_next);
        ic_next.setClickable(false);

    }
    public void initListener(){}
    public void initDate(){
        list.add(new Country(getBitmapFromResource(this,R.mipmap.zh),"Chinese"));
        list.add(new Country(getBitmapFromResource(this,R.mipmap.au),"America"));
        list.add(new Country(getBitmapFromResource(this,R.mipmap.fr),"French"));
        list.add(new Country(getBitmapFromResource(this,R.mipmap.gr),"German"));
        list.add(new Country(getBitmapFromResource(this,R.mipmap.jp),"Japanese"));
        list.add(new Country(getBitmapFromResource(this,R.mipmap.kr),"Korean"));
        list.add(new Country(getBitmapFromResource(this,R.mipmap.sp),"Spanish"));

        start(list);
    }

    public  Bitmap getBitmapFromResource(Context context, int resId) {
        Resources resources = context.getResources();
        Bitmap bitmap = BitmapFactory.decodeResource(resources, resId);
        return bitmap;
    }

    public void start(ArrayList<Country> list){
        LinearLayoutManager manager = new LinearLayoutManager(SelectAct.this, LinearLayoutManager.VERTICAL,false);
        countAdapter = new CountAdapter(this, list, new CountAdapter.ItemOnClickListener() {
            @Override
            public void OnClick() {
                System.out.println("----------");
                ic_next.setImageResource(R.drawable.ic_next);
                ic_next.setClickable(true);
                ic_next.setOnClickListener(SelectAct.this);
            }
        });
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(countAdapter);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ic_next) {
            MMKV.defaultMMKV().encode("gj","America");
            startActivity(new Intent(SelectAct.this, CheckPermissions.class));
            finish();
        }

    }
}