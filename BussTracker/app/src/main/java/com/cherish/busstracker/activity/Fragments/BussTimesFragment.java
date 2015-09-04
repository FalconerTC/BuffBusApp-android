package app.com.activity.Fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import app.com.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class BussTimesFragment extends Fragment {

    public BussTimesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_buss_times, container, false);
    }
}
