package com.benefit.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.benefit.R;

public class TipSliderAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;

    public TipSliderAdapter(Context context){
        this.context = context;
    }

    public int[] slide_images = {
            R.drawable.tip_background,
            R.drawable.tip_wear_it,
            R.drawable.tip_lighting,
            R.drawable.tip_preparations
    };


    @Override
    public int getCount() {
        return slide_images.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (RelativeLayout) object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View tipLayoutView = layoutInflater.inflate((R.layout.tip_slide_layout),container, false);

        ImageView slideImageView = tipLayoutView.findViewById(R.id.tip_slide_image);
        slideImageView.setImageResource(slide_images[position]);

        container.addView(tipLayoutView);
        return tipLayoutView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout) object);
    }
}
