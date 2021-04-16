package de.realliferpg.app;

public class Constants {

    public static final String HOST = "https://api.realliferpg.de/v1/";

    public static final String URL_CHANGELOG = HOST + "changelog";
    public static final String URL_SERVER =  HOST + "servers";
    public static final String URL_PLAYERSTATS =  HOST + "player/";
    public static final String URL_MARKETPRICES =  HOST + "market_all";

    // Shop Info
    public static final String URL_SHOPTYPES_ITEMS =  HOST + "info/items_shoptypes";
    public static final String URL_SHOPTYPES_VEHICLES =  HOST + "info/vehicles_shoptypes";

    public static final String URL_CBS =  HOST + "cbs";

    public static final String URL_SHOP_ITEMS =  HOST + "info/items/";
    public static final String URL_SHOP_VEHICLES =  HOST + "info/vehicles/";

    public static final String URL_SHOPS_COMPANYS =  HOST + "company_shops";

    public static final int CATEGORY_VEHICLE = 1;
    public static final int CATEGORY_SHOP = 2;

    public static final int ERROR_SNACKBAR_DURATION = 5000;
}
