package com.example.mbda_yts;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class ExposedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        String url = extras.getString(Intent.EXTRA_TEXT);
        String id = url.replaceAll("https://youtu.be/", "");

        API.getVideo("https://www.googleapis.com/youtube/v3/videos?part=snippet&id=" + id + "&key=" + API.getApiKey(this), this);
    }
}
