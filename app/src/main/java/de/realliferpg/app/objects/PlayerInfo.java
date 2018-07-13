package de.realliferpg.app.objects;

public class PlayerInfo {
    public class Wrapper{
        public PlayerInfo[] data;
        public String requested_at;
    }

    public int id;
    public String pid;
    public String guid;
    public String name;

    public int cash;
    public int bankacc;

    public String coplevel;
    public String mediclevel;
    public String adaclevel;
    public String adminlevel;
    public String donatorlvl;


    public int arrested;
    public int citizen;
    public int tutorial;
    public int dsgvo;

    public int level;
    public int exp;
    public int skillpoint;

    public int hunger;
    public int thirst;
    public int drugs;
    public int addiction;
    public int drug_story;
    public int drug_lvl;
    public int jail_time;

    public String pos;
    public int pos_alive;
    public int pos_free;

    public int server_id;
    public String updated_at;
    public String created_at;

    public String avatar;
    public String avatar_full;
    public String avatar_medium;
    public String profilename;
    public String profileurl;

    @Override
    public String toString() {
        return "ID: " + id + " PID:" + pid + " Name: " + name;
    }
}
