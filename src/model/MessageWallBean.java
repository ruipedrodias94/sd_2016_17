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
            for(int i=0; i<messages.size();i++)
            {
                System.out.println(messages.get(i).getText());
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
