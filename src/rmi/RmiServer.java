package rmi;

import java.io.Serializable;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;

import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.*;
import java.util.ArrayList;

import java.util.Properties;
import java.util.Scanner;


import components.Auction;
import components.Bid;
import components.Client;
import components.Message;

import resources.GetPropertiesValues;
import tcp.CallbackInterface;

import static java.sql.JDBCType.NULL;


public class RmiServer extends UnicastRemoteObject implements RmiInterface, Serializable{

    public static RmiServer rmiServer;
    private Connection connection;
    private Statement statement;
    static CallbackInterface clientNotification;


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

        String add = "INSERT INTO USER (userName, password, online, idFacebook) VALUES ('" + username + "', '" + password + "', " + 0 + ","+"NULL);";


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
     * @param
     * @return
     */

    //falta verificar quando o username e a password não estão certos. faz login na mesma
    public boolean doLogin(String username, String password) {
        String search = "SELECT * FROM USER WHERE userName = '" + username
                + "' AND password = '" + password+"';";

        ResultSet resultSet;
        try {
            resultSet = statement.executeQuery(search);
            while (resultSet.next()) {
                putOnline(username,password);
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    /**
     * Método que nos retorna o cliente para guardar na conexão. Mais fácil para depois associar sempre o cliente à operação
     *
     * @param username

     * @return
     */

    public Client getClient(String username) {

        Client client = null;

        String search = "SELECT * FROM USER WHERE userName ='" + username + "';";

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


    public String getUserName(int id){
        String search = "SELECT userName FROM USER WHERE idUser ='" + 1 + "';";

        ResultSet resultSet;

        String userName = "";

        try{
            resultSet = statement.executeQuery(search);
            while (resultSet.next()){
                userName = resultSet.getString(2);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userName;
    }

    /**
     * Get username by facebook id!
     * @param idFacebook
     * @return
     */
    public String getUserNameFacebook(String idFacebook){
        String search = "SELECT * FROM USER WHERE idFacebook ='" + idFacebook + "';";

        ResultSet resultSet;

        String userName = "";

        try{
            resultSet = statement.executeQuery(search);
            while (resultSet.next()){
                userName = resultSet.getString(2);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println(userName);
        return userName;
    }

    public int getUserId(String username){
        String search = "SELECT idUSER FROM USER WHERE username ='" + username + "';";

        ResultSet resultSet;

        int userID = 0;

        try{
            resultSet = statement.executeQuery(search);
            while (resultSet.next()){
                userID = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userID;
    }

    /**
     *
     * @param username
     * @param password
     */

    public void putOnline(String username, String password) {
        String update = "UPDATE USER SET online = " + 1 + " WHERE userName = '" + username +" 'AND password = '" + password +"' ;";

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

    public ArrayList<Auction> searchAuction(String code) {

        Auction auction;

        ArrayList<Auction> auctions = new ArrayList<>();

        String search = "SELECT * FROM AUCTION WHERE idITEM = " + code + ";";

        ResultSet resultSet;

        try {

            resultSet = statement.executeQuery(search);

            while (resultSet.next()) {


                int idAuction = resultSet.getInt(1);
                String idItem = resultSet.getString(2);
                String title = resultSet.getString(3);
                String description = resultSet.getString(4);
                Timestamp timestamp = resultSet.getTimestamp(5);
                int amount = resultSet.getInt(6);

                auction = new Auction(idAuction, idItem, title, description, timestamp, amount);

                auctions.add(auction);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return auctions;
    }




    /**
     * Get auctions that the client have created
     * @param client
     * @return
     */

    public ArrayList<Auction> searchAuctionsCreated(Client client) {

        Auction auction;

        ArrayList<Auction> auctions = new ArrayList<>();

        String search = "SELECT * FROM AUCTION WHERE USER_idUSER = " + client.getIdUser() + ";";

        ResultSet resultSet;
        Connection connection1 = null;

        try {
            connection1 = DriverManager.getConnection(DB_URL,USER,PASS);
            Statement statement = connection1.createStatement();
            resultSet = statement.executeQuery(search);
            while (resultSet.next()) {

                int idAuction = resultSet.getInt(1);
                String idItem = resultSet.getString(2);
                String title = resultSet.getString(3);
                String description = resultSet.getString(4);
                Timestamp timestamp = resultSet.getTimestamp(5);
                int amount = resultSet.getInt(6);

                auction = new Auction(idAuction, idItem, title, description, timestamp, amount);

                auctions.add(auction);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            connection1.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return auctions;
    }





    /**
     * Get bids that the client have created
     * @param client
     * @return
     */

    public ArrayList<Bid> searchBids(Client client) {

        ArrayList<Bid> bids = new ArrayList<>();
        Bid bid;

        System.out.println("ID USER:"+client.getIdUser());

        String search = "SELECT * FROM BID WHERE USER_idUSER = " + client.getIdUser() + ";";

        ResultSet resultSet;
        Connection connection1 = null;

        try {
            connection1 = DriverManager.getConnection(DB_URL,USER,PASS);
            Statement statement = connection1.createStatement();
            resultSet = statement.executeQuery(search);
            while (resultSet.next()) {
                int idBID = resultSet.getInt(1);
                int amount = resultSet.getInt(2);
                int idAuction = resultSet.getInt(4);

                bid = new Bid(idBID,amount,client.getIdUser(), idAuction);

                bids.add(bid);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            connection1.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bids;
    }

    /**
     * Return my auctions
     * @param client
     * @return
     */

    public ArrayList<Auction> myAuctions(Client client) {

        ArrayList <Auction> auctions ;
        ArrayList <Bid> bids ;
        Auction auction ;
        int idAuction;
        boolean contains;

        //bids que o cliente fez
        bids = searchBids(client);

        //leiloes que o cliente criou
        auctions = searchAuctionsCreated(client);

        //todos os leiloes que tenha feito bid e nao esteja nos que criou adiciona
        for(int i = 0;  i < bids.size(); i++)
        {
            idAuction = bids.get(i).getIdAuction();
            contains = false;
            for(int j=0;j< auctions.size();j++)
            {
                if(auctions.get(j).getIdAuction() == idAuction)
                {
                    contains = true;
                }
            }
            if(contains == false)
            {
                auction = detailAuction(String.valueOf(idAuction));
                auctions.add(auction);
            }
        }
        return auctions;
    }

    public ArrayList<Message> getMUnreadedMessages(int idUser){

        Message message;
        ArrayList<Message> messages = new ArrayList<>();

        String search = "SELECT * FROM UNREADED WHERE IdUSER=" + idUser+";";

        ResultSet resultSet;
        Connection connection1 = null;

        try {
            connection1 = DriverManager.getConnection(DB_URL,USER,PASS);
            Statement statement = connection1.createStatement();
            resultSet = statement.executeQuery(search);

            while (resultSet.next()){
                int idMessage = resultSet.getInt(1);
                String text = resultSet.getString(3);
                idUser = resultSet.getInt(2);
                int idAuction = resultSet.getInt(4);
                String idUserfrom = resultSet.getString(5);
                System.out.println(idUserfrom);

                message = new Message(idMessage, idUser,text,idAuction,idUserfrom);

                messages.add(message);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            connection1.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return  messages;
    }


    public synchronized void deleteUnreadedMessages(int idmessage){

        Message message;
        ArrayList<Message> messages = new ArrayList<>();

        String search = "DELETE FROM UNREADED WHERE IdUNREADED='"+idmessage+"';";

        ResultSet resultSet;
        Connection connection1 = null;

        try {
            connection1 = DriverManager.getConnection(DB_URL,USER,PASS);
            Statement statement = connection1.createStatement();
            statement.executeUpdate(search);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            connection1.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            connection1.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

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
        Connection connection1 = null;

        try {
            connection1 = DriverManager.getConnection(DB_URL,USER,PASS);
            Statement statement = connection1.createStatement();
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
        try {
            connection1.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return  messages;
    }


    public ArrayList<Bid> getBids(int idAuction)  {

        Bid bid;
        String search = " SELECT BID.* FROM BID WHERE AUCTION_idAUCTION = "+idAuction+" ORDER BY amount ASC;";
        ArrayList<Bid> bids = new ArrayList<>();
        ResultSet resultSet;

        Connection connection1 = null;

        try {
            connection1 = DriverManager.getConnection(DB_URL,USER,PASS);
            Statement statement = connection1.createStatement();
            resultSet = statement.executeQuery(search);

            while (resultSet.next()){

                int idBid = resultSet.getInt(1);
                int amount = resultSet.getInt(2);
                int iduser = resultSet.getInt(3);
                int idauction = resultSet.getInt(4);

                bid = new Bid(idBid,amount,iduser,idauction);

                bids.add(bid);
            }
            connection1.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            connection1.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bids;

    }

    /**
     * This detail is the PROJECT ID
     * @param code
     * @return
     */
    public Auction detailAuction(String code) {

        Auction auction = null;
        Bid bid;
        Message message;

        ArrayList<Bid> bids;
        ArrayList<Message> messages;

        String search = "select * from AUCTION where idAUCTION = '" + code +"';";

        Connection connection1 = null;

        ResultSet resultSet;
        try {
            connection1 = DriverManager.getConnection(DB_URL,USER,PASS);
            Statement statement = connection1.createStatement();
            resultSet = statement.executeQuery(search);


            while (resultSet.next()) {

                messages = getMessages(Integer.parseInt(code));
                bids = getBids(Integer.parseInt(code));
                auction = new Auction(resultSet.getInt(1),resultSet.getString(2),
                        resultSet.getString(3), resultSet.getString(4),
                        resultSet.getTimestamp(5), resultSet.getInt(6),resultSet.getInt(7), messages, bids);

            }
            connection1.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return auction;
    }



    /**
     * Fazer uma licitacao. Recebe um argumento do tipo BID
     *
     * @param bid
     * @return
     */

    public synchronized boolean bid(Bid bid) {

        String add = "INSERT INTO BID(idBID, amount, USER_idUSER, AUCTION_idAUCTION) VALUES('" + bid.getIdBid() + "','" +
                bid.getAmount() + "', '" + bid.getIdUser() + "', '" + bid.getIdAuction() + "');";
        try {

            statement.executeUpdate(add);
            commit();
            System.out.println("Faz grande bid");
            return true;

        } catch (SQLException e) {
            System.out.println("Nao faz grande bid");
            e.printStackTrace();
            rollback();
            return false;
        }
    }

    /**
     *
     * @param antigo
     * @param novo
     * @param client
     * @return
     */

    public synchronized boolean editAuction(Auction antigo, Auction novo, Client client) {

        ResultSet resultSet;
        Connection connection1 = null;
        Connection connection2 = null;

        String anterior = "INSERT INTO AUCTION_HIST(idITEM,title,description,deadline,amount,AUCTION_idAUCTION,AUCTION_USER_idUSER) VALUES( "+antigo.getIdItem()+" ,'" + antigo.getTitle() + "', '" +
                antigo.getDescription() +"','"+antigo.getDeadline()+"','" + antigo.getAmount()+ "','"+antigo.getIdAuction()+"', '"+antigo.getIdUser()+"');";

        String update = "UPDATE AUCTION SET idITEM = '" + novo.getIdItem() + "', title = '" + novo.getTitle() + "', description = '" +
                novo.getDescription() + "', amount = '" + novo.getAmount() + "' WHERE idAUCTION = '" + novo.getIdAuction() + "'" +
                "AND USER_idUSER = " + client.getIdUser() +";";


        try {


            connection1 = DriverManager.getConnection(DB_URL,USER,PASS);
            Statement statement = connection1.createStatement();
            statement.executeUpdate(anterior);

            connection2 = DriverManager.getConnection(DB_URL,USER,PASS);
            statement = connection2.createStatement();
            statement.executeUpdate(update);


            commit();
            connection1.close();
            connection2.close();
            return true;
        } catch (SQLException e) {

            e.printStackTrace();
            rollback();
        }
        return false;
    }


    /**
     * Clients to notify when a message is written
     *
     * @param m
     * @return
     */

    public ArrayList<Integer> clientsToNotificate(Message m)
    {


        ArrayList<Integer> clientsToNotify = new ArrayList<>();
        ArrayList<Message> auctionMessages = new ArrayList<>();

        //id do criador do leilão
        clientsToNotify.add(detailAuction(String.valueOf(m.getIdAuction())).getIdUser());
        clientsToNotify.add(detailAuction(String.valueOf(m.getIdAuction())).getIdUser());

        //Mensagens do leilão
        auctionMessages = getMessages(m.getIdAuction());

        for(int i = 0; i<auctionMessages.size();i++)
        {
            if(!clientsToNotify.contains(auctionMessages.get(i).getIdCient()))
            {
                clientsToNotify.add(auctionMessages.get(i).getIdCient());
            }
        }

        return  clientsToNotify;


    }


    /**
     * Check if user is online by idUSER
     *
     * @param idUser
     * @return
     */

    public boolean checkClientOnline(int idUser)
    {
        String search = "SELECT * FROM USER WHERE idUSER = '" + idUser +"';";

        ResultSet resultSet;

        int onlineFlag;
        try {
            resultSet = statement.executeQuery(search);
            while (resultSet.next()) {
                onlineFlag = resultSet.getInt(4);
                if(onlineFlag==1){
                    return true;
                }
                else
                {
                    return false;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    /**
     * Send a message to a auction
     *
     * @param message
     * @return
     */

    public synchronized boolean message(Message message) {

        ArrayList<Message> auction_messages = new ArrayList<>();

        String add = "INSERT INTO MESSAGE (text, readed, USER_idUSER, AUCTION_idAUCTION) VALUES ('" + message.getText() + "', '" +
                message.getReaded() + "', '" + message.getIdCient() + "', '" + message.getIdAuction() + "');";

        ArrayList<Integer> clientsToNotify = clientsToNotificate(message);
        String writer = message.getUsername();

        try {
            statement.executeUpdate(add);
            commit();
        } catch (SQLException e) {
            e.printStackTrace();
            rollback();
        }


        for (int i = 0;i < clientsToNotify.size();i++)
        {
            /*if(checkClientOnline(clientsToNotify.get(i))==true) {

                try {
                    clientNotification.printOnClient(message,writer,clientsToNotify.get(i));
                    return true;
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }else{*/

                try {
                    Connection connection1 = null;
                    ResultSet resultSet;
                    connection1 = DriverManager.getConnection(DB_URL,USER,PASS);
                    Statement statement = connection1.createStatement();

                    String message_offline = "INSERT INTO UNREADED(idUser,TEXT,idAuction,idUserFrom) VALUES(" + clientsToNotify.get(i) + ",'" + message.getText() + "'," + message.getIdAuction()+",'"+ message.getUsername()+"');";

                    try {
                        statement.executeUpdate(message_offline);
                        commit();
                        connection1.close();
                        return true;
                    } catch (SQLException e) {
                        e.printStackTrace();
                        rollback();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        //}
        return false;
    }

    /**
     * Verifica se o facebook já está associado à sua conta
     * @param idFacebook
     * @return
     */
    public synchronized boolean verifyIfExist(String idFacebook){

        String search = "SELECT * FROM USER WHERE idFacebook ='" + idFacebook+ "';";

        ResultSet resultSet;

        try{
            resultSet = statement.executeQuery(search);
            while (resultSet.next()){
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Verifica se o facebook já está associado à sua conta
     * @param idUser
     * @return
     */
    public synchronized String verifyIfExistFacebookToPost(int idUser){

        String search = "SELECT * FROM USER WHERE idUser ='" + idUser+ "';";

        ResultSet resultSet;
        String idFacebook=null;

        try{
            resultSet = statement.executeQuery(search);
            while (resultSet.next()){
                idFacebook = resultSet.getString(5);
                return idFacebook;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return idFacebook;
    }

    /**
     * Se não estiver associado à conta, ele vai mandar grande update na sua BD
     * @param idUser
     * @param idFacebook
     * @return
     */
    public synchronized boolean associateFacebook(int idUser, String idFacebook){
        String update = "UPDATE user SET idFacebook = '" + idFacebook + "' WHERE idUSER = '" + idUser +"';";

        Connection connection2;
        ResultSet resultSet;

        try {

            connection2 = DriverManager.getConnection(DB_URL,USER,PASS);
            statement = connection2.createStatement();
            statement.executeUpdate(update);

            commit();
            connection2.close();
            return true;
        } catch (SQLException e) {

            e.printStackTrace();
            rollback();
        }
        return false;
    }

    /**
     * Do Facebook Login
     * @param username
     * @param idFacebook
     * @return
     */
    public synchronized boolean loginFacebook(String username, String idFacebook) {

        String add = "INSERT INTO USER (userName, password, online, idFacebook) VALUES ('" + username + "', '"+ " " + "', "+ 1 +"," + "'"+idFacebook+"');";

        System.out.println("Recebeu o registo clinete");

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
    //Callback

    static class HelloServer extends UnicastRemoteObject implements ServerCallbackInterface{


        public HelloServer() throws RemoteException {
            super();
        }

        public void print_on_server(String s) throws RemoteException {
            System.out.println("> "+s);
        }


        public void subscribe(String name, CallbackInterface c) throws RemoteException {
            System.out.println("Subscribing: "+name);
            clientNotification = c;
        }
    }




    public static void main(String[] args) throws UnknownHostException {

        //System.out.println(InetAddress.getLocalHost().getHostAddress());

        GetPropertiesValues gpv = new GetPropertiesValues();
        Properties prop = gpv.getProperties();


        try {
            rmiServer = new RmiServer();
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        int rmiPort = Integer.parseInt(prop.getProperty("rmi1port"));

        String rmiHost;

        boolean runningRMI = true;

        if (runningRMI) {
            rmiHost = prop.getProperty("rmi1host");
        } else {
            rmiHost = prop.getProperty("rmi2host");
        }



        System.getProperties().put("java.security.policy", "security.policy");
        System.setSecurityManager(new RMISecurityManager());

        /*try {
            LocateRegistry.createRegistry(8000);
            HelloServer h = new HelloServer();
            Naming.rebind("hello", h);
            System.out.println("Notifications Server ready.");

        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }*/


        //----------------------------------------------------------------------------
        // Parte de ligar o RMI SERVER

        while (true) {

            try {

                System.getProperties().put("java.security.policy", "security.policy");
                System.setSecurityManager(new RMISecurityManager());
                System.out.println(InetAddress.getLocalHost());
                RmiServer r = new RmiServer();

                Registry registry = LocateRegistry.createRegistry(2080);
                registry.rebind("rmi_server",r);

                System.out.println("RMI ligado como servidor primário com registo no porto: " + rmiPort);
                System.out.println("HOST: " + rmiHost);



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
