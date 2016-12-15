package actions;

import com.opensymphony.xwork2.ActionSupport;
import components.Auction;
import components.Client;
import model.EditAuctionBean;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.SessionAware;

import javax.servlet.http.HttpServletRequest;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.Map;

/**
 * Created by Rui Pedro Dias on 14/12/2016.
 */
public class EditAuctionAction extends ActionSupport implements SessionAware{

    private static final long serialVersionUID = 4L;
    private Map<String, Object> session;
    private String title;
    private String idItem;
    private String description;
    private String deadline;
    private float amount;
    private int idUser;
    private String username;
    private String password;
    private Client client;
    private String auctionId;


    @Override
    public String execute() throws RemoteException {

        HttpServletRequest request = ServletActionContext.getRequest();
        this.auctionId = request.getParameter("auctionId");
        this.session.put("auctionId",this.auctionId);
        //this.auctionId = String.valueOf(session.get("auctionId"));
        this.getEditAuctionBean().setIdAntigo(this.auctionId);
        this.getEditAuctionBean().setAuctionAntigo();

        this.username = String.valueOf(session.get("username"));
        this.password = String.valueOf(session.get("password"));
        this.idUser = Integer.parseInt(String.valueOf(session.get("userID")));

        client = new Client(this.idUser, this.username, this.password);
        this.getEditAuctionBean().setClient(client);

        Auction novo = new Auction(this.idItem, this.title, this.description, Timestamp.valueOf(this.deadline), this.amount, this.idUser);
        this.getEditAuctionBean().setNovo(novo);

        if (this.getEditAuctionBean().editAuction()){
            return SUCCESS;
        }

        return ERROR;

    }


    @Override
    public void setSession(Map<String, Object> map) {
        this.session = map;
    }

    public void setEditAuctionBean(EditAuctionBean editAuctionBean){
        this.session.put("editAuctionBean", editAuctionBean);
    }

    public EditAuctionBean getEditAuctionBean(){
        if (!session.containsKey("editAuctionBean")){
            this.setEditAuctionBean(new EditAuctionBean());
        }
        return (EditAuctionBean) session.get("editAuctionBean");
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIdItem() {
        return idItem;
    }

    public void setIdItem(String idItem) {
        this.idItem = idItem;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
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

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public String getAuctionId(){
        return auctionId;
    }

    public void setAuctionId(String auctionId){
        this.auctionId = auctionId;
    }

}
