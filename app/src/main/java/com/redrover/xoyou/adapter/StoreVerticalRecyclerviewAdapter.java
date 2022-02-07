package com.redrover.xoyou.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.redrover.xoyou.R;
import com.redrover.xoyou.model.Mong;
import com.redrover.xoyou.utils.CommandUtil;

public class StoreVerticalRecyclerviewAdapter extends RecyclerView.Adapter<StoreVerticalRecyclerviewAdapter.MyViewHolder>{
    private Context mContext;
    private ArrayList<Mong> data;
    public StoreVerticalRecyclerviewAdapter(Context context, ArrayList<Mong> data){
        mContext = context;
        this.data = data;

    }

    @NonNull
    @Override
    public StoreVerticalRecyclerviewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_manage_list_vertical_bak,parent,false);
        return new StoreVerticalRecyclerviewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StoreVerticalRecyclerviewAdapter.MyViewHolder holder, int position) {

        String date = data.get(position).getREG_DATE().substring(0,10);

        Glide.with(mContext).load(data.get(position).getIMAGE_URL()).thumbnail(0.1f).centerCrop().into(holder.image);

        Log.v("ifeelbluu", "getSELL_TYPE :: " + data.get(position).getSELL_TYPE());
        if(data.get(position).getSELL_TYPE() == null || data.get(position).getSELL_TYPE().equals("null")){
            holder.text_recommend.setVisibility(View.GONE);
        }else{
            holder.text_recommend.setVisibility(View.VISIBLE);
            holder.text_recommend.setText(data.get(position).getSELL_TYPE());
        }



        holder.text_sell_type.setVisibility(View.GONE);

        Log.v("ifeelbluu", "TRANS ::: " + data.get(position).getTRANS_STATUS());

        if(4 == data.get(position).getTRANS_STATUS()){
            holder.layout_store.setVisibility(View.VISIBLE);
            holder.txt_sell_count.setText(data.get(position).getBID_COUNT());
            Log.v("ifeelbluu", "TRANS ::: true");
            if("0".equals(data.get(position).getBID_COUNT())){
                holder.txt_sell_status.setText(CommandUtil.getInstance().getStr(R.string.mong_store_bid1));
                holder.txt_sell_count.setTextColor(ContextCompat.getColor(mContext, R.color.mong_grey_default));
                holder.txt_sell_status.setTextColor(ContextCompat.getColor(mContext, R.color.mong_grey_default));
            }else{
                holder.txt_sell_status.setText(CommandUtil.getInstance().getStr(R.string.mong_store_bid2));
                holder.txt_sell_count.setTextColor(ContextCompat.getColor(mContext, R.color.mong_dou_color_g1));
                holder.txt_sell_status.setTextColor(ContextCompat.getColor(mContext, R.color.mong_dou_color_g1));
            }
        }else{
            Log.v("ifeelbluu", "TRANS ::: false");
            holder.layout_store.setVisibility(View.GONE);
        }





        String mongType = data.get(position).getMONG_TYPE();

        if(mongType == null || mongType.equals("null")){
            holder.text_mong_type.setText("[TYPE]");
        }else{
            holder.text_mong_type.setText("["+data.get(position).getMONG_TYPE()+"]");
            holder.text_mong_kind.setText(data.get(position).getMONG_TYPE() + "꿈");
        }

        holder.text_top_1.setText("등록일 : ");
        holder.text_top_2.setText(date+getDayOfweek(date));
        holder.text_middle.setText(data.get(position).getTITLE());

        holder.text_mong_type.setVisibility(View.VISIBLE);
        if(data.get(position).getTRANS_TYPE()==1){ //고정가
            holder.text_mong_type.setText(CommandUtil.getInstance().getStr(R.string.mong_store_constant_price)+", ");
            holder.text_bottom.setText(CommandUtil.getInstance().getStr(R.string.mong_store_sell_price) + data.get(position).getMONG_PRICE());
            holder.text_price.setVisibility(View.GONE);
        }else{ //경매가
            holder.text_mong_type.setText(CommandUtil.getInstance().getStr(R.string.mong_store_auction)+", ");
            Log.d("CHECK@","endDate : " + data.get(position).getEND_DATE());

            String d_day ="";
            if(data.get(position).getEND_DATE()!=null&&!data.get(position).getEND_DATE().equals("")){
                d_day = getD_Day(data.get(position).getEND_DATE());
            }


            holder.text_bottom.setText("D-"+d_day+", " + CommandUtil.getInstance().getStr(R.string.mong_store_auction_person) + data.get(position).getBID_COUNT() + CommandUtil.getInstance().getStr(R.string.mong_store_person_count) +", " + CommandUtil.getInstance().getStr(R.string.mong_store_current_price) + data.get(position).getBID_VALUE());
            //holder.text_price.setVisibility(View.VISIBLE);
            String priceTitle = "";
            String priceValue = "";


            switch (data.get(position).getTRANS_STATUS()){
//                case 1: //default
//                    priceTitle = CommandUtil.getInstance().getStr(R.string.mong_store_auction_price_start);
//                    priceValue = data.get(position).getMIN_PRICE();
//                    break;
                case 2:
                    priceTitle = CommandUtil.getInstance().getStr(R.string.mong_store_auction_price_end);
                    priceValue = data.get(position).getMONG_PRICE();
                    holder.text_price.setTextColor(ContextCompat.getColor(mContext, R.color.btn_ok_color));
                    break;
                default:
                    priceTitle = CommandUtil.getInstance().getStr(R.string.mong_store_auction_price_start);
                    priceValue = data.get(position).getMIN_PRICE();
                    break;
            }

            holder.text_price.setText(priceTitle +" : " +priceValue);
        }
        String priceString = CommandUtil.getInstance().getStr(R.string.fragment_store_price);
        holder.price.setText(priceString + data.get(position).getMIN_PRICE() + ".0");


        final int onClickIndex = position;
        holder.itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mListener != null)
                mListener.onClickItem(onClickIndex,0);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView text_sell_type, text_top_1,text_top_2,text_middle,text_bottom,text_mong_type, text_recommend, text_price,price,text_mong_kind;
        private ImageView image;
        private RelativeLayout itemLayout;
        private LinearLayout layout_store;
        private TextView txt_sell_count, txt_sell_status;
        public MyViewHolder(View itemView) {
            super(itemView);
            txt_sell_count = itemView.findViewById(R.id.txt_sell_count);
            txt_sell_status = itemView.findViewById(R.id.txt_sell_status);
            text_top_1 = itemView.findViewById(R.id.text_top_1);
            text_top_2 = itemView.findViewById(R.id.text_top_2);
            text_middle = itemView.findViewById(R.id.text_middle);
            text_bottom = itemView.findViewById(R.id.text_bottom);
            image = itemView.findViewById(R.id.manage_image_view);
            itemLayout = itemView.findViewById(R.id.itemLayout);
            text_mong_type = itemView.findViewById(R.id.text_mong_type);
            text_recommend = itemView.findViewById(R.id.text_recommend);
            text_price = itemView.findViewById(R.id.text_price);
            text_sell_type = itemView.findViewById(R.id.text_sell_type);
            price = itemView.findViewById(R.id.price);
            text_mong_kind = itemView.findViewById(R.id.text_mong_kind);
            layout_store = itemView.findViewById(R.id.layout_store);
        }
    }

    public listOnClickListener mListener;

    public void setListOnClickListener(listOnClickListener listener){
        mListener = listener;
    }
    public interface listOnClickListener {
        void onClickItem(int id, int action);
    }

    //리소스 아이디참조
    public int findRes(String pVariableName) {
        Log.v("ifeelbluu","findRes ::: "  + pVariableName);
        try {
            return mContext.getResources().getIdentifier(pVariableName, "drawable", mContext.getPackageName());
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
    public String getDayOfweek(String date){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String[] week = mContext.getResources().getStringArray(R.array.str_week);
        Calendar cal = Calendar.getInstance();
        Date getDate; try { getDate = format.parse(date);
            cal.setTime(getDate);
            int w = cal.get(Calendar.DAY_OF_WEEK)-1;
            return "("+week[w]+")";
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public String getD_Day(String date){
        String endDate1 = date.split(" ")[0];
        String[] endDate2 = endDate1.split("/");
        int year = Integer.parseInt(endDate2[2]);
        int month = Integer.parseInt(endDate2[0]);
        int day = Integer.parseInt(endDate2[1]);

        int dCount = countdday(year,month,day);

        return ""+dCount;
    }
    public int countdday(int myear, int mmonth, int mday) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

            Calendar todaCal = Calendar.getInstance(); //오늘날자 가져오기
            Calendar ddayCal = Calendar.getInstance(); //오늘날자를 가져와 변경시킴

            mmonth -= 1; // 받아온날자에서 -1을 해줘야함.
            ddayCal.set(myear,mmonth,mday);// D-day의 날짜를 입력

            long today = todaCal.getTimeInMillis()/86400000; //->(24 * 60 * 60 * 1000) 24시간 60분 60초 * (ms초->초 변환 1000)
            long dday = ddayCal.getTimeInMillis()/86400000;
            long count = dday - today; // 오늘 날짜에서 dday 날짜를 빼주게 됩니다.
            return (int) count;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return -1;
        }
    }
}

