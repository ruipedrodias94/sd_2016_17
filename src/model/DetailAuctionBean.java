package model;

import components.Auction;
import rmi.RmiInterface;
import rmi.rmiConnection;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

/**
 * Created by Rui Pedro Dias on 10/12/2016.
 */
public class DetailAuctionBean {

    private RmiInterface rmiInterface;
    private rmiConnection rmiC;

    public Auction getAuction() {
        return auction;
    }

    public String getId() {
        return id;
    }

    private Auction auction = null;
    private String id;

    public DetailAuctionBean(){
        rmiC = new rmiConnection();
        rmiInterface = rmiC.getInterface();
    }

    public Auction detailAuction() throws RemoteException {
        rmiInterface = rmiC.getInterface();
        auction = rmiInterface.detailAuction(this.id);
        return auction;
    }

    public void setId(String id){
        this.id = id;
    }

    public void setAuction(Auction auction){
        this.auction = auction;
    }
}
