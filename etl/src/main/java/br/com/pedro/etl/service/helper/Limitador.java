package br.com.pedro.etl.service.helper;

import org.springframework.stereotype.Component;

@Component
public class Limitador {

    public void aguardar() {
        try {
            Thread.sleep(250);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }
}
