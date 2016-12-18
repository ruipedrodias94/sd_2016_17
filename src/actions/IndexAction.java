package actions;

import com.github.scribejava.apis.FacebookApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.oauth.OAuth20Service;
import com.opensymphony.xwork2.ActionSupport;
import model.IndexBean;
import org.apache.struts2.interceptor.SessionAware;

import java.util.Map;
import java.util.Random;

/**
 * Created by Rui Pedro Dias on 16/12/2016.
 */
public class IndexAction extends ActionSupport implements SessionAware {

    private static final long serialVersionUID = 4L;
    private Map<String, Object> session;
    private String authURL;

    public String getAuthURL2() {
        return authURL2;
    }

    public void setAuthURL2(String authURL2) {
        this.authURL2 = authURL2;
    }

    private String authURL2;

    private static final String NETWORK_NAME = "Facebook";
    private static final String PROTECTED_RESOURCE_URL = "https://graph.facebook.com/v2.8/me";

    private String state;
    private String code;



    @Override
    public String execute(){

        final String clientId = "1692489987709601";
        final String clientSecret = "d3b58f3dfa1180d96dc4e41453d313c6";
        final String secretState = "secret" + new Random().nextInt(999_999);
        final OAuth20Service service = new ServiceBuilder()
                .apiKey(clientId)
                .apiSecret(clientSecret)
                .state(secretState)
                .callback("http://localhost:8080/loginFBAction.action")
                .build(FacebookApi.instance());

        final OAuth20Service service2 = new ServiceBuilder()
                .apiKey(clientId)
                .apiSecret(clientSecret)
                .state(secretState)
                .callback("http://localhost:8080/associateAcount.action")
                .build(FacebookApi.instance());

        this.authURL = service.getAuthorizationUrl();
        this.authURL2 = service2.getAuthorizationUrl();

        this.getIndexBean().setAuthUrl(this.authURL);
        this.getIndexBean().setAuthURL2(this.authURL2);
        return SUCCESS;
    }

    public IndexBean getIndexBean(){
        if (!session.containsKey("indexBean")){
            this.setIndexBean(new IndexBean());
        }
        return (IndexBean) session.get("indexBean");
    }

    public void setIndexBean(IndexBean indexBean){
        this.session.put("indexBean", indexBean);
    }

    @Override
    public void setSession(Map<String, Object> map) {
        this.session = map;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
