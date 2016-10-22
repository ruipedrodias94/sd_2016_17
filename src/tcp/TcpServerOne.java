package tcp;

import rmi.RmiInterface;

import java.io.EOFException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.util.ArrayList;


public class TcpServerOne {



    //Funciona como cliente RMI
    public static void main(String[] args) throws IOException, NotBoundException {

        //Dados Server TCP
        ArrayList <Connection> ClientConnections = new ArrayList <Connection>() ;
        int porto = 8000;

        //Socket de ligação ao Cliente
        ServerSocket listenSocket;

        //waiting for client connections
        try
        {
            //Estatico apenas para testes
            InetAddress hostAdress = InetAddress.getByName("localhost");
            listenSocket = new ServerSocket(porto,500,hostAdress);
            System.out.println("[Ligacao TCP à escuta no host: localhost no porto "+porto+"]");

            //Threads de balanceamento de carga
            new udpMulticastReceiver();
            new udpMulticastSender(ClientConnections,"localhost",porto);



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
