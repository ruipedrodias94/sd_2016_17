package model;

import rmi.RmiInterface;
import rmi.rmiConnection;

/**
 * Created by Rui Pedro Dias on 16/12/2016.
 */
public class IndexBean {

    public String getAuthUrl() {
        return authUrl;
    }

    public void setAuthUrl(String authUrl) {
        this.authUrl = authUrl;
    }

    private String authUrl;

    private RmiInterface rmiInterface;
    private rmiConnection rmiConnection;


    public IndexBean(){
        rmiConnection = new rmiConnection();
        rmiInterface = rmiConnection.getInterface();
    }

}
