package rmi;

import java.rmi.RemoteException;

import java.rmi.*;
import java.rmi.registry.LocateRegistry;


/**
 * Created by jorgearaujo on 11/12/16.
 */
public class rmiConnection {

    RmiInterface rmi;

    public RmiInterface getInterface() {
        try {
            rmi = (RmiInterface) LocateRegistry.getRegistry("localhost", 2080).lookup("rmi_server");
            System.out.println("RMI é " + rmi);
            return rmi;
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        }

        return rmi;
    }

}
