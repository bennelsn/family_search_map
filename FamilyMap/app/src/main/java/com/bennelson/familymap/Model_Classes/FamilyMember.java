package com.bennelson.familymap.Model_Classes;

/**
 * Created by BenNelson on 12/6/16.
 */

public class FamilyMember
{
    private Person person;
    private Event event;
    private String relationship;
    private boolean isFamilyMember = false;
    private boolean isEvent = false;

    public FamilyMember()
    {

    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public boolean isFamilyMember() {
        return isFamilyMember;
    }

    public void setFamilyMember(boolean familyMember) {
        isFamilyMember = familyMember;
    }

    public boolean isEvent() {
        return isEvent;
    }

    public void setEvent(boolean event) {
        isEvent = event;
    }
}
