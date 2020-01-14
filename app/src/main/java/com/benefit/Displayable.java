package com.benefit;

/**
 * Represents a UI component of an item: the rounded rectangle with a photo and title.
 * Used in the RecyclerView as the object of the CardView.
 */
public interface Displayable {

    String getImageResource();

    String getName();

}
