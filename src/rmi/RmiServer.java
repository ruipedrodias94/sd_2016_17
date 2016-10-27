package rmi;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.rmi.AccessException;
import java.rmi.RMISecurityManager;
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
import java.util.Properties;
import java.util.StringJoiner;

import components.Auction;
import components.Bid;
import components.Client;
import components.Message;
import database.*;
import helpers.ProtocolParser;
import resources.GetPropertiesValues;


public class RmiServer extends UnicastRemoteObject implements RmiInterface {

    public static ConnectDatabase connectDatabase;
    public static RmiServer rmiServer;


    public RmiServer() throws RemoteException {
        connectDatabase = new ConnectDatabase();
    }

    /**
     * @param username
     * @param password
     * @return
     */
    public synchronized boolean registerClient(String username, String password) {

        String add = "insert into USER (userName, password, online) values ('" + username + "', '" + password + "', " + 0 + ");";

        System.out.println("Recebeu o registo clinete");

        try {
            connectDatabase.statement.executeUpdate(add);
            connectDatabase.connection.commit();
            System.out.println("DEU");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                connectDatabase.connection.rollback();
                System.out.println("NAO DEU");
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
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
        String search = "select * from USER where userName = '" + String.valueOf(client.getUserName())
                + "' and password = '" + String.valueOf(client.getPassword()) + "';";

        try {
            connectDatabase.resultSet = connectDatabase.statement.executeQuery(search);
            while (connectDatabase.resultSet.next()) {
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

        String search = "select * from USER where userName ='" + username
                + "'and password='" + password + "';";

        try {
            connectDatabase.resultSet = connectDatabase.statement.executeQuery(search);
            while (connectDatabase.resultSet.next()) {
                client = new Client(connectDatabase.resultSet.getInt(1), connectDatabase.resultSet.getString(2), connectDatabase.resultSet.getString(3));
            }
        } catch (SQLException e) {
            //e.printStackTrace();
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
        String update = "update USER set online = " + 1 + " WHERE idUSER = " + String.valueOf(client.getIdUser());

        try {
            connectDatabase.statement.executeUpdate(update);
            connectDatabase.connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                connectDatabase.connection.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
    }

    /**
     * Update user status
     *
     * @param client
     */

    public void putOffline(Client client) {
        String update = "update USER set online = " + 0 + " WHERE idUSER = " + String.valueOf(client.getIdUser());

        try {
            connectDatabase.statement.executeUpdate(update);
            connectDatabase.connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                connectDatabase.connection.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
    }

    /**
     * Procurar os utilizadores, depois temos de ver como postar os online e os offline
     *
     * @return
     */

    public ArrayList<Client> searchOnlineUsers() {
        String search = "select * from USER where online = 1;";
        ArrayList<Client> clients = new ArrayList<>();
        Client client;

        try {
            connectDatabase.resultSet = connectDatabase.statement.executeQuery(search);

            while (connectDatabase.resultSet.next()) {
                client = new Client(connectDatabase.resultSet.getInt(1), connectDatabase.resultSet.getString(2), connectDatabase.resultSet.getString(3));
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

        try {
            String add = "insert into AUCTION(idItem, title, description, deadline, amount, USER_idUSER) values('"
                    + auction.getIdItem() + "', '" + auction.getTitle() + "', '" + auction.getDescription() + "', '"
                    + auction.getDeadline() + "', '" + auction.getAmount() + "', '" + auction.getIdUser() + "');";

            connectDatabase.statement.executeUpdate(add);
            connectDatabase.connection.commit();

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
     *
     * @param code
     * @return auctions
     */

    public ArrayList<Auction> searchAuction(int code) {

        Auction auction;
        Bid bid;
        Message message;

        ArrayList<Auction> auctions = new ArrayList<>();
        ArrayList<Bid> bids = new ArrayList<>();
        ArrayList<Message> messages = new ArrayList<>();

        String search = "select * from AUCTION where idITEM = '"+code+"';";

        try {
            connectDatabase.resultSet = connectDatabase.statement.executeQuery(search);
            if (!connectDatabase.resultSet.next()) {
                System.out.println("ali");
                return auctions;

            }
            while (connectDatabase.resultSet.next()) {

                System.out.println("aqui");

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
     * Retorna apenas um projecto, para poder-mos aceder ao detalhe do projecto
     *
     * @param code
     * @return
     */

    public Auction detailAuction(int code) {

        Auction auction = null;
        Bid bid;
        Message message;

        ArrayList<Bid> bids = new ArrayList<>();
        ArrayList<Message> messages = new ArrayList<>();

        String search = "select * from AUCTION, MESSAGE, BID where idAUCTION = " + code +
                " and MESSAGE.AUCTION_idAUCTION = AUCTION.idAUCTION" +
                " and BID.AUCTION_idAUCTION = AUCTION.idAUCTION;";

        try {
            connectDatabase.resultSet = connectDatabase.statement.executeQuery(search);
            if (!connectDatabase.resultSet.next()) {
                return null;
            }
            while (connectDatabase.resultSet.next()) {
                message = new Message(connectDatabase.resultSet.getString(9),
                        connectDatabase.resultSet.getInt(10), connectDatabase.resultSet.getInt(11), connectDatabase.resultSet.getInt(12));

                messages.add(message);

                bid = new Bid(connectDatabase.resultSet.getInt(13), connectDatabase.resultSet.getInt(14),
                        connectDatabase.resultSet.getInt(15), connectDatabase.resultSet.getInt(16));

                bids.add(bid);

                auction = new Auction(connectDatabase.resultSet.getInt(1), connectDatabase.resultSet.getInt(2),
                        connectDatabase.resultSet.getString(3), connectDatabase.resultSet.getString(4),
                        connectDatabase.resultSet.getDate(5), connectDatabase.resultSet.getInt(6), connectDatabase.resultSet.getInt(7), messages, bids);

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return auction;
    }


    // TODO: ESTA TAMBEM NAO ME ATREVO
    public void myAuctions(Client client) {

    }

    /**
     * Fazer uma licitacao. Recebe um argumento do tipo BID
     *
     * @param bid
     * @return
     */

    public boolean bid(Bid bid) {
        String add = "insert into BID(idBID, amount, USER.idUSER, AUCTION.idAUCTION) values('" + bid.getIdBid() + "','" +
                bid.getAmount() + "', '" + bid.getIdUser() + "', '" + bid.getIdAuction() + "');";
        try {
            connectDatabase.statement.executeUpdate(add);
            connectDatabase.connection.commit();
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
     *
     * @param auction
     * @return
     */

    public boolean editAuction(Auction auction) {
        String update = "update AUCTION set idITEM = '" + auction.getIdItem() + "', title = '" + auction.getTitle() + "', description = '" +
                auction.getDescription() + "', amount = '" + auction.getAmount() + "', where idAUCTION = '" + auction.getIdAuction() + "';";

        try {
            connectDatabase.statement.executeUpdate(update);
            connectDatabase.connection.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                connectDatabase.connection.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
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
        String add = "insert int MESSAGE (text, readed, USER_idUSER, AUCTION_idAUCTION) values ('" + message.getText() + "', '" +
                message.getReaded() + "', '" + message.getIdCient() + "', '" + message.getIdAuction() + "');";

        try {
            connectDatabase.statement.executeUpdate(add);
            connectDatabase.connection.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                connectDatabase.connection.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
        return false;
    }



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

        boolean runningRMI = true;

        if (runningRMI) {
            rmiHost = prop.getProperty("rmi1host");
        } else {
            rmiHost = prop.getProperty("rmi2host");
        }

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
