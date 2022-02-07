package com.redrover.xoyou.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import com.redrover.xoyou.R;
import com.redrover.xoyou.model.DetailLocation;

public class DetailLocationRecyclerviewAdapter extends RecyclerView.Adapter<DetailLocationRecyclerviewAdapter.MyViewHolder> {
    private Context mContext;
    private List<DetailLocation> locationListDataList;
    public DetailLocationRecyclerviewAdapter(Context context){
        mContext = context;
        locationListDataList = new ArrayList<>();
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_mymong_detail_5,parent,false);
        return new MyViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder,int position) {
        DetailLocation relationListData = locationListDataList.get(position);
        holder.title.setText(relationListData.getTitle());
        holder.sub1.setText(relationListData.getSub1());
        holder.sub2.setText(relationListData.getSub2());
        holder.sub3.setText(relationListData.getSub3());
        holder.sub4.setText(relationListData.getSub4());

        if(position%2==0){
            holder.root_layout.setBackgroundColor(ContextCompat.getColor(mContext, R.color.list_bg_0));
        }else{
            holder.root_layout.setBackgroundColor(ContextCompat.getColor(mContext, R.color.list_bg_1));
        }

        final int itemIndex = position;
        holder.item_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onClickItem(itemIndex,0);
            }
        });
    }
    @Override
    public int getItemCount() {
        return locationListDataList.size();
    }
    public void setTaskList(List<DetailLocation> relationListDataList) {
        /*
        this.locationListDataList.clear();
        this.locationListDataList.addAll(relationListDataList);*/
        this.locationListDataList = relationListDataList;
        notifyDataSetChanged();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView title,sub1,sub2,sub3,sub4;
        private LinearLayout item_layout;
        private RelativeLayout root_layout;
        public MyViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            sub1 = itemView.findViewById(R.id.sub1);
            sub2 = itemView.findViewById(R.id.sub2);
            sub3 = itemView.findViewById(R.id.sub3);
            sub4 = itemView.findViewById(R.id.sub4);
            item_layout = itemView.findViewById(R.id.layout_item);
            root_layout = itemView.findViewById(R.id.rootLayout);

        }
    }

    private listOnClickListener mListener;

    public void setListOnClickListener(listOnClickListener listener){
        mListener = listener;
    }
    public interface listOnClickListener {
        void onClickItem(int id, int action);
    }
}
