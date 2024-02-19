package br.com.ifce;

import br.com.ifce.controller.MainViewController;
import br.com.ifce.network.SocketIntegrationService;

public class Main {
    public static void main(String[] args) {
        new MainViewController(SocketIntegrationService.getInstance());
    }
}