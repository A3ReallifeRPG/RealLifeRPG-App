package de.realliferpg.app.objects;

public class Server {
    public class Wrapper{
        public Server[] data;
        public String requested_at;
    }

    public int Id;
    public int ModId;
    public int appId;
    public int online;

    public String Servername;
    public String Description;

    public String IpAddress;
    public int Port;
    public String ServerPassword;
    public int Gamemode;
    public String StartParameters;

    public int Slots;
    public int Playercount;
    public int Civilians;
    public int Medics;
    public int Cops;
    public int Adac;

    public String[] Players;

    //TODO add other parameters

    @Override
    public String toString() {
        return "ID: " + Id + " - " + Servername + " Players: " + Playercount;
    }
}
