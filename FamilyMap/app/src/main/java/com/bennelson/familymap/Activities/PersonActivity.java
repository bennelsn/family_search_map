package com.bennelson.familymap.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bennelson.familymap.Model_Classes.Event;
import com.bennelson.familymap.Model_Classes.FamilyMapModel;
import com.bennelson.familymap.Model_Classes.FamilyMember;
import com.bennelson.familymap.Model_Classes.Person;
import com.bennelson.familymap.R;
import com.bennelson.familymap.Utilities.FamilyMapTools;
import com.bennelson.familymap.Utilities.PersonListAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by BenNelson on 12/5/16.
 */

public class PersonActivity extends AppCompatActivity
{
    ExpandableListView expListView;
    PersonActivity personActivity = this;

    private String personID;
    private String firstName;
    private String lastName;
    private String father;
    private String mother;
    private String gender;
    private String spouse;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.person_act_title);
        setContentView(R.layout.activity_person);

        //UP BUTTON
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();

        getIntentExtras(intent);
        setCurrentPersonToView();


        final ArrayList<Event> sortedEvents = FamilyMapTools.getInstance().getCurrentPersonsEvents(personID);
        final ArrayList<FamilyMember> lifeEvents = FamilyMapTools.getInstance()
                .changeSortedEventsToFamilyMemberObjects(sortedEvents,firstName,lastName,personID);

        final ArrayList<FamilyMember> family = FamilyMapTools.getInstance().getCurrentPersonsFamily(personID,spouse,father,mother);

        ExpandableListAdapter adapter = new PersonListAdapter(this,lifeEvents,family);
        expListView = (ExpandableListView) findViewById(R.id.life_events);
        expListView.setAdapter(adapter);

        //Set them to start expanded
        expListView.expandGroup(0);
        expListView.expandGroup(1);

        //expListView.setOnChildClickListener();
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id)
            {
                //LIFE EVENTS will be groupPosition 0 .... so use lifeEvents arrayList
                if(groupPosition == 0)
                {
                    //Youve got the event and personID
                    FamilyMember fMember = lifeEvents.get(childPosition);
                    Intent intent = new Intent(personActivity, MapActivity.class);
                    //intent.putExtra();

                    //PASS THE EVENT ONWARD
                    Event e = fMember.getEvent();
                    String eventID  = e.getEventID();
                    intent.putExtra("eventID",eventID);

                    startActivity(intent);

                }
                //FAMILY will be groupPosition 1 .... so use family arraylist
                if(groupPosition == 1)
                {
                    FamilyMember fMember = family.get(childPosition);
                    Person person = fMember.getPerson();

                    Intent intent = new Intent(personActivity, PersonActivity.class);
                    FamilyMapTools.getInstance().passIntentExtrasToPersonActivity(intent,person);

                    startActivity(intent);
                }

                return false;
            }
        });




    }


    private void setCurrentPersonToView()
    {
        View view = getWindow().getDecorView().getRootView();
        final TextView firstname = (TextView) view.findViewById(R.id.firstName);
        firstname.setText(firstName);

        final TextView lastname = (TextView) view.findViewById(R.id.lastName);
        lastname.setText(lastName);

        gender = gender.toLowerCase();
        String g = "g";
        if(gender.equals("m"));
        {
            g = "Gender: Male";
        }
        if(gender.equals("f"))
        {
            g = "Gender: Female";
        }
        final TextView gender = (TextView) view.findViewById(R.id.gender);
        gender.setText(g);



    }

    private void getIntentExtras(Intent intent)
    {
        personID = intent.getStringExtra("personID");
        firstName = intent.getStringExtra("firstName");
        lastName =intent.getStringExtra("lastName");
        gender = intent.getStringExtra("gender");
        spouse = intent.getStringExtra("spouse");
        father = intent.getStringExtra("father");
        mother = intent.getStringExtra("mother");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.go_to_top_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        FamilyMapTools.getInstance().menuSwitches(this, item);
        return true;
    }


}
