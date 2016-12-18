package actions;

import com.opensymphony.xwork2.ActionSupport;
import model.MyAuctionsBean;
import org.apache.struts2.interceptor.SessionAware;

import java.rmi.RemoteException;
import java.util.Map;

/**
 * Created by jorgearaujo on 13/12/16.
 */
public class MyAuctionsAction extends ActionSupport implements SessionAware{

    private static final long serialVersionUID = 4L;
    private Map<String, Object> session;
    private String username;
    private String password;

    @Override
    public String execute() throws RemoteException
    {
        if(session.containsKey("username") && this.session.containsKey("password")){

            this.username = (String) session.get("username");
            this.password = (String) session.get("password");
            System.out.println("USERNAME: "+this.username);
            System.out.println("PASS: "+this.password);
            this.getMyAuctionsBean().setUsername(this.username);
            this.getMyAuctionsBean().setPassword(this.password);
            this.getMyAuctionsBean().getClient(this.username);
            this.getMyAuctionsBean().getMyAuctions();
            //this.getMyAuctionsBean().getMyAuctions();

        return SUCCESS;}
        else{
            System.out.println("nao contem");
        }
        return SUCCESS;
    }


    @Override
    public void setSession(Map<String, Object> session ) { this.session = session;}


    public MyAuctionsBean getMyAuctionsBean(){
        if (!session.containsKey("myAuctionsBean")){
            this.setMyAuctionsBean(new MyAuctionsBean());
        }
        return (MyAuctionsBean) session.get("myAuctionsBean");
    }

    public void setMyAuctionsBean(MyAuctionsBean myAuctionsBean){
        this.session.put("myAuctionsBean", myAuctionsBean);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
