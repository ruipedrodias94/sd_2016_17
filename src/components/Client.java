package components;

import java.io.Serializable;

public class Client implements Serializable{

    private int idUser;
    private String userName;
    private String password;
    private String idFacebook;

    public Client(int idUser, String userName, String password){
        this.idUser = idUser;
        this.userName = userName;
        this.password = password;
    }

    public Client(int idUser, String userName, String password, String idFacebook){
        this.idUser = idUser;
        this.userName = userName;
        this.password = password;
        this. idFacebook = idFacebook;
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getIdFacebook(){return idFacebook;}

    public void setIdFacebook(String idFacebook){this.idFacebook = idFacebook;}
}
