package com.bennelson.familymap.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;

import com.bennelson.familymap.Model_Classes.Event;
import com.bennelson.familymap.Model_Classes.FamilyMapModel;
import com.bennelson.familymap.Model_Classes.FamilyMember;
import com.bennelson.familymap.Model_Classes.Person;
import com.bennelson.familymap.R;
import com.bennelson.familymap.Utilities.FamilyMapTools;
import com.bennelson.familymap.Utilities.PersonListAdapter;
import com.bennelson.familymap.Utilities.SearchHolder;
import com.bennelson.familymap.Utilities.SearchListAdapter;

import java.util.ArrayList;

/**
 * Created by BenNelson on 12/8/16.
 */

public class SearchActivity extends AppCompatActivity
{
    private RecyclerView myRecyclerView;
    private ExpandableListView expListView;
    private ArrayList<FamilyMember> lifeEvents;
    private ArrayList<FamilyMember> family;
    SearchActivity searchActivity = this;
    //private MyItemAdapter myItemAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setTitle(R.string.search_act_title);
        //setContentView(R.layout.activity_search);
        setContentView(R.layout.activity_search_expv);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        View view = this.getWindow().getDecorView().getRootView();
        assert view != null;

        Button searchButton = (Button) view.findViewById(R.id.mysearch_button_expv);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //search();

                //NOT A SEARCH ALGORITHM...just displaying a random person....SEARCH NOT IMPLEMENTED
                ArrayList<Person> persons = FamilyMapModel.getInstance().getPersons();
                Person p = persons.get(0);
                String personID = p.getPersonID();
                String firstName = p.getFirstName();
                String lastName = p.getLastName();
                String spouse = p.getSpouse();
                String father = p.getFather();
                String mother = p.getMother();
                final ArrayList<Event> sortedEvents = FamilyMapTools.getInstance().getCurrentPersonsEvents(personID);
                lifeEvents = FamilyMapTools.getInstance()
                        .changeSortedEventsToFamilyMemberObjects(sortedEvents,firstName,lastName,personID);

                family = FamilyMapTools.getInstance().getCurrentPersonsFamily(personID,spouse,father,mother);


                ExpandableListAdapter adapter = new SearchListAdapter(getBaseContext(),lifeEvents,family);
                expListView = (ExpandableListView) findViewById(R.id.search_expv);
                expListView.setAdapter(adapter);

                //Set them to start expanded
                expListView.expandGroup(0);
                expListView.expandGroup(1);

                onItemsClicked();
            }
        });

        //onItemsClicked();
    }

    private void onItemsClicked()
    {
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
                    Intent intent = new Intent(searchActivity, MapActivity.class);
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

                    Intent intent = new Intent(searchActivity, PersonActivity.class);
                    FamilyMapTools.getInstance().passIntentExtrasToPersonActivity(intent,person);

                    startActivity(intent);
                }

                return false;
            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        FamilyMapTools.getInstance().menuSwitches(this, item);
        return true;
    }

    public void search()
    {
        FamilyMapModel fmm = FamilyMapModel.getInstance();
    }

}
