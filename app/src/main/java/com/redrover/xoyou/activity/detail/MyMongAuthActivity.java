package com.redrover.xoyou.activity.detail;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.Random;

import com.redrover.xoyou.R;
import com.redrover.xoyou.common.CommonActivity;
import com.redrover.xoyou.interfaces.DialogClickListener;
import com.redrover.xoyou.network.action.ActionRuler;
import com.redrover.xoyou.network.interfaces.ActionResultListener;
import com.redrover.xoyou.network.request.ActionRequestMongStorePrice;
import com.redrover.xoyou.network.request.ActionRequestUpdateCerfify;
import com.redrover.xoyou.network.response.DefaultResult;
import com.redrover.xoyou.storage.JWSharePreference;
import com.redrover.xoyou.utils.CommandUtil;
import com.redrover.xoyou.view.CommonPopupDialog;
import okhttp3.ResponseBody;

/**
 * 마이몽 인증등록
 */
public class MyMongAuthActivity extends CommonActivity {
    private ImageView check_1,check_2,check_3,check_4,check_5;
    private ProgressBar progressBar_1,progressBar_2,progressBar_3,progressBar_4;
    private TextView btn_mymong_auth,mymong_auth_text_4,mymong_auth_text_3,mymong_auth_text_2,mymong_auth_text_1;
    private int step = 0;
    private String mColor,mSound,mYear,mPyeongga,mImgPath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_my_mong_auth);
        super.onCreate(savedInstanceState);

        Intent i = getIntent();
        mColor = i.getStringExtra("color");
        mSound = i.getStringExtra("sound");
        mYear = i.getStringExtra("year");
        mPyeongga = i.getStringExtra("pyeongga");
        mImgPath = i.getStringExtra("imgPath");

        initView();

        check_1.setBackgroundResource(R.drawable.ic_baseline_check_circle_outline_24);

        btn_mymong_auth.setOnClickListener(v ->{
            switch (step){
                case 0:
                    mongPriceApi();
                    Log.d("CHECK@","mColor : " + mColor);
                    Log.d("CHECK@","mSound : " + mSound);
                    Log.d("CHECK@","mYear : " + mYear);
                    Log.d("CHECK@","mPyeongga : " + mPyeongga);
                    Log.d("CHECK@","mImgPath : " + mImgPath);
                    break;
                case 1:
                    startAnimation(progressBar_2,check_3);
                    break;
                case 2:
                    startAnimation(progressBar_3,check_4);
                    break;
                case 3:
                    startAnimation(progressBar_4,check_5);
                    break;
                case 4:
                    updateCertify();
//                    Intent intent = new Intent(this,MyMongResultActivity.class);
//                    startActivity(intent);
                    break;
            }
        });
    }

    public void startAnimation(ProgressBar mProgressBar,ImageView checkImage){
        btn_mymong_auth.setClickable(false);

        AnimatorSet set = new AnimatorSet();
        set.playTogether(
                ObjectAnimator.ofInt(mProgressBar, "progress", 0, 100)
        );
        set.setDuration(1000).start();

        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                checkImage.setBackgroundResource(R.drawable.ic_baseline_check_circle_outline_24);
                step++;
                btn_mymong_auth.setClickable(true);

                switch (step){
                    case 0:
                        startAnimation(progressBar_1,check_2);
                        btn_mymong_auth.setText(CommandUtil.getInstance().getStr(R.string.activity_mong_auth_2));
                        break;
                    case 1:
                        mymong_auth_text_1.setText("100%");
                        btn_mymong_auth.setText(CommandUtil.getInstance().getStr(R.string.activity_mong_auth_4));
                        //startAnimation(progressBar_2,check_3);
                        break;
                    case 2:
                        //startAnimation(progressBar_3,check_4);
                        mymong_auth_text_2.setText(pg);
                        btn_mymong_auth.setText(CommandUtil.getInstance().getStr(R.string.activity_mong_auth_6));
                        break;
                    case 3:
                        //startAnimation(progressBar_4,check_5);
                        String NFTno = "MI-en"+ createNFTNumber(6) + "-" + createNFTNumber(8);
                        btn_mymong_auth.setText(CommandUtil.getInstance().getStr(R.string.activity_mong_auth_8));
                        mymong_auth_text_3.setText(NFTno);
                        break;
                    case 4:
                        String price ="";
                        if(mp.indexOf(".") != -1){
                            price = mp.substring(0,mp.indexOf("."));
                        }else{
                            price = mp;
                        }

                        mymong_auth_text_4.setText(CommandUtil.getInstance().getStr(R.string.mong_auth_price) + Integer.parseInt(price) + CommandUtil.getInstance().getStr(R.string.mong_auth_price_won));
                        btn_mymong_auth.setText(CommandUtil.getInstance().getStr(R.string.mong_auth_success));
                        break;

                }
            }
        });
    }

    public String createNFTNumber(int pcs){
        String result = "";
        int count = pcs; // 난수 생성 갯수
        int a[] = new int[count];
        Random r = new Random();

        for(int i=0; i<count; i++){
            a[i] = r.nextInt(9) + 1; // 1 ~ 99까지의 난수
//            for(int j=0; j<i; j++){
//                if(a[i] == a[j]){
//                    i--;
//                }
//            }
            result += a[i]+"";
        }

        return result;
    }

    public void initView(){
        check_1 = findViewById(R.id.check_1);
        check_2 = findViewById(R.id.check_2);
        check_3 = findViewById(R.id.check_3);
        check_4 = findViewById(R.id.check_4);
        check_5 = findViewById(R.id.check_5);
        progressBar_1 = findViewById(R.id.progressBar_1);
        progressBar_2 = findViewById(R.id.progressBar_2);
        progressBar_3 = findViewById(R.id.progressBar_3);
        progressBar_4 = findViewById(R.id.progressBar_4);
        btn_mymong_auth = findViewById(R.id.btn_mymong_auth);
        mymong_auth_text_1 = findViewById(R.id.mymong_auth_text_1);
        mymong_auth_text_2 = findViewById(R.id.mymong_auth_text_2);
        mymong_auth_text_3 = findViewById(R.id.mymong_auth_text_3);
        mymong_auth_text_4 = findViewById(R.id.mymong_auth_text_4);

        JWSharePreference jp = new JWSharePreference();
        String user_name = jp.getString(JWSharePreference.PREFERENCE_LOGIN_NAME,"");
        TextView txt_result = findViewById(R.id.txt_result);
        String txtInfo = txt_result.getText().toString().replace("_UserName",user_name);
        txt_result.setText(txtInfo);
    }

    String pg, mp;
    public void mongPriceApi(){
        ActionRuler.getInstance().addAction(new ActionRequestMongStorePrice(this,mColor,mSound,mYear,mPyeongga, new ActionResultListener<DefaultResult>() {
            @Override
            public void onResponseResult(DefaultResult response) {
                try {
                    DefaultResult result = response;

                    if(result.isSuccess()){
                        Log.v("ifeelbluu", "result.getData() :: " + result.getData());
                        JSONObject obj = new JSONObject(result.getData());
                        pg = obj.getString("PyeongGa");
                        mp = obj.getString("MongPrice");
                        startAnimation(progressBar_1,check_2);

                    }else{
                        //todo failText
                        Log.d("TEST","fail");
                    }

                } catch (Exception e) {
                    Log.d("TEST","에러");
                    e.printStackTrace();
                }
            }
            @Override
            public void onResponseError(String message) {
                setResult(RESULT_CANCELED);
                finish();
                Log.d("TEST","에러 : " + message);
            }
        }));
        ActionRuler.getInstance().runNext();
    }

    public void updateCertify(){
        ActionRuler.getInstance().addAction(new ActionRequestUpdateCerfify(this, mImgPath, new ActionResultListener<ResponseBody>() {
            @Override
            public void onResponseResult(ResponseBody response) {
                try {
                    ResponseBody result = response;
                    String body = result.string();
                    Log.v("ifeelbluu", "result :: " + body);
                    try{
                        JSONObject job = new JSONObject(body);
                        boolean success = job.getBoolean("result");
                        if(success){
                            CommandUtil.getInstance().showCommonOneButtonDialog(MyMongAuthActivity.this, CommandUtil.getInstance().getStr(R.string.mong_auth_success_finish), CommandUtil.getInstance().getStr(R.string.str_cofirm), CommonPopupDialog.COMMON_DIALOG_OPTION_CLOSE_DIALOG, new DialogClickListener() {
                                @Override
                                public void onClick(int button) {
                                    setResult(RESULT_OK);
                                    finish();
                                }
                            });

                        }else{
                            Toast.makeText(MyMongAuthActivity.this,CommandUtil.getInstance().getStr(R.string.mong_auth_fail),Toast.LENGTH_SHORT).show();
                        }
                    }catch (Exception e){
                        Toast.makeText(MyMongAuthActivity.this,CommandUtil.getInstance().getStr(R.string.mong_auth_fail),Toast.LENGTH_SHORT).show();
                    }



                } catch (Exception e) {
                    Log.e("TEST","에러" + e.getMessage());
                    e.printStackTrace();
                    Toast.makeText(MyMongAuthActivity.this,CommandUtil.getInstance().getStr(R.string.mong_auth_fail),Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onResponseError(String message) {
                Log.d("TEST","에러 : " + message);
                Toast.makeText(MyMongAuthActivity.this,CommandUtil.getInstance().getStr(R.string.mong_auth_fail),Toast.LENGTH_SHORT).show();
            }
        }));
        ActionRuler.getInstance().runNext();
    }
}