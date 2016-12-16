package actions;

import com.github.scribejava.apis.FacebookApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.oauth.OAuth20Service;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Random;
import model.LoginFacebookBean;

import java.net.HttpURLConnection;

/**
 * Created by Rui Pedro Dias on 15/12/2016.
 */
public class LoginFacebookAction extends ActionSupport implements SessionAware, ServletRequestAware, ServletResponseAware{

    private static final long serialVersionUID = 4L;
    private Map<String, Object> session;
    private HttpServletRequest httpServletRequest;
    private HttpServletResponse httpServletResponse;
    private String authUrl;

    @Override
    public String execute()
    {
        final String clientId = "1692489987709601";
        final String clientSecret = "d3b58f3dfa1180d96dc4e41453d313c6";
        final String secretState = "secret" + new Random().nextInt(999_999);
        final OAuth20Service service = new ServiceBuilder()
                .apiKey(clientId)
                .apiSecret(clientSecret)
                .state(secretState)
                .callback("http://localhost:8080/login.action/  ")
                .build(FacebookApi.instance());

        this.authUrl = service.getAuthorizationUrl();

        this.getLoginFBBean().setAuthUrl(this.authUrl);
        return SUCCESS;

    }
    @Override
    public void setServletRequest(HttpServletRequest httpServletRequest) {
        this.httpServletRequest = httpServletRequest;
    }

    @Override
    public void setServletResponse(HttpServletResponse httpServletResponse) {
        this.httpServletResponse = httpServletResponse;
    }

    @Override
    public void setSession(Map<String, Object> map) {
        this.session = map;
    }

    public String getAuthUrl() {
        return authUrl;
    }

    public void setAuthUrl(String authUrl) {
        this.authUrl = authUrl;
    }


    public LoginFacebookBean getLoginFBBean(){
        if (!session.containsKey("FBloginBean")){
            this.setLoginBean(new LoginFacebookBean());
        }
        return (LoginFacebookBean) session.get("FBloginBean");
    }

    public void setLoginBean(LoginFacebookBean loginFBBean){
        this.session.put("loginFBBean", loginFBBean);
    }

}

