package actions;

import com.opensymphony.xwork2.ActionSupport;
import components.Auction;
import components.Bid;
import model.DetailAuctionBean;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.SessionAware;

import javax.servlet.http.HttpServletRequest;
import java.rmi.RemoteException;
import java.security.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;


/**
 * Created by Rui Pedro Dias on 10/12/2016.
 */
public class DetailAuctionAction extends ActionSupport implements SessionAware {

    private static final long serialVersionUID = 4L;
    private Map<String, Object> session;
    private String code;
    private Auction auction = null;
    private String auctionId;
    private boolean isActive;

    @Override
    public String execute() throws RemoteException {

            HttpServletRequest request = ServletActionContext.getRequest();
            this.code = request.getParameter("auctionId");

            System.out.println("CODE1: "+this.code);



                this.session.put("auctionId",this.code);

            this.getDetailAuctionBean().setAuctionId(this.code);
            auction = getDetailAuctionBean().detailAuction();
            if (auction != null) {
                this.getDetailAuctionBean().setAuction(this.auction);
                Date date = new Date();
                java.sql.Timestamp now = new java.sql.Timestamp(date.getTime());

                if(auction.getDeadline().before(now))
                {
                    getDetailAuctionBean().setActive(false);
                }
                else
                {
                    getDetailAuctionBean().setActive(true);
                }
                return SUCCESS;
            }
        return ERROR;
    }

    @Override
    public void setSession(Map<String, Object> map) {
        this.session = map;
    }

    public DetailAuctionBean getDetailAuctionBean(){
        if (!session.containsKey("detailAuctionBean")){
            this.setDetailAuctionBean(new DetailAuctionBean());
        }
        return (DetailAuctionBean) session.get("detailAuctionBean");
    }

    public void setDetailAuctionBean(DetailAuctionBean detailAuctionBean){
        this.session.put("detailAuctionBean", detailAuctionBean);
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setAuction(Auction auction) {
        this.auction = auction;
    }


    public String getAuctionId() {
        return auctionId;
    }

    public void setAuctionId(String auctionId) {
        this.auctionId = auctionId;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
