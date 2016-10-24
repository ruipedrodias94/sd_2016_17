package rmi;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import java.sql.Date;
import java.util.HashMap;

import components.Auction;
import components.Client;
import database.*;
import helpers.ProtocolParser;


public class RmiServer extends UnicastRemoteObject implements RmiInterface {

    public static ConnectDatabase connectDatabase;
    public static RmiServer rmiServer;


    public RmiServer() throws RemoteException {
        connectDatabase = new ConnectDatabase();
    }

    // add some functional methods here

    /**
     * Criar uma nova conta de cliente
     * @param userName
     * @param password
     * @return
     */

    public synchronized String registerClient(String userName, String password){

        String add = "insert into USER (userName, password, online) values ('"+userName+"', '"+ password+"', "+ 0+");";
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
        String search = "select * from USER where userName ='" + userName + "'and password='" + password +"';";
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

    public String searchOnlineUsers(){
        String search = "select * from USER where online = 1;";
        String result = "";
        ArrayList<Client> clients = new ArrayList<>();
        Client client;

        try{
            connectDatabase.resultSet = connectDatabase.statement.executeQuery(search);
            if (!connectDatabase.resultSet.next()){
                result = "type: online_users, items_count: 0";
            }
            while (connectDatabase.resultSet.next()){
                client = new Client(connectDatabase.resultSet.getString(2), connectDatabase.resultSet.getString(3));
                clients.add(client);
            }

            result = "type: online_users, items_count: " + clients.size();

            String result2 = "";
            for (int i = 0; i < clients.size(); i++) {
                result2 = result2 + ", users_"+i+"_username: " + clients.get(i).getUserName();
            }
            result = result + result2;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }


    public String createAuction(String command, int userId){

        String result = "";


        // Intrepreta o comando
        HashMap<String, String> m = ProtocolParser.parse(command);

        try {

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
            Date parsedDate = (Date) dateFormat.parse(m.get("deadline"));
            Timestamp timestamp = new java.sql.Timestamp(parsedDate.getTime());
            String add = "insert into AUCTION(idItem, title, description, deadline, amount, USER_idUSER) values('"
                    + Double.valueOf(m.get("code")) + "', '" + m.get("title")+ "', '" +m.get("description") +"', '"
                    + timestamp + "', '" + Integer.parseInt(m.get("amount"))+ "');";

            connectDatabase.statement.executeUpdate(add);
            connectDatabase.commit();
            result = "type: create_auction, ok: true";
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                connectDatabase.connection.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        } catch (ParseException e) {
            e.printStackTrace();
            result = "type: create_auction, ok: false";
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
