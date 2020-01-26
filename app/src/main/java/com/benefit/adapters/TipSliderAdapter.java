package com.benefit.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.benefit.R;

/**
 * Engine behind the sliding pages of the TipActivity
 */
public class TipSliderAdapter extends PagerAdapter {

    private Context context;

    public TipSliderAdapter(Context context) {
        this.context = context;
    }

    public int[] slide_images = {
            R.drawable.tip_background,
            R.drawable.tip_wear_it,
            R.drawable.tip_lighting,
    };

    private int[] slide_icons = {
            R.drawable.ic_tip_background,
            R.drawable.ic_tip_wear_it,
            R.drawable.ic_tip_lighting
    };

    private int[] slide_headings = {
            R.string.tip_background_title,
            R.string.tip_wear_it_title,
            R.string.tip_lighting_title
    };

    private int[] slide_texts = {
            R.string.tip_background_description,
            R.string.tip_wear_it_description,
            R.string.tip_lighting_description
    };


    @Override
    public int getCount() {
        return slide_images.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (LinearLayout) object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View tipLayoutView = layoutInflater.inflate((R.layout.tip_slide_layout), container, false);
        instantiateTipComponents(tipLayoutView, position);
        container.addView(tipLayoutView);
        return tipLayoutView;
    }

    private void instantiateTipComponents(View tipLayoutView, int position) {
        ImageView slideIcon = tipLayoutView.findViewById(R.id.tip_icon);
        slideIcon.setImageResource(slide_icons[position]);
        ImageView slideImageView = tipLayoutView.findViewById(R.id.tip_slide_image);
        slideImageView.setImageResource(slide_images[position]);
        TextView tipHeading = tipLayoutView.findViewById(R.id.tip_heading);
        tipHeading.setText(slide_headings[position]);
        TextView tipDescription = tipLayoutView.findViewById(R.id.tip_description);
        tipDescription.setText(slide_texts[position]);
    }


    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout) object);
    }
}
