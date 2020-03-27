package com.example.mbda_yts;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import android.view.View;

import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.mbda_yts.ui.ResultFragment;
import com.example.mbda_yts.ui.TitleSearch.TitleSearchFragment;
import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.util.List;

public class MainActivity extends AppCompatActivity implements ResultFragment.OnListFragmentInteractionListener {

    private AppBarConfiguration mAppBarConfiguration;

    public String titleFromExternalIntent = "";

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

        Bundle extras = getIntent().getExtras();
        if ( extras != null && extras.containsKey("fromYoutube") ) {
            String title = extras.getString("fromYoutube");

            titleFromExternalIntent = title;

            navController.navigate(R.id.action_nav_home_to_title);
        }

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

    @Override
    public void onListFragmentInteraction(YTVideo item) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        Fragment currentFragment = getCurrentFragment();

        FragmentTransaction ft = currentFragment.getFragmentManager().beginTransaction();
        ft.detach(currentFragment);
        ft.attach(currentFragment);
        ft.commit();
    }


    public void onSearch(View view) {
        ((SearchableFragment)getCurrentFragment()).onSearch(view);

        if (this.getCurrentFocus() != null) {

            InputMethodManager inputMethodManager = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);

        }
    }

    public void onClick(YTVideo video) {
        ((SearchableFragment)getCurrentFragment()).onClick(video);

        Fragment currentFragment = getCurrentFragment();
        FragmentTransaction ft = currentFragment.getFragmentManager().beginTransaction();
        ft.detach(currentFragment);
        ft.attach(currentFragment);
        ft.commit();
    }

    public Fragment getCurrentFragment() {
        int count = this.getSupportFragmentManager().getBackStackEntryCount();
        Fragment frag = this.getSupportFragmentManager().getFragments().get(count>0?count-1:count);
        return frag.getChildFragmentManager().getFragments().get(0);
    }

}
