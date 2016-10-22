package tcp;



import rmi.RmiConnection;
import rmi.RmiInterface;

import java.io.*;
import java.net.*;
import java.rmi.RemoteException;
import java.sql.*;
import java.util.*;
/**
 * Created by jorgearaujo on 19/10/16.
 */
class Connection extends Thread {

    DataInputStream in;
    DataOutputStream out;
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
            in = new DataInputStream(clientSocket.getInputStream());
            this.out = new DataOutputStream(clientSocket.getOutputStream());

            out.writeUTF("Bem vindo ao iBEi\n");
            this.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //Thread que vai tratar do pedido do cliente
    public void run()
    {
        rmiConnection = new RmiConnection(rmi);
        while (true){

            try
            {
                while(true)
                {
                    in = new DataInputStream(clientSocket.getInputStream());
                    String data = in.readUTF();
                    System.out.println("Recebeu: "+data);
                    System.out.println("Foi invocada uma nova chamada ao servidor rmi");
                    rmi = rmiConnection.connectToRmi();
                    boolean consegiu = rmi.doLogin("ruidias", "123");
                    if (consegiu)
                    this.out.writeUTF("Caralho cliente de piça");
                }
            } catch (EOFException e) {
                this.clients.remove(this);
                System.out.println("Cliente Desligado");
                System.out.println("Numero de users: "+ clients.size());
                break;

            } catch (IOException e) {
                //e.printStackTrace();
                System.out.println("RMI Desligado religue e digite novo comando");
                break;
            }
        }

    }
}