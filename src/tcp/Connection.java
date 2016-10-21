package tcp;



import rmi.RmiInterface;

import java.io.*;
import java.net.*;
import java.sql.*;
import java.util.*;
/**
 * Created by jorgearaujo on 19/10/16.
 */
 class Connection extends Thread {

    DataInputStream in;
    DataOutputStream out;
    Socket clientSocket;
    int threadNumber;
    RmiInterface rmi;

    public Connection(Socket clientSockt, int clientNumber, RmiInterface RmiI)
    {
        threadNumber = clientNumber;
        rmi = RmiI;
        try
        {
            clientSocket = clientSockt;
            in = new DataInputStream(clientSocket.getInputStream());
            out = new DataOutputStream(clientSocket.getOutputStream());
            out.writeUTF("Bem vindo ao iBEi\n");
            this.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //Thread que vai tratar do pedido do cliente
    public void run()
    {
        try
        {
            while(true)
            {
                in = new DataInputStream(clientSocket.getInputStream());
                String data = in.readUTF();
                System.out.println("Recebeu: "+data);
                System.out.println(rmi.teste());

            }
        } catch (EOFException e) {
            System.out.println("Cliente Desligado");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    }

