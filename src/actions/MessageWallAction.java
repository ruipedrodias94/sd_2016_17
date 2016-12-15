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
    private String messageText;

    @Override
    public String execute() throws RemoteException
    {
        postMessage();
        if(session.containsKey("auctionId") && session.containsKey("userID")){
            this.getMessageWallBean().setAuctionId(Integer.parseInt((String)session.get("auctionId")));
            this.getMessageWallBean().getMessagesAuction();
            this.getMessageWallBean().setUserID((int)(session.get("userID")));
        }

        else{
            System.out.println("nao contem");
            return ERROR;
        }
        return SUCCESS;
    }


    public Map<String, Object> getSession() {
        return session;
    }

    public String postMessage()
    {
        if(messageText!=null) {
            if(!messageText.equals("")){
                this.getMessageWallBean().setMessageText(this.messageText);
                this.getMessageWallBean().postMessage();
                return SUCCESS;
            }
        }
        return ERROR;
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

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }
}
