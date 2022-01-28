package kr.co.genericit.mybase.xoyou2.activity.main;

import static kr.co.genericit.mybase.xoyou2.storage.JWSharePreference.PREFERENCE_AUTO_FINGER_LOGIN;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.Executor;

import kr.co.genericit.mybase.xoyou2.R;
import kr.co.genericit.mybase.xoyou2.activity.CalendarActivity;
import kr.co.genericit.mybase.xoyou2.activity.CardActivity;
import kr.co.genericit.mybase.xoyou2.activity.MainActivity;
import kr.co.genericit.mybase.xoyou2.activity.MongStoreSaveActivity;
import kr.co.genericit.mybase.xoyou2.activity.detail.MyMongRegistActivity;
import kr.co.genericit.mybase.xoyou2.activity.detail.MymongDetailActivity;
import kr.co.genericit.mybase.xoyou2.common.Constants;
import kr.co.genericit.mybase.xoyou2.network.action.ActionRuler;
import kr.co.genericit.mybase.xoyou2.network.interfaces.ActionResultListener;
import kr.co.genericit.mybase.xoyou2.network.request.ActionRequestLogin;
import kr.co.genericit.mybase.xoyou2.network.request.ActionRequestSelectData;
import kr.co.genericit.mybase.xoyou2.network.request.ActionRequestStorySearch;
import kr.co.genericit.mybase.xoyou2.network.response.DefaultResult;
import kr.co.genericit.mybase.xoyou2.network.response.LoginResult;
import kr.co.genericit.mybase.xoyou2.network.response.MongSelectDataResult;
import kr.co.genericit.mybase.xoyou2.storage.JWSharePreference;
import kr.co.genericit.mybase.xoyou2.utils.CommandUtil;
import kr.co.genericit.mybase.xoyou2.view.CommonPopupDialog;
import kr.co.genericit.mybase.xoyou2.view.PwAuthDialog;
import kr.co.genericit.mybase.xoyou2.view.bottomsheet.BottomSheetDefaultListFragment;
import kr.co.genericit.mybase.xoyou2.view.bottomsheet.model.BottomSheetData;


public class HomeFragment extends Fragment implements View.OnClickListener {

    private ViewPager2 pager;
    private HomePagerAdapter pagerAdapter;
    private TextView btn_bottom_left,btn_bottom_right;

    private int mCurrentTabIndex = 1;
    private LinearLayout btn_keyword_selectbox, btn_keyword_inputbox;
    private TextView txt_keyword1, txt_keyword2;
    private View view_under_line1, view_under_line2;
    ImageView test;

    private EditText edt_keyword;
    private TextView inputLength;

    private RelativeLayout select_box_1,select_box_2,select_box_3,select_box_4;
    private TextView select_box_txt_1,select_box_txt_2,select_box_txt_3,select_box_txt_4;
    //private EditText select_box_data;

    private TextView txt_select_result, txt_select_result_default;
    private LinearLayout home_content_input, home_content_select;

    private BottomSheetDefaultListFragment bottomSheetDLFragment;
    private DatePickerDialog datePickerDialog;
    private String birthday="";
    private final JWSharePreference sharePreference = new JWSharePreference();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        initView(v);

        v.findViewById(R.id.btn_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).MainOpenDrawer();
            }
        });
        return v;
    }

    private void initView(View v){
        init();
        btn_keyword_selectbox = v.findViewById(R.id.btn_keyword_selectbox);
        btn_keyword_inputbox = v.findViewById(R.id.btn_keyword_inputbox);
        home_content_input = v.findViewById(R.id.home_content_input);
        home_content_select = v.findViewById(R.id.home_content_select);
        txt_keyword1 = v.findViewById(R.id.txt_keyword1);
        txt_keyword2 = v.findViewById(R.id.txt_keyword2);
        view_under_line1 = v.findViewById(R.id.view_under_line1);
        view_under_line2 = v.findViewById(R.id.view_under_line2);
        edt_keyword = v.findViewById(R.id.edt_keyword);
        inputLength = v.findViewById(R.id.inputLength);


        btn_bottom_left = v.findViewById(R.id.btn_bottom_left);
        btn_bottom_right = v.findViewById(R.id.btn_bottom_right);
        pager = v.findViewById(R.id.frag_home_pager);
        pagerAdapter = new HomePagerAdapter(this,2);
        pager.setAdapter(pagerAdapter);
        pager.setCurrentItem(0);


        pager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
           @Override
           public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
               super.onPageScrolled(position, positionOffset, positionOffsetPixels);
               if (positionOffsetPixels == 0) {
                   pager.setCurrentItem(position);
               }
           }
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                bottomButtonIndex = position;
                bottomButtonAction();
            }
        });

        btn_bottom_left.setOnClickListener(view ->{
            pager.setCurrentItem(0);
        });
        btn_bottom_right.setOnClickListener(view ->{
            pager.setCurrentItem(1);
        });
//        edt_keyword.setText("이건 테스트용 텍스트입니다. 오늘은 육개장을 먹었습니다.");

        btn_keyword_selectbox.setOnClickListener(this);
        btn_keyword_inputbox.setOnClickListener(this);


        test = v.findViewById(R.id.btn_test);
        test.setOnClickListener( k ->{
            Intent i = new Intent(getActivity(), CardActivity.class);
            startActivity(i);
        });

        v.findViewById(R.id.btn_mymong_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCurrentTabIndex==0){
                    getDataSelectBoxPlay();
                }else if(mCurrentTabIndex == 1){
                    storySearchPlay();
                }
            }
        });
        v.findViewById(R.id.btn_calendar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), CalendarActivity.class);
                startActivity(i);
            }
        });
        setTextWatcher();
        txt_select_result_default =v.findViewById(R.id.txt_select_result_default);
        select_box_1 = v.findViewById(R.id.select_box_1);
        select_box_2 = v.findViewById(R.id.select_box_2);
        select_box_3 = v.findViewById(R.id.select_box_3);
        select_box_4 = v.findViewById(R.id.select_box_4);
        select_box_txt_1 = v.findViewById(R.id.select_box_txt_1);
        select_box_txt_2 = v.findViewById(R.id.select_box_txt_2);
        select_box_txt_3 = v.findViewById(R.id.select_box_txt_3);
        select_box_txt_4 = v.findViewById(R.id.select_box_txt_4);
        txt_select_result = v.findViewById(R.id.txt_select_result);
        select_box_1.setOnClickListener(this);
        select_box_2.setOnClickListener(this);
        select_box_3.setOnClickListener(this);
        select_box_4.setOnClickListener(this);

        //select_box_data = v.findViewById(R.id.select_box_data);
//        RelativeLayout select_box_date = v.findViewById(R.id.select_box_date);
//        datePickerCreate();
//        select_box_date.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                datePickerDialog.show();
//            }
//        });

//        setSearchResultTxt("");
        JWSharePreference sharePreference = new JWSharePreference();
        String name = sharePreference.getString(JWSharePreference.PREFERENCE_LOGIN_NAME, "");
        txt_select_result_default.setText(getResources().getString(R.string.home_tooltip1)+ " " + name + getResources().getString(R.string.home_tooltip));

        scrollView = v.findViewById(R.id.scrollView);

        edt_keyword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    scrollView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                        @Override
                        public void onGlobalLayout() {
                            if(getActivity()!= null){
                                (getActivity()).runOnUiThread(new Runnable(){
                                    @Override
                                    public void run() {
                                        scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                                    }
                                });
                            }
                        }
                    });

                }
            }
        });


    }

    private int bottomButtonIndex = 0;
    private void bottomButtonAction(){
        if(bottomButtonIndex == 1){
            btn_bottom_left.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.black));
            btn_bottom_left.setTextColor(ContextCompat.getColor(requireContext(),R.color.mong_grey_default));
            btn_bottom_right.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.mong_grey_default));
            btn_bottom_right.setTextColor(ContextCompat.getColor(requireContext(),R.color.white));
        }else{
            btn_bottom_right.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.black));
            btn_bottom_right.setTextColor(ContextCompat.getColor(requireContext(),R.color.mong_grey_default));
            btn_bottom_left.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.mong_grey_default));
            btn_bottom_left.setTextColor(ContextCompat.getColor(requireContext(),R.color.white));

        }

    }

    private NestedScrollView scrollView;
    private void setTextWatcher(){
        edt_keyword.addTextChangedListener(new TextWatcher(){
            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
//                boolean valid = Util.getInstance(CheckPasswordChangeActivity.this).validPW(arg0.toString());
                String length = arg0.toString().length()+"";
                inputLength.setText(length);
            }
            @Override
            public void afterTextChanged(Editable arg0) {
            }
            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }
        });
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.btn_keyword_selectbox:
                mCurrentTabIndex = 0;
                txt_keyword1.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
                txt_keyword2.setTextColor(ContextCompat.getColor(getActivity(), R.color.app_disable_text_color));
                view_under_line1.setVisibility(View.VISIBLE);
                view_under_line2.setVisibility(View.INVISIBLE);
                home_content_select.setVisibility(View.VISIBLE);
                home_content_input.setVisibility(View.GONE);
                break;
            case R.id.btn_keyword_inputbox:
                mCurrentTabIndex = 1;
                txt_keyword2.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
                txt_keyword1.setTextColor(ContextCompat.getColor(getActivity(), R.color.app_disable_text_color));
//                view_under_line2.setVisibility(View.VISIBLE);
                view_under_line1.setVisibility(View.INVISIBLE);
                home_content_input.setVisibility(View.VISIBLE);
                home_content_select.setVisibility(View.GONE);
                break;
            case R.id.select_box_1:
                getDataSelectBox1();
                break;
            case R.id.select_box_2:
                if(select_box_txt_1.getText().toString().equals(""))
                    return;
                getDataSelectBox2();
                break;
            case R.id.select_box_3:
                if(select_box_txt_2.getText().toString().equals(""))
                    return;
                getDataSelectBox3();
                break;
            case R.id.select_box_4:
                if(select_box_txt_3.getText().toString().equals(""))
                    return;
                getDataSelectBox4();
                break;
        }
    }

    private void getDataSelectBox1(){
        mSelectData1 = new ArrayList<>();
        ActionRuler.getInstance().addAction(new ActionRequestSelectData(getActivity(),0,null,null,null,null, new ActionResultListener<MongSelectDataResult>() {
            @Override
            public void onResponseResult(MongSelectDataResult response) {
                try {
                    MongSelectDataResult result = response;

                    if(result.isSuccess()){
                        JSONArray jsa = new JSONArray(result.getData());
                        for(int i=0; i<jsa.length(); i++){
                            BottomSheetData item = new BottomSheetData(0,jsa.get(i).toString(),false);
                            mSelectData1.add(item);
                        }
                        BottomSheetDefaultListFragment bottomSheetDLFragment = BottomSheetDefaultListFragment.newInstance();
                        bottomSheetDLFragment.setData(mSelectData1);
                        bottomSheetDLFragment.setListener(mBottomSheetEventListener1);
                        bottomSheetDLFragment.setCancelable(false);
                        bottomSheetDLFragment.show(getActivity().getSupportFragmentManager(), "SELECT1");
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

    private void getDataSelectBox2(){
        mSelectData2 = new ArrayList<>();
        ActionRuler.getInstance().addAction(new ActionRequestSelectData(getActivity(),1, select_box_txt_1.getText().toString(),null,null,null, new ActionResultListener<MongSelectDataResult>() {
            @Override
            public void onResponseResult(MongSelectDataResult response) {
                try {
                    MongSelectDataResult result = response;

                    if(result.isSuccess()){
                        Log.d("TEST",result.getData());
                        JSONArray jsa = new JSONArray(result.getData());
                        for(int i=0; i<jsa.length(); i++){
                            BottomSheetData item = new BottomSheetData(0,jsa.get(i).toString(),false);
                            mSelectData2.add(item);
                        }
                        showBottomSheet(mSelectData2,mBottomSheetEventListener2);
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

    private void getDataSelectBox3(){
        mSelectData3 = new ArrayList<>();
        ActionRuler.getInstance().addAction(new ActionRequestSelectData(getActivity(),2, select_box_txt_1.getText().toString(),select_box_txt_2.getText().toString(),null,null, new ActionResultListener<MongSelectDataResult>() {
            @Override
            public void onResponseResult(MongSelectDataResult response) {
                try {
                    MongSelectDataResult result = response;

                    if(result.isSuccess()){
                        Log.d("TEST",result.getData());
                        JSONArray jsa = new JSONArray(result.getData());
                        for(int i=0; i<jsa.length(); i++){
                            BottomSheetData item = new BottomSheetData(0,jsa.get(i).toString(),false);
                            mSelectData3.add(item);
                        }
                        showBottomSheet(mSelectData3,mBottomSheetEventListener3);
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

    private void getDataSelectBox4(){
        mSelectData4 = new ArrayList<>();
        ActionRuler.getInstance().addAction(new ActionRequestSelectData(getActivity(),3, select_box_txt_1.getText().toString(),select_box_txt_2.getText().toString(),select_box_txt_3.getText().toString(),null, new ActionResultListener<MongSelectDataResult>() {
            @Override
            public void onResponseResult(MongSelectDataResult response) {
                try {
                    MongSelectDataResult result = response;

                    if(result.isSuccess()){
                        Log.d("TEST",result.getData());
                        JSONArray jsa = new JSONArray(result.getData());
                        for(int i=0; i<jsa.length(); i++){
                            BottomSheetData item = new BottomSheetData(0,jsa.get(i).toString(),false);
                            mSelectData4.add(item);
                        }
                        showBottomSheet(mSelectData4,mBottomSheetEventListener4);
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

    private void getDataSelectBoxPlay(){
        if(select_box_txt_1.getText().toString().equals("") ||
                select_box_txt_2.getText().toString().equals("") ||
                select_box_txt_3.getText().toString().equals("") ||
                select_box_txt_4.getText().toString().equals("")){
            return;
        }
        ActionRuler.getInstance().addAction(new ActionRequestSelectData(getActivity(),4, select_box_txt_1.getText().toString(),select_box_txt_2.getText().toString(),select_box_txt_3.getText().toString(),select_box_txt_4.getText().toString(), new ActionResultListener<MongSelectDataResult>() {
            @Override
            public void onResponseResult(MongSelectDataResult response) {
                try {
                    MongSelectDataResult result = response;
                    if(result.isSuccess()){
                        Log.d("TEST",result.getData());
                        JSONArray jsa = new JSONArray(result.getData());
                        for(int i=0; i<jsa.length(); i++){
                            setSearchResultTxt(jsa.get(0).toString());
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

    private void setSearchResultTxt(String txt){
        Log.v("ifeelbluu", "txt :: " + txt);
        ((MainActivity)getActivity()).setMainTopText(txt);
        if(txt.equals("")){
            txt_select_result.setVisibility(View.GONE);
            txt_select_result_default.setVisibility(View.VISIBLE);
        }else{
            txt_select_result.setVisibility(View.VISIBLE);
            txt_select_result_default.setVisibility(View.GONE);
            txt_select_result.setText(txt);
        }

    }

    public void showBottomSheet(ArrayList<BottomSheetData> data, BottomSheetDefaultListFragment.bottomSheetListener listener){
        BottomSheetDefaultListFragment bottomSheetDLFragment = BottomSheetDefaultListFragment.newInstance();
        bottomSheetDLFragment.setData(data);
        bottomSheetDLFragment.setListener(listener);
        bottomSheetDLFragment.setCancelable(false);
        bottomSheetDLFragment.show(getActivity().getSupportFragmentManager(), "SELECT2");
    }

    private ArrayList<BottomSheetData> mSelectData1,mSelectData2,mSelectData3,mSelectData4,mSelectDataStory;

    private BottomSheetDefaultListFragment.bottomSheetListener mBottomSheetEventListener1 = new BottomSheetDefaultListFragment.bottomSheetListener() {
        @Override
        public void onSelectItem(int type, int prev) {
            if(prev != -1)
                mSelectData1.get(prev).setCheck(false);
            mSelectData1.get(type).setCheck(true);
            select_box_txt_1.setText(mSelectData1.get(type).getItemTitle());
            select_box_txt_2.setText("");
            select_box_txt_3.setText("");
            select_box_txt_4.setText("");
        }
    };
    private BottomSheetDefaultListFragment.bottomSheetListener mBottomSheetEventListener2 = new BottomSheetDefaultListFragment.bottomSheetListener() {
        @Override
        public void onSelectItem(int type, int prev) {
            if(prev != -1)
                mSelectData2.get(prev).setCheck(false);
            mSelectData2.get(type).setCheck(true);
            select_box_txt_2.setText(mSelectData2.get(type).getItemTitle());

            select_box_txt_3.setText("");
            select_box_txt_4.setText("");
        }
    };
    private BottomSheetDefaultListFragment.bottomSheetListener mBottomSheetEventListener3 = new BottomSheetDefaultListFragment.bottomSheetListener() {
        @Override
        public void onSelectItem(int type, int prev) {
            if(prev != -1)
                mSelectData3.get(prev).setCheck(false);
            mSelectData3.get(type).setCheck(true);
            select_box_txt_3.setText(mSelectData3.get(type).getItemTitle());
            select_box_txt_4.setText("");
        }
    };
    private BottomSheetDefaultListFragment.bottomSheetListener mBottomSheetEventListener4 = new BottomSheetDefaultListFragment.bottomSheetListener() {
        @Override
        public void onSelectItem(int type, int prev) {
            if(prev != -1)
                mSelectData4.get(prev).setCheck(false);
            mSelectData4.get(type).setCheck(true);
            select_box_txt_4.setText(mSelectData4.get(type).getItemTitle());
        }
    };

    public void storySearchPlay(){
        //edt_keyword.setText("집에가는길에 돈을주워서 떡사먹었는데 맛있었음");
        String keyword = edt_keyword.getText().toString();

        if(keyword.length() < 20){
            CommandUtil.getInstance().showCommonOneButtonDialog(getActivity(),"꿈 내용은 20글자 이상 입력해 주시기 바랍니다.","확인", CommonPopupDialog.COMMON_DIALOG_OPTION_CLOSE_DIALOG,null);
            return;
        }
        if(keyword.length() > 200){
            CommandUtil.getInstance().showCommonOneButtonDialog(getActivity(),"꿈 내용은 200글자 이내로 입력해 주시기 바랍니다.","확인", CommonPopupDialog.COMMON_DIALOG_OPTION_CLOSE_DIALOG,null);
            return;
        }

        //String date = select_box_data.getText().toString().replaceAll("년","").replaceAll("월","").replaceAll("일","").replaceAll(" ","");
        String date = "2022";

        if(date.length() != 4){
            CommandUtil.getInstance().showCommonOneButtonDialog(getActivity(),"꿈 꾼 년도를 입력해 주세요.","확인", CommonPopupDialog.COMMON_DIALOG_OPTION_CLOSE_DIALOG,null);
            return;
        }
        Log.v("ifeelbluu", "date :: " +date.substring(0,4));
        ActionRuler.getInstance().addAction(new ActionRequestStorySearch(getActivity(),"",keyword ,"", new ActionResultListener<DefaultResult>() {
            @Override
            public void onResponseResult(DefaultResult response) {

                try {
                    DefaultResult result = response;
                    if(result.isSuccess()){
                        Log.d("TEST",result.getData());
                        //해초추천 초기화
                        Constants.setRandomCardPickInit();


                        Constants.StoryJob = new JSONObject(result.getData());
                        Log.d("CHECK@",result.getData().toString());
//                        setSearchResultTxt(Constants.StoryJob.getString("MongHaeInfo"));
                        String MongSeq = Constants.StoryJob.getString("MongSeq");
                        ((MainActivity)getActivity()).setMainTopText(Constants.StoryJob.getString("MongHaeInfo"));
                        onResumeAgree = true;
                        Intent i = new Intent(getActivity(), MongStoreSaveActivity.class);
                        i.putExtra("MongSeq",MongSeq);
                        startActivity(i);

                    }else{
                        StorySearchError(0,"result :: " + result.isSuccess());
                    }

                } catch (Exception e) {
                    StorySearchError(1, e.getMessage());
                    e.printStackTrace();
                }
            }

            @Override
            public void onResponseError(String message) {
                StorySearchError(2,message);
            }
        }));
        ActionRuler.getInstance().runNext();
    }

    private void StorySearchError(int errorPoint,String msg){
        Log.e("HOME","마이몽 꿈 분석 ERROR[" + errorPoint+"] " + msg);
        CommandUtil.getInstance().showCommonOneButtonDefaultDialog(getActivity(),"마이몽 꿈 분석에 실패했습니다.\n꿈 내용을 다시 확인해주세요!");
    }

    private boolean onResumeAgree = false;
    public static boolean MONG_DETAIL_ACTION = false;
    @Override
    public void onResume() {
        super.onResume();
        Log.v("ifeelbluu","onResumeAgree :: " + onResumeAgree );
        if(onResumeAgree == true){
            onResumeAgree = false;
            if(MONG_DETAIL_ACTION){
                MONG_DETAIL_ACTION = false;
                mSelectDataStory = new ArrayList<>();
                BottomSheetData item = new BottomSheetData(0,"세부분석 보기",false);
                mSelectDataStory.add(item);
                item = new BottomSheetData(1,"인증등록",false);
                mSelectDataStory.add(item);
                item = new BottomSheetData(2,"닫기",false);
                mSelectDataStory.add(item);

                bottomSheetDLFragment = BottomSheetDefaultListFragment.newInstance();
                bottomSheetDLFragment.setData(mSelectDataStory);
                bottomSheetDLFragment.setListener(mBottomSheetEventListener_Story);
                bottomSheetDLFragment.setDisable(true);
                bottomSheetDLFragment.setCancelable(false);
                bottomSheetDLFragment.show(getActivity().getSupportFragmentManager(), "SELECT_STORY");
//                Intent intent = new Intent(getContext(), ManageDetailActivity.class);
//                startActivity(intent);

            }

        }
        Log.v("ifeelbluu", "requestCode :: onResume");
    }

    public void datePickerCreate(){
        Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        birthday = changeBirthdayToString(mYear, mMonth, mDay);
        //select_box_data.setText(birthday);

        datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                birthday = changeBirthdayToString(year, month, dayOfMonth);
                //select_box_data.setText(birthday);
            }
        }, mYear, mMonth, mDay);
    }

    public String changeBirthdayToString(int year, int month, int dayOfMonth){
        String monthText = "";
        String dayText = "";
        if(month<9){
            monthText = "0"+(month+1);
        }else{
            monthText = String.valueOf(month+1);
        }
        if(dayOfMonth<10){
            dayText = "0"+dayOfMonth;
        }else{
            dayText = String.valueOf(dayOfMonth);
        }

        return ""+year+"년 "+monthText+ "월 " +dayText+"일";
    }


    private BottomSheetDefaultListFragment.bottomSheetListener mBottomSheetEventListener_Story = new BottomSheetDefaultListFragment.bottomSheetListener() {
        @Override
        public void onSelectItem(int type, int prev) {
            if(type == 0){
                Intent i = new Intent(getActivity(), MymongDetailActivity.class);
                startActivity(i);
                edt_keyword.setText("");
            }else if(type == 1){
                if(Constants.isBioMetric){
                    String isFinger = sharePreference.getString(PREFERENCE_AUTO_FINGER_LOGIN, "N");
                    if(isFinger.equals("Y")){
                        auth();
                        return;
                    }
                }
                showCustomDialog();

            }else {

            }
//            select_box_txt_4.setText(mSelectDataStory.get(type).getItemTitle());
        }
    };

    public void showCustomDialog(){
        PwAuthDialog dialog = new PwAuthDialog(getActivity(), new PwAuthDialog.CustomDialogClickListener() {
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
        ActionRuler.getInstance().addAction(new ActionRequestLogin(getActivity(), sharePreference.getString(JWSharePreference.PREFERENCE_LOGIN_ID,""), pw, new ActionResultListener<LoginResult>() {
            @Override
            public void onResponseResult(LoginResult response) {

                LoginResult result = response;
                if(result.isResult()){
                    //인증등록 Intent
                    edt_keyword.setText("");
                    Intent i = new Intent(getActivity(), MyMongRegistActivity.class);
                    startActivity(i);
                    bottomSheetDLFragment.dismiss();
                }else{
                    Log.d("!TEST","비밀번호가 일치하지 않습니다.");

                }
            }

            @Override
            public void onResponseError(String message) { }
        }));
        ActionRuler.getInstance().runNext();
    }

    private boolean keyboardListenersAttached = false;
    private ViewGroup rootLayout;
    private ViewTreeObserver.OnGlobalLayoutListener keyboardLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            int heightDiff = scrollView.getRootView().getHeight() - scrollView.getHeight();
            int contentViewTop = getActivity().getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();

            if(heightDiff <= contentViewTop){
                scrollView.post(new Runnable() {
                    @Override
                    public void run() {
                        scrollView.fullScroll(ScrollView.FOCUS_UP);
                    }
                });
            }
            else {
                //int keyboardHeight = heightDiff - contentViewTop;

                rootLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                        scrollView.scrollTo(0,2000);
                    }
                });
            }
        }
    };

    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;
    public void init(){
        executor = ContextCompat.getMainExecutor(getActivity().getApplicationContext());
        biometricPrompt = new BiometricPrompt(getActivity(),
                executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(getActivity(),"Error", Toast.LENGTH_SHORT)
                        .show();
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);

                Intent i = new Intent(getActivity(), MyMongRegistActivity.class);
                startActivity(i);
                bottomSheetDLFragment.dismiss();

            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(getActivity(), "nFailed", Toast.LENGTH_SHORT)
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

    public class HomePagerAdapter extends FragmentStateAdapter{

        public int mCount;

        public HomePagerAdapter(@NonNull Fragment fragment, int count) {
            super(fragment);
            mCount = count;
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            int index = position;

            if(index==0) return new HomeLeftFragment();
            else return new HomeRightFragment();
        }

        @Override
        public int getItemCount() {
            return mCount;
        }
    }

}