package br.com.pedro.etl.ibge;

import br.com.pedro.etl.exception.EstadoNaoBuscadoException;
import br.com.pedro.etl.exception.MunicipioNaoBuscadoException;
import br.com.pedro.etl.ibge.entidadesIBGE.EstadoIbgeDTO;
import br.com.pedro.etl.ibge.entidadesIBGE.MunicipioIbgeDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Component
public class IbgeClient {

    private final WebClient webClient;

    @Autowired
    public IbgeClient(WebClient.Builder webClient) {
        this.webClient = webClient.baseUrl("https://servicodados.ibge.gov.br/api/v1/localidades")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).build();
    }

    public List<MunicipioIbgeDTO> listarMunicipios() {

        log.info("Buscando municípios na API do IBGE");

        return webClient.get()
                .uri("/municipios")
                .retrieve()
                .onStatus(HttpStatusCode::isError, clientResponse -> {
                        log.error("Erro ao buscar municipios na API do IBGE. Status: {}", clientResponse.statusCode());
                        return Mono.error(new MunicipioNaoBuscadoException("Erro! Não foi possível buscar municipios " +
                                "na API do IBGE"));
                })
                .bodyToFlux(MunicipioIbgeDTO.class)
                .collectList()
                .block();
    }

    public List<EstadoIbgeDTO> listarEstados() {

        log.info("Buscando estados na API do IBGE");

        return webClient.get()
                .uri("/estados")
                .retrieve()
                .onStatus(HttpStatusCode::isError, clientResponse -> {
                        log.error("Erro ao buscar estados na API do IBGE. Status: {}", clientResponse.statusCode());
                        return Mono.error(new EstadoNaoBuscadoException("Erro! Não foi possível buscar estados na " +
                                "API do IBGE"));
                })
                .bodyToFlux(EstadoIbgeDTO.class)
                .collectList()
                .block();
    }
}
