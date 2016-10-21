package tcp;

import rmi.RmiConnection;
import rmi.RmiInterface;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Created by jorgearaujo on 21/10/16.
 */
public class udpmulticast extends Thread {


    public udpmulticast(Socket clientSockt, ArrayList<Connection> clients)
    {

    }


    //Thread que vai tratar do pedido do cliente
    public void run() {

    }
    }