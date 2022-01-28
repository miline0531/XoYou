package kr.co.genericit.mybase.xoyou2.adapter;

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

import kr.co.genericit.mybase.xoyou2.R;

public class ReccomendRecyclerviewAdapter extends RecyclerView.Adapter<ReccomendRecyclerviewAdapter.MyViewHolder>{
    private Context mContext;
    private ArrayList<String> data;
    public ReccomendRecyclerviewAdapter(Context context, ArrayList<String> data){
        mContext = context;
        this.data = data;
    }

    @NonNull
    @Override
    public ReccomendRecyclerviewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_recommend_list,parent,false);
        return new ReccomendRecyclerviewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReccomendRecyclerviewAdapter.MyViewHolder holder, int position) {
        holder.text.setText(data.get(position));
        String resName = "recmd"+position;
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
            text = itemView.findViewById(R.id.manage_text);
            image = itemView.findViewById(R.id.manage_image_view);
        }
    }

    private ReccomendRecyclerviewAdapter.listOnClickListener mListener;

    public void setListOnClickListener(ReccomendRecyclerviewAdapter.listOnClickListener listener){
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
