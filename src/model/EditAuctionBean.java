package model;

import components.Auction;
import components.Client;
import rmi.RmiInterface;
import rmi.rmiConnection;

import java.rmi.RemoteException;
import java.sql.Timestamp;

/**
 * Created by Rui Pedro Dias on 13/12/2016.
 */
public class EditAuctionBean {

    //Parte do Editar auction
    private Auction antigo = null;
    private Auction novo = null;
    private String idAuction;

    private String idItem;
    private String title;
    private String description;
    private Timestamp deadline;
    private float amount;
    private int idUser;

    // Parte do cliente
    private Client client = null;
    private String username;
    private String password;
    private int userID;

    // Parte do RMI
    private RmiInterface rmiInterface;
    private rmiConnection rmiC;

    public EditAuctionBean(){
        rmiC = new rmiConnection();
        rmiInterface = rmiC.getInterface();
    }

    public boolean editAuction() throws RemoteException {
        rmiC = new rmiConnection();
        rmiInterface = rmiC.getInterface();
        client = rmiInterface.getClient(this.username, this.password);
        return rmiInterface.editAuction(antigo, novo, client);
    }

    public void detailAuction() throws RemoteException {
        rmiInterface = rmiC.getInterface();
        this.setAntigo(rmiInterface.detailAuction(this.idAuction));
    }

    public Auction getAntigo() {
        return antigo;
    }

    public void setAntigo(Auction antigo) {
        this.antigo = antigo;
    }

    public Auction getNovo() {
        return novo;
    }

    public void setNovo(Auction novo) {
        this.novo = novo;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getIdAuction() {
        return idAuction;
    }

    public void setIdAuction(String idAuction) {
        this.idAuction = idAuction;
    }

    public String getIdItem() {
        return idItem;
    }

    public void setIdItem(String idItem) {
        this.idItem = idItem;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getDeadline() {
        return deadline;
    }

    public void setDeadline(Timestamp deadline) {
        this.deadline = deadline;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }


}
