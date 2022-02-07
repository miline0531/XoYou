package com.redrover.xoyou.activity.detail;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;

import java.util.ArrayList;

import com.redrover.xoyou.R;
import com.redrover.xoyou.adapter.SlideMenuDetailAdapter;
import com.redrover.xoyou.common.Constants;
import com.redrover.xoyou.model.SlideMenuDetailData;
import com.redrover.xoyou.storage.JWSharePreference;
import com.redrover.xoyou.utils.RecyclerDecoration;


public class MymongDetailActivity extends FragmentActivity {


    private final JWSharePreference sharePreference = new JWSharePreference();

    public DrawerLayout drawerLayout;
    public View drawerView;

    public FrameLayout flContent = null;
    public TextView main_title, btn_close;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mymong_detail2);
//        getSupportActionBar().hide();
        initView();
        initDrawerLayout();
        setFragment(0);
    }

    private void initView() {
        flContent = findViewById(R.id.flContent);
        main_title = findViewById(R.id.main_title);
        btn_close = findViewById(R.id.btn_close);

        RelativeLayout root_bg = findViewById(R.id.root_bg);

        String mTitle = "";
        try {
            mTitle = Constants.StoryJob.getString("GoodBadMong"); //그래프 title
        } catch (JSONException e) {
            e.printStackTrace();
        }

//        String goodbad = "good";
//        //흉몽
//        if(mTitle.equals(CommandUtil.getInstance().getStr(R.string.mong_detail_tab_1_mong_type_1))) {
//            goodbad = "bad";
//            root_bg.setBackgroundColor(ContextCompat.getColor(this, R.color.detail_menu1_root_bg1));
//
//        }//길몽
//        else if(mTitle.equals(CommandUtil.getInstance().getStr(R.string.mong_detail_tab_1_mong_type_2))){
//            root_bg.setBackgroundColor(ContextCompat.getColor(this, R.color.detail_menu1_root_bg2));
//
//        }//악몽
//        else if(mTitle.equals(CommandUtil.getInstance().getStr(R.string.mong_detail_tab_1_mong_type_3))){
//            goodbad = "bad";
//            root_bg.setBackgroundColor(ContextCompat.getColor(this, R.color.detail_menu1_root_bg3));
//
//        }//영몽
//        else if(mTitle.equals(CommandUtil.getInstance().getStr(R.string.mong_detail_tab_1_mong_type_4))){
//            root_bg.setBackgroundColor(ContextCompat.getColor(this, R.color.detail_menu1_root_bg4));
//        }else{
//            root_bg.setBackgroundColor(ContextCompat.getColor(this, R.color.detail_menu1_root_bg2));
//        }
        String MongDou = "";
        float MongDouValue = 0f;
        try {
            MongDou = Constants.StoryJob.getString("MongDou"); //그래프 1
            MongDouValue = Float.parseFloat(MongDou)* 100;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (MongDouValue > 66)
        {
            root_bg.setBackgroundColor(ContextCompat.getColor(this, R.color.detail_menu1_root_bg2));
        }
        else if (MongDouValue > 33)
        {
            root_bg.setBackgroundColor(ContextCompat.getColor(this, R.color.detail_menu1_root_bg4));
        }
        else
        {
            root_bg.setBackgroundColor(ContextCompat.getColor(this, R.color.detail_menu1_root_bg1));
        }

        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void initDrawerLayout(){
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

        initSlideMenuView();
    }

    /**
     * 슬라이드메뉴
     */
    ArrayList<SlideMenuDetailData> mSlideMenuItem;
    SlideMenuDetailAdapter mSlideMenuAdapter;
    private void initSlideMenuView(){
        //사용자
        String[] mSlideMenuItemTitle = getResources().getStringArray(R.array.detail_menu_title);
        String[] mSlideMenuItemSubTitle = getResources().getStringArray(R.array.detail_menu_sub_title);


        //슬라이드메뉴 아이템 셋팅
        mSlideMenuItem = new ArrayList<>();

        for(int i=0; i<mSlideMenuItemTitle.length; i++){
            SlideMenuDetailData item = new SlideMenuDetailData();
            item.setId(i);
            item.setTxt_menu_title(mSlideMenuItemTitle[i]);
            item.setTxt_menu_sub_title(mSlideMenuItemSubTitle[i]);
            mSlideMenuItem.add(item);
        }

        RecyclerView recyclerView1 = findViewById(R.id.rcv_menu_detail);
        mSlideMenuAdapter = new SlideMenuDetailAdapter(this, mSlideMenuItem);
        mSlideMenuAdapter.setMenuClickListener(mSlideMenuClickListener);
        recyclerView1.setAdapter(mSlideMenuAdapter);
        recyclerView1.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false)) ;
        RecyclerDecoration spaceDecoration2 = new RecyclerDecoration(8,"vertical");
        recyclerView1.addItemDecoration(spaceDecoration2);

    }

    private SlideMenuDetailAdapter.menuClickListener mSlideMenuClickListener = new SlideMenuDetailAdapter.menuClickListener() {
        @Override
        public void onClickItem(int id, int menuType) {
            setFragment(id);
        }
    };

    public void setFragment(int id){
        Fragment fragment = null;
        Class fragmentClass = null;

        switch (id){
            case 0://기본분석
                fragmentClass = MymongTabFragment_01.class;
                break;
            case 1://세부분석
                fragmentClass = MymongTabFragment_02.class;
                break;
            case 2://연관분석
                fragmentClass = MymongTabFragment_03.class;
                break;
            case 3://적용기간
                fragmentClass = MymongTabFragment_04.class;
                break;
            case 4://장소분석
                fragmentClass = MymongTabFragment_05.class;
                break;
            case 5://해소추천
                fragmentClass = MymongTabFragment_06.class;
                break;
        }

        if(fragmentClass == null){
            drawerLayout.closeDrawers();
            return;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(flContent.getId(), fragment).commit();

        // Set action bar title
        main_title.setText(mSlideMenuItem.get(id).getTxt_menu_sub_title());
        drawerLayout.closeDrawers();
    }

}