package kr.co.genericit.mybase.xoyou2.adapterxo;

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

import kr.co.genericit.mybase.xoyou2.R;
import kr.co.genericit.mybase.xoyou2.model.SimRi;

public class XoYouUserLoadRecyclerviewAdapter extends RecyclerView.Adapter<XoYouUserLoadRecyclerviewAdapter.MyViewHolder>{
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

