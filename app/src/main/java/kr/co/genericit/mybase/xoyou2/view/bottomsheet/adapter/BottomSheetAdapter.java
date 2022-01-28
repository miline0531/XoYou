package kr.co.genericit.mybase.xoyou2.view.bottomsheet.adapter;

import android.annotation.SuppressLint;
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

import kr.co.genericit.mybase.xoyou2.R;
import kr.co.genericit.mybase.xoyou2.view.bottomsheet.model.BottomSheetData;

public class BottomSheetAdapter extends RecyclerView.Adapter<BottomSheetAdapter.ViewHolder> {
        int prevSelectIndex = -1;
        private Context mContext;
        private ArrayList<BottomSheetData> localDataSet;
        private boolean checkVisible = false;

        /**
         * Provide a reference to the type of views that you are using
         * (custom ViewHolder).
         */

        public void setCheckVisible(boolean visible){
            checkVisible = visible;
        }
        public static class ViewHolder extends RecyclerView.ViewHolder {
            ImageView img_check;
            TextView text_title;
            RelativeLayout root_layout;
            public ViewHolder(View view) {
                super(view);
                // Define click listener for the ViewHolder's View
                root_layout = (RelativeLayout) view.findViewById(R.id.root_layout);
                text_title = (TextView) view.findViewById(R.id.text_title);
                img_check = (ImageView) view.findViewById(R.id.img_check);
            }

            public ImageView getImg_check() {
                return img_check;
            }

            public TextView getText_title() {
                return text_title;
            }

            public RelativeLayout getRoot_layout() {
                return root_layout;
            }
        }

        /**
         * Initialize the dataset of the Adapter.
         *
         * @param dataSet String[] containing the data to populate views to be used
         * by RecyclerView.
         */
        public BottomSheetAdapter(Context context, ArrayList<BottomSheetData> dataSet) {
            mContext = context;
            localDataSet = dataSet;
            Log.v("ifeelbluu", "localDataSetSize :: " + localDataSet.size());
        }

        // Create new views (invoked by the layout manager)
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            // Create a new view, which defines the UI of the list item
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.item_bottom_sheet_default, viewGroup, false);
            return new ViewHolder(view);
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(ViewHolder viewHolder, @SuppressLint("RecyclerView") final int position) {

            final BottomSheetData item = localDataSet.get(position);
            viewHolder.getText_title().setText(item.getItemTitle());

            if(item.isCheck()){
                viewHolder.getImg_check().setVisibility(View.VISIBLE);
                prevSelectIndex = position;
            }else{
                viewHolder.getImg_check().setVisibility(View.GONE);
            }

            if(!checkVisible){
                viewHolder.getImg_check().setVisibility(View.GONE);
            }

            viewHolder.getRoot_layout().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    if(prevSelectIndex != -1){
                        localDataSet.get(prevSelectIndex).setCheck(false);
                    }

                    localDataSet.get(position).setCheck(true);
                    mListener.onClick(position, prevSelectIndex);

                    notifyDataSetChanged();
                }
            });
        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return localDataSet.size();
        }


        private itemClickListener mListener;

        public void setItemClickListener(itemClickListener listener){
            mListener = listener;
        }

        public interface itemClickListener {
            void onClick(int current, int  prev);
        }
}
