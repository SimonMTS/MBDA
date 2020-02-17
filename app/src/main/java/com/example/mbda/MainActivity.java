package com.example.mbda;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.LinkedHashSet;

public class MainActivity extends AppCompatActivity {

    public static final LinkedHashSet<String> EXTRA_SEARCH_TEXT = new LinkedHashSet<>();

    RecyclerView recyclerView;
    SearchResultAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        recyclerView = findViewById(R.id.recycler_view);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);

        recyclerView.setLayoutManager(mLayoutManager);

        adapter = new SearchResultAdapter(EXTRA_SEARCH_TEXT,getApplicationContext());
        recyclerView.setAdapter(adapter);
    }

    public void onSearch(View view) {
        Intent intent = new Intent(this, results.class);
        EditText editText = findViewById(R.id.editText);
        String message = editText.getText().toString();

        EXTRA_SEARCH_TEXT.remove(message);
        EXTRA_SEARCH_TEXT.add(message);
        intent.putStringArrayListExtra("list", new ArrayList<>(EXTRA_SEARCH_TEXT));
        adapter.notifyDataSetChanged();

        startActivity(intent);
    }

}
