package kr.co.genericit.mybase.xoyou2.fregment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import kr.co.genericit.mybase.xoyou2.R;

public class MainFragment3 extends Fragment {
    ViewPager viewPager;

    public static MainFragment3 newInstance(int number) {
        MainFragment3 fragment2 = new MainFragment3();
        Bundle bundle = new Bundle();
        bundle.putInt("number", number);
        fragment2.setArguments(bundle);
        return fragment2;
    }

    public MainFragment3(){

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

//        fragments = new int[]{R.layout.fragment_main_top_1,
//                R.layout.fragment_main_top_2,
//                R.layout.fragment_main_top_3,
//                R.layout.fragment_main_top_4};

        View view = inflater.inflate(R.layout.fragment_main_top_3, container, false);
        return view;

    }

}