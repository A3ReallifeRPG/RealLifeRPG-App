package de.realliferpg.app.helper;


import android.util.Log;

import com.google.gson.Gson;

import de.realliferpg.app.objects.Changelog;

public class ApiHelper {


    public void getChangelog(){

        String json = "{" +
                "'data': [" +
                "        {" +
                "            'id': 128," +
                "            'version': '1.3.4'," +
                "            'change_mission': [" +
                "                'LSD Verarbeitung gefixt'," +
                "                'JSRS Update in Spyglass freigeschaltet'," +
                "                'BMW M5 in Shop eingetragen'," +
                "                'Debug Meldung beim verarbeiten entfernt'," +
                "                'Zamak gebufft'," +
                "                'Bug beim F\u00fcllen von Tanklastern behoben'," +
                "                'Event Tool verbessert'" +
                "            ]," +
                "            'change_map': []," +
                "            'change_mod': []," +
                "            'note': ''," +
                "            'active': 1," +
                "            'release_at': '2018-07-03 15:00:00'," +
                "            'created_at': '2018-07-03 11:16:16'," +
                "            'updated_at': '2018-07-03 11:22:00'" +
                "        }," +
                "        {" +
                "            'id': 123," +
                "            'version': '1.3.3'," +
                "            'change_mission': [" +
                "                'Zeigen von Fahrzeugpapieren (als Besitzer) (noch mit tempor\u00e4rem Bild)'," +
                "                'Tuning Shop Fehler behoben'," +
                "                'Tankstelllen Fehler behoben (jetzt hoffentlich alle)'," +
                "                'Cop ELW Shop gefixt'," +
                "                'Ricky Nacho abbruch Funktion klarer gemacht'," +
                "                'Traktor Farmen gefixt'," +
                "                'Craften/Verarbeiten zeigt nun die Icons von den I-Inventar-Items wenn sie fehlen'," +
                "                'Tankstellenraub z\u00e4hlt nur noch Cops f\u00fcr minimale Anzahl (nicht mehr auch justiz)'," +
                "                'Fehler im Crafting behoben (Camo Helm, Ural Plane)'," +
                "                'H\u00f6here Chance f\u00fcr Taschenlampe und M\u00fcndungsd\u00e4mpfer beim Forschen'," +
                "                'Traktor auf Fahrzeug laden gefixt'," +
                "                'Truck Box und Transport Unterschiedliche Werte'," +
                "                'Doppeltes erscheinen der Map Marker liste behoben'," +
                "                'Cops landen nicht mehr auf der Wanted Liste wenn sie Appartements aufbrechen'," +
                "                'Bushmaster und Katana etwas g\u00fcnstiger'" +
                "            ]," +
                "            'change_map': [" +
                "                'St\u00fchle im RAC Leitstellen Raum'" +
                "            ]," +
                "            'change_mod': []," +
                "            'note': 'Dieser Changelog ist ein Sammelchangelog, einige \u00c4nderungen sind bereits vor dem angezeigten Datum aktiv auf dem Server gewesen.'," +
                "            'active': 1," +
                "            'release_at': '2018-06-29 15:00:00'," +
                "            'created_at': '2018-06-21 18:03:40'," +
                "            'updated_at': '2018-06-29 14:49:09'" +
                "        }" +
                "]" +
                "}";

        Gson gson = new Gson();
        Changelog.Wrapper value = gson.fromJson(json, Changelog.Wrapper.class);
        Log.d("Test","Test");
    }

}
