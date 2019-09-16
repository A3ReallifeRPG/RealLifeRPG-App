package de.realliferpg.app.objects;

import java.util.Map;

public class CBSData {

    public class Wrapper{
        public CBSData[] data;
        public long requested_at;
    }

    public int id;
    public int active;
    public String title;
    public String image;
    public String desc;
    public int funding_required;
    public int live;
    public int finished;
    public int map_pos_x;
    public int map_pos_y;
    public int amount;

    public Map<String, Integer> required;
    public Map<String, Integer> delivered;
}
