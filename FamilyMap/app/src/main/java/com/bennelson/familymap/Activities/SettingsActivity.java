package com.bennelson.familymap.Activities;

import android.content.ClipData;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;

import com.bennelson.familymap.Fragments.LoginFragment;
import com.bennelson.familymap.Model_Classes.Event;
import com.bennelson.familymap.Model_Classes.FamilyMapModel;
import com.bennelson.familymap.Model_Classes.Person;
import com.bennelson.familymap.R;
import com.bennelson.familymap.Utilities.FamilyMapTools;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 *
 * Created by BenNelson on 12/5/16.
 */

public class SettingsActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.settings_act_title);
        setContentView(R.layout.activity_settings);

        //UP BUTTON
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        onLogoutClicked(this);

        onResyncClicked(this);

        setSpinners();

        onSpinnersSelected();

        onSwitchesChecked(this);

    }

    private void onSwitchesChecked(final SettingsActivity settingsActivity)
    {
        //Click on Person listener
        View view = this.getWindow().getDecorView().getRootView();
        assert view != null;

        final Switch famSwitch = (Switch) view.findViewById(R.id.switch_family);

        if(FamilyMapModel.getInstance().isDrawFamilyLines()) {
            boolean checked = true;
            famSwitch.setChecked(checked);
        }

        famSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    FamilyMapModel.getInstance().setDrawFamilyLines(true);
                }
                else {
                    FamilyMapModel.getInstance().setDrawFamilyLines(false);
                }
            }
        });

        final Switch lifeSwitch = (Switch) view.findViewById(R.id.swith_life);

        if(FamilyMapModel.getInstance().isDrawEventLines()) {
            boolean checked = true;
            lifeSwitch.setChecked(checked);
        }

        lifeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    FamilyMapModel.getInstance().setDrawEventLines(true);
                }
                else {
                    FamilyMapModel.getInstance().setDrawEventLines(false);
                }
            }
        });

        final Switch spouseSwitch = (Switch) view.findViewById(R.id.switch_spouse);

        if(FamilyMapModel.getInstance().isDrawSpouseLines()) {
            boolean checked = true;
            spouseSwitch.setChecked(checked);
        }

        spouseSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    FamilyMapModel.getInstance().setDrawSpouseLines(true);
                }
                else {
                    FamilyMapModel.getInstance().setDrawSpouseLines(false);
                }
            }
        });
    }

    public void setSpinners() {

        //LINE SPINNNERS
        Spinner famSpinner = (Spinner) findViewById(R.id.spinner_family);
        Spinner spouseSpinner = (Spinner) findViewById(R.id.spinner_spouse);
        Spinner lifeSpinner = (Spinner) findViewById(R.id.spinner_life);

        ArrayAdapter<CharSequence> adapter =
                ArrayAdapter.createFromResource(this, R.array.color_array, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        famSpinner.setAdapter(adapter);
        spouseSpinner.setAdapter(adapter);
        lifeSpinner.setAdapter(adapter);

        //MAPS SPINNER
        Spinner mapSpinner = (Spinner) findViewById(R.id.map_type_spinner);

        ArrayAdapter<CharSequence> mapAdapter =
                ArrayAdapter.createFromResource(this, R.array.maps_array, android.R.layout.simple_spinner_item);

        mapAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mapSpinner.setAdapter(mapAdapter);


    }

    private void onSpinnersSelected()
    {
        onLifeSpinnerSelected();
        onFamilySpinnerSelected();
        onSpouseSpinnerSelected();
        onMapSpinnerSelected();

    }

    private void onMapSpinnerSelected() {

        Spinner mapSpinner = (Spinner) findViewById(R.id.map_type_spinner);

        mapSpinner.setSelection(FamilyMapTools.getInstance().getCurrentMapSelection());

        mapSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String mapType = (String) parent.getItemAtPosition(position);
                int mapID = FamilyMapTools.getInstance().getCorrectMapType(mapType);
                FamilyMapModel.getInstance().setGoogleMapType(mapID);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void onLifeSpinnerSelected() {

        Spinner lifeSpinner = (Spinner) findViewById(R.id.spinner_life);

        lifeSpinner.setSelection(FamilyMapTools.getInstance().getCurrentColorSelection("l"));

        lifeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // An item was selected. You can retrieve the selected item using
                // parent.getItemAtPosition(pos)
                String color = (String) parent.getItemAtPosition(position);
                int newColor = FamilyMapTools.getInstance().getCorrectColor(color);
                FamilyMapModel.getInstance().setEventLinesColor(newColor);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void onFamilySpinnerSelected() {

        Spinner famSpinner = (Spinner) findViewById(R.id.spinner_family);

        famSpinner.setSelection(FamilyMapTools.getInstance().getCurrentColorSelection("f"));

        famSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // An item was selected. You can retrieve the selected item using
                // parent.getItemAtPosition(pos)
                String color = (String) parent.getItemAtPosition(position);
                int newColor = FamilyMapTools.getInstance().getCorrectColor(color);
                FamilyMapModel.getInstance().setFamilyLinesColor(newColor);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void onSpouseSpinnerSelected()
    {
        Spinner spouseSpinner = (Spinner) findViewById(R.id.spinner_spouse);

        spouseSpinner.setSelection(FamilyMapTools.getInstance().getCurrentColorSelection("s"));

        spouseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // An item was selected. You can retrieve the selected item using
                // parent.getItemAtPosition(pos)
                String color = (String) parent.getItemAtPosition(position);
                int newColor = FamilyMapTools.getInstance().getCorrectColor(color);
                FamilyMapModel.getInstance().setSpouseLinesColor(newColor);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void onLogoutClicked(final SettingsActivity settingsActivity)
    {
        //Click on Person listener
        View view = this.getWindow().getDecorView().getRootView();
        assert view != null;

        final Button button = (Button) view.findViewById(R.id.button_logout);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FamilyMapModel fmm = FamilyMapModel.getInstance();
                fmm.resetAllData();

                Intent intent = new Intent(settingsActivity, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();

            }
        });

    }

    private void onResyncClicked(final SettingsActivity settingsActivity)
    {
        //Click on Person listener
        View view = this.getWindow().getDecorView().getRootView();
        assert view != null;

        final Button button = (Button) view.findViewById(R.id.button_resync);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Clearcurent DATA

                FamilyMapModel fmm = FamilyMapModel.getInstance();
                fmm.resetAllData();

                Intent intent = new Intent(settingsActivity, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                FamilyMapModel.getInstance().setResync(true);
                startActivity(intent);
                finish();

            }
        });
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
