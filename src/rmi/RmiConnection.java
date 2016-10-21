package rmi;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;


public class RmiConnection {

    RmiInterface clienteRmi;
    int numTentativas = 0;

    public RmiConnection(RmiInterface clienteRmi){
        this.clienteRmi = clienteRmi;
    }

    public RmiInterface connectToRmi(){

        while (numTentativas<=30)
        {
            try {

                clienteRmi = (RmiInterface) Naming.lookup("rmi://localhost:1099/rmi_server");
                break;
            } catch (Exception e) {
                System.out.println("Nao encontrou o servidor RMI tentando ligar em 30s");
                try {
                    Thread.sleep(2000);
                    numTentativas++;
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                e.printStackTrace();
            }
        }
        return clienteRmi;
    }
}

