package com.redrover.xoyou.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import com.redrover.xoyou.R;
import com.redrover.xoyou.model.Mong;

public class BidRecyclerviewAdapter extends RecyclerView.Adapter<BidRecyclerviewAdapter.MyViewHolder>{
    private Context mContext;
    private ArrayList<Mong> data;
    public BidRecyclerviewAdapter(Context context, ArrayList<Mong> data){
        mContext = context;
        this.data = data;

    }

    @NonNull
    @Override
    public BidRecyclerviewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_bid_textview,parent,false);
        return new BidRecyclerviewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BidRecyclerviewAdapter.MyViewHolder holder, int position) {
        holder.text.setText(data.get(position).getUSER_NAME());


    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView text;
        public MyViewHolder(View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.text);
        }
    }


}

