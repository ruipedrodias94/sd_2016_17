package actions;

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
import model.AssociateAcountBean;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.util.Map;
import java.util.Random;

/**
 * Created by Rui Pedro Dias on 17/12/2016.
 */
public class AssociateAcountAction extends ActionSupport implements SessionAware, ServletRequestAware, ServletResponseAware {

    private static final long serialVersionUID = 4L;
    private Map<String, Object> session;

    private HttpServletRequest httpServletRequest;
    private HttpServletResponse httpServletResponse;


    private String code;

    private OAuth2AccessToken oAuth2AccessToken;
    private OAuthRequest oAuthRequest;

    public String getIdFacebook() {
        return idFacebook;
    }

    public void setIdFacebook(String idFacebook) {
        this.idFacebook = idFacebook;
    }

    private String idFacebook;

    private static final String PROTECTED_RESOURCE_URL = "https://graph.facebook.com/v2.8/me";


    @Override
    public String execute() throws IOException {
        final String clientId = "1692489987709601";
        final String clientSecret = "d3b58f3dfa1180d96dc4e41453d313c6";
        final String secretState = "secret" + new Random().nextInt(999_999);
        final OAuth20Service service = new ServiceBuilder()
                .apiKey(clientId)
                .apiSecret(clientSecret)
                .state(secretState)
                .callback("http://localhost:8080/associateAcount.action")
                .build(FacebookApi.instance());


        HttpServletRequest r = ServletActionContext.getRequest();
        this.code = r.getParameter("code");

        this.oAuth2AccessToken = service.getAccessToken(this.code);

        this.oAuthRequest = new OAuthRequest(Verb.GET, PROTECTED_RESOURCE_URL, service.getConfig());

        service.signRequest(this.oAuth2AccessToken, oAuthRequest);

        Response response = oAuthRequest.send();

        this.idFacebook = getId(response);

        this.getAssociateAcountBean().setIdFacebook(this.idFacebook);

        String idUser = String.valueOf(this.session.get("userID"));

        this.getAssociateAcountBean().setIdUser(Integer.parseInt(idUser));

        if (this.getAssociateAcountBean().associateAcount()){
            return SUCCESS;
        }
        return ERROR;
    }

    public AssociateAcountBean getAssociateAcountBean(){
        if (!session.containsKey("associateBean")){
            this.setAssociateAcountBean(new AssociateAcountBean());
        }
        return (AssociateAcountBean) session.get("associateBean");
    }

    public void setAssociateAcountBean(AssociateAcountBean associateAcountBean){
        this.session.put("associateBean", associateAcountBean);
    }


    public String getId(Response response) throws IOException {
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        JsonNode idNode = jsonNode.get("id");
        return idNode.asText();
    }

    @Override
    public void setSession(Map<String, Object> map) {
        this.session = map;
    }

    @Override
    public void setServletRequest(HttpServletRequest httpServletRequest) {
        this.httpServletRequest = httpServletRequest;
    }

    @Override
    public void setServletResponse(HttpServletResponse httpServletResponse) {
        this.httpServletResponse = httpServletResponse;
    }
}
