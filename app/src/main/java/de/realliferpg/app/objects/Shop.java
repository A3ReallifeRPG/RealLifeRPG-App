package de.realliferpg.app.objects;

/**
 * Wrapper for response of shop type query
 */
public class Shop {
    public class Wrapper{
        public Shop[] data;
        public String requested_at;
    }

    public String shoptype;
    public String shopname;

    @Override
    public String toString() {
        return shopname;
    }
}
