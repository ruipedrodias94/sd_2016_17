package model;

import rmi.RmiInterface;

import rmi.rmiConnection;

import java.io.IOException;


/**
 * Created by Rui Pedro Dias on 17/12/2016.
 */
public class AssociateAcountBean {

    public String getIdFacebook() {
        return idFacebook;
    }

    public void setIdFacebook(String idFacebook) {
        this.idFacebook = idFacebook;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    private String idFacebook;
    private int idUser;

    private RmiInterface rmiInterface;
    private rmiConnection rmiConnection;

    public AssociateAcountBean() {
        rmiConnection = new rmiConnection();
        rmiInterface = rmiConnection.getInterface();
    }

    public boolean associateAcount() throws IOException {
        rmiConnection = new rmiConnection();
        rmiInterface = rmiConnection.getInterface();

        if (!rmiInterface.verifyIfExist(this.idFacebook)){
            rmiInterface.associateFacebook(this.idUser, this.idFacebook);
            return true;
        }
        return false;
    }
}
