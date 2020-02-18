package com.example.mbda;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.LinkedHashSet;

public class MainActivity extends AppCompatActivity {

    public static final LinkedHashSet<String> EXTRA_SEARCH_TEXT = new LinkedHashSet<>();

    RecyclerView recyclerView;
    SearchResultAdapter adapter;
    ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar bar = findViewById(R.id.toolbar);
        setSupportActionBar(bar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawer, bar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toggle.setDrawerIndicatorEnabled(true);
        toggle.syncState();
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NavigationView nv = findViewById(R.id.nav_view);
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int id = item.getItemId();



                return true;

            }
        });

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(toggle.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }

}
