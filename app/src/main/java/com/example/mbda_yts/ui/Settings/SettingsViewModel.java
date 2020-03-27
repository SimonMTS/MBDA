package com.example.mbda_yts.ui.Settings;

import androidx.fragment.app.Fragment;
import android.content.SharedPreferences;

import androidx.lifecycle.ViewModel;


public class SettingsViewModel extends ViewModel {

    public void updateApiKey(String key, Fragment fragment) {
        System.out.println(key);

        SharedPreferences prefs = fragment.getActivity().getSharedPreferences("API_KEY", fragment.getActivity().MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putString("API_KEY", key);
        editor.commit();

    }

}
