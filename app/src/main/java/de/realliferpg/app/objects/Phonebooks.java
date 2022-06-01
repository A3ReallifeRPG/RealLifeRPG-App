package de.realliferpg.app.objects;

public class Phonebooks {
    public int idNR;
    public PhonebookEntry[] phonebook;

    public Phonebooks(int _idNr, PhonebookEntry[] _phonebook){
        this.idNR = _idNr;
        this.phonebook = _phonebook;
    }
}
