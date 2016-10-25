package tcp;

import resources.GetPropertiesValues;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;


public class TcpServerOne {



    //Funciona como cliente RMI
    public static void main(String[] args) throws IOException, NotBoundException {

        //Carregar Configurações
        GetPropertiesValues gpv = new GetPropertiesValues();
        Properties prop = gpv.getProperties();

        //Dados Server TCP
        ArrayList <Connection> ClientConnections = new ArrayList <Connection>() ;
        int porto = Integer.parseInt(prop.getProperty("server1TcpPort"));
        String host = prop.getProperty("server1TcpHost");

        //Socket de ligação ao Cliente
        ServerSocket listenSocket;


        //waiting for client connections
        try
        {
            //Estatico apenas para testesw
            InetAddress hostAdress = InetAddress.getByName(host);
            listenSocket = new ServerSocket(porto,500,hostAdress);
            System.out.println("[Ligacao TCP à escuta no host: "+host+" no porto :"+porto+"]");

            //Threads de balanceamento de carga
            new UdpMulticastReceiver();
            new UdpMulticastSender(ClientConnections,host,porto);



            //Thread de ligação ao cliente TCP
            //noinspection InfiniteLoopStatement
            while(true)
            {
                Socket clientSocket = listenSocket.accept();
                Connection C = new Connection(clientSocket,ClientConnections);
                System.out.println("Numero de users: "+ ClientConnections.size());
            }

        }catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
}
