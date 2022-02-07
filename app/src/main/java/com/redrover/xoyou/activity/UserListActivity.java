package com.redrover.xoyou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import com.redrover.xoyou.R;
import com.redrover.xoyou.adapter.UserRecyclerviewAdapter;
import com.redrover.xoyou.common.CommonActivity;
import com.redrover.xoyou.interfaces.DialogClickListener;
import com.redrover.xoyou.interfaces.RelationRecyclerTouchListener;
import com.redrover.xoyou.network.action.ActionRuler;
import com.redrover.xoyou.network.interfaces.ActionResultListener;
import com.redrover.xoyou.model.User;
import com.redrover.xoyou.network.request.ActionRequestDeleteUser;
import com.redrover.xoyou.network.request.ActionRequestUserList;
import com.redrover.xoyou.network.response.DeleteUserResult;
import com.redrover.xoyou.network.response.UserListResult;
import com.redrover.xoyou.storage.JWSharePreference;
import com.redrover.xoyou.utils.CommandUtil;
import com.redrover.xoyou.utils.LogUtil;

public class UserListActivity extends CommonActivity {
    private List<User> userDataList;
    private UserRecyclerviewAdapter userRecyclerviewAdapter;
    private RecyclerView recyclerView;
    private ImageView btn_plus;
    private RelationRecyclerTouchListener touchListener;
    private int seq,deleteUserPosition;
    private JWSharePreference jwSharePreference;
    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_user_list);
        super.onCreate(savedInstanceState);

        initView();
        setRecyclerView();
        jwSharePreference = new JWSharePreference();


        btn_plus.setOnClickListener(v ->{
//            Intent i = new Intent(UserListActivity.this, AddUserActivity.class);
//            startActivityForResult(i,300);
        });

        requestUserList();
    }

    public void requestUserList(){
        String id = jwSharePreference.getString(JWSharePreference.PREFERENCE_LOGIN_ID,"");
        ActionRuler.getInstance().addAction(new ActionRequestUserList(this,id, new ActionResultListener<UserListResult>() {
            @Override
            public void onResponseResult(UserListResult response) {
                try {
                    UserListResult result = response;
                    Log.d("!TEST",result.getData());
                   /* userDataList.addAll(result.getData());*/
                    userDataList.clear();
                    userDataList.addAll(stringToArrayList(result.getData()));
                    count = userDataList.size();

                    userRecyclerviewAdapter.notifyDataSetChanged();

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
    public void initView(){
        btn_plus = findViewById(R.id.btn_plus);
        recyclerView = findViewById(R.id.rcv_user_list);
    }

    public void deleteUser(String SEQ){
        ActionRuler.getInstance().addAction(new ActionRequestDeleteUser(this,SEQ, new ActionResultListener<DeleteUserResult>() {
            @Override
            public void onResponseResult(DeleteUserResult response) {
                try {
                    DeleteUserResult result = response;


                    if(result.isSuccess()){
                        //성공
                        userDataList.remove(deleteUserPosition);
                        userRecyclerviewAdapter.notifyItemRemoved(deleteUserPosition);
                        Log.d("TEST","성공");
                        if(result.getData()!=null){

                        }
                    }else{
                        Log.d("TEST","zzzz");

                    }

                } catch (Exception e) {
                    Log.d("TEST","zzzzzzz");
                    e.printStackTrace();
                }
            }

            @Override
            public void onResponseError(String message) {
                LogUtil.LogD("사용자 삭제 실패 !!");
            }
        }));
        ActionRuler.getInstance().runNext();
    }

    public void setRecyclerView(){
        userDataList = new ArrayList<>();
        userRecyclerviewAdapter = new UserRecyclerviewAdapter(this);


        userRecyclerviewAdapter.setTaskList(userDataList);
        recyclerView.setAdapter(userRecyclerviewAdapter);

        touchListener = new RelationRecyclerTouchListener(this,recyclerView);
        touchListener.setClickable(new RelationRecyclerTouchListener.OnRowClickListener() {
            @Override
            public void onRowClicked(int position) {
                CommandUtil.getInstance().showCommonTwoButtonDialog(UserListActivity.this, CommandUtil.getInstance().getStr(R.string.mong_user_list_change), CommandUtil.getInstance().getStr(R.string.str_cofirm), CommandUtil.getInstance().getStr(R.string.str_cancel), new DialogClickListener() {
                    @Override
                    public void onClick(int button) {
                        switch (button){
                            case 1: //확인
                                //sharePreference에 user정보 저장
                                jwSharePreference.setString(JWSharePreference.PREFERENCE_USER_SEQ,String.valueOf(userDataList.get(position).getSEQ()));
                                jwSharePreference.setString(JWSharePreference.PREFERENCE_LOGIN_NAME,userDataList.get(position).getNAME());
                                jwSharePreference.setString(JWSharePreference.PREFERENCE_LOGIN_NICKNAME,userDataList.get(position).getNICK_NAME());
                                jwSharePreference.setString(JWSharePreference.PREFERENCE_LOGIN_BIRTH,userDataList.get(position).getBIRTH_DATE());
                                jwSharePreference.setInt(JWSharePreference.PREFERENCE_GENDER,userDataList.get(position).getMW());
                                Toast.makeText(getApplicationContext(), "사용자가 변경되었습니다. : " + userDataList.get(position).getNAME(),Toast.LENGTH_SHORT).show();
                                break;
                            case 2:
                                Log.d("Test","취소");
                                break;
                        }
                    }
                });
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
                                Toast.makeText(getApplicationContext(),"삭제",Toast.LENGTH_SHORT).show();
                                seq = userDataList.get(position).getSEQ();

                                CommandUtil.getInstance().showCommonTwoButtonDialog(UserListActivity.this, CommandUtil.getInstance().getStr(R.string.mong_user_list_delete), CommandUtil.getInstance().getStr(R.string.str_cofirm), CommandUtil.getInstance().getStr(R.string.str_cancel), new DialogClickListener() {
                                    @Override
                                    public void onClick(int button) {
                                        switch (button){
                                            case 1:
                                                Log.d("Test","SEQ : "+String.valueOf(userDataList.get(position).getSEQ()));
                                                deleteUserPosition = position;
                                                if((userDataList.get(position).getUSER_TYPE()+"").equals("0")){
                                                    Toast.makeText(UserListActivity.this,CommandUtil.getInstance().getStr(R.string.mong_user_list_delete_error_self),Toast.LENGTH_SHORT).show();
                                                }else{
                                                    deleteUser(String.valueOf(userDataList.get(position).getSEQ()));
                                                }
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
                                //todo 수정화면 이동
                                if(userDataList.get(position).getUSER_TYPE().equals("0") == false){

//                                    String nickname = userDataList.get(position).getNICK_NAME();
//                                    String name = userDataList.get(position).getNAME();
//                                    int mw = userDataList.get(position).getMW();
//                                    String birth = userDataList.get(position).getBIRTH_DATE();
//                                    int SEQ = userDataList.get(position).getSEQ();
//
//                                    Intent i = new Intent(UserListActivity.this, AddUserActivity.class);
//
//                                    i.putExtra("nickname",nickname);
//                                    i.putExtra("SEQ",SEQ);
//                                    i.putExtra("name",name);
//                                    i.putExtra("mw",mw);
//                                    i.putExtra("birth",birth);
//                                    i.putExtra("update",true);
//
//                                    startActivityForResult(i,200);
                                }else{
                                    String nickname = userDataList.get(position).getNICK_NAME();
                                    String name = userDataList.get(position).getNAME();
                                    int mw = userDataList.get(position).getMW();
                                    String birth = userDataList.get(position).getBIRTH_DATE();
                                    int SEQ = userDataList.get(position).getSEQ();

                                    Intent i = new Intent(UserListActivity.this, YourSelfSettingActivity.class);
                                    i.putExtra("nickname",nickname);
                                    i.putExtra("SEQ",SEQ+"");
                                    i.putExtra("name",name);
                                    i.putExtra("mw",mw);
                                    i.putExtra("birth",birth);
                                    startActivityForResult(i,200);

                                }
                                break;


                        }
                    }
                });
        recyclerView.addOnItemTouchListener(touchListener);
    }

    public ArrayList<User> stringToArrayList(String data) throws JSONException {
        JSONArray jsonArray = new JSONArray(data);
        ArrayList<User> arrayList = new ArrayList<>();
        for(int i=0; i<jsonArray.length(); i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            User user = new User();
            user.setSEQ(Integer.parseInt(jsonObject.getString("SEQ")));
            Log.d("test",user.getSEQ()+"");
            user.setUSER_ID(jsonObject.getString("USER_ID"));
            Log.d("test",user.getUSER_ID()+"");
            user.setUSER_TYPE(jsonObject.getString("USER_TYPE"));
            Log.d("test",user.getUSER_TYPE()+"");
            user.setNICK_NAME(jsonObject.getString("NICK_NAME"));
            Log.d("test",user.getNICK_NAME()+"");
            user.setNAME(jsonObject.getString("NAME"));
            Log.d("test",user.getNAME()+"");
            user.setMW(Integer.parseInt(jsonObject.getString("MW")));
            Log.d("test",user.getMW()+"");
            user.setBIRTH_DATE(jsonObject.getString("BIRTH_DATE"));
            Log.d("test",user.getBIRTH_DATE()+"");
            user.setIMAGE_URL(jsonObject.getString("IMAGE_URL"));
            arrayList.add(user);
        }

        return arrayList;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            requestUserList();
        }
    }

//    @Override
//    public void onBackPressed() {
//        Log.d("TEST","RESULT_OK");
//        Intent i = getIntent();
//        i.putExtra("userCount", count);
//        setResult(RESULT_OK, i);
//        Log.v("CHECK2:","userCount : " + count);
//
//        super.onBackPressed();
//        finish();
//    }
}
