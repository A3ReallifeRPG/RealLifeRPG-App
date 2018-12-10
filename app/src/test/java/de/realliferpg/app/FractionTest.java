package de.realliferpg.app;

import org.junit.Assert;
import org.junit.Test;

import de.realliferpg.app.helper.FractionMappingHelper;
import de.realliferpg.app.objects.FractionInfo;

public class FractionTest {
    @Test
    public void justizRangTest(){
        FractionInfo fractionInfo = new FractionInfo(1,0,0);
        String temp = FractionMappingHelper.getCopRankAsString(fractionInfo.coplevel);
        Assert.assertEquals("Cop-Level 1 ist Justizbeamter", "Justizbeamter", temp);
    }

    @Test
    public void medicRang1Test(){
        FractionInfo fractionInfo = new FractionInfo(0,1,0);
        String temp = FractionMappingHelper.getMedicRankAsString(fractionInfo.mediclevel);
        Assert.assertEquals("Medic-Level 1 ist Ersthelfer", "Ersthelfer", temp);
    }

    @Test
    public void racRang1Test(){
        FractionInfo fractionInfo = new FractionInfo(0,0,1);
        String temp = FractionMappingHelper.getRacRankAsString(fractionInfo.raclevel);
        Assert.assertEquals("RAC-Level 1 ist Azubi", "Azubi", temp);
    }

    @Test
    public void copRang2Test(){
        FractionInfo fractionInfo = new FractionInfo(2,0,0);
        String temp = FractionMappingHelper.getCopRankAsString(fractionInfo.coplevel);
        Assert.assertEquals("Cop-Level 2 ist Polizeimeisteranwärter", "Polizeimeisteranwärter", temp);
    }

    @Test
    public void copRang16Test(){
        FractionInfo fractionInfo = new FractionInfo(16,0,0);
        String temp = FractionMappingHelper.getCopRankAsString(fractionInfo.coplevel);
        Assert.assertEquals("Cop-Level 16 ist Polizeipräsident", "Polizeipräsident",  temp);
    }

    @Test
    public void verschRaengeTest(){
        FractionInfo fractionInfo = new FractionInfo(4,6,2);
        String cop = FractionMappingHelper.getCopRankAsString(fractionInfo.coplevel);
        String med = FractionMappingHelper.getMedicRankAsString(fractionInfo.mediclevel);
        String rac = FractionMappingHelper.getRacRankAsString(fractionInfo.raclevel);
        Assert.assertEquals("Cop-Level 4 ist Polizeiobermeister", "Polizeiobermeister", cop);
        Assert.assertEquals("Medic-Level 6 ist Notarzt", "Notarzt", med);
        Assert.assertEquals("RAC-Level 2 ist Geselle", "Geselle", rac);
    }
}
