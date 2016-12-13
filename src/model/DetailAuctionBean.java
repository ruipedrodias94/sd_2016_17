package model;

import components.Auction;
import components.Bid;
import components.Message;
import rmi.RmiInterface;
import rmi.rmiConnection;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.ArrayList;

/**
 * Created by Rui Pedro Dias on 10/12/2016.
 */
public class DetailAuctionBean {

    private RmiInterface rmiInterface;
    private rmiConnection rmiC;

    private Auction auction = null;
    private String auctionId;

    public DetailAuctionBean(){
        rmiC = new rmiConnection();
        rmiInterface = rmiC.getInterface();
    }

    public Auction detailAuction() throws RemoteException {
        rmiInterface = rmiC.getInterface();
        this.auction = rmiInterface.detailAuction(this.auctionId);
        return auction;
    }


    public void setAuction(Auction auction){
        this.auction = auction;
    }

    public Auction getAuction() {
        return auction;
    }


    public String getAuctionId() {
        return auctionId;
    }

    public void setAuctionId(String auctionId) {
        this.auctionId = auctionId;
    }
}
