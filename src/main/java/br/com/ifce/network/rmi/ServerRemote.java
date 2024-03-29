package br.com.ifce.network.rmi;

import br.com.ifce.model.Message;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerRemote extends Remote {
    String registerClient(ClientRemote client) throws RemoteException;

    void onMessage(String playerKey, Message<?> message) throws RemoteException;
}
