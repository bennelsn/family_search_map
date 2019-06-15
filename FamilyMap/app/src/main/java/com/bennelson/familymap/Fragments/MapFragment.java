package com.bennelson.familymap.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bennelson.familymap.Activities.FilterActivity;
import com.bennelson.familymap.Activities.MainActivity;
import com.bennelson.familymap.Activities.PersonActivity;
import com.bennelson.familymap.Activities.SearchActivity;
import com.bennelson.familymap.Activities.SettingsActivity;
import com.bennelson.familymap.Model_Classes.Event;
import com.bennelson.familymap.Model_Classes.FamilyLines;
import com.bennelson.familymap.Model_Classes.FamilyMapModel;
import com.bennelson.familymap.Model_Classes.FamilyMember;
import com.bennelson.familymap.Model_Classes.Person;
import com.bennelson.familymap.R;
import com.bennelson.familymap.Utilities.FamilyMapTools;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 *
 * * Created by BenNelson on 12/1/16.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback
{

    //GOOGLE API KEY : AIzaSyAYyzn9isfVVuymvlQ7rX2GSaqWXijLcP8
    //   com.google.android.geo.API_KEY
    private GoogleMap myMap;
    private boolean menuOptions = true;
    private boolean comingFromMapActivity = false;
    //private boolean dataGenerated = false;
    private ArrayList<Polyline> currentLines = new ArrayList<>();
    private Person currentPerson;
    private String incomingEventID = null;

    public static MapFragment newInstance()
    {

         return new MapFragment();
    }

    //This tells my map fragment that we want a menu bar
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();

        if(args != null) {
            menuOptions = args.getBoolean("menuOptions", menuOptions);
            comingFromMapActivity = args.getBoolean("comingFromMapActivity", comingFromMapActivity);
            //dataGenerated = args.getBoolean("dataGenerated", dataGenerated);
            incomingEventID = args.getString("eventID");

        }

        setHasOptionsMenu(menuOptions);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState)
    {

        View view = inflater.inflate(R.layout.map_frag, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        return view;

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        myMap = googleMap;

        // Add a marker in Sydney, Australia, and move the camera.
        //LatLng sydney = new LatLng(-34, 151);
        //myMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        //myMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        //if(!dataGenerated) {
        if(!FamilyMapModel.getInstance().isDataGenerated())
        {
            //Generate events
            FamilyMapTools.getInstance().generateEvents();
            //Generate Marker colors
            FamilyMapTools.getInstance().generateEventColors();
            //Generate persons
            FamilyMapModel.getInstance().generatePersons();

            FamilyMapModel.getInstance().setDataGenerated(true);
        }


        ArrayList<Event> events = FamilyMapModel.getInstance().getEvents();

        addMarkers(events);

        onMarkerClicked();

        onPersonClicked();


        myMap.setMapType(FamilyMapModel.getInstance().getGoogleMapType());

        if(incomingEventID != null)
        {
            Event incomingEvent = getCorrectEvent(incomingEventID);
            zoomInOnEvent(incomingEvent);
            changeEventInformation(incomingEvent);
            clearPolyLines();
            addPolyLines(incomingEvent);
        }
    }


    private void zoomInOnEvent(Event incomingEvent)
    {
        double lat = incomingEvent.getLatitude();
        double lon = incomingEvent.getLongitude();
        LatLng markerPos = new LatLng(lat,lon);

        /* GOOGLE ZOOM VALUES
        1: World
        5: Landmass/continent
        10: City
        15: Streets
        20: Buildings */

        myMap.moveCamera(CameraUpdateFactory.newLatLng(markerPos));
        // Zoom in, animating the camera.
        myMap.animateCamera(CameraUpdateFactory.zoomIn());
        // Zoom out to zoom level 10, animating with a duration of 4 seconds.
        myMap.animateCamera(CameraUpdateFactory.zoomTo(10), 4000, null);
    }

    private Event getCorrectEvent(String incomingEventID) {
        ArrayList<Event> allEvents = FamilyMapModel.getInstance().getEvents();
        Event correctEvent = new Event();
        for(Event event : allEvents)
        {
            if(event.getEventID().equals(incomingEventID))
            {
                //we found the event
                correctEvent = event;
                break;
            }
        }
        return correctEvent;
    }

    private void onPersonClicked() {

        //Click on Person listener
        View view = this.getView();
        assert view != null;

        final LinearLayout icon = (LinearLayout) view.findViewById(R.id.personInfo);
        icon.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                if(currentPerson != null) {

                    //Transition to Person Activity
                    Intent intent = new Intent(getActivity(), PersonActivity.class);


                    FamilyMapTools.getInstance().passIntentExtrasToPersonActivity(intent, currentPerson);
                    startActivity(intent);
                }


            }
        });
    }

    private void onMarkerClicked() {
        //Marker listener
        myMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                // Change text
                LatLng currentMarkerPosition = marker.getPosition();

                Map<LatLng, Event> markerMap = FamilyMapModel.getInstance().getMarkerMap();
                if(markerMap.containsKey(currentMarkerPosition))
                {

                    Event currentEvent = markerMap.get(currentMarkerPosition);
                    changeEventInformation(currentEvent);
                    //myMap.animateCamera(CameraUpdateFactory.newLatLng(currentMarkerPosition)); to move
                    clearPolyLines();
                    addPolyLines(currentEvent);

                }
                return false;
            }
        });

    }


    private void changeEventInformation(final Event currentEvent)
    {
        //Get the name of the person associated with the event
        View view = this.getView();
        assert view != null;
        currentPerson = getCorrectPerson(currentEvent);

        //Change Icon
        changeIcon(currentPerson, view);

        //Change name
        changeName(currentPerson, view);

        //Change event
        changeEvent(currentEvent, view);

        //Change Location
        changeLocation(currentEvent,view);

    }

    private void changeLocation(Event currentEvent, View view)
    {
        final TextView event = (TextView) view.findViewById(R.id.location);
        String location = currentEvent.getCity() + ", " + currentEvent.getCountry() + " (" +
                currentEvent.getYear() + ")";
        event.setText(location);
    }

    private void changeEvent(Event currentEvent, View view)
    {
        final TextView event = (TextView) view.findViewById(R.id.event);
        String typeOfEvent = currentEvent.getDescription() + ":";
        event.setText(typeOfEvent);
    }

    private void changeName(Person person, View view)
    {
        final TextView name = (TextView) view.findViewById(R.id.name);
        String nameOfPerson = person.getFirstName() + " " + person.getLastName();
        name.setText(nameOfPerson);
    }

    private void changeIcon(Person person, View view)
    {
        final ImageView icon = (ImageView) view.findViewById(R.id.genderIcon);
        String gender = person.getGender();
        gender = gender.toLowerCase();
        if(gender.equals("m"))
        {
            icon.setImageResource(R.drawable.boy);
        }
        else
        {
            icon.setImageResource(R.drawable.girl);
        }
    }

    private Person getCorrectPerson(Event currentEvent)
    {
        String personID = currentEvent.getPersonID();
        ArrayList<Person> persons = FamilyMapModel.getInstance().getPersons();
        Person person = new Person();

        for(int i = 0; i < persons.size(); i++)
        {
            person = persons.get(i);
            //If we find our person
            if(personID.equals(person.getPersonID()))
            {
                break; // We found our person
            }

        }

        return person;
    }

    public void addMarkers(ArrayList<Event> events)
    {
        for(Event event : events)
        {
            float color = FamilyMapTools.getInstance().colorPicker(event);
            LatLng location = new LatLng(event.getLatitude(),event.getLongitude());
            myMap.addMarker(new MarkerOptions().position(location)
                    .icon(BitmapDescriptorFactory.defaultMarker(color)));

            FamilyMapModel.getInstance().addToMarkerMap(location,event);
        }



    }

    private void addPolyLines(Event eventSelected)
    {
        Person personForEventSelected = getCorrectPerson(eventSelected);
        //Draw Spouse Lines
        if(FamilyMapModel.getInstance().isDrawSpouseLines())
        {
            Map<LatLng, LatLng> spouseLines = FamilyMapTools.getInstance().getSpousePolyLines(personForEventSelected, eventSelected);
            int color = FamilyMapModel.getInstance().getSpouseLinesColor();
            drawLines(spouseLines, color);

        }

        //Draw Family Lines
        if(FamilyMapModel.getInstance().isDrawFamilyLines())
        {
            ArrayList<FamilyLines> familyLines = new ArrayList<>();
            familyLines = FamilyMapTools.getInstance().generateFamilyPolyLines(personForEventSelected, eventSelected, familyLines);
            int color = FamilyMapModel.getInstance().getFamilyLinesColor();

            int generations = FamilyMapTools.getInstance().getHowManyGenerations();
            double defaultPolyWidth = 10;
            double widthFactor = defaultPolyWidth/generations;

            drawLines(familyLines, color, widthFactor, generations);

            //ONCE we draw the lines we need to set the generation counts back to default
            FamilyMapTools.getInstance().resetGenCounts();

        }

        //Draw Event Lines
        if(FamilyMapModel.getInstance().isDrawEventLines())
        {
            Map<LatLng, LatLng> eventLines = FamilyMapTools.getInstance().getEventPolyLines(personForEventSelected);
            int color = FamilyMapModel.getInstance().getEventLinesColor();
            drawLines(eventLines, color);
        }

    }
    private void clearPolyLines()
    {
        for(Polyline p : currentLines)
        {
            p.remove();
        }
        currentLines.clear();
    }

    private void drawLines(ArrayList<FamilyLines> lines, int color, double widthFactor, int generations)
    {

        for (FamilyLines fl : lines) {
            LatLng pointA = fl.getPointA();
            LatLng pointB = fl.getPointB();

            double width = (generations - fl.getGenCount()) * widthFactor;

            Polyline line = myMap.addPolyline(new PolylineOptions().add(pointA,pointB).color(color).width((float)width));
            currentLines.add(line);
        }
    }
    private void drawLines(Map<LatLng, LatLng> lines, int color)
    {

        for (Map.Entry<LatLng, LatLng> entry : lines.entrySet()) {
            LatLng pointA = entry.getKey();
            LatLng pointB = entry.getValue();

            Polyline line = myMap.addPolyline(new PolylineOptions().add(pointA,pointB).color(color));
            currentLines.add(line);
        }
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.settings_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch(item.getItemId()) {
            case R.id.searchMenuSettings:
                intent = new Intent(getActivity(), SettingsActivity.class);
                startActivity(intent);
                break;
            case R.id.searchMenuItem:
                intent = new Intent(getActivity(), SearchActivity.class);
                startActivity(intent);
                break;
            case R.id.searchMenuFilter:
                intent = new Intent(getActivity(), FilterActivity.class);
                startActivity(intent);
                break;
            case android.R.id.home:
                getActivity().finish();
                break;

            default:
        }
        return true;

    }
    //onResume OVERRIDE
}
