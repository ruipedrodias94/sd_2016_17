package actions;

import com.opensymphony.xwork2.ActionSupport;
import components.Auction;
import model.SearchAuctionBean;
import org.apache.struts2.interceptor.SessionAware;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Rui Pedro Dias on 10/12/2016.
 */
public class SearchAuctionAction extends ActionSupport implements SessionAware {

    private static final long serialVersionUID = 4L;
    private Map<String, Object> session;
    private String code;

    public ArrayList<Auction> getAuctions() {
        return auctions;
    }

    private ArrayList <Auction> auctions;


    @Override
    public String execute() throws RemoteException {
        if (this.code != null && !this.code.equals("")){
            this.getSearchAuctionBean().setCode(this.code);
            //Se existir alguma coisa no ArrayList
            if (this.getSearchAuctionBean().searchAuction()!=null){
                this.getSearchAuctionBean().setAuctions(this.getSearchAuctionBean().searchAuction());
                return SUCCESS;
            }
        }
        return ERROR;
    }

    public SearchAuctionBean getSearchAuctionBean(){
        if (!session.containsKey("searchAuctionBean")){
            this.setSearchAuctionBean(new SearchAuctionBean());
        }
        return (SearchAuctionBean) session.get("searchAuctionBean");
    }

    public void setSearchAuctionBean(SearchAuctionBean searchAuctionBean){
        this.session.put("searchAuctionBean", searchAuctionBean);
    }

    @Override
    public void setSession(Map<String, Object> map) {
        this.session = map;
    }


    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
