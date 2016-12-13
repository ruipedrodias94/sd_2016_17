package actions;

import com.opensymphony.xwork2.ActionSupport;
import model.MessageWallBean;
import org.apache.struts2.interceptor.SessionAware;

import java.rmi.RemoteException;
import java.util.Map;

/**
 * Created by jorgearaujo on 13/12/16.
 */
public class MessageWallAction extends ActionSupport implements SessionAware {

    private static final long serialVersionUID = 4L;
    private Map<String, Object> session;
    private int auctionId;

    @Override
    public String execute() throws RemoteException
    {
        if(session.containsKey("auctionId")){
            this.getMessageWallBean().setAuctionId(Integer.parseInt((String)session.get("auctionId")));
            this.getMessageWallBean().getMessagesAuction();
            return SUCCESS;
        }
        else{
            System.out.println("nao contem");
            return ERROR;
        }
    }


    public Map<String, Object> getSession() {
        return session;
    }

    @Override
    public void setSession(Map<String, Object> session) {
        this.session = session;
    }

    public void setMessageWallBean(MessageWallBean messageWallBean){
        this.session.put("messageWallBean", messageWallBean);
    }

    public MessageWallBean getMessageWallBean(){
        if (!session.containsKey("messageWallBean")){
            this.setMessageWallBean(new MessageWallBean());
        }
        return (MessageWallBean) session.get("messageWallBean");
    }

    public int getAuctionId() {
        return auctionId;
    }

    public void setAuctionId(int auctionId) {
        this.auctionId = auctionId;
    }
}
