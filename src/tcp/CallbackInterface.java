package tcp;

import components.Message;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by jorgearaujo on 29/10/16.
 */
public interface CallbackInterface extends Remote {
    public void printOnClient(Message m, String writer, int idUser) throws RemoteException;
}
