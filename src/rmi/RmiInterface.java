package rmi;


import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interface do Servidor RMI
 */

public interface RmiInterface extends Remote{

    int teste() throws RemoteException;
}
