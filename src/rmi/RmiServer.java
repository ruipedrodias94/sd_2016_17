package rmi;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.ArrayList;

import java.sql.Date;

import database.*;


public class RmiServer extends UnicastRemoteObject implements RmiInterface {

    public static ConnectDatabase connectDatabase;
    public static RmiServer rmiServer;


    public RmiServer() throws RemoteException {
        connectDatabase = new ConnectDatabase();
    }

    // add some functional methods here

    /**
     * Criar uma nova conta de cliente
     * @param name
     * @param userName
     * @param password
     * @return
     */

    public synchronized boolean registerClient(String name, String userName, String password){

        String add = "insert into Utilizador(nome, userName, pass) values ('"+name+"','"+userName+"','"+password+"');";

        try {
            connectDatabase.statement.executeUpdate(add);
            connectDatabase.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                connectDatabase.connection.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            return false;
        }
        return true;
    }


    /**
     * Fazer o login
     * @param userName
     * @param password
     * @return
     */

    public boolean doLogin(String userName, String password){
        String search = "select * from Utilizador where userName ='" + userName + "'and pass='" + password +"';";

        try {
            connectDatabase.resultSet = connectDatabase.statement.executeQuery(search);
            while (connectDatabase.resultSet.next()){
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
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
            }
        }
    }
}
