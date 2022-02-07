package com.redrover.xoyou.activity;

import android.os.Bundle;
import android.util.Log;

import com.redrover.xoyou.R;
import com.redrover.xoyou.activity.main.HomeLeftFragment;
import com.redrover.xoyou.common.CommonActivity;
import com.redrover.xoyou.common.Constants;
import com.redrover.xoyou.databinding.ActivityMongStoreSaveNewBinding;
import com.redrover.xoyou.network.action.ActionRuler;
import com.redrover.xoyou.network.interfaces.ActionResultListener;
import com.redrover.xoyou.network.request.ActionRequestMongStoreSave;
import com.redrover.xoyou.network.response.DefaultResult;
import com.redrover.xoyou.storage.JWSharePreference;
import com.redrover.xoyou.utils.CommandUtil;

public class MongStoreSaveActivity extends CommonActivity {
    private JWSharePreference sharePreference = new JWSharePreference();

    private ActivityMongStoreSaveNewBinding binding;
    private boolean check = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        binding = ActivityMongStoreSaveBinding.inflate(getLayoutInflater());
        binding = ActivityMongStoreSaveNewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        super.onCreate(savedInstanceState);
        mMongSeq = getIntent().getStringExtra("MongSeq");
        Constants.mongSEQ = mMongSeq;
        initView();
    }

    private void initView() {
//        binding.btnMymongSubmit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mongStoreSave();
//            }
//        });
        binding.btnAnalyze.setOnClickListener(v -> {
            if (binding.chkAgree.isChecked()) {
                mongStoreSave();
            }else{
                Log.e("MongStoreSaveActivity","체크 해주세요.!");
            }

        });
        binding.btnExit.setOnClickListener(v -> {
            finish();
        });

        binding.chkAgree.setOnCheckedChangeListener((buttonView, isChecked) -> {
            this.check = isChecked;
        });


        String userName = sharePreference.getString(JWSharePreference.PREFERENCE_LOGIN_NAME, "");
        String agree = CommandUtil.getInstance().getStr(R.string.mong_store_save_agree);
        agree = agree.replaceAll("_userName",userName);
        binding.txtAgree.setText(agree);
    }

    private String mMongSeq = "";
    private void mongStoreSave(){
        ActionRuler.getInstance().addAction(new ActionRequestMongStoreSave(this, mMongSeq, new ActionResultListener<DefaultResult>() {
            @Override
            public void onResponseResult(DefaultResult response) {
                try {
                    DefaultResult result = response;
                    Log.v("ifeelbluu", "result.isSuccess() :: " + result.isSuccess());
                    if(result.isSuccess()){
                        HomeLeftFragment.MONG_DETAIL_ACTION = true;
                        Constants.MONG_SRL = result.getData();
                        Log.v("ifeelbluu", " Constants.MONG_SRL: " + Constants.MONG_SRL);
                        finish();
                    }else{
                        Log.d("TEST","실패");
                    }

                } catch (Exception e) {
                    Log.e("TEST","에러" + e.getMessage());
                    e.printStackTrace();
                }
            }

            @Override
            public void onResponseError(String message) {
                Log.e("TEST","에러 : " + message);
            }
        }));
        ActionRuler.getInstance().runNext();
    }

}
