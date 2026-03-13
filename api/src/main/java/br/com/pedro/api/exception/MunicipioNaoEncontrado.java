package br.com.pedro.api.exception;

public class MunicipioNaoEncontrado extends RuntimeException {

    private static final String MSG_PADRAO = "Erro! Nenhum município foi encontrado";

    public MunicipioNaoEncontrado() {
        super(MSG_PADRAO);
    }

    public MunicipioNaoEncontrado(String message) {
        super(message);
    }
}
