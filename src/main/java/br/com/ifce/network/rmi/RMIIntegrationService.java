package br.com.ifce.network.rmi;

import br.com.ifce.model.Message;
import br.com.ifce.model.enums.MessageType;
import br.com.ifce.service.IntegrationService;
import br.com.ifce.service.MessageListener;
import lombok.Getter;
import lombok.Setter;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class RMIIntegrationService extends UnicastRemoteObject implements ClientRemote, IntegrationService {
    @Setter
    private MessageListener listener;

    @Getter
    @Setter
    private String playerKey;

    @Getter
    @Setter
    private String currentPlayer;

    @Getter
    @Setter
    private boolean gameFinished = false;

    private final ServerRemote server;

    private static IntegrationService instance;

    protected RMIIntegrationService() throws RemoteException {
        super();
        try {
            Registry registry = LocateRegistry.getRegistry();
            this.server = (ServerRemote) registry.lookup("Server");
            this.playerKey = this.server.registerClient(this);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static IntegrationService getInstance() {
        if (instance == null) {
            try {
                instance = new RMIIntegrationService();
            } catch (RemoteException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }

        return instance;
    }

    @Override
    public void send(Message<?> message) {
        try {
            this.server.onMessage(this.playerKey, message);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() {
        this.listener.onMessage(new Message<>(MessageType.CLOSE, null));
    }

    @Override
    public void onMessage(Message<?> message) throws RemoteException {
        this.listener.onMessage(message);
    }
}
