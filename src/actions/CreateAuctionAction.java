package actions;

import com.opensymphony.xwork2.ActionSupport;
import components.Auction;
import model.CreateAuctionBean;
import org.apache.struts2.interceptor.SessionAware;

import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.Map;

/**
 * Created by Rui Pedro Dias on 09/12/2016.
 */
public class CreateAuctionAction extends ActionSupport implements SessionAware{

    private static final long serialVersionUID = 4L;
    private Map<String, Object> session;

    private Auction auction;
    private String idItem;
    private String title;
    private String description;
    private Timestamp deadline;
    private float amount;
    private int idUser;

    @Override
    public void setSession(Map<String, Object> map) {
        this.session = map;
    }

    @Override
    public String execute() throws RemoteException {

        this.getCreateAuctionBean().setTitle(this.title);
        this.getCreateAuctionBean().setIdItem(this.idItem);
        this.getCreateAuctionBean().setDescription(this.description);
        this.getCreateAuctionBean().setDeadline(this.deadline);
        this.getCreateAuctionBean().setAmount(this.amount);

        this.idUser = Integer.parseInt(String.valueOf(session.get("userID")));
        this.getCreateAuctionBean().setIdUser(this.idUser);

        System.out.println("ID DO USER: " + idUser);

        auction = new Auction(this.idItem, this.title, this.description, this.deadline, this.amount, this.idUser);

        this.getCreateAuctionBean().setAuction(auction);

        if (this.getCreateAuctionBean().createAuction()){
            return SUCCESS;
        }

        return ERROR;
    }

    public CreateAuctionBean getCreateAuctionBean(){
        if (!session.containsKey("createAuctionBean")){
            this.setCreateAuctionBean(new CreateAuctionBean());
        }
        return (CreateAuctionBean) session.get("createAuctionBean");
    }

    public void setCreateAuctionBean(CreateAuctionBean createAuctionBean){
        this.session.put("createAuctionBean", createAuctionBean);
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
}
