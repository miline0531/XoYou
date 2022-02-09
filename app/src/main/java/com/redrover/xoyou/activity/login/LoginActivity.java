package com.redrover.xoyou.activity.login;

import static com.redrover.xoyou.storage.JWSharePreference.PREFERENCE_AUTO_FINGER_LOGIN;
import static com.redrover.xoyou.storage.JWSharePreference.PREFERENCE_AUTO_LOGIN_ID;
import static com.redrover.xoyou.storage.JWSharePreference.PREFERENCE_AUTO_LOGIN_PASS;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.biometric.BiometricPrompt;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executor;

import com.redrover.xoyou.BuildConfig;
import com.redrover.xoyou.R;
import com.redrover.xoyou.activity.FingerRegActivity;
import com.redrover.xoyou.activity.MainActivity;
import com.redrover.xoyou.common.CommonActivity;
import com.redrover.xoyou.common.Constants;
import com.redrover.xoyou.common.NetInfo;
import com.redrover.xoyou.interfaces.DialogClickListener;
import com.redrover.xoyou.network.action.ActionRuler;
import com.redrover.xoyou.network.interfaces.ActionResultListener;
import com.redrover.xoyou.network.request.ActionRequestLogin;
import com.redrover.xoyou.network.request.ActionRequestVersionCheck;
import com.redrover.xoyou.network.response.LoginResult;
import com.redrover.xoyou.network.response.VersionCheckResponse;
import com.redrover.xoyou.storage.JWSharePreference;
import com.redrover.xoyou.utils.CommandUtil;
import com.redrover.xoyou.view.CommonPopupDialog;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Streaming;

public class LoginActivity extends CommonActivity {
    private LinearLayout layout_intro_bg;
    final JWSharePreference sharePreference = new JWSharePreference();
    private String id = "";
    private String pw = "";
    private int time = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        showIntroBG();

        Constants.setRootUserInit();

        EditText idEditText = findViewById(R.id.login_id_edittext);
        EditText pwEditText = findViewById(R.id.login_pw_edittext);

        String getId = sharePreference.getString(PREFERENCE_AUTO_LOGIN_ID,"");
        String getPass = sharePreference.getString(PREFERENCE_AUTO_LOGIN_PASS,"");
        idEditText.setText(getId);
        pwEditText.setText(getPass);
        Log.d("TEST","pass : " + getPass);

        //회원가입 버튼
        TextView signUpTextView = findViewById(R.id.signup_textview);
        signUpTextView.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
//            Intent intent = new Intent(getApplicationContext(), ADevTempActivity.class);
            startActivity(intent);
        });


        //로그인 버튼
        TextView loginButton = findViewById(R.id.login_btn);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id = idEditText.getText().toString();
                pw = pwEditText.getText().toString();

                if (id.equals("") || pw.equals("")) {
                    Toast.makeText(getApplicationContext(), CommandUtil.getInstance().getStr(R.string.mong_login_id_pw_input), Toast.LENGTH_SHORT).show();
                } else {
//                    Intent i = new Intent(LoginActivity.this, MainActivity.class);
//                    startActivity(i);
                    sendRequestForLogin(false);

                }

            }
        });

        //아이디 찾기 버튼
        TextView findIdTextView = findViewById(R.id.findid_textview);
        findIdTextView.setOnClickListener(v ->{
            Intent intent = new Intent(getApplicationContext(), FindIdActivity.class);
            startActivity(intent);
        });


        //비밀번호 찾기 버튼
        TextView findPwTextView = findViewById(R.id.findpw_textview);
        findPwTextView.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), FindPwActivity.class);
            startActivity(intent);
        });

        String isFinger = sharePreference.getString(PREFERENCE_AUTO_FINGER_LOGIN, "N");
        if(Constants.isBioMetric && isFinger.equals("Y")){
            id = idEditText.getText().toString();
            pw = pwEditText.getText().toString();
            init();
            auth();
        }

//        //test
//        idEditText.setText("kdh0002");
//        pwEditText.setText("plokijuh1@");
//
//
//        id = idEditText.getText().toString();
//        pw = pwEditText.getText().toString();
//
//        if (id.equals("") || pw.equals("")) {
//            Toast.makeText(getApplicationContext(), CommandUtil.getInstance().getStr(R.string.mong_login_id_pw_input), Toast.LENGTH_SHORT).show();
//        } else {
////                    Intent i = new Intent(LoginActivity.this, MainActivity.class);
////                    startActivity(i);
//            sendRequestForLogin(false);
//
//        }
    }

    private void showIntroBG(){


        TimerTask IntroBG = new TimerTask() {
            @Override
            public void run() {
                Message m = new Message();
                m.what = INTRO_VERSION_CHECK;
                LoginHandler.sendMessage(m);
            }
        };
        Timer timer = new Timer();
        timer.schedule(IntroBG, 2000);

    }

    private final int INTRO_VERSION_CHECK = 0;
    private final int INTRO_BG_ANIMATION = 1;
    private final int INTRO_VERSION_UPDATE = 2;

    public static int ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE= 5469;
    public static boolean Prem = false;
    public static boolean isPerm(){
        return Prem;
    }
    //인트로 및 버전 체크 핸들러
    private final Handler LoginHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if(msg.what == INTRO_VERSION_CHECK){
                ServerVersionCehck();
            }else if(msg.what == INTRO_BG_ANIMATION){
                layout_intro_bg = findViewById(R.id.layout_intro_bg);
                Animation animation = AnimationUtils.loadAnimation(getBaseContext(),R.anim.fade_out_1500);
                layout_intro_bg.startAnimation(animation);
                //권한 체크
                Permission();
                Prem = grantExternalStoragePermission();
            }else if(msg.what == INTRO_VERSION_UPDATE){
//                Intent i = new Intent(LoginActivity.this,DownloadActivity.class);
//                startActivity(i);
//                startDownload();
            }
        }
    };
    public void Permission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M     // M 버전(안드로이드 6.0 마시멜로우 버전) 보다 같거나 큰 API에서만 설정창 이동 가능합니다.,
                && !Settings.canDrawOverlays(this)) {                     //지금 창이 오버레이 설정창이 아니라면 조건 입니다.
            PermissionOverlay();
        } else {
            System.out.println("버전이 낮거나 오버레이설정창이 아니라면");
        }
    }

    @TargetApi(Build.VERSION_CODES.M)  //M 버전 이상 API를 타겟으로,
    public void PermissionOverlay() {
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:" + getPackageName()));
        startActivityForResult(intent, ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE);
    }

    /**********************
     * Permission Check
     **********************/
    private boolean grantExternalStoragePermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED &&
                    checkSelfPermission(android.Manifest.permission.READ_CALL_LOG) == PackageManager.PERMISSION_GRANTED &&
                    checkSelfPermission(android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED &&
                    checkSelfPermission(android.Manifest.permission.PROCESS_OUTGOING_CALLS) == PackageManager.PERMISSION_GRANTED &&
                    checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                    checkSelfPermission(android.Manifest.permission.RECEIVE_BOOT_COMPLETED) == PackageManager.PERMISSION_GRANTED &&
                    checkSelfPermission(android.Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED &&
                    checkSelfPermission(android.Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED &&
                    checkSelfPermission(android.Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED &&
                    checkSelfPermission(android.Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED
            ) {
                Log.v("ifeelbluu","Permission is granted");
                return true;
            }else{
                Log.v("ifeelbluu","Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]
                        {android.Manifest.permission.READ_CONTACTS,android.Manifest.permission.READ_CALL_LOG,
                                android.Manifest.permission.CALL_PHONE,
                                android.Manifest.permission.PROCESS_OUTGOING_CALLS,
                                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                android.Manifest.permission.RECEIVE_BOOT_COMPLETED,
                                android.Manifest.permission.READ_PHONE_STATE,
                                android.Manifest.permission.READ_SMS,
                                android.Manifest.permission.SEND_SMS,
                                android.Manifest.permission.RECEIVE_SMS,

                        }, 1);

                return false;
            }
        }else{
            Toast.makeText(this, "External Storage Permission is Grant",
                    Toast.LENGTH_SHORT).show();
            Log.d("ifeelbluu", "External Storage Permission is Grant ");

            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (Build.VERSION.SDK_INT >= 23) {
            if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
                Log.v("ifeelbluu","Permission: "+permissions[0]+ "was "+grantResults[0]);
                //resume tasks needing this permission
            }
        }

        if (requestCode == ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (Settings.canDrawOverlays(this)) {
                    // You have permission

                    // 오버레이 설정창 이동 후 이벤트 처리합니다.

                }
            }
        }
    }
    public void sendRequestForLogin(final boolean pass){
        ActionRuler.getInstance().addAction(new ActionRequestLogin(this, id, pw, new ActionResultListener<LoginResult>() {
            @Override
            public void onResponseResult(LoginResult response) {

                LoginResult result = response;
                if(result.isResult()){
                    int srl = result.getData().getU_srl();
                    Log.v("ifeelbluu", "srl ::: " + srl);

                    String loginId = result.getData().getU_id();
                    String userId = result.getData().getU_email();
                    String userName = result.getData().getU_name();
                    String birth = result.getData().getU_birth();
                    int gender = result.getData().getU_gender();
                    String loginToken = result.getData().getAccess_token();

                    sharePreference.setInt(JWSharePreference.PREFERENCE_SRL, srl);
                    sharePreference.setString(JWSharePreference.PREFERENCE_LOGIN_ID, loginId);
                    sharePreference.setString(JWSharePreference.PREFERENCE_USER_ID, userId);
                    sharePreference.setString(JWSharePreference.PREFERENCE_LOGIN_NAME, userName);
                    sharePreference.setString(JWSharePreference.PREFERENCE_LOGIN_BIRTH, birth);
                    sharePreference.setInt(JWSharePreference.PREFERENCE_GENDER, gender);
//                    sharePreference.setString(JWSharePreference.PREFERENCE_USER_ID, "1");
                    sharePreference.setString(JWSharePreference.PREFERENCE_LOGIN_TOKEN, loginToken);
                    sharePreference.setString(JWSharePreference.PREFERENCE_LOGIN_FLAG, "Y");

                    sharePreference.setString(PREFERENCE_AUTO_LOGIN_ID, loginId);
                    sharePreference.setString(PREFERENCE_AUTO_LOGIN_PASS, pw);
                    Log.v("ifeelbluu", "StartMainView :: ");
                    if(Constants.isBioMetric) {
                        if(!pass){
                            String isFinger = sharePreference.getString(PREFERENCE_AUTO_FINGER_LOGIN, "N");
                            if(isFinger.equals("N")){
                                Intent fi = new Intent(LoginActivity.this, FingerRegActivity.class);
                                startActivityForResult(fi,1000);
                                return;
                            }
                        }

                        CommandUtil.getInstance().showLoadingDialog(LoginActivity.this);
                        Intent i = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(i);
                        finish();
                        return;
                    }else{
                        CommandUtil.getInstance().showLoadingDialog(LoginActivity.this);
                        Intent i = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(i);
                        finish();
                    }

                }else{
                    CommandUtil.getInstance().showCommonOneButtonDialog(LoginActivity.this, CommandUtil.getInstance().getStr(R.string.mong_email_find_pw_error), CommandUtil.getInstance().getStr(R.string.str_cofirm), CommonPopupDialog.COMMON_DIALOG_OPTION_CLOSE_DIALOG, new DialogClickListener() {
                        @Override
                        public void onClick(int button) {
//                            finish();
                        }
                    });
                }
            }

            @Override
            public void onResponseError(String message)
            {
                CommandUtil.getInstance().dismissLoadingDialog();
                CommandUtil.getInstance().showCommonOneButtonDialog(LoginActivity.this, message, CommandUtil.getInstance().getStr(R.string.str_cofirm), CommonPopupDialog.COMMON_DIALOG_OPTION_CLOSE_DIALOG, new DialogClickListener() {
                    @Override
                    public void onClick(int button) {
//                            finish();
                    }
                });
            }
        }));
        ActionRuler.getInstance().runNext();
    }

    private void ServerVersionCehck(){
        ActionRuler.getInstance().addAction(new ActionRequestVersionCheck(this, new ActionResultListener<VersionCheckResponse>() {
            @Override
            public void onResponseResult(VersionCheckResponse response) {
                VersionCheckResponse result = response;

                try {
                    Log.v("ifeelbluu","ActionRequestVersionCheck :: " + result.getData().getVersion());
                    versionCheck(result.getData().getVersion());

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onResponseError(String message)
            {
                CommandUtil.getInstance().dismissLoadingDialog();
                CommandUtil.getInstance().showCommonOneButtonDialog(LoginActivity.this, message, CommandUtil.getInstance().getStr(R.string.str_cofirm), CommonPopupDialog.COMMON_DIALOG_OPTION_CLOSE_DIALOG, new DialogClickListener() {
                    @Override
                    public void onClick(int button) {
//                            finish();
                    }
                });
            }
        }));
        ActionRuler.getInstance().runNext();
    }

    private void versionCheck(String ServerVersion){
        if(ServerVersion.equals(Constants.APP_VERSION)){
            //pass
            Message m = new Message();
            m.what = INTRO_BG_ANIMATION;
            LoginHandler.sendMessage(m);
        }else{
            Toast.makeText(getApplicationContext() , "업데이트 이동.." , Toast.LENGTH_SHORT).show();
            Message m = new Message();
            m.what = INTRO_VERSION_UPDATE;
            LoginHandler.sendMessage(m);
            //update
//            CommandUtil.getInstance().showCommonOneButtonDefaultDialog(LoginActivity.this,"업데이트가 필요합니다.");
        }

    }

    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;
    public void init(){
        executor = ContextCompat.getMainExecutor(getApplicationContext());
        biometricPrompt = new BiometricPrompt(LoginActivity.this,
                executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(getApplicationContext(),"Error", Toast.LENGTH_SHORT)
                        .show();
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                sendRequestForLogin(true);
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(getApplicationContext(), "nFailed", Toast.LENGTH_SHORT)
                        .show();
            }
        });

        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("생체 인증")
                .setSubtitle("인증해주세요.")
                .setNegativeButtonText("취소")
                .setDeviceCredentialAllowed(false)
                .build();

    }

    public void auth(){
        biometricPrompt.authenticate(promptInfo);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.v("CHECK", "dd :: " +requestCode);
        if(requestCode == 1000){ //사용자변경/등록
            if(resultCode == RESULT_OK){
                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        }
    }



    //다운로드 -------------

    private String TAG = "DownloadAPK::";
    private ProgressDialog mProgressDialog;
    public static final int DIALOG_DOWNLOAD_PROGRESS = 0;

    public class downloadRequest{
        public String distCd;

        public String getDistCd() {
            return distCd;
        }

        public void setDistCd(String distCd) {
            this.distCd = distCd;
        }
    }


    public static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    public interface ServerAPI {


//        builder.addHeader("Content-Type", "application/octet-stream; charset=utf-8");
        @Streaming
        @GET("filedown")
        Call<ResponseBody> downlload(@Query("distCd") String cd);

        Retrofit retrofit =
                new Retrofit.Builder()
                        .baseUrl(NetInfo.SERVER_DOWNLOAD_URL) // REMEMBER TO END with /
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(httpClient.build()).build();

    }

    private void startDownload() {
        new DownloadFileAsync().execute();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DIALOG_DOWNLOAD_PROGRESS:
                mProgressDialog = new ProgressDialog(this);
                mProgressDialog.setMessage(CommandUtil.getInstance().getStr(R.string.mong_login_update_download));
                mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                mProgressDialog.setCancelable(false);
                mProgressDialog.show();
                return mProgressDialog;
            default:
                return null;
        }
    }

    class DownloadFileAsync extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(DIALOG_DOWNLOAD_PROGRESS);
        }

        @Override
        protected String doInBackground(String... aurl) {

            httpClient.addInterceptor(new Interceptor() {
                @Override
                public okhttp3.Response intercept(Chain chain) throws IOException {
                    Request request = chain.request().newBuilder().addHeader("Content-Type", "application/octet-stream").addHeader("Accept-Charset","utf-8").build();
                    Log.v("ifeelbluu", "intercept :: " + request.url() );
                    Log.v("ifeelbluu", "intercept :: " );
                    return chain.proceed(request);
                }
            });

            ServerAPI api = ServerAPI.retrofit.create(ServerAPI.class);

            try {
                File file = getFileStreamPath("Mymong.apk");

                downloadRequest info = new downloadRequest();
                info.setDistCd("DIST03");
                Response<ResponseBody> response = api.downlload("DIST03").execute();

                Log.v("ifeelbluu", "response ::[code] " + response.code());
                Log.v("ifeelbluu", "response ::[success] " + response.isSuccessful());
                if(response.code() == 404){
                    return CommandUtil.getInstance().getStr(R.string.mong_login_update_download_error);
                }else{
                    ResponseBody apiResponse = response.body();

                    InputStream input = apiResponse.byteStream();
                    OutputStream output = new FileOutputStream(file);

                    byte data[] = new byte[4096];
                    long fileSize = apiResponse.contentLength();
                    long fileSizeDownloaded = 0;
                    while (true) {
                        int read = input.read(data);

                        if (read == -1) {
                            Log.d(TAG, "file download: break" + fileSizeDownloaded + " of " + fileSize);
                            break;
                        }

                        output.write(data, 0, read);

                        fileSizeDownloaded += read;
                        publishProgress(""+(int)((fileSizeDownloaded*100)/fileSize));
                        Log.d(TAG, "file download: " + fileSizeDownloaded + " of " + fileSize);
                    }
                    Log.d(TAG, "file download: break" + false);
                    output.flush();
                }
            }
            catch (Exception ex){
                ex.printStackTrace();
            }

            return null;

        }
        protected void onProgressUpdate(String... progress) {
            Log.d("ANDRO_ASYNC",progress[0]);
            mProgressDialog.setProgress(Integer.parseInt(progress[0]));
        }

        @Override
        protected void onPostExecute(String unused) {
            dismissDialog(DIALOG_DOWNLOAD_PROGRESS);
            if(unused != null){
                CommandUtil.getInstance().showCommonOneButtonDefaultDialog(LoginActivity.this,CommandUtil.getInstance().getStr(R.string.mong_login_update_download_error));
            }else{
                setUpAPK();
            }

        }
    }

    public void setUpAPK(){
        File file = getFileStreamPath("Mymong.apk");
        // 다운로드한 파일 실행하여 업그레이드 진행하는 코드
        if (Build.VERSION.SDK_INT >= 24) {
            // Android Nougat ( 7.0 ) and later
            installApk(file);
            System.out.println("SDK_INT 24 이상 ");
        } else {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            Uri apkUri = Uri.fromFile(file);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getApplicationContext().startActivity(intent);
            System.out.println("SDK_INT 23 이하 ");
        }

        finish();
    }

    //android 7.0이상 apk 설치
    public void installApk(File file) {
        Uri uri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".fileprovider",file);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        this.startActivity(intent);
    }
}