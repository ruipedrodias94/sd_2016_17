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
     * Registar Cliente
     * @param client
     * @return
     */

    public synchronized boolean registerClient(Client client){

        String add = "insert into USER (userName, password, online) values ('"+client.getUserName()+"', '"+ client.getPassword()+"', "+ 0+");";

        try {
            connectDatabase.statement.executeUpdate(add);
            connectDatabase.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                connectDatabase.connection.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            return false;
        }
    }


    /**
     * Fazer grande Login
     * @param client
     * @return
     */

    public boolean doLogin(Client client){
        String search = "select * from USER where userName ='" + client.getUserName()
                + "'and password='" + client.getPassword() +"';";

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
     * Procurar os utilizadores, depois temos de ver como postar os online e os offline
     * @return
     */

    public ArrayList<Client> searchOnlineUsers(){
        String search = "select * from USER where online = 1;";
        ArrayList<Client> clients = new ArrayList<>();
        Client client;

        try{
            connectDatabase.resultSet = connectDatabase.statement.executeQuery(search);

            while (connectDatabase.resultSet.next()){
                client = new Client(connectDatabase.resultSet.getString(2), connectDatabase.resultSet.getString(3));
                clients.add(client);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return clients;
    }

    /**
     * Cria leilao
     * @param auction
     * @return
     */
    public synchronized boolean createAuction(Auction auction){

        try{
            String add = "insert into AUCTION(idItem, title, description, deadline, amount, USER_idUSER) values('"
                    + auction.getIdItem()+ "', '" + auction.getTitle()+ "', '" + auction.getDescription() +"', '"
                    + auction.getDeadline() + "', '" + auction.getAmount()+ "', '" + auction.getIdUser() + "');";

            connectDatabase.statement.executeUpdate(add);
            connectDatabase.commit();

            return true;
        } catch (SQLException e) {
            e.printStackTrace();

            try {
                connectDatabase.connection.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            return false;
        }
    }

    /**
     * Procura o leilao por codigo do item
     * @param code
     * @return
     */

    public ArrayList<Auction> searchAuction(int code){

        ArrayList<Auction> auctions = new ArrayList<>();
        Auction auction;

        String search = "select * from AUCTION where idITEM = " + code +";";

        try{
            connectDatabase.resultSet = connectDatabase.statement.executeQuery(search);
            if (!connectDatabase.resultSet.next()){
                return auctions;
            }
            while (connectDatabase.resultSet.next()){
                auction = new Auction(connectDatabase.resultSet.getInt(1), connectDatabase.resultSet.getInt(2),
                        connectDatabase.resultSet.getString(3), connectDatabase.resultSet.getString(4),
                        connectDatabase.resultSet.getDate(5), connectDatabase.resultSet.getInt(6), connectDatabase.resultSet.getInt(7));
                auctions.add(auction);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return auctions;
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
