package model;

import components.Bid;
import rmi.RmiInterface;
import rmi.rmiConnection;

import java.rmi.RemoteException;

/**
 * Created by Rui Pedro Dias on 12/12/2016.
 */
public class BidBean {

    private RmiInterface rmiInterface;
    private rmiConnection rmiC;

    public Bid getBid() {
        return bid;
    }

    public void setBid(Bid bid) {
        this.bid = bid;
    }

    private Bid bid;

    public BidBean(){
        rmiC = new rmiConnection();
        rmiInterface = rmiC.getInterface();
    }

    public boolean bid() throws RemoteException {
        rmiC = new rmiConnection();
        rmiInterface = rmiC.getInterface();
        return rmiInterface.bid(this.bid);
    }

}
