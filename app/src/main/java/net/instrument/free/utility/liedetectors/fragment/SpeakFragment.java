package net.instrument.free.utility.liedetectors.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import net.instrument.free.utility.liedetectors.R;
import net.instrument.free.utility.liedetectors.ui.ResultAct;

import java.io.File;
import java.io.IOException;

public class SpeakFragment extends Fragment implements View.OnClickListener{
    private Context context;
    private TextView title_text;
    private ImageView speak;
    private boolean isTacking = false;
    private Vibrator vibrator;
    public SpeakFragment(Context context){
        this.context = context;
    }
    private MediaRecorder mediaRecorder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_speak, container, false);
        initView(rootView);
        initDate();
        initListener();
        return rootView;
    }

    public void initView(View rootView){
        speak = rootView.findViewById(R.id.speak);
        title_text = rootView.findViewById(R.id.title_text);
        vibrator = (Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);

    }

    public void initDate(){

    }

    @SuppressLint("ClickableViewAccessibility")
    public void initListener(){
        speak.setOnClickListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.speak){
            if (isTacking){
                isTacking = false;
                speak.setImageResource(R.drawable.ic_yuyin);
                title_text.setText("Tap The Icon To Start Record");
                startDialog();
            }else {
                isTacking = true;
                speak.setImageResource(R.drawable.ic_yuyin_ing);
                title_text.setText("Tap The Icon To Finish Record");
                startRecording();
            }
            vibrator.vibrate(10);
        }

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
                stopRecording();
                context.startActivity(new Intent(context, ResultAct.class));
            }
        });
        dialog.show();
    }


    @Override
    public void onResume() {
        super.onResume();

    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startRecording() {
        String tempFileName = context.getExternalCacheDir().getAbsolutePath() + "/temp_audio.3gp";
        File tempFile = new File(tempFileName);
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setOutputFile(tempFile);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mediaRecorder.prepare();
            mediaRecorder.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void stopRecording() {
        if (mediaRecorder != null) {
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;
        }
    }
}