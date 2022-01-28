package kr.co.genericit.mybase.xoyou2.activity.detail;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Random;

import kr.co.genericit.mybase.xoyou2.R;
import kr.co.genericit.mybase.xoyou2.adapter.CardRecyclerviewAdapter;
import kr.co.genericit.mybase.xoyou2.common.Constants;

public class MymongTabFragment_06 extends Fragment {
    private View v;

    private RecyclerView cardRecyclerview;

    private int mHeight = 0;
    public int getChildViewHeight(){
        return mHeight;
    }

    public static MymongTabFragment_06 newInstance(boolean isTooltipView) {
        MymongTabFragment_06 f = new MymongTabFragment_06();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_detail_6, container, false);

        initView(v);
        initRecycler();

        v.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mHeight = v.findViewById(R.id.scrollView).getHeight();
            }
        });
        return v;
    }

    public void initView(View v){
        cardRecyclerview = v.findViewById(R.id.rcv_card);
    }

    public void initRecycler(){
        ArrayList<String> data = getRandomCard();
        cardRecyclerview.setLayoutManager(new GridLayoutManager(getActivity(),3));
        CardRecyclerviewAdapter mAdapter = new CardRecyclerviewAdapter(getActivity(),data);
        cardRecyclerview.setAdapter(mAdapter);


    }

    JSONArray randomCardList;
    private ArrayList<String> getRandomCard(){
        ArrayList<String> data = new ArrayList<>();
        try {
            randomCardList = Constants.StoryJob.getJSONArray("MongJeomList");
        }catch (Exception e){
            Log.e("Mymong",e.getMessage());
        }

        int count = 9; // 난수 생성 갯수
        int a[] = new int[count];
        Random r = new Random();

        for(int i=0; i<count; i++){
            a[i] = r.nextInt(randomCardList.length()) + 1; // 1 ~ 99까지의 난수
            for(int j=0; j<i; j++){
                if(a[i] == a[j]){
                    i--;
                }
            }
        }
        for(int i=0; i<count; i++){
            try {
                data.add(randomCardList.getJSONObject(i).getString("GwanGyeInfo") +"\n"+randomCardList.getJSONObject(i).getString("Name"));

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return data;
    }

}
