package model;

import rmi.RmiInterface;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

/**
 * Created by Rui Pedro Dias on 08/12/2016.
 */

//The beans are responsable for comunication between rmi and the actions

public class LoginBean {

    private RmiInterface rmiInterface;
    private String username;
    private String password;

    public LoginBean(){

        try {
            //Se o rmi estiver a dar problemas vir aqui mudar esta merda

            System.getProperties().put("java.security.policy", "security.policy");
            System.setSecurityManager(new RMISecurityManager());
            rmiInterface = (RmiInterface) LocateRegistry.getRegistry("localhost", 1098).lookup("rmi_server");
        } catch (NotBoundException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public boolean doLogin() throws RemoteException {
        return rmiInterface.doLogin(this.username, this.password);
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
