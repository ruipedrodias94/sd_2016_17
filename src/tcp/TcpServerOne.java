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


public class TcpServerOne {


    //Funciona como cliente RMI
    public static void main(String[] args) throws IOException, NotBoundException {

        //Dados Server TCP
        int n_users = 0;
        RmiInterface rmii = null;

        //Socket de ligação ao Cliente
        ServerSocket listenSocket;

        //Ligação ao RMI
        try {
            RmiInterface clienteRmi = (RmiInterface) Naming.lookup("rmi://localhost:1099/rmi_server");
            rmii = clienteRmi;
            int a = clienteRmi.teste();
            System.out.println(a);
        } catch (NotBoundException e) {
            System.out.println("Nao encontrou o servidor RMI");
            e.printStackTrace();
        }

        //waiting for client connections
        try
        {
            //Estatico apenas para testes
            InetAddress hostAdress = InetAddress.getByName("localhost");
            listenSocket = new ServerSocket(7000,500,hostAdress);
            System.out.println("[Ligacao TCP à escuta no host: localhost no porto 7000]");

            //Thread de ligação ao cliente TCP
            //noinspection InfiniteLoopStatement
            while(true)
            {
                Socket clientSocket = listenSocket.accept();
                n_users++;
                new Connection(clientSocket,n_users,rmii);
                System.out.println("Numero de users: "+ n_users);
            }

        }catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
}
