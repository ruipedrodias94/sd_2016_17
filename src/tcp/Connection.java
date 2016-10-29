package tcp;




import components.Auction;
import components.Bid;
import components.Client;
import components.Message;
import helpers.ProtocolParser;
import resources.GetPropertiesValues;
import rmi.RmiInterface;

import java.io.*;
import java.net.*;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

import java.rmi.server.UnicastRemoteObject;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


class Connection extends Thread {

    PrintWriter outToClient;
    BufferedReader inFromClient = null;
    Socket clientSocket;
    ArrayList<Connection> clients = null;
    CallbackInterface c;
    Client client = null;


    public Connection(Socket clientSockt, ArrayList<Connection> clients, CallbackInterface c) {
        this.clients = clients;
        this.clients.add(this);

        this.c = c;
        try {
            clientSocket = clientSockt;
            // create streams for writing to and reading from the socket

            outToClient = new PrintWriter(clientSocket.getOutputStream(), true);

            this.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //Thread que vai tratar do pedido do cliente
    public void run() {

        String messageFromClient;
        HashMap<String, String> messageParsed;
        String type;
        Auction auction;
        RmiInterface rmi = null;

        while (true) {

            try {
                inFromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                while ((messageFromClient = inFromClient.readLine()) != null) {

                    String init = "";
                    String aux = "";
                    auction = null;

                    try {
                        System.out.println(messageFromClient);
                        messageParsed = ProtocolParser.parse(messageFromClient);
                        type = messageParsed.get("type");

                        switch (type) {


                            case ("register"): {

                                rmi = invoqueRMI();

                                if (rmi.registerClient(messageParsed.get("username"), messageParsed.get("password"))) {
                                    outToClient.println("type: register, ok: true");
                                } else {
                                    outToClient.println("type: register, ok: false");
                                }
                                break;
                            }


                            case ("login"): {


                                ArrayList<Message> unreaded = new ArrayList<>();

                                rmi = invoqueRMI();

                                //Get that cliente back online
                                client = rmi.getClient(messageParsed.get("username"), messageParsed.get("password"));





                                if (rmi.doLogin(messageParsed.get("username"), messageParsed.get("password"))) {

                                    client.setUserName(messageParsed.get("username"));
                                    outToClient.println("type: login, ok: true");


                                    unreaded = rmi.getMUnreadedMessages(client.getIdUser());

                                    for(int i = 0;i<unreaded.size();i++)
                                    {
                                        String notification = "type: notification_message, id:"+unreaded.get(i).getIdAuction()+", user: "+unreaded.get(i).getUsername()+", text: "+unreaded.get(i).getText();
                                        outToClient.println(notification);
                                        rmi.deleteUnreadedMessages(unreaded.get(i).getIdMessage());
                                    }




                                } else {
                                    outToClient.println("type: login, ok: false");
                                }

                                break;
                            }


                            case ("create_auction"): {

                                String idItem = messageParsed.get("code");
                                String title = messageParsed.get("title");
                                String description = messageParsed.get("description");
                                String data = messageParsed.get("deadline");

                                int amount = Integer.parseInt(messageParsed.get("amount"));

                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm");
                                SimpleDateFormat simpleDateFormatnew = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                try {
                                    data = simpleDateFormatnew.format(simpleDateFormat.parse(data));
                                }
                                catch (ParseException pqp){

                                }

                                Timestamp newData = Timestamp.valueOf(data);

                                auction = new Auction(idItem, title, description, newData, amount, client.getIdUser());

                                //Chamada ao RMI

                                rmi = invoqueRMI();

                                if (rmi.createAuction(auction)) {
                                    outToClient.println("type : create_auction , ok: true");
                                } else {
                                    outToClient.println("type : create_auction , ok: false");
                                }

                                break;
                            }

                            //TODO----> Working? Checa aqui JJ
                            case ("search_auction"): {

                                String code = (messageParsed.get("code"));

                                rmi = invoqueRMI();

                                ArrayList<Auction> auctions = rmi.searchAuction(code);

                                Date date = new Date();
                                Timestamp now = new Timestamp(date.getTime());


                                // Make new string
                                if (auctions.isEmpty()){
                                    init = "type: search_auction, items_count: " + auctions.size();
                                    outToClient.print(init);
                                }else{
                                    init = "type: search_auction, items_count: " + auctions.size();

                                    for (int i = 0; i < auctions.size(); i++) {
                                        aux += ", items_" + i +"_id: " + auctions.get(i).getIdAuction() + ", items_"+i+"_code: "+ auctions.get(i).getIdItem() +
                                                ", items_"+i+"_title: "+ auctions.get(i).getTitle();

                                    }

                                    init += aux;
                                }

                                outToClient.println(init);
                                break;
                            }

                            case ("detail_auction"): {

                                rmi = invoqueRMI();

                                String id = messageParsed.get("id");

                                auction = rmi.detailAuction(id);


                                init = "type: detail_auction, title: " + auction.getTitle() + ", description: " + auction.getDescription() +
                                        ", deadline: " + auction.getDeadline() + ", messages_count: " + auction.getMessages().size();

                                for (int i = 0; i < auction.getMessages().size(); i++) {
                                    // Aqui esta ele

                                    String userName = rmi.getUserName(auction.getMessages().get(i).getIdCient());

                                    aux += ", message_" + i + "_user: " + userName + ", messages_" + i +
                                            "_text: " + auction.getMessages().get(i).getText();

                                }

                                init += aux + ", bids_count: " + auction.getBids().size();

                                Date date = new Date();
                                Timestamp now = new Timestamp(date.getTime());

                                /*if (now.after(auction.getDeadline())){
                                    init += ", bid_over: true";
                                }else{
                                    init += ", bid_over: false";
                                }*/

                                outToClient.println(init);

                                break;

                            }


                            case ("my_auctions"): {
                                ArrayList<Auction> auctions ;
                                rmi = invoqueRMI();
                                auctions = rmi.myAuctions(client);

                                init = "type: my_auctions, items_count: "+auctions.size();
                                for (int i = 0; i < auctions.size(); i++) {
                                    aux += ", items_"+ i +"_id: "+auctions.get(i).getIdAuction()+", items_"+ i +"_code: "+auctions.get(i).getIdItem()+", items_"+ i +"_title:"+auctions.get(i).getTitle();
                                }

                                init += aux;
                                outToClient.println(init);
                                break;
                            }

                            case ("bid"): {

                                rmi = invoqueRMI();

                                String idAuction = messageParsed.get("id");

                                float amount = Float.parseFloat(messageParsed.get("amount"));

                                auction = rmi.detailAuction(idAuction);

                                Date date = new Date();
                                Timestamp now = new Timestamp(date.getTime());

                                ArrayList<Bid> bids = auction.getBids();

                                float best;

                                if (bids.size() == 0){
                                    best = auction.getAmount();
                                } else {
                                    best = bids.get(0).getAmount();
                                }

                                if (!now.after(auction.getDeadline())) {

                                    if (amount < best){

                                        Bid bid = new Bid(amount, client.getIdUser(), Integer.parseInt(idAuction));

                                        if (rmi.bid(bid)){
                                            init = "type: bid, ok: true";
                                            outToClient.println(init);
                                        }
                                        else{
                                            init = "type: bid, ok: false";
                                            outToClient.println(init);
                                        }
                                    } else {
                                        init = "type: bid, ok: false";
                                        outToClient.println(init);
                                    }

                                } else {
                                    init = "type: bid, ok: false";
                                    outToClient.println(init);
                                }

                                break;
                            }


                            case ("edit_auction"): {

                                // Variables

                                String code;
                                String title;
                                String description;
                                Timestamp deadline;
                                float amount;
                                String data = "";

                                rmi = invoqueRMI();

                                String idAuction = messageParsed.get("id");

                                Auction old = rmi.detailAuction(idAuction);

                                if (messageParsed.get("code")!=null){
                                    code = messageParsed.get("code");
                                }else {
                                    System.out.println("here");
                                    code = old.getIdItem();
                                }
                                if (messageParsed.get("title")!=null){
                                    title = messageParsed.get("title");
                                }else {
                                    title = old.getTitle();
                                }
                                if (messageParsed.get("description")!=null){
                                    description = messageParsed.get("description");
                                }else {
                                    description = old.getDescription();
                                }
                                if (messageParsed.get("deadline")!=null){

                                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm");
                                    SimpleDateFormat simpleDateFormatnew = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                                    try {
                                        data = simpleDateFormatnew.format(simpleDateFormat.parse(messageParsed.get("deadline")));
                                    }
                                    catch (ParseException pqp){

                                    }

                                    deadline = Timestamp.valueOf(data);


                                }else {
                                    deadline = old.getDeadline();
                                }
                                if (messageParsed.get("amount")!=null){
                                    amount = Float.parseFloat(messageParsed.get("amount"));
                                }else{
                                    amount = old.getAmount();
                                }

                                Auction newAuction = new Auction(old.getIdAuction(),code,title, description, deadline, amount,old.getIdUser(),old.getMessages(),old.getBids());

                                if (rmi.editAuction(old, newAuction, client)){
                                    outToClient.println("type: edit_auction, ok: true");
                                }else {
                                    outToClient.println("type: edit_auction, ok: false");
                                }

                                break;
                            }


                            case ("message") :{

                                int idAuction = Integer.parseInt(messageParsed.get("id"));

                                String text = messageParsed.get("text");

                                Message message = new Message(text, 0, client.getIdUser(), idAuction);

                                message.setUsername(client.getUserName());

                                rmi = invoqueRMI();

                                if (rmi.message(message)){
                                    init = "type: message, ok: true";
                                    outToClient.println(init);
                                }
                                else {
                                    init = "type: message, ok: false";
                                    outToClient.println(init);
                                }
                                break;
                            }


                            case ("online_users"): {
                                rmi = invoqueRMI();

                                ArrayList<Client> clients = rmi.searchOnlineUsers();

                                if (clients.isEmpty()){
                                    init = "type: online_users, users_count: " + clients.size();
                                    outToClient.print(init);
                                }else {
                                    init = "type: online_users, users_count: " + clients.size();
                                    for (int i = 0; i < clients.size(); i++) {
                                        aux += ", users_"+i+"_username: " + clients.get(i).getUserName();
                                    }

                                    init += aux;
                                    outToClient.println(init);
                                }
                                break;
                            }


                            case ("logout"): {

                                rmi = invoqueRMI();
                                rmi.putOffline(client);
                                client = null;
                                break;
                            }

                            default: {
                                break;
                            }

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        outToClient.println("parser problem, correct your string command");
                    }
                }
            } catch (Exception e) {
                outToClient.println("string null repeat please");
                e.printStackTrace();
            }


            try {
                clients.remove(this);
                invoqueRMI().putOffline(client);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            break;
        }
    }



    public RmiInterface invoqueRMI(){


        GetPropertiesValues gpv = new GetPropertiesValues();
        Properties prop = gpv.getProperties();

        int rmiPort = Integer.parseInt(prop.getProperty("rmi1port"));

        String rmiHost;

        boolean runningRMI = true;

        if (runningRMI){
            rmiHost = prop.getProperty("rmi1host");
        }

        else{
            rmiHost = prop.getProperty("rmi2host");
        }

        System.out.println(rmiHost);
        System.out.println(rmiPort);

        RmiInterface rmiInterface = null;

        int numTentativas = 30;

        while(numTentativas>=0){

            try {
                System.getProperties().put("java.security.policy", "security.policy");
                System.setSecurityManager(new RMISecurityManager());
                //Registry xota = LocateRegistry.getRegistry(rmiHost, rmiPort);
                rmiInterface = (RmiInterface) LocateRegistry.getRegistry(rmiHost, rmiPort).lookup("rmi_server");
                numTentativas = 30;
                break;
            } catch (Exception e) {
                System.out.println("Nao encontrou o servidor RMI tentando ligar em " + numTentativas + "s");
                try {
                    Thread.sleep(1000);
                    numTentativas--;
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return rmiInterface;
    }
}

