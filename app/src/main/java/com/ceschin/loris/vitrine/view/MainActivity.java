package com.ceschin.loris.vitrine.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.ceschin.loris.vitrine.R;
import com.ceschin.loris.vitrine.model.User;

public class MainActivity extends AppCompatActivity {

    private String mToken;
    private User mUser;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment =  null;

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fragment = new FavoritesFragment();
                    break;
                case R.id.navigation_search:
                    fragment =  new FavoritesFragment();
                    break;
                case R.id.navigation_camera:
                    // TODO: START THE CAMERA
                    fragment = new FavoritesFragment();
                    break;
                case R.id.navigation_favorites:
                    fragment = new FavoritesFragment();
                    break;
                case R.id.navigation_profile:
                    fragment = new FavoritesFragment();
                    break;
            }

            return loadFragment(fragment);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // Getting the token from login activity
        mToken = getIntent().getStringExtra("token");

        // Getting the user
        mUser = getIntent().getExtras().getParcelable("user");

        // loading the default fragment
        loadFragment(new FavoritesFragment());
    }


    private boolean loadFragment(Fragment fragment) {
        if(fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();

            return true;
        }

        return false;
    }

    public String getToken() {
        return mToken;
    }
    public User getUser() {
        return mUser;
    }
}
