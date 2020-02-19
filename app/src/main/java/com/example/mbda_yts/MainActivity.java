package com.example.mbda_yts;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.provider.SyncStateContract;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mbda_yts.ui.MyResultRecyclerViewAdapter;
import com.example.mbda_yts.ui.ResultFragment;
import com.example.mbda_yts.ui.dummy.DummyContent;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.JsonObject;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements ResultFragment.OnListFragmentInteractionListener {

    private AppBarConfiguration mAppBarConfiguration;

//    private String API_KEY = "AIzaSyDkRzjbJaolgw9IzF7ao-jQw4H3BzpO9pM";
//    private String API_KEY = "AIzaSyAKehrK5DM0wciSam-XTJv9WIjK8svx1yk";
    private String API_KEY = "AIzaSyBD2zXC-GlNj35r5Qz6R2IbhutHrjQmvVk";
    private static String NEXT_PAGE_TOKEN = "";

    private String SharedPreferencesKey = "search_history3";

    LinkedHashSet<YTVideo> searchHistory = new LinkedHashSet<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_slideshow, R.id.nav_tools)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void onSearch(View view){
        EditText editText = findViewById(R.id.editText);
        String message = editText.getText().toString();

        if (!message.equals("")) {

            searchHistory.remove(new YTVideo("", message, ""));
            searchHistory.add(new YTVideo("", message, ""));

            SharedPreferences prefs = getSharedPreferences(SharedPreferencesKey, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();

            JSONArray json = new JSONArray();
            for (YTVideo video : searchHistory) {
                json.put(video.video_title);
            }

            editor.putString(SharedPreferencesKey, json.toString());
            editor.commit();

            NEXT_PAGE_TOKEN = "";
            getVideosByTitle(message,this, null);

        }

        if (this.getCurrentFocus() != null) {

            InputMethodManager inputMethodManager = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);

        }

    }

    @Override
    public void onListFragmentInteraction(YTVideo item) {

    }

    public void onTitleSearchLoad() {

        SharedPreferences prefs = getSharedPreferences(SharedPreferencesKey, Context.MODE_PRIVATE);
        try {
            JSONArray jsonArray = new JSONArray(prefs.getString(SharedPreferencesKey, "[]"));
            searchHistory.clear();
            for (int i = 0; i < jsonArray.length(); i++) {
                searchHistory.add(new YTVideo("", jsonArray.get(i).toString(), ""));
            }
        } catch (Exception e) {}

        getSupportFragmentManager()
            .beginTransaction()
            .replace(R.id.below_search, new ResultFragment(searchHistory, "", null))
            .commit();

    }

    public static void getVideosByTitle(final String searchText, final MainActivity activity, final RecyclerView rv) {

        String URL = "https://www.googleapis.com/youtube/v3/search/?part=snippet&q=" + searchText + "&type=video&maxResults=15&key=" + activity.API_KEY;

        if (!NEXT_PAGE_TOKEN.equals("")) {
            URL += "&pageToken=" + NEXT_PAGE_TOKEN;
        }

        final LinkedHashSet<YTVideo> result = new LinkedHashSet<>();

        RequestQueue queue = Volley.newRequestQueue(activity);
        StringRequest request = new StringRequest(com.android.volley.Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray jsonArray=jsonObject.getJSONArray("items");

                    if ( jsonObject.getString("nextPageToken").equals(NEXT_PAGE_TOKEN) ) return;

                    for (int i=0;i<jsonArray.length();i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        JSONObject jsonVideoId=jsonObject1.getJSONObject("id");
                        String videoId=jsonVideoId.getString("videoId");

                        JSONObject jsonsnippet= jsonObject1.getJSONObject("snippet");
                        String videoTitle=jsonsnippet.getString("title");

                        JSONObject imageObject = jsonsnippet.getJSONObject("thumbnails").getJSONObject("high");
                        String imageUrl=imageObject.getString("url");

                        result.add(new YTVideo(videoId, videoTitle, imageUrl));
                    }

                    if (NEXT_PAGE_TOKEN.equals("")) {

                        ArrayList<YTVideo> result_as_list = new ArrayList<>(result);
                        Collections.reverse(result_as_list);
                        LinkedHashSet<YTVideo>final_result = new LinkedHashSet<>(result_as_list);

                        activity.getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.below_search, new ResultFragment(final_result, searchText, activity))
                            .commit();

                    } else {

                        ((MyResultRecyclerViewAdapter)rv.getAdapter()).addItems(result);

                    }

                    NEXT_PAGE_TOKEN = jsonObject.getString("nextPageToken");

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error",error.toString());
            }
        });
        queue.add(request);


//        return result;
    }
}
