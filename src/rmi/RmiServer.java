package rmi;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
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
import components.Bid;
import components.Client;
import components.Message;
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
                putOnline(client);
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Método para colocar os utilizadores online a partir do momento em que é feito o login
     * @param client
     */

    public void putOnline(Client client){
        String update = "update USER online = '" + 1 + "' WHERE idUSER =" +client.getIdUser();

        try {
            connectDatabase.statement.executeUpdate(update);
            connectDatabase.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
                client = new Client(connectDatabase.resultSet.getInt(1),connectDatabase.resultSet.getString(2), connectDatabase.resultSet.getString(3));
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
     * @param mode 1 - Search Auctions / 2 - Detail of Auctions / 3 - My Auctions
     * @return
     */

    //TODO: Esta merda está mal, estou-me a baralhar todo fodasse. Acabar esta merda

    public ArrayList<Auction> searchAuction(int code, int mode){

        Auction auction;
        Bid bid;
        Message message;

        ArrayList<Auction> auctions = new ArrayList<>();
        ArrayList<Bid> bids = new ArrayList<>();
        ArrayList<Message> messages = new ArrayList<>();

        String search = "";

        String search1 = "select * from AUCTION, MESSAGE, BID where idITEM = " + code +" and MESSAGE.AUCTION_idAUCTION = AUCTION.idAUCTION;";
        String search2 = "select * from AUCTION, MESSAGE, BID where idAUCTION = " + code +" and MESSAGE.AUCTION_idAUCTION = AUCTION.idAUCTION;";
        String search3 = "select * from AUCTION, MESSAGE, BID where AUCTION.USER_idUSER = " + code +" and MESSAGE.AUCTION_idAUCTION = AUCTION.idAUCTION;";
        if (mode == 1){
            search = search1;
        }else if (mode == 2){
            search = search2;
        }else if (mode == 3){
            search = search3;
        }

        try{
            connectDatabase.resultSet = connectDatabase.statement.executeQuery(search);
            if (!connectDatabase.resultSet.next()){
                return auctions;
            }
            while (connectDatabase.resultSet.next()){
                message = new Message(connectDatabase.resultSet.getInt(8), connectDatabase.resultSet.getString(9),
                        connectDatabase.resultSet.getInt(10), connectDatabase.resultSet.getInt(11), connectDatabase.resultSet.getInt(12));

                messages.add(message);

                bid = new Bid(connectDatabase.resultSet.getInt(13), connectDatabase.resultSet.getInt(14),
                        connectDatabase.resultSet.getInt(15), connectDatabase.resultSet.getInt(16));

                bids.add(bid);

                auction = new Auction(connectDatabase.resultSet.getInt(1), connectDatabase.resultSet.getInt(2),
                        connectDatabase.resultSet.getString(3), connectDatabase.resultSet.getString(4),
                        connectDatabase.resultSet.getDate(5), connectDatabase.resultSet.getInt(6), connectDatabase.resultSet.getInt(7), messages, bids);

                auctions.add(auction);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return auctions;
    }


    /**
     * Fazer uma licitacao. Recebe um argumento do tipo BID
     * @param bid
     * @return
     */

    public boolean bid(Bid bid) {
        String add = "insert into BID(idBID, amount, USER.idUSER, AUCTION.idAUCTION) values('" + bid.getIdBid() + "','" +
                bid.getAmount() + "', '" + bid.getIdUser() + "', '" + bid.getIdAuction() + "');";
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
     * Edit auction. Recebe um parametro auction
     * @param auction
     * @return
     */

    public boolean editAuction(Auction auction){
        String update = "update AUCTION set idITEM = '" + auction.getIdItem() + "', title = '" + auction.getTitle() + "', description = '" +
                auction.getDescription() + "', amount = '" + auction.getAmount() + "', where idAUCTION = '" +auction.getIdAuction() +"';";

        try {
            connectDatabase.statement.executeUpdate(update);
            connectDatabase.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    //funcao para ver se o server rmi esta ligado num determinado host e porto
    public static boolean checkRMIServer(String address, int port, int numtry) {
        int tries = 0;
        boolean status = false;
        while (tries < numtry) {
            try {
                Socket testSocket = new Socket();
                testSocket.connect(new InetSocketAddress(address, port), 500);
                status = false;
                testSocket.close();
            } catch (IOException e) {
                status = true;
                break;
            }
            tries++;
        }
        return status;
    }



    public static void main(String[] args) throws RemoteException, InterruptedException{

        rmiServer = new RmiServer();
        String remoteRMIHost = "localhost";
        int remotermiPort = 1093;

        while(true){
                //vê se algum registo rmi está ligado naquele host e porto
                if(checkRMIServer(remoteRMIHost,remotermiPort,500)==true)
                {
                    //se não estiver liga-se como primario
                    Registry registry = LocateRegistry.createRegistry(1099);
                    registry.rebind("rmi_server", rmiServer);
                    System.out.println("Rmi Ligado");
                    System.out.println("Servidor Primário");
                break;
                }
                else
                {
                    //Se estiver fica como secuandário e vai tentando ligar-se
                    System.out.println("Servidor Secundário... Tentativa de religação como primário.");
                    Thread.sleep(1000);
                }
        }
    }
}
