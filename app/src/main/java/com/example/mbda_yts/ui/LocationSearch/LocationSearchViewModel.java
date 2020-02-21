package com.example.mbda_yts.ui.LocationSearch;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

public class LocationSearchViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public LocationSearchViewModel() {

        mText = new MutableLiveData<>();
        mText.setValue("<location>");
    }

    public LiveData<String> getText() {
        return mText;
    }
}