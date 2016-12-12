package model;

import rmi.RmiInterface;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

/**
 * Created by jorgearaujo on 12/12/16.
 */
public class RegisterBean {

    private RmiInterface rmiInterface;
    //Things need to do the Register
    private String username;
    private String password;

    public RegisterBean() {

        try {
            rmiInterface = (RmiInterface) LocateRegistry.getRegistry("localhost", 2080).lookup("rmi_server");
            System.out.println("O RMI é " + rmiInterface);
        } catch (NotBoundException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public boolean registerUser() throws RemoteException {
        return rmiInterface.registerClient(this.username,this.password);
    }



    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}