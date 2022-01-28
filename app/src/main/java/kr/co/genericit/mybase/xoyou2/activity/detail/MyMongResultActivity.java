package kr.co.genericit.mybase.xoyou2.activity.detail;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import kr.co.genericit.mybase.xoyou2.R;
import kr.co.genericit.mybase.xoyou2.network.action.ActionRuler;
import kr.co.genericit.mybase.xoyou2.network.interfaces.ActionResultListener;
import kr.co.genericit.mybase.xoyou2.network.request.ActionRequestLogin;
import kr.co.genericit.mybase.xoyou2.network.response.LoginResult;
import kr.co.genericit.mybase.xoyou2.storage.JWSharePreference;
import kr.co.genericit.mybase.xoyou2.view.PwAuthDialog;

/**
 * 사용하지않음
 */
public class MyMongResultActivity extends AppCompatActivity {
    private TextView btn_pw_auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_mong_result);

        initView();

        btn_pw_auth.setOnClickListener(v ->{
            showCustomDialog();
        });
    }

    public void initView(){
        btn_pw_auth = findViewById(R.id.btn_pw_auth);
    }

    public void showCustomDialog(){
        PwAuthDialog dialog = new PwAuthDialog(this, new PwAuthDialog.CustomDialogClickListener() {
            @Override
            public void onPositiveClick(String pw) {
                requestPwAuth(pw);
            }

            @Override
            public void onNegativeClick() {

            }
        });

        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialog.show();
    }

    private void requestPwAuth(String pw){
        final JWSharePreference sharePreference = new JWSharePreference();
        ActionRuler.getInstance().addAction(new ActionRequestLogin(this, sharePreference.getString(JWSharePreference.PREFERENCE_LOGIN_ID,""), pw, new ActionResultListener<LoginResult>() {
            @Override
            public void onResponseResult(LoginResult response) {

                LoginResult result = response;
                if(result.isResult()){
                    Toast.makeText(getApplicationContext(),"비밀번호 인증 완료",Toast.LENGTH_SHORT).show();
                }else{
                    Log.d("!TEST","비밀번호가 일치하지 않습니다.");

                }
            }

            @Override
            public void onResponseError(String message) { }
        }));
        ActionRuler.getInstance().runNext();
    }
}