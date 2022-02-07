package com.redrover.xoyou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import com.redrover.xoyou.R;
import com.redrover.xoyou.adapter.LocationRecyclerviewAdapter;
import com.redrover.xoyou.common.CommonActivity;
import com.redrover.xoyou.interfaces.DialogClickListener;
import com.redrover.xoyou.network.action.ActionRuler;
import com.redrover.xoyou.network.interfaces.ActionResultListener;
import com.redrover.xoyou.model.Location;
import com.redrover.xoyou.network.request.ActionRequestDeleteLocation;
import com.redrover.xoyou.network.request.ActionRequestLocationList;
import com.redrover.xoyou.network.response.DeleteLocationResult;
import com.redrover.xoyou.network.response.LocationListResult;
import com.redrover.xoyou.storage.JWSharePreference;
import com.redrover.xoyou.utils.CommandUtil;
import com.redrover.xoyou.utils.LogUtil;


public class LocationListActivity extends CommonActivity {
    private RecyclerView recyclerView;
    private LocationRecyclerviewAdapter relationRecyclerviewAdapter;
    private ArrayList<Location> locationDataList;
    private String userId;
    private int deleteItemPosition;
    private JWSharePreference preference;
    int count = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_setting_location_list);
        super.onCreate(savedInstanceState);
        preference = new JWSharePreference();
        userId = preference.getString(JWSharePreference.PREFERENCE_LOGIN_ID,"");
        Log.d("USERID",userId);
        getListData();
        initView();


    }

    public void initView(){
        recyclerView = findViewById(R.id.rcv_location_list);
        relationRecyclerviewAdapter = new LocationRecyclerviewAdapter(this);

        relationRecyclerviewAdapter.setListOnClickListener(onListClickListener);
        relationRecyclerviewAdapter.setTaskList(locationDataList);
        recyclerView.setAdapter(relationRecyclerviewAdapter);

    }

    LocationRecyclerviewAdapter.listOnClickListener onListClickListener = new LocationRecyclerviewAdapter.listOnClickListener() {
        @Override
        public void onClickItem(int position, int action) {
            if(action == 0){
                //삭제
                CommandUtil.getInstance().showCommonTwoButtonDialog(LocationListActivity.this, CommandUtil.getInstance().getStr(R.string.location_list_place_delete), CommandUtil.getInstance().getStr(R.string.str_cofirm), CommandUtil.getInstance().getStr(R.string.str_cancel), new DialogClickListener() {
                    @Override
                    public void onClick(int button) {
                        switch (button){
                            case 1:
                                Log.d("Test","확인");
                                deleteItemPosition = position;
                                requestDeleteItem();
                                break;
                            case 2:
                                Log.d("Test","취소");
                                break;
                        }
                    }
                });
            }else {


                //수정
                String addr = locationDataList.get(position).getADDR1();
                String floor =locationDataList.get(position).getFLOOR();
                String name =locationDataList.get(position).getNAME();
                String gubun =locationDataList.get(position).getGUBUN();
                int seq = locationDataList.get(position).getSEQ();
                String lat = locationDataList.get(position).getLAT();
                String lng = locationDataList.get(position).getLNG();


                Intent i = new Intent(LocationListActivity.this, AddLocationActivity.class);
                i.putExtra("addr",addr);
                i.putExtra("floor",floor);
                i.putExtra("name",name);
                i.putExtra("gubun",gubun);
                i.putExtra("seq",seq);
                i.putExtra("lat",lat);
                i.putExtra("lng",lng);
                startActivity(i);
                finish();
            }
        }
    };


    private void getListData(){
        //todo 리스트가져오기 API
        locationDataList = new ArrayList<>();
        requestLocationList();
    }

    private void requestDeleteItem(){
        ActionRuler.getInstance().addAction(new ActionRequestDeleteLocation(this,locationDataList.get(deleteItemPosition).getSEQ(), new ActionResultListener<DeleteLocationResult>() {
            @Override
            public void onResponseResult(DeleteLocationResult response) {
                try {
                    DeleteLocationResult result = response;
                    if(result!=null){
                        if(result.isSuccess()){
                            locationDataList.remove(deleteItemPosition);
                            relationRecyclerviewAdapter.notifyItemRemoved(deleteItemPosition);
                            Toast.makeText(LocationListActivity.this,CommandUtil.getInstance().getStr(R.string.location_list_place_delete_success),Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onResponseError(String message) {
                Toast.makeText(LocationListActivity.this,CommandUtil.getInstance().getStr(R.string.location_list_place_delete_fail),Toast.LENGTH_SHORT).show();
                LogUtil.LogD("장소삭제 실패!!!!");
            }
        }));
        ActionRuler.getInstance().runNext();
    }

    private void requestLocationList(){
        ActionRuler.getInstance().addAction(new ActionRequestLocationList(this,userId, "0", new ActionResultListener<LocationListResult>() {

            @Override
            public void onResponseResult(LocationListResult response) {
                try {
                    LocationListResult result = response;

                    if(result!=null){
                        if(result.isSuccess()){
                            locationDataList.clear();
                            locationDataList.addAll(stringToArrayList(result.getData()));
                            count = locationDataList.size();
                        }
                        relationRecyclerviewAdapter.notifyDataSetChanged();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onResponseError(String message) {
                Toast.makeText(LocationListActivity.this,CommandUtil.getInstance().getStr(R.string.location_list_place_load_fail),Toast.LENGTH_SHORT).show();
                LogUtil.LogD("장소가져오기 실패!!!!");
            }
        }));
        ActionRuler.getInstance().runNext();
    }
    public ArrayList<Location> stringToArrayList(String data) throws JSONException {
        JSONArray jsonArray = new JSONArray(data);
        ArrayList<Location> arrayList = new ArrayList<>();
        for(int i=0; i<jsonArray.length(); i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            Location location = new Location();

            location.setSEQ(Integer.parseInt(jsonObject.getString("SEQ")));
            location.setUSER_ID(jsonObject.getString("USER_ID"));
            location.setGUBUN(jsonObject.getString("GUBUN"));
            location.setBZGUBUN(jsonObject.getString("BZGUBUN"));
            location.setNAME(jsonObject.getString("NAME"));
            location.setPOST_CODE(jsonObject.getString("POST_CODE"));
            location.setADDR1(jsonObject.getString("ADDR1"));
            location.setADDR2(jsonObject.getString("ADDR2"));
            location.setLAT(jsonObject.getString("LAT"));
            location.setLNG(jsonObject.getString("LNG"));
            location.setFLOOR(jsonObject.getString("FLOOR"));

            arrayList.add(location);
        }

        return arrayList;
    }
    @Override
    public void onBackPressed() {
        Intent i = getIntent();
        i.putExtra("locationCount", count);
        setResult(RESULT_OK, i);
        Log.v("CHECK:2","locationCount : " + count);

        super.onBackPressed();
        finish();
    }
}