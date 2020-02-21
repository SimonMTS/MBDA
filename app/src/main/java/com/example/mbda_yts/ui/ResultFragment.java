package com.example.mbda_yts.ui;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mbda_yts.MainActivity;
import com.example.mbda_yts.R;
import com.example.mbda_yts.YTVideo;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class ResultFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;

    boolean loading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;

    private List<YTVideo> History = new ArrayList<>();
    private String URL;
    private MainActivity MainActivity;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ResultFragment() {
    }

    public ResultFragment(LinkedHashSet<YTVideo> history, String url, MainActivity mainactivity) {
        History.addAll(0, history);
        URL = url;
        MainActivity = mainactivity;
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static ResultFragment newInstance(int columnCount) {
        ResultFragment fragment = new ResultFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_result_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                LinearLayoutManager lm = new LinearLayoutManager(context);
                lm.setReverseLayout(true);
                lm.setStackFromEnd(true);

                recyclerView.setLayoutManager(lm);
            } else {
                LinearLayoutManager lm = new GridLayoutManager(context, mColumnCount);
                lm.setReverseLayout(true);
                lm.setStackFromEnd(true);

                recyclerView.setLayoutManager(lm);
            }
            recyclerView.setAdapter(new MyResultRecyclerViewAdapter(History, mListener));


            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener()
            {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy)
                {
                    if (MainActivity != null && dy > 0) //check for scroll down
                    {
                        visibleItemCount = recyclerView.getLayoutManager().getChildCount();
                        totalItemCount = recyclerView.getLayoutManager().getItemCount();
                        pastVisiblesItems = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();

                        if (loading)
                        {
//                            if ( (visibleItemCount + pastVisiblesItems) >= totalItemCount)
                            if ( pastVisiblesItems < 10)
                            {
                                loading = false;

                                MainActivity.getVideos(URL, MainActivity, recyclerView);

                                loading = true;
                            }
                        }
                    }
                }
            });
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(YTVideo item);
    }
}
