package de.realliferpg.app.objects;

import java.util.Date;

public class Changelog {
    public class Wrapper{
        public Changelog[] data;
        public String requested_at;
    }

    public int id;
    public String version;
    public String[] change_mission;
    public String[] change_map;
    public String[] change_mod;

    public String note;
    public String release_at;
    public String created_at;
    public String updated_at;
}
