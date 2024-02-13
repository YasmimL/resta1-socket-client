package br.com.ifce.network;

import br.com.ifce.model.Message;
import br.com.ifce.service.IntegrationService;
import br.com.ifce.service.MessageListener;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class SocketIntegrationService implements IntegrationService {

    private final Socket socket;

    private final ObjectInputStream inputStream;

    private final ObjectOutputStream outputStream;

    private MessageListener listener;

    private Boolean interrupted = false;

    public SocketIntegrationService() {
        var host = "localhost";
        var port = 5000;
        try {
            this.socket = new Socket(host, port);
            this.inputStream = new ObjectInputStream(socket.getInputStream());
            this.outputStream = new ObjectOutputStream(socket.getOutputStream());
            Runnable serverListener = () -> {
                while (!this.interrupted) {
                    try {
                        this.listener.onMessage((Message<?>) this.inputStream.readObject());
                    } catch (Exception e) {
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
    public void send(Message<?> message) {
        try {
            this.outputStream.writeObject(message);
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
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
