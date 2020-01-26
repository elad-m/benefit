package com.benefit.activities;

import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.Gravity;
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
public class TipsActivity extends AppCompatActivity {

    private final Spanned mHtmlFullDot = Html.fromHtml("&#9679");
    private final Spanned mHtmlCircle = Html.fromHtml("&#9675;");

    private ViewPager mSlideViewPager;
    private LinearLayout mDotLayout;
    private TextView[] mDots;
    private int mNumOfPages;

    private int mChosenColor;

    private Button mNextButton;
    private Button mBackButton;

    private int mCurrentPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tips_layout);

        instantiateLayoutComponents();
    }

    private void instantiateLayoutComponents(){
        mChosenColor = getResources().getColor(R.color.colorBenefitBlue);
        mDotLayout = findViewById(R.id.dots);
        mNextButton = findViewById(R.id.tips_next_button);
        mBackButton = findViewById(R.id.tips_back_button);
        setNextButton();
        setBackButton();
        initializeViewPager();
    }

    private void setNextButton(){
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurrentPage == mDots.length - 1) {
                    finish();
                } else {
                    mSlideViewPager.setCurrentItem(mCurrentPage + 1);
                }
            }
        });
    }

    private void setBackButton(){
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

    private void initializeViewPager() {
        mSlideViewPager = findViewById(R.id.tip_view_pager);
        TipSliderAdapter mTipsSliderAdapter = new TipSliderAdapter(this);
        mSlideViewPager.setAdapter(mTipsSliderAdapter);
        mNumOfPages = mTipsSliderAdapter.slide_images.length;
        mSlideViewPager.addOnPageChangeListener(mViewListener);
        initializeDotsIndicator();
    }

    private void initializeDotsIndicator() {
        mDots = new TextView[mNumOfPages];
        for (int i = 0; i < mDots.length; i++) {
            mDots[i] = new TextView(this);
            mDots[i].setGravity(Gravity.CENTER);
            mDots[i].setText(mHtmlCircle);
            mDots[i].setTextSize(20);
            mDots[i].setTextColor(mChosenColor);
            mDotLayout.addView(mDots[i]);
        }
        mDots[0].setText(mHtmlFullDot);
        mDots[0].setTextColor(mChosenColor);
    }

    public void addDotsIndicator(int position) {
        if (position > 0)
            mDots[position - 1].setText(mHtmlCircle);
        mDots[position].setText(mHtmlFullDot);
        if (position < mNumOfPages - 1)
            mDots[position + 1].setText(mHtmlCircle);
    }


    ViewPager.OnPageChangeListener mViewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addDotsIndicator(position);
            mCurrentPage = position;
            if (position == 0) {
                mBackButton.setVisibility(View.INVISIBLE);
            } else if (position == mDots.length - 1) {
                mNextButton.setText(getResources().getString(R.string.view_pager_finish_button_text));
            } else {
                mNextButton.setText(getResources().getString(R.string.view_pager_next_button_text));
                mBackButton.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };


}
