package model;

import components.Auction;
import rmi.RmiInterface;

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
    private Auction auction = null;
    private String code;

    public DetailAuctionBean(){
        try {
            rmiInterface = (RmiInterface) LocateRegistry.getRegistry("localhost", 2080).lookup("rmi_server");
        } catch (NotBoundException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public Auction detailAuction(){
        try{
            auction = rmiInterface.detailAuction(this.code);
            return auction;
        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setCode(String code){
        this.code = code;
    }

    public void setAuction(Auction auction){
        this.auction = auction;
    }
}
