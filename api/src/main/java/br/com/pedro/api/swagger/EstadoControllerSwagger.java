package br.com.pedro.api.swagger;

import br.com.pedro.api.domain.DTO.EstadoDTO;
import br.com.pedro.api.domain.DTO.MunicipioDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface EstadoControllerSwagger {

    @Operation(summary = "Lista todos os estados do Brasil", description = "Endpoint que retorna todos os estados do" +
            "Brasil, ou pode retornar um estado específico se adicionado o parâmetro \"nome\"")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estado(s) encontrados e retornados com sucessos")
    })
    public ResponseEntity<List<EstadoDTO>> listarTodos(
            @Parameter(description = "Nome do estado com acentuação", example = "Paraná", required = false) String nome);

    @Operation(summary = "Busca um estado pelo seu id", description = "Endpoint que retorna o estado buscado pelo seu id" +
            "do IBGE")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estado encontrado e retornado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida", content = @Content),
            @ApiResponse(responseCode = "404", description = "Estado não encontrado", content = @Content)
    })
    public ResponseEntity<EstadoDTO> buscarPorId(
            @Parameter(description = "ID do estado segundo o IBGE", example = "12", required = true) Long id);

    @Operation(summary = "Busca municípios do estado", description = "Endpoint que retorna os municípios que fazem" +
            "parte do estado atual")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Municípios retornados com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida", content = @Content),
            @ApiResponse(responseCode = "404", description = "Município não encontrado", content = @Content)
    })
    public ResponseEntity<List<MunicipioDTO>> buscarMunicipiosDoEstado(
            @Parameter(description = "ID do município segundo o IBGE", example = "1100015", required = true) Long id);

}
