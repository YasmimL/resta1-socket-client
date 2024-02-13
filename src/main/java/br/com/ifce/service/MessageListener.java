package br.com.ifce.service;

import br.com.ifce.model.Message;

public interface MessageListener {
    void onMessage(Message<?> message);
}
