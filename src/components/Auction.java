package components;


import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;

public class Auction implements Serializable{

    private int idAuction;
    private String idItem;
    private String title;
    private String description;
    private Timestamp deadline;
    private int amount;
    private int idUser;
    private ArrayList<Message> messages;
    private ArrayList<Bid> bids;


    public Auction(int idAuction, String idItem, String title, String description, Timestamp deadline, int amount, int idUser, ArrayList<Message> messages, ArrayList<Bid> bids) {
        this.idAuction = idAuction;
        this.idItem = idItem;
        this.title = title;
        this.description = description;
        this.deadline = deadline;
        this.amount = amount;
        this.idUser = idUser;
        this.messages = messages;
        this.setBids(bids);
    }

    public Auction( String idItem, String title, String description, Timestamp deadline, int amount) {
        this.idItem = idItem;
        this.title = title;
        this.description = description;
        this.deadline = deadline;
        this.amount = amount;
    }

    public Auction( String idItem, String title, String description, Timestamp deadline, int amount, int idUser) {
        this.idItem = idItem;
        this.title = title;
        this.description = description;
        this.deadline = deadline;
        this.amount = amount;
        this.idUser = idUser;
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

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getIdItem() {
        return idItem;
    }

    public void setIdItem(String idItem) {
        this.idItem = idItem;
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

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<Message> messages) {
        this.messages = messages;
    }

    public ArrayList<Bid> getBids() {
        return bids;
    }

    public void setBids(ArrayList<Bid> bids) {
        this.bids = bids;
    }
}
