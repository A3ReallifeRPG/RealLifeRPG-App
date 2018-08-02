package de.realliferpg.app.helper;

import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceManager;

import de.realliferpg.app.Constants;
import de.realliferpg.app.Singleton;
import de.realliferpg.app.interfaces.RequestCallbackInterface;
import de.realliferpg.app.objects.Changelog;
import de.realliferpg.app.objects.PlayerInfo;
import de.realliferpg.app.objects.Server;
import de.realliferpg.app.objects.Shop;
import de.realliferpg.app.objects.ShopEntry;
import de.realliferpg.app.objects.Vehicle;

public class ApiHelper {

    private RequestCallbackInterface callbackInterface;
    private PreferenceHelper preferenceHelper;

    public ApiHelper(RequestCallbackInterface callbackInterface){
        this.callbackInterface = callbackInterface;
        preferenceHelper = new PreferenceHelper();
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

        String secret = preferenceHelper.getPlayerAPIToken();

        networkHelper.doJSONRequest(Constants.URL_PLAYERSTATS + secret,callbackInterface,PlayerInfo.Wrapper.class);
    }

    public void getShops(int shopType) {
        NetworkHelper networkHelper = new NetworkHelper();

        if(shopType == Constants.CATEGORY_SHOP){
            networkHelper.doJSONRequest(Constants.URL_SHOPTYPES_ITEMS,callbackInterface,Shop.Wrapper.class);
        }else if(shopType == Constants.CATEGORY_VEHICLE){
            networkHelper.doJSONRequest(Constants.URL_SHOPTYPES_VEHICLES,callbackInterface,Shop.Wrapper.class);
        }
    }

    public void getShopInfo(int shopType,String shop) {
        NetworkHelper networkHelper = new NetworkHelper();

        if(shopType == Constants.CATEGORY_SHOP){
            networkHelper.doJSONRequest(Constants.URL_SHOP_ITEMS + shop,callbackInterface,ShopEntry.Wrapper.class);
        }else if(shopType == Constants.CATEGORY_VEHICLE){
            networkHelper.doJSONRequest(Constants.URL_SHOP_VEHICLES + shop,callbackInterface,Vehicle.Wrapper.class);
        }
    }

}
