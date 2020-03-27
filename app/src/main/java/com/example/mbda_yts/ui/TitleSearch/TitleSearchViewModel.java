package com.example.mbda_yts.ui.TitleSearch;

import android.content.SharedPreferences;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mbda_yts.API;
import com.example.mbda_yts.MainActivity;
import com.example.mbda_yts.YTVideo;

import org.json.JSONArray;

import java.util.LinkedHashSet;

public class TitleSearchViewModel extends ViewModel {

    public String SharedPreferencesKey = "search_history3";
    LinkedHashSet<YTVideo> searchHistory = new LinkedHashSet<>();

    public void loadVideos( Fragment fragment, String message ) {

        if (!message.equals("")) {

            updateSearchHistory(fragment, message);

            API.NEXT_PAGE_TOKEN = "";

            String URL = "https://www.googleapis.com/youtube/v3/search/?part=snippet&q=" + message + "&type=video&maxResults=15&key=" + API.API_KEY;
            API.getVideos(URL, (MainActivity) fragment.getActivity(), null);

        }

    }

    private void updateSearchHistory( Fragment fragment, String message ) {

        searchHistory.remove(new YTVideo("", message, ""));
        searchHistory.add(new YTVideo("", message, ""));

        SharedPreferences prefs = fragment.getActivity().getSharedPreferences(SharedPreferencesKey, fragment.getActivity().MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        JSONArray json = new JSONArray();
        for (YTVideo video : searchHistory) {
            json.put(video.video_title);
        }

        editor.putString(SharedPreferencesKey, json.toString());
        editor.commit();

    }

}