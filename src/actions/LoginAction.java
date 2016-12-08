package actions;

import com.opensymphony.xwork2.ActionSupport;
import model.LoginBean;
import org.apache.struts2.interceptor.SessionAware;

import java.rmi.RemoteException;
import java.util.Map;

/**
 * Created by Rui Pedro Dias on 08/12/2016.
 */

//Every action must have a execute method

public class LoginAction extends ActionSupport implements SessionAware {

    private static final Long serialVersionUID = 4L;
    private Map<String, Object> session;
    private String username = null;
    private String password = null;

    @Override
    public String execute() throws RemoteException{
        //Caso o recebido não seja nulo
        if (this.username != null && !username.equals("") && this.password!= null && !password.equals("")){
            //Vai receber as cenas do jsp?
            this.getLoginBean().setUsername(this.username);
            this.getLoginBean().setPassword(this.password);
            //Verifica o login
            if (this.getLoginBean().doLogin()){
                session.put("username", username);
                session.put("loggedin", true);
                return SUCCESS;
            }
            return ERROR;
        }
        return ERROR;
    }

    public LoginBean getLoginBean(){
        if (!session.containsKey("loginBean")){
            this.setLoginBean(new LoginBean());
        }
        return (LoginBean) session.get("loginBean");
    }

    public void setLoginBean(LoginBean loginBean){
        this.session.put("loginBean", loginBean);
    }

    @Override
    public void setSession(Map<String, Object> map) {
        this.session = map;
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
