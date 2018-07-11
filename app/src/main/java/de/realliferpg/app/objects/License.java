package de.realliferpg.app.objects;

public class License {

    public String displayName;

    public String faction;
    public int price;
    public int level;

    public License(String displayName, String faction){
        this.displayName = displayName;
        this.faction = faction;
    }

}
