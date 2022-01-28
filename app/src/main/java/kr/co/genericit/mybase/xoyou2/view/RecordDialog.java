package kr.co.genericit.mybase.xoyou2.view;

import static android.content.Context.ACTIVITY_SERVICE;

import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.File;
import java.io.IOException;

import kr.co.genericit.mybase.xoyou2.R;
import kr.co.genericit.mybase.xoyou2.utils.CommandUtil;

public class RecordDialog extends Dialog {
    private Context context;
    private CustomDialogClickListener customDialogClickListener;
    private LinearLayout btn_record;
    private TextView record_title, txt_record_status, txt_record_time, txt_play_status, btn_submit;
    private ImageView btn_close, img_record_status, img_play_status;

    private int mRecordStatus = 0; //0정지, 1녹음중, 2일시정지

    private LinearLayout btn_play;
    private LinearLayout submit_layout;

    //음성녹음 정보
    private String mTitle;
    private String mPath = null;
    private int mIndex = 0;

    //녹음
    public MediaRecorder recorder;
    public boolean isRecording = false;

    //재생
    public MediaPlayer mp;
    private boolean isPlay = false;

    //타이머
    private int sWatch = 0;
    private boolean isWatch = true;
    WatchThread memory;

    public RecordDialog(@NonNull Context context,String title, String path,int index, CustomDialogClickListener customDialogClickListener){
        super(context);
        this.context = context;
        mTitle = title;
        mPath = path;
        mIndex = index;
        this.customDialogClickListener = customDialogClickListener;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_record);


        initView();
    }

    public void initView(){
        btn_submit = findViewById(R.id.btn_submit);

        txt_play_status = findViewById(R.id.txt_play_status);
        img_play_status = findViewById(R.id.img_play_status);

        submit_layout = findViewById(R.id.submit_layout);
        btn_play = findViewById(R.id.btn_play);

        record_title = findViewById(R.id.record_title);
        btn_close = findViewById(R.id.btn_close);
        img_record_status = findViewById(R.id.img_record_status);
        txt_record_status = findViewById(R.id.txt_record_status);
        txt_record_time = findViewById(R.id.txt_record_time);
        btn_record = findViewById(R.id.btn_record);

        record_title.setText(mTitle);

        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mp!=null)
                {
                    mp.stop();
                    mp=null;
                }
                dismiss();
            }
        });
        btn_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mRecordStatus == 0){
                    mRecordStatus = 1;
                    startRecording();
                }else if(mRecordStatus == 1){
                    mRecordStatus = 0;
                    stopRecording();
                }else if(mRecordStatus == 2){

                }

                setStatusChange();
            }
        });

        successRecord();
    }

    public interface CustomDialogClickListener{
        void onPositiveClick(int index, String path);
        void onNegativeClick();
    }

    private void setStatusChange(){
        if(mRecordStatus == 0){
            img_record_status.setBackgroundResource(R.drawable.ic_mic);
            txt_record_status.setText(R.string.record_status0);
            txt_record_time.setVisibility(View.GONE);
        }else{
            img_record_status.setBackgroundResource(R.drawable.ic_record);
            txt_record_status.setText(R.string.record_status1);
            txt_record_time.setVisibility(View.VISIBLE);
        }
    }


    public void startRecording() {
        File audioDir = getContext().getCacheDir(); //  path = /data/user/0/YOUR_PACKAGE_NAME/cache
        File recordingFile = new File(audioDir + "/" + "record_temp_"+mIndex+".m4a");
        Log.d("ifeelbluu", "Created file: " + recordingFile.getAbsolutePath());

        mPath = recordingFile.getAbsolutePath();

        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
        recorder.setOutputFile(recordingFile.getAbsolutePath());

        try {
            recorder.prepare();
        } catch (IOException e) {
            Log.e("ifeelbluu", "prepare() failed");
        }
        recorder.start();
        isRecording = true;
        isWatch = true;
        memory = new WatchThread();
        memory.start();
    }


    class WatchThread extends Thread {
        @Override
        public void run() {
            super.run();
            while (isWatch) {
                try {
                    ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
                    ActivityManager activityManager = (ActivityManager) getContext().getSystemService(ACTIVITY_SERVICE);
                    activityManager.getMemoryInfo(mi);
                    Thread.sleep(1000);
                    sWatch++;
                    int min = sWatch / 60;
                    int sec = sWatch % 60;
                    Message msg = new Message();

                    if(sWatch == 0){
                        isWatch = false;
                        msg.what = 2222;
                    }else{
                        msg.what = 1111;
                    }
                    String tempzero = "";
                    if(sec < 10){
                        tempzero = "0";
                    }
                    msg.obj = String.valueOf(min + ":" + tempzero + sec);
                    mWatchHandler.sendMessage(msg);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(isWatch == false){
                    sWatch = 0;
                }
            }

        }
    }

    Handler mWatchHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == 1111) {
                String mem = (String) msg.obj;
                String CheckMin = mem.split(":")[0];
                if(CheckMin.equals("10")){
                    if(recorder != null) {
                        recorder.stop();
                        recorder.release();
                        recorder = null;

                        Log.i("SampleAudioRecorder", "recorder.stop();");
                        isRecording = false;

                        mRecordStatus = 0;
                        setStatusChange();

                        try {
                            isWatch = false;
                            memory.stop();
                            memory.destroy();
                            memory = null;
                        } catch (Exception e) {
                            // TODO: handle exception
                        }
                    }
                }
                txt_record_time.setText(mem);
            }else{
                String mem = (String) msg.obj;
                txt_record_time.setText(mem);
            }
            return false;
        }
    });

    public void stopRecording(){
        if(recorder != null) {
            recorder.stop();
            recorder.release();
            recorder = null;

            Log.i("SampleAudioRecorder", "recorder.stop();");
            isRecording = false;

            mRecordStatus = 0;
            setStatusChange();

            try {
                isWatch = false;
                memory.stop();
                memory.destroy();
                memory = null;
            } catch (Exception e) {
                // TODO: handle exception
            }


        }
    }

    public void successRecord(){
//        File recordingFile = new File(audioDir + "/" + "test1.m4a");
//        Log.d("ifeelbluu", "Created file: " + recordingFile.getAbsolutePath());

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mPath == null || mPath.equals("")){
                    CommandUtil.getInstance().showCommonOneButtonDefaultDialog(context,CommandUtil.getInstance().getStr(R.string.mong_dialog_record_not_file));
                    return;
                }

                if(isRecording){
                    stopRecording();
                }

                customDialogClickListener.onPositiveClick(mIndex, mPath);
                dismiss();
            }
        });

        btn_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(mPath == null || mPath.equals("")){
                    CommandUtil.getInstance().showCommonOneButtonDefaultDialog(context,CommandUtil.getInstance().getStr(R.string.mong_dialog_record_not_file));
                    return;
                }

                if(isRecording){
                    return;
                }

                if(isPlay == false){
                    isPlay = true;
                    mp = new MediaPlayer();
                    try {
                        mp.setDataSource(mPath);
                        mp.prepare();
                    } catch (Exception e) {
                        // TODO: handle exception
                    }
                    mp.start();
                    setPlayBtnImage();

                    mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mediaPlayer) {
                            isPlay = false;
                            setPlayBtnImage();
                        }
                    });
                }else{
                    if(mp!=null){
                        mp.stop();
                        mp=null;
                    }
                    isPlay = false;
                    setPlayBtnImage();
                }
            }
        });
    }

    @Override
    public void setOnDismissListener(@Nullable OnDismissListener listener) {
        super.setOnDismissListener(listener);
        if(mp!=null)
        {
            mp.stop();
            mp=null;
        }

    }

    private void setPlayBtnImage(){
        if(isPlay){
            img_play_status.setBackgroundResource(R.drawable.icon_top_pause_w);
            txt_play_status.setText(context.getResources().getString(R.string.play_status1));
        }else{
            img_play_status.setBackgroundResource(R.drawable.icon_top_play_w);
            txt_play_status.setText(context.getResources().getString(R.string.play_status0));
        }

    }
}
