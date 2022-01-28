package kr.co.genericit.mybase.xoyou2.activity.main;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.AppBarLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import kr.co.genericit.mybase.xoyou2.R;
import kr.co.genericit.mybase.xoyou2.activity.MainActivity;
import kr.co.genericit.mybase.xoyou2.activity.MymongBidActivity;
import kr.co.genericit.mybase.xoyou2.adapter.ManageHorizontalRecyclerviewAdapter;
import kr.co.genericit.mybase.xoyou2.adapter.StoreVerticalRecyclerviewAdapter;
import kr.co.genericit.mybase.xoyou2.network.action.ActionRuler;
import kr.co.genericit.mybase.xoyou2.network.interfaces.ActionResultListener;
import kr.co.genericit.mybase.xoyou2.model.Mong;
import kr.co.genericit.mybase.xoyou2.network.request.ActionRequestStoreList;
import kr.co.genericit.mybase.xoyou2.network.response.StoreListResult;
import kr.co.genericit.mybase.xoyou2.storage.JWSharePreference;
import kr.co.genericit.mybase.xoyou2.utils.LogUtil;
import kr.co.genericit.mybase.xoyou2.utils.RecyclerDecoration;


public class StoreFragment extends Fragment {

    private Context mContext;
    private TextView txt_date_search,txt_event,txt_bid,txt_store;
    private LinearLayout btn_date_search;
    private JWSharePreference jwSharePreference;
    private ArrayList<Mong> dateList,eventList,auctionList;

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
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_store, container, false);

        rcv_horizontal_1 = v.findViewById(R.id.rcv_manage_list_horizontal1);
        rcv_horizontal_2 = v.findViewById(R.id.rcv_manage_list_horizontal2);
        rcv_vertical = v.findViewById(R.id.rcv_manage_list_vertical);
        btn_date_search = v.findViewById(R.id.btn_date_search);
        txt_date_search = v.findViewById(R.id.txt_date_search);

        txt_event = v.findViewById(R.id.txt_event);
        txt_bid = v.findViewById(R.id.txt_bid);
        txt_store = v.findViewById(R.id.txt_store);



        txt_event.setOnClickListener(view ->{
            Toast.makeText(mContext, "준비중입니다.", Toast.LENGTH_SHORT).show();
        });
        txt_bid.setOnClickListener(view ->{
            Toast.makeText(mContext, "준비중입니다.", Toast.LENGTH_SHORT).show();
        });
        txt_store.setOnClickListener(view ->{
            Toast.makeText(mContext, "준비중입니다.", Toast.LENGTH_SHORT).show();
        });



        datePickerCreate();
        dateList = new ArrayList<>();
        eventList = new ArrayList<>();
        auctionList = new ArrayList<>();
        setRecycler();
        requestStoreList();

        v.findViewById(R.id.btn_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).MainOpenDrawer();
            }
        });


        btn_date_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog.show();
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
        return v;
    }

    private void requestStoreList(){
        jwSharePreference = new JWSharePreference();
        int userId = jwSharePreference.getInt(JWSharePreference.PREFERENCE_SRL,0);
        ActionRuler.getInstance().addAction(new ActionRequestStoreList(getActivity(),String.valueOf(userId), new ActionResultListener<StoreListResult>() {

            @Override
            public void onResponseResult(StoreListResult response) {
                try {
                    StoreListResult result = response;
                    Log.d("TEST",result.getData().toString());

                    if(result.isSuccess()){
                        dateList.clear();
                        dateList.addAll(stringToArrayList(result.getData(),"date"));
                        eventList.clear();
                        eventList.addAll(stringToArrayList(result.getData(),"event"));
                        auctionList.clear();
                        auctionList.addAll(stringToArrayList(result.getData(),"auction"));
                        adapter_vertical.notifyDataSetChanged();
                        adapter_horizontal1.notifyDataSetChanged();
                        adapter_horizontal2.notifyDataSetChanged();
                        Log.v("ifeelbluu", "dateList :: " + dateList.size());
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

    //Manage화면
    private RecyclerView rcv_horizontal_1,rcv_horizontal_2,rcv_vertical;
    private ManageHorizontalRecyclerviewAdapter adapter_horizontal1;
    private ManageHorizontalRecyclerviewAdapter adapter_horizontal2;
    private StoreVerticalRecyclerviewAdapter adapter_vertical;
    private ArrayList<String> data = new ArrayList<>();
    private ArrayList<String> data2 = new ArrayList<>();
    private ArrayList<String> data3 = new ArrayList<>();
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
        adapter_vertical = new StoreVerticalRecyclerviewAdapter(mContext,dateList);
        adapter_vertical.setListOnClickListener(onClick);
        rcv_vertical.setAdapter(adapter_vertical);

        RecyclerDecoration spaceDecoration3 = new RecyclerDecoration(10,"vertical");
        rcv_vertical.addItemDecoration(spaceDecoration3);
    }

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

    public StoreVerticalRecyclerviewAdapter.listOnClickListener onClick = new StoreVerticalRecyclerviewAdapter.listOnClickListener() {
        @Override
        public void onClickItem(int id, int action) {
            Log.v("ifeelbluu","SEQ : " + dateList.get(id).getSTORE_ID() );
            Intent i = new Intent(getActivity(), MymongBidActivity.class);
            i.putExtra("SEQ",dateList.get(id).getSTORE_ID()+"");
            startActivity(i);
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private DatePickerDialog datePickerDialog;
    private String birthday="";

    public void datePickerCreate(){
        Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                birthday = changeBirthdayToString(year, month, dayOfMonth);
                txt_date_search.setText(birthday);
            }
        }, mYear, mMonth, mDay);
    }

    public String changeBirthdayToString(int year, int month, int dayOfMonth){
        String monthText = "";
        String dayText = "";
        if(month<9){
            monthText = "0"+(month+1);
        }else{
            monthText = String.valueOf(month+1);
        }
        if(dayOfMonth<10){
            dayText = "0"+dayOfMonth;
        }else{
            dayText = String.valueOf(dayOfMonth);
        }

        return ""+year+monthText+dayText;
    }
    public ArrayList<Mong> stringToArrayList(String data, String type) throws JSONException {
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
}