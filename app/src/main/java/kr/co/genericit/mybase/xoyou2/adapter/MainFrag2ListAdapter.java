package kr.co.genericit.mybase.xoyou2.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import kr.co.genericit.mybase.xoyou2.R;
import kr.co.genericit.mybase.xoyou2.common.SkyLog;
import kr.co.genericit.mybase.xoyou2.model.WeListObj;


public class MainFrag2ListAdapter extends RecyclerView.Adapter<MainFrag2ListAdapter.MyViewHolder> {

    private Context mContext;
    private static LayoutInflater inflater = null;
    ArrayList<WeListObj> items;

    public MainFrag2ListAdapter(Context a, ArrayList<WeListObj> m_board) {
        mContext = a;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        items = m_board;
    }

    @NonNull
    @Override
    public MainFrag2ListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.mainfrag2_item,parent,false);
        return new MainFrag2ListAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MainFrag2ListAdapter.MyViewHolder holder, int position) {
        final WeListObj board = items.get(position);
        SkyLog.d("onBindViewHolder :: " + position);

        holder.txt1.setText(board.getUnName());
        holder.txt2.setText(board.getMaxName());
        holder.txt3.setText(board.getMinName());

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
        private TextView txt3;
        private LinearLayout itemLayout;

        public MyViewHolder(View itemView) {
            super(itemView);
            txt1 = itemView.findViewById(R.id.txt1);
            txt2 = itemView.findViewById(R.id.txt2);
            txt3 = itemView.findViewById(R.id.txt3);
            itemLayout = itemView.findViewById(R.id.itemLayout);
        }
    }

    private MainFrag2ListAdapter.listOnClickListener mListener;

    public void setListOnClickListener(MainFrag2ListAdapter.listOnClickListener listener){
        mListener = listener;
    }
    public interface listOnClickListener {
        void onClickItem(int id, int action);
    }



}