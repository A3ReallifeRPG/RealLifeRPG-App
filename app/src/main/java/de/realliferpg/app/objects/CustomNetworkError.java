package de.realliferpg.app.objects;

public class CustomNetworkError {
    public Class requestReturnClass;
    public String msg;
    public int statusCode;

    @Override
    public String toString() {
        String typeName = requestReturnClass.getName();
        typeName = typeName.replaceAll("de.realliferpg.app.","");

        String error = "Error in Request of type " + typeName + " Status code " + statusCode;

        if(statusCode == 404){
            error += "\n API Token valid ?";
        }

        if(statusCode == 0){
            error += "\n Internet connection ?";
        }

        return error;
    }
}
