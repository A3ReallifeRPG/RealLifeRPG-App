package de.realliferpg.app.objects;

import de.realliferpg.app.interfaces.IBuilding;

public class Building implements IBuilding {

    public int id;
    public String classname;
    public int stage;
    public String location;
    public int disabled;
    public String[] players;
    public int payed_for;


    @Override
    public int getId() {
        return id;
    }

    @Override
    public int getPayedForDays() { return (int)(payed_for/24); }

    @Override
    public int getPayedForHours() { return payed_for; }

    @Override
    public int getDisabled() {
        return disabled;
    }
}
