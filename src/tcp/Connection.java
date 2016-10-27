package tcp;



import components.Auction;
import components.Client;
import helpers.ProtocolParser;
import resources.GetPropertiesValues;
import rmi.RmiInterface;

import java.io.*;
import java.net.*;
import java.rmi.RMISecurityManager;
import java.rmi.registry.LocateRegistry;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


class Connection extends Thread {

    PrintWriter outToClient;
    BufferedReader inFromClient = null;
    Socket clientSocket;
    ArrayList<Connection> clients = null;


    public Connection(Socket clientSockt, ArrayList<Connection> clients) {
        this.clients = clients;
        this.clients.add(this);
        try {
            clientSocket = clientSockt;
            // create streams for writing to and reading from the socket

            outToClient = new PrintWriter(clientSocket.getOutputStream(), true);

            outToClient.println("Bem vindo ao iBEi\n");

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
        Client client = null;
        Auction auction;
        boolean result;
        RmiInterface rmi = null;


        while (true) {

            try {
                inFromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                while ((messageFromClient = inFromClient.readLine()) != null) {

                    try {
                        System.out.println(messageFromClient);
                        messageParsed = ProtocolParser.parse(messageFromClient);
                        type = messageParsed.get("type");

                        switch (type) {
                            case ("register"): {

                                rmi = invoqueRMI();

                                if (rmi!=null){
                                    System.out.println("Epah que qualidade, criaste uma interface com sucesso.");
                                }
                                if (rmi.registerClient(messageParsed.get("username"), messageParsed.get("password"))) {
                                    outToClient.println("type: register, ok: true");
                                } else {
                                    outToClient.println("type: register, ok: false");
                                }
                                break;
                            }

                            case ("login"): {

                                rmi = invoqueRMI();

                                //Get that cliente back online
                                client = rmi.getClient(messageParsed.get("username"), messageParsed.get("password"));

                                if (rmi.doLogin(client)) {
                                    outToClient.println("type: login, ok: true");
                                } else {
                                    outToClient.println("type: login, ok: false");
                                }

                                break;
                            }

                            case ("create_auction"): {

                                int idAuction = Integer.parseInt(messageParsed.get("code"));
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

                                auction = new Auction(idAuction, title, description, newData, amount, client.getIdUser());

                                //Chamada ao RMI

                                rmi = invoqueRMI();

                                if (rmi.createAuction(auction)) {
                                    outToClient.println("type : create_auction , ok: true");
                                } else {
                                    outToClient.println("type : create_auction , ok: false");
                                }
                                break;
                            }

                            case ("search_auction"): {

                                int code = Integer.parseInt(messageParsed.get("code"));

                                rmi = invoqueRMI();

                                ArrayList<Auction> auctions = rmi.searchAuction(code);

                                for (int i = 0; i < auctions.size(); i++) {
                                    outToClient.print(auctions.get(i).getTitle());
                                    System.out.println(auctions.get(i).getTitle());
                                }

                                break;
                            }

                            case ("detail_auction"): {
                                // TODO: Este tb
                                //rmi.searchAuction();
                                break;
                            }

                            case ("my_auctions"): {
                                //rmi.searchAuction();
                                break;
                            }

                            case ("bid"): {
                                //rmi.bid();
                                break;
                            }

                            case ("edit_auction"): {
                                //rmi.editAuction();
                                break;
                            }

                            case ("online_users"): {
                                rmi = invoqueRMI();

                                ArrayList<Client> clients = rmi.searchOnlineUsers();

                                for (int i = 0; i < clients.size(); i++) {
                                    System.out.println(clients.get(i).getUserName());
                                }
                                break;
                            }

                            case ("logout"): {
                                //rmi = rmiConnection.connectToRmi();

                                //rmi.putOffline(client);
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
            clients.remove(this);
        }
    }


    public RmiInterface invoqueRMI(){


        GetPropertiesValues gpv = new GetPropertiesValues();
        Properties prop = gpv.getProperties();

        int rmiPort = Integer.parseInt(prop.getProperty("rmi1port"));

        String rmiHost;

        boolean runningRMI =true;

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