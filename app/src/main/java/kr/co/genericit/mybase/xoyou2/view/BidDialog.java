package kr.co.genericit.mybase.xoyou2.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import kr.co.genericit.mybase.xoyou2.R;
import kr.co.genericit.mybase.xoyou2.utils.CommandUtil;

public class BidDialog extends Dialog {
    private Context context;
    private CustomDialogClickListener customDialogClickListener;
    private TextView btnNegative, btnPositive;
    private EditText pwEditText;

    public BidDialog(@NonNull Context context, CustomDialogClickListener customDialogClickListener){
        super(context);
        this.context = context;
        this.customDialogClickListener = customDialogClickListener;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_mymong_bid_dialog);

        initView();


        btnPositive.setOnClickListener(v -> {
            // 저장버튼 클릭
            String bid = pwEditText.getText().toString();
            if(bid.equals("")){
                Toast.makeText(context, CommandUtil.getInstance().getStr(R.string.mong_dialog_input_price),Toast.LENGTH_SHORT).show();
            }else{
                this.customDialogClickListener.onPositiveClick(bid);
                dismiss();
            }
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
        pwEditText = findViewById(R.id.pw_edittext);
    }


    public interface CustomDialogClickListener{
        void onPositiveClick(String pw);
        void onNegativeClick();
    }
}
