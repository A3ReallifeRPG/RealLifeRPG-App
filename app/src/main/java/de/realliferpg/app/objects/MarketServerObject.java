package de.realliferpg.app.objects;

public class MarketServerObject {
    public class Wrapper{
        public MarketServerObject[] data;
        public String requested_at;
    }

    public int server_id;
    public String server_name;
    public boolean online;
    public Item[] market;

    public class Item {
        public String item;
        public int price;
        public int server;
        public String updated_at;
        public String created_at;
        public String localized;
    }


}
