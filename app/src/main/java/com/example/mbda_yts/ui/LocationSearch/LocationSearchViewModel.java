package com.example.mbda_yts.ui.LocationSearch;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mbda_yts.API;
import com.example.mbda_yts.MainActivity;

import java.io.IOException;
import java.util.List;

public class LocationSearchViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private static final int MY_PERMISSION_ACCESS_FINE_LOCATION = 12;
    public Location Location = null;

    public LocationSearchViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("<location>");
    }

    public LiveData<String> getText() {
        return mText;
    }

    public String getUserLocation( Fragment fragment ) {

        if (!requestUserPermission(fragment)) return "Location permission denied.";

        LocationManager locationManager = (LocationManager) fragment.getActivity().getSystemService(fragment.getActivity().LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String provider = locationManager.getBestProvider(criteria, false);

        if (ActivityCompat.checkSelfPermission(fragment.getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(fragment.getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return "Location permission denied.";
        }
        Location location = locationManager.getLastKnownLocation(provider);

        if (location != null) {
            Location = location;

            Geocoder gcd = new Geocoder(fragment.getActivity());
            try {
                List<Address> addresses = gcd.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                if (addresses.size() > 0) {
                    return addresses.get(0).getCountryName() +", "+ addresses.get(0).getAdminArea() +", "+ addresses.get(0).getLocality();
                } else {
                    return "Location not found.";
                }
            } catch (IOException e) {
                return "Location parsing error.";
            }
        } else {
            return "Location not found.";
        }

    }

    public boolean requestUserPermission( Fragment fragment ) {

        LocationManager service = (LocationManager) fragment.getActivity().getSystemService(fragment.getActivity().LOCATION_SERVICE);
        boolean enabled = service.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (!enabled) {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            fragment.getActivity().startActivity(intent);
        }

        if (ContextCompat.checkSelfPermission(fragment.getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    fragment.getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    MY_PERMISSION_ACCESS_FINE_LOCATION
            );
        } else {
            return true;
        }

        return false;

    }

    public void loadVideos( Fragment fragment ) {

        if (Location != null) {

            API.NEXT_PAGE_TOKEN = "";

            String URL = "https://www.googleapis.com/youtube/v3/search/?part=snippet&type=video&maxResults=15&key=" + API.getApiKey(fragment.getActivity()) + "&locationRadius=10km&location=" + Location.getLatitude() + "," + Location.getLongitude();
            API.getVideos(URL, (MainActivity) fragment.getActivity(), null);

        }

    }
}