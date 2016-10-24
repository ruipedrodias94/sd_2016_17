package components;


import java.sql.Date;

public class Auction {

    private int idAuction;
    private int idItem;
    private String title;
    private String description;
    private Date deadline;
    private int amount;
    private int idUser;


    public Auction(int idAuction, int idItem, String title, String description, Date deadline, int amount, int idUser) {
        this.setIdAuction(idAuction);
        this.setIdItem(idItem);
        this.title = title;
        this.description = description;
        this.deadline = deadline;
        this.amount = amount;
        this.setIdUser(idUser);
    }

    public int getId() {
        return getIdAuction();
    }

    public void setId(int id) {
        this.setIdAuction(id);
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

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getIdAuction() {
        return idAuction;
    }

    public void setIdAuction(int idAuction) {
        this.idAuction = idAuction;
    }

    public int getIdItem() {
        return idItem;
    }

    public void setIdItem(int idItem) {
        this.idItem = idItem;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }
}
