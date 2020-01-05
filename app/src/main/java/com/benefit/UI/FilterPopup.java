package com.benefit.UI;

import android.view.View;
import android.widget.LinearLayout;

import com.benefit.MacroFiles.Filter;
import com.benefit.R;

import java.util.List;

public class FilterPopup {

    View view;
    List<Filter> filters;

    public FilterPopup(View view, List<Filter> filters){
        this.view = view;
        this.filters = filters;
    }

    public void populateFilter(){
        LinearLayout body = view.findViewById(R.id.filter_body);
        for (Filter filter: filters){
            addFilter();
        }
    }

    private void addFilter() {
    }
}
