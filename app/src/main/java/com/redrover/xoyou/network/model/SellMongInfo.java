package com.redrover.xoyou.network.model;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import com.redrover.xoyou.R;
import com.redrover.xoyou.model.SimRi;

public class SellMongInfo {
    int mong_srl;
    String start_time;
    String end_time;
    int transaction_type;
    int start_value;
    String description;
    String title;
    String[] category_code;


    public SellMongInfo(int mong_srl, String start_time, String end_time, int transaction_type, int start_value, String description, String title, String[] category_code) {
        this.mong_srl = mong_srl;
        this.start_time = start_time;
        this.end_time = end_time;
        this.transaction_type = transaction_type;
        this.start_value = start_value;
        this.description = description;
        this.title = title;
        this.category_code = category_code;
    }

    public int getMong_srl() {
        return mong_srl;
    }

    public void setMong_srl(int mong_srl) {
        this.mong_srl = mong_srl;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public int getTransaction_type() {
        return transaction_type;
    }

    public void setTransaction_type(int transaction_type) {
        this.transaction_type = transaction_type;
    }

    public int getStart_value() {
        return start_value;
    }

    public void setStart_value(int start_value) {
        this.start_value = start_value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String[] getCategory_code() {
        return category_code;
    }

    public void setCategory_code(String[] category_code) {
        this.category_code = category_code;
    }

    public static class XoYouUserLoadRecyclerviewAdapter extends RecyclerView.Adapter<XoYouUserLoadRecyclerviewAdapter.MyViewHolder>{
        private Context mContext;
        private ArrayList<SimRi> data;
        public XoYouUserLoadRecyclerviewAdapter(Context context, ArrayList<SimRi> data){
            mContext = context;
            this.data = data;

        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_xoyouuserload_list_vertical,parent,false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

            holder.text_name.setText(data.get(position).getName());
            holder.text_SimRiInfo.setText(data.get(position).getSimRiInfo());

            final int onClickIndex = position;
            holder.itemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(mListener != null)
                    mListener.onClickItem(onClickIndex,0);
                }
            });
        }

        @Override
        public int getItemCount() {
            return data.size();
        }


        public class MyViewHolder extends RecyclerView.ViewHolder {
            private TextView text_name,text_SimRiInfo;
            private ImageView image;
            private RelativeLayout itemLayout;
            public MyViewHolder(View itemView) {
                super(itemView);
                text_name = itemView.findViewById(R.id.text_name);
                text_SimRiInfo = itemView.findViewById(R.id.text_SimRiInfo);
                image = itemView.findViewById(R.id.manage_image_view);
                itemLayout = itemView.findViewById(R.id.itemLayout);
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
    }
}
