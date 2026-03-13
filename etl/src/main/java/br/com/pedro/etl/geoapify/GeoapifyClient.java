package br.com.pedro.etl.geoapify;

import br.com.pedro.etl.exception.MunicipioNaoBuscadoException;
import br.com.pedro.etl.geoapify.entidadesGeoapify.GeoapifyResponse;
import br.com.pedro.etl.geoapify.entidadesGeoapify.MunicipioGeoapifyDTO;
import br.com.pedro.etl.ibge.entidadesIBGE.MunicipioIbgeDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class GeoapifyClient {

    private final WebClient webClient;
    private final ExtratorGeoapify extratorGeoapify;
    private final String chave;

    @Autowired
    public GeoapifyClient(WebClient.Builder webClient, ExtratorGeoapify extratorGeoapify,
                          @Value("${app.geoapify.key}") String chave) {
        this.webClient = webClient.baseUrl("https://api.geoapify.com/v1/geocode/")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).build();
        this.extratorGeoapify = extratorGeoapify;
        this.chave = chave;
    }

    public MunicipioGeoapifyDTO buscarCoordenadas(MunicipioIbgeDTO municipioIbgeDTO) {

        List<GeoapifyResponse> listaGeoapifyResponses = new ArrayList<>();

        String nomeMunicipio = municipioIbgeDTO.getNome().equals("Barão do Monte Alto") ?
                "Barão de Monte Alto" : municipioIbgeDTO.getNome();

        String siglaEstado = municipioIbgeDTO.getEstadoIbgeDTO().getSigla();
        log.debug("buscando coordenadas para municipio {} - {}", nomeMunicipio, siglaEstado);
        GeoapifyResponse response = webClient.get()
                .uri(uriBuilder -> uriBuilder.path("search")
                        .queryParam("city", nomeMunicipio)
                        .queryParam("state", siglaEstado)
                        .queryParam("country", "Brazil")
                        .queryParam("result_type", "city")
                        .queryParam("apiKey", chave)
                        .build())
                .retrieve()
                .onStatus(HttpStatusCode::isError, clientResponse -> {
                        log.error("Erro ao buscar coordenadas no geoapify para {} - {}. Status: {}",
                                nomeMunicipio, siglaEstado, clientResponse.statusCode());
                        return Mono.error(new MunicipioNaoBuscadoException("Erro! Municipio não foi encontrado na API Geoapify"));
                })
                .bodyToMono(GeoapifyResponse.class)
                .block();
        listaGeoapifyResponses.add(response);

        return extratorGeoapify.extrairMunicipioGeoapifyDTO(listaGeoapifyResponses);
    }
}
