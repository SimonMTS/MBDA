package com.example.mbda_yts.ui.TitleSearch;

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

public class TitleSearchFragment extends Fragment {

    private TitleSearchViewModel titleSearchViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        titleSearchViewModel =
                ViewModelProviders.of(this).get(TitleSearchViewModel.class);
        View root = inflater.inflate(R.layout.fragment_title_search, container, false);

        ((MainActivity)this.getActivity()).onTitleSearchLoad();

        return root;
    }
}