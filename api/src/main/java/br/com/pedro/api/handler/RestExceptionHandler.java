package br.com.pedro.api.handler;

import br.com.pedro.api.exception.EstadoNaoEcontrado;
import br.com.pedro.api.exception.MunicipioNaoEncontrado;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.net.URI;

@Slf4j
@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(EstadoNaoEcontrado.class)
    public ResponseEntity<ProblemDetail> handleEstadoNaoEcontrado(EstadoNaoEcontrado ex, HttpServletRequest request) {

        log.warn("Estado não encontrado. Path={}",  request.getRequestURI());

        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.NOT_FOUND.value());

        problem.setDetail(ex.getMessage());
        problem.setTitle("Estado não encontrado");
        problem.setInstance(URI.create(request.getRequestURI()));

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problem);
    }

    @ExceptionHandler(MunicipioNaoEncontrado.class)
    public ResponseEntity<ProblemDetail> handleMunicipioNaoEcontrado(MunicipioNaoEncontrado ex, HttpServletRequest request) {

        log.warn("Município não encontrado. Path={}",  request.getRequestURI());

        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.NOT_FOUND.value());

        problem.setDetail(ex.getMessage());
        problem.setTitle("Município não encontrado");
        problem.setInstance(URI.create(request.getRequestURI()));

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problem);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ProblemDetail> handleIllegalArgumentException(IllegalArgumentException ex, HttpServletRequest request) {

        log.warn("Parâmetro inválido na requisição. Path={}, Erro={}",  request.getRequestURI(), ex.getMessage());

        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST.value());

        problem.setDetail(ex.getMessage());
        problem.setTitle("Muncipio não pode ser buscado");
        problem.setInstance(URI.create(request.getRequestURI()));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problem);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ProblemDetail> handleMethodArgumentTypeMismatchException(HttpServletRequest request) {

        log.warn("Erro de tipo de argumento na requisição. Path={}",  request.getRequestURI());

        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST.value());

        problem.setDetail("Requisição inválida!");
        problem.setTitle("Erro na requisição");
        problem.setInstance(URI.create(request.getRequestURI()));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problem);
    }

    @Override
    public ResponseEntity<Object> handleNoResourceFoundException(NoResourceFoundException ex, HttpHeaders headers,
                                                                 HttpStatusCode status, WebRequest request) {

        log.warn("Recurso não encontrado. Path={}", ex.getResourcePath());

        ProblemDetail problem = ProblemDetail.forStatus(status.value());

        problem.setDetail("O caminho '%s' não existe".formatted(ex.getResourcePath()));
        problem.setTitle("Recurso não encontrado");
        problem.setInstance(URI.create(((ServletWebRequest) request).getRequest().getRequestURI()));

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problem);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleAllUncaughtException(Exception ex, HttpServletRequest request) {

        log.debug("Erro inesperado no servidor. Path={}",  request.getRequestURI(), ex);

        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());

        problem.setDetail("Ocorreu um erro inesperado! Tente novamente mais tarde");
        problem.setTitle("Erro interno do servidor");
        problem.setInstance(URI.create(request.getRequestURI()));

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(problem);
    }
}
