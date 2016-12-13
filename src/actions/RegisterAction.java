package actions;

import com.opensymphony.xwork2.ActionSupport;
import model.RegisterBean;
import org.apache.struts2.interceptor.SessionAware;
import java.rmi.RemoteException;
import java.util.Map;

/**
 * Created by jorgearaujo on 12/12/16.
 */
public class RegisterAction extends ActionSupport implements SessionAware {

    private static final long serialVersionUID = 4L;
    private Map<String, Object> session;
    private String username;
    private String password;

    @Override
    public String execute() throws RemoteException{



        this.getRegisterBean().setUsername(this.username);
        this.getRegisterBean().setPassword(this.password);

        if (this.getRegisterBean().registerUser()){
            session.put("username", username);
            session.put("password", password);

            //session.put("loggedin", true); // this marks the user as logged in
            return SUCCESS;
        }
        else return ERROR;
    }


    public RegisterBean getRegisterBean(){
        if (!session.containsKey("registerBean")){
            this.setRegisterBean(new RegisterBean());
        }
        return (RegisterBean) session.get("registerBean");
    }

    public void setRegisterBean(RegisterBean registerBean){
        this.session.put("registerBean", registerBean);
    }

    @Override
    public void setSession(Map<String, Object> session) {this.session=session;}

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {this.password = password;}
    }

