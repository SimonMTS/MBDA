package com.example.mbda_yts.ui.LocationSearch;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
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

import com.example.mbda_yts.MainActivity;
import com.example.mbda_yts.R;

import java.io.IOException;
import java.util.List;

public class LocationSearchFragment extends Fragment {

    private LocationSearchViewModel locationSearchViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        locationSearchViewModel =
                ViewModelProviders.of(this).get(LocationSearchViewModel.class);
        View root = inflater.inflate(R.layout.fragment_location_search, container, false);
        final TextView textView = root.findViewById(R.id.text_tools);

        ((MainActivity)this.getActivity()).onLocationSearchLoad();

        Location location = ((MainActivity)this.getActivity()).Location;

        String locString = "No location data available";
        if (location != null) {

            Geocoder gcd = new Geocoder(this.getActivity());
            try {
                List<Address> addresses = gcd.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                if (addresses.size() > 0) {

                    locString = addresses.get(0).getCountryName() +", "+ addresses.get(0).getAdminArea() +", "+ addresses.get(0).getLocality();


                }
            } catch (IOException e) {
            }
        }

        final String fLocString = locString;
        locationSearchViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(fLocString);
            }
        });
        return root;
    }
}