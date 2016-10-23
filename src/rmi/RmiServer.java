package rmi;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.ArrayList;

import java.sql.Date;

import components.Auction;
import components.Client;
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

    public synchronized String registerClient(String name, String userName, String password){

        String add = "insert into Utilizador(nome, userName, pass) values ('"+name+"','"+userName+"','"+password+"');";
        String result = "";
        try {
            connectDatabase.statement.executeUpdate(add);
            connectDatabase.commit();
            result = "type: register, ok: true";
        } catch (SQLException e) {
            e.printStackTrace();
            result = "type: register, ok: false";
            try {
                connectDatabase.connection.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            return result;
        }
        return result;
    }


    /**
     * Fazer o login
     * @param userName
     * @param password
     * @return
     */

    public String doLogin(String userName, String password){
        String search = "select * from Utilizador where userName ='" + userName + "'and pass='" + password +"';";
        String result = "";
        try {
            connectDatabase.resultSet = connectDatabase.statement.executeQuery(search);
            while (connectDatabase.resultSet.next()){
                result = "type: login, ok: true";
                return result;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            result = "type: login, ok: false";
        }
        return result;
    }

    /**
     * Procurar os utilizadores, depois temos de ver como postar os online e os offline
     * @return
     */

    public String searchUsers(){
        String search = "select * from Utilizador;";
        String result = "";
        ArrayList<Client> clients = new ArrayList<>();
        Client client = null;

        try{
            connectDatabase.resultSet = connectDatabase.statement.executeQuery(search);
            if (!connectDatabase.resultSet.next()){
                result = "type: users_registered, items_count: 0";
            }
            while (connectDatabase.resultSet.next()){
                client = new Client(connectDatabase.resultSet.getString(2), connectDatabase.resultSet.getString(3), connectDatabase.resultSet.getString(4));
                clients.add(client);
            }

            result = "type: registered_users, item_count: " + clients.size();

            String result2 = "";
            for (int i = 0; i < clients.size(); i++) {
                result2 = result2 + ", item_"+i+"_name: " + clients.get(i).getName();
            }
            result = result + result2;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
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
