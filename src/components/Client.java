package components;

import java.io.Serializable;

public class Client implements Serializable{

    private int idUser;
    private String userName;
    private String password;

    public Client(int idUser, String userName, String password){
        this.idUser = idUser;
        this.userName = userName;
        this.password = password;
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
}
