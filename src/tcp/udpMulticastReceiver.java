package tcp;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;

/**
 * Created by jorgearaujo on 22/10/16.
 */
public class udpMulticastReceiver extends Thread {


    MulticastSocket socket;


    public udpMulticastReceiver()
    {

        this.start();

    }



    public void run()
    {
        //thread que vai receber a informação relativa à carga do servidor
        //Receiver
        int port = 5000;
        String group = "225.4.5.6";
        int ttl = 1;


        while(true){
            try
            {
                //Criar Socket sem fazer bind porque é apenas para mandar info
                socket = new MulticastSocket(port);


                //fazer join ao grupo de multicast
                socket.joinGroup(InetAddress.getByName(group));

                //Receber pacote
                byte buf[] = new byte[1024];
                DatagramPacket pack = new DatagramPacket(buf,buf.length);
                socket.receive(pack);

                //Imprimir info
                String numberClientsMessage = "Número de clientes ligados ao";
                System.out.print(numberClientsMessage);
                System.out.write(pack.getData(),0,pack.getLength());
                System.out.println("\n");


            } catch (UnknownHostException e) {
                try {
                    socket.leaveGroup(InetAddress.getByName(group));
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                socket.close();
                e.printStackTrace();

            } catch (IOException e) {
                try {
                    socket.leaveGroup(InetAddress.getByName(group));
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                socket.close();
                e.printStackTrace();
            }
        }
    }
}