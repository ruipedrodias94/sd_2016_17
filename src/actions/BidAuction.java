package actions;

import com.opensymphony.xwork2.ActionSupport;
import components.Bid;
import model.BidBean;
import model.DetailAuctionBean;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.SessionAware;

import javax.servlet.http.HttpServletRequest;
import java.rmi.RemoteException;
import java.util.Map;

/**
 * Created by Rui Pedro Dias on 12/12/2016.
 */
public class BidAuction extends ActionSupport implements SessionAware {

    private static final long serialVersionUID = 4L;
    private Map<String, Object> session;



    private int amount;
    private Bid bid;
    private String idAuction;

    @Override
    public String execute() throws RemoteException {

        HttpServletRequest request = ServletActionContext.getRequest();
        this.idAuction = request.getParameter("idAuction");

        if (this.amount != 0){
            this.bid = new Bid(this.amount, Integer.parseInt(String.valueOf(session.get("userID"))), Integer.parseInt(this.idAuction));
            this.getBidBean().setBid(this.bid);
            if (this.getBidBean().bid()){
                return SUCCESS;
            }
        }
        return ERROR;
    }

    public BidBean getBidBean(){
        if (!session.containsKey("bidBean")){
            this.setBidBean(new BidBean());
        }
        return (BidBean) session.get("bidBean");
    }

    public void setBidBean(BidBean bidBean){
        this.session.put("bidBean", bidBean);
    }

    @Override
    public void setSession(Map<String, Object> map) {
        this.session = map;
    }

    public Map<String, Object> getSession() {
        return session;
    }

    public String getIdAuction() {
        return idAuction;
    }

    public void setIdAuction(String idAuction) {
        this.idAuction = idAuction;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
