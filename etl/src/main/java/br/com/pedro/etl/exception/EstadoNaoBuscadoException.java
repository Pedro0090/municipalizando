package br.com.pedro.etl.exception;

public class EstadoNaoBuscadoException extends RuntimeException {

    public EstadoNaoBuscadoException(String message) {
        super(message);
    }
}
