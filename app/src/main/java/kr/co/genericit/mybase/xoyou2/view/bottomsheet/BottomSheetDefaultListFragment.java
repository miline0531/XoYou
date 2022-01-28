package kr.co.genericit.mybase.xoyou2.view.bottomsheet;
import android.app.Dialog;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;

import kr.co.genericit.mybase.xoyou2.R;
import kr.co.genericit.mybase.xoyou2.view.bottomsheet.adapter.BottomSheetAdapter;
import kr.co.genericit.mybase.xoyou2.view.bottomsheet.model.BottomSheetData;

public class BottomSheetDefaultListFragment extends BottomSheetDialogFragment{
    bottomSheetListener mListener;
    RecyclerView rcv_bottom_sheet;
    BottomSheetAdapter mAdapter;
    ArrayList<BottomSheetData> mData;
    public boolean setFailDisable = false;

    public void setDisable(boolean value){
        setFailDisable = value;
    }

    public static BottomSheetDefaultListFragment newInstance() {
        BottomSheetDefaultListFragment fragment = new BottomSheetDefaultListFragment();
        return fragment;
    }

    public void setData(ArrayList<BottomSheetData> data){
        mData = new ArrayList<>();
        mData = data;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
    }

    @Override
    public void setupDialog(Dialog dialog, int style) {
        View contentView = View.inflate(getContext(), R.layout.bottom_sheet_default_list, null);
        dialog.setContentView(contentView);
        ((View) contentView.getParent()).setBackgroundColor(getResources().getColor(android.R.color.transparent));

        rcv_bottom_sheet = (RecyclerView) contentView.findViewById(R.id.rcv_bottom_sheet);
        mAdapter = new BottomSheetAdapter(((View) contentView.getParent()).getContext(), mData);
        mAdapter.setItemClickListener(onListClickListener);
        rcv_bottom_sheet.setAdapter(mAdapter);
        rcv_bottom_sheet.setLayoutManager(new LinearLayoutManager(((View) contentView.getParent()).getContext(),LinearLayoutManager.VERTICAL, false)) ;
        RecyclerDecoration spaceDecoration2 = new RecyclerDecoration(8,"vertical");
        rcv_bottom_sheet.addItemDecoration(spaceDecoration2);

    }


    public void setListener(bottomSheetListener listener){
        mListener = listener;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public interface bottomSheetListener {
        void onSelectItem(int type,int text);
    }

    BottomSheetAdapter.itemClickListener onListClickListener = new BottomSheetAdapter.itemClickListener() {
        @Override
        public void onClick(int code, int prev) {
            mListener.onSelectItem(code,prev);
            if(setFailDisable){
                if(code != 1) dismiss();
            }else{
                dismiss();
            }
        }
    };

    class RecyclerDecoration extends RecyclerView.ItemDecoration {
        private String orientation = "horizontal";
        private final int divHeight;


        public RecyclerDecoration(int divHeight, String orient) {
            this.divHeight = divHeight;
            this.orientation = orient;
        }

        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);


            if(orientation.equals("horizontal")){
                if(parent.getChildAdapterPosition(view) == 0){
                    outRect.left = divHeight * 2;
                    outRect.right = divHeight;
                }else{
                    outRect.left = divHeight;
                    outRect.right = divHeight;
                }
            }else{
                outRect.top = divHeight;
                outRect.bottom = divHeight*2;
            }

//            if (parent.getChildAdapterPosition(view) != parent.getAdapter().getItemCount() - 1)
//                outRect.right = divHeight;
        }
    }
}