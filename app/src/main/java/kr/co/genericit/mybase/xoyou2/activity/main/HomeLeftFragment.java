package kr.co.genericit.mybase.xoyou2.activity.main;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import org.json.JSONObject;

import java.util.ArrayList;

import kr.co.genericit.mybase.xoyou2.R;
import kr.co.genericit.mybase.xoyou2.activity.MainActivity;
import kr.co.genericit.mybase.xoyou2.activity.ManageDetailActivity;
import kr.co.genericit.mybase.xoyou2.activity.MongStoreSaveActivity;
import kr.co.genericit.mybase.xoyou2.common.Constants;
import kr.co.genericit.mybase.xoyou2.network.action.ActionRuler;
import kr.co.genericit.mybase.xoyou2.network.interfaces.ActionResultListener;
import kr.co.genericit.mybase.xoyou2.network.request.ActionRequestStorySearch;
import kr.co.genericit.mybase.xoyou2.network.response.DefaultResult;
import kr.co.genericit.mybase.xoyou2.utils.CommandUtil;
import kr.co.genericit.mybase.xoyou2.view.CommonPopupDialog;
import kr.co.genericit.mybase.xoyou2.view.bottomsheet.model.BottomSheetData;


public class HomeLeftFragment extends Fragment {
    private boolean onResumeAgree = false;
    public static boolean MONG_DETAIL_ACTION = false;

    private EditText edt_keyword;
    private TextView inputLength;
    private RelativeLayout frag_home_submit;
    private ImageView frag_home_refresh;
    private ArrayList<BottomSheetData> mSelectDataStory;
    @Override
    public void onResume() {
        super.onResume();
        Log.v("ifeelbluu","onResumeAgree :: 2"+ onResumeAgree);
        if(onResumeAgree == true){
            onResumeAgree = false;
            if(MONG_DETAIL_ACTION){
//                MONG_DETAIL_ACTION = false;
//                mSelectDataStory = new ArrayList<>();
//                BottomSheetData item = new BottomSheetData(0,"세부분석 보기",false);
//                mSelectDataStory.add(item);
//                item = new BottomSheetData(1,"인증등록",false);
//                mSelectDataStory.add(item);
//                item = new BottomSheetData(2,"닫기",false);
//                mSelectDataStory.add(item);
//
//                bottomSheetDLFragment = BottomSheetDefaultListFragment.newInstance();
//                bottomSheetDLFragment.setData(mSelectDataStory);
//                bottomSheetDLFragment.setListener(mBottomSheetEventListener_Story);
//                bottomSheetDLFragment.setDisable(true);
//                bottomSheetDLFragment.setCancelable(false);
//                bottomSheetDLFragment.show(getActivity().getSupportFragmentManager(), "SELECT_STORY");
                Intent intent = new Intent(getContext(), ManageDetailActivity.class);
                startActivity(intent);

            }

        }
        Log.v("ifeelbluu", "requestCode :: onResume");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_left, container, false);

        initView(view);

        return view;
    }

    public void initView(View v){
        edt_keyword = v.findViewById(R.id.edt_keyword);
        frag_home_submit = v.findViewById(R.id.frag_home_submit);
        inputLength = v.findViewById(R.id.inputLength);
        frag_home_refresh = v.findViewById(R.id.frag_home_refresh);



        edt_keyword.addTextChangedListener(new TextWatcher(){
            private int length = 0;
            private String lengthString = "";

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                length =edt_keyword.getText().toString().length();

                if(length>=20){
                    frag_home_submit.setVisibility(View.VISIBLE);
                }else{
                    frag_home_submit.setVisibility(View.GONE);
                }

                lengthString = String.valueOf(length);
                inputLength.setText(lengthString);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        frag_home_submit.setOnClickListener(view ->{
            storySearchPlay();
        });

        frag_home_refresh.setOnClickListener(view ->{
            edt_keyword.setText("");
        });
    }

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
                        onResumeAgree = true;
                        Constants.StoryJob = new JSONObject(result.getData());
                        Log.d("CHECK@",result.getData().toString());
                        //setSearchResultTxt(Constants.StoryJob.getString("MongHaeInfo"));
                        String MongSeq = Constants.StoryJob.getString("MongSeq");
                        ((MainActivity)getActivity()).setMainTopText(Constants.StoryJob.getString("MongHaeInfo"));
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
}