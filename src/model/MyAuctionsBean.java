package model;

import components.Auction;
import components.Client;
import rmi.RmiInterface;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.ArrayList;

/**
 * Created by jorgearaujo on 13/12/16.
 */
public class MyAuctionsBean {

    private RmiInterface rmiInterface;
    //Things need to do the Register
    private String username;
    private String password;
    private ArrayList myAuctions = new ArrayList<Auction>();
    private Client client;


        public MyAuctionsBean(){
        try {
            rmiInterface = (RmiInterface) LocateRegistry.getRegistry("localhost", 2080).lookup("rmi_server");
            System.out.println("O RMI é " + rmiInterface);
        } catch (NotBoundException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }


    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public void setMyAuctions(ArrayList myAuctions) {
        this.myAuctions = myAuctions;
    }

    public void getClient(String user, String pass) throws RemoteException {


            this.client = rmiInterface.getClient(user,pass);
        System.out.println("User:"+client.getUserName()+" pass: "+client.getPassword());

    }

    public ArrayList getMyAuctions() throws RemoteException {
        this.setMyAuctions(rmiInterface.myAuctions(this.client));
        return this.myAuctions;
    }




}
