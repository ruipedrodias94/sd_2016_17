package actions;

import com.opensymphony.xwork2.ActionSupport;
import components.Auction;
import model.EditAuctionBean;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.SessionAware;

import javax.persistence.criteria.CriteriaBuilder;
import javax.servlet.http.HttpServletRequest;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.Map;

/**
 * Created by Rui Pedro Dias on 13/12/2016.
 */
public class EditAuctionAction extends ActionSupport implements SessionAware {

    private static final long serialVersionUID = 4L;
    private Map<String, Object> session;

    private Auction novo = null;
    private String auctionID;

    private int idUser;
    private String title;
    private String idItem;
    private String description;
    private Timestamp deadline;
    private float amount;


    @Override
    public String execute() throws RemoteException{
        //Get auction ID
        HttpServletRequest request = ServletActionContext.getRequest();
        this.auctionID = request.getParameter("auctionId");

        //Para criação do antigo
        this.getEditAuctionBean().setIdAuction(this.auctionID);
        this.getEditAuctionBean().detailAuction();

        //Para criacão do cliente
        this.getEditAuctionBean().setUsername(String.valueOf(session.get("username")));
        this.getEditAuctionBean().setPassword(String.valueOf(session.get("password")));

        this.idUser = Integer.parseInt(String.valueOf(session.get("userID")));

        this.getEditAuctionBean().setTitle(this.title);
        this.getEditAuctionBean().setIdItem(this.idItem);
        this.getEditAuctionBean().setDescription(this.description);
        this.getEditAuctionBean().setDeadline(this.deadline);
        this.getEditAuctionBean().setAmount(this.amount);

        novo = new Auction(this.idItem, this.title, this.description, this.deadline, this.amount, this.idUser);

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

    public EditAuctionBean getEditAuctionBean(){
        if (!session.containsKey("editAuctionBean")){
            this.setEditAuctionBean(new EditAuctionBean());
        }
        return (EditAuctionBean) session.get("editAuctionBean");
    }

    public void setEditAuctionBean(EditAuctionBean editAuctionBean){
        this.session.put("editAuctionBean", editAuctionBean);
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setIdItem(String idItem) {
        this.idItem = idItem;
    }

    public String getIdItem() {
        return idItem;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDeadline(Timestamp deadline) {
        this.deadline = deadline;
    }

    public Timestamp getDeadline() {
        return deadline;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public float getAmount() {
        return amount;
    }
}
