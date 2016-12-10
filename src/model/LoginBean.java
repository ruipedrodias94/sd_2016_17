package model;

import org.apache.struts2.components.Debug;
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
    //Things need to do the login
    private String username;
    private String password;

    public LoginBean() {

        try {

            rmiInterface = (RmiInterface) Naming.lookup("rmi_server");
            System.out.println("O RMI é " + rmiInterface);
        } catch (NotBoundException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public boolean doLogin() throws RemoteException {
        if (this.username.equals("ruipedro") && this.password.equals("1234")){
            return true;
        }
        //return rmiInterface.userMatchesPass(this.username, this.password);
        return false;
    }

    //TODO método para obter o id do user
    public int userID(){
        return 1;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
