package rmi;


import components.Auction;
import components.Bid;
import components.Client;
import components.Message;
import tcp.CallbackInterface;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.StringJoiner;

/**
 * Interface do Servidor RMI
 */

public interface RmiInterface extends Remote{

    // Put the methods here!

    boolean registerClient(String username, String password) throws RemoteException;
    String getUserName(int id) throws RemoteException;
    boolean doLogin(String username, String password) throws RemoteException;
    ArrayList<Client> searchOnlineUsers() throws RemoteException;
    boolean createAuction(Auction auction) throws RemoteException;
    ArrayList<Auction> searchAuction(String code) throws RemoteException;
    Auction detailAuction(int code) throws RemoteException;
    boolean bid(Bid bid) throws RemoteException;
    boolean editAuction(Auction antigo, Auction novo, Client client) throws RemoteException;
    Client getClient(String username, String password) throws RemoteException;
    boolean message(Message message) throws RemoteException;
    void putOffline(Client client) throws RemoteException;
    ArrayList<Auction> myAuctions(Client client) throws RemoteException;
    public ArrayList<Message> getMUnreadedMessages(int idUser) throws RemoteException;

}
