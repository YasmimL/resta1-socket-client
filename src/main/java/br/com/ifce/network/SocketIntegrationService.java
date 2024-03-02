package br.com.ifce.network;

import br.com.ifce.model.Message;
import br.com.ifce.model.enums.MessageType;
import br.com.ifce.service.IntegrationService;
import br.com.ifce.service.MessageListener;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class SocketIntegrationService implements IntegrationService {

    private final Socket socket;

    private MessageListener listener;

    private Boolean interrupted = false;

    @Getter
    @Setter
    private String playerKey;

    @Getter
    @Setter
    private String currentPlayer;

    @Getter
    @Setter
    private boolean gameFinished = false;

    private static IntegrationService instance;

    public static IntegrationService getInstance() {
        if (instance == null) instance = new SocketIntegrationService();

        return instance;
    }

    private SocketIntegrationService() {
        var host = "127.0.0.1";
        var port = 5000;
        try {
            this.socket = new Socket(host, port);
            Runnable serverListener = () -> {
                while (!this.interrupted) {
                    try {
                        var inputStream = new ObjectInputStream(this.socket.getInputStream());
                        this.listener.onMessage((Message<?>) inputStream.readObject());
                    } catch (Exception e) {
                        this.close();
                        throw new RuntimeException(e);
                    }
                }
            };
            new Thread(serverListener).start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean isPlayerTurn() {
        return this.playerKey.equals(this.currentPlayer);
    }

    @Override
    public void send(Message<?> message) {
        try {
            var outputStream = new ObjectOutputStream(this.socket.getOutputStream());
            outputStream.writeObject(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setMessageListener(MessageListener listener) {
        this.listener = listener;
    }

    @Override
    public void close() {
        this.interrupted = true;
        try {
            this.socket.close();
            this.listener.onMessage(new Message<>(MessageType.CLOSE, null));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
