package model;

import rmi.RmiInterface;
import rmi.rmiConnection;

/**
 * Created by Rui Pedro Dias on 16/12/2016.
 */
public class IndexBean {

    String state;
    String code;

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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
