package de.realliferpg.app.objects;

public class Vehicle {

    public String displayName;
    public String className;

    public String shop;
    public int price;
    public int vItemSpace;
    public int level;

    public Vehicle(String displayName, String shop){
        this.displayName = displayName;
        this.shop = shop;
    }
}
