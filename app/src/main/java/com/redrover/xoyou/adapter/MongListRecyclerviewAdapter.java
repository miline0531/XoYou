package com.redrover.xoyou.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
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

public class MongListRecyclerviewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private Context mContext;
    private ArrayList<Mong> data;
    public MongListRecyclerviewAdapter(Context context, ArrayList<Mong> data){
        mContext = context;
        this.data = data;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == VIEW_TYPE_ITEM){
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_manage_list_vertical_bak,parent,false);
            return new MongListRecyclerviewAdapter.MyViewHolder(view);
        }else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.view_common_loading,parent,false);
            return new MongListRecyclerviewAdapter.LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof MyViewHolder){
            populateItemRows((MyViewHolder) holder,position);
        }else if(holder instanceof LoadingViewHolder){
            showLoadingView((LoadingViewHolder) holder,position);
        }
    }

    public void populateItemRows(MyViewHolder holder,int position){
        String date = data.get(position).getREG_DATE().substring(0,10);

        holder.text_top_1.setText(CommandUtil.getInstance().getStr(R.string.mong_manage_vertical_reg_date));
        holder.text_top_2.setText(date+getDayOfweek(date));
        holder.text_middle.setText(data.get(position).getTITLE());

        holder.text_sell_type.setVisibility(View.GONE);

        holder.text_recommend.setText(data.get(position).getSELL_TYPE());

        String mongType = data.get(position).getMONG_TYPE();
        if(mongType == null || mongType.equals("null")){
            holder.text_mong_type.setText("[TYPE]");
        }else{
            holder.text_mong_type.setText("["+data.get(position).getMONG_TYPE()+"]");
        }

        if(data.get(position).getTRANS_TYPE()==1){ //고정가
            holder.text_bottom.setText(CommandUtil.getInstance().getStr(R.string.mong_manage_vertical_recommend) + data.get(position).getMONG_PRICE());
        }else{ //경매가
            holder.text_bottom.setText("D-10" + data.get(position).getBID_VALUE() + ", " + data.get(position).getBID_COUNT());
        }


        String resName = "a_sample_icon"+position;
        //holder.image.setBackgroundResource(findRes(resName));
        Glide.with(mContext).load(data.get(position).getIMAGE_URL()).thumbnail(0.1f).centerCrop().placeholder(findRes(resName)).into(holder.image);


        final int onClickIndex = position;
        holder.itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mListener != null)
                    mListener.onClickItem(onClickIndex,0);
            }
        });
    }

    private void showLoadingView(LoadingViewHolder holder, int position){

    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView text_sell_type, text_top_1,text_top_2,text_middle,text_bottom, text_recommend, text_mong_type;
        private ImageView image;
        private RelativeLayout itemLayout;
        public MyViewHolder(View itemView) {
            super(itemView);
            text_top_1 = itemView.findViewById(R.id.text_top_1);
            text_top_2 = itemView.findViewById(R.id.text_top_2);
            text_middle = itemView.findViewById(R.id.text_middle);
            text_bottom = itemView.findViewById(R.id.text_bottom);
            image = itemView.findViewById(R.id.manage_image_view);
            itemLayout = itemView.findViewById(R.id.itemLayout);
            text_mong_type = itemView.findViewById(R.id.text_mong_type);
            text_recommend = itemView.findViewById(R.id.text_recommend);
            text_sell_type = itemView.findViewById(R.id.text_sell_type);
        }
    }

    public class LoadingViewHolder extends RecyclerView.ViewHolder {
        private ProgressBar progressBar;

        public LoadingViewHolder(View view){
            super(view);
            progressBar = view.findViewById(R.id.common_loading_image_view);
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


}

