package tcp;



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

                        switch (type) {
                            case ("register"): {
                                //chamada aqui para registar;
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