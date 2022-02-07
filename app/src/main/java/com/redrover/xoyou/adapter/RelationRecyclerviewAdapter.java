package com.redrover.xoyou.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.redrover.xoyou.R;
import com.redrover.xoyou.model.Relation;
import com.redrover.xoyou.utils.CommandUtil;

public class RelationRecyclerviewAdapter extends RecyclerView.Adapter<RelationRecyclerviewAdapter.MyViewHolder> {
    private Context mContext;
    private List<Relation> relationListDataList;
    public RelationRecyclerviewAdapter(Context context){
        mContext = context;
        relationListDataList = new ArrayList<>();
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_relation_swipe_view,parent,false);
        return new MyViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Relation relationListData = relationListDataList.get(position);
        holder.tvTaskName.setText(relationListData.getNAME());

        String gender;
        if(relationListDataList.get(position).getMW()==1){
            gender = CommandUtil.getInstance().getStr(R.string.mong_relation_gender_m);
        }else{
            gender = CommandUtil.getInstance().getStr(R.string.mong_relation_gender_f);
        }

//        String info =relationListDataList.get(position).getGWANGYE() + ", " + gender + " " +
//                changeBirthToAge(relationListDataList.get(position).getBIRTH_DATE()) + " " +
//                changeBirthToBelt(relationListDataList.get(position).getBIRTH_DATE());

        String info =relationListDataList.get(position).getGWANGYE() + ", " + gender;

        holder.tvTaskDesc.setText(info);
    }
    @Override
    public int getItemCount() {
        return relationListDataList.size();
    }
    public void setTaskList(List<Relation> relationListDataList) {
        this.relationListDataList = relationListDataList;
        notifyDataSetChanged();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTaskName;
        private TextView tvTaskDesc;
        public MyViewHolder(View itemView) {
            super(itemView);
            tvTaskName = itemView.findViewById(R.id.txt_relation_name);
            tvTaskDesc = itemView.findViewById(R.id.txt_relation_info);
        }
    }

    public int changeBirthToAge(String birthday){
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        int birth = Integer.parseInt(birthday);
        int year = birth;

        return currentYear-year+1;
    }

    public String changeBirthToBelt(String birthday){
        int birth = Integer.parseInt(birthday);
        int year = birth/10000;
        String [] belt = mContext.getResources().getStringArray(R.array.str_belt);
        String result="";
        switch (year%12){
            case 0: result = belt[0]; break;
            case 1: result = belt[1]; break;
            case 2: result = belt[2]; break;
            case 3: result = belt[3]; break;
            case 4: result = belt[4]; break;
            case 5: result = belt[5]; break;
            case 6: result = belt[6]; break;
            case 7: result = belt[7]; break;
            case 8: result = belt[8]; break;
            case 9: result = belt[9]; break;
            case 10: result = belt[10]; break;
            case 11: result = belt[11]; break;
        }

        return result + CommandUtil.getInstance().getStr(R.string.mong_relation_belt);
    }
}
