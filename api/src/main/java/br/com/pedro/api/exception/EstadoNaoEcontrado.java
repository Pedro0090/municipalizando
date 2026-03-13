package br.com.pedro.api.exception;

public class EstadoNaoEcontrado extends RuntimeException {

    private static final String MSG_PADRAO = "Erro! Nenhum estado foi encontrado";

    public EstadoNaoEcontrado() {
        super(MSG_PADRAO);
    }

    public EstadoNaoEcontrado(String message) {
        super(message);
    }
}
