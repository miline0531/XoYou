package kr.co.genericit.mybase.xoyou2.activity;

import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import kr.co.genericit.mybase.xoyou2.R;
import kr.co.genericit.mybase.xoyou2.common.CommonActivity;

public class CardImageActivity extends CommonActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_card_edit);
        super.onCreate(savedInstanceState);

        initView();
    }

    ImageView cardBg;
    public void initView(){
        cardBg = findViewById(R.id.card_bg);
        String imagePath = getIntent().getStringExtra("imagePath");
        Glide.with(this).load(imagePath).into(cardBg);

    }
}