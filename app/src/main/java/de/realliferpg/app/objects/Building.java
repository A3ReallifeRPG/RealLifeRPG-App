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
    public int getPayed_for() {
        return payed_for;
    }

    @Override
    public int getDisabled() {
        return disabled;
    }
}
