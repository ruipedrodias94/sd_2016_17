package tcp;



import helpers.ProtocolParser;
import rmi.RmiConnection;
import rmi.RmiInterface;

import java.io.*;
import java.net.*;
import java.rmi.RemoteException;
import java.sql.*;
import java.util.*;


class Connection extends Thread {

    PrintWriter outToClient;
    BufferedReader inFromClient = null;
    Socket clientSocket;
    RmiInterface rmi;
    RmiConnection rmiConnection;
    ArrayList<Connection> clients = null;

    //Teste do parser
    HashMap<String, String> m = null;

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

        while (true){

            try
            {
                inFromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                while((messageFromClient = inFromClient.readLine()) != null)
                {

                    System.out.println("Recebeu: "+messageFromClient);
                    System.out.println("Foi invocada uma nova chamada ao servidor rmi");
                    rmi = rmiConnection.connectToRmi();
                   /*
                    m = ProtocolParser.parse(data);
                    if (m.get("type").equals("search_users")){
                       String teste = rmi.searchUsers();
                        out.writeUTF(teste);
                    }*/}


                } catch (IOException e) {
                e.printStackTrace();
            }
            clients.remove(this);

        }

    }
}