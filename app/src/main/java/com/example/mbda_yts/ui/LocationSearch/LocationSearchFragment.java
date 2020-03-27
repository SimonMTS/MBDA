package com.example.mbda_yts.ui.LocationSearch;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
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
import com.example.mbda_yts.SearchableFragment;
import com.example.mbda_yts.YTVideo;

public class LocationSearchFragment extends Fragment implements SearchableFragment {

    private LocationSearchViewModel locationSearchViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        locationSearchViewModel = ViewModelProviders.of(this).get(LocationSearchViewModel.class);
        View root = inflater.inflate(R.layout.fragment_location_search, container, false);
        final TextView textView = root.findViewById(R.id.text_tools);

        final String fLocString = locationSearchViewModel.getUserLocation(this);
        locationSearchViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(fLocString);
            }
        });
        return root;
    }

    @Override
    public void onSearch(View view) {
        locationSearchViewModel.loadVideos(this);
    }

    public void onClick(YTVideo video) {

        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + video.video_id));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + video.video_id));
        try {
            getActivity().startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            getActivity().startActivity(webIntent);
        }

    }

}