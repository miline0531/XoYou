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
import kr.co.genericit.mybase.xoyou2.model.Mong;
import kr.co.genericit.mybase.xoyou2.utils.CommandUtil;

public class HomeRecyclerviewAdapter extends RecyclerView.Adapter<HomeRecyclerviewAdapter.MyViewHolder>{
    private Context mContext;
    private ArrayList<Mong> data;
    public HomeRecyclerviewAdapter(Context context, ArrayList<Mong> data){
        mContext = context;
        this.data = data;

    }

    @NonNull
    @Override
    public HomeRecyclerviewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_frag_home_mong,parent,false);
        return new HomeRecyclerviewAdapter.MyViewHolder(view);
    }

    private int selected = 0;
    @Override
    public void onBindViewHolder(@NonNull HomeRecyclerviewAdapter.MyViewHolder holder, int position) {
        String date = CommandUtil.getInstance().getStr(R.string.fragment_home_frag_regdate) + " " + data.get(position).getREG_DATE();
        holder.txt_date.setText(date);

        int priceInt = Math.round(Float.parseFloat(data.get(position).getMONG_PRICE())*100);

        String price = CommandUtil.getInstance().getStr(R.string.fragment_home_frag_recommend) + " " + priceInt/100 + "." + priceInt%100;
        holder.txt_price.setText(price);
        String title = CommandUtil.getInstance().getStr(R.string.fragment_home_frag_title) + " " + data.get(position).getTITLE();
        holder.txt_title.setText(title);


        if(selected==position){
            holder.itemLayout.setBackgroundColor(CommandUtil.getInstance().getColor(mContext,R.color.white));
        }else{
            holder.itemLayout.setBackgroundColor(CommandUtil.getInstance().getColor(mContext,R.color.grey));
        }

        int index = position;
        holder.itemLayout.setOnClickListener(v ->{
            if(mListener!=null){
                mListener.onClickItem(index,0);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public void setSelected(int selected) {
        this.selected = selected;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView txt_title,txt_price,txt_date;
        private LinearLayout itemLayout;
        public MyViewHolder(View itemView) {
            super(itemView);
            txt_title = itemView.findViewById(R.id.txt_title);
            txt_price = itemView.findViewById(R.id.txt_price);
            txt_date = itemView.findViewById(R.id.txt_date);
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

