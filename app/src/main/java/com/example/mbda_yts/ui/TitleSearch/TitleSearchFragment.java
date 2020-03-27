package com.example.mbda_yts.ui.TitleSearch;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.example.mbda_yts.MainActivity;
import com.example.mbda_yts.R;
import com.example.mbda_yts.SearchableFragment;
import com.example.mbda_yts.YTVideo;
import com.example.mbda_yts.ui.ResultFragment;

import org.json.JSONArray;

public class TitleSearchFragment extends Fragment implements SearchableFragment {

    private TitleSearchViewModel titleSearchViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        titleSearchViewModel = ViewModelProviders.of(this).get(TitleSearchViewModel.class);
        View root = inflater.inflate(R.layout.fragment_title_search, container, false);

        SharedPreferences prefs = getActivity().getSharedPreferences(titleSearchViewModel.SharedPreferencesKey, Context.MODE_PRIVATE);
        try {
            JSONArray jsonArray = new JSONArray(prefs.getString(titleSearchViewModel.SharedPreferencesKey, "[]"));
            titleSearchViewModel.searchHistory.clear();
            for (int i = 0; i < jsonArray.length(); i++) {
                titleSearchViewModel.searchHistory.add(new YTVideo("", jsonArray.get(i).toString(), ""));
            }
        } catch (Exception e) {
        }

        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.below_search, new ResultFragment(titleSearchViewModel.searchHistory, "", null))
                .commit();

        return root;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {

        if (!((MainActivity)getActivity()).titleFromExternalIntent.equals("")) {
            EditText editText = ((MainActivity)getActivity()).findViewById(R.id.editText);
            editText.setText( ((MainActivity)getActivity()).titleFromExternalIntent );
            titleSearchViewModel.updateSearchHistory(this, ((MainActivity)getActivity()).titleFromExternalIntent);

            ((MainActivity)getActivity()).titleFromExternalIntent = "";

            Fragment currentFragment = ((MainActivity)getActivity()).getCurrentFragment();
            FragmentTransaction ft = currentFragment.getFragmentManager().beginTransaction();
            ft.detach(currentFragment);
            ft.attach(currentFragment);
            ft.commit();
        }

    }

    @Override
    public void onSearch(View view) {
        EditText editText = this.getActivity().findViewById(R.id.editText);
        titleSearchViewModel.updateSearchHistory(this, editText.getText().toString());

        titleSearchViewModel.loadVideos(this, editText.getText().toString());
    }

    public void onClick(YTVideo video) {
        if (video.video_id.equals("") && video.video_image_url.equals("") && !video.video_title.equals("")) {

            EditText editText = this.getActivity().findViewById(R.id.editText);
            editText.setText( video.video_title );

            titleSearchViewModel.updateSearchHistory(this, video.video_title);

        } else {

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

}