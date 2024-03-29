package actions;

import com.opensymphony.xwork2.ActionSupport;
import facebook.FacebookTest;
import model.LoginBean;
import org.apache.struts2.interceptor.SessionAware;

import java.rmi.RemoteException;
import java.util.Map;

import java.util.Random;
import java.util.Scanner;
import com.github.scribejava.apis.FacebookApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.*;
import com.github.scribejava.core.oauth.OAuth20Service;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Created by Rui Pedro Dias on 08/12/2016.
 */

//Every action must have a execute method

public class LoginAction extends ActionSupport implements SessionAware {

    private static final long serialVersionUID = 4L;
    private Map<String, Object> session;
    private String username;
    private String password;
    private String code;


    @Override
    public String execute() throws RemoteException{
        session.clear();
        if(this.username != null && !username.equals("") && this.password != null && !password.equals("")) {

            this.getLoginBean().setUsername(this.username);
            this.getLoginBean().setPassword(this.password);

            if (this.getLoginBean().doLogin()){
                session.put("username", username);
                session.put("password", password);
                session.put("userID", this.getLoginBean().userID());
                session.put("loggedin", true);
                return SUCCESS;
            }
            else return ERROR;
        }
        else
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
    public void setSession(Map<String, Object> session) {
        this.session = session;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
