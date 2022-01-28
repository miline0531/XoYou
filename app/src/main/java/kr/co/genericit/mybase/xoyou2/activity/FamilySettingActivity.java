package kr.co.genericit.mybase.xoyou2.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import kr.co.genericit.mybase.xoyou2.R;
import kr.co.genericit.mybase.xoyou2.adapter.CheckFamilyAdapter;
import kr.co.genericit.mybase.xoyou2.common.CommonActivity;
import kr.co.genericit.mybase.xoyou2.network.action.ActionRuler;
import kr.co.genericit.mybase.xoyou2.network.interfaces.ActionResultListener;
import kr.co.genericit.mybase.xoyou2.network.request.ActionRequestFamilyMemberCount;
import kr.co.genericit.mybase.xoyou2.network.request.ActionRequestFamilyMemberList;
import kr.co.genericit.mybase.xoyou2.network.request.ActionRequestSetFamilyMemberList;
import kr.co.genericit.mybase.xoyou2.network.response.FamilyMemberCountResult;
import kr.co.genericit.mybase.xoyou2.network.response.FamilyMemberResult;
import kr.co.genericit.mybase.xoyou2.network.response.SetFamilyMemberResult;
import kr.co.genericit.mybase.xoyou2.utils.CommandUtil;
import kr.co.genericit.mybase.xoyou2.utils.RecyclerDecoration;


public class FamilySettingActivity extends CommonActivity {
    private ArrayList<FamilyMemberResult.Data.FamilyInfo> mCheckFamilyItem1, mCheckFamilyItem2, mCheckFamilyItem3, mCheckFamilyItem4, mCheckFamilyItem5;
    private CheckFamilyAdapter mCheckFamilyAdapter1, mCheckFamilyAdapter2, mCheckFamilyAdapter3, mCheckFamilyAdapter4, mCheckFamilyAdapter5;
    private ArrayList<ArrayList<FamilyMemberResult.Data.FamilyInfo>> arrayListSet;
    private RecyclerView recyclerView1,recyclerView2,recyclerView3,recyclerView4,recyclerView5;
    private TextView btn_save;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_setting_family);
        super.onCreate(savedInstanceState);


        initView();

        requestFamilyMemberList();
        requestFamilyMemberCount();
    }

    public void initView(){
        recyclerView1 = findViewById(R.id.rcv_family1);
        recyclerView2 = findViewById(R.id.rcv_family2);
        recyclerView3 = findViewById(R.id.rcv_family3);
        recyclerView4 = findViewById(R.id.rcv_family4);
        recyclerView5 = findViewById(R.id.rcv_family5);

        btn_save = findViewById(R.id.btn_save);
    }

    private void setRecyclerView(RecyclerView recyclerView, CheckFamilyAdapter adapter){
        RecyclerDecoration spaceDecoration = new RecyclerDecoration(24,"vertical");
        recyclerView.addItemDecoration(spaceDecoration);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        recyclerView.setAdapter(adapter);

        recyclerView.setVisibility(RecyclerView.VISIBLE);
    }

    private void requestFamilyMemberList(){
        ActionRuler.getInstance().addAction(new ActionRequestFamilyMemberList(this,new ActionResultListener<FamilyMemberResult>() {
            @Override
            public void onResponseResult(FamilyMemberResult response) {
                try {
                    FamilyMemberResult result = response;
                    if(result!=null){
                        settingFamilyMember(result);

                    }else{
                        Toast.makeText(getApplicationContext(), CommandUtil.getInstance().getStr(R.string.family_load_fail),Toast.LENGTH_SHORT).show();
                        finish();
                    }

                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(),CommandUtil.getInstance().getStr(R.string.family_load_fail),Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                    finish();
                }
            }

            @Override
            public void onResponseError(String message) {
                Toast.makeText(getApplicationContext(),CommandUtil.getInstance().getStr(R.string.family_load_fail),Toast.LENGTH_SHORT).show();
                finish();
            }

        }));

        ActionRuler.getInstance().runNext();
    }

    private void requestFamilyMemberCount(){
        ActionRuler.getInstance().addAction(new ActionRequestFamilyMemberCount(this,new ActionResultListener<FamilyMemberCountResult>() {
            @Override
            public void onResponseResult(FamilyMemberCountResult response) {
                try {
                    FamilyMemberCountResult result = response;
                    if(result!=null){
                        Log.d("!TEST","+"+result.getData().getDataCnt());
                    }else{
                        Log.d("!TEST","result가 널");

                    }

                } catch (Exception e) {
                    Log.d("!TEST",e.toString());
                    e.printStackTrace();
                }
            }

            @Override
            public void onResponseError(String message) {
                Toast.makeText(getApplicationContext(),CommandUtil.getInstance().getStr(R.string.family_refresh_fail),Toast.LENGTH_SHORT).show();
                finish();
            }

        }));

        ActionRuler.getInstance().runNext();
    }

    private void requestSetFamilyMemberList(){
        ActionRuler.getInstance().addAction(new ActionRequestSetFamilyMemberList(this,arrayListSet,new ActionResultListener<SetFamilyMemberResult>() {
            @Override
            public void onResponseResult(SetFamilyMemberResult response) {
                try {
                    SetFamilyMemberResult result = response;
                    if(result!=null){
                        Log.d("!TEST",result.getCode());
                        Toast.makeText(getApplicationContext(),CommandUtil.getInstance().getStr(R.string.family_refresh_success),Toast.LENGTH_SHORT).show();
                        finish();
                    }else{
                        Log.d("!TEST","result가 널");
                    }

                } catch (Exception e) {
                    Log.d("!TEST",e.toString());
                    e.printStackTrace();
                }
            }

            @Override
            public void onResponseError(String message) {
                Toast.makeText(getApplicationContext(),CommandUtil.getInstance().getStr(R.string.family_refresh_fail),Toast.LENGTH_SHORT).show();
                finish();
            }

        }));

        ActionRuler.getInstance().runNext();
    }

    private void settingFamilyMember(FamilyMemberResult info){
        arrayListSet = new ArrayList<>();
        mCheckFamilyItem1 = new ArrayList<>();
        mCheckFamilyItem2 = new ArrayList<>();
        mCheckFamilyItem3 = new ArrayList<>();
        mCheckFamilyItem4 = new ArrayList<>();
        mCheckFamilyItem5 = new ArrayList<>();

        arrayListSet.add(info.getData().getDataList().get(0).getFamily());
        arrayListSet.add(info.getData().getDataList().get(0).getFatherFamily());
        arrayListSet.add(info.getData().getDataList().get(0).getMotherFamily());
        arrayListSet.add(info.getData().getDataList().get(0).getAfterMarry());
        arrayListSet.add(info.getData().getDataList().get(0).getJob());

        for(int i=0; i<arrayListSet.size(); i++){
            for(int z=0; z<arrayListSet.get(i).size(); z++){
                FamilyMemberResult.Data.FamilyInfo item = arrayListSet.get(i).get(z);
                switch (i){
                    case 0:
                        mCheckFamilyItem1.add(item);
                        break;
                    case 1:
                        mCheckFamilyItem2.add(item);
                        break;
                    case 2:
                        mCheckFamilyItem3.add(item);
                        break;
                    case 3:
                        mCheckFamilyItem4.add(item);
                        break;
                    case 4:
                        mCheckFamilyItem5.add(item);
                        break;
                }
            }
        }



        mCheckFamilyAdapter1 = new CheckFamilyAdapter(this, mCheckFamilyItem1);
        mCheckFamilyAdapter2 = new CheckFamilyAdapter(this, mCheckFamilyItem2);
        mCheckFamilyAdapter3 = new CheckFamilyAdapter(this, mCheckFamilyItem3);
        mCheckFamilyAdapter4 = new CheckFamilyAdapter(this, mCheckFamilyItem4);
        mCheckFamilyAdapter5 = new CheckFamilyAdapter(this, mCheckFamilyItem5);

        setRecyclerView(recyclerView1,mCheckFamilyAdapter1);
        setRecyclerView(recyclerView2,mCheckFamilyAdapter2);
        setRecyclerView(recyclerView3,mCheckFamilyAdapter3);
        setRecyclerView(recyclerView4,mCheckFamilyAdapter4);
        setRecyclerView(recyclerView5,mCheckFamilyAdapter5);

        btn_save.setOnClickListener(v ->{
            requestSetFamilyMemberList();
        });
    }
}