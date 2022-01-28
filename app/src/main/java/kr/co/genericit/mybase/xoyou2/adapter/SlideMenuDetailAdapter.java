package kr.co.genericit.mybase.xoyou2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import kr.co.genericit.mybase.xoyou2.R;
import kr.co.genericit.mybase.xoyou2.model.SlideMenuDetailData;

public class SlideMenuDetailAdapter extends RecyclerView.Adapter<SlideMenuDetailAdapter.ViewHolder> {

        private Context mContext;
        private ArrayList<SlideMenuDetailData> localDataSet;
        private String toDate = "";

        /**
         * Provide a reference to the type of views that you are using
         * (custom ViewHolder).
         */
        public static class ViewHolder extends RecyclerView.ViewHolder {
            private final LinearLayout rl_root;
            private final TextView txt_title, txt_sub_title;

            public ViewHolder(View view) {
                super(view);
                // Define click listener for the ViewHolder's View

                rl_root = view.findViewById(R.id.rl_root);
                txt_title = view.findViewById(R.id.txt_title);
                txt_sub_title = view.findViewById(R.id.txt_sub_title);
            }

            public LinearLayout getRl_root() {
                return rl_root;
            }

            public TextView getTxt_title() {
                return txt_title;
            }

            public TextView getTxt_sub_title() {
                return txt_sub_title;
            }
        }

        /**
         * Initialize the dataset of the Adapter.
         *
         * @param dataSet String[] containing the data to populate views to be used
         * by RecyclerView.
         */
        public SlideMenuDetailAdapter(Context context, ArrayList<SlideMenuDetailData> dataSet) {
            mContext = context;
            localDataSet = dataSet;

        }

        // Create new views (invoked by the layout manager)
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            // Create a new view, which defines the UI of the list item
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.item_slide_detail_menu, viewGroup, false);

            return new ViewHolder(view);
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(ViewHolder viewHolder, final int position) {

            final SlideMenuDetailData item = localDataSet.get(position);

            viewHolder.txt_title.setText(item.getTxt_menu_title());
            viewHolder.txt_sub_title.setText(item.getTxt_menu_sub_title());

            //클릭이벤트
            viewHolder.getRl_root().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onClickItem(item.getId(), 0);
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
}
