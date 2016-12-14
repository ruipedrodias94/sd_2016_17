package model;

import components.Auction;
import components.Client;
import rmi.RmiInterface;
import rmi.rmiConnection;

import java.rmi.RemoteException;

/**
 * Created by Rui Pedro Dias on 14/12/2016.
 */
public class EditAuctionBean {
    private RmiInterface rmiInterface;
    private rmiConnection rmiConnection;


    private Auction antigo;
    private String idAntigo;
    private Auction novo;

    private Client client;

    public Auction getAntigo() {

        return antigo;
    }

    public void setAntigo(Auction antigo) {
        this.antigo = antigo;
    }

    public String getIdAntigo() {
        return idAntigo;
    }

    public void setIdAntigo(String idAntigo) {
        this.idAntigo = idAntigo;
    }

    public Auction getNovo() {
        return novo;
    }

    public void setNovo(Auction novo) {
        this.novo = novo;
    }

    public void setClient(Client client){
        this.client = client;
    }

    public Client getClient(){
        return client;
    }

    public EditAuctionBean(){
        rmiConnection = new rmiConnection();
        rmiInterface = rmiConnection.getInterface();
    }

    public boolean editAuction() throws RemoteException {
        rmiConnection = new rmiConnection();
        rmiInterface = rmiConnection.getInterface();
        this.novo.setIdAuction(this.antigo.getIdAuction());
        boolean ratedo = rmiInterface.editAuction(this.antigo, this.novo, this.client);
        return ratedo;
    }

    public void setAuctionAntigo() throws RemoteException {
        rmiConnection = new rmiConnection();
        rmiInterface = rmiConnection.getInterface();
        this.antigo = rmiInterface.detailAuction(this.idAntigo);
    }

}
