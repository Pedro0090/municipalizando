package br.com.pedro.etl.exception;

public class MunicipioNaoBuscadoException extends RuntimeException {

    public MunicipioNaoBuscadoException() {}

    public MunicipioNaoBuscadoException(String message) {
        super(message);
    }
}
