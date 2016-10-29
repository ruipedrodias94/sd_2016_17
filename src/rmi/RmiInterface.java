package rmi;


import components.Auction;
import components.Bid;
import components.Client;
import components.Message;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Interface do Servidor RMI
 */

public interface RmiInterface extends Remote{

    // Put the methods here!

    boolean registerClient(String username, String passwordt) throws RemoteException;
    boolean doLogin(Client client) throws RemoteException;
    ArrayList<Client> searchOnlineUsers() throws RemoteException;
    boolean createAuction(Auction auction) throws RemoteException;
    ArrayList<Auction> searchAuction(int code) throws RemoteException;
    Auction detailAuction(int code) throws RemoteException;
    boolean bid(Bid bid) throws RemoteException;
    boolean editAuction(Auction antigo, Auction novo, Client client) throws RemoteException;
    Client getClient(String username, String password) throws RemoteException;
    boolean message(Message message) throws RemoteException;
    void putOffline(Client client) throws RemoteException;
    public ArrayList<Auction> myAuctions(Client client) throws RemoteException;

}
