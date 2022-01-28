package kr.co.genericit.mybase.xoyou2.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.icu.text.DecimalFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import kr.co.genericit.mybase.xoyou2.R;
import kr.co.genericit.mybase.xoyou2.model.MongDateList;
import kr.co.genericit.mybase.xoyou2.network.action.ActionRuler;
import kr.co.genericit.mybase.xoyou2.network.interfaces.ActionResultListener;
import kr.co.genericit.mybase.xoyou2.network.request.ActionRequestMyMongStoryGetDateUn;
import kr.co.genericit.mybase.xoyou2.network.response.DefaultResult;
import kr.co.genericit.mybase.xoyou2.utils.CommandUtil;
import kr.co.genericit.mybase.xoyou2.utils.LogUtil;

public class DetailDateListviewAdapter extends RecyclerView.Adapter<DetailDateListviewAdapter.MyViewHolder> {
//    private Activity mActivity;
    private Activity mContext;
    private List<MongDateList> ListDataList;
    public DetailDateListviewAdapter(Activity context, List<MongDateList> ListDataList){
        mContext = context;
        this.ListDataList = ListDataList;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_date_list,parent,false);
        return new MyViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder,int position) {
        MongDateList dateListData = ListDataList.get(position);

        Log.v("ifeelbluu", "dateListData.getDateFormat() getView :: " + dateListData.getDateFormat());
        holder.txt_DateFormat.setText(dateListData.getDateFormat());
        holder.txt_Info.setText(dateListData.getInfo());

        if(position%2==0){
            holder.bg_color.setBackgroundColor(ContextCompat.getColor(mContext, R.color.list_bg_0));
        }else{
            holder.bg_color.setBackgroundColor(ContextCompat.getColor(mContext, R.color.list_bg_1));
        }


        holder.bg_color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.lineChart.getVisibility() == View.GONE){
                    holder.lineChart.setVisibility(View.VISIBLE);
                    String sDate = dateListData.getsDate().replaceAll("-","").replaceAll("T","").replaceAll(":","");//1983-03-22T20:03:00
                    String eDate = dateListData.geteDate().replaceAll("-","").replaceAll("T","").replaceAll(":","");
                    getDateGraph(holder.lineChart, sDate, eDate);

                }else{
                    holder.lineChart.setVisibility(View.GONE);
                }

            }
        });

    }
    @Override
    public int getItemCount() {
        return ListDataList.size();
    }

    public void setTaskList(List<MongDateList> ListDataList) {
        this.ListDataList = ListDataList;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView txt_DateFormat,txt_Info;
        private LinearLayout bg_color;
        private LineChart lineChart;
        public MyViewHolder(View itemView) {
            super(itemView);
            txt_DateFormat = itemView.findViewById(R.id.txt_dateformat);
            txt_Info = itemView.findViewById(R.id.txt_info);
            bg_color = itemView.findViewById(R.id.item_bg);
            lineChart = itemView.findViewById(R.id.lineChart);
        }
    }

    private listOnClickListener mListener;

    public void setListOnClickListener(listOnClickListener listener){
        mListener = listener;
    }
    public interface listOnClickListener {
        void onClickItem(int id, int action);
    }



    private void getDateGraph(final LineChart lineChar1, String sDate,String eDate){
        ActionRuler.getInstance().addAction(new ActionRequestMyMongStoryGetDateUn(mContext, sDate, eDate, new ActionResultListener<DefaultResult>() {

            @Override
            public void onResponseResult(DefaultResult response) {
                try {
                    DefaultResult result = response;
                    Log.d("TEST",result.getData().toString());
                    lineChartInit(lineChar1, result.getData().toString());

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


    private void lineChartInit(LineChart chart1,String dateInfo) {
        JSONArray list1 = null;
        JSONArray list2 = null;
        JSONArray list3 = null;
        try {
            JSONObject obj = new JSONObject(dateInfo);
            list1 = obj.getJSONArray("List1");
            list2 = obj.getJSONArray("List2");
            list3 = obj.getJSONArray("List3");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String[] xLabel = new String[list1.length()];
//        String[] yLabel = new String[6];
        for(int i=0; i<list1.length(); i++){
            String value = "";
            try{
                JSONObject sago = list1.getJSONObject(i);
                value = sago.getString("DateFormat");
                value = value.substring(value.indexOf("-")+1);
            }catch (Exception e){

            }
            xLabel[i] = value;
        }

//        float temp = -0.1f;
//        for(int i=0; i<yLabel.length; i++){
//            yLabel[i] = temp+"";
//            temp+= 0.1f;
//        }
        setData(chart1, list1,list2, list3);
        int duration = list1.length() * 50;
        chart1.animateX(duration);
        chart1.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        XAxis XAxis = chart1.getXAxis();
        XAxis.setLabelCount(list1.length()-1,false);
        XAxis.setTextColor(Color.WHITE);
        XAxis.setValueFormatter(new MyXAxisValueFormatter(xLabel));


        YAxis rightYAxis = chart1.getAxisRight();
        YAxis leftYAxis = chart1.getAxisLeft();
        leftYAxis.setEnabled(false);
        rightYAxis.setEnabled(false);
//        leftYAxis.setLabelCount(5,true);
//        leftYAxis.setAxisMinimum(0);
//        leftYAxis.setAxisMaximum(6);
//        leftYAxis.setValueFormatter(new MyYAxisValueFormatter(yLabel));

        chart1.getDescription().setEnabled(false);
        chart1.getLegend().setEnabled(true);
        chart1.setTouchEnabled(false);
        chart1.setEnabled(false);
        setLegends(chart1);
    }


    public void setLegends(LineChart chart1){

        Legend l = chart1.getLegend();

        l.getEntries();
        l.setTextColor(Color.WHITE);

        l.setPosition(Legend.LegendPosition.ABOVE_CHART_CENTER);

        l.setYEntrySpace(10f);
        l.setTextSize(16f);

        l.setWordWrapEnabled(true);

        LegendEntry l1=new LegendEntry(CommandUtil.getInstance().getStr(R.string.mong_detail_adapter_legend_1),Legend.LegendForm.CIRCLE,10f,2f,null,ContextCompat.getColor(mContext, R.color.progress_level1));
        LegendEntry l2=new LegendEntry(CommandUtil.getInstance().getStr(R.string.mong_detail_adapter_legend_2), Legend.LegendForm.CIRCLE,10f,2f,null,ContextCompat.getColor(mContext, R.color.progress_level2));
        LegendEntry l3=new LegendEntry(CommandUtil.getInstance().getStr(R.string.mong_detail_adapter_legend_3), Legend.LegendForm.CIRCLE,10f,2f,null,ContextCompat.getColor(mContext, R.color.progress_level3));

        l.setCustom(new LegendEntry[]{l1,l2,l3});

        l.setEnabled(true);

    }

    public class MyYAxisValueFormatter implements IAxisValueFormatter {

        private DecimalFormat mFormat;
        private String[] mValues;
        public MyYAxisValueFormatter(String[] values) {
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

    public class MyXAxisValueFormatter implements IAxisValueFormatter {

        private String[] mValues;

        public MyXAxisValueFormatter(String[] values) {
            this.mValues = values;
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            // "value" represents the position of the label on the axis (x or y)
            String rst_str = "";
            if((int) value%2==1){
                rst_str = mValues[(int) value];
            }

            return rst_str;
        }

        /** this is only needed if numbers are returned, else return 0 */

        public int getDecimalDigits() { return 0; }
    }


    public void setData(LineChart chart1, JSONArray list1, JSONArray list2,JSONArray list3){
        ArrayList<Entry> yVals1 = new ArrayList<>();
        for(int i=0; i<list1.length(); i++){
            try{
                JSONObject sago = list1.getJSONObject(i);
                float val = Float.parseFloat(sago.getString("iDou"));
                yVals1.add(new Entry(i,val));
            }catch (Exception e){

            }

        }

        ArrayList<Entry> yVals2 = new ArrayList<>();
        for(int i=0; i<list2.length(); i++){
            try{
                JSONObject sago = list2.getJSONObject(i);
                float val = Float.parseFloat(sago.getString("iDou"));
                yVals2.add(new Entry(i,val));
            }catch (Exception e){

            }
        }

        ArrayList<Entry> yVals3 = new ArrayList<>();
        for(int i=0; i<list3.length(); i++){
            try{
                JSONObject sago = list3.getJSONObject(i);
                float val = Float.parseFloat(sago.getString("iDou"));
                yVals3.add(new Entry(i,val));
            }catch (Exception e){

            }
        }

        LineDataSet set1, set2, set3;

        set1 = new LineDataSet(yVals1,"data1");
        set1.setDrawCircles(true);
        set1.setLineWidth(1f);
        set1.setDrawValues(false);
        set1.setColor(ContextCompat.getColor(mContext, R.color.progress_level1));
        set1.setCircleColor(ContextCompat.getColor(mContext, R.color.progress_level1));
        set1.setCircleColorHole(ContextCompat.getColor(mContext, R.color.progress_level1));
        set1.setCircleSize(2f);
        set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);

        set2 = new LineDataSet(yVals2,"data2");
        set2.setDrawCircles(true);
        set2.setLineWidth(1f);
        set2.setDrawValues(false);
        set2.setColor(ContextCompat.getColor(mContext, R.color.progress_level2));
        set2.setCircleColor(ContextCompat.getColor(mContext, R.color.progress_level2));
        set2.setCircleColorHole(ContextCompat.getColor(mContext, R.color.progress_level2));
        set2.setCircleSize(2f);
        set2.setMode(LineDataSet.Mode.CUBIC_BEZIER);

        set3 = new LineDataSet(yVals3,"data3");
        set3.setDrawCircles(true);
        set3.setLineWidth(1f);
        set3.setDrawValues(false);
        set3.setColor(ContextCompat.getColor(mContext, R.color.progress_level3));
        set3.setCircleColor(ContextCompat.getColor(mContext, R.color.progress_level3));
        set3.setCircleColorHole(ContextCompat.getColor(mContext, R.color.progress_level3));
        set3.setCircleSize(2f);
        set3.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        LineData data = new LineData(set1,set2,set3);
        chart1.setData(data);
    }
}
