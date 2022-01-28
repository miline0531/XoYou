package kr.co.genericit.mybase.xoyou2.activity;

import android.os.Bundle;
import android.util.Log;

import kr.co.genericit.mybase.xoyou2.R;
import kr.co.genericit.mybase.xoyou2.activity.main.HomeLeftFragment;
import kr.co.genericit.mybase.xoyou2.common.CommonActivity;
import kr.co.genericit.mybase.xoyou2.common.Constants;
import kr.co.genericit.mybase.xoyou2.databinding.ActivityMongStoreSaveNewBinding;
import kr.co.genericit.mybase.xoyou2.network.action.ActionRuler;
import kr.co.genericit.mybase.xoyou2.network.interfaces.ActionResultListener;
import kr.co.genericit.mybase.xoyou2.network.request.ActionRequestMongStoreSave;
import kr.co.genericit.mybase.xoyou2.network.response.DefaultResult;
import kr.co.genericit.mybase.xoyou2.storage.JWSharePreference;
import kr.co.genericit.mybase.xoyou2.utils.CommandUtil;

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
