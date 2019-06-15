package com.bennelson.familymap.Activities;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.bennelson.familymap.Fragments.MapFragment;
import com.bennelson.familymap.Model_Classes.FamilyMapModel;
import com.bennelson.familymap.R;
import com.bennelson.familymap.Utilities.FamilyMapTools;
import com.google.android.gms.maps.GoogleMap;

import com.joanzapata.android.iconify.IconDrawable;
import com.joanzapata.android.iconify.Iconify;

/**
 * Created by BenNelson on 12/7/16.
 */

public class MapActivity extends AppCompatActivity
{

    private GoogleMap myMap;
    private String incomingEventID = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setTitle(R.string.map_act_title);
        setContentView(R.layout.activity_map);


        //Toolbar myToolbar = (Toolbar) findViewById(R.id.go_to_top_item);
        //setSupportActionBar(myToolbar);
        //UP BUTTON
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setIcon(new IconDrawable(this, Iconify.IconValue.fa_angle_double_up));


        Intent intent = getIntent();
        getIntentInfo(intent);

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.map_fragment);

        if(fragment == null)
        {

            fragment = new MapFragment();

            //Set the arguments for the fragment
            Bundle bundleArgs = new Bundle();
            bundleArgs.putBoolean("menuOptions", false);
            bundleArgs.putBoolean("comingFromMapActivity", true);
            //bundleArgs.putBoolean("dataGenerated", true);
            FamilyMapModel.getInstance().setDataGenerated(true);
            if(incomingEventID != null)
            {
                bundleArgs.putString("eventID", incomingEventID);
            }

            fragment.setArguments(bundleArgs);

            fragmentManager.beginTransaction().add(R.id.map_fragment, fragment).commit();
        }

    }

    private void getIntentInfo(Intent intent)
    {
        if(intent.hasExtra("eventID"))
        {
            incomingEventID = intent.getStringExtra("eventID");
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.go_to_top_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        FamilyMapTools.getInstance().menuSwitches(this, item);
        return true;
    }
}
