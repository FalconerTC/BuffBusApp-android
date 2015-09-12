package com.cherish.bustracker.activity.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cherish.bustracker.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class BusSelectorFragment extends Fragment {

    public BusSelectorFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bus_selector, container, false);
    }
}
