package kr.co.genericit.mybase.xoyou2.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import kr.co.genericit.mybase.xoyou2.R;
import kr.co.genericit.mybase.xoyou2.adapter.RelationRecyclerviewAdapter;
import kr.co.genericit.mybase.xoyou2.common.CommonActivity;
import kr.co.genericit.mybase.xoyou2.interfaces.DialogClickListener;
import kr.co.genericit.mybase.xoyou2.interfaces.RelationRecyclerTouchListener;
import kr.co.genericit.mybase.xoyou2.network.action.ActionRuler;
import kr.co.genericit.mybase.xoyou2.network.interfaces.ActionResultListener;
import kr.co.genericit.mybase.xoyou2.model.Relation;
import kr.co.genericit.mybase.xoyou2.network.request.ActionRequestDeleteRelation;
import kr.co.genericit.mybase.xoyou2.network.request.ActionRequestRelationList;
import kr.co.genericit.mybase.xoyou2.network.response.DeleteRelationResult;
import kr.co.genericit.mybase.xoyou2.network.response.RelationListResult;
import kr.co.genericit.mybase.xoyou2.storage.JWSharePreference;
import kr.co.genericit.mybase.xoyou2.utils.CommandUtil;
import kr.co.genericit.mybase.xoyou2.utils.LogUtil;


public class RelationListActivity extends CommonActivity {
    private List<Relation> relationDataList;
    private RelationRecyclerviewAdapter relationRecyclerviewAdapter;
    private RelationRecyclerTouchListener touchListener;
    private RecyclerView recyclerView;
    private int deleteRelationPosition;
    private JWSharePreference jwSharePreference;
    private String USER_ID,SEQ,CURRENT_PAGE="0";
    private ImageView btn_plus;
    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_setting_relation_list);
        super.onCreate(savedInstanceState);

        initView();
        getListData();
    }

    public void initView(){
        jwSharePreference = new JWSharePreference();
        USER_ID = jwSharePreference.getString(JWSharePreference.PREFERENCE_LOGIN_ID,"");

        btn_plus = findViewById(R.id.btn_plus);
        btn_plus.setOnClickListener(v ->{
            Intent i = new Intent(RelationListActivity.this, AddRelationActivity.class);
            startActivity(i);
        });

        relationDataList = new ArrayList<>();
        recyclerView = findViewById(R.id.rcv_relation_list);
        relationRecyclerviewAdapter = new RelationRecyclerviewAdapter(this);

        relationRecyclerviewAdapter.setTaskList(relationDataList);
        recyclerView.setAdapter(relationRecyclerviewAdapter);

        touchListener = new RelationRecyclerTouchListener(this,recyclerView);
        touchListener.setClickable(new RelationRecyclerTouchListener.OnRowClickListener() {
                    @Override
                    public void onRowClicked(int position) {
//                        Toast.makeText(getApplicationContext(), relationListDataList.get(position).getName(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onIndependentViewClicked(int independentViewID, int position) {

                    }
                })
                .setSwipeOptionViews(R.id.delete_task,R.id.edit_task)
                .setSwipeable(R.id.rowFG, R.id.rowBG, new RelationRecyclerTouchListener.OnSwipeOptionsClickListener() {
                    @Override
                    public void onSwipeOptionClicked(int viewID, int position) {
                        switch (viewID){
                            case R.id.delete_task:
                                //todo 삭제
                                SEQ = String.valueOf(relationDataList.get(position).getSEQ());

                                CommandUtil.getInstance().showCommonTwoButtonDialog(RelationListActivity.this, CommandUtil.getInstance().getStr(R.string.mong_relation_list_delete), CommandUtil.getInstance().getStr(R.string.str_cofirm), CommandUtil.getInstance().getStr(R.string.str_cancel), new DialogClickListener() {
                                    @Override
                                    public void onClick(int button) {
                                        switch (button){
                                            case 1:
                                                Log.d("Test","확인");
                                                deleteRelationPosition = position;
                                                deleteRelationItem();
                                                break;
                                            case 2:
                                                Log.d("Test","취소");
                                                break;
                                        }
                                    }
                                });


//                                relationListDataList.remove(position);
//                                relationRecyclerviewAdapter.setTaskList(relationListDataList);
                                break;
                            case R.id.edit_task:
                                //todo 수정화면 이동?
                                int seq = relationDataList.get(position).getSEQ();
                                String relation = relationDataList.get(position).getGWANGYE();
                                String name = relationDataList.get(position).getNAME();
                                String birth = relationDataList.get(position).getBIRTH_DATE();

                                Intent i = new Intent(RelationListActivity.this, AddRelationActivity.class);
                                i.putExtra("seq",seq);
                                i.putExtra("relation",relation);
                                i.putExtra("name",name);
                                i.putExtra("birth",birth);
                                startActivity(i);

                            break;


                        }
                    }
                });
        recyclerView.addOnItemTouchListener(touchListener);
    }

    private void deleteRelationItem(){
        ActionRuler.getInstance().addAction(new ActionRequestDeleteRelation(this,SEQ, new ActionResultListener<DeleteRelationResult>() {
            @Override
            public void onResponseResult(DeleteRelationResult response) {
                try {
                    DeleteRelationResult result = response;

                    Log.d("TEST","관계인 삭제");

                    if(result.isSuccess()){
                        //성공
                        Log.d("TEST","관계인 삭제 성공");
                        relationDataList.remove(deleteRelationPosition);
                        relationRecyclerviewAdapter.notifyItemRemoved(deleteRelationPosition);
                    }else{

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onResponseError(String message) {
                LogUtil.LogD("관계인 삭제 실패 !!");
            }
        }));
        ActionRuler.getInstance().runNext();
    }

    private void getListData(){
        //todo 리스트가져오기 API
//        relationListDataList = new ArrayList<>();

        requestRelationList();


        //testData
        /*RelationListData relationListData;
        for(int i=0; i<10; i++){
            relationListData = new RelationListData(i,"마이몽"+i,"(친구, 여성 23세 토끼띠 "+ i+ " )");
            relationListDataList.add(relationListData);
        }*/
    }

    private void requestRelationList(){
        ActionRuler.getInstance().addAction(new ActionRequestRelationList(this,USER_ID,CURRENT_PAGE, new ActionResultListener<RelationListResult>() {
            @Override
            public void onResponseResult(RelationListResult response) {
                try {
                    RelationListResult result = response;


                    if(result.isSuccess()){
                        //성공
                        if(result.getData()!=null){
                           relationDataList.clear();
                           relationDataList.addAll(stringToArrayList(result.getData()));
                           count = relationDataList.size();
                           relationRecyclerviewAdapter.notifyDataSetChanged();
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

    public ArrayList<Relation> stringToArrayList(String data) throws JSONException {
        JSONArray jsonArray = new JSONArray(data);
        ArrayList<Relation> arrayList = new ArrayList<>();
        for(int i=0; i<jsonArray.length(); i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            Relation relation = new Relation();

            relation.setSEQ(jsonObject.getInt("SEQ"));
            relation.setIN_SEQ(jsonObject.getInt("IN_SEQ"));
            relation.setUSER_ID(jsonObject.getString("USER_ID"));
            relation.setGWANGYE(jsonObject.getString("GWANGYE"));
            relation.setNAME(jsonObject.getString("NAME"));
            relation.setMW(jsonObject.getInt("MW"));
            relation.setBIRTH_DATE(jsonObject.getString("BIRTH_DATE"));
            relation.setIMAGE_URL(jsonObject.getString("IMAGE_URL"));
            relation.setJUSEO_SEQ(jsonObject.getInt("JUSEO_SEQ"));
            relation.setREG_DATE(jsonObject.getString("REG_DATE"));
            relation.setUPD_DATE(jsonObject.getString("UPD_DATE"));

            arrayList.add(relation);
        }
        return arrayList;
    }
}