package tcp;

import rmi.RmiConnection;
import rmi.RmiInterface;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.*;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Created by jorgearaujo on 21/10/16.
 */
public class udpMulticastSender extends Thread {

    ArrayList<Connection> clients = null;
    int porto;
    String host;

    public udpMulticastSender( ArrayList<Connection> clients, String host, int porto)
    {
        this.clients = clients;
        this.host = host;
        this.porto = porto;
        this.start();

    }



    public void run()
    {
        //thread que vai mandar de tempos a tempos a informação relativa à carga do servidor
        //Sender
        MulticastSocket socket = null;
        int port = 5000;
        String group = "225.4.5.6";
        int ttl = 1;
        while(true){
            try
            {
                //Criar Socket sem fazer bind porque é apenas para mandar info
                socket = new MulticastSocket(port);
                socket.setTimeToLive(2);

                String numberClientsMessage = " servidor host: "+host+" no porto: "+porto+" Número de clientes -->"+clients.size();
                byte[] buf = numberClientsMessage.getBytes();
                DatagramPacket msgOut = new DatagramPacket(buf,buf.length,InetAddress.getByName(group),port);
                socket.send(msgOut);
                Thread.sleep(10000);
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}