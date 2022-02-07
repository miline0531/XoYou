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
import com.redrover.xoyou.model.User;
import com.redrover.xoyou.utils.CommandUtil;

public class UserRecyclerviewAdapter extends RecyclerView.Adapter<UserRecyclerviewAdapter.MyViewHolder> {
    private Context mContext;
    private List<User> userDataList;
    public UserRecyclerviewAdapter(Context context){
        mContext = context;
        userDataList = new ArrayList<>();
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_relation_swipe_view,parent,false);
        return new MyViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        //RelationListData relationListData = relationListDataList.get(position);
        //holder.tvTaskName.setText(relationListData.getLastName()+relationListData.getFirstName());
        //holder.tvTaskDesc.setText(relationListData.getInfo());
        String desc = userDataList.get(position).getBIRTH_DATE() + " ";
        if(userDataList.get(position).getMW()==1){
            desc += CommandUtil.getInstance().getStr(R.string.mong_relation_gender_m);
            holder.imgRelationProfile.setBackgroundResource(R.drawable.icon_m);
        }else{
            desc +=CommandUtil.getInstance().getStr(R.string.mong_relation_gender_f);
            holder.imgRelationProfile.setBackgroundResource(R.drawable.icon_f);
        }
        holder.tvTaskName.setText(userDataList.get(position).getNAME()+" ("+userDataList.get(position).getNICK_NAME()+")");
        holder.tvTaskDesc.setText(desc);
    }
    @Override
    public int getItemCount() {
        return userDataList.size();
    }
    public void setTaskList(List<User> relationListDataList) {
        this.userDataList = relationListDataList;
        notifyDataSetChanged();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTaskName;
        private TextView tvTaskDesc;
        private ImageView imgRelationProfile;
        public MyViewHolder(View itemView) {
            super(itemView);
            tvTaskName = itemView.findViewById(R.id.txt_relation_name);
            tvTaskDesc = itemView.findViewById(R.id.txt_relation_info);
            imgRelationProfile = itemView.findViewById(R.id.img_relation_profile);

        }
    }
}
