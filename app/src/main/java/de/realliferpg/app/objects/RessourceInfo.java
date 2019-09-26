package de.realliferpg.app.objects;

public class RessourceInfo {

    public RessourceInfo(String class_name, Integer delivered, Integer required){
        this.class_name = class_name;
        this.delivered = delivered;
        this.required = required;
    }

    public String class_name;
    public Integer delivered;
    public Integer required;
}
