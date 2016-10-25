package tcp;



import components.Client;
import helpers.ProtocolParser;
import rmi.RmiConnection;
import rmi.RmiInterface;

import java.io.*;
import java.net.*;
import java.rmi.RemoteException;
import java.sql.*;
import java.text.ParseException;
import java.util.*;


class Connection extends Thread {

    PrintWriter outToClient;
    BufferedReader inFromClient = null;
    Socket clientSocket;
    RmiInterface rmi;
    RmiConnection rmiConnection;
    ArrayList<Connection> clients = null;


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
                        rmi = rmiConnection.connectToRmi();

                        //TODO: Completar o "menu", criar as instancias de cada merda, e fazer o parse da string recebida
                        //TODO: Aqui é para fazer a intrepertação das merdas

                        switch (type) {
                            case ("register"): {
                                //chamada aqui para registar;
                                System.out.println("Tentou fazer registo");
                                outToClient.println("bom registo");
                                break;
                            }

                            case ("login") :{
                                // Nao esquecer de criar o Cliente depois de intrepertar as merdas
                                // Guardar o cliente
                                // rmi.doLogin();
                                break;
                            }


                            case ("create_auction"): {
                                //rmi.createAuction();
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
                        outToClient.println("parser problem, correct your string command");
                        System.out.println("Penso que o problema esta no parser: " + e.getLocalizedMessage());
                    }
                }
            } catch (Exception e) {
                System.out.println("Não sei");
                e.printStackTrace();
            }
            clients.remove(this);
        }
    }
}