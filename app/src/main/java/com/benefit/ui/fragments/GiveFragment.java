package com.benefit.ui.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.benefit.R;
import com.benefit.model.Product;
import com.benefit.model.User;

public class GiveFragment extends Fragment {

    public static GiveFragment newInstance(User user, Product productToEdit) {
        GiveFragment fragment = new GiveFragment();
        fragment.mUser = user;
        fragment.mProductToEdit = productToEdit;
        return fragment;
    }

    private Product mProductToEdit;
    private User mUser;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.give_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

}
