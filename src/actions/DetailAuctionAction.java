package actions;

import com.opensymphony.xwork2.ActionSupport;
import components.Auction;
import model.DetailAuctionBean;
import org.apache.struts2.interceptor.SessionAware;

import java.util.Map;

/**
 * Created by Rui Pedro Dias on 10/12/2016.
 */
public class DetailAuctionAction extends ActionSupport implements SessionAware {

    private static final long serialVersionUID = 4L;
    private Map<String, Object> session;
    private String code;
    private Auction auction = null;

    @Override
    public String execute(){
        if (this.code!=null && !this.code.equals("")){
            this.getDetailAuctionBean().setCode(this.code);
            auction = this.getDetailAuctionBean().detailAuction();
            if (auction != null) {
                this.getDetailAuctionBean().setAuction(this.auction);
                return SUCCESS;
            }
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
}
