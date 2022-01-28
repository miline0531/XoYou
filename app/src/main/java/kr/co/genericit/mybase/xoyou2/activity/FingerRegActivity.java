package kr.co.genericit.mybase.xoyou2.activity;

import static kr.co.genericit.mybase.xoyou2.storage.JWSharePreference.PREFERENCE_AUTO_FINGER_LOGIN;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import java.util.concurrent.Executor;

import kr.co.genericit.mybase.xoyou2.R;
import kr.co.genericit.mybase.xoyou2.storage.JWSharePreference;
import kr.co.genericit.mybase.xoyou2.utils.CommandUtil;

public class FingerRegActivity extends AppCompatActivity {

    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finger_id_reg);
        initView();
    }

    public void initView(){
        findViewById(R.id.btn_fido).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isBiometric) biometricPrompt.authenticate(promptInfo);
                else Toast.makeText(FingerRegActivity.this,"지문인증을 사용 할 수 없는 기기입니다.",Toast.LENGTH_SHORT).show();
            }
        });
        findViewById(R.id.btnNext).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JWSharePreference sharePreference = new JWSharePreference();
                sharePreference.setString(PREFERENCE_AUTO_FINGER_LOGIN, "C");
                finish();
            }
        });

        checkbiometric();
    }


    public void setupFingerPrint(){
        executor = ContextCompat.getMainExecutor(this);
        biometricPrompt = new BiometricPrompt(this,
                executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Log.v("ifeelbluu", "Authentication :: 에러");
//                Toast.makeText(getApplicationContext(),
//                        "지문인증 에러 : " + errString, Toast.LENGTH_SHORT)
//                        .show();
            }

            @Override
            public void onAuthenticationSucceeded(
                    @NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Log.v("ifeelbluu", "Authentication :: 성공");
                Toast.makeText(getApplicationContext(),"지문인증 성공", Toast.LENGTH_SHORT).show();
                finishActivity();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Log.v("ifeelbluu", "Authentication :: 실패");
                Toast.makeText(getApplicationContext(), "지문이 일치하지 않아요.",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        });

        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("지문을 인식시켜주세요.")
                .setSubtitle("지문을 입력하세요.")
                .setNegativeButtonText("확인")
                .build();
    }
    private boolean isBiometric = false;
    /**
     * 기기에 지문인증 등록되어 있는지 확인
     */
    void checkbiometric() {
        BiometricManager biometricManager = BiometricManager.from(this);
        switch (biometricManager.canAuthenticate()) {
            case BiometricManager.BIOMETRIC_SUCCESS:
                isBiometric = true;
                Log.v("ifeelbluu", "App can authenticate using biometrics.");

                setupFingerPrint();
                isBiometric = true;
//                unSelectedCircle(b.frameFingerPrint, b.imageViewFingerPrint, b.textViewFingerPrint);
                break;
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                Log.v("ifeelbluu", "지문 로그인을 사용할 수 없습니다.");
                Toast.makeText(this, "지문 로그인을 사용할 수 없습니다.", Toast.LENGTH_SHORT).show();
//                disableSelectedCircle(b.frameFingerPrint, b.imageViewFingerPrint, b.textViewFingerPrint);
                break;
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                Log.v("ifeelbluu", "지문 로그인을 사용할 수 없습니다.");
                Toast.makeText(this, "지문 로그인을 사용할 수 없습니다.", Toast.LENGTH_SHORT).show();
//                disableSelectedCircle(b.frameFingerPrint, b.imageViewFingerPrint, b.textViewFingerPrint);
                break;
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                Toast.makeText(this, "지문 로그인을 사용할 수 없습니다.", Toast.LENGTH_SHORT).show();
//                disableSelectedCircle(b.frameFingerPrint, b.imageViewFingerPrint, b.textViewFingerPrint);
                // Prompts the user to create credentials that your app accepts.
//                final Intent enrollIntent = new Intent(Settings.ACTION_BIOMETRIC_ENROLL);
//                enrollIntent.putExtra(Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
//                        BIOMETRIC_STRONG | DEVICE_CREDENTIAL);
//                startActivityForResult(enrollIntent, REQUEST_CODE);
                break;
        }
    }

    public void finishActivity(){
        CommandUtil.getInstance().showLoadingDialog(this);
        JWSharePreference sharePreference = new JWSharePreference();
        sharePreference.setString(PREFERENCE_AUTO_FINGER_LOGIN, "Y");
        setResult(RESULT_OK);
        finish();
    }
}