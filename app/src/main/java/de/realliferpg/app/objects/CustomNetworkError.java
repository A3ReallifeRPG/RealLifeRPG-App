package de.realliferpg.app.objects;

import de.realliferpg.app.interfaces.RequestTypeEnum;

public class CustomNetworkError {
    public RequestTypeEnum requestType;
    public String msg;
    public int statusCode;

    @Override
    public String toString() {
        String typeName = requestType.toString();

        String error = "Error in Request of type " + typeName + " Status code " + statusCode;

        if(statusCode == 404 || statusCode == 500){
            error += "\n API Token valid ?";
        }

        if(statusCode == 0){
            error += "\n Internet connection ?";
        }

        return error;
    }
}
