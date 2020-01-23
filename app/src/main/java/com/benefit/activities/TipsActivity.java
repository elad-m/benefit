package com.benefit.activities;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.benefit.R;
import com.benefit.adapters.TipSliderAdapter;

/**
 * Activity that slides the tips for good photography
 */
public class TipsActivity extends AppCompatActivity{

    private ViewPager mSlideViewPager;
    private LinearLayout mDotLayout;

    private TextView[] mDots;

    private TipSliderAdapter tipSliderAdapter;

    private Button mNextButton;
    private Button mBackButton;

    private int mCurrentPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tips_layout);

        mSlideViewPager = findViewById(R.id.tip_view_pager);
        mDotLayout = findViewById(R.id.dots);
        mNextButton = findViewById(R.id.tips_next_button);
        mBackButton = findViewById(R.id.tips_back_button);

        tipSliderAdapter = new TipSliderAdapter(this);
        mSlideViewPager.setAdapter(tipSliderAdapter);

        initializeDotsIndicator();
        mSlideViewPager.addOnPageChangeListener(viewListener);

        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurrentPage == mDots.length - 1){
                    finish();
                } else {
                    mSlideViewPager.setCurrentItem(mCurrentPage + 1);
                }
            }
        });

        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurrentPage == 0) {
                    finish();
                } else {
                    mSlideViewPager.setCurrentItem(mCurrentPage - 1);
                }
            }
        });
    }

    private void initializeDotsIndicator(){
        mDots = new TextView[tipSliderAdapter.slide_images.length];
        for(int i = 0; i < mDots.length; i++){
            mDots[i] = new TextView(this);
            mDots[i].setText(Html.fromHtml("&#8226"));
            mDots[i].setTextSize(35);
            mDots[i].setTextColor(getResources().getColor(R.color.colorGrayDark));
            mDotLayout.addView(mDots[i]);
        }
        mDots[0].setTextColor(getResources().getColor(R.color.colorBenefitBlue));
    }

    public void addDotsIndicator(int position){
        for(TextView dot: mDots){
            dot.setTextColor(getResources().getColor(R.color.colorGrayDark));
        }
        mDots[position].setTextColor(getResources().getColor(R.color.colorBenefitBlue));
    }



    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addDotsIndicator(position);
            mCurrentPage = position;
            if (position == mDots.length - 1){
                mNextButton.setText("Finish");
            } else {
                mNextButton.setText("Next");
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };


}
