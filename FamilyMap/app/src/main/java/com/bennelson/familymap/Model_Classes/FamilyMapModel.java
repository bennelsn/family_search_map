package com.bennelson.familymap.Model_Classes;

import android.graphics.Color;
import android.widget.Switch;

import com.bennelson.familymap.Utilities.FamilyMapTools;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by BenNelson on 11/28/16.
 */
public class FamilyMapModel {

    private static final int defaultValue = 0;
    private boolean dataGenerated = false;
    private String username;
    private String password;
    private String host;
    private String port;
    private String personId;
    private String currentAToken;
    private JSONObject familyData;
    private JSONObject eventsData;
    private ArrayList<Event> events;
    private ArrayList<Person> persons = new ArrayList<>();
    private Set<String> allTypesOfEvents;
    private Map<String, Double> eventColors;
    private Map<LatLng, Event> markerMap = new HashMap<>();
    private boolean drawFamilyLines = true;
    private boolean drawSpouseLines = true;
    private boolean drawEventLines = true;
    private int familyLinesColor = Color.RED; //DEFAULT
    private int spouseLinesColor = Color.BLUE;// default
    private int eventLinesColor = Color.GREEN; // DEFAULT
    private int fatherGenCount = defaultValue;
    private int motherGenCount = defaultValue;
    private int googleMapType = GoogleMap.MAP_TYPE_NORMAL;
    private boolean resync = false;




    private static FamilyMapModel instance = null;


    protected FamilyMapModel()
    {
        //exists only to defeat instantiation
    }
    public static FamilyMapModel getInstance()
    {
        if(instance == null)
        {
            instance = new FamilyMapModel();
        }
        return instance;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getHost() {
        return host;
    }

    public String getPort() {
        return port;
    }

    public String getCurrentAToken() {
        return currentAToken;
    }

    public void setCurrentAToken(String currentAToken) {
        this.currentAToken = currentAToken;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public JSONObject getFamilyData() {
        return familyData;
    }

    public void setFamilyData(JSONObject familyData) {
        this.familyData = familyData;
        int x = 0;
    }

    public JSONObject getEventsData() {
        return eventsData;
    }

    public void setEventsData(JSONObject eventsData) {
        this.eventsData = eventsData;
    }

    public ArrayList<Event> getEvents() {
        return events;
    }

    public void setEvents(ArrayList<Event> events) {
        this.events = events;
    }

    public void setAllTypesOfEvents(Set<String> allTypesOfEvents)
    {
        this.allTypesOfEvents = allTypesOfEvents;
    }

    public Set<String> getAllTypesOfEvents() {
        return allTypesOfEvents;
    }

    public Map<String, Double> getEventColors() {
        return eventColors;
    }

    public void setEventColors(Map<String, Double> eventColors) {
        this.eventColors = eventColors;
    }

    public void addToMarkerMap(LatLng location, Event event)
    {
        markerMap.put(location,event);
    }

    public Map<LatLng, Event> getMarkerMap() {
        return markerMap;
    }

    public void setMarkerMap(Map<LatLng, Event> markerMap) {
        this.markerMap = markerMap;
    }

    public boolean isDrawFamilyLines() {
        return drawFamilyLines;
    }

    public void setDrawFamilyLines(boolean drawFamilyLines) {
        this.drawFamilyLines = drawFamilyLines;
    }

    public boolean isDrawSpouseLines() {
        return drawSpouseLines;
    }

    public void setDrawSpouseLines(boolean drawSpouseLines) {
        this.drawSpouseLines = drawSpouseLines;
    }

    public boolean isDrawEventLines() {
        return drawEventLines;
    }

    public void setDrawEventLines(boolean drawEventLines) {
        this.drawEventLines = drawEventLines;
    }

    public int getEventLinesColor() {
        return eventLinesColor;
    }

    public void setEventLinesColor(int eventLinesColor) {
        this.eventLinesColor = eventLinesColor;
    }

    public int getFamilyLinesColor() {
        return familyLinesColor;
    }

    public void setFamilyLinesColor(int familyLinesColor) {
        this.familyLinesColor = familyLinesColor;
    }

    public int getSpouseLinesColor() {
        return spouseLinesColor;
    }

    public void setSpouseLinesColor(int spouseLinesColor) {
        this.spouseLinesColor = spouseLinesColor;
    }

    public int getMotherGenCount() {
        return motherGenCount;
    }

    public void setMotherGenCount(int motherGenCount) {
        this.motherGenCount = motherGenCount;
    }

    public int getFatherGenCount() {
        return fatherGenCount;
    }

    public void setFatherGenCount(int fatherGenCount) {
        this.fatherGenCount = fatherGenCount;
    }

    public static int getDefaultValue() {
        return defaultValue;
    }

    public boolean isResync() {
        return resync;
    }

    public void setResync(boolean resync) {
        this.resync = resync;
    }

    public boolean isDataGenerated() {
        return dataGenerated;
    }

    public void setDataGenerated(boolean dataGenerated) {
        this.dataGenerated = dataGenerated;
    }

    public int getGoogleMapType() {
        return googleMapType;
    }

    public void setGoogleMapType(int googleMapType) {
        this.googleMapType = googleMapType;
    }


    public void generatePersons()
    {
        try {
            JSONArray persons = familyData.getJSONArray("data");

            for(int i = 0; i < persons.length(); i++)
            {
                JSONObject person = persons.getJSONObject(i);
                Person p = new Person();

                p.setDescendant(person.getString("descendant"));
                p.setPersonID(person.getString("personID"));
                p.setFirstName(person.getString("firstName"));
                p.setLastName(person.getString("lastName"));
                p.setGender(person.getString("gender"));

                if(person.has("spouse"))
                {
                    p.setSpouse(person.getString("spouse"));
                }
                if(person.has("father"))
                {
                    p.setFather(person.getString("father"));
                }

                if(person.has("mother"))
                {
                    p.setMother(person.getString("mother"));
                }

                this.persons.add(p);
            }

        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

    }

    public ArrayList<Person> getPersons() {
        return persons;
    }

    public void resetAllData()
    {
        //Defaults for the app
        dataGenerated = false;
        events.clear();
        persons.clear();
        allTypesOfEvents.clear();
        eventColors.clear();
        markerMap.clear();
        drawFamilyLines = true;
        drawEventLines = true;
        drawSpouseLines = true;
        familyLinesColor = Color.RED;
        eventLinesColor = Color.GREEN;
        spouseLinesColor = Color.BLUE;
        fatherGenCount = defaultValue;
        motherGenCount = defaultValue;
        googleMapType = GoogleMap.MAP_TYPE_NORMAL;

    }
}
