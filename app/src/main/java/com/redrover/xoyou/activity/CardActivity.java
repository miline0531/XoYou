package com.redrover.xoyou.activity;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

import com.redrover.xoyou.R;
import com.redrover.xoyou.adapter.CardRecyclerviewAdapter;
import com.redrover.xoyou.common.CommonActivity;

public class CardActivity extends CommonActivity {
    RecyclerView cardRecyclerview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);
        initView();
    }

    public void initView(){
        cardRecyclerview = findViewById(R.id.rcv_card);
        initRecycler();
    }

    public void initRecycler(){
        ArrayList<String> data = new ArrayList<>();
        for(int i=0; i<9; i++){
            data.add("a");
        }
        cardRecyclerview.setLayoutManager(new GridLayoutManager(this,3));
        CardRecyclerviewAdapter mAdapter = new CardRecyclerviewAdapter(this,data);
        cardRecyclerview.setAdapter(mAdapter);
    }
}