package net.instrument.free.utility.liedetectors.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import net.instrument.free.utility.liedetectors.R;

import java.util.Random;

public class ResultAct extends AppCompatActivity {
    private TextView title_text;
    private ImageView ic_lie;
    private ImageView ic_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_result);
        initUI();
        initData();
        initListener();
    }
    public void initUI(){
        title_text = findViewById(R.id.title_text);
        ic_lie = findViewById(R.id.ic_lie);
        ic_back = findViewById(R.id.ic_back);
    };
    public void initListener(){
        ic_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    };
    public void initData(){
        boolean b = new Random().nextBoolean();
        if (b){
            title_text.setText("You Lie");
            title_text.setTextColor(android.graphics.Color.RED);
            ic_lie.setImageResource(R.drawable.tt);
        }else {
            title_text.setText("You Tell THe Truth");
            title_text.setTextColor(Color.GREEN);
            ic_lie.setImageResource(R.drawable.tk);
        }
    };
}