package rmi;

import tcp.CallbackInterface;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by jorgearaujo on 29/10/16.
 */
public interface ServerCallbackInterface extends Remote {
    public void print_on_server(String s) throws java.rmi.RemoteException;
    public void subscribe(String name, CallbackInterface client) throws RemoteException;
}
