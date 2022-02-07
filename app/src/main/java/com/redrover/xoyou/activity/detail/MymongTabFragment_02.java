package com.redrover.xoyou.activity.detail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import org.json.JSONException;

import com.redrover.xoyou.R;
import com.redrover.xoyou.common.Constants;
import com.redrover.xoyou.utils.CommandUtil;

public class MymongTabFragment_02 extends Fragment {
    private View v;


    public static MymongTabFragment_02 newInstance(boolean isTooltipView) {
        MymongTabFragment_02 f = new MymongTabFragment_02();
        return f;
    }
    private int mHeight = 0;
    public int getChildViewHeight(){
        return mHeight;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_detail_2, container, false);

        initView(v);

        v.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mHeight = v.findViewById(R.id.scrollView).getHeight();
            }
        });
        return v;
    }

    public void initView(View v){
        String MongInfo = "";
        String UiMi = "";
        String GoodBadMong = "";
        String mTitleType = "";
        try {
            MongInfo = Constants.StoryJob.getString("MongInfo");
            UiMi = Constants.StoryJob.getString("UiMi");
            GoodBadMong = Constants.StoryJob.getString("GoodBadMong");
            mTitleType = Constants.StoryJob.getString("Name"); //Name
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ((TextView)v.findViewById(R.id.txt_title)).setText(mTitleType+ CommandUtil.getInstance().getStr(R.string.mong_detail_and) + GoodBadMong);
        ((TextView)v.findViewById(R.id.txt_top)).setText(MongInfo);
        ((TextView)v.findViewById(R.id.txt_bottom)).setText(UiMi);
    }

}
