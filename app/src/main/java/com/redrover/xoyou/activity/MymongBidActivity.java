package com.redrover.xoyou.activity;

import android.content.Intent;
import android.graphics.Color;
import android.icu.text.DecimalFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import com.redrover.xoyou.R;
import com.redrover.xoyou.activity.detail.MyMongRegistActivity;
import com.redrover.xoyou.adapter.BidRecyclerviewAdapter;
import com.redrover.xoyou.common.CommonActivity;
import com.redrover.xoyou.common.Constants;
import com.redrover.xoyou.databinding.ActivityBidBinding;
import com.redrover.xoyou.model.BidSale;
import com.redrover.xoyou.model.Mong;
import com.redrover.xoyou.network.action.ActionRuler;
import com.redrover.xoyou.network.interfaces.ActionResultListener;
import com.redrover.xoyou.network.request.ActionRequestBidMong;
import com.redrover.xoyou.network.request.ActionRequestBuyMong;
import com.redrover.xoyou.network.request.ActionRequestCoinData;
import com.redrover.xoyou.network.request.ActionRequestGetMong;
import com.redrover.xoyou.network.response.BidMongResult;
import com.redrover.xoyou.network.response.BuyMongResult;
import com.redrover.xoyou.network.response.CoinDataResult;
import com.redrover.xoyou.network.response.GetMongResult;
import com.redrover.xoyou.storage.JWSharePreference;
import com.redrover.xoyou.utils.CommandUtil;
import com.redrover.xoyou.utils.LogUtil;
import com.redrover.xoyou.view.BidDialog;


public class MymongBidActivity extends CommonActivity {

    private ActivityBidBinding binding;

    private JWSharePreference jwSharePreference;
    private Mong mong;
    private ArrayList<Mong> bidList;
    private ArrayList<BidSale> saleList;


    @Override
    protected void onResume() {
        super.onResume();
        if(seq!=null && seq.equals("") == false)
        requestMong(seq);
//        initData();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityBidBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        super.onCreate(savedInstanceState);
        initData();
    }

    private String seq;
    public void initData(){
        mong = new Mong();
        bidList = new ArrayList<>();
        saleList = new ArrayList<>();
        seq = getIntent().getStringExtra("SEQ");
//        requestMong(seq);
    }

    public void initView(){
        binding.title.setText(mong.getTITLE());
        binding.note.setText(mong.getNOTE());
        binding.userName.setText(mong.getUSER_NAME());
        binding.bidValue.setText(mong.getBID_VALUE());
        Log.v("ifeelbluu", "URL :: "  + mong.getIMAGE_URL());

        Glide.with(this).load(mong.getIMAGE_URL()).thumbnail(0.1f).centerCrop().into(binding.image);

        try{
            String[] startDate = stringToDateArr(mong.getSTART_DATE());
            String[] endDate = stringToDateArr(mong.getEND_DATE());
            binding.startDate.setText(stringToDate(startDate[0]));
            String startString="";
            for(int i=0;i<startDate.length-1;i++){
                startString = startString.concat(startDate[i+1]);
            }
            binding.startTime.setText(startString);

            binding.endDate.setText(stringToDate(endDate[0]));
            String endString="";
            for(int i=0;i<endDate.length-1;i++){
                endString = endString.concat(endDate[i+1]);
            }
            binding.endTime.setText(endString);
        }catch (Exception e){

        }

        BidRecyclerviewAdapter adapter = new BidRecyclerviewAdapter(this,bidList);
        binding.rcvBid.setLayoutManager(new LinearLayoutManager(this));
        binding.rcvBid.setAdapter(adapter);


        switch (mong.getTRANS_TYPE()){
            case 1:
                binding.sellType.setText(CommandUtil.getInstance().getStr(R.string.mong_bid_constant_price));
                binding.txtCurrentPrice.setText(CommandUtil.getInstance().getStr(R.string.mong_bid_sell_price));
                binding.bidValue.setText(mong.getMIN_PRICE());
                binding.txtSellTitle.setVisibility(View.GONE);
                binding.minPrice.setVisibility(View.GONE);
                binding.layoutBidPrice.setVisibility(View.VISIBLE);
                break;
            case 2:
                binding.sellType.setText(CommandUtil.getInstance().getStr(R.string.mong_bid_auction));
                binding.txtSellTitle.setText(CommandUtil.getInstance().getStr(R.string.mong_bid_action_start));
                binding.minPrice.setText(mong.getMIN_PRICE());
                binding.layoutBidList.setVisibility(View.VISIBLE);
                break;
//            case 3:
//                binding.sellType.setText(CommandUtil.getInstance().getStr(R.string.mong_bid_auth));
//                binding.txtSellTitle.setText(CommandUtil.getInstance().getStr(R.string.mong_bid_sell_recommend_price));
//                binding.layoutBidPrice.setVisibility(View.GONE);
//                break;
            default:
                binding.sellType.setText(CommandUtil.getInstance().getStr(R.string.mong_bid_auth));
                binding.txtSellTitle.setText(CommandUtil.getInstance().getStr(R.string.mong_bid_sell_recommend_price));
                binding.layoutBidPrice.setVisibility(View.GONE);
                break;
        }


        String status = "";
        Log.v("ifeelbluu", " mong.getTRANS_STATUS :: " + mong.getTRANS_STATUS());
        switch (mong.getTRANS_STATUS()){
            case 0 : //권한없음
                status = CommandUtil.getInstance().getStr(R.string.mong_bid_status_permission_not);
                binding.btnSubmit.setVisibility(View.GONE);
                break;
            case 1 : //인증
                status = CommandUtil.getInstance().getStr(R.string.mong_bid_status_auth);
                break;
            case 2 : //판매
                status = CommandUtil.getInstance().getStr(R.string.mong_bid_status_sell);
                break;
            case 3 : //구매
                status = CommandUtil.getInstance().getStr(R.string.mong_bid_status_buy);
                break;
            case 4 : //입찰
                status = CommandUtil.getInstance().getStr(R.string.mong_bid_status_auction);
                break;
        }
        binding.btnSubmit.setText(status);
        binding.btnSubmit.setOnClickListener(v ->{
            switch (mong.getTRANS_STATUS()){
                case 0 : //권한없음
                    break;
                case 1 : //인증
                    Constants.mongSEQ = seq;//SEQ
                    Constants.MONG_SRL = mong.getSTORE_ID();//SRL
                    Constants.mongTitle = mong.getTITLE();
                    Constants.mongNote = mong.getNOTE();
                    Intent reg = new Intent(MymongBidActivity.this, MyMongRegistActivity.class);
                    startActivity(reg);
                    break;
                case 2 : //판매
                    //requestSellMong();
                    Intent i = new Intent(MymongBidActivity.this, SellMongActivity.class);
                    i.putExtra("srl",mong.getSTORE_ID());
                    i.putExtra("description",mong.getNOTE());
                    i.putExtra("title",mong.getTITLE());
                    startActivity(i);
                    break;
                case 3 : //구매
                    if(Constants.myCoinValue.equals("0")){
                        CommandUtil.getInstance().showCommonOneButtonDefaultDialog(MymongBidActivity.this,getResources().getString(R.string.bid_error_empty_coin));
                        return;
                    }
                    requestBuyMong();
                    break;
                case 4 : //입찰
                    if(Constants.myCoinValue.equals("0")){
                        CommandUtil.getInstance().showCommonOneButtonDefaultDialog(MymongBidActivity.this,getResources().getString(R.string.bid_error_empty_coin));
                        return;
                    }
                    showBidDialog();
                    break;
            }
        });

        binding.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MymongBidActivity.this,CardImageActivity.class);
                i.putExtra("imagePath",mong.getIMAGE_URL());
                startActivity(i);
            }
        });
        /**
         *  *추가항목
         * */
//        binding.tvMongType, binding.txtMongCost  => 노란색
//        binding.txtMongDate => 회색
//        나머지 흰색
//        binding.tvMongType.setText("사고꿈");
//        binding.tvMongStatus.setText("인증완료");
//        binding.txtMongDate.setText(" ・ 록일: 2022년 01월26일 14시57분(수)");
//        binding.txtMongTitle.setText(" ・ 제목 : 길을 가다가 검은 ");
//        binding.txtMongPlace.setText(" ・ 꿈관련 장소수 : 10 개");
//        binding.txtMongNamEunSu.setText(" ・ 적용기간 남은수: 0 개 ");
//        binding.txtMongHyenSilPer.setText(" ・ 현실율 : 96.48%");
//        binding.txtMongCost.setText(" ・ 판매금액 : 65,170.00원");
//        binding.txtMongSubTitle.setText("몸을 허락하다 허허하다가 회계가 알아보는 의미를 가집니다.");


    }

    ArrayList<BarEntry> list = new ArrayList<>();
    LineData linelist = null;
    LineData resultlinelist = null;
    String[] labels;

    public void setSale(ArrayList<BidSale> saleList){
        list = new ArrayList<>();
        labels = new String[saleList.size()];

        ArrayList<Entry> entries = new ArrayList<Entry>();
        ArrayList<Entry> resultentries = new ArrayList<Entry>();

        for(int i=0; i<saleList.size(); i++){
            Log.v("ifeelbluu","saleList :: " + saleList.get(i).getBase_iDou() );
            //barList
            list.add(new BarEntry(i, Float.valueOf(saleList.get(i).getBase_iDou())));
            //labels
            labels[i] = saleList.get(i).getMongName();
            //lineList

            entries.add(new Entry(i, Float.parseFloat(saleList.get(i).getHap_iDou())));
            resultentries.add(new Entry(i, Float.parseFloat(saleList.get(i).getVal_iDou())));
        }

        LineDataSet lineDataSet = new LineDataSet(entries, "Line");
        LineDataSet resultlineDataSet = new LineDataSet(entries, "Line2");

        linelist = new LineData(lineDataSet);
        resultlinelist = new LineData(resultlineDataSet);

        LineDataSet set = new LineDataSet(entries, "Line DataSet1");
        set.setColor(ContextCompat.getColor(this, R.color.btn_ok_color));
        set.setLineWidth(1.5f);
        set.setCircleColor(ContextCompat.getColor(this, R.color.btn_ok_color));
        set.setCircleSize(3f);
        set.setFillColor(ContextCompat.getColor(this, R.color.btn_ok_color));
//        set.setDrawCubic(true);
        set.setDrawValues(true);
        set.setValueTextSize(10f);
        set.setValueTextColor(Color.WHITE);
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
//        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        linelist.addDataSet(set);

        LineDataSet set2 = new LineDataSet(resultentries, "Line DataSet2");
        set2.setColor(ContextCompat.getColor(this, R.color.progress_level2));
        set2.setLineWidth(1.5f);
        set2.setCircleColor(ContextCompat.getColor(this, R.color.progress_level2));
        set2.setCircleSize(3f);
        set2.setFillColor(ContextCompat.getColor(this, R.color.progress_level2));
//        set.setDrawCubic(true);
        set2.setDrawValues(true);
        set2.setValueTextSize(10f);
        set2.setValueTextColor(Color.WHITE);
        set2.setAxisDependency(YAxis.AxisDependency.LEFT);
//        set2.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        resultlinelist.addDataSet(set2);

    }
    private void requestBuyMong(){
        ActionRuler.getInstance().addAction(new ActionRequestBuyMong(this,Integer.parseInt(mong.getSTORE_ID()),mong.getUSER_ID(), new ActionResultListener<BuyMongResult>() {

            @Override
            public void onResponseResult(BuyMongResult response) {
                try {
                    BuyMongResult result = response;

                    if(result.getResult()){
                        requestMong(seq);
                    }else{
                        Log.v("ifeelbluu", "result.getError() : " + result.getError());
                        Log.v("ifeelbluu", "result.getError_desc() : " + result.getError_desc());
                        if("51001".equals(result.getError())){
                            CommandUtil.getInstance().showCommonOneButtonDefaultDialog(MymongBidActivity.this,getResources().getString(R.string.bid_error_buy_time_buy_invalid));
                        }else{
                            CommandUtil.getInstance().showCommonOneButtonDefaultDialog(MymongBidActivity.this,getResources().getString(R.string.bid_error_buy));
                        }
                    }
                    Log.d("Buy TEST",""+result.getResult());

                } catch (Exception e) {
                    CommandUtil.getInstance().showCommonOneButtonDefaultDialog(MymongBidActivity.this,getResources().getString(R.string.bid_error_buy_fail));
                }
            }

            @Override
            public void onResponseError(String message) {
                CommandUtil.getInstance().showCommonOneButtonDefaultDialog(MymongBidActivity.this,getResources().getString(R.string.bid_error_buy_fail));
            }
        }));
        ActionRuler.getInstance().runNext();
    }

    public void showBidDialog(){
        BidDialog dialog = new BidDialog(this, new BidDialog.CustomDialogClickListener() {
            @Override
            public void onPositiveClick(String bid) {
                int bidInt = Integer.parseInt(bid);
                if(bidInt<=Integer.parseInt(mong.getBID_VALUE())){
                    Toast.makeText(MymongBidActivity.this,CommandUtil.getInstance().getStr(R.string.mong_bid_auction_error_low),Toast.LENGTH_SHORT).show();
                }else{
                    requestBidMong(bidInt);
                }
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
    private void requestBidMong(int bid_value){

        ActionRuler.getInstance().addAction(new ActionRequestBidMong(this,bid_value,Integer.parseInt(mong.getSTORE_ID()),
                new ActionResultListener<BidMongResult>() {
            @Override
            public void onResponseResult(BidMongResult response) {
                try {
                    BidMongResult result = response;

                    if(result.getResult()){
                        requestMong(seq);
                    }else{
                        CommandUtil.getInstance().showCommonOneButtonDefaultDialog(MymongBidActivity.this,getResources().getString(R.string.bid_error_bid));
                    }
//
//                    Log.d("Bid TEST Result : ",""+result.getResult());
//                    Log.d("Bid TEST Error : ",result.getError());
//                    Log.d("Bid TEST Error_desc : ",result.getError_desc());

                } catch (Exception e) {
                    CommandUtil.getInstance().showCommonOneButtonDefaultDialog(MymongBidActivity.this,getResources().getString(R.string.bid_error_bid_fail));
                    e.printStackTrace();
                }
            }

            @Override
            public void onResponseError(String message) {
                CommandUtil.getInstance().showCommonOneButtonDefaultDialog(MymongBidActivity.this,getResources().getString(R.string.bid_error_bid_fail));
            }
        }));
        ActionRuler.getInstance().runNext();
    }
    private void requestMong(String seq){
        jwSharePreference = new JWSharePreference();
        String userId = jwSharePreference.getString(JWSharePreference.PREFERENCE_LOGIN_ID,"");
        ActionRuler.getInstance().addAction(new ActionRequestGetMong(this,userId,seq, new ActionResultListener<GetMongResult>() {

            @Override
            public void onResponseResult(GetMongResult response) {
                try {


                    GetMongResult result = response;
                    JSONObject object = new JSONObject(result.getData());

                    Log.v("ifeelbluu","mongdat :: " + result.getData());
                    String mongDataString = object.getString("mong_data");
                    JSONObject mongData = new JSONObject(mongDataString);
                    mong = jsonToMong(mongData);
                    Log.d("mong",mong.getSTART_DATE());

                    String listBidString = object.getString("list_bid");
                    JSONArray listBidArray = new JSONArray(listBidString);
                    bidList.clear();
                    for(int i=0; i<listBidArray.length(); i++){
                        bidList.add(jsonToMong(listBidArray.getJSONObject(i)));
                    }

                    String listSaleString = object.getString("list_sale");
                    JSONArray listSaleArray = new JSONArray(listSaleString);
                    saleList.clear();
                    for(int i=0; i<listSaleArray.length(); i++){
                        saleList.add(jsonToBidSale(listSaleArray.getJSONObject(i)));
                    }
                    if(saleList.size() != 0) setSale(saleList);

                    Log.d("ifeelbluu",result.getData());
                    initView();

                    if(saleList == null ||saleList.size() ==0|| list == null || list.size() == 0){
                        Log.v("ifeelbluu","list.size() :: null");
                        binding.chartTitle.setVisibility(View.GONE);
                        binding.barchart.setVisibility(View.GONE);
                    }else{
                        Log.v("ifeelbluu","list.size() :: " + list.size() );
                        setCombindCart(binding.barchart,linelist,list,labels,CommandUtil.getInstance().getStr(R.string.mong_bid_cartegory));

                        String resultText = "";
                        for(int i=0;i<list.size(); i++){
                            float dreamDou = list.get(i).getY();
                            float myDou = linelist.getDataSets().get(0).getEntryForIndex(i).getY();
                            if(dreamDou - myDou >= 0.5f){
                                if(resultText.equals("") == false){
                                    resultText += "\n";
                                }
                                resultText += CommandUtil.getInstance().getStr(R.string.mong_bid_result_text_1) + labels[i] + CommandUtil.getInstance().getStr(R.string.mong_bid_result_text_2);
                            }
                        }

                        binding.chartTextResult.setText(resultText);

                    }

                    requestCoinData();

                    if(result.isSuccess()){/*
                        dataList.clear();
                        dataList.addAll(stringToArrayList(result.getData()));
                        adapter_vertical.notifyDataSetChanged();*/
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

    private void setCombindCart(CombinedChart chart, LineData linedata, ArrayList<BarEntry> list, String[] labels, String category){
        chart.setTouchEnabled(false);
        chart.getDescription().setEnabled(false);

        //샘플데이터
        BarDataSet barDataSet = new BarDataSet(list, "TEST");
        barDataSet.setColors(BARCHART_COLORS);
        barDataSet.setDrawValues(false);

        BarData data = new BarData(barDataSet);
        YAxis leftAxis = chart.getAxisLeft();
        YAxis rightAxis = chart.getAxisRight();
        leftAxis.setDrawAxisLine(false);
        leftAxis.setTextColor(Color.WHITE);
        rightAxis.setDrawAxisLine(false);
        rightAxis.setEnabled(false);
        XAxis xAxis = chart.getXAxis();
        xAxis.setLabelCount(10);
        xAxis.setValueFormatter(new MyXAxisValueFormatter(labels));
        xAxis.setTextColor(Color.WHITE);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGridColor(Color.RED);
        xAxis.setAxisLineColor(Color.BLACK);
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawGridLines(false);
        xAxis.setEnabled(true);
        xAxis.setSpaceMin(0.5f);
        xAxis.setSpaceMax(0.5f);
        chart.setDrawValueAboveBar(false);
        chart.setScaleEnabled(false);
        chart.setPinchZoom(false);
//        chart.setFitBars(true);
//        chart.setData(data);
        chart.animateY(2000);

        CombinedData data1 = new CombinedData();
        data1.setData(data);

        //라인데이터 추가
        data1.setData(linelist);
//        data1.setData(resultlinelist);
        chart.setData(data1);

        chart.getLegend().setEnabled(false);
//        setLegends(chart,category);
    }

    public final int[] BARCHART_COLORS = {
            rgb("#FF3636"), rgb("#A6A6A6"),
            rgb("#4374D9"), rgb("#6B9900"),
            rgb("#F2CB61"), rgb("#8041D9"),
            rgb("#3DB7CC"), rgb("#D9418C")
    };

    public int rgb(String hex) {
        int color = (int) Long.parseLong(hex.replace("#", ""), 16);
        int r = (color >> 16) & 0xFF;
        int g = (color >> 8) & 0xFF;
        int b = (color >> 0) & 0xFF;
        return Color.argb(90,r, g, b);
    }

    public class MyXAxisValueFormatter implements IAxisValueFormatter {

        private DecimalFormat mFormat;
        private String[] mValues;
        public MyXAxisValueFormatter(String[] values) {
            this.mValues = values;
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            // "value" represents the position of the label on the axis (x or y)
            return mValues[(int) value];
        }

        /** this is only needed if numbers are returned, else return 0 */

        public int getDecimalDigits() { return 0; }
    }

    public void setLegends(CombinedChart chart,String category){

        Legend l = chart.getLegend();

        l.getEntries();
        l.setTextColor(Color.WHITE);

        l.setPosition(Legend.LegendPosition.ABOVE_CHART_CENTER);

        l.setYEntrySpace(10f);

        l.setWordWrapEnabled(true);

        LegendEntry l1=new LegendEntry(CommandUtil.getInstance().getStr(R.string.mong_bid_legend_1)+category,Legend.LegendForm.CIRCLE,10f,2f,null, ContextCompat.getColor(this, R.color.progress_level1));
        LegendEntry l2=new LegendEntry(CommandUtil.getInstance().getStr(R.string.mong_bid_legend_2)+category, Legend.LegendForm.CIRCLE,10f,2f,null,ContextCompat.getColor(this, R.color.progress_level2));

        l.setCustom(new LegendEntry[]{l1,l2});

        l.setEnabled(true);

    }

    public String[] stringToDateArr(String date){
        Log.v("ifeelbluu", "date :: " + date);
        String[] dateArr = date.split(" ");
        return dateArr;
    }
    public String stringToDate(String date){
        Log.v("ifeelbluu", "date :: " + date);
        String[] dateArr = date.split("/");
        String startDate = dateArr[2]+CommandUtil.getInstance().getStr(R.string.mong_bid_year)+dateArr[0]+CommandUtil.getInstance().getStr(R.string.mong_bid_month)+dateArr[1]+CommandUtil.getInstance().getStr(R.string.mong_bid_day);
        return startDate;
    }

    public BidSale jsonToBidSale(JSONObject saleData) throws JSONException{
        BidSale sale = new BidSale();

        sale.setMongName(saleData.getString("MongName"));
        sale.setBase_iDou(saleData.getString("Base_iDou"));
        sale.setHap_iDou(saleData.getString("Hap_iDou"));
        sale.setVal_iDou(saleData.getString("Val_iDou"));
        return sale;
    }

    public Mong jsonToMong(JSONObject mongData) throws JSONException {

        Mong mong = new Mong();
        mong.setSEQ(mongData.getInt("SEQ"));
        mong.setUSER_ID(mongData.getString("USER_ID"));
        mong.setUSER_NAME(mongData.getString("USER_NAME"));
        mong.setTITLE(mongData.getString("TITLE"));
        mong.setNOTE(mongData.getString("NOTE"));
        //mong.setSTATUC_CODE(jsonObject.getInt("STATUC_CODE"));
        mong.setSTORE_ID(mongData.getString("STORE_ID"));
        mong.setSTART_DATE(mongData.getString("START_DATE"));
        mong.setEND_DATE(mongData.getString("END_DATE"));
        mong.setMAX_PRICE(mongData.getString("MAX_PRICE"));
        mong.setMIN_PRICE(mongData.getString("MIN_PRICE"));
        mong.setMONG_PRICE(mongData.getString("MONG_PRICE"));
        mong.setBID_VALUE(mongData.getString("BID_VALUE"));
        mong.setBID_COUNT(mongData.getString("BID_COUNT"));
        mong.setIMAGE_URL(mongData.getString("IMAGE_URL"));
        mong.setSELL_TYPE(mongData.getString("SELL_TYPE"));
        mong.setBUY_TYPE(mongData.getString("BUY_TYPE"));
        mong.setTRANS_ID(mongData.getInt("TRANS_ID"));
        mong.setTRANS_TYPE(mongData.getInt("TRANS_TYPE"));
        mong.setTRANS_STATUS(mongData.getInt("TRANS_STATUS"));
        mong.setJUSEO_SEQ(mongData.getInt("JUSEO_SEQ"));
        mong.setREG_DATE(mongData.getString("REG_DATE"));
        mong.setMONG_JST(mongData.getString("MONG_JST"));
        mong.setCURRENT_PAGE(mongData.getInt("CURRENT_PAGE"));


        return mong;
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
                            binding.myCoinValue.setText("(" + CommandUtil.getInstance().getStr(R.string.mong_bid_my_coin) + Constants.myCoinValue+")");

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
}