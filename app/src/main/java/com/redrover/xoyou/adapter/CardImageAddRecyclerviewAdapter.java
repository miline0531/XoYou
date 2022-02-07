package com.redrover.xoyou.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import com.redrover.xoyou.R;

public class CardImageAddRecyclerviewAdapter extends RecyclerView.Adapter<CardImageAddRecyclerviewAdapter.MyViewHolder> {
    private Context mContext;
    private List<String> dataList;
    public CardImageAddRecyclerviewAdapter(Context context, List<String> dataList){
        mContext = context;
        this.dataList = dataList;
    }

    public void setDataList() {

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView image;
        public MyViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
        }
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_mymong_regist_image,parent,false);
        return new CardImageAddRecyclerviewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder,int position) {
        holder.image.setImageResource(findRes("ic_card_img_item_"+(position+1)));

        final int clickPosition = position;
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onClickCard(clickPosition);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    //리소스 아이디참조
    public int findRes(String pVariableName) {

        try {
            return mContext.getResources().getIdentifier(pVariableName, "drawable", mContext.getPackageName());
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    private onSelectCardListener mListener;

    public void setOnSelectCardListener(onSelectCardListener listener){
        mListener = listener;
    }

    public interface onSelectCardListener {
        void onClickCard(int id);
    }
}
