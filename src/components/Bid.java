package components;

import java.io.Serializable;

public class Bid implements Serializable {

    private int idBid;
    private int amount;
    private int idUser;
    private int idAuction;

    public Bid(int idBid, int amount, int idUser, int idAuction) {
        this.setIdBid(idBid);
        this.setAmount(amount);
        this.setIdUser(idUser);
        this.setIdAuction(idAuction);
    }

    public int getIdBid() {
        return idBid;
    }

    public void setIdBid(int idBid) {
        this.idBid = idBid;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getIdUser() {
        return idUser;
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
