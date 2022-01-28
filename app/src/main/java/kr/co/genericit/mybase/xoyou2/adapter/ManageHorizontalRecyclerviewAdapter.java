package kr.co.genericit.mybase.xoyou2.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import kr.co.genericit.mybase.xoyou2.R;
import kr.co.genericit.mybase.xoyou2.model.Mong;

public class ManageHorizontalRecyclerviewAdapter extends RecyclerView.Adapter<ManageHorizontalRecyclerviewAdapter.MyViewHolder>{
    private Context mContext;
    private ArrayList<Mong> data;
    public ManageHorizontalRecyclerviewAdapter(Context context, ArrayList<Mong> data){
        mContext = context;
        this.data = data;
    }

    @NonNull
    @Override
    public ManageHorizontalRecyclerviewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_manage_list_horizontal,parent,false);
        return new ManageHorizontalRecyclerviewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ManageHorizontalRecyclerviewAdapter.MyViewHolder holder, int position) {


        String resName = "a_sample_icon"+position;
        //holder.image.setBackgroundResource(findRes(resName));
        Glide.with(mContext).load(data.get(position).getIMAGE_URL()).thumbnail(0.1f).centerCrop().placeholder(findRes(resName)).into(holder.image);

        holder.text.setText(data.get(position).getTITLE());
        holder.mongtype.setText(data.get(position).getMONG_TYPE()+"꿈");
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
        private TextView text,mongtype;
        private ImageView image;
        private LinearLayout itemLayout;
        public MyViewHolder(View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.manage_text);
            mongtype = itemView.findViewById(R.id.manage_mongtype);
            image = itemView.findViewById(R.id.manage_image_view);
            itemLayout = itemView.findViewById(R.id.item_layout);
        }


    }

    private ManageHorizontalRecyclerviewAdapter.listOnClickListener mListener;

    public void setListOnClickListener(ManageHorizontalRecyclerviewAdapter.listOnClickListener listener){
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
