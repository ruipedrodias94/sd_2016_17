package rmi;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.ArrayList;

import database.*;


public class RmiServer extends UnicastRemoteObject implements RmiInterface {

    public static ConnectDatabase connectDatabase;
    public static RmiServer rmiServer;


    public RmiServer() throws RemoteException {
        connectDatabase = new ConnectDatabase();
    }

    public int teste() throws RemoteException{
        int a = 2;
        int b = 3;
        return a+b;

    }


    public ArrayList<String> test() throws RemoteException, SQLException {
        return connectDatabase.test();
    }

    public static void main(String[] args) throws RemoteException {

        rmiServer = new RmiServer();

        while(true){
            try {
                LocateRegistry.createRegistry(1099).rebind("rmi_server", rmiServer);
                System.out.println("Rmi Ligado");
                System.out.println("Servidor Primário");
                break;
            } catch (RemoteException e) {
                System.out.println("O resgisto está em uso. --> Servidor Secundário");
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                //e.printStackTrace();
            }
        }
    }
}
