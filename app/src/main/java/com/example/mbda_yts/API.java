package com.example.mbda_yts;

import android.text.Html;
import android.util.Log;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mbda_yts.ui.ResultRecyclerViewAdapter;
import com.example.mbda_yts.ui.ResultFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;

public class API {

 // public static String API_KEY = "AIzaSyDkRzjbJaolgw9IzF7ao-jQw4H3BzpO9pM";
 // public static String API_KEY = "AIzaSyAKehrK5DM0wciSam-XTJv9WIjK8svx1yk";
    public static String API_KEY = "AIzaSyBD2zXC-GlNj35r5Qz6R2IbhutHrjQmvVk";

    public static String NEXT_PAGE_TOKEN = "";

    public static void getVideos(String url, final MainActivity activity, final RecyclerView rv) {

        if (!NEXT_PAGE_TOKEN.equals("")) {
            url += "&pageToken=" + NEXT_PAGE_TOKEN;
        }

        final String URL = url;
        final LinkedHashSet<YTVideo> result = new LinkedHashSet<>();

        RequestQueue queue = Volley.newRequestQueue(activity);
        StringRequest request = new StringRequest(com.android.volley.Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("items");

                    if (jsonObject.getString("nextPageToken").equals(NEXT_PAGE_TOKEN)) return;

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        JSONObject jsonVideoId = jsonObject1.getJSONObject("id");
                        String videoId = jsonVideoId.getString("videoId");

                        JSONObject jsonsnippet = jsonObject1.getJSONObject("snippet");
                        String videoTitle = jsonsnippet.getString("title");

                        JSONObject imageObject = jsonsnippet.getJSONObject("thumbnails").getJSONObject("high");
                        String imageUrl = imageObject.getString("url");

                        result.add(new YTVideo(videoId, Html.fromHtml(videoTitle).toString(), imageUrl));
                    }

                    if (NEXT_PAGE_TOKEN.equals("")) {

                        ArrayList<YTVideo> result_as_list = new ArrayList<>(result);
                        Collections.reverse(result_as_list);
                        LinkedHashSet<YTVideo> final_result = new LinkedHashSet<>(result_as_list);

                        activity.getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.below_search, new ResultFragment(final_result, URL, activity))
                                .commit();

                    } else {

                        ((ResultRecyclerViewAdapter) rv.getAdapter()).addItems(result);

                    }

                    NEXT_PAGE_TOKEN = jsonObject.getString("nextPageToken");

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error", error.toString());
            }
        });
        queue.add(request);

    }

}
