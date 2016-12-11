package model;

import components.Auction;
import rmi.RmiInterface;
import rmi.rmiConnection;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Created by Rui Pedro Dias on 10/12/2016.
 */
public class SearchAuctionBean {

    private RmiInterface rmiInterface;
    private String code;
    private rmiConnection rmiC;


    public SearchAuctionBean() {
        rmiC = new rmiConnection();
        rmiInterface = rmiC.getInterface();
    }

    public ArrayList<Auction> searchAuction() throws RemoteException {
        rmiInterface = rmiC.getInterface();
        return rmiInterface.searchAuction(this.code);
    }

    public void setCode(String code) {
        this.code = code;
    }
}
