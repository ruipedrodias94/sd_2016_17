package actions;

import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.scribejava.apis.FacebookApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth20Service;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.text.View;
import java.io.IOException;
import java.util.Map;
import java.util.Random;
import model.LoginFacebookBean;



/**
 * Created by Rui Pedro Dias on 15/12/2016.
 */
public class LoginFacebookAction extends ActionSupport implements SessionAware, ServletRequestAware, ServletResponseAware{

    private static final long serialVersionUID = 4L;
    private Map<String, Object> session;
    private HttpServletRequest httpServletRequest;
    private HttpServletResponse httpServletResponse;
    private String authUrl;
    private String code;
    private String secret;
    private OAuth2AccessToken oAuth2AccessToken;
    private OAuthRequest oAuthRequest;
    private String state;

    private static final String NETWORK_NAME = "Facebook";
    private static final String PROTECTED_RESOURCE_URL = "https://graph.facebook.com/v2.4/me";
    private static final String PROTECTED_RESOURCE_URL2 = "https://graph.facebook.com/v2.4/1432086836832191";

    @Override
    public String execute() throws IOException {

        final String clientId = "1692489987709601";
        final String clientSecret = "d3b58f3dfa1180d96dc4e41453d313c6";
        final String secretState = "secret" + new Random().nextInt(999_999);
        final OAuth20Service service = new ServiceBuilder()
                .apiKey(clientId)
                .apiSecret(clientSecret)
                .state(secretState)
                .callback("http://localhost:8080/loginFBAction.action")
                .build(FacebookApi.instance());


        HttpServletRequest r = ServletActionContext.getRequest();
        this.code = r.getParameter("code");
        secret = r.getParameter("secret");



        this.getLoginFBBean().setCode(this.code);
        this.getLoginFBBean().setSecret(this.secret);

        this.oAuth2AccessToken = service.getAccessToken(this.code);

        this.getLoginFBBean().setoAuth2AccessToken(this.oAuth2AccessToken);

        this.oAuthRequest = new OAuthRequest(Verb.GET, PROTECTED_RESOURCE_URL, service.getConfig());

        this.getLoginFBBean().setoAuthRequest(this.oAuthRequest);

        this.getLoginFBBean().setoAuth20Service(service);

        service.signRequest(this.oAuth2AccessToken, oAuthRequest);

        Response response = oAuthRequest.send();

        String user = getUsername(response);

        this.session.put("username", user);

        System.out.println(response.getBody());

        this.oAuthRequest = new OAuthRequest(Verb.GET, PROTECTED_RESOURCE_URL2, service.getConfig());

        this.getLoginFBBean().setoAuthRequest(this.oAuthRequest);

        service.signRequest(this.oAuth2AccessToken, oAuthRequest);

        response = oAuthRequest.send();

        System.out.println(response.getBody());

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
        if (!session.containsKey("loginFBBean")){
            this.setLoginBean(new LoginFacebookBean());
        }
        return (LoginFacebookBean) session.get("loginFBBean");
    }

    public void setLoginBean(LoginFacebookBean loginFBBean){
        this.session.put("loginFBBean", loginFBBean);
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getUsername(Response response) throws IOException{
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        JsonNode idNode = jsonNode.get("name");
        return idNode.asText();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}

