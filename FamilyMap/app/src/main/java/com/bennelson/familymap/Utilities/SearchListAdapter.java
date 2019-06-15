package com.bennelson.familymap.Utilities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bennelson.familymap.Model_Classes.Event;
import com.bennelson.familymap.Model_Classes.FamilyMember;
import com.bennelson.familymap.R;

import java.util.ArrayList;

/**
 * Created by BenNelson on 12/6/16.
 */

public class SearchListAdapter extends BaseExpandableListAdapter {

    private ArrayList<String> headers = new ArrayList<>();
    private ArrayList<FamilyMember> lifeEvents;
    private ArrayList<FamilyMember> family;
    private ArrayList<ArrayList<FamilyMember>> groups = new ArrayList<>();
    private Context context;


    // private static String[] group1 = {"Person1", "Person2"}; //list of children
    // private static String[] group2 = {"Chevy", "Ford", "Toyota", "Honda"}; //list of children
    //private String[][] groups = {group1, group2};


    public SearchListAdapter(Context context, ArrayList<FamilyMember> lifeEvents, ArrayList<FamilyMember> family)
    {
        headers.add("Events found:");
        headers.add("People found:");

        this.context = context;
        this.lifeEvents = lifeEvents;
        this.family = family;

        groups.clear();
        groups.add(lifeEvents);
        groups.add(family);

    }


    //Returns the size of groups....how many parents there are
    @Override
    public int getGroupCount() {
        return 2;
    }

    //Returns how many children exist in this specific group?
    @Override
    public int getChildrenCount(int groupPosition) {
        return groups.get(groupPosition).size();
    }

    //Goes to the groups array and returns the position of the list
    @Override
    public Object getGroup(int groupPosition) {
        return headers.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return groups.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    //Populate the header with the list or information it needs. convertView is the actual GUI
    @Override
    public View getGroupView(final int groupPosition, final boolean isExpanded, View convertView, ViewGroup parent) {


        if(convertView == null)
        {
            LayoutInflater layoutInflater = (LayoutInflater)this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.person_header,null);


        }

        String title = (String) getGroup(groupPosition);
        TextView header = (TextView) convertView.findViewById(R.id.person);
        header.setText(title);

        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent)
    {

        if(convertView == null)
        {
            LayoutInflater layoutInflater = (LayoutInflater)this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.person_child, null);
        }
        FamilyMember familyMember = (FamilyMember) getChild(groupPosition, childPosition);
        if(familyMember.isFamilyMember())
        {
            //FAMILY

            String firstLine = familyMember.getPerson().getFirstName() + " " + familyMember.getPerson().getLastName();
            ((TextView) convertView.findViewById(R.id.firstLine)).setText(firstLine);

            String secondLine = familyMember.getRelationship();
            ((TextView) convertView.findViewById(R.id.secondLine)).setText(secondLine);

            //PUT BOY OR GIRL ICON CORRECTLY
            final ImageView icon = (ImageView) convertView.findViewById(R.id.marker_or_gender);
            String gender = familyMember.getPerson().getGender();
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
        else
        {
            //LIFE EVENTS

            String firstLine = familyMember.getEvent().getDescription() +
                    ": " + familyMember.getEvent().getCity() + ", " + familyMember.getEvent().getCountry();
            if(!familyMember.getEvent().getYear().equals("none"))
            {
                //then we know we have a year, so append it
                firstLine += " (" + familyMember.getEvent().getYear() + ")";
            }
            ((TextView) convertView.findViewById(R.id.firstLine)).setText(firstLine);

            String secondLine = familyMember.getPerson().getFirstName() + " " + familyMember.getPerson().getLastName();
            ((TextView) convertView.findViewById(R.id.secondLine)).setText(secondLine);



            //Set Marker Icon
            final ImageView icon = (ImageView) convertView.findViewById(R.id.marker_or_gender);

            icon.setImageResource(R.drawable.marker);






        }


        return convertView;

    }



    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
