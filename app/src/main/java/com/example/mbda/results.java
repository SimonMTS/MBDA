package com.example.mbda;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;

public class results extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        ArrayList<String> messageArray = getIntent().getExtras().getStringArrayList("list");

        TextView textView = findViewById(R.id.textView);
        textView.setText(messageArray.get(messageArray.size()-1));
    }
}
