package com.benefit.ui.Items;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

import com.benefit.R;
import com.benefit.utilities.StaticFunctions;
import com.benefit.model.PropertyName;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FilterPopup {

    private View view;
    private List<PropertyName> filters;
    private Map<String, List<String>> currentFilters;

    public FilterPopup(View view, List<PropertyName> filters) {
        this.view = view;
        currentFilters = new HashMap<>();
        this.filters = filters;
    }

    public void populateFilter(Map<String, List<String>> currentFilters) {
        LinearLayout body = view.findViewById(R.id.filter_body);

        this.currentFilters = currentFilters;

        for (PropertyName filter : filters) {
            if (filter.getValidValues() != null && filter.getValidValues().size() > 0) {
                addFilter(filter, body);
            }
        }
    }

    private void addFilter(PropertyName filter, LinearLayout body) {
        RelativeLayout line = new RelativeLayout(view.getContext());
        defineLineAttributes(line);
        int titleId = addLineTitle(filter, line);


        addAttributes(filter, line, titleId);
        body.addView(line);
    }

    private void defineLineAttributes(RelativeLayout line) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.height = 0;
        layoutParams.weight = (float) 0.3;
        line.setLayoutParams(layoutParams);
    }

    private void addAttributes(PropertyName filter, RelativeLayout line, int titleId) {
        HorizontalScrollView scrollView = new HorizontalScrollView(view.getContext());
        setScrollViewToRightOfText(scrollView, titleId);
        ChipGroup chipGroup = new ChipGroup(view.getContext());
        chipGroup.setChipSpacing(StaticFunctions.convertDpToPx(10));

        setChipGroupLayoutParams(chipGroup);
        if (filter.getValidValues() != null) {
            for (final String propertyName : filter.getValidValues()) {

                Chip chip = new Chip(view.getContext(), null, R.attr.CustomChipChoiceStyle);
                chip.setText(propertyName);

                if (currentFilterContainsKeyAndValue(filter.getName(), propertyName)) {
                    chip.setChipBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(view.getContext(), R.color.lightBlue)));
                    chip.setTextColor(Color.WHITE);
                }

                addClickListener(chip, filter, propertyName);
                chipGroup.addView(chip);

            }
            scrollView.addView(chipGroup);

            line.addView(scrollView);
        }

    }

    private void addClickListener(Chip chip, PropertyName filter, String propertyName) {
        chip.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                if (currentFilterContainsKeyAndValue(filter.getName(), propertyName)) {
                    chip.setChipBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(view.getContext(), R.color.browser_actions_bg_grey)));
                    chip.setTextColor(Color.BLACK);
                    removeAttributeToCurrentFilters(filter.getName(), propertyName);
                } else {
                    chip.setChipBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(view.getContext(), R.color.lightBlue)));
                    chip.setTextColor(Color.WHITE);
                    addAttributeToCurrentFilters(filter.getName(), propertyName);
                }
            }
        });
    }

    private void removeAttributeToCurrentFilters(String name, String property) {
        if (currentFilters.get(name).size() > 1) {
            currentFilters.get(name).remove(property);
        } else {
            currentFilters.remove(name);
        }
    }

    private void addAttributeToCurrentFilters(String name, String property) {
        if (currentFilters.containsKey(name)) {
            currentFilters.get(name).add(property);
        } else {
            List<String> newList = new ArrayList<>();
            newList.add(property);
            currentFilters.put(name, newList);
        }
    }

    private void setScrollViewToRightOfText(HorizontalScrollView scrollView, int titleId) {
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.RIGHT_OF, titleId);
        scrollView.setLayoutParams(layoutParams);

    }

    private void setChipGroupLayoutParams(ChipGroup chipGroup) {
        HorizontalScrollView.LayoutParams layoutParams = new HorizontalScrollView.LayoutParams(
                HorizontalScrollView.LayoutParams.WRAP_CONTENT,
                HorizontalScrollView.LayoutParams.MATCH_PARENT);
        layoutParams.gravity = Gravity.CENTER_VERTICAL;
        int margin = StaticFunctions.convertDpToPx(5);
        layoutParams.setMargins(margin, margin, margin, margin);
        chipGroup.setLayoutParams(layoutParams);


    }

    private boolean currentFilterContainsKeyAndValue(String filter, String attribute) {
        if (currentFilters.containsKey(filter)) {
            for (String currentFilterAttribute : currentFilters.get(filter)) {
                if (currentFilterAttribute.equals(attribute)) {
                    return true;
                }
            }
        }
        return false;
    }

    private int addLineTitle(PropertyName filter, RelativeLayout line) {
        TextView filterName = new TextView(view.getContext());
        setTextAttributes(filterName, filter);
        setLayoutAttributes(filterName);
        line.addView(filterName);
        filterName.setId(View.generateViewId());
        return filterName.getId();
    }

    private void setLayoutAttributes(TextView filterName) {
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        filterName.setLayoutParams(layoutParams);
        int padding = StaticFunctions.convertDpToPx(6);
        filterName.setPadding(padding, padding, padding, padding);
    }

    private void setTextAttributes(TextView filterName, PropertyName filter) {
        String name = filter.getName() + view.getResources().getString(R.string.filter_break);
        filterName.setText(name);
        filterName.setTextSize(StaticFunctions.convertDpToSp(15));
    }

    public Map<String, List<String>> getCurrentFilters() {
        return currentFilters;
    }

    public void refreshFilter() {
        LinearLayout body = view.findViewById(R.id.filter_body);
        body.removeAllViews();
        currentFilters.clear();
        populateFilter(currentFilters);
    }
}
