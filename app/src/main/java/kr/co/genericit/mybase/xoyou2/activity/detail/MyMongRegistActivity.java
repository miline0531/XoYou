package kr.co.genericit.mybase.xoyou2.activity.detail;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ScrollView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

import kr.co.genericit.mybase.xoyou2.R;
import kr.co.genericit.mybase.xoyou2.activity.CardImageEditActivity;
import kr.co.genericit.mybase.xoyou2.activity.SignView2Activity;
import kr.co.genericit.mybase.xoyou2.adapter.MongRegistRecyclerviewAdapter;
import kr.co.genericit.mybase.xoyou2.common.CommonActivity;
import kr.co.genericit.mybase.xoyou2.common.Constants;
import kr.co.genericit.mybase.xoyou2.databinding.ActivityMyMongRegistNewBinding;
import kr.co.genericit.mybase.xoyou2.network.action.ActionRuler;
import kr.co.genericit.mybase.xoyou2.network.interfaces.ActionResultListener;
import kr.co.genericit.mybase.xoyou2.network.request.ActionRequestCardImageUpload;
import kr.co.genericit.mybase.xoyou2.network.request.ActionRequestSelectData1;
import kr.co.genericit.mybase.xoyou2.network.response.DefaultResult;
import kr.co.genericit.mybase.xoyou2.storage.JWSharePreference;
import kr.co.genericit.mybase.xoyou2.utils.CommandUtil;
import kr.co.genericit.mybase.xoyou2.view.CommonPopupDialog;
import kr.co.genericit.mybase.xoyou2.view.MongAuthDialog;
import kr.co.genericit.mybase.xoyou2.view.MongRegistDialog;
import kr.co.genericit.mybase.xoyou2.view.RecordDialog;
import kr.co.genericit.mybase.xoyou2.view.bottomsheet.BottomSheetDefaultListFragment;
import kr.co.genericit.mybase.xoyou2.view.bottomsheet.model.BottomSheetData;
import okhttp3.ResponseBody;

public class MyMongRegistActivity extends CommonActivity implements View.OnClickListener {


    private ActivityMyMongRegistNewBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        binding = ActivityMyMongRegistNewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        super.onCreate(savedInstanceState);

        initView();

        binding.btnMymongRegist.setOnClickListener(v ->{
            if(binding.cbEssential1.isChecked() && binding.cbEssential2.isChecked()){
                if(binding.saveImageLayout.getVisibility() == View.VISIBLE){
                    showDialog();
                }else{
                    CommandUtil.getInstance().showCommonOneButtonDialog(MyMongRegistActivity.this,CommandUtil.getInstance().getStr(R.string.mong_regist_bg_select),CommandUtil.getInstance().getStr(R.string.str_cofirm), CommonPopupDialog.COMMON_DIALOG_OPTION_CLOSE_DIALOG,null);
                }

            }else{
                CommandUtil.getInstance().showCommonOneButtonDialog(MyMongRegistActivity.this,CommandUtil.getInstance().getStr(R.string.mong_regist_error_essential),CommandUtil.getInstance().getStr(R.string.str_cofirm), CommonPopupDialog.COMMON_DIALOG_OPTION_CLOSE_DIALOG,null);
            }
        });

    }

    public void initView(){
        setRecyclerView();

        binding.btnEssential1.setOnClickListener(this);
        binding.btnEssential2.setOnClickListener(this);
        binding.btnEssential3.setOnClickListener(this);

        binding.cbEssential1.setOnClickListener(this);
        binding.cbEssential2.setOnClickListener(this);
        binding.cbEssential3.setOnClickListener(this);

        binding.btnCb11.setOnClickListener(this);
        binding.btnCb12.setOnClickListener(this);
        binding.btnCb13.setOnClickListener(this);
        binding.cb11.setOnClickListener(this);
        binding.cb12.setOnClickListener(this);
        binding.cb13.setOnClickListener(this);

        binding.btnSelect1.setOnClickListener(onClickBottomSheetView);
        binding.btnSelect2.setOnClickListener(onClickBottomSheetView);
        binding.btnSelect3.setOnClickListener(onClickBottomSheetView);

        try{
            binding.txtTitle.setText(Constants.StoryJob.getString("Title"));
            binding.txtNote.setText(Constants.StoryJob.getString("Note"));
        }catch (Exception e){
            binding.txtTitle.setText(Constants.mongTitle);
            binding.txtNote.setText(Constants.mongNote);
        }

        getMongSelectData();

        binding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.saveImageLayout.setVisibility(View.GONE);
                binding.rcvMymongRegistImage.setVisibility(View.VISIBLE);
            }
        });

        binding.btnGallery.setOnClickListener(v ->{
            openGallery();
        });
    }

    public void openGallery(){
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(i,OPEN_GARRELY_CODE);
    }

    public void setRecyclerView(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,RecyclerView.HORIZONTAL,false);
        binding.rcvMymongRegistImage.setLayoutManager(layoutManager);

        ArrayList<String> dataList = new ArrayList<>();
        for(int i=0; i<10; i++){
            dataList.add(String.valueOf(i));
        }

        MongRegistRecyclerviewAdapter adapter = new MongRegistRecyclerviewAdapter(this,dataList);
        adapter.setOnSelectCardListener(onSelectCardListener);
        binding.rcvMymongRegistImage.setAdapter(adapter);
    }

    public void showCustomDialog(){
        MongRegistDialog dialog = new MongRegistDialog(this, new MongRegistDialog.CustomDialogClickListener() {
            @Override
            public void onPositiveClick() {

                uploadImage();


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

    public void uploadImage(){
        File storage = getCacheDir(); //  path = /data/user/0/YOUR_PACKAGE_NAME/cache
        String fileName = Constants.CardImageName + ".png";
        File imgFile = new File(storage, fileName);
        Log.v("ifeelbluu", "imgFile == " + imgFile.length());

        if(imgFile != null && imgFile.length() != 0){
            ActionRuler.getInstance().addAction(new ActionRequestCardImageUpload(this, imgFile, new ActionResultListener<ResponseBody>() {
                @Override
                public void onResponseResult(ResponseBody response) {
                    try {
                        ResponseBody result = response;

                        String body = result.string();
                        Log.v("ifeelbluu", "result :: " + body);
                        JSONObject job = new JSONObject(body);
                        String ETag = job.getString("ETag");
                        String Location = job.getString("Location");
                        String key = job.getString("key");
                        String Key_ = job.getString("Key");
                        String Bucket = job.getString("Bucket");

                        

                        //링크업로드
                        Intent i = new Intent(getApplicationContext(),MyMongAuthActivity.class);
                        i.putExtra("color",binding.txtSelectColor.getText());
                        i.putExtra("sound",binding.txtSelectSound.getText());
                        i.putExtra("year",binding.txtSelectYear.getText());
                        i.putExtra("pyeongga",binding.edtPyeongga.getText().toString());
                        i.putExtra("imgPath", Location);

                        Log.v("ifeelbluu", "txtSelectColor binding : " + binding.txtSelectColor.getText());
                        Log.v("ifeelbluu", "txtSelectSound binding : " + binding.txtSelectSound.getText());
                        Log.v("ifeelbluu", "edtPyeongga binding : " + binding.edtPyeongga.getText().toString());
                        startActivityForResult(i,MYMONGAUTH_REQUEST_CODE);
                    } catch (Exception e) {
                        Log.e("TEST","에러" + e.getMessage());
                        e.printStackTrace();
                    }
                }
                @Override
                public void onResponseError(String message) {
                    Log.d("TEST","에러 : " + message);
                }
            }));
            ActionRuler.getInstance().runNext();
        }else{
            Intent i = new Intent(getApplicationContext(),MyMongAuthActivity.class);
            i.putExtra("color",binding.txtSelectColor.getText());
            i.putExtra("sound",binding.txtSelectSound.getText());
            i.putExtra("pyeongga",binding.edtPyeongga.getText());
            startActivityForResult(i,MYMONGAUTH_REQUEST_CODE);
        }
    }

    public void showDialog(){
        JWSharePreference jp = new JWSharePreference();
        String user_name = jp.getString(JWSharePreference.PREFERENCE_LOGIN_NAME,"");
//        String message = "본 꿈을 마이몽스토어에 인증등록을 하려면 등록비를 2,000원 납부하여야 진행됩니다.\n" +
//                "\n인증등록비는 "+user_name+"님의 꿈을 NFT로 전환하고 등록된 다른 꿈과 비교 분석하여 보다 가치있게 관리하기 위한 비용입니다.\n" +
//                "\n인증등록 할까요 ?";
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("인증등록");
//        builder.setMessage(message);
//        builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        showCustomDialog();
//                    }
//
//                });
//        builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//
//                    }
//                });
//        builder.show();

        MongAuthDialog dialog = new MongAuthDialog(this,user_name, new MongAuthDialog.CustomDialogClickListener() {
                @Override
                public void onPositiveClick() {
                    showCustomDialog();

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

    MongRegistRecyclerviewAdapter.onSelectCardListener onSelectCardListener = new MongRegistRecyclerviewAdapter.onSelectCardListener() {
        @Override
        public void onClickCard(int id) {
            Intent i = new Intent(MyMongRegistActivity.this, CardImageEditActivity.class);
            i.putExtra("cardBgIndex", id);
            startActivityForResult(i,1000);
        }
    };




    private final int CHECK_ESSENTAIL_1 = 100;
    private final int CHECK_ESSENTAIL_2 = 200;
    private final int CHECK_ESSENTAIL_3 = 300;
    private final int MYMONGAUTH_REQUEST_CODE = 400;
    private final int OPEN_GARRELY_CODE = 1100;
    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){

            case R.id.btn_cb_1_1:
                Prem = grantExternalStoragePermission();
                if(Prem){
                    if(binding.cb11.isChecked()){
                        binding.cb11.setChecked(false);
                    }else{
                        showRecordDialog(recordFile1, 1);
                    }
                }
                break;

            case R.id.btn_cb_1_2:
                Prem = grantExternalStoragePermission();
                if(Prem){
                    if(binding.cb12.isChecked()){
                        binding.cb12.setChecked(false);
                    }else{
                        showRecordDialog(recordFile2, 2);
                    }
                }
                break;

            case R.id.btn_cb_1_3:
                CommandUtil.getInstance().showCommonOneButtonDefaultDialog(MyMongRegistActivity.this,CommandUtil.getInstance().getStr(R.string.txt_common_error_soon));
                break;
            case R.id.btn_essential1:
                if(binding.cbEssential1.isChecked()){ //선택
                    binding.cbEssential1.setChecked(false);
                }else{
                    Intent i = new Intent(MyMongRegistActivity.this, SignView2Activity.class);
                    startActivityForResult(i,CHECK_ESSENTAIL_1);
                }
                break;
            case R.id.btn_essential2:
                if(binding.cbEssential2.isChecked()){ //선택
                    binding.cbEssential2.setChecked(false);
                }else{
                    Intent i = new Intent(MyMongRegistActivity.this, SignView2Activity.class);
                    startActivityForResult(i,CHECK_ESSENTAIL_2);
                }
                break;
            case R.id.btn_essential3:
                if(binding.cbEssential3.isChecked()){ //선택
                    binding.cbEssential3.setChecked(false);
                }else{
                    Intent i = new Intent(MyMongRegistActivity.this, SignView2Activity.class);
                    startActivityForResult(i,CHECK_ESSENTAIL_3);
                }
                break;
        }
    }

    private ArrayList<BottomSheetData> selectData1;
    private ArrayList<BottomSheetData> selectData2;
    private ArrayList<BottomSheetData> selectData3;
    private void getMongSelectData(){
        selectData1 = new ArrayList<>();
        selectData2 = new ArrayList<>();
        selectData3 = new ArrayList<>();

        int mYear = Calendar.getInstance().get(Calendar.YEAR);
        for(int i=0; i<100; i++){
            BottomSheetData item = new BottomSheetData(i,String.valueOf(mYear-i),false);
            selectData3.add(item);
        }

        ActionRuler.getInstance().addAction(new ActionRequestSelectData1(this, new ActionResultListener<DefaultResult>() {
            @Override
            public void onResponseResult(DefaultResult response) {
                try {
                    DefaultResult result = response;

                    if(result.isSuccess()){
                        JSONObject obj = new JSONObject(result.getData());

                        JSONArray MongColorList = obj.getJSONArray("MongColorList");
                        JSONArray MongSoRIList = obj.getJSONArray("MongSoRIList");
//
                        for(int i=0; i<MongColorList.length(); i++){
                            BottomSheetData item = new BottomSheetData(i,MongColorList.get(i).toString(),false);
                            selectData1.add(item);
                        }

                        for(int i=0; i<MongSoRIList.length(); i++){
                            BottomSheetData item = new BottomSheetData(i,MongSoRIList.get(i).toString(),false);
                            selectData2.add(item);
                        }

                    }else{
                        Log.d("TEST","실패");
                    }

                } catch (Exception e) {
                    Log.d("TEST","에러");
                    e.printStackTrace();
                }
            }
            @Override
            public void onResponseError(String message) {
                Log.d("TEST","에러 : " + message);
            }
        }));
        ActionRuler.getInstance().runNext();
    }

    View.OnClickListener onClickBottomSheetView = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            BottomSheetDefaultListFragment bottomSheetDLFragment = BottomSheetDefaultListFragment.newInstance();

            switch (v.getId()){
                case R.id.btn_select_1:
                    bottomSheetDLFragment.setData(selectData1);
                    bottomSheetDLFragment.setListener(mBottomSheetEventListener1);
                    break;
                case R.id.btn_select_2:
                    bottomSheetDLFragment.setData(selectData2);
                    bottomSheetDLFragment.setListener(mBottomSheetEventListener2);
                    break;
                case R.id.btn_select_3:


                    bottomSheetDLFragment.setData(selectData3);
                    bottomSheetDLFragment.setListener(mBottomSheetEventListener3);
                    break;

            }

            bottomSheetDLFragment.setCancelable(false);
            bottomSheetDLFragment.show(getSupportFragmentManager(), "SELECT1");
        }
    };

    private BottomSheetDefaultListFragment.bottomSheetListener mBottomSheetEventListener1 = new BottomSheetDefaultListFragment.bottomSheetListener() {
        @Override
        public void onSelectItem(int type, int prev) {
            if(prev != -1)
                selectData1.get(prev).setCheck(false);
            selectData1.get(type).setCheck(true);
            binding.txtSelectColor.setText(selectData1.get(type).getItemTitle());
        }
    };
    private BottomSheetDefaultListFragment.bottomSheetListener mBottomSheetEventListener2 = new BottomSheetDefaultListFragment.bottomSheetListener() {
        @Override
        public void onSelectItem(int type, int prev) {
            if(prev != -1)
                selectData2.get(prev).setCheck(false);
            selectData2.get(type).setCheck(true);
            binding.txtSelectSound.setText(selectData2.get(type).getItemTitle());

        }
    };
    private BottomSheetDefaultListFragment.bottomSheetListener mBottomSheetEventListener3 = new BottomSheetDefaultListFragment.bottomSheetListener() {
        @Override
        public void onSelectItem(int type, int prev) {
            if(prev != -1)
                selectData3.get(prev).setCheck(false);
            selectData3.get(type).setCheck(true);
            binding.txtSelectYear.setText(selectData3.get(type).getItemTitle());

        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){

            switch (requestCode){
                case CHECK_ESSENTAIL_1:
                    binding.cbEssential1.setChecked(true);
                    break;

                case CHECK_ESSENTAIL_2:
                    binding.cbEssential2.setChecked(true);
                    break;

                case CHECK_ESSENTAIL_3:
                    binding.cbEssential3.setChecked(true);
                    break;

                case 1000:  //이미지 저장완료
                    binding.saveImageLayout.setVisibility(View.VISIBLE);
                    binding.rcvMymongRegistImage.setVisibility(View.GONE);

                    File storage = getCacheDir(); //  path = /data/user/0/YOUR_PACKAGE_NAME/cache
                    String fileName = Constants.CardImageName + ".png";
                    File imgFile = new File(storage, fileName);
                    Log.v("ifeelbluu", "imgFile == " + imgFile.length());

                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

                    binding.saveCardImage.setImageBitmap(myBitmap);

                    break;

                case OPEN_GARRELY_CODE:  //갤러리에서 이미지 선택
                    try{
                        Uri uri = data.getData();
                        Intent i = new Intent(MyMongRegistActivity.this, CardImageEditActivity.class);
                        i.putExtra("uri", uri.toString());
                        startActivityForResult(i,1000);

                    }catch(Exception e){

                    }
                    break;

                case MYMONGAUTH_REQUEST_CODE:
                    finish();
                    break;


            }
        }else{
            if(requestCode == MYMONGAUTH_REQUEST_CODE){
                CommandUtil.getInstance().showCommonOneButtonDefaultDialog(MyMongRegistActivity.this,"꿈꾼 후 소감을 자세히 입력 해주세요");
                binding.edtPyeongga.requestFocus();

                scrollToView(binding.edtPyeongga,binding.scrollView,-300);

                //키보드 보이게 하는부분
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
            }
        }
    }

    public static void scrollToView(View view, final ScrollView scrollView, int count) {
        if (view != null && view != scrollView) {
            count += view.getTop();
            scrollToView((View) view.getParent(), scrollView, count);
        } else if (scrollView != null) {
            final int finalCount = count;
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    scrollView.smoothScrollTo(0, finalCount);
                }
            }, 200);
        }
    }


    private String recordFile1,recordFile2 = null;
    public void showRecordDialog(String path, int index){
        String title = CommandUtil.getInstance().getStr(R.string.mong_regist_record);
        RecordDialog dialog = new RecordDialog(this,title,path,index, new RecordDialog.CustomDialogClickListener() {
            @Override
            public void onPositiveClick(int index, String path) {
                if(index == 1){
                    recordFile1 = path;
                    binding.cb11.setChecked(true);
                }else{
                    recordFile2 = path;
                    binding.cb12.setChecked(true);
                }
            }

            @Override
            public void onNegativeClick() {

            }
        });

        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(false);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialog.show();
    }

    public static int ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE= 5469;
    public boolean Prem = false;

    /**********************
     * Permission Check
     **********************/
    private boolean grantExternalStoragePermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
                Log.v("ifeelbluu","Permission is granted");
                return true;
            }else{
                Log.v("ifeelbluu","Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]
                        {android.Manifest.permission.RECORD_AUDIO}, 1);

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

}