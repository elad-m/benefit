package com.benefit.UI;

import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.benefit.MacroFiles.Filter;
import com.benefit.R;
import com.benefit.StaticFunctions;

import java.util.List;

public class FilterPopup {

    private View view;
    private List<Filter> filters;

    public FilterPopup(View view, List<Filter> filters){
        this.view = view;
        this.filters = filters;
    }

    public void populateFilter(){
        LinearLayout body = view.findViewById(R.id.filter_body);
        for (Filter filter: filters){
            addFilter(filter, body);
        }
    }

    private void addFilter(Filter filter, LinearLayout body) {
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

    private void addAttributes(Filter filter, LinearLayout newLine) {
        HorizontalScrollView scrollAttribute = new HorizontalScrollView(view.getContext());
        LinearLayout attributeList = new LinearLayout(view.getContext());
        attributeList.setOrientation(LinearLayout.HORIZONTAL);
        for (String attribute :filter.getOptions()){
            Button attributeButton = new Button(view.getContext());
            attributeButton.setText(attribute);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            int margin = StaticFunctions.convertDpToPx(8);
            layoutParams.setMargins(margin, margin, margin, margin);
            layoutParams.gravity = Gravity.CENTER_VERTICAL;
            attributeButton.setLayoutParams(layoutParams);
            attributeButton.setMinimumHeight(StaticFunctions.convertDpToPx(25));
            attributeButton.setMinHeight(StaticFunctions.convertDpToPx(25));
            attributeButton.setBackground(view.getResources().getDrawable(R.drawable.oval));
            attributeList.addView(attributeButton);
        }
        scrollAttribute.addView(attributeList);
        newLine.addView(scrollAttribute);
    }

    private void addLineTitle(Filter filter, LinearLayout newLine) {
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
}
