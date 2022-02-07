package com.redrover.xoyou.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import com.redrover.xoyou.R;
import com.redrover.xoyou.model.SimRi;


public class MainFrag1ListAdapter extends RecyclerView.Adapter<MainFrag1ListAdapter.MyViewHolder> {

    private Context mContext;
    private static LayoutInflater inflater = null;
    ArrayList<SimRi> items;

    public MainFrag1ListAdapter(Context a, ArrayList<SimRi> m_board) {
        mContext = a;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        items = m_board;
    }

    @NonNull
    @Override
    public MainFrag1ListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.mainfrag1_item,parent,false);
        return new MainFrag1ListAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MainFrag1ListAdapter.MyViewHolder holder, int position) {
        final SimRi board = items.get(position);
        //SkyLog.d("onBindViewHolder :: " + position);

        holder.txt1.setText(board.getName());
        holder.txt2.setText(board.getSimRiInfo());
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
        return items.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView txt1;
        private TextView txt2;
        private LinearLayout itemLayout;

        public MyViewHolder(View itemView) {
            super(itemView);
            txt1 = itemView.findViewById(R.id.txt1);
            txt2 = itemView.findViewById(R.id.txt2);
            itemLayout = itemView.findViewById(R.id.itemLayout);
        }
    }

    private MainFrag1ListAdapter.listOnClickListener mListener;

    public void setListOnClickListener(MainFrag1ListAdapter.listOnClickListener listener){
        mListener = listener;
    }
    public interface listOnClickListener {
        void onClickItem(int id, int action);
    }

}