package de.realliferpg.app.objects;

public class FractionInfo {

    public FractionInfo (int cop, int medic, int rac){
        this.coplevel = cop;
        this.mediclevel = medic;
        this.raclevel = rac;
    }

    public int coplevel;
    public int mediclevel;
    public int raclevel;
    public int sizeGroup = 4;
    public int sizeChild = 1;
}
