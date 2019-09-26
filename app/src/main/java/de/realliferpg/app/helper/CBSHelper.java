package de.realliferpg.app.helper;

import java.util.ArrayList;
import java.util.Map;

import de.realliferpg.app.objects.CBSData;
import de.realliferpg.app.objects.RessourceInfo;

public class CBSHelper {


    public static void setRessourceInfo(CBSData cbsData) {

        if (cbsData.ressourceInfo.size() > 0) {
            // hat bereits Elemente
            return;
        }

        for (Map.Entry<String, Integer> entry : cbsData.required.entrySet()) {
            String ressource_name = entry.getKey();
            int required = entry.getValue();
            int delivered = cbsData.delivered.get(ressource_name);

            cbsData.ressourceInfo.add(new RessourceInfo(ressource_name, delivered, required));
        }
    }
}
