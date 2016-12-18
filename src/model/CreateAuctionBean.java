package model;

import components.Auction;
import rmi.RmiInterface;
import rmi.RmiServer;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.sql.Timestamp;

/**
 * Created by Rui Pedro Dias on 09/12/2016.
 */
public class CreateAuctionBean {

    private RmiInterface rmiInterface;
    private Auction auction;
    //Tipo isto?
    private String idItem;

    private int idAuction;

    public Auction getAuction() {
        return auction;
    }

    public String getIdItem() {
        return idItem;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Timestamp getDeadline() {
        return deadline;
    }

    public float getAmount() {
        return amount;
    }

    public int getIdUser() {
        return idUser;
    }

    private String title;
    private String description;
    private Timestamp deadline;
    private float amount;
    private int idUser;

    public CreateAuctionBean(){

        try{
            rmiInterface = (RmiInterface) LocateRegistry.getRegistry("localhost", 2080).lookup("rmi_server");
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        }
    }


    public boolean createAuction() throws RemoteException {
        return this.rmiInterface.createAuction(this.auction);
    }

    public void setAuction(Auction auction) {
        this.auction = auction;
            }

    public void setIdItem(String idItem) {
        this.idItem = idItem;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDeadline(Timestamp deadline) {
        this.deadline = deadline;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public int getIdAuction() {
        return idAuction;
    }

    public void setIdAuction(int idAuction) {
        this.idAuction = idAuction;
    }
}
