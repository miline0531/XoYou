package com.redrover.xoyou.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import com.redrover.xoyou.R;

public class FindMeRecyclerviewAdapter extends RecyclerView.Adapter<FindMeRecyclerviewAdapter.MyViewHolder>{
    private Context mContext;
    private ArrayList<String> data;
    public FindMeRecyclerviewAdapter(Context context, ArrayList<String> data){
        mContext = context;
        this.data = data;
    }

    @NonNull
    @Override
    public FindMeRecyclerviewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_findme_list,parent,false);
        return new FindMeRecyclerviewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FindMeRecyclerviewAdapter.MyViewHolder holder, int position) {

        holder.text.setVisibility(View.GONE);
        String resName = "food"+position;
        holder.image.setBackgroundResource(findRes(resName));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView text;
        private ImageView image;
        public MyViewHolder(View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.text);
            image = itemView.findViewById(R.id.image_view);
        }
    }

    private FindMeRecyclerviewAdapter.listOnClickListener mListener;

    public void setListOnClickListener(FindMeRecyclerviewAdapter.listOnClickListener listener){
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
}
