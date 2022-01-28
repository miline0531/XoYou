package kr.co.genericit.mybase.xoyou2.activity.main;

import static kr.co.genericit.mybase.xoyou2.storage.JWSharePreference.PREFERENCE_AUTO_FINGER_LOGIN;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.AppBarLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.Executor;

import kr.co.genericit.mybase.xoyou2.R;
import kr.co.genericit.mybase.xoyou2.activity.MainActivity;
import kr.co.genericit.mybase.xoyou2.activity.ManageDetailActivity;
import kr.co.genericit.mybase.xoyou2.activity.MymongBidActivity;
import kr.co.genericit.mybase.xoyou2.activity.detail.MyMongRegistActivity;
import kr.co.genericit.mybase.xoyou2.activity.detail.MymongDetailActivity;
import kr.co.genericit.mybase.xoyou2.adapter.ManageHorizontalRecyclerviewAdapter;
import kr.co.genericit.mybase.xoyou2.adapter.ManageVerticalRecyclerviewAdapter;
import kr.co.genericit.mybase.xoyou2.common.Constants;
import kr.co.genericit.mybase.xoyou2.model.Mong;
import kr.co.genericit.mybase.xoyou2.network.action.ActionRuler;
import kr.co.genericit.mybase.xoyou2.network.interfaces.ActionResultListener;
import kr.co.genericit.mybase.xoyou2.network.request.ActionRequestLogin;
import kr.co.genericit.mybase.xoyou2.network.request.ActionRequestMongStoreSave;
import kr.co.genericit.mybase.xoyou2.network.request.ActionRequestMyMongList;
import kr.co.genericit.mybase.xoyou2.network.request.ActionRequestStoreList;
import kr.co.genericit.mybase.xoyou2.network.request.ActionRequestStorySearch;
import kr.co.genericit.mybase.xoyou2.network.response.DefaultResult;
import kr.co.genericit.mybase.xoyou2.network.response.LoginResult;
import kr.co.genericit.mybase.xoyou2.network.response.MyMongResult;
import kr.co.genericit.mybase.xoyou2.network.response.StoreListResult;
import kr.co.genericit.mybase.xoyou2.storage.JWSharePreference;
import kr.co.genericit.mybase.xoyou2.utils.CommandUtil;
import kr.co.genericit.mybase.xoyou2.utils.LogUtil;
import kr.co.genericit.mybase.xoyou2.utils.RecyclerDecoration;
import kr.co.genericit.mybase.xoyou2.view.PwAuthDialog;
import kr.co.genericit.mybase.xoyou2.view.bottomsheet.BottomSheetDefaultListFragment;
import kr.co.genericit.mybase.xoyou2.view.bottomsheet.model.BottomSheetData;


public class ManageFragment extends Fragment {
    private Context mContext;
    private String mCurrentTabIndex = "1";
    private NestedScrollView scrollView;
    private JWSharePreference jwSharePreference;
    private ArrayList<Mong> eventList,auctionList;
    private boolean isLoading = false;
    private int currentPage = 0;
    private int selectedItem = -1;

    private ArrayList<BottomSheetData> mSelectDataStory;
    private BottomSheetDefaultListFragment bottomSheetDLFragment;
    private final JWSharePreference sharePreference = new JWSharePreference();
    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        //context가져오기
        mContext = context;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity)getActivity()).visibleFloatingButton(View.VISIBLE);

        if(isBidMove){
            isBidMove = false;
            requestMyMongList(mCurrentTabIndex,data);
        }

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_manage, container, false);

        eventList = new ArrayList<>();
        auctionList = new ArrayList<>();



        jwSharePreference = new JWSharePreference();
        rcv_vertical = v.findViewById(R.id.rcv_manage_list_vertical);
        rcv_horizontal_1 = v.findViewById(R.id.rcv_manage_list_horizontal1);
        rcv_horizontal_2 = v.findViewById(R.id.rcv_manage_list_horizontal2);

        setRecycler();

        btn1 = v.findViewById(R.id.btn_manage1);
        btn2 = v.findViewById(R.id.btn_manage2);
        btn3 = v.findViewById(R.id.btn_manage3);
        btn4 = v.findViewById(R.id.btn_manage4);

        initData();
        setOnClickBtn();
        setTabLayout(v);

        v.findViewById(R.id.btn_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).MainOpenDrawer();
            }
        });

        AppBarLayout collapsingView = v.findViewById(R.id.collapsingView);
        collapsingView.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                int visible = 0;
                if (Math.abs(verticalOffset)-appBarLayout.getTotalScrollRange() == 0){
                    visible = View.GONE;
                }else{
                    visible = View.VISIBLE;
                }
                ((MainActivity)getActivity()).visibleFloatingButton(visible);
            }
        });

        TextView top_result = v.findViewById(R.id.top_result);
        String resultStr = "";
        try {
            if(Constants.MainData != null)
            resultStr = Constants.MainData.getString("ManageTitle");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        top_result.setText(resultStr);
        return v;
    }

    public void initData(){
        requestMyMongList(mCurrentTabIndex,data);
        requestStoreList();
//        initBottomSheet();
        initBioAuth();
    }
    public void initBottomSheet(){
        Intent intent = new Intent(getContext(), ManageDetailActivity.class);
        startActivity(intent);

//        mSelectDataStory = new ArrayList<>();
//        BottomSheetData item = new BottomSheetData(0,"세부분석 보기",false);
//        mSelectDataStory.add(item);
//        item = new BottomSheetData(1,"인증등록",false);
//        mSelectDataStory.add(item);
//        item = new BottomSheetData(2,"판매하기",false);
//        mSelectDataStory.add(item);
//        item = new BottomSheetData(3,"닫기",false);
//        mSelectDataStory.add(item);
//
//        bottomSheetDLFragment = BottomSheetDefaultListFragment.newInstance();
//        bottomSheetDLFragment.setData(mSelectDataStory);
        bottomSheetDLFragment.setListener(mBottomSheetEventListener);
//        bottomSheetDLFragment.setDisable(true);
//        bottomSheetDLFragment.setCancelable(false);
    }

    public void initBioAuth(){
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

    private BottomSheetDefaultListFragment.bottomSheetListener mBottomSheetEventListener = new BottomSheetDefaultListFragment.bottomSheetListener() {
        @Override
        public void onSelectItem(int type, int prev) {


            switch (type){
                case 0 : //세부분석보기
                    requestStoryResult(type);
                    break;
                case 1 : //인증
                    requestStoryResult(type);
                    break;
                case 2 :  //판매하기
                    Intent i = new Intent(getActivity(), MymongBidActivity.class);
                    i.putExtra("SEQ",data.get(selectedItem).getSTORE_ID());
                    startActivity(i);
                    isBidMove = true;
                    break;
                case 3 :
                    bottomSheetDLFragment.dismiss();
                    break;

            }
            mSelectDataStory.get(type).setCheck(false);

        }
    };

    public void auth(){
        biometricPrompt.authenticate(promptInfo);
    }

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
    private void requestStoreList(){
        Log.d("CHECK@MANAGE","storeList호출");
        jwSharePreference = new JWSharePreference();
        int userId = jwSharePreference.getInt(JWSharePreference.PREFERENCE_SRL,0);
        ActionRuler.getInstance().addAction(new ActionRequestStoreList(getActivity(),String.valueOf(userId), new ActionResultListener<StoreListResult>() {

            @Override
            public void onResponseResult(StoreListResult response) {
                try {
                    StoreListResult result = response;
                    Log.d("TEST",result.getData().toString());

                    if(result.isSuccess()){
                        eventList.clear();
                        eventList.addAll(stringToArrayList(result.getData(),"event"));
                        auctionList.clear();
                        auctionList.addAll(stringToArrayList(result.getData(),"auction"));
                        adapter_horizontal1.notifyDataSetChanged();
                        adapter_horizontal2.notifyDataSetChanged();
                    }else{
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onResponseError(String message) {
                //Toast.makeText(getContext(), "꿈 가져오기 실패.\n잠시 후 다시 시도해주세요.",Toast.LENGTH_SHORT).show();
                LogUtil.LogD("꿈 가져오기 실패!!!!");
            }
        }));
        ActionRuler.getInstance().runNext();
    }
    public ArrayList<Mong> stringToArrayList(String data, String type) throws JSONException {
        Log.v("ifeelbluu", "data :: " + data);
        Log.v("ifeelbluu", "type :: " + type);
        JSONObject object = new JSONObject(data);
        JSONArray jsonArray = new JSONArray(object.getString("list_"+type+"_store"));
        ArrayList<Mong> arrayList = new ArrayList<>();
        for(int i=0; i<jsonArray.length(); i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            Mong mong = new Mong();
            //TODO 데이터 추가

            mong.setSEQ(jsonObject.getInt("SEQ"));
            mong.setUSER_ID(jsonObject.getString("USER_ID"));
            mong.setUSER_NAME(jsonObject.getString("USER_NAME"));
            mong.setTITLE(jsonObject.getString("TITLE"));
            mong.setNOTE(jsonObject.getString("NOTE"));
            mong.setMONG_DATE(jsonObject.getString("MONG_DATE"));
            mong.setSTATUS_CODE(jsonObject.getInt("STATUS_CODE"));
            mong.setMONG_TYPE(jsonObject.getString("MONG_TYPE"));
            mong.setTHEME_TYPE(jsonObject.getString("THEME_TYPE"));
            mong.setSTART_DATE(jsonObject.getString("START_DATE"));
            mong.setEND_DATE(jsonObject.getString("END_DATE"));
            mong.setMAX_PRICE(jsonObject.getString("MAX_PRICE"));
            mong.setMIN_PRICE(jsonObject.getString("MIN_PRICE"));
            mong.setMONG_PRICE(jsonObject.getString("MONG_PRICE"));
            mong.setSTORE_ID(jsonObject.getString("STORE_ID"));
            mong.setMONG_TOKEN_ID(jsonObject.getString("MONG_TOKEN_ID"));
            mong.setMONG_CERT(jsonObject.getString("MONG_CERT"));
            mong.setIMAGE_URL(jsonObject.getString("IMAGE_URL"));
            mong.setSELL_TYPE(jsonObject.getString("SELL_TYPE"));
            mong.setBUY_TYPE(jsonObject.getString("BUY_TYPE"));
            mong.setBID_VALUE(jsonObject.getString("BID_VALUE"));
            mong.setBID_COUNT(jsonObject.getString("BID_COUNT"));
            mong.setTRANS_ID(jsonObject.getInt("TRANS_ID"));
            mong.setTRANS_TYPE(jsonObject.getInt("TRANS_TYPE"));
            mong.setTRANS_STATUS(jsonObject.getInt("TRANS_STATUS"));
            mong.setJUSEO_SEQ(jsonObject.getInt("JUSEO_SEQ"));
            mong.setUSE_YN(jsonObject.getString("USE_YN"));
            mong.setREG_DATE(jsonObject.getString("REG_DATE"));
            mong.setUPD_DATE(jsonObject.getString("UPD_DATE"));
            mong.setJUSEO_DATA(jsonObject.getString("JUSEO_DATA"));
            mong.setMONG_JST(jsonObject.getString("MONG_JST"));
            mong.setVIEW_TYPE(jsonObject.getString("VIEW_TYPE"));
            mong.setCURRENT_PAGE(jsonObject.getInt("CURRENT_PAGE"));

            Log.v("ifeelbluu", "mong :: " +mong.getIMAGE_URL());
            arrayList.add(mong);
        }

        return arrayList;
    }
    private void requestPwAuth(String pw){
        final JWSharePreference sharePreference = new JWSharePreference();
        ActionRuler.getInstance().addAction(new ActionRequestLogin(getActivity(), sharePreference.getString(JWSharePreference.PREFERENCE_LOGIN_ID,""), pw, new ActionResultListener<LoginResult>() {
            @Override
            public void onResponseResult(LoginResult response) {

                LoginResult result = response;
                if(result.isResult()){
                    //인증등록 Intent
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
    private void mongStoreSave(){
        ActionRuler.getInstance().addAction(new ActionRequestMongStoreSave(getActivity(), Constants.mongSEQ, new ActionResultListener<DefaultResult>() {
            @Override
            public void onResponseResult(DefaultResult response) {
                try {
                    DefaultResult result = response;
                    Log.v("ifeelbluu", "result.isSuccess() :: " + result.isSuccess());
                    if(result.isSuccess()){
                        Constants.MONG_SRL = result.getData();

                        Intent intent = new Intent(getContext(), ManageDetailActivity.class);
                        startActivity(intent);
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
    private void requestStoryResult(int type){
        Log.d("TEST@","storeId : " + data.get(selectedItem).getSTORE_ID());
        ActionRuler.getInstance().addAction(new ActionRequestStorySearch(getActivity(),data.get(selectedItem).getSTORE_ID(),
                "" ,"", new ActionResultListener<DefaultResult>() {
            @Override
            public void onResponseResult(DefaultResult response) {

                try {
                    DefaultResult result = response;
                    if(result.isSuccess()){
                        //해초추천 초기화
                        Constants.setRandomCardPickInit();

                        Constants.StoryJob = new JSONObject(result.getData());
                        Constants.mongSEQ = data.get(selectedItem).getSTORE_ID();
                        mongStoreSave();

                        switch (type){
                            case 0:
                                Intent i = new Intent(getActivity(), MymongDetailActivity.class);
                                startActivity(i);
                                break;
                            case 1:
                                if(Constants.isBioMetric){
                                    String isFinger = sharePreference.getString(PREFERENCE_AUTO_FINGER_LOGIN, "N");
                                    if(isFinger.equals("Y")){
                                        auth();
                                        return;
                                    }
                                }
                                showCustomDialog();
                                break;
                        }

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

    private void requestStoryResult2(){
        Log.d("TEST@","storeId : " + data.get(selectedItem).getSTORE_ID());
        ActionRuler.getInstance().addAction(new ActionRequestStorySearch(getActivity(),data.get(selectedItem).getSEQ()+"",
                "" ,"", new ActionResultListener<DefaultResult>() {
            @Override
            public void onResponseResult(DefaultResult response) {

                try {
                    DefaultResult result = response;
                    if(result.isSuccess()){
                        //해초추천 초기화
                        Constants.setRandomCardPickInit();

                        Constants.StoryJob = new JSONObject(result.getData());

                        Constants.mongSEQ = data.get(selectedItem).getSEQ() + "";
//                        mongStoreSave();

                        Constants.MONG_SRL = data.get(selectedItem).getSTORE_ID();
                        Log.v("ifeelbluu","Constants.mongSEQ :: " + Constants.mongSEQ);
                        Log.v("ifeelbluu","Constants.MONG_SRL :: " + Constants.MONG_SRL);
                        Log.v("ifeelbluu","Constants.MONG_SRL :: " + Constants.MONG_SRL);
                        Intent intent = new Intent(getContext(), ManageDetailActivity.class);
                        startActivity(intent);


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
    private boolean isBidMove = false;
    public ManageVerticalRecyclerviewAdapter.listOnClickListener onClick = new ManageVerticalRecyclerviewAdapter.listOnClickListener() {
        @Override
        public void onClickItem(int id, int action) {
            if(Integer.parseInt(mCurrentTabIndex)==1){
                selectedItem = id;

                Log.v("ifeelbluu"," dddddd ::id " + data.get(selectedItem).getSTORE_ID());
                Log.v("ifeelbluu"," dddddd ::type " + data.get(selectedItem).getTRANS_TYPE());
                Log.v("ifeelbluu"," dddddd ::status " + data.get(selectedItem).getTRANS_STATUS());
                if(data.get(selectedItem).getTRANS_STATUS() == 1){
                    requestStoryResult2();
                }else{
                    Intent i = new Intent(getActivity(), MymongBidActivity.class);
                    i.putExtra("SEQ",data.get(id).getSTORE_ID());
                    startActivity(i);
                }
            }else{
                Intent i = new Intent(getActivity(), MymongBidActivity.class);
                i.putExtra("SEQ",data.get(id).getSTORE_ID());
                startActivity(i);
                isBidMove = true;
            }
        }
    };


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    //Manage화면
    private RecyclerView rcv_vertical,rcv_horizontal_1,rcv_horizontal_2;
    private ManageHorizontalRecyclerviewAdapter adapter_horizontal1;
    private ManageHorizontalRecyclerviewAdapter adapter_horizontal2;
    private ManageVerticalRecyclerviewAdapter adapter_vertical;
    private LinearLayout btn1,btn2,btn3,btn4;
    private ArrayList<Mong> data = new ArrayList<>();
    public void setRecycler(){
        adapter_horizontal1 = new ManageHorizontalRecyclerviewAdapter(mContext,eventList);
        adapter_horizontal1.setListOnClickListener(hOnclick);
        rcv_horizontal_1.setAdapter(adapter_horizontal1);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1, GridLayoutManager.HORIZONTAL, false);
        rcv_horizontal_1.setLayoutManager(gridLayoutManager);
        RecyclerDecoration spaceDecoration = new RecyclerDecoration(10,"singleLineHorizontal");
        rcv_horizontal_1.addItemDecoration(spaceDecoration);

        adapter_horizontal2 = new ManageHorizontalRecyclerviewAdapter(mContext,auctionList);
        adapter_horizontal2.setListOnClickListener(hOnclick2);
        rcv_horizontal_2.setAdapter(adapter_horizontal2);
        GridLayoutManager gridLayoutManager2 = new GridLayoutManager(getActivity(), 1, GridLayoutManager.HORIZONTAL, false);
        rcv_horizontal_2.setLayoutManager(gridLayoutManager2);
        RecyclerDecoration spaceDecoration2 = new RecyclerDecoration(10,"singleLineHorizontal");
        rcv_horizontal_2.addItemDecoration(spaceDecoration2);

        rcv_vertical.setLayoutManager(new LinearLayoutManager(mContext));
        adapter_vertical = new ManageVerticalRecyclerviewAdapter(mContext,data);
        adapter_vertical.setManageType(true);
        adapter_vertical.setListOnClickListener(onClick);
        rcv_vertical.setAdapter(adapter_vertical);

        RecyclerDecoration spaceDecoration3 = new RecyclerDecoration(10,"vertical");
        rcv_vertical.addItemDecoration(spaceDecoration3);

        initScrollListener();
    }

    private ArrayList<TextView> tabText;
    private ArrayList<View> tabUnderBar;

    public ManageHorizontalRecyclerviewAdapter.listOnClickListener hOnclick = new ManageHorizontalRecyclerviewAdapter.listOnClickListener() {
        @Override
        public void onClickItem(int id, int action) {
            Intent i = new Intent(getActivity(), MymongBidActivity.class);
            Log.v("ifeelbluu","SEQ : " + eventList.get(id).getSTORE_ID() );
            i.putExtra("SEQ",eventList.get(id).getSTORE_ID()+"");
            startActivity(i);
        }
    };

    public ManageHorizontalRecyclerviewAdapter.listOnClickListener hOnclick2 = new ManageHorizontalRecyclerviewAdapter.listOnClickListener() {
        @Override
        public void onClickItem(int id, int action) {
            Intent i = new Intent(getActivity(), MymongBidActivity.class);
            Log.v("ifeelbluu","SEQ : " + auctionList.get(id).getSTORE_ID() );
            i.putExtra("SEQ",auctionList.get(id).getSTORE_ID()+"");
            startActivity(i);
        }
    };


    public void setOnClickBtn(){

        ArrayList<String> sampleData = new ArrayList<>();
        for(int i=0; i<6; i++){
            sampleData.add(i*100 + "");
        }
        ArrayList<String> sampleData2 = new ArrayList<>();
        for(int i=0; i<6; i++){
            sampleData2.add((i+6)*100 + "");
        }
        ArrayList<String> sampleData3 = new ArrayList<>();
        for(int i=0; i<6; i++){
            sampleData3.add((i+12)*100 + "");
        }
        ArrayList<String> sampleData4 = new ArrayList<>();
        for(int i=0; i<6; i++){
            sampleData4.add((i+18)*100 + "");
        }

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                data.clear();
                int selectIndex = 0;
                switch (view.getId()){
                    case R.id.btn_manage1:
                        mCurrentTabIndex = "1";
                        requestMyMongList(mCurrentTabIndex,data);
                        //data.addAll(dataList1);
                        selectIndex = 0;
                        currentPage = 1;
                        isLoading = false;
                        break;

                    case R.id.btn_manage2:
                        mCurrentTabIndex = "2";
                        requestMyMongList(mCurrentTabIndex,data);
                        //data.addAll(dataList2);
                        selectIndex = 1;
                        currentPage = 1;
                        isLoading = false;
                        break;

                    case R.id.btn_manage3:
                        mCurrentTabIndex = "3";
                        requestMyMongList(mCurrentTabIndex,data);
                       // data.addAll(dataList3);
                        selectIndex = 2;
                        currentPage = 1;
                        isLoading = false;
                        break;

                    case R.id.btn_manage4:
                        mCurrentTabIndex = "4";
                        requestMyMongList(mCurrentTabIndex,data);
                      //  data.addAll(dataList4);
                        selectIndex = 3;
                        currentPage = 1;
                        isLoading = false;

                        break;
                }
                changeTabView(selectIndex);
                adapter_vertical.notifyDataSetChanged();
            }
        };


        btn1.setOnClickListener(onClickListener);
        btn2.setOnClickListener(onClickListener);
        btn3.setOnClickListener(onClickListener);
        btn4.setOnClickListener(onClickListener);


    }

    private void requestReadMoreMyMongList(String viewType,ArrayList<Mong> dataList){
        String userId = String.valueOf(jwSharePreference.getInt(JWSharePreference.PREFERENCE_SRL,0));
        ActionRuler.getInstance().addAction(new ActionRequestMyMongList(getActivity(),userId, viewType,String.valueOf(currentPage), new ActionResultListener<MyMongResult>() {

            @Override
            public void onResponseResult(MyMongResult response) {
                try {
                    MyMongResult result = response;
                    Log.d("TEST",result.getData().toString());

                    if(result.isSuccess()){
                        int positionStart = dataList.size();
                        ArrayList<Mong> newData = stringToArrayList(result.getData());
                        int itemCount = newData.size();
                        dataList.addAll(newData);
                        adapter_vertical.notifyItemRangeInserted(positionStart,itemCount);
                        isLoading = false;
                        currentPage++;
                    }else{

                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onResponseError(String message) {
                //Toast.makeText(getContext(), "꿈 가져오기 실패.\n잠시 후 다시 시도해주세요.",Toast.LENGTH_SHORT).show();
                LogUtil.LogD("꿈 가져오기 실패!!!!");
            }
        }));
        ActionRuler.getInstance().runNext();
    }
    private void requestMyMongList(String viewType,ArrayList<Mong> dataList){
        String userId = String.valueOf(jwSharePreference.getInt(JWSharePreference.PREFERENCE_SRL,0));
        ActionRuler.getInstance().addAction(new ActionRequestMyMongList(getActivity(),userId, viewType,"0", new ActionResultListener<MyMongResult>() {

            @Override
            public void onResponseResult(MyMongResult response) {
                try {
                    MyMongResult result = response;
                    Log.d("TEST",result.getData().toString());

                    if(result.isSuccess()){
                        dataList.clear();
                        dataList.addAll(stringToArrayList(result.getData()));
                        adapter_vertical.notifyDataSetChanged();
                        currentPage++;
                    }else{
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onResponseError(String message) {
                //Toast.makeText(getContext(), "꿈 가져오기 실패.\n잠시 후 다시 시도해주세요.",Toast.LENGTH_SHORT).show();
                LogUtil.LogD("꿈 가져오기 실패!!!!");
            }
        }));
        ActionRuler.getInstance().runNext();
    }
    private void initScrollListener() {
        rcv_vertical.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);


            }


            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (layoutManager != null && layoutManager.findLastCompletelyVisibleItemPosition()==data.size()-1) {
                    //더이상 스크롤 할수 없을때 direction : -1.최상단, 1.최하단
                    //리스트 마지막
                    isLoading = true;
                    requestReadMoreMyMongList(mCurrentTabIndex,data);
                    Log.d("TEST","최하단");
                    Log.d("TEST","position : " + layoutManager.findLastCompletelyVisibleItemPosition());

                }
            }
        });
    }

    private void setTabLayout(View v){
        tabText = new ArrayList<>();
        tabUnderBar = new ArrayList<>();

        for(int i=0; i<4; i++){
            TextView item = v.findViewById(findRes("txt_keyword"+(i+1), "id"));
            tabText.add(item);
            View item2 = v.findViewById(findRes("view_under_line"+(i+1), "id"));
            tabUnderBar.add(item2);
        }
    }

    private void changeTabView(int index){
        for(int i=0; i<4; i++){
            if(i==index){
                tabText.get(i).setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
                tabUnderBar.get(i).setVisibility(View.VISIBLE);
            }else{
                tabText.get(i).setTextColor(ContextCompat.getColor(getActivity(), R.color.app_disable_text_color));
                tabUnderBar.get(i).setVisibility(View.INVISIBLE);
            }
        }
    }
    //리소스 아이디참조
    public int findRes(String pVariableName, String type) {
        Log.v("ifeelbluu","findRes ::: "  + pVariableName);
        try {
            return mContext.getResources().getIdentifier(pVariableName, type, mContext.getPackageName());
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public ArrayList<Mong> stringToArrayList(String data) throws JSONException {
        JSONArray jsonArray = new JSONArray(data);
        ArrayList<Mong> arrayList = new ArrayList<>();
        for(int i=0; i<jsonArray.length(); i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            Mong mong = new Mong();
            //TODO 데이터 추가
            Log.v("ifeelbluu","Constants.add" + jsonObject.getInt("SEQ"));
            mong.setSEQ(jsonObject.getInt("SEQ"));
            mong.setMONG_TYPE(jsonObject.getString("MONG_TYPE"));
            mong.setUSER_ID(jsonObject.getString("USER_ID"));
            mong.setTITLE(jsonObject.getString("TITLE"));
            mong.setNOTE(jsonObject.getString("NOTE"));
            //mong.setSTATUC_CODE(jsonObject.getInt("STATUC_CODE"));
            mong.setMONG_TYPE(jsonObject.getString("MONG_TYPE"));
            mong.setTHEME_TYPE(jsonObject.getString("THEME_TYPE"));
            mong.setSELL_TYPE(jsonObject.getString("SELL_TYPE"));
            mong.setSTORE_ID(jsonObject.getString("STORE_ID"));
            mong.setSTART_DATE(jsonObject.getString("START_DATE"));
            mong.setEND_DATE(jsonObject.getString("END_DATE"));
            mong.setMAX_PRICE(jsonObject.getString("MAX_PRICE"));
            mong.setMIN_PRICE(jsonObject.getString("MIN_PRICE"));
            mong.setMONG_PRICE(jsonObject.getString("MONG_PRICE"));
            mong.setBID_VALUE(jsonObject.getString("BID_VALUE"));
            mong.setBID_COUNT(jsonObject.getString("BID_COUNT"));
            mong.setIMAGE_URL(jsonObject.getString("IMAGE_URL"));
            mong.setTRANS_STATUS(jsonObject.getInt("TRANS_STATUS"));
            mong.setJUSEO_SEQ(jsonObject.getInt("JUSEO_SEQ"));
            mong.setREG_DATE(jsonObject.getString("REG_DATE"));
            mong.setMONG_JST(jsonObject.getString("MONG_JST"));
            mong.setCURRENT_PAGE(jsonObject.getInt("CURRENT_PAGE"));

            Log.d("TEST","SEQ : " + mong.getSEQ());
            arrayList.add(mong);
        }

        return arrayList;
    }
}