package br.com.ifce.service;

import br.com.ifce.model.Message;

public interface IntegrationService {
    void send(Message<?> message);

    void setMessageListener(MessageListener listener);

    void close();
}
