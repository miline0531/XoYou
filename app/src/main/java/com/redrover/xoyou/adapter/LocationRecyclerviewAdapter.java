package com.redrover.xoyou.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import com.redrover.xoyou.R;
import com.redrover.xoyou.model.Location;

public class LocationRecyclerviewAdapter extends RecyclerView.Adapter<LocationRecyclerviewAdapter.MyViewHolder> {
    private Context mContext;
    private List<Location> locationDataList;
    public LocationRecyclerviewAdapter(Context context){
        mContext = context;
        locationDataList = new ArrayList<>();
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_location_list,parent,false);
        return new MyViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder,int position) {
        Location relationListData = locationDataList.get(position);
        holder.txt_title.setText(relationListData.getGUBUN());
        holder.txt_sub_title.setText(relationListData.getBZGUBUN());
        holder.txt_floor.setText(relationListData.getFLOOR());
        holder.txt_addr.setText(relationListData.getADDR1());

        final int index = position;
        holder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onClickItem(index, 0);
            }
        });

        holder.btn_write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onClickItem(index, 1);
            }
        });
    }
    @Override
    public int getItemCount() {
        return locationDataList.size();
    }
    public void setTaskList(List<Location> locationDataList) {
        this.locationDataList = locationDataList;
        notifyDataSetChanged();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView txt_title, txt_sub_title,txt_floor, txt_addr;
        private ImageView btn_delete, btn_write;
        public MyViewHolder(View itemView) {
            super(itemView);
            txt_title = itemView.findViewById(R.id.txt_title);
            txt_sub_title = itemView.findViewById(R.id.txt_sub_title);
            txt_floor = itemView.findViewById(R.id.txt_floor);
            txt_addr = itemView.findViewById(R.id.txt_addr);
            btn_delete = itemView.findViewById(R.id.btn_delete);
            btn_write = itemView.findViewById(R.id.btn_write);
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
