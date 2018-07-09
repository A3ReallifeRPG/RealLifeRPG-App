package de.realliferpg.app.objects;

import java.util.Date;

public class Changelog {
    public class Wrapper{
        Changelog[] data;
        String requested_at;
    }

    int id;
    String version;
    String[] change_mission;
    String[] change_map;
    String[] change_mod;

    String note;
    String release_at;
    String created_at;
    String updated_at;
}
