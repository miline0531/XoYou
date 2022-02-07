package com.redrover.xoyou.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

import com.redrover.xoyou.R;
import com.redrover.xoyou.model.SlideMenuData;

public class SlideMenuAdapter extends RecyclerView.Adapter<SlideMenuAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<SlideMenuData> localDataSet;
    private String toDate = "";

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final RelativeLayout btn_slide_menu, btn_slide_count;
        private final TextView txt_slide_menu, txt_slide_count, img_slide_count;
        private final ImageView img_slide_menu;
        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            btn_slide_menu = view.findViewById(R.id.btn_slide_menu);
            btn_slide_count = view.findViewById(R.id.btn_slide_count);
            txt_slide_menu = view.findViewById(R.id.txt_slide_menu);
            txt_slide_count = view.findViewById(R.id.txt_slide_count);
            img_slide_menu = view.findViewById(R.id.img_slide_menu);
            img_slide_count = view.findViewById(R.id.img_slide_count);
        }

        public RelativeLayout getBtn_slide_menu() {
            return btn_slide_menu;
        }

        public RelativeLayout getBtn_slide_count() {
            return btn_slide_count;
        }

        public TextView getTxt_slide_menu() {
            return txt_slide_menu;
        }

        public TextView getTxt_slide_count() { return txt_slide_count; }


        public ImageView getImg_slide_menu() {
            return img_slide_menu;
        }

        public TextView getImg_slide_count() { return img_slide_count; }

           /* public ImageView getImg_slide_count() {
                return img_slide_count;
            }*/
    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used
     * by RecyclerView.
     */
    public SlideMenuAdapter(Context context, ArrayList<SlideMenuData> dataSet) {
        mContext = context;
        localDataSet = dataSet;

    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_slide_menu, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        final SlideMenuData item = localDataSet.get(position);


        viewHolder.getTxt_slide_menu().setText(item.getTxt_menu_title());
        viewHolder.getTxt_slide_count().setText(item.getTxt_count_title());
        Log.v("CHECK:", position + " : " + item.getTxt_count_num() );
        viewHolder.getImg_slide_count().setText(item.getTxt_count_num());

        if(item.getImg_menu_id().equals("temp")){
            //todo 이미지코드에 맞는 리소스 참조
            //viewHolder.getImg_slide_menu().setBackgroundResource(R.drawable.slide_icon);
        }

        viewHolder.getImg_slide_menu().setBackgroundResource(findRes("icon_slide_" + item.getId()));
        //클릭이벤트
        viewHolder.getBtn_slide_menu().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onClickItem(item.getId(), 0);
            }
        });

        viewHolder.getBtn_slide_count().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onClickItem(item.getId(), 1);
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.size();
    }


    private menuClickListener mListener;

    public void setMenuClickListener(menuClickListener listener){
        mListener = listener;
    }

    public interface menuClickListener {
        void onClickItem(int id, int menuType);
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
