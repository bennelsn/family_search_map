package com.bennelson.familymap.Activities;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


import com.bennelson.familymap.Fragments.LoginFragment;
import com.bennelson.familymap.Fragments.MapFragment;
import com.bennelson.familymap.R;
import com.bennelson.familymap.Utilities.FamilyMapTools;

public class MainActivity extends AppCompatActivity {


    private boolean fromTop = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();

        if(intent.hasExtra("fromTop"))
        {
            switchToMapFragment();
        }
        else {

            FragmentManager fragmentManager = getSupportFragmentManager();
            Fragment fragment = fragmentManager.findFragmentById(R.id.login_fragment);


            if (fragment == null) {
                fragment = new LoginFragment();
                fragmentManager.beginTransaction().add(R.id.login_fragment, fragment).commit();
            }
        }

    }

    public void switchToMapFragment()
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = new MapFragment();
        fragmentManager.beginTransaction().replace(R.id.login_fragment, fragment).commit();
    }
}
