package com.benefit.UI;

import android.graphics.Color;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.benefit.R;
import com.benefit.StaticFunctions;
import com.benefit.model.PropertyName;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FilterPopup {

    private View view;
    private List<PropertyName> filters;
//    private Map<String, Button> filterOptionsMap;
    private Map<String, String> currentFilters;

    public FilterPopup(View view, List<PropertyName> filters){
        this.view = view;
        currentFilters = new HashMap<>();
        this.filters = filters;
    }

    public void populateFilter(Map<String, String> currentFilters){
        LinearLayout body = view.findViewById(R.id.filter_body);

        this.currentFilters = currentFilters;

        for (PropertyName filter: filters){
            addFilter(filter, body);
        }
    }

    private void addFilter(PropertyName filter, LinearLayout body) {
        LinearLayout newLine = new LinearLayout(view.getContext());
        newLine.setOrientation(LinearLayout.HORIZONTAL);
        addLineTitle(filter, newLine);
        addAttributes(filter, newLine);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT );
        int margin = StaticFunctions.convertDpToPx(5);
        layoutParams.setMargins(0, margin, 0, margin);
        newLine.setLayoutParams(layoutParams);
        body.addView(newLine);
    }

    private void addAttributes(PropertyName filter, LinearLayout newLine) {
        HorizontalScrollView scrollAttribute = new HorizontalScrollView(view.getContext());
        LinearLayout attributeList = new LinearLayout(view.getContext());
        attributeList.setOrientation(LinearLayout.HORIZONTAL);
        for (final String attribute: filter.getValidValues()){

            final Button attributeButton = new Button(view.getContext());
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            int margin = StaticFunctions.convertDpToPx(8);
            layoutParams.setMargins(margin, margin, margin, margin);
//            layoutParams.height = ;
//            layoutParams.height = StaticFunctions.convertDpToPx(20);
            layoutParams.gravity = Gravity.CENTER_VERTICAL;
            attributeButton.setLayoutParams(layoutParams);
            attributeButton.setText(attribute);
            if (currentFilters != null && containsKeyAndValue(filter.getName(), attribute)){
                attributeButton.setBackground(view.getResources().getDrawable(R.drawable.filled_oval));
                attributeButton.setTextColor(Color.parseColor("#ffff"));
            } else {
                attributeButton.setBackground(view.getResources().getDrawable(R.drawable.oval));
            }
//            attributeButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, attributeButton.getHeight()/ 2);
//            attributeButton.setTextSize(StaticFunctions.convertDpToSp(StaticFunctions.
//                    convertPixelsToDp((float) (0.015 * view.getResources().getDisplayMetrics().heightPixels))));

//            attributeButton.setMinimumHeight((int) (0.02 * view.getResources().getDisplayMetrics().heightPixels));
//            attributeButton.setMinHeight((int) (0.02 * view.getResources().getDisplayMetrics().heightPixels));
            attributeButton.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onClick(View v) {
                    if (currentFilters != null && containsKeyAndValue(filter.getName(), attribute)){
                        attributeButton.setBackground(view.getResources().getDrawable(R.drawable.oval));
                        currentFilters.remove(filter.getName(), attribute);
                    } else {
                        attributeButton.setBackground(view.getResources().getDrawable(R.drawable.filled_oval));
                        currentFilters.put(filter.getName(), attribute);
                    }
                }
            });
            attributeList.addView(attributeButton);

        }
        scrollAttribute.addView(attributeList);
        newLine.addView(scrollAttribute);
    }

    private boolean containsKeyAndValue(String filter, String attribute) {
        if (currentFilters.containsKey(filter) && currentFilters.get(filter).equals(attribute)){
            return true;
        } else {
            return false;
        }
    }

    private void addLineTitle(PropertyName filter, LinearLayout newLine) {
        TextView filterName = new TextView(view.getContext());
        String name = filter.getName() + view.getResources().getString(R.string.filter_break);
        filterName.setText(name);
        filterName.setTextSize(StaticFunctions.convertDpToSp(15));
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER_VERTICAL;
        filterName.setLayoutParams(layoutParams);
        newLine.addView(filterName);
    }

    public Map<String, String> getCurrentFilters(){
        return currentFilters;
    }

    public void refresh() {
        LinearLayout body = view.findViewById(R.id.filter_body);
        body.removeAllViews();
        currentFilters.clear();
        populateFilter(currentFilters);
    }
}
