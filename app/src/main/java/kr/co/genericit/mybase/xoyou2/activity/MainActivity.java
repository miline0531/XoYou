package kr.co.genericit.mybase.xoyou2.activity;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import kr.co.genericit.mybase.xoyou2.R;
import kr.co.genericit.mybase.xoyou2.activity.login.LoginActivity;
import kr.co.genericit.mybase.xoyou2.activity.main.FindFragment;
import kr.co.genericit.mybase.xoyou2.activity.main.HomeFragment;
import kr.co.genericit.mybase.xoyou2.activity.main.ManageFragment;
import kr.co.genericit.mybase.xoyou2.activity.main.StoreFragment;
import kr.co.genericit.mybase.xoyou2.adapter.SectionPageAdapter;
import kr.co.genericit.mybase.xoyou2.adapter.SlideMenuAdapter;
import kr.co.genericit.mybase.xoyou2.common.CommonActivity;
import kr.co.genericit.mybase.xoyou2.common.Constants;
import kr.co.genericit.mybase.xoyou2.common.MySQLiteOpenHelper;
import kr.co.genericit.mybase.xoyou2.common.SkyLog;
import kr.co.genericit.mybase.xoyou2.interfaces.DialogClickListener;
import kr.co.genericit.mybase.xoyou2.model.Message;
import kr.co.genericit.mybase.xoyou2.model.SlideMenuData;
import kr.co.genericit.mybase.xoyou2.network.action.ActionRuler;
import kr.co.genericit.mybase.xoyou2.network.interfaces.ActionResultListener;
import kr.co.genericit.mybase.xoyou2.network.model.AddUserInfo;
import kr.co.genericit.mybase.xoyou2.network.request.ActionRequestCoinData;
import kr.co.genericit.mybase.xoyou2.network.request.ActionRequestLocationList;
import kr.co.genericit.mybase.xoyou2.network.request.ActionRequestMainUserInfo;
import kr.co.genericit.mybase.xoyou2.network.request.ActionRequestRelationList;
import kr.co.genericit.mybase.xoyou2.network.request.ActionRequestUserList;
import kr.co.genericit.mybase.xoyou2.network.response.CoinDataResult;
import kr.co.genericit.mybase.xoyou2.network.response.DefaultResult;
import kr.co.genericit.mybase.xoyou2.network.response.LocationListResult;
import kr.co.genericit.mybase.xoyou2.network.response.RelationListResult;
import kr.co.genericit.mybase.xoyou2.network.response.UserListResult;
import kr.co.genericit.mybase.xoyou2.storage.JWSharePreference;
import kr.co.genericit.mybase.xoyou2.utils.BackPressFinishHandler;
import kr.co.genericit.mybase.xoyou2.utils.CommandUtil;
import kr.co.genericit.mybase.xoyou2.utils.LogUtil;
import kr.co.genericit.mybase.xoyou2.utils.RecyclerDecoration;
import kr.co.genericit.mybase.xoyou2.view.CommonPopupDialog;


public class MainActivity extends CommonActivity {
    public static Activity mainAc;
    //슬라이드메뉴
    private final int BTN_COIN_SETTING = 0; //코인
    private final int BTN_USER_SETTING = 1; //사용자
    private final int BTN_RELATION_SETTING = 100; //관계인
    private final int BTN_LOCATION_SETTING = 101; //관계장소
    private final int BTN_FAMILY_SETTING = 102; //가족구성

    private int userCount=0, relationCount=0, locationCount=0;

    private ArrayList<SlideMenuData> mSlideMenuItem1;
    private ArrayList<SlideMenuData> mSlideMenuItem2;
    private SlideMenuAdapter mSlideMenuAdapter1, mSlideMenuAdapter2;

    JWSharePreference jwSharePreference = new JWSharePreference();

    //메인화면
    private TextView main_title;
    private ImageView main_image;

    private BottomNavigationView navView;
    private NavController navController;


    private final int INTENT_RELATION = 1;
    private final int INTENT_LOCATION = 2;
    private final int INTENT_FAMILY = 3;
    private final int INTENT_USER = 4;

    private BackPressFinishHandler mBackPressFinishHandler;

    private String id;

    //20220126
    private ViewPager2 viewPager;
    private LinearLayout layoutDots;
    private int[] fragments;
    private TextView[] dots;
    private ViewsSliderAdapter mAdapter;
    private boolean initDotted = true;
    private MySQLiteOpenHelper vc;

    @Override
    protected void onResume() {
        super.onResume();
        if(isUserReg == false){
            getUserInfo();
        }
        setUserInfo();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainAc = this;
        id = jwSharePreference.getString(JWSharePreference.PREFERENCE_LOGIN_ID,"");
        vc = new MySQLiteOpenHelper(this);

        initView();
        AddUserInfo addUserInfo = new AddUserInfo("id","5","1212","name","1","2222","");
        Log.v("CHECK","DDDDD :: " + addUserInfo.toString() );

        //floatingButton.setVisibility(View.GONE);
        getUserInfo();
//        requestUserList();

        mBackPressFinishHandler = new BackPressFinishHandler(this);
        this.getOnBackPressedDispatcher().addCallback(this, callback);
    }


    public void MainOpenDrawer(){
        drawerLayout.openDrawer(drawerView);
    }

    public DrawerLayout drawerLayout;
    public View drawerView;

    public RelativeLayout floatingButton;
    public void initView(){

        viewPager = findViewById(R.id.view_pager);
        layoutDots = findViewById(R.id.layoutDots);
        setViewPager();


        //floatingButton = findViewById(R.id.btn_keyword_input);

        //슬라이드메뉴
        // 전체화면인 DrawerLayout 객체 참조
        drawerLayout = findViewById(R.id.drawerLayout);

        // Drawer 화면(뷰) 객체 참조
        drawerView = findViewById(R.id.drawer);
        // 드로어 화면을 열고 닫을 버튼 객체 참조
        TextView btnOpenDrawer = findViewById(R.id.btn_slide);

        // 드로어 여는 버튼 리스너
        btnOpenDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(drawerView);
            }
        });

//        setViewPager();

//        main_title = findViewById(R.id.main_title);
//        main_image = findViewById(R.id.main_image);

        //상단 타이틀 제거
        getSupportActionBar().hide();

        //슬라이드메뉴 초기화
        initSlideMenuView();

        /*navView = findViewById(R.id.nav_view);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_manage, R.id.navigation_store, R.id.navigation_home, R.id.navigation_find)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                if(controller.getCurrentDestination().getId() == R.id.navigation_manage){
//                    main_title.setText(R.string.title_manage);
                    main_image.setImageResource(R.drawable.img_manage);
                }else if(controller.getCurrentDestination().getId() == R.id.navigation_home){
//                    main_title.setText(R.string.title_home);
                    main_image.setImageResource(R.drawable.img_home);
                }else if(controller.getCurrentDestination().getId() == R.id.navigation_store){
//                    main_title.setText(R.string.title_store);
                    main_image.setImageResource(R.drawable.img_store);
                }else if(controller.getCurrentDestination().getId() == R.id.navigation_find){
//                    main_title.setText(R.string.title_find);
                    main_image.setImageResource(R.drawable.img_findme);
                }
//                else if(controller.getCurrentDestination().getId() == R.id.navigation_recommend){
////                    main_title.setText(R.string.title_house);
//                    main_image.setImageResource(R.drawable.img_house);
//                }
            }
        });*/

        /*findViewById(R.id.btn_keyword_input).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, KeywordInputActivity.class);
                startActivity(i);
            }
        });*/

        findViewById(R.id.btn_logout).setOnClickListener(v ->{
            String confirm = CommandUtil.getInstance().getStr(R.string.str_cofirm);
            String cancel = CommandUtil.getInstance().getStr(R.string.str_cancel);
            String message = CommandUtil.getInstance().getStr(R.string.str_logout);
            CommandUtil.getInstance().showCommonTwoButtonDialog(this, message, cancel, confirm, new DialogClickListener() {
                @Override
                public void onClick(int button) { //확인 : 2 , 취소 : 1
                    if(button==2){
                        logout();
                    }
                }
            });
        });
    }

    private void logout(){
        JWSharePreference sharePreference = new JWSharePreference();
        sharePreference.setInt(JWSharePreference.PREFERENCE_SRL, 0);
        sharePreference.setString(JWSharePreference.PREFERENCE_LOGIN_ID, "");
        sharePreference.setString(JWSharePreference.PREFERENCE_USER_ID, "");
        sharePreference.setString(JWSharePreference.PREFERENCE_LOGIN_NAME, "");
        sharePreference.setString(JWSharePreference.PREFERENCE_LOGIN_BIRTH, "");
        sharePreference.setInt(JWSharePreference.PREFERENCE_GENDER, -1);
//                    sharePreference.setString(JWSharePreference.PREFERENCE_USER_ID, "1");
        sharePreference.setString(JWSharePreference.PREFERENCE_LOGIN_TOKEN, "");
        sharePreference.setString(JWSharePreference.PREFERENCE_LOGIN_FLAG, "N");

        Intent i = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(i);
        finish();
    }

    /**
     * 탑페이지뷰
     */

    public void setViewPager(){

        fragments = new int[]{R.layout.fragment_main_top_1,
                R.layout.fragment_main_top_2,
                R.layout.fragment_main_top_3,
                R.layout.fragment_main_top_4};

        mainTopView = new ArrayList<>();
        mAdapter = new ViewsSliderAdapter();
        viewPager.setAdapter(mAdapter);
        viewPager.registerOnPageChangeCallback(pageChangeCallback);

        addBottomDots(0);
    }





    /**
     * 슬라이드메뉴
     */

    private void initSlideMenuView(){
        //사용자
        String[] mSlideMenuItemText1 = getResources().getStringArray(R.array.slide_menu_1);
        String[] mSlideMenuItemCount1 = getResources().getStringArray(R.array.slide_count_1);

        //관계인, 장소
        String[] mSlideMenuItemText2 = getResources().getStringArray(R.array.slide_menu_2);
        String[] mSlideMenuItemCount2 = getResources().getStringArray(R.array.slide_count_2);

        //개인설정
        findViewById(R.id.slide_menu_self).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, YourSelfSettingActivity.class);

                i.putExtra("nickname",Constants.RootNICK_NAME);
                i.putExtra("SEQ",Constants.RootSEQ);
                i.putExtra("name",Constants.RootNAME);
                i.putExtra("mw",Constants.RootMW);
                i.putExtra("birth",Constants.RootBIRTH);

                startActivity(i);
            }
        });

        //슬라이드메뉴 아이템 셋팅
        mSlideMenuItem1 = new ArrayList<>();
        mSlideMenuItem2 = new ArrayList<>();

        for(int i=0; i<mSlideMenuItemText1.length; i++){
            SlideMenuData item = new SlideMenuData();
            item.setId(i);
            item.setTxt_menu_title(mSlideMenuItemText1[i]);
            item.setTxt_count_title(mSlideMenuItemCount1[i]);
            item.setImg_menu_id("");
            item.setImg_count_id("");
            item.setTxt_count_num("0");
            mSlideMenuItem1.add(item);
        }

        for(int i=0; i<mSlideMenuItemText2.length; i++){
            SlideMenuData item = new SlideMenuData();
            item.setId(i+100);
            item.setTxt_menu_title(mSlideMenuItemText2[i]);
            item.setTxt_count_title(mSlideMenuItemCount2[i]);
            item.setImg_menu_id("");
            item.setImg_count_id("");
            item.setTxt_count_num("0");
            mSlideMenuItem2.add(item);
        }

        RecyclerView recyclerView1 = findViewById(R.id.rcv_slide_1);
        mSlideMenuAdapter1 = new SlideMenuAdapter(this, mSlideMenuItem1);
        mSlideMenuAdapter1.setMenuClickListener(mSlideMenuClickListener);
        recyclerView1.setAdapter(mSlideMenuAdapter1);
        recyclerView1.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false)) ;
        RecyclerDecoration spaceDecoration2 = new RecyclerDecoration(0,"vertical");
        recyclerView1.addItemDecoration(spaceDecoration2);

        RecyclerView recyclerView2 = findViewById(R.id.rcv_slide_2);
        mSlideMenuAdapter2 = new SlideMenuAdapter(this, mSlideMenuItem2);
        mSlideMenuAdapter2.setMenuClickListener(mSlideMenuClickListener);
        recyclerView2.setAdapter(mSlideMenuAdapter2);
        recyclerView2.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false)) ;
        recyclerView2.addItemDecoration(spaceDecoration2);

        setUserInfo();


    }

    public void setUserInfo(){
        JWSharePreference sharePreference = new JWSharePreference();
        String name = sharePreference.getString(JWSharePreference.PREFERENCE_LOGIN_NICKNAME,"");
        ((TextView)findViewById(R.id.txt_slide_name)).setText(name);
        String birth = sharePreference.getString(JWSharePreference.PREFERENCE_LOGIN_BIRTH,"");
        if(birth.length()>5){
            birth = birth.substring(0,4);
        }
        String birthStr = birth;
        ((TextView)findViewById(R.id.txt_slide_birth)).setText(birthStr);
        int gender = sharePreference.getInt(JWSharePreference.PREFERENCE_GENDER,1);
        if(gender == 1){
            ((ImageView)findViewById(R.id.icon_profile)).setBackgroundResource(R.drawable.icon_m);
        }else{
            ((ImageView)findViewById(R.id.icon_profile)).setBackgroundResource(R.drawable.icon_f);

        }
    }

    private SlideMenuAdapter.menuClickListener mSlideMenuClickListener = new SlideMenuAdapter.menuClickListener() {
        @Override
        public void onClickItem(int id, int menuType) {
            int viewCode = 0;
            Intent i=  null;
            if(menuType == 0){ //메뉴
                switch (id){
                    case BTN_COIN_SETTING:

                        break;
                    case BTN_USER_SETTING:
                        viewCode = INTENT_USER;
                        i = new Intent(MainActivity.this, AddUserActivity.class);
                        break;
                    case BTN_RELATION_SETTING:
                        viewCode = INTENT_RELATION;
                        i = new Intent(MainActivity.this, AddRelationActivity.class);
                        break;
                    case BTN_LOCATION_SETTING:
                        viewCode = INTENT_LOCATION;
                        i = new Intent(MainActivity.this, AddLocationActivity.class);
                        break;
                    case BTN_FAMILY_SETTING:
                        viewCode = INTENT_FAMILY;
                        i = new Intent(MainActivity.this,FamilySettingActivity.class);
                        break;
                    default:
                        break;
                }

                if(i != null) startActivityForResult(i,viewCode);
            }else{//카운트
                switch (id){
                    case BTN_COIN_SETTING:
                        requestCoinData();
                        Toast.makeText(MainActivity.this,CommandUtil.getInstance().getStr(R.string.main_coin_update), Toast.LENGTH_SHORT).show();
                        break;

                    case BTN_USER_SETTING:
                        viewCode = INTENT_USER;
                        i = new Intent(MainActivity.this,UserListActivity.class);
                        break;
                    case BTN_RELATION_SETTING:
                        viewCode = INTENT_RELATION;
                        i = new Intent(MainActivity.this,RelationListActivity.class);
                        break;
                    case BTN_LOCATION_SETTING:
                        viewCode = INTENT_LOCATION;
                        i = new Intent(MainActivity.this,LocationListActivity.class);
                        break;
                    default:
                        break;
                }

                if(i != null) startActivityForResult(i,viewCode);
            }
        }
    };

    public void visibleFloatingButton(int visible){
//        if(visible == View.VISIBLE){
//            if(floatingButton.getVisibility() == View.GONE){
//                Animation animation = AnimationUtils.loadAnimation(getBaseContext(),R.anim.fade_in);
//                floatingButton.startAnimation(animation);
//                floatingButton.setVisibility(View.VISIBLE);
//            }
//        }else{
//            if(floatingButton.getVisibility() == View.VISIBLE){
//                Animation animation = AnimationUtils.loadAnimation(getBaseContext(),R.anim.fade_out);
//                floatingButton.startAnimation(animation);
//                floatingButton.setVisibility(View.GONE);
//            }
//        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("TEST","onActivityResult호출");
        /*
        if(data != null){
            userCount = data.getIntExtra("userCount",userCount);
            relationCount = data.getIntExtra("relationCount",relationCount);
            locationCount = data.getIntExtra("locationCount",locationCount);
        }
        mSlideMenuItem1.get(1).setTxt_count_num(String.valueOf(userCount));
        mSlideMenuItem2.get(0).setTxt_count_num(String.valueOf(relationCount));
        mSlideMenuItem2.get(1).setTxt_count_num(String.valueOf(locationCount));

        mSlideMenuAdapter1.notifyDataSetChanged();
        mSlideMenuAdapter2.notifyDataSetChanged();*/

        Log.v("CHECK", "dd :: " +requestCode);
        if(requestCode == INTENT_USER){ //사용자변경/등록
            requestUserList();
        }
        if(requestCode == INTENT_RELATION){
            requestRelationList();
        }
        if(requestCode == INTENT_LOCATION){
            requestLocationList();
        }
    }

    boolean isUserReg = true;
    public void moveUserRegView(){
        isUserReg = false;
        CommandUtil.getInstance().showCommonOneButtonDialog(MainActivity.this, CommandUtil.getInstance().getStr(R.string.main_user_empty), CommandUtil.getInstance().getStr(R.string.str_cofirm), CommonPopupDialog.COMMON_DIALOG_OPTION_CLOSE_DIALOG, new DialogClickListener() {
            @Override
            public void onClick(int button) {
//                Intent i = new Intent(MainActivity.this,AddUserActivity.class);
//                startActivity(i);
            }
        });
    }

    public void getUserInfo(){ //유저리스트
        ActionRuler.getInstance().addAction(new ActionRequestMainUserInfo(this,id, new ActionResultListener<DefaultResult>() {
            @Override
            public void onResponseResult(DefaultResult response) {
                try {
                    DefaultResult result = response;
                    Log.d("!TEST",result.getData());
                    if(result != null){
                        Constants.MainData = new JSONObject(result.getData());

                        //사용자
                        JSONArray l_tb_in = Constants.MainData.getJSONArray("l_tb_in");
                        //관계인
                        JSONArray l_tb_yu = Constants.MainData.getJSONArray("l_tb_yu");
                        //장소
                        JSONArray l_tb_te = Constants.MainData.getJSONArray("l_tb_te");

                        //사용자 카운트
                        int UserCount = l_tb_in.length();
                        int RelationCount = l_tb_yu.length();
                        int LocationCount = l_tb_te.length();

//                        mSlideMenuItem1.get(1).setTxt_count_num(UserCount+"");
                        mSlideMenuItem2.get(0).setTxt_count_num(RelationCount+"");
                        mSlideMenuItem2.get(1).setTxt_count_num(LocationCount+"");
                        mSlideMenuAdapter2.notifyDataSetChanged();
                        mSlideMenuAdapter1.notifyDataSetChanged();

                        for(int i =0; i<l_tb_in.length(); i++){
                            String USER_TYPE = l_tb_in.getJSONObject(i).getString("USER_TYPE");
                            if(Constants.RootSEQ.equals("")){
                                //첫로그인시 Root사용자로 설정
                            }else{
                                //Resume시 현재 선택된 사용자로 설정
                            }

                            if(USER_TYPE.equals("0")){
                                if(Constants.RootSEQ.equals("")){
                                    Constants.RootSEQ = l_tb_in.getJSONObject(i).getString("SEQ");
                                    Constants.RootNAME = l_tb_in.getJSONObject(i).getString("NAME");
                                    Constants.RootNICK_NAME = l_tb_in.getJSONObject(i).getString("NICK_NAME");
                                    Constants.RootBIRTH = l_tb_in.getJSONObject(i).getString("BIRTH_DATE");
                                    Constants.RootMW = l_tb_in.getJSONObject(i).getInt("MW");

                                    jwSharePreference.setString(JWSharePreference.PREFERENCE_USER_SEQ, Constants.RootSEQ);
                                    jwSharePreference.setString(JWSharePreference.PREFERENCE_LOGIN_NICKNAME, Constants.RootNICK_NAME);
                                }
                            }
                        }
                        String name = jwSharePreference.getString(JWSharePreference.PREFERENCE_LOGIN_NICKNAME,"");
                        ((TextView)findViewById(R.id.txt_slide_name)).setText(name);
                        requestCoinData();
                    }


                } catch (Exception e) {
                    Log.d("!TEST","에러");
                    e.printStackTrace();
                }
            }

            @Override
            public void onResponseError(String message) {

                Log.d("!TEST","에러 : " + message);
            }
        }));
        ActionRuler.getInstance().runNext();
    }



    public void requestUserList(){ //유저리스트
        ActionRuler.getInstance().addAction(new ActionRequestUserList(this,id, new ActionResultListener<UserListResult>() {
            @Override
            public void onResponseResult(UserListResult response) {
                try {
                    UserListResult result = response;
                    Log.d("!TEST",result.getData());
                    if(result != null){
                        JSONArray jsonArray = new JSONArray(result.getData());

                        if(jsonArray.length() > 0){
                            mSlideMenuItem1.get(1).setTxt_count_num(jsonArray.length()+"");
                            mSlideMenuAdapter1.notifyDataSetChanged();
                            return;
                        }
                    }
                    moveUserRegView();


                } catch (Exception e) {
                    moveUserRegView();
                    Log.d("!TEST","에러");
                    e.printStackTrace();
                }
            }

            @Override
            public void onResponseError(String message) {
                moveUserRegView();
                Log.d("!TEST","에러 : " + message);
            }
        }));
        ActionRuler.getInstance().runNext();
    }
    private void requestRelationList(){ //관계인리스트
        ActionRuler.getInstance().addAction(new ActionRequestRelationList(this,id,"0", new ActionResultListener<RelationListResult>() {
            @Override
            public void onResponseResult(RelationListResult response) {
                try {
                    RelationListResult result = response;


                    if(result.isSuccess()){
                        //성공
                        if(result.getData()!=null){

                            JSONArray jsonArray = new JSONArray(result.getData());
                            if(jsonArray.length() > 0){
                                mSlideMenuItem2.get(0).setTxt_count_num(jsonArray.length()+"");
                                mSlideMenuAdapter2.notifyDataSetChanged();
                                return;
                            }
                        }
                    }else{

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onResponseError(String message) {
                LogUtil.LogD("관계인 리스트 불러오기 실패 !!");
            }
        }));
        ActionRuler.getInstance().runNext();
    }
    private void requestLocationList(){
        ActionRuler.getInstance().addAction(new ActionRequestLocationList(this,id, "0", new ActionResultListener<LocationListResult>() {

            @Override
            public void onResponseResult(LocationListResult response) {
                try {
                    LocationListResult result = response;

                    if(result!=null){
                        if(result.isSuccess()){
                            JSONArray jsonArray = new JSONArray(result.getData());
                            if(jsonArray.length() > 0){
                                mSlideMenuItem2.get(1).setTxt_count_num(jsonArray.length()+"");
                                mSlideMenuAdapter2.notifyDataSetChanged();
                                return;
                            }
                        }

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onResponseError(String message) {
//                Toast.makeText(MainActivity.this,"장소가져오기 실패.\n잠시 후 다시 시도해주세요.",Toast.LENGTH_SHORT).show();
            }
        }));
        ActionRuler.getInstance().runNext();
    }

    public void requestCoinData(){
        ActionRuler.getInstance().addAction(new ActionRequestCoinData(this, new ActionResultListener<CoinDataResult>() {

            @Override
            public void onResponseResult(CoinDataResult response) {
                try {
                    CoinDataResult result = response;

                    if(result!=null) {
                        Log.d("CHECK", "CoinData : " + result.getResp().toString());
                        if (result.isResult()) {

                            String coinValue = result.getResp().getMy_balance();
                            if(!coinValue.equals("")){
                                if(coinValue.contains(".")){
                                    String[] coinArr = coinValue.split("\\.");
                                    coinValue = coinArr[0] + "." + coinArr[1].substring(0,2);
                                }
                            }
                            Constants.myCoinValue = coinValue;
                            mSlideMenuItem1.get(0).setTxt_count_num(coinValue);
                            mSlideMenuAdapter1.notifyDataSetChanged();

                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onResponseError(String message) {

            }
        }));
        ActionRuler.getInstance().runNext();
    }

    OnBackPressedCallback callback = new OnBackPressedCallback(true) {
        @Override
        public void handleOnBackPressed() {
            CommandUtil.getInstance().dismissLoadingDialog();
            //백버튼 두번 종료
            mBackPressFinishHandler.onBackPressed();
        }
    };

    //ViewPager관련
    private void FragmentView(int fragment){
        SkyLog.d("FragmentView position :: " + fragment);
        //FragmentTransactiom를 이용해 프래그먼트를 사용합니다.
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        switch (fragment){
            case 0:
                // 홈 프래그먼트 호출
                HomeFragment homeFragment = new HomeFragment();
                transaction.replace(R.id.nav_host_fragment_activity_main, homeFragment);
                transaction.commit();
                break;

            case 1:
                // 매니지 프래그먼트 호출
                ManageFragment manageFragment = new ManageFragment();
                transaction.replace(R.id.nav_host_fragment_activity_main, manageFragment);
                transaction.commit();
                break;
            case 2:
                // 스토어 프래그먼트 호출
                StoreFragment storeFragment = new StoreFragment();
                transaction.replace(R.id.nav_host_fragment_activity_main, storeFragment);
                transaction.commit();
                break;
            case 3:
                // FindMe 프래그먼트 호출
                FindFragment findFragment = new FindFragment();
                transaction.replace(R.id.nav_host_fragment_activity_main, findFragment);
                transaction.commit();
                break;
        }

    }


    private void addBottomDots(int currentPage) {
        Log.v("ifeelbluu","addBottomDots :: " +currentPage);
        dots = new TextView[fragments.length];

        layoutDots.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(ContextCompat.getColor(this, R.color.mong_grey_default));
            layoutDots.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(ContextCompat.getColor(this, R.color.white));

    }

    String[] titleString = {
            CommandUtil.getInstance().getStr(R.string.fragment_home_result),
            CommandUtil.getInstance().getStr(R.string.fragment_manage_title),
            CommandUtil.getInstance().getStr(R.string.fragment_store_1),
            CommandUtil.getInstance().getStr(R.string.fragment_find_title)
    };

    ViewPager2.OnPageChangeCallback pageChangeCallback = new ViewPager2.OnPageChangeCallback() {
        @Override
        public void onPageSelected(int position) {
            super.onPageSelected(position);
            if(!initDotted) addBottomDots(position);
            FragmentView(position);
            initDotted = false;

            CurrentPosition = position;
            Log.v("ifeelbluu", "mainTopView Size :: " + mainTopView.size());

            String mainText = "";
            switch (position){
                case 0:
                    String name = jwSharePreference.getString(JWSharePreference.PREFERENCE_LOGIN_NICKNAME,"");
                    mainText = CommandUtil.getInstance().getStr(R.string.fragment_home_result).replaceAll("name", name);
                    break;
                case 1:
                    try {
                        mainText = Constants.MainData.getString("ManageTitle");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 2:
//                    mainText = CommandUtil.getInstance().getStr(R.string.fragment_store_1);
                    break;
                case 3:
//                    mainText = CommandUtil.getInstance().getStr(R.string.fragment_store_1);
                    break;
            }
            //setMainTopText(mainText);
        }
    };

    public int CurrentPosition = 0;
    public ArrayList<View> mainTopView;
    public void setMainTopText(String text){
        TextView mainText = mainTopView.get(CurrentPosition).findViewById(R.id.main_title);
        mainText.setText(text);
    }

    public class ViewsSliderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        public ViewsSliderAdapter() {

        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(viewType, parent, false);
            return new SliderViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            if(mainTopView.size()==4){
                mainTopView.set(position, holder.itemView);
            }else{
                mainTopView.add(holder.itemView);
            }

        }

        @Override
        public int getItemViewType(int position) {
            return fragments[position];
        }

        @Override
        public int getItemCount() {
            return fragments.length;
        }

        public class SliderViewHolder extends RecyclerView.ViewHolder {
            public TextView title, year, genre;

            public SliderViewHolder(View view) {
                super(view);
            }
        }
    }
}