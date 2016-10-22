package rmi;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Date;

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

    // add some functional methods here

    /**
     * Criar uma nova conta de cliente
     * @param name
     * @param userName
     * @param password
     * @return
     */

    public boolean registerClient(String name, String userName, String password){

        return true;
    }

    /**
     * Fazer o login
     * @param userName
     * @param password
     * @return
     */

    public boolean doLogin(String userName, String password){

        return true;
    }


    /**
     * Criar um leilao
     * @param code
     * @param title
     * @param descritption
     * @param deadline
     * @param amount
     * @return
     */
    public boolean createAuction(int code, String title, String descritption, Date deadline, int amount){

        return true;
    }

    /*
    public () searchAuction(int code){

        return auction;
    }

    public */


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
