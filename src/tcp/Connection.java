package tcp;



import components.Auction;
import components.Client;
import helpers.ProtocolParser;
import rmi.RmiConnection;
import rmi.RmiInterface;

import java.io.*;
import java.net.*;
import java.rmi.RemoteException;
import java.sql.*;
import java.sql.Date;
import java.text.ParseException;
import java.util.*;


class Connection extends Thread {

    PrintWriter outToClient;
    BufferedReader inFromClient = null;
    Socket clientSocket;
    RmiInterface rmi;
    RmiConnection rmiConnection;
    ArrayList<Connection> clients = null;

    //assim que se faça o login atribui-se o userID nesta variável
    int userID;


    public Connection(Socket clientSockt, ArrayList<Connection> clients)
    {
        this.clients = clients;
        this.clients.add(this);
        try
        {
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
    public void run()
    {
        rmiConnection = new RmiConnection(rmi);
        String messageFromClient;
        HashMap<String, String> messageParsed;
        String type;
        String answer;
        Client client;
        Auction auction;
        boolean result;

        while (true){

            try
            {
                inFromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                while((messageFromClient = inFromClient.readLine()) != null) {

                    try {
                        System.out.println(messageFromClient);
                        messageParsed = ProtocolParser.parse(messageFromClient);
                        type = messageParsed.get("type");

                        //TODO: Completar o "menu", criar as instancias , e fazer o parse da string recebida
                        //TODO: Aqui é para fazer a intrepertação das strings

                        switch (type) {
                            case ("register"): {
                                //chamada aqui para registar;
                                client = new Client(1,messageParsed.get("username"),messageParsed.get("password"));

                                rmi = rmiConnection.connectToRmi();

                                if(rmi.registerClient(client)==true)
                                {
                                    outToClient.println("type: register, ok: true");
                                }
                                else
                                {
                                    outToClient.println("type: register, ok: false");
                                }
                                break;
                            }

                            case ("login") :{
                                // Nao esquecer de criar o Cliente depois de intrepertar as merdas
                                // Guardar o cliente
                                client = new Client(1,messageParsed.get("username"),messageParsed.get("password"));
                                if(rmi.doLogin(client)==true)
                                {
                                    outToClient.println("type: login, ok: true");
                                    userID = rmi.returnUserID(client);
                                }
                                else
                                {
                                    outToClient.println("type: login, ok: true");
                                }
                                break;
                            }


                            case ("create_auction"): {
                                auction = new Auction(Integer.parseInt(messageParsed.get("code")),messageParsed.get("title"),messageParsed.get("description"), Date.valueOf(messageParsed.get("deadline")),Integer.parseInt(messageParsed.get("amount")));
                                auction.setIdUser(userID);
                                if(rmi.createAuction(auction)==true)
                                {
                                    outToClient.println("type : create_auction , ok: true");
                                }
                                else
                                {
                                    outToClient.println("type : create_auction , ok: false");
                                }
                                break;
                            }

                            case ("search_auction"): {
                                //TODO: Este metodo deve ser revisto, pq acho que nao deve ter 3 merdas na mesma merda
                                //rmi.searchAuction();
                                break;
                            }

                            case ("detail_auction") :{
                                // TODO: Este tb
                                //rmi.searchAuction();
                                break;
                            }

                            case ("my_auctions") :{
                                //rmi.searchAuction();
                                break;
                            }

                            case ("bid") :{
                                //rmi.bid();
                                break;
                            }

                            case ("edit_auction") :{
                                //rmi.editAuction();
                                break;
                            }

                            case ("online_users") :{
                                //rmi.searchOnlineUsers();
                                break;
                            }

                            default :{
                                break;
                            }

                        }
                    }catch (Exception e){
                        e.printStackTrace();
                        outToClient.println("parser problem, correct your string command");
                        System.out.println("Penso que o problema esta no parser: " + e.getLocalizedMessage());
                    }
                }
            } catch (Exception e) {
                outToClient.println("string null repeat please");
                e.printStackTrace();
            }
            clients.remove(this);
        }
    }
}