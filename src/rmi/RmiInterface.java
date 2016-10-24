package rmi;


import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Interface do Servidor RMI
 */

public interface RmiInterface extends Remote{

    // Put the methods here!

    String registerClient(String userName, String password) throws RemoteException;
    String doLogin(String userName, String password) throws RemoteException;
    String searchOnlineUsers() throws RemoteException;
    String createAuction(String command, int userId) throws RemoteException;

}
