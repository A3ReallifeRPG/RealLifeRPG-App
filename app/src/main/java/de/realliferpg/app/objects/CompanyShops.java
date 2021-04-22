package de.realliferpg.app.objects;

public class CompanyShops {
    public class Wrapper {
        public CompanyShops[] data;
        public long requested_at;
    }

    public long industrialAreaID;
    public Company company;
    private String pos;
    public Shop[] shops;
}

