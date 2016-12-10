package model;

import components.Auction;
import rmi.RmiInterface;

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

    public SearchAuctionBean(){
        try {
            rmiInterface = (RmiInterface) Naming.lookup("rmi_server");
        } catch (NotBoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Auction> searchAuction() throws RemoteException {
        return rmiInterface.searchAuction(this.code);
    }


    public void setRmiInterface(RmiInterface rmiInterface) {
        this.rmiInterface = rmiInterface;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
