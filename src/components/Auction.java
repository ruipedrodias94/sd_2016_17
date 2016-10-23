package components;


import java.sql.Date;

public class Auction {

    private String id;
    private String title;
    private String description;
    private Date deadline;
    private int amount;
    private Client client;
    private Item item;

    public Auction(String id, String title, String description, Date deadline, int amount, Client client, Item item){
        this.id = id;
        this.title = title;
        this.description = description;
        this.deadline = deadline;
        this.amount = amount;
        this.client = client;
        this.item = item;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }
}
