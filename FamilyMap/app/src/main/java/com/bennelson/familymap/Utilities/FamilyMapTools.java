package com.bennelson.familymap.Utilities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.bennelson.familymap.Activities.MainActivity;
import com.bennelson.familymap.Model_Classes.Event;
import com.bennelson.familymap.Model_Classes.FamilyLines;
import com.bennelson.familymap.Model_Classes.FamilyMapModel;
import com.bennelson.familymap.Model_Classes.FamilyMember;
import com.bennelson.familymap.Model_Classes.Person;
import com.bennelson.familymap.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Created by BenNelson on 11/29/16.
 */
public class FamilyMapTools
{
    private static FamilyMapTools instance = null;
    protected FamilyMapTools()
    {
        //exists only to defeat instantiation
    }

    public static FamilyMapTools getInstance()
    {
        if(instance == null)
        {
            instance = new FamilyMapTools();
        }
        return instance;
    }

    public void generateEvents()
    {
        FamilyMapModel fmm = FamilyMapModel.getInstance();
        JSONObject eventsData = fmm.getEventsData();
        ArrayList<Event> generatedEvents = new ArrayList<>();
        Set<String> typesOfEvents = new TreeSet<>();
        try {
            JSONArray events = eventsData.getJSONArray("data");

            for(int i = 0; i < events.length(); i++)
            {
                JSONObject event = events.getJSONObject(i);
                Event e = new Event();

                e.setEventID(event.getString("eventID"));
                e.setPersonID(event.getString("personID"));
                e.setLatitude(event.getDouble("latitude"));
                e.setLongitude(event.getDouble("longitude"));
                e.setCountry(event.getString("country"));
                e.setCity(event.getString("city"));
                e.setDescription(event.getString("description"));
                if(event.has("year"))
                {
                    e.setYear(event.getString("year"));
                }
                e.setDescendant(event.getString("descendant"));


                //Make sure all events are changed to lowercase just to make sure that case doesn't matter
                String typeOfEvent = event.getString("description");
                typeOfEvent = typeOfEvent.toLowerCase();


                typesOfEvents.add(typeOfEvent);
                generatedEvents.add(e);
            }


        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }


        FamilyMapModel.getInstance().setAllTypesOfEvents(typesOfEvents);
        FamilyMapModel.getInstance().setEvents(generatedEvents);
    }

    public String buildUserPasswordString(String username, String password)
    {
        /*
        We need to build a string looking like this:
                    {
	                    username:"username",
	                    password:"password"
                    }
         */

        StringBuilder sb = new StringBuilder("{ \"username\" : \"");
        sb.append(username);
        sb.append("\" , \"password\" : \"");
        sb.append(password);
        sb.append("\" }");

        return sb.toString();


    }

    public String buildURLUserLogin(String host, String port)
    {
        //http://HOST:PORT/user/login
        StringBuilder sb = new StringBuilder("http://");
        sb.append(host);
        sb.append(":");
        sb.append(port);
        sb.append("/user/login");

        return sb.toString();
    }

    public String buildURLPersonId(String host, String port, String personID)
    {
        //http://HOST:PORT/person/personID
        StringBuilder sb = new StringBuilder("http://");
        sb.append(host);
        sb.append(":");
        sb.append(port);
        sb.append("/person/");
        sb.append(personID);

        return sb.toString();
    }

    public String buildURLPerson(String host, String port)
    {
        //http://HOST:PORT/person/
        StringBuilder sb = new StringBuilder("http://");
        sb.append(host);
        sb.append(":");
        sb.append(port);
        sb.append("/person/");

        return sb.toString();
    }

    public String buildURLEvent(String host, String port)
    {
        //http://HOST:PORT/person/
        StringBuilder sb = new StringBuilder("http://");
        sb.append(host);
        sb.append(":");
        sb.append(port);
        sb.append("/event/");

        return sb.toString();
    }

    public void generateEventColors()
    {
        Set<String> allTypesOfEvents = FamilyMapModel.getInstance().getAllTypesOfEvents();

        double hue = 360.0;
        int numberOfEvents = allTypesOfEvents.size();
        double baseColor = hue/numberOfEvents;
        int eventIndex = 1;
        Map<String, Double> eventColors = new TreeMap<>();

        for(String eventType : allTypesOfEvents)
        {
            //assign it a color relative to size
            double color = (baseColor * eventIndex);
            if(color >= 360.0)
            {
                color = 359.99;
            }
            eventColors.put(eventType, color);
            eventIndex++;
        }

        FamilyMapModel.getInstance().setEventColors(eventColors);
    }

    public float colorPicker(Event event)
    {
        String typeOfEvent = event.getDescription();
        typeOfEvent = typeOfEvent.toLowerCase();
        float finalColor = (float) 0.0;

        if(FamilyMapModel.getInstance().getEventColors().containsKey(typeOfEvent))
        {
            double color = FamilyMapModel.getInstance().getEventColors().get(typeOfEvent);
            finalColor = (float) color;
        }

        return finalColor;
    }


    public void passIntentExtrasToPersonActivity(Intent intent, Person person)
    {
        //Pass the person
        intent.putExtra("personID", person.getPersonID());
        intent.putExtra("firstName",person.getFirstName());
        intent.putExtra("lastName",person.getLastName());
        intent.putExtra("gender",person.getGender());
        intent.putExtra("spouse",person.getSpouse());
        intent.putExtra("father",person.getFather());
        intent.putExtra("mother",person.getMother());

    }

    public ArrayList<FamilyMember> changeSortedEventsToFamilyMemberObjects(ArrayList<Event> sortedEvents,String firstName,String lastName, String personID) {
        ArrayList<FamilyMember> lifeEvents = new ArrayList<>();

        for(Event e : sortedEvents)
        {
            FamilyMember fMember = new FamilyMember();
            fMember.setEvent(e);
            fMember.setEvent(true);

            Person p = new Person();
            p.setFirstName(firstName);
            p.setLastName(lastName);
            p.setPersonID(personID);

            fMember.setPerson(p);

            lifeEvents.add(fMember);
        }

        return lifeEvents;
    }

    public ArrayList<Event> getCurrentPersonsEvents(String personID) {


            ArrayList<Event> currentPersonsEvents = new ArrayList<>();
            FamilyMapModel fmm = FamilyMapModel.getInstance();
            ArrayList<Event> allEvents = fmm.getEvents();

            Event deathEvent = null;
            ArrayList<Event> eventsWithoutYears = new ArrayList<>();
            ArrayList<Event> eventsWithYears = new ArrayList<>();
            for(Event e : allEvents)
            {
                if(e.getPersonID().equals(personID))
                {
                    //we found and event associated with the current person
                /*
                A person’s life events are ordered chronologically as follows:
                1. Birth events, if present, are always first (whether they have a year or not)
                2. Events with years, sorted primarily by year, and secondarily by description
                normalized to lower-case
                3. Events without years sorted by description normalized to lower-case
                4. Death events, if present, are always last (whether they have a year or not)
                */

                    String typeOfEvent = e.getDescription();
                    typeOfEvent = typeOfEvent.toLowerCase();
                    String year = e.getYear();

                    //Birth
                    if(typeOfEvent.equals("birth"))
                    {
                        //We found our birth
                        currentPersonsEvents.add(0,e);
                        continue;
                    }
                    if(typeOfEvent.equals("death"))
                    {
                        //We found our death, so let's store it.
                        deathEvent = e;
                        continue;

                    }
                    if(year.equals("none"))
                    {
                        eventsWithoutYears.add(e);
                    }
                    else
                    {
                        eventsWithYears.add(e);
                    }


                }
            }

            addEventsWithYearsCorrectly(currentPersonsEvents, eventsWithYears);
            addEventsWithoutYearsCorrectly(currentPersonsEvents, eventsWithoutYears);

            //add our death to the very end if there is one
            if(deathEvent != null) {
                currentPersonsEvents.add(deathEvent);
            }


            return currentPersonsEvents;


    }

    private void addEventsWithoutYearsCorrectly(ArrayList<Event> currentPersonsEvents, ArrayList<Event> eventsWithoutYears)
    {

        ArrayList<Event> sortedEvents = sortedEvents(eventsWithoutYears);
        for(Event e : sortedEvents)
        {
            currentPersonsEvents.add(e);
        }
    }

    private void addEventsWithYearsCorrectly(ArrayList<Event> currentPersonsEvents, ArrayList<Event> eventsWithYears)
    {
        Map<String, ArrayList<Event>> eventsMap = new HashMap<>();
        Set<Integer> sortedYears = new TreeSet<>();

        for(Event event : eventsWithYears)
        {
            String year = event.getYear();
            if(eventsMap.containsKey(year))
            {
                ArrayList<Event> dummy = eventsMap.get(year);
                dummy.add(event);

                dummy = sortedEvents(dummy);

                eventsMap.put(year, dummy);
            }
            else
            {
                ArrayList<Event> events = new ArrayList<>();
                events.add(event);
                eventsMap.put(year, events);
            }
            sortedYears.add(Integer.parseInt(year));

        }

        for(int y : sortedYears)
        {
            String year = Integer.toString(y);
            if(eventsMap.containsKey(year))
            {
                ArrayList<Event> eventList = eventsMap.get(year);
                for(Event e : eventList)
                {
                    currentPersonsEvents.add(e);
                }
            }
        }

    }

    private ArrayList<Event> sortedEvents(ArrayList<Event> list)
    {
        Set<String> sortedDescriptions = new TreeSet<>();
        Map<String, Event> eventsMap = new HashMap<>();

        for(Event e : list)
        {
            String description =  e.getDescription();
            description = description.toLowerCase();
            sortedDescriptions.add(description);

            eventsMap.put(description, e);
        }
        list.clear();

        for(String description : sortedDescriptions)
        {
            Event e = eventsMap.get(description);
            list.add(e);
        }

        return list;
    }


    public ArrayList<FamilyMember> getCurrentPersonsFamily(String personID, String spouse, String father, String mother)
    {
        ArrayList<FamilyMember> currentPersonsFamily = new ArrayList<>();

        FamilyMapModel fmm = FamilyMapModel.getInstance();
        ArrayList<Person> allPersons = fmm.getPersons();

        for(Person p : allPersons)
        {
            //Check the spouse of p
            if(p.getPersonID().equals(spouse))
            {
                //We found the spouse!
                currentPersonsFamily.add(makeAFamilyMember("Spouse", p));
                continue;
            }
            if(p.getFather().equals(personID) || p.getMother().equals(personID))
            {
                //We found a child!
                String child = "child";
                String childGender = p.getGender();
                childGender = childGender.toLowerCase();

                if(childGender.equals("m"))
                {
                    child = "Son";
                }
                else
                {
                    child = "Daughter";
                }

                currentPersonsFamily.add(makeAFamilyMember(child, p));
                continue;
            }
            if(p.getPersonID().equals(father))
            {

                currentPersonsFamily.add(makeAFamilyMember("Father", p));
                continue;
            }
            if(p.getPersonID().equals(mother))
            {
                //We found the mother!
                currentPersonsFamily.add(makeAFamilyMember("Mother",p));

            }


        }
        return currentPersonsFamily;
    }

    private FamilyMember makeAFamilyMember(String relationship, Person p)
    {
        FamilyMember fMember = new FamilyMember();
        fMember.setPerson(p);
        fMember.setRelationship(relationship);
        fMember.setFamilyMember(true);
        return fMember;
    }

    public Map<LatLng,LatLng> getSpousePolyLines(Person currentPerson, Event eventSelected)
    {
        Map<LatLng,LatLng> spouseLines = new HashMap<>();
        //FIND THE SPOUSE
        Person spouse = FamilyMapTools.getInstance().getCurrentPersonsSpouse(currentPerson.getPersonID());

        //These events are sorted chronologically for the spouse....we want to use the first one in the list if any
        ArrayList<Event> sortedEvents = FamilyMapTools.getInstance()
                .getCurrentPersonsEvents(spouse.getPersonID());

        if(!sortedEvents.isEmpty())
        {
            //We should get the first event in the list only
            int earliestEvent = 0;
            Event spouseEarilestEvent = sortedEvents.get(earliestEvent);

            //Get coordinates for both
            //PointA
            LatLng pointA = new LatLng(eventSelected.getLatitude(),eventSelected.getLongitude());

            //PointB
            LatLng pointB = new LatLng(spouseEarilestEvent.getLatitude(),spouseEarilestEvent.getLongitude());

            spouseLines.put(pointA,pointB);

        }

        return spouseLines;
    }

    private Person getCurrentPersonsSpouse(String currentPersonID)
    {
        ArrayList<Person> allPersons = FamilyMapModel.getInstance().getPersons();
        Person spouse = new Person();
        for(Person p : allPersons)
        {
            if(p.getSpouse().equals(currentPersonID))
            {
                spouse = p;
            }
        }
        return spouse;
    }

    public ArrayList<FamilyLines> generateFamilyPolyLines(Person currentPerson, Event eventSelected, ArrayList<FamilyLines> familyLines)
    {

        /*
        Lines linking the selected event to the person’s ancestors
        (i.e., the person associated with the selected event) are drawn as follows:
         A line is drawn between the selected event and the birth event of the selected person’s father.
        */

        //THIS IS ONE GENERATION
        String fatherID = currentPerson.getFather();
        ArrayList<Event> fatherEvents = getCurrentPersonsEvents(fatherID);

        boolean isFather = addParentLinePoints(fatherEvents, eventSelected, familyLines, "father");




        String motherID = currentPerson.getMother();
        ArrayList<Event> motherEvents = getCurrentPersonsEvents(motherID);


        boolean isMother = addParentLinePoints(motherEvents, eventSelected, familyLines, "mother");


        if(isFather)
        {
            Person father = getCurrentPersonsFather(currentPerson);
            familyLines = generateFamilyPolyLines(father, fatherEvents.get(0), familyLines);
        }


        if(isMother)
        {
            Person mother = getCurrentPersonsMother(currentPerson);
            familyLines = generateFamilyPolyLines(mother, motherEvents.get(0), familyLines);
        }



        /*
         Lines are drawn recursively from parents’ birth events to grandparents’ birth
        events, from grandparents’ birth events to great grandparents’ birth events, etc. including all available generations.
        As lines are drawn recursively up the family tree, they should become progressively and noticeably thinner.
        *
        * */


        return familyLines;
    }



    private boolean addParentLinePoints(ArrayList<Event> parentEvents, Event eventSelected, ArrayList<FamilyLines> familyLines, String parent)
    {
        if(!parentEvents.isEmpty())
        {
            //if the father has events, get the earliest one which is first on the list at index 0
            int earliest = 0;
            Event earliestParentEvent = parentEvents.get(earliest);

            //Get pointA to pointB
            LatLng pointA = new LatLng(eventSelected.getLatitude(),eventSelected.getLongitude());
            LatLng pointB = new LatLng(earliestParentEvent.getLatitude(), earliestParentEvent.getLongitude());

            FamilyLines fl = new FamilyLines(pointA,pointB);


            if(parent.equals("father"))
            {
                FamilyMapModel fmm = FamilyMapModel.getInstance();
                fl.setGenCount(fmm.getFatherGenCount());

                int genCount = fmm.getFatherGenCount();
                genCount++;
                fmm.setFatherGenCount(genCount);


            }
            else
            {
                FamilyMapModel fmm = FamilyMapModel.getInstance();
                fl.setGenCount(fmm.getMotherGenCount());

                int genCount = fmm.getMotherGenCount();
                genCount++;
                fmm.setMotherGenCount(genCount);
            }


            familyLines.add(fl);
            return true;
        }
        else
        {
            return false;
        }
    }

    public Map<LatLng,LatLng> getEventPolyLines(Person currentPerson)
    {
        Map<LatLng,LatLng> eventLines = new HashMap<>();

        //These events are sorted chronologically
        ArrayList<Event> sortedEvents = FamilyMapTools.getInstance()
                .getCurrentPersonsEvents(currentPerson.getPersonID());

        //Now make pairs from one event to the next in the list
        LatLng pointA;
        LatLng pointB;
        for(int i = 0; i < sortedEvents.size(); i++)
        {
            if(i == sortedEvents.size() - 1)
            {
                //We would be on the last event, so it wont have a point B, so we can break from the loop
                break;
            }
            else {
                Event eventA = sortedEvents.get(i);
                pointA = new LatLng(eventA.getLatitude(), eventA.getLongitude());

                Event eventB = sortedEvents.get(i + 1);
                pointB = new LatLng(eventB.getLatitude(), eventB.getLongitude());

                eventLines.put(pointA,pointB);
            }

        }

        return eventLines;
    }

    private Person getCurrentPersonsMother(Person currentPerson)
    {
        Person currentPersonsMother = new Person();

        ArrayList<Person> allPersons = FamilyMapModel.getInstance().getPersons();

        for(Person p : allPersons)
        {
            if(currentPerson.getMother().equals(p.getPersonID()))
            {
                //we found a father
                currentPersonsMother = p;
                return currentPersonsMother;
            }
        }

        return currentPersonsMother;
    }

    private Person getCurrentPersonsFather(Person person)
    {
        Person currentPersonsFather = new Person();

        ArrayList<Person> allPersons = FamilyMapModel.getInstance().getPersons();

        for(Person p : allPersons)
        {
            if(person.getFather().equals(p.getPersonID()))
            {
                //we found a father
                currentPersonsFather = p;
                return currentPersonsFather;
            }
        }

        return currentPersonsFather;
    }

    public int getHowManyGenerations()
    {
        int generations = 0;

        FamilyMapModel fmm = FamilyMapModel.getInstance();
        int fatherGens = fmm.getFatherGenCount();
        int motherGens = fmm.getMotherGenCount();

        if(fatherGens >= motherGens)
        {
            generations = fatherGens;
        }
        else
        {
            generations = motherGens;
        }

        return generations;
    }

    public void resetGenCounts() {
        FamilyMapModel fmm = FamilyMapModel.getInstance();
        fmm.getDefaultValue();
        fmm.setFatherGenCount(fmm.getDefaultValue());
        fmm.setMotherGenCount(fmm.getDefaultValue());
    }

    public void menuSwitches(Activity activity, MenuItem item)
    {
        switch(item.getItemId())
        {
            case android.R.id.home:
                activity.finish();
                break;
            case R.id.go_to_top_item:
                Intent intent = new Intent(activity, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("fromTop", true);
                activity.startActivity(intent);
                activity.finish();
            default:
        }
    }

    public int getCorrectColor(String color)
    {
        color = color.toLowerCase();

        if(color.equals("red"))
        {
            return Color.RED;
        }
        else if(color.equals("blue"))
        {
            return Color.BLUE;
        }
        else if(color.equals("green"))
        {
            return Color.GREEN;
        }
        else //Color is yellow
        {
            return Color.YELLOW;
        }

    }
    public int getSpinnerColorPosition(int color)
    {
        int position = 0;
        if(color == Color.RED)
        {
            position = 0;
            return position;
        }
        else if(color == Color.BLUE)
        {
            position = 1;
            return position;
        }
        else if(color == Color.GREEN)
        {
            position = 2;
            return position;
        }
        else //Color is yellow
        {
            position = 3;
            return position;
        }
    }


    public int getCurrentColorSelection(String spinner)
    {
        FamilyMapModel fmm = FamilyMapModel.getInstance();
        int color = 0;

        if(spinner.equals("f")) //f for FAMILY
        {
            color = fmm.getFamilyLinesColor();
            return getSpinnerColorPosition(color);
        }
        else if(spinner.equals("l")) // l for LIFE event
        {
            color = fmm.getEventLinesColor();
            return getSpinnerColorPosition(color);
        }
        else // spinner is s for SPOUSE
        {
            color = fmm.getSpouseLinesColor();
            return getSpinnerColorPosition(color);
        }

    }

    public int getCorrectMapType(String mapType)
    {
        mapType = mapType.toLowerCase();

        /* FROM GOOGLE
        public static final int MAP_TYPE_NORMAL = 1;
    public static final int MAP_TYPE_SATELLITE = 2;
    public static final int MAP_TYPE_TERRAIN = 3;
    public static final int MAP_TYPE_HYBRID = 4;*/

        if(mapType.equals("normal"))
        {
            return GoogleMap.MAP_TYPE_NORMAL;
        }
        else if(mapType.equals("satellite"))
        {
            return GoogleMap.MAP_TYPE_SATELLITE;
        }
        else if(mapType.equals("terrain"))
        {
            return GoogleMap.MAP_TYPE_TERRAIN;
        }
        else
        {
            return GoogleMap.MAP_TYPE_HYBRID;
        }

    }

    public int getCurrentMapSelection()
    {
        int mapType = FamilyMapModel.getInstance().getGoogleMapType();
        /* FROM GOOGLE
        public static final int MAP_TYPE_NORMAL = 1;
    public static final int MAP_TYPE_SATELLITE = 2;
    public static final int MAP_TYPE_TERRAIN = 3;
    public static final int MAP_TYPE_HYBRID = 4;*/

        /*<item>Normal</item>
        <item>Terrain</item>
        <item>Satellite</item>
        <item>Hybrid</item>*/
        int position = 0;
        if(mapType == GoogleMap.MAP_TYPE_NORMAL)
        {
            position = 0;
            return position;
        }
        else if(mapType == GoogleMap.MAP_TYPE_SATELLITE)
        {
            position = 2;
            return position;
        }
        else if(mapType == GoogleMap.MAP_TYPE_TERRAIN)
        {
            position = 1;
            return position;
        }
        else{
            position = 3;
            return position;
        }

    }
}
