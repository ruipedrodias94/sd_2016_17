package rmi;


import components.Auction;
import components.Bid;
import components.Client;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Interface do Servidor RMI
 */

public interface RmiInterface extends Remote{

    // Put the methods here!

    boolean registerClient(Client client) throws RemoteException;
    boolean doLogin(Client client) throws RemoteException;
    ArrayList<Client> searchOnlineUsers() throws RemoteException;
    boolean createAuction(Auction auction) throws RemoteException;
    ArrayList<Auction> searchAuction(int code) throws RemoteException;
    boolean bid(Bid bid) throws RemoteException;
    boolean editAuction(Auction auction) throws RemoteException;
    int returnUserID(Client client)throws RemoteException;
    Client getClient(String username, String password) throws RemoteException;

}
