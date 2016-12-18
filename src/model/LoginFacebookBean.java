package model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.oauth.OAuth20Service;
import rmi.RmiInterface;
import rmi.rmiConnection;

import java.io.IOException;
import java.rmi.RemoteException;

/**
 * Created by Rui Pedro Dias on 15/12/2016.
 */
public class LoginFacebookBean {

    private String authUrl;
    private String code;
    private String secret;
    private OAuth2AccessToken oAuth2AccessToken;
    private String state;

    private OAuth20Service oAuth20Service;

    private OAuthRequest oAuthRequest;

    private RmiInterface rmiInterface;
    private rmiConnection rmiConnection;

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    private int idUser;

    private String idFacebook;
    private String username;

    public LoginFacebookBean(){
        rmiConnection = new rmiConnection();
        rmiInterface = rmiConnection.getInterface();

    }

    public boolean doLoginFacebook() throws IOException {
        rmiConnection = new rmiConnection();
        rmiInterface = rmiConnection.getInterface();

        this.oAuth20Service.signRequest(this.oAuth2AccessToken, this.oAuthRequest);
        Response response = oAuthRequest.send();
        this.setUsername(getUsername(response));
        this.setIdFacebook(getId(response));
        rmiInterface.loginFacebook(this.username, this.idFacebook);
        return true;
    }



    public int userID() throws RemoteException {
        return rmiInterface.getUserId(this.username);
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

    public OAuth20Service getoAuth20Service() {
        return oAuth20Service;
    }

    public void setoAuth20Service(OAuth20Service oAuth20Service) {
        this.oAuth20Service = oAuth20Service;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getIdFacebook() {
        return idFacebook;
    }

    public void setIdFacebook(String idFacebook) {
        this.idFacebook = idFacebook;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public String getUsername(Response response) throws IOException {
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        JsonNode idNode = jsonNode.get("name");
        return idNode.asText();
    }

    public String getId(Response response) throws IOException {
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        JsonNode idNode = jsonNode.get("id");
        return idNode.asText();
    }
}
