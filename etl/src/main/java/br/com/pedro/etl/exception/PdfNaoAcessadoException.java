package br.com.pedro.etl.exception;

public class PdfNaoAcessadoException extends RuntimeException {

    public PdfNaoAcessadoException(String message) {
        super(message);
    }

    public PdfNaoAcessadoException(String message, Throwable cause) {
    super(message, cause);
  }
}
