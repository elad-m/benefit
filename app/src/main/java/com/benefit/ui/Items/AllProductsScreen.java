package com.benefit.ui.Items;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.benefit.R;
import com.benefit.utilities.StaticFunctions;
import com.benefit.ui.Displayable;
import com.benefit.ui.DisplayableRecycleAdapter;
import com.benefit.model.Category;
import com.benefit.model.CategoryCluster;
import com.benefit.model.Product;
import com.benefit.model.PropertyName;

import java.util.List;
import java.util.Map;

public class AllProductsScreen {

    private static final int PRODUCTS = 2;

    private View view;
    private Category currentCategory;
    private CategoryCluster categoryCluster;
    private View popupView;
    private FilterPopup filterPopup;
    private PopupWindow popup;
    private MetaCategoryBar metaCategoryBar;
    private ItemsDisplay itemsDisplay;


    public AllProductsScreen(View view, Category currentCategory, CategoryCluster categoryCluster) {
        this.view = view;
        this.currentCategory = currentCategory;
        this.categoryCluster = categoryCluster;
        itemsDisplay = new ItemsDisplay(view.findViewById(android.R.id.content).getRootView(), PRODUCTS);
    }

    public void addMetaCategoryBar(List<Category> categories, Category metaCategoryChosen) {
        metaCategoryBar = new MetaCategoryBar(view.findViewById(android.R.id.content).getRootView());
        metaCategoryBar.createCategoryBar(categories, metaCategoryChosen);
    }

    public void addDisplayTable(List<Product> products) {
        itemsDisplay.populateDisplayTable(products);
    }

    public void refreshTable() {
        itemsDisplay.refreshDisplay();
    }

    /**
     * writes the filter bar on the screen
     *
     * @param currentFilters the current filters of the items
     */
    public void writeFiltersOnScreen(Map<String, List<String>> currentFilters) {
        LinearLayout currentFilterLayout = view.findViewById(R.id.current_filters);
        currentFilterLayout.removeAllViews();
        writeCategoryInFilterBar(currentFilterLayout);
        if (currentFilters.size() > 0) {
            for (List<String> nameOfAttributes : currentFilters.values()) {
                writeOrSign(currentFilterLayout);
                int i = 0;
                while (i < nameOfAttributes.size()) {
                    writeFilter(nameOfAttributes.get(i), currentFilterLayout);
                    if (i < nameOfAttributes.size() - 1) {
                        writeOrSign(currentFilterLayout);
                    }
                    i++;
                }
            }
        }
    }

    private void writeCategoryInFilterBar(LinearLayout currentFilterLayout) {
        if (categoryCluster != null) {
            writeFilter(categoryCluster.getName(), currentFilterLayout);
        } else {
            writeFilter(currentCategory.getName(), currentFilterLayout);
        }
    }

    private void writeOrSign(LinearLayout currentFilterLayout) {
        TextView orSign = new TextView(view.getContext());
        orSign.setText(view.getResources().getString(R.string.divider));
        currentFilterLayout.addView(orSign);
    }

    private void writeFilter(String name, LinearLayout currentFilterLayout) {
        TextView filterText = new TextView(view.getContext());
        filterText.setText(name);
        setTextLayoutMargins(filterText);
        currentFilterLayout.addView(filterText);
    }

    private void setTextLayoutMargins(TextView filterText) {
        LinearLayout.LayoutParams layoutParams = new
                LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(StaticFunctions.convertDpToPx(5), 0,
                StaticFunctions.convertDpToPx(5), 0);
        filterText.setLayoutParams(layoutParams);
    }

    /**
     * opens the filter popup
     *
     * @param currentFilters the current filters wanted
     */
    public void openFilter(View view, List<PropertyName> filters, Map<String, List<String>> currentFilters) {

        RelativeLayout viewGroup = view.findViewById(R.id.filter_popup);
        LayoutInflater layoutInflater = (LayoutInflater) view.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        popupView = layoutInflater.inflate(R.layout.filter, viewGroup);
        placePopupOnScreen();
        populatePopup(filters, currentFilters);
    }

    private void populatePopup(List<PropertyName> filters, Map<String, List<String>> currentFilters) {
        filterPopup = new FilterPopup(popupView, filters);
        filterPopup.populateFilter(currentFilters);
    }

    private void placePopupOnScreen() {
        popup = new PopupWindow(view.getContext());
        setPopupAttributes(popup, popupView);
        popup.showAtLocation(popupView, Gravity.END, 0, 0);
    }

    private void setPopupAttributes(PopupWindow popup, View layout) {
        int width = (int) ((view.getResources().getDisplayMetrics().widthPixels) / 1.2);
        int height = view.getResources().getDisplayMetrics().heightPixels;
        popup.setContentView(layout);
        popup.setWidth(width);
        popup.setHeight(height);
        popup.setFocusable(true);
    }

    /**
     * gets the popup view
     *
     * @return popup view
     */
    public View getPopupView() {
        return popupView;
    }

    /**
     * gets the filter popup
     *
     * @return filterPopup
     */
    public FilterPopup getFilterPopup() {
        return filterPopup;
    }

    /**
     * get the popup window
     *
     * @return popup
     */
    public PopupWindow getPopup() {
        return popup;
    }

    public Map<Category, Button> getMetaCategoryButtonMap() {
        return metaCategoryBar.getMetaCategoryButtonMap();
    }

    public DisplayableRecycleAdapter getmAdapter() {
        return itemsDisplay.getmAdapter();
    }

    public List<? extends Displayable> getDisplayableItems() {
        return itemsDisplay.getDisplayableItems();
    }
}
