package br.com.ifce.controller;

import br.com.ifce.model.Message;
import br.com.ifce.service.IntegrationService;
import br.com.ifce.service.MessageListener;

import java.util.Arrays;

public class MainViewController implements MessageListener {

    private IntegrationService service;

    public MainViewController(IntegrationService service) {
        this.service = service;
        this.service.setMessageListener(this);
    }

    @Override
    public void onMessage(Message<?> message) {
        for (int[] row : (int[][]) message.getPayload())
            System.out.println(Arrays.toString(row));
    }
}
