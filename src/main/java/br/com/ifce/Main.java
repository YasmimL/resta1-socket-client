package br.com.ifce;

import br.com.ifce.controller.MainViewController;
import br.com.ifce.service.IntegrationService;

public class Main {
    public static void main(String[] args) {
        new MainViewController(IntegrationService.getInstance());
    }
}