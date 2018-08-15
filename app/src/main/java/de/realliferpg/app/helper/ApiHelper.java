package de.realliferpg.app.helper;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;

import de.realliferpg.app.Constants;
import de.realliferpg.app.Singleton;
import de.realliferpg.app.interfaces.RequestCallbackInterface;
import de.realliferpg.app.interfaces.RequestTypeEnum;
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

    public Object handleResponse(RequestTypeEnum type, Object response){
        Gson gson = new Gson();

        switch (type){
            case PLAYER:
                PlayerInfo.Wrapper playerWrapper = gson.fromJson(response.toString(), PlayerInfo.Wrapper.class);
                PlayerInfo playerInfo = playerWrapper.data[0];
                playerInfo.requested_at = playerWrapper.requested_at;
                Singleton.getInstance().setPlayerInfo(playerInfo);
                return playerInfo;
            case SERVER:
                Server.Wrapper serverWrapper = gson.fromJson(response.toString(), Server.Wrapper.class);
                final ArrayList<Server> servers = new ArrayList<>(Arrays.asList(serverWrapper.data));
                Singleton.getInstance().setServerList(servers);
                return servers;
        }

        return response;
    }

    public Object handleResponse(Object response, Class<?> type){
        Gson gson = new Gson();

        if(type.equals(Changelog.Wrapper.class)){
            Changelog.Wrapper value = gson.fromJson(response.toString(), Changelog.Wrapper.class);
            ArrayList<Changelog> changelogs = new ArrayList<>(Arrays.asList(value.data));
            Singleton.getInstance().setChangelogList(changelogs);
            return changelogs;
        }

        return response;
    }

    private boolean checkCache(Class<?> type){
        long sysTime = (int) (System.currentTimeMillis() / 1000L);

        if(type.equals(PlayerInfo.class)){
            PlayerInfo playerInfo = Singleton.getInstance().getPlayerInfo();
            return playerInfo != null && (sysTime - playerInfo.requested_at) < 30; // TODO define cache intervals somewhere
        }

        return false;
    }

    public void getChangelog() {
        NetworkHelper networkHelper = new NetworkHelper();
        networkHelper.doJSONRequest(Constants.URL_CHANGELOG,callbackInterface,RequestTypeEnum.CHANGELOG);
    }

    public void getServers() {
        NetworkHelper networkHelper = new NetworkHelper();
        networkHelper.doJSONRequest(Constants.URL_SERVER,callbackInterface,RequestTypeEnum.SERVER);
    }

    public void getPlayerStats() {
        NetworkHelper networkHelper = new NetworkHelper();

        String secret = preferenceHelper.getPlayerAPIToken();

        networkHelper.doJSONRequest(Constants.URL_PLAYERSTATS + secret,callbackInterface,RequestTypeEnum.PLAYER);
    }

    public void getShops(int shopType) {
        NetworkHelper networkHelper = new NetworkHelper();

        if(shopType == Constants.CATEGORY_SHOP){
            networkHelper.doJSONRequest(Constants.URL_SHOPTYPES_ITEMS,callbackInterface,RequestTypeEnum.SHOP);
        }else if(shopType == Constants.CATEGORY_VEHICLE){
            networkHelper.doJSONRequest(Constants.URL_SHOPTYPES_VEHICLES,callbackInterface,RequestTypeEnum.SHOP);
        }
    }

    public void getShopInfo(int shopType,String shop) {
        NetworkHelper networkHelper = new NetworkHelper();

        if(shopType == Constants.CATEGORY_SHOP){
            networkHelper.doJSONRequest(Constants.URL_SHOP_ITEMS + shop,callbackInterface,RequestTypeEnum.SHOP_INFO_ITEM);
        }else if(shopType == Constants.CATEGORY_VEHICLE){
            networkHelper.doJSONRequest(Constants.URL_SHOP_VEHICLES + shop,callbackInterface,RequestTypeEnum.SHOP_INFO_VEHICLE);
        }
    }

}
