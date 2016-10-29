package tcp;

import components.Message;
import resources.GetPropertiesValues;
import rmi.ServerCallbackInterface;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

 class HelloClient extends UnicastRemoteObject implements CallbackInterface
{
    ArrayList<Connection> clientsToNotificate;

    HelloClient( ArrayList<Connection> c) throws RemoteException {
        super();
        this.clientsToNotificate = c;

    }


    public void printOnClient(Message m,String writer, int idUser) throws RemoteException {
        for(int i = 0;i<clientsToNotificate.size();i++)
        {
            String notification;
            notification = "type: notification_message, id: "+m.getIdAuction()+", user: "+writer+", text: "+m.getText();
            if(clientsToNotificate.get(i).client.getIdUser() == idUser) {
                clientsToNotificate.get(i).outToClient.println(notification);
            }
        }

    }
}


public class TcpServerOne {

    static HelloClient c;

    public TcpServerOne() throws RemoteException {
    }

    //Funciona como cliente RMI
    public static void main(String[] args) throws IOException, NotBoundException {

        //Carregar Configurações
        GetPropertiesValues gpv = new GetPropertiesValues();
        Properties prop = gpv.getProperties();

        //Dados Server TCP
        ArrayList <Connection> ClientConnections = new ArrayList <Connection>() ;
        int porto = Integer.parseInt(prop.getProperty("server2TcpPort"));
        //String host = prop.getProperty("server1TcpHost");

        //Socket de ligação ao Cliente
        ServerSocket listenSocket;

        try
        {
            c = new HelloClient(ClientConnections);
            ServerCallbackInterface h = (ServerCallbackInterface) Naming.lookup("hello");
            h.subscribe(" Servidor TCP ", (CallbackInterface) c);
            System.out.println("Server TCP sent subscription to RMI Server");
        }catch (Exception e)
        {
            e.printStackTrace();
        }

        //waiting for client connections
        try
        {
            //InetAddress hostAdress = InetAddress.getLocalHost();
            listenSocket = new ServerSocket(porto);
            System.out.println("[Ligacao TCP à escuta no host:  no porto :" + porto + "]");

            //Threads de balanceamento de carga
            new UdpMulticastReceiver();
            new UdpMulticastSender(ClientConnections, "localhost" ,porto);

            //Thread de ligação ao cliente TCP
            //noinspection InfiniteLoopStatement
            while(true)
            {
                Socket clientSocket = listenSocket.accept();
                Connection C = new Connection(clientSocket,ClientConnections,c);
                System.out.println("Numero de users: "+ ClientConnections.size());
            }
        }catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
}
