package de.realliferpg.app.helper;

import de.realliferpg.app.Constants;
import de.realliferpg.app.interfaces.RequestCallbackInterface;
import de.realliferpg.app.objects.Changelog;

public class ApiHelper {

    private RequestCallbackInterface callbackInterface;

    public ApiHelper(RequestCallbackInterface callbackInterface){
        this.callbackInterface = callbackInterface;
    }

    public void getChangelog() {
        NetworkHelper networkHelper = new NetworkHelper();
        networkHelper.doJSONRequest(Constants.URL_CHANGELOG,callbackInterface,Changelog.Wrapper.class);
    }
}
