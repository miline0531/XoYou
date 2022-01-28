package kr.co.genericit.mybase.xoyou2.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;

import kr.co.genericit.mybase.xoyou2.R;

public class MongRegistDialog extends Dialog {
    private Context context;
    private CustomDialogClickListener customDialogClickListener;
    private TextView btnNegative, btnPositive;

    public MongRegistDialog(@NonNull Context context, CustomDialogClickListener customDialogClickListener){
        super(context);
        this.context = context;
        this.customDialogClickListener = customDialogClickListener;


    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_mymong_regist_dialog_2);

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
        btnNegative = findViewById(R.id.btn_negative);
        btnPositive = findViewById(R.id.btn_positive);
    }


    public interface CustomDialogClickListener{
        void onPositiveClick();
        void onNegativeClick();
    }
}
