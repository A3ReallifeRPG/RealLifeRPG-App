package de.realliferpg.app.objects;

import de.realliferpg.app.interfaces.IBuilding;

public class House implements IBuilding {
    public int id;
    public int payed_for;
    public int disabled;
    public String location;
    public String[] players;

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
