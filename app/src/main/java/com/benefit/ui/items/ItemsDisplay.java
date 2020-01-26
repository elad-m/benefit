package com.benefit.ui.items;

import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.benefit.R;
import com.benefit.adapters.DisplayableRecycleAdapter;
import com.benefit.ui.Displayable;

import java.util.ArrayList;
import java.util.List;

/**
 * The UI for the part of the screen that displays the items (categories/products)
 */
public class ItemsDisplay {

    private View view;

    private List<Displayable> displayableItems;

    private RecyclerView recyclerView;
    private DisplayableRecycleAdapter displayableRecycleAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private int typeOfDisplay;


    public ItemsDisplay(View view, int typeOfDisplay) {
        this.view = view;
        this.typeOfDisplay = typeOfDisplay;
        displayableItems = new ArrayList<>();
    }

    public <T extends Displayable> void populateDisplayTable(List<T> displayableItems) {
        addItemsToDisplayableItems(displayableItems);
        if (this.displayableItems.size() > 0) {
            view.findViewById(R.id.no_item_text).setVisibility(View.INVISIBLE);
        } else {
            view.findViewById(R.id.no_item_text).setVisibility(View.VISIBLE);
        }
        recyclerView = view.findViewById(R.id.categories);
        recyclerView.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(view.getContext(), 2);
        displayableRecycleAdapter = new DisplayableRecycleAdapter(this.displayableItems, typeOfDisplay);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(displayableRecycleAdapter);

    }

    private <T extends Displayable> void addItemsToDisplayableItems(List<T> displayableItems) {
        for (Displayable item : displayableItems) {
            if (!itemInList(item)) {
                this.displayableItems.add(item);
            }
        }
    }

    private boolean itemInList(Displayable item) {
        for (Displayable displayable : this.displayableItems) {
            if (item.getName().equals(displayable.getName()) &&
                    item.getImageResource().equals(displayable.getImageResource())) {
                return true;
            }
        }
        return false;
    }

    public DisplayableRecycleAdapter getDisplayableRecycleAdapter() {
        return displayableRecycleAdapter;
    }

    public List<? extends Displayable> getDisplayableItems() {
        return displayableItems;
    }

    public <T extends Displayable> void refreshDisplay() {
        recyclerView = view.findViewById(R.id.categories);
        recyclerView.removeAllViews();
        this.displayableItems.clear();
    }
}
