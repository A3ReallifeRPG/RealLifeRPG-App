package de.realliferpg.app.objects;

public class ShopEntry {

    public String displayName;
    public String className;

    public String shop;
    public int price;
    public int level;

    public ShopEntry(String displayName, String shop){
        this.displayName = displayName;
        this.shop = shop;
    }

}
