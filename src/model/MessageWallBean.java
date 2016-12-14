package model;

import components.Message;
import rmi.RmiInterface;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.ArrayList;

/**
 * Created by jorgearaujo on 13/12/16.
 */
public class MessageWallBean {
    RmiInterface rmiInterface;
    int auctionId;
    ArrayList<Message> messages = new ArrayList<Message>();
    String messageText;
    int userID;
    public MessageWallBean(){
        try {
            rmiInterface = (RmiInterface) LocateRegistry.getRegistry("localhost", 2080).lookup("rmi_server");
            System.out.println("O RMI é " + rmiInterface);
        } catch (NotBoundException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }



    }



    public int getAuctionId() {
        return auctionId;
    }

    public void setAuctionId(int auctionId) {
        this.auctionId = auctionId;
    }

    public ArrayList getMessages() {
        return messages;
    }

    public void setMessages(ArrayList messages) {
        this.messages = messages;
    }

    public void getMessagesAuction()
    {
        try {

            messages = rmiInterface.getMessages(auctionId);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public boolean postMessage()
    {
        Message m = new Message(this.getMessageText(),0,this.getUserID(),this.auctionId);
        try {
            rmiInterface.message(m);
            return true;
        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        }
    }


    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }
}
