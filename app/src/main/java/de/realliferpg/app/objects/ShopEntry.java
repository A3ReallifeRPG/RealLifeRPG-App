package de.realliferpg.app.objects;

/**
 * Item Shop entry object
 */
public class ShopEntry {
    public class Wrapper{
        public ShopEntry[] data;
        public String requested_at;
    }

    public int id;

    public String name;
    public String classname;

    public String shoptype;
    public String shopname;

    public String category;
    public int price;
    public int level;


}
