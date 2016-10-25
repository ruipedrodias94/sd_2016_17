package rmi;

import resources.GetPropertiesValues;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.rmi.RMISecurityManager;
import java.rmi.registry.LocateRegistry;
import java.util.Properties;

public class RmiConnection {

    RmiInterface clienteRmi = null;
    int numTentativas = 0;
    int rmiPort ;
    String rmiHost;



    public RmiConnection(RmiInterface clienteRmi) {
        this.clienteRmi = clienteRmi;
    }

    public RmiInterface connectToRmi() {

        GetPropertiesValues gpv = new GetPropertiesValues();
        Properties prop = gpv.getProperties();

        rmiHost = prop.getProperty("rmi1host");
        rmiPort = Integer.parseInt(prop.getProperty("rmi1port"));

        numTentativas = 30;
        //while(numTentativas>=0){ Este ciclo ta fodido
            try {
                System.getProperties().put("java.security.policy", "security.policy");
                System.setSecurityManager(new RMISecurityManager());
                clienteRmi = (RmiInterface) LocateRegistry.getRegistry("localhost", rmiPort).lookup("rmi_server");


            } catch (Exception e) {
                System.out.println("Nao encontrou o servidor RMI tentando ligar em " + numTentativas + "s");
                try {
                    Thread.sleep(1000);
                    numTentativas--;
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        //}

        return clienteRmi;
    }




}