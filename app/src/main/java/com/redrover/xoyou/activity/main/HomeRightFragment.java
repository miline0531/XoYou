package com.redrover.xoyou.activity.main;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import com.redrover.xoyou.R;
import com.redrover.xoyou.activity.MainActivity;
import com.redrover.xoyou.adapter.HomeRecyclerviewAdapter;
import com.redrover.xoyou.model.Mong;
import com.redrover.xoyou.network.action.ActionRuler;
import com.redrover.xoyou.network.interfaces.ActionResultListener;
import com.redrover.xoyou.network.request.ActionRequestMyMongList;
import com.redrover.xoyou.network.response.MyMongResult;
import com.redrover.xoyou.storage.JWSharePreference;
import com.redrover.xoyou.utils.LogUtil;


public class HomeRightFragment extends Fragment {
    private RecyclerView rcv_home;
    private HomeRecyclerviewAdapter adapter;
    private int currentPage = 0;
    private JWSharePreference jwSharePreference;
    private ArrayList<Mong> mongList;
    private boolean isLoading = false;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_home_right, container, false);

        initView(view);

        return view;
    }

    public void initView(View v){
        rcv_home = v.findViewById(R.id.rcv_home);

        initRecycler();
    }

    private void initRecycler(){
        jwSharePreference = new JWSharePreference();
        mongList = new ArrayList<>();


        rcv_home.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new HomeRecyclerviewAdapter(requireContext(),mongList);


        requestMyMongList("1",mongList);



        HomeRecyclerviewAdapter.listOnClickListener listOnClickListener = new HomeRecyclerviewAdapter.listOnClickListener() {
            @Override
            public void onClickItem(int id, int action) {
                ((MainActivity)getActivity()).setMainTopText(mongList.get(id).getMONG_HAE_INFO());
                adapter.setSelected(id);
            }
        };
        adapter.setListOnClickListener(listOnClickListener);
        rcv_home.setAdapter(adapter);
        initScrollListener();
    }

    private void initScrollListener() {
        rcv_home.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);


            }


            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (layoutManager != null && layoutManager.findLastCompletelyVisibleItemPosition()==mongList.size()-1) {
                    //????????? ????????? ?????? ????????? direction : -1.?????????, 1.?????????
                    //????????? ?????????
                    isLoading = true;
                    requestReadMoreMyMongList("1",mongList);
                    Log.d("TEST","?????????");
                    Log.d("TEST","position : " + layoutManager.findLastCompletelyVisibleItemPosition());

                }
            }
        });
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
                        adapter.notifyItemRangeInserted(positionStart,itemCount);
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
                //Toast.makeText(getContext(), "??? ???????????? ??????.\n?????? ??? ?????? ??????????????????.",Toast.LENGTH_SHORT).show();
                LogUtil.LogD("??? ???????????? ??????!!!!");
            }
        }));
        ActionRuler.getInstance().runNext();
    }
    private void requestMyMongList(String viewType, ArrayList<Mong> dataList){
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
                        adapter.notifyDataSetChanged();
                        currentPage++;
                    }else{

                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onResponseError(String message) {
                //Toast.makeText(getContext(), "??? ???????????? ??????.\n?????? ??? ?????? ??????????????????.",Toast.LENGTH_SHORT).show();
                LogUtil.LogD("??? ???????????? ??????!!!!");
            }
        }));
        ActionRuler.getInstance().runNext();
    }

    public ArrayList<Mong> stringToArrayList(String data) throws JSONException {
        JSONArray jsonArray = new JSONArray(data);
        ArrayList<Mong> arrayList = new ArrayList<>();
        for(int i=0; i<jsonArray.length(); i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            Mong mong = new Mong();
            //TODO ????????? ??????
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
            mong.setJUSEO_SEQ(jsonObject.getInt("JUSEO_SEQ"));
            mong.setREG_DATE(jsonObject.getString("REG_DATE"));
            mong.setMONG_JST(jsonObject.getString("MONG_JST"));
            mong.setCURRENT_PAGE(jsonObject.getInt("CURRENT_PAGE"));
            mong.setMONG_HAE_INFO(jsonObject.getString("MONG_HAE_INFO"));
            Log.d("TEST","SEQ : " + mong.getSEQ());
            arrayList.add(mong);
        }

        return arrayList;
    }
}