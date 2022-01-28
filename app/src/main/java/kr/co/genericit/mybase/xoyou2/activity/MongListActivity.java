package kr.co.genericit.mybase.xoyou2.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;

import java.util.ArrayList;

import kr.co.genericit.mybase.xoyou2.adapter.MongListRecyclerviewAdapter;
import kr.co.genericit.mybase.xoyou2.databinding.ActivityMongListBinding;
import kr.co.genericit.mybase.xoyou2.model.Mong;

public class MongListActivity extends AppCompatActivity {

    private ActivityMongListBinding binding;
    private MongListRecyclerviewAdapter adapter;
    private ArrayList<Mong> data;
    private RecyclerView recyclerView;
    private boolean isLoading = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMongListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initView();
    }

    public void initView(){
        String title = getIntent().getStringExtra("title");
        binding.title.setText(title);
        recyclerView = binding.rcvMongList;

        data = new ArrayList<>();
        data = (ArrayList<Mong>) getIntent().getSerializableExtra("data");
        adapter = new MongListRecyclerviewAdapter(this, data);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void initScrollListener() {
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (!isLoading) {
                    if (layoutManager != null && layoutManager.findLastCompletelyVisibleItemPosition() == data.size() - 1) {
                        //리스트 마지막
                        loadMore();
                        isLoading = true;
                    }
                }
            }
        });
    }

    private void loadMore() {
        data.add(null);
        adapter.notifyItemInserted(data.size() - 1);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                data.remove(data.size() - 1);
                int scrollPosition = data.size();
                adapter.notifyItemRemoved(scrollPosition);
                int currentSize = scrollPosition;
                int nextLimit = currentSize + 10;

                while (currentSize - 1 < nextLimit) {
                   // data.add("Item " + currentSize);
                    currentSize++;
                }

                adapter.notifyDataSetChanged();
                isLoading = false;
            }
        }, 2000);
    }
}