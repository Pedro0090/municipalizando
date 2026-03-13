package br.com.pedro.etl.infra.ratelimit;

public class Intervalo {

    int inicio;
    int fim;

    public Intervalo(int inicio, int fim) {
        this.inicio = inicio;
        this.fim = fim;
    }

    public int inicio() {
        return inicio;
    }

    public int fim() {
        return fim;
    }

}
