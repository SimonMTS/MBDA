package com.example.mbda_yts.ui.LocationSearch;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.mbda_yts.R;

public class LocationSearchFragment extends Fragment {

    private LocationSearchViewModel locationSearchViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        locationSearchViewModel =
                ViewModelProviders.of(this).get(LocationSearchViewModel.class);
        View root = inflater.inflate(R.layout.fragment_location_search, container, false);
        final TextView textView = root.findViewById(R.id.text_tools);
        locationSearchViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}