package de.realliferpg.app.objects;

import de.realliferpg.app.interfaces.IBuilding;

public class Rental implements IBuilding {
    public int id;
    public int payed_for;
    public int disabled;
    public int active;

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
