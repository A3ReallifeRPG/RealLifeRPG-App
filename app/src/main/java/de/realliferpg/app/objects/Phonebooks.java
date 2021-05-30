package de.realliferpg.app.objects;

public class Phonebooks {
    public String side;
    public PhonebookEntry[] phonebook;

    public Phonebooks(String _side, PhonebookEntry[] _phonebook){
        this.side = _side;
        this.phonebook = _phonebook;
    }
}
