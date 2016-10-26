package tcp;

import resources.GetPropertiesValues;
import rmi.RmiConnection;
import rmi.RmiInterface;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.*;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Properties;

/**
 * Created by jorgearaujo on 21/10/16.
 */
public class UdpMulticastSender extends Thread {

    ArrayList<Connection> clients = null;
    int porto;
    String host;

    public UdpMulticastSender(ArrayList<Connection> clients, String host, int porto)
    {
        this.clients = clients;
        this.host = host;
        this.porto = porto;
        this.start();

    }



    public void run()
    {

        //Carrega Ficheiro de propriedades
        GetPropertiesValues gpv = new GetPropertiesValues();
        Properties prop = gpv.getProperties();

        //thread que vai mandar de tempos a tempos a informação relativa à carga do servidor
        //Sender
        MulticastSocket socket = null;
        int port = Integer.parseInt(prop.getProperty("multicastPort"));
        String group = prop.getProperty("multicastGroup");
        int ttl = Integer.parseInt(prop.getProperty("multicastTTL"));

        while(true){
            try
            {
                //Criar Socket sem fazer bind porque é apenas para mandar info
                socket = new MulticastSocket(port);
                socket.setTimeToLive(2);

                String numberClientsMessage = " servidor: "+host+" no porto: "+porto+" Número de clientes --> "+clients.size();
                byte[] buf = numberClientsMessage.getBytes();
                DatagramPacket msgOut = new DatagramPacket(buf,buf.length,InetAddress.getByName(group),port);
                socket.send(msgOut);
                Thread.sleep(Integer.parseInt(prop.getProperty("multicastRefreshTime")));
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