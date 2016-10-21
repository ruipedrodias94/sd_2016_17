package rmi;

import java.rmi.Naming;

public class RmiConnection {

    RmiInterface clienteRmi = null;
    int numTentativas = 0;

    public RmiConnection(RmiInterface clienteRmi){
        this.clienteRmi = clienteRmi;
    }

    public RmiInterface connectToRmi(){
        numTentativas = 10;
        while (numTentativas >= 0)
        {
            try {
                clienteRmi = (RmiInterface) Naming.lookup("rmi://localhost:1099/rmi_server");
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

