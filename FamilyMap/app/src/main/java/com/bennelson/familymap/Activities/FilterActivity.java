package com.bennelson.familymap.Activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.bennelson.familymap.Model_Classes.FamilyMapModel;
import com.bennelson.familymap.R;
import com.bennelson.familymap.Utilities.FamilyMapTools;

/**
 * Created by BenNelson on 12/8/16.
 */

public class FilterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setTitle(R.string.filter_act_title);
        setContentView(R.layout.activity_filter);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        FamilyMapTools.getInstance().menuSwitches(this, item);
        return true;
    }

}
