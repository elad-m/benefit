package com.benefit.ui.Items;

import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.benefit.R;
import com.benefit.model.PropertyName;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FilterPopup {

    private View view;
    private LinearLayout layout;
    private List<PropertyName> filters;
    private Map<String, List<String>> currentFilters;

    FilterPopup(View view, List<PropertyName> filters) {

        this.view = view;
        currentFilters = new HashMap<>();
        this.filters = filters;
    }

    void populateFilter(Map<String, List<String>> currentFilters) {
        layout = view.findViewById(R.id.filter_body);
        inflater = LayoutInflater.from(view.getContext());
        this.currentFilters = currentFilters;

        for (PropertyName filter : filters) {
            if (filter.getValidValues() != null && filter.getValidValues().size() > 0) {
                createChips(filter);
            }
        }
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


    private LayoutInflater inflater;


    private void createChips(PropertyName propertyName) {
        ChipGroup chipGroup = createChipGroup(propertyName.getName());
        for (String property : propertyName.getValidValues()) {
            View chipAsView = inflater.inflate(R.layout.chip_layout, null);
            Chip chip = (Chip) chipAsView;
            chip.setText(property);
            chip.setTag(property);
            setCheckOnChip(propertyName, property, chip);
            chip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (chip.isChecked()) {
                        addAttributeToCurrentFilters(propertyName.getName(), property);
                    } else {
                        removeAttributeFromCurrentFilters(propertyName.getName(), property);
                    }
                }
            });
            chipGroup.addView(chipAsView);
        }
        layout.addView(
                chipGroup, layout.getChildCount() - 1);
    }

    private void setCheckOnChip(PropertyName propertyName, String property, Chip chip) {
        if (currentFilters.size() > 0){
            if (currentFilterContainsKeyAndValue(propertyName.getName(), property)){
                chip.setChecked(true);
            }
        }
        chip.setChecked(false);
    }

    private boolean currentFilterContainsKeyAndValue(String filter, String attribute) {
        return (currentFilters.containsKey(filter) &&
                currentFilters.get(filter).contains(attribute));

    }

    private void removeAttributeFromCurrentFilters(String name, String property) {
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

    private ChipGroup createChipGroup(String groupName) {
        View chipGroupAsView = inflater.inflate(R.layout.chip_group_layout, null);
        ChipGroup chipGroup = (ChipGroup) chipGroupAsView;
        TextView filterName = ((TextView) chipGroup.getChildAt(0));
        String text = groupName + ": ";
        filterName.setText(text);
        filterName.setTypeface(filterName.getTypeface(), Typeface.BOLD);
        filterName.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        return chipGroup;
    }
}
