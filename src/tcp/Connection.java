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
import java.util.*;


class Connection extends Thread {

    PrintWriter outToClient;
    BufferedReader inFromClient = null;
    Socket clientSocket;
    RmiInterface rmi;
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

                                // Guardar o cliente

                                client = new Client(rmi.getClient(messageParsed.get("username"), messageParsed.get("password")).getIdUser(),
                                        messageParsed.get("username"), messageParsed.get("password"));

                                if (rmi.doLogin(client) == true) {
                                    outToClient.println("type: login, ok: true");
                                } else {
                                    outToClient.println("type: login, ok: false");
                                }

                                break;
                            }

                            case ("create_auction"): {
                                auction = new Auction(Integer.parseInt(messageParsed.get("code")), messageParsed.get("title"), messageParsed.get("description"), Date.valueOf(messageParsed.get("deadline")), Integer.parseInt(messageParsed.get("amount")),client.getIdUser());
                                //auction.setIdUser(client.getIdUser());
                                rmi = invoqueRMI();
                                if (rmi.createAuction(auction) == true) {
                                    outToClient.println("type : create_auction , ok: true");
                                } else {
                                    outToClient.println("type : create_auction , ok: false");
                                }
                                break;

                            }

                            case ("search_auction"): {
                                //TODO: Este metodo deve ser revisto, pq acho que nao deve ter 3 na mesma
                                ArrayList <Auction> search;
                                rmi = invoqueRMI();
                                search = rmi.searchAuction(Integer.parseInt(messageParsed.get("code")));
                                System.out.println("TAMANHO:"+search.size());
                                for (int i=0;i<search.size();i++)
                                {
                                    System.out.println(search.get(i).getIdItem());
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
                                //rmi.searchOnlineUsers();
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
                        //System.out.println("Penso que o problema esta no parser: " + e.getLocalizedMessage());
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