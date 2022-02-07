package com.redrover.xoyou.utils;

import android.graphics.Rect;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class RecyclerDecoration extends RecyclerView.ItemDecoration {
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
        }else if(orientation.equals("2LineHorizontal")){
            if(parent.getChildAdapterPosition(view) == 0 || parent.getChildAdapterPosition(view) == 1){
                outRect.left = divHeight * 2;
                outRect.right = divHeight;
                outRect.bottom = divHeight;
            }else if(parent.getChildAdapterPosition(view) == parent.getChildCount()-1){
                outRect.left = divHeight;
                outRect.right = divHeight * 2;
                outRect.bottom = divHeight;
            }else{
                outRect.left = divHeight;
                outRect.right = divHeight;
                outRect.bottom = divHeight;
            }
        }else if(orientation.equals("singleLineHorizontal")){
            if(parent.getChildAdapterPosition(view) == 0){
                outRect.left = divHeight * 2;
                outRect.right = divHeight;
            }else if(parent.getChildAdapterPosition(view) == parent.getChildCount()-1){
                outRect.left = divHeight;
                outRect.right = divHeight * 2;
            }else{
                outRect.left = divHeight;
                outRect.right = divHeight;
            }
        }else{
//            outRect.bottom = divHeight*2;
        }
    }
}