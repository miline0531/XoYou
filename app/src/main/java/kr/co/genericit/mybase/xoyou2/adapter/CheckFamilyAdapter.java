package kr.co.genericit.mybase.xoyou2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import kr.co.genericit.mybase.xoyou2.R;
import kr.co.genericit.mybase.xoyou2.network.response.FamilyMemberResult;

public class CheckFamilyAdapter extends RecyclerView.Adapter<CheckFamilyAdapter.ViewHolder> {

        private Context mContext;
        private ArrayList<FamilyMemberResult.Data.FamilyInfo> localDataSet;
        private String toDate = "";

        /**
         * Provide a reference to the type of views that you are using
         * (custom ViewHolder).
         */
        public static class ViewHolder extends RecyclerView.ViewHolder {
            private final CheckBox check_family;
            private final TextView txt_family;
            private final LinearLayout btn_check;
            public ViewHolder(View view) {
                super(view);
                // Define click listener for the ViewHolder's View

                check_family = view.findViewById(R.id.check_family);
                txt_family = view.findViewById(R.id.txt_family);
                btn_check = view.findViewById(R.id.btn_check);
            }

            public CheckBox getCheck_family() {
                return check_family;
            }

            public TextView getTxt_family() {
                return txt_family;
            }

            public LinearLayout getBtn_check() {
                return btn_check;
            }
        }

        /**
         * Initialize the dataset of the Adapter.
         *
         * @param dataSet String[] containing the data to populate views to be used
         * by RecyclerView.
         */
        public CheckFamilyAdapter(Context context, ArrayList<FamilyMemberResult.Data.FamilyInfo> dataSet) {
            mContext = context;
            localDataSet = dataSet;
        }

        // Create new views (invoked by the layout manager)
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            // Create a new view, which defines the UI of the list item
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.item_family_check, viewGroup, false);

            return new ViewHolder(view);
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(ViewHolder viewHolder, final int position) {

            final FamilyMemberResult.Data.FamilyInfo item = localDataSet.get(position);

            viewHolder.getTxt_family().setText(item.getFamilyName());

            if(item.getFamilyName().equals("none")){
                viewHolder.getBtn_check().setVisibility(View.INVISIBLE);
            }else{
                viewHolder.getBtn_check().setVisibility(View.VISIBLE);
            }


            //초기화하는 부분
            if(item.getFamilyYn().equals("Y")){
                viewHolder.getCheck_family().setChecked(true);
            }else{
                viewHolder.getCheck_family().setChecked(false);
            }

            viewHolder.getCheck_family().setEnabled(false);

            viewHolder.getBtn_check().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(item.getFamilyYn().equals("Y")){
                        viewHolder.getCheck_family().setChecked(false);
                        item.setFamilyYn("N");
                    }else{
                        viewHolder.getCheck_family().setChecked(true);
                        item.setFamilyYn("Y");
                    }

//                    notifyDataSetChanged();
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
