package rmi;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
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


    /**
     * Criar um leilao. Recebe a string, que é intrepertada pelo parser e adicionada a base de dados
     * @param command
     * @param userId
     * @return
     */

    public String createAuction(String command, int userId){

        String result = "";

        // Intrepreta o comando
        HashMap<String, String> m = ProtocolParser.parse(command);

        System.out.println(m.get("deadline"));

        try {
            String add = "insert into AUCTION(idItem, title, description, deadline, amount, USER_idUSER) values('"
                    + Double.valueOf(m.get("code")) + "', '" + m.get("title")+ "', '" +m.get("description") +"', '"
                    + Timestamp.valueOf(m.get("deadline")) + "', '" + Integer.parseInt(m.get("amount"))+ "');";

            connectDatabase.statement.executeUpdate(add);
            connectDatabase.commit();
            result = "type: create_auction, ok: true";
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
            result = "type: create_auction, ok: false";
            try {
                connectDatabase.connection.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
        return result;
    }

    /**
     * Procura o leilao por codigo do item
     * @param command
     * @return
     */

    public String searchAuction(String command){
        String result = "";
        ArrayList<Auction> auctions = new ArrayList<>();
        Auction auction;

        HashMap<String, String> m = ProtocolParser.parse(command);

        String code = m.get("code");

        String search = "select * from AUCTION where idITEM = " + code +";";

        try{
            connectDatabase.resultSet = connectDatabase.statement.executeQuery(search);
            if (!connectDatabase.resultSet.next()){
                result = "type: search_auction, items_count: 0";
            }
            while (connectDatabase.resultSet.next()){
                auction = new Auction(connectDatabase.resultSet.getInt(1), connectDatabase.resultSet.getInt(2),
                        connectDatabase.resultSet.getString(3), connectDatabase.resultSet.getString(4),
                        connectDatabase.resultSet.getDate(5), connectDatabase.resultSet.getInt(6), connectDatabase.resultSet.getInt(7));
                auctions.add(auction);
            }

            result = "type: search_auction, items_count: " + auctions.size();

            String result2 = "";

            for (int i = 0; i < auctions.size(); i++) {
                result2 = result2 + ", items_"+i+"_id: " + auctions.get(i).getIdAuction() + ", items_"+i+"_code: "
                        + auctions.get(i).getIdItem() + ", items_"+i+"_title: " + auctions.get(i).getTitle();
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
                Registry registry = LocateRegistry.createRegistry(1099);
                registry.rebind("rmi_server", rmiServer);
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
