package com.redrover.xoyou.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import com.redrover.xoyou.R;
import com.redrover.xoyou.activity.detail.MyMongRegistActivity;

public class MongRegistRecyclerviewAdapter extends RecyclerView.Adapter<MongRegistRecyclerviewAdapter.MyViewHolder> {
    private Context mContext;
    private List<String> dataList;

    public MongRegistRecyclerviewAdapter(Context context, List<String> dataList){
        mContext = context;
        this.dataList = dataList;
        this.dataList.add("1");
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
    public class MyViewHolderNone extends MyViewHolder {
        private TextView tv_gallery;
        public MyViewHolderNone(View itemView) {
            super(itemView);
            tv_gallery = itemView.findViewById(R.id.tv_gallery);
            if (itemView instanceof CardView){
                ((CardView)itemView).setCardBackgroundColor(Color.TRANSPARENT);
//                ((CardView)itemView).setCardElevation(0);
                Log.e("=>","태그 ");
                tv_gallery.setTextColor(Color.parseColor("#80e0e0e0"));
            }
        }
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == 0 ) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_mymong_regist_image,parent,false);
            return new MongRegistRecyclerviewAdapter.MyViewHolder(view);
        } else {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_mymong_regist_image_none,parent,false);
            return new MongRegistRecyclerviewAdapter.MyViewHolderNone(view);
        }


    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder,int position) {
        holder.image.setImageResource(findRes("card_back"+(position)));

        final int clickPosition = position;
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onClickCard(clickPosition);
            }
        });

        if (holder instanceof MyViewHolderNone) {
            ((MyViewHolderNone) holder).tv_gallery.setOnClickListener(v->{
                Intent i = new Intent();
                i.setType("image/*");
                i.setAction(Intent.ACTION_GET_CONTENT);
                ((MyMongRegistActivity)mContext).startActivityForResult(i,1100);
            });
        }

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

    @Override
    public int getItemViewType(int position) {
        return position == dataList.size() - 1 ? 1:0;
    }

    private onSelectCardListener mListener;

    public void setOnSelectCardListener(onSelectCardListener listener){
        mListener = listener;
    }

    public interface onSelectCardListener {
        void onClickCard(int id);
    }
}
