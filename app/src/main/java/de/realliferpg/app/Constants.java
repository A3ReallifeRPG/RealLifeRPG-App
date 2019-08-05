package de.realliferpg.app;

public class Constants {

    public static final String URL_CHANGELOG = "https://api.realliferpg.de/v1/changelog";
    public static final String URL_SERVER = "https://api.realliferpg.de/v1/servers";
    public static final String URL_SERVERLOG = "https://api.realliferpg.de/v1/servers/log";
    public static final String URL_PLAYERSTATS = "https://api.realliferpg.de/v1/player/";
    public static final String URL_MARKETPRICES = "https://api.realliferpg.de/v1/market_all";

    // Shop Info
    public static final String URL_SHOPTYPES_ITEMS = "https://api.realliferpg.de/v1/info/items_shoptypes";
    public static final String URL_SHOPTYPES_VEHICLES = "https://api.realliferpg.de/v1/info/vehicles_shoptypes";

    public static final String URL_CBS = "https://api.realliferpg.de/v1/cbs";

    public static final String URL_SHOP_ITEMS = "https://api.realliferpg.de/v1/info/items/";
    public static final String URL_SHOP_VEHICLES = "https://api.realliferpg.de/v1/info/vehicles/";

    public static final int CATEGORY_VEHICLE = 1;
    public static final int CATEGORY_SHOP = 2;
    public static final int CATEGORY_LICENSE = 3;

    public static final int CATEGORY_CALC_VEHICLES = 1;
    public static final int CATEGORY_CALC_GUNS = 2;
    public static final int CATEGORY_CALC_SELL = 3;
    public static final int CATEGORY_CALC_SUM = 4;

    public static final int ERROR_SNACKBAR_DURATION = 5000;
    public static final boolean IS_DEBUG = BuildConfig.DEBUG;
}
