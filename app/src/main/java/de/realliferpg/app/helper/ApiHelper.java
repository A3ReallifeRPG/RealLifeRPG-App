package de.realliferpg.app.helper;

import de.realliferpg.app.Constants;
import de.realliferpg.app.interfaces.RequestCallbackInterface;
import de.realliferpg.app.objects.Changelog;
import de.realliferpg.app.objects.PlayerInfo;
import de.realliferpg.app.objects.Server;

public class ApiHelper {

    private RequestCallbackInterface callbackInterface;

    public ApiHelper(RequestCallbackInterface callbackInterface){
        this.callbackInterface = callbackInterface;
    }

    public void getChangelog() {
        NetworkHelper networkHelper = new NetworkHelper();
        networkHelper.doJSONRequest(Constants.URL_CHANGELOG,callbackInterface,Changelog.Wrapper.class);
    }

    public void getServers() {
        NetworkHelper networkHelper = new NetworkHelper();
        networkHelper.doJSONRequest(Constants.URL_SERVER,callbackInterface,Server.Wrapper.class);
    }

    public void getPlayerStats() {
        NetworkHelper networkHelper = new NetworkHelper();
        networkHelper.doJSONRequest(Constants.URL_PLAYERSTATS,callbackInterface,PlayerInfo.Wrapper.class);
    }
}
