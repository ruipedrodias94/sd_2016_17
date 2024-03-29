package model;

import com.github.scribejava.apis.FacebookApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.oauth.OAuth20Service;
import org.apache.struts2.components.Debug;
import rmi.RmiInterface;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.Random;

/**
 * Created by Rui Pedro Dias on 08/12/2016.
 */

//The beans are responsable for comunication between rmi and the actions

public class LoginBean {

    private RmiInterface rmiInterface;
    //Things need to do the login
    private String username;
    private String password;
    private String authUrl;
    private String code;



    public LoginBean() {

        try {
            rmiInterface = (RmiInterface) LocateRegistry.getRegistry("localhost", 2080).lookup("rmi_server");
            System.out.println("O RMI é " + rmiInterface);
        } catch (NotBoundException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public boolean doLogin() throws RemoteException {
        return rmiInterface.doLogin(this.username, this.password);
    }


   public int userID() throws RemoteException {
        return rmiInterface.getUserId(this.username);
   }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public String getAuthUrl() {
        return authUrl;
    }

    public void setAuthUrl(String authUrl) {
        this.authUrl = authUrl;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
