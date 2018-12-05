package de.realliferpg.app.objects;

public class FractionInfo {

    public FractionInfo (int cop, int medic, int rac, int justiz){
        this.coplevel = cop;
        this.mediclevel = medic;
        this.raclevel = rac;
        this.justizlevel = justiz;
    }

    public int coplevel;
    public int mediclevel;
    public int raclevel;
    public int justizlevel;
    public int sizeGroup = 4;
    public int sizeChild = 1;
}
