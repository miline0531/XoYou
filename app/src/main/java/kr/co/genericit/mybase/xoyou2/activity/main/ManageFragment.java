package kr.co.genericit.mybase.xoyou2.activity.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.AppBarLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import kr.co.genericit.mybase.xoyou2.R;
import kr.co.genericit.mybase.xoyou2.activity.MainActivity;
import kr.co.genericit.mybase.xoyou2.activity.MymongBidActivity;
import kr.co.genericit.mybase.xoyou2.adapterxo.XoYouUserLoadRecyclerviewAdapter;
import kr.co.genericit.mybase.xoyou2.common.Constants;
import kr.co.genericit.mybase.xoyou2.model.SimRi;
import kr.co.genericit.mybase.xoyou2.network.action.ActionRuler;
import kr.co.genericit.mybase.xoyou2.network.interfaces.ActionResultListener;
import kr.co.genericit.mybase.xoyou2.network.requestxo.ActionRequestXoYouUserLoad;
import kr.co.genericit.mybase.xoyou2.network.response.DefaultResult;
import kr.co.genericit.mybase.xoyou2.storage.JWSharePreference;
import kr.co.genericit.mybase.xoyou2.utils.LogUtil;
import kr.co.genericit.mybase.xoyou2.utils.RecyclerDecoration;


public class ManageFragment extends Fragment {
    private Context mContext;
    private String mCurrentTabIndex = "1";
    private NestedScrollView scrollView;
    private JWSharePreference jwSharePreference;
    private ArrayList<SimRi> dataList;
    private boolean isLoading = false;
    private int currentPage = 0;
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
            //requestMyMongList(mCurrentTabIndex,data);
        }

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_manage, container, false);

        jwSharePreference = new JWSharePreference();
        rcv_vertical = v.findViewById(R.id.rcv_manage_list_vertical);
        setRecycler();

        initData();

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
            resultStr = Constants.MainData.getString("ManageTitle");
        } catch (Exception e) {
            e.printStackTrace();
        }
        //top_result.setText(resultStr);
        return v;
    }

    public void initData(){
        requestMyMongList(mCurrentTabIndex,data);
    }

    private boolean isBidMove = false;
    public XoYouUserLoadRecyclerviewAdapter.listOnClickListener onClick = new XoYouUserLoadRecyclerviewAdapter.listOnClickListener() {
        @Override
        public void onClickItem(int id, int action) {
            Intent i = new Intent(getActivity(), MymongBidActivity.class);
            i.putExtra("SEQ",data.get(id).getId());
            startActivity(i);
            isBidMove = true;
        }
    };


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    //Manage화면
    private RecyclerView rcv_vertical;
    private RecyclerView.Adapter adapter_horizontal1;
    private XoYouUserLoadRecyclerviewAdapter adapter_vertical;
    private LinearLayout btn1,btn2,btn3,btn4;
    private ArrayList<SimRi> data = new ArrayList<>();
    public void setRecycler(){

        rcv_vertical.setLayoutManager(new LinearLayoutManager(mContext));
        adapter_vertical = new XoYouUserLoadRecyclerviewAdapter(mContext,data);
        adapter_vertical.setListOnClickListener(onClick);
        rcv_vertical.setAdapter(adapter_vertical);

        RecyclerDecoration spaceDecoration3 = new RecyclerDecoration(10,"vertical");
        rcv_vertical.addItemDecoration(spaceDecoration3);

        initScrollListener();
    }

    private ArrayList<TextView> tabText;
    private ArrayList<View> tabUnderBar;

    private void requestMyMongList(String viewType,ArrayList<SimRi> dataList){
        String userId = String.valueOf(jwSharePreference.getString(JWSharePreference.PREFERENCE_LOGIN_ID,""));
        ActionRuler.getInstance().addAction(new ActionRequestXoYouUserLoad(getActivity(),userId, new ActionResultListener<DefaultResult>() {

            @Override
            public void onResponseResult(DefaultResult response) {
                try {
                    DefaultResult result = response;
                    Log.d("TEST",result.getData().toString());

                    if(result.isSuccess()){
                        dataList.clear();
                        dataList.addAll(stringToArrayList(result.getData()));
                        Log.d("TEST",dataList.size()+"");
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
                    //requestReadMoreMyMongList(mCurrentTabIndex,data);
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

    public ArrayList<SimRi> stringToArrayList(String data) throws JSONException {
        JSONObject jsonObj = new JSONObject(data);
        JSONArray jsonArray = new JSONArray(jsonObj.getString("listSimRi"));
        ArrayList<SimRi> arrayList = new ArrayList<>();
        for(int i=0; i<jsonArray.length(); i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            SimRi simri = new SimRi();
            //TODO 데이터 추가
            simri.setId(jsonObject.getInt("Id"));
            simri.setNo(jsonObject.getInt("No"));
            simri.setNickName(jsonObject.getString("NickName"));
            simri.setName(jsonObject.getString("Name"));
            simri.setUserInfo(jsonObject.getString("UserInfo"));
            simri.setGwanInfo(jsonObject.getString("GwanInfo"));
            simri.setSimRiInfo(jsonObject.getString("SimRiInfo"));
            simri.setValue(jsonObject.getString("Value"));
            simri.setiDou(jsonObject.getDouble("iDou"));
            simri.setXO(jsonObject.getBoolean("XO"));

            Log.d("TEST","ID : " + simri.getId());
            arrayList.add(simri);
        }

        return arrayList;
    }
}