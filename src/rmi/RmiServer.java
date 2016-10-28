package rmi;

import java.io.Serializable;
import java.rmi.AccessException;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

import java.rmi.server.UnicastRemoteObject;
import java.sql.*;
import java.util.ArrayList;

import java.util.Properties;

import components.Auction;
import components.Bid;
import components.Client;
import components.Message;

import resources.GetPropertiesValues;

import javax.xml.transform.Result;


public class RmiServer extends UnicastRemoteObject implements RmiInterface, Serializable{

    public static RmiServer rmiServer;
    private Connection connection;
    private Statement statement;

    GetPropertiesValues gpv = new GetPropertiesValues();
    Properties prop = gpv.getProperties();

    // JDBC driver name and database URL
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    // Esta na minha maquina! Ter atencao para depois nao haver conflitos
    final String DB_URL = prop.getProperty("stringJDBC");

    //  Database credentials

    String USER = prop.getProperty("dbUser");
    String PASS = prop.getProperty("dbPass");

    public RmiServer() throws RemoteException {
        connectDatabase();
    }


    //-----------------------------------------------------------------------
    // DATABASE METHODS

    /**
     * @param username
     * @param password
     * @return
     */
    public synchronized boolean registerClient(String username, String password) {

        String add = "INSERT INTO USER (userName, password, online) VALUES ('" + username + "', '" + password + "', " + 0 + ");";

        System.out.println("Recebeu o registo clinete");

        try {
            statement.executeUpdate(add);
            commit();
            System.out.println("DEU");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            rollback();
            return false;
        }
    }


    /**
     * Fazer grande Login
     *
     * @param client
     * @return
     */

    //falta verificar quando o username e a password não estão certos. faz login na mesma
    public boolean doLogin(Client client) {
        String search = "SELECT * FROM USER WHERE userName = '" + String.valueOf(client.getUserName())
                + "' AND password = '" + String.valueOf(client.getPassword()) + "';";

        ResultSet resultSet;
        try {
            resultSet = statement.executeQuery(search);
            while (resultSet.next()) {
                putOnline(client);
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Método que nos retorna o cliente para guardar na conexão. Mais fácil para depois associar sempre o cliente à operação
     *
     * @param username
     * @param password
     * @return
     */

    public Client getClient(String username, String password) {

        Client client = null;

        String search = "SELECT * FROM USER WHERE userName ='" + username
                + "'AND password='" + password + "';";

        ResultSet resultSet;

        try {
            resultSet = statement.executeQuery(search);

            while (resultSet.next()) {

                int idUser = resultSet.getInt(1);
                String userName = resultSet.getString(2);
                String pass = resultSet.getString(3);

                client = new Client(idUser, userName, pass);
            }
        } catch (SQLException e) {
            System.out.println("client not found");
        }
        return client;
    }

    /**
     * Método para colocar os utilizadores online a partir do momento em que é feito o login
     *
     * @param client
     */

    public void putOnline(Client client) {
        String update = "UPDATE USER SET online = " + 1 + " WHERE idUSER = " + client.getIdUser();

        ResultSet resultSet;

        try {
            statement.executeUpdate(update);
            commit();
        } catch (SQLException e) {
            e.printStackTrace();
            rollback();
        }
    }

    /**
     * Update user status
     *
     * @param client
     */

    public void putOffline(Client client) {

        String update = "UPDATE USER SET online = " + 0 + " WHERE idUSER = " + client.getIdUser();

        ResultSet resultSet;

        try {
            statement.executeUpdate(update);
            commit();
        } catch (SQLException e) {
            e.printStackTrace();
            rollback();
        }
    }

    /**
     * Procurar os utilizadores, depois temos de ver como postar os online e os offline
     *
     * @return
     */

    public ArrayList<Client> searchOnlineUsers() {
        String search = "SELECT * FROM USER WHERE online = 1;";
        ArrayList<Client> clients = new ArrayList<>();
        Client client;

        ResultSet resultSet;

        try {
            resultSet = statement.executeQuery(search);
            while (resultSet.next()){

                int idUser = resultSet.getInt(1);
                String userName = resultSet.getString(2);
                String password = resultSet.getString(3);

                client = new Client(idUser, userName, password);

                clients.add(client);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return clients;
    }


    /**
     * Cria leilao
     *
     * @param auction
     * @return
     */
    public synchronized boolean createAuction(Auction auction) {

        String add = "INSERT INTO AUCTION(idItem, title, description, deadline, amount, USER_idUSER) VALUES('"
                + auction.getIdItem() + "', '" + auction.getTitle() + "', '" + auction.getDescription() + "', '"
                + auction.getDeadline() + "', '" + auction.getAmount() + "', '" + auction.getIdUser() + "');";


        try {

            statement.executeUpdate(add);
            commit();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            rollback();
            return false;
        }
    }

    /**
     * Procura o leilao por codigo do item
     *
     * @param code
     * @return auctions
     */

    public ArrayList<Auction> searchAuction(int code) {

        Auction auction;

        ArrayList<Auction> auctions = new ArrayList<>();

        String search = "SELECT * FROM AUCTION WHERE idITEM = " + code + ";";

        ResultSet resultSet;

        try {

            resultSet = statement.executeQuery(search);

            while (resultSet.next()) {

                System.out.println("Ele entra aqui");

                int idAuction = resultSet.getInt(1);
                String title = resultSet.getString(3);
                String description = resultSet.getString(4);
                Timestamp timestamp = resultSet.getTimestamp(5);
                int amount = resultSet.getInt(6);

                auction = new Auction(idAuction, title, description, timestamp, amount);

                System.out.println("E aqui?");

                auctions.add(auction);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return auctions;
    }


    /**
     * Get messages with the idAuction
     * @param idAuction
     * @return
     */

    public ArrayList<Message> getMessages(int idAuction){

        Message message;
        ArrayList<Message> messages = new ArrayList<>();

        String search = "SELECT MESSAGE.* FROM MESSAGE, AUCTION WHERE MESSAGE.AUCTION_idAUCTION = AUCTION.idAUCTION AND AUCTION.idAUCTION =" + idAuction+";";

        ResultSet resultSet;

        try {
            resultSet = statement.executeQuery(search);

            while (resultSet.next()){
                int idMessage = resultSet.getInt(1);
                String text = resultSet.getString(2);
                int readed = resultSet.getInt(3);
                int idUser = resultSet.getInt(4);

                message = new Message(idMessage, text, readed, idUser, idAuction);

                messages.add(message);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return  messages;
    }


    public ArrayList<Bid> getBids(int idAuction){

        Bid bid;
        String searchBid = " SELECT BID.* FROM BID WHERE AUCTION_idAUCTION = "+idAuction+";";
        ArrayList<Bid> bids = new ArrayList<>();

        ResultSet resultSet;

        try {
            resultSet = statement.executeQuery(searchBid);

            while (resultSet.next()){

                int idBid = resultSet.getInt(1);
                int amount = resultSet.getInt(2);
                int iduser = resultSet.getInt(3);
                int idauction = resultSet.getInt(4);

                bid = new Bid(idBid,amount,iduser,idauction);

                bids.add(bid);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bids;

    }

    /**
     * Retorna apenas um projecto, para poder-mos aceder ao detalhe do projecto
     *
     * //@param code
     * @return
     */

    public Auction detailAuction(int code) {

        Auction auction = null;
        Bid bid;
        Message message;

        ArrayList<Bid> bids = new ArrayList<>();
        ArrayList<Message> messages = getMessages(code);

        String search = "select * from AUCTION where idAUCTION = " + code +";";

        ResultSet resultSet;

        try {
            resultSet = statement.executeQuery(search);
            if (!resultSet.next()) {
                return null;
            }
            while (resultSet.next()) {


                messages = getMessages(code);
                getBids(code);
                auction = new Auction(resultSet.getInt(1),resultSet.getInt(2),
                        resultSet.getString(3), resultSet.getString(4),
                        resultSet.getTimestamp(5), resultSet.getInt(6),resultSet.getInt(7), messages, bids);

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return auction;
    }


    public void myAuctions(Client client) {

    }

    /**
     * Fazer uma licitacao. Recebe um argumento do tipo BID
     *
     * @param bid
     * @return
     */

    public synchronized boolean bid(Bid bid) {
        String add = "INSERT INTO BID(idBID, amount, USER.idUSER, AUCTION.idAUCTION) VALUES('" + bid.getIdBid() + "','" +
                bid.getAmount() + "', '" + bid.getIdUser() + "', '" + bid.getIdAuction() + "');";
        try {

            statement.executeUpdate(add);
            commit();
            return true;

        } catch (SQLException e) {

            e.printStackTrace();
            rollback();

            return false;
        }
    }

    /**
     * Edit auction. Recebe um parametro auction
     *
     * @param auction
     * @return
     */

    public synchronized boolean editAuction(Auction auction, Client client) {
        String update = "UPDATE AUCTION SET idITEM = '" + auction.getIdItem() + "', title = '" + auction.getTitle() + "', description = '" +
                auction.getDescription() + "', amount = '" + auction.getAmount() + "', WHERE idAUCTION = '" + auction.getIdAuction() + "'" +
                "AND USER_idUSER = " + client.getIdUser() +"';";

        try {
            statement.executeUpdate(update);
            commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            rollback();
        }
        return false;
    }


    /**
     * Send a message to a auction
     *
     * @param message
     * @return
     */

    public boolean message(Message message) {
        String add = "INSERT INTO MESSAGE (text, readed, USER_idUSER, AUCTION_idAUCTION) VALUES ('" + message.getText() + "', '" +
                message.getReaded() + "', '" + message.getIdCient() + "', '" + message.getIdAuction() + "');";

        try {
            statement.executeUpdate(add);
            commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            rollback();
        }
        return false;
    }

    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------

    //-----------------------------------------
    // LIGAR A BASE DE DADOS

    public void connectDatabase(){
        try{
            // Register JDBC
            Class.forName(JDBC_DRIVER);

            connection = DriverManager.getConnection(DB_URL, USER, PASS);

            statement = connection.createStatement();
            connection.setAutoCommit(false);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {

            e.printStackTrace();
        }

        if (connection != null){
            System.out.println("Connection to the database successfull");
        }else{
            System.out.println("Fail to connect to data base");
        }
    }

    private void rollback(){
        try{
            connection.rollback();
        }catch(SQLException se){
            System.err.println("Rollback failed!");
        }
    }

    private void commit(){
        try{
            connection.commit();
        }catch(SQLException se){
            System.err.println("Commit failed!");
        }
    }

    //----------------------------------------------------------------------------------
    //----------------------------------------------------------------------------------

    public static void main(String[] args) {

        GetPropertiesValues gpv = new GetPropertiesValues();
        Properties prop = gpv.getProperties();


        try {
            rmiServer = new RmiServer();
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        int rmiPort = Integer.parseInt(prop.getProperty("rmi1port"));

        String rmiHost;

        boolean runningRMI = false;

        if (runningRMI) {
            rmiHost = prop.getProperty("rmi1host");
        } else {
            rmiHost = prop.getProperty("rmi2host");
        }


        //----------------------------------------------------------------------------
        // Parte de ligar o RMI SERVER

        while (true) {

            try {

                System.getProperties().put("java.security.policy", "security.policy");
                System.setSecurityManager(new RMISecurityManager());

                System.out.println("RMI ligado como servidor primário com registo no porto: " + rmiPort);
                System.out.println("HOST: " + rmiHost);

                LocateRegistry.createRegistry(rmiPort).rebind("rmi_server", rmiServer);

                break;

            } catch (AccessException e) {
                e.printStackTrace();
            } catch (RemoteException e) {
                e.printStackTrace();

                System.out.println("Servidor Secundário... Tentativa de religação como primário.");
                try {
                    Thread.sleep(Integer.parseInt(prop.getProperty("sleepTimeSecondaryRmi")));
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }
}
