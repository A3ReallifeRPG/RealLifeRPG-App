package de.realliferpg.app;

import android.content.Context;
import android.support.design.widget.Snackbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import de.realliferpg.app.objects.PlayerInfo;

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

    private String scanResponse;
    private String errorMsg;

    public RequestQueue getRequestQueue() {
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
}
