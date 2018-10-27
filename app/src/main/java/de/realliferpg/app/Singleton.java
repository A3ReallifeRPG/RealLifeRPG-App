package de.realliferpg.app;

import android.content.Context;
import android.support.design.widget.Snackbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;

import de.realliferpg.app.objects.Changelog;
import de.realliferpg.app.objects.CustomNetworkError;
import de.realliferpg.app.objects.MarketItem;
import de.realliferpg.app.objects.PlayerInfo;
import de.realliferpg.app.objects.Server;
import de.realliferpg.app.objects.Shop;
import de.realliferpg.app.objects.ShopItem;
import de.realliferpg.app.objects.ShopVehicle;

public class Singleton {
    private static final Singleton instance = new Singleton();

    public static Singleton getInstance() {
        return instance;
    }

    private Singleton() {
    }

    private RequestQueue volleyQueue;
    private Context context;

    private Snackbar currentSnackbar;

    private PlayerInfo playerInfo;
    private ArrayList<Server> serverList;
    private ArrayList<Changelog> changelogList;
    private ArrayList<Shop> shopList;
    private ArrayList<ShopVehicle> shopVehicleList;
    private ArrayList<ShopItem> shopItemList;
    private ArrayList<MarketItem> marketPriceslist;

    private CustomNetworkError networkError;

    private String scanResponse;
    private String errorMsg;

    private RequestQueue getRequestQueue() {
        if (volleyQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            volleyQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return volleyQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }


    public void setContext(Context context) {
        this.context = context;
    }

    public Context getContext (){return this.context;}

    public PlayerInfo getPlayerInfo() {
        return playerInfo;
    }

    public void setPlayerInfo(PlayerInfo playerInfo) {
        this.playerInfo = playerInfo;
    }

    public String getScanResponse() {
        if(scanResponse == null){
            scanResponse = "";
        }
        return scanResponse;
    }

    public void setScanResponse(String scanResponse) {
        this.scanResponse = scanResponse;
    }

    public String getErrorMsg() {
        if(scanResponse == null){
            scanResponse = context.getString(R.string.str_no_msg);
        }
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public void setCurrentSnackbar(Snackbar currentSnackbar) {
        this.currentSnackbar = currentSnackbar;
    }

    public void dismissSnackbar(){
        if(currentSnackbar != null){
            currentSnackbar.dismiss();
        }
    }

    public ArrayList<Server> getServerList() {
        return serverList;
    }

    public void setServerList(ArrayList<Server> serverList) {
        this.serverList = serverList;
    }

    public ArrayList<Changelog> getChangelogList() {
        return changelogList;
    }

    public void setChangelogList(ArrayList<Changelog> changelogList) {
        this.changelogList = changelogList;
    }

    public ArrayList<Shop> getShopList() {
        return shopList;
    }

    public void setShopList(ArrayList<Shop> shopList) {
        this.shopList = shopList;
    }

    public ArrayList<ShopVehicle> getShopVehicleList() {
        return shopVehicleList;
    }

    public void setShopVehicleList(ArrayList<ShopVehicle> shopVehicleList) {
        this.shopVehicleList = shopVehicleList;
    }

    public ArrayList<ShopItem> getShopItemList() {
        return shopItemList;
    }

    public void setShopItemList(ArrayList<ShopItem> shopItemList) {
        this.shopItemList = shopItemList;
    }

    public CustomNetworkError getNetworkError() {
        return networkError;
    }

    public void setNetworkError(CustomNetworkError networkError) {
        this.networkError = networkError;
    }

    public ArrayList<MarketItem> getMarketPrices() { return marketPriceslist;
    }

    public void setMarketItemList(ArrayList<MarketItem> marketItems) {
        this.marketPriceslist = marketItems;
    }
}
