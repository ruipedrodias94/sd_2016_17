package model;

import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import rmi.RmiInterface;
import rmi.rmiConnection;

/**
 * Created by Rui Pedro Dias on 15/12/2016.
 */
public class LoginFacebookBean {

    private String authUrl;
    private String code;
    private String secret;
    private OAuth2AccessToken oAuth2AccessToken;

    private OAuthRequest oAuthRequest;

    private RmiInterface rmiInterface;
    private rmiConnection rmiConnection;

    public LoginFacebookBean(){
        rmiConnection = new rmiConnection();
        rmiInterface = rmiConnection.getInterface();
    }

    public String getAuthUrl() {
        return authUrl;
    }

    public void setAuthUrl(String authUrl) {
        this.authUrl = authUrl;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public OAuth2AccessToken getoAuth2AccessToken() {
        return oAuth2AccessToken;
    }

    public void setoAuth2AccessToken(OAuth2AccessToken oAuth2AccessToken) {
        this.oAuth2AccessToken = oAuth2AccessToken;
    }

    public OAuthRequest getoAuthRequest() {
        return oAuthRequest;
    }

    public void setoAuthRequest(OAuthRequest oAuthRequest) {
        this.oAuthRequest = oAuthRequest;
    }



}
