package com.redrover.xoyou.adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import com.redrover.xoyou.R;
import com.redrover.xoyou.common.Constants;

public class CardRecyclerviewAdapter extends RecyclerView.Adapter<CardRecyclerviewAdapter.MyViewHolder> {
    private Context mContext;
    private List<String> dataList;
    public CardRecyclerviewAdapter(Context context,List<String> dataList){
        mContext = context;
        this.dataList = dataList;
    }

    public void setDataList() {

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView card;
        private TextView txt_card;
        public MyViewHolder(View itemView) {
            super(itemView);
            card = itemView.findViewById(R.id.card_image);
            txt_card = itemView.findViewById(R.id.txt_card);
        }
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_card,parent,false);
        return new CardRecyclerviewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.card.setScaleType(ImageView.ScaleType.FIT_XY);
        MultiTransformation multiOption = new MultiTransformation( new CenterCrop(), new RoundedCorners(20) );
        Glide.with(mContext)
                .load(findRes("joker4"))
                .apply(RequestOptions.bitmapTransform(multiOption))
                .into(holder.card);

        final int index = position;
        holder.card.setOnClickListener(v ->{
            if(Constants.MongCardPickIndex != -1){
                return;
            }
            Constants.MongCardPickIndex = index;
            Constants.MongCardPickText = dataList.get(index);
            final ObjectAnimator oa1 = ObjectAnimator.ofFloat(holder.card, "scaleX", 1f, 0f);
            final ObjectAnimator oa2 = ObjectAnimator.ofFloat(holder.card, "scaleX", 0f, 1f);
            oa1.setInterpolator(new DecelerateInterpolator());
            oa2.setInterpolator(new AccelerateDecelerateInterpolator());
            oa1.setDuration(1000);
            oa2.setDuration(1000);
            oa1.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
//                    holder.card.setBackgroundResource(findRes("card_front"));

                    MultiTransformation multiOption = new MultiTransformation( new CenterCrop(), new RoundedCorners(20) );

                    Glide.with(mContext)
                            .load(findRes("card_front"))
                            .apply(RequestOptions.bitmapTransform(multiOption))
                            .into(holder.card);
                    oa2.start();


                }
            });
            oa2.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    holder.txt_card.setText(dataList.get(index).replace(".과", ""));

                }
            });
            oa1.start();
        });


        if(Constants.MongCardPickIndex != -1){
            if(position == Constants.MongCardPickIndex){
                Glide.with(mContext)
                        .load(findRes("card_front"))
                        .apply(RequestOptions.bitmapTransform(multiOption))
                        .into(holder.card);

                holder.txt_card.setText(Constants.MongCardPickText.replace(".과", ""));
            }

            return;
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
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
