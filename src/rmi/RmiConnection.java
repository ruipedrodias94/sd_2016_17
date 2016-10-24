package rmi;

import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.registry.LocateRegistry;

public class RmiConnection {

    RmiInterface clienteRmi = null;
    int numTentativas = 0;
    int rmiPort = 1099;

    public RmiConnection(RmiInterface clienteRmi){
        this.clienteRmi = clienteRmi;
    }

    public RmiInterface connectToRmi(){
        numTentativas = 10;
        while (numTentativas >= 0)
        {
            try {
                System.getProperties().put("java.security.policy", "security.policy");
                System.setSecurityManager(new RMISecurityManager());

                clienteRmi = (RmiInterface) LocateRegistry.getRegistry("localhost", rmiPort).lookup("rmi_server");
                numTentativas = 10;
                break;

            } catch (Exception e) {
                System.out.println("Nao encontrou o servidor RMI tentando ligar em " + numTentativas + "s");
                try {
                    Thread.sleep(1000);
                    numTentativas--;
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }

        return clienteRmi;
    }
}

