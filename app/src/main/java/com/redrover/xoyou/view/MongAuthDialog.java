package com.redrover.xoyou.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.redrover.xoyou.R;

public class MongAuthDialog extends Dialog {
    private Context context;
    private CustomDialogClickListener customDialogClickListener;
    private TextView btnNegative, btnPositive;
    private String user_name;

    public MongAuthDialog(@NonNull Context context,String userName, CustomDialogClickListener customDialogClickListener){
        super(context);
        this.context = context;
        this.user_name = userName;
        this.customDialogClickListener = customDialogClickListener;


    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_mymong_regist_dialog_1);

        initView();


        btnPositive.setOnClickListener(v -> {
            // 저장버튼 클릭
            this.customDialogClickListener.onPositiveClick();
            dismiss();
        });
        btnNegative.setOnClickListener(v -> {
            // 취소버튼 클릭
            this.customDialogClickListener.onNegativeClick();
            dismiss();
        });
    }

    public void initView(){
        btnNegative = findViewById(R.id.left_button_text_view);
        btnPositive = findViewById(R.id.right_button_text_view);

        TextView txtComment = (TextView)findViewById(R.id.comment_text_view);
        String comment = txtComment.getText().toString();
        txtComment.setText(comment.replace("_userName", user_name));

    }


    public interface CustomDialogClickListener{
        void onPositiveClick();
        void onNegativeClick();
    }
}
