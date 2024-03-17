package br.com.ifce.network.rmi;

import br.com.ifce.model.Message;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientRemote extends Remote {
    void onMessage(Message<?> message) throws RemoteException;
}
