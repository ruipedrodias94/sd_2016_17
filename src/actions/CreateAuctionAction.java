package actions;

import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.opensymphony.xwork2.ActionSupport;
import components.Auction;
import model.CreateAuctionBean;
import org.apache.struts2.interceptor.SessionAware;

import java.io.IOException;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.Map;
import model.LoginFacebookBean;

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
    private OAuthRequest oAuthRequest;
    private     String PROTECTED_RESOURCE_URL2;
    private OAuth2AccessToken oAuth2AccessToken;

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

        System.out.println("ID do cliente: "+this.session.get("userID"));
        if(this.session.get("idFacebook")!=null)
        {
            this.idUser = Integer.parseInt(String.valueOf(session.get("idFacebook")));
        }
        else{
            this.idUser = Integer.parseInt(String.valueOf(session.get("userID")));
        }
        this.getCreateAuctionBean().setIdUser(idUser);

        System.out.println("ID DO USER: " + idUser);

        auction = new Auction(this.idItem, this.title, this.description, this.deadline, this.amount, this.idUser);

        this.getCreateAuctionBean().setAuction(auction);

        if (this.getCreateAuctionBean().createAuction()){

            if(this.session.get("idFacebook")!=null){
            PROTECTED_RESOURCE_URL2 = "https://graph.facebook.com/v2.8/me/feed?message="+this.title;
           this.oAuth2AccessToken=this.getLoginFBBean().getoAuth2AccessToken();

            this.getLoginFBBean().setoAuth2AccessToken(this.oAuth2AccessToken);
            oAuthRequest = new OAuthRequest(Verb.POST,PROTECTED_RESOURCE_URL2,this.getLoginFBBean().getoAuth20Service().getConfig());
            this.getLoginFBBean().setoAuthRequest(oAuthRequest);
            this.getLoginFBBean().getoAuth20Service().signRequest(this.getLoginFBBean().getoAuth2AccessToken(),oAuthRequest);
            Response response = this.getLoginFBBean().getoAuthRequest().send();
            try {
                System.out.println(response.getBody());
            } catch (IOException e) {
                e.printStackTrace();
            }
            }
            return SUCCESS;
        }
        else
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

    public LoginFacebookBean getLoginFBBean(){
        if (!session.containsKey("loginFBBean")){
            this.setLoginBean(new LoginFacebookBean());
        }
        return (LoginFacebookBean) session.get("loginFBBean");
    }

    public void setLoginBean(LoginFacebookBean loginFBBean){
        this.session.put("loginFBBean", loginFBBean);
    }
}
