package actions;

import com.opensymphony.xwork2.ActionSupport;
import components.Auction;
import model.CreateAuctionBean;
import org.apache.struts2.interceptor.SessionAware;

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
    public String execute(){
        //TODO mandar aqui grande tratamento da informação
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
}
