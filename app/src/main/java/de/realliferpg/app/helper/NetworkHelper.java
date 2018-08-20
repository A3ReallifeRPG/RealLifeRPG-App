package de.realliferpg.app.helper;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import de.realliferpg.app.Singleton;
import de.realliferpg.app.interfaces.RequestCallbackInterface;
import de.realliferpg.app.interfaces.RequestTypeEnum;
import de.realliferpg.app.objects.CustomNetworkError;

public class NetworkHelper {

    public void doJSONRequest(String url, final RequestCallbackInterface callback, final RequestTypeEnum type) {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        callback.onResponse(type, response);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("NetworkHelper", "Error in response");
                        CustomNetworkError customNetworkError = new CustomNetworkError();

                        customNetworkError.requestType = type;

                        if (error.networkResponse != null) {
                            customNetworkError.statusCode = error.networkResponse.statusCode;
                        }
                        customNetworkError.msg = error.getMessage();

                        // TODO better handling since multiple simultaneous requests could override this error
                        Singleton.getInstance().setNetworkError(customNetworkError);
                        callback.onResponse(RequestTypeEnum.NETWORK_ERROR, null);
                    }
                });

        Singleton.getInstance().addToRequestQueue(jsonObjectRequest);
    }
}
