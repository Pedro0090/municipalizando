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

public interface MunicipioControllerSwagger {

    @Operation(summary = "Lista todos os municípios do Brasil", description = "Endpoint que retorna todos os municípios do" +
            "Brasil, ou pode retornar município(s) específico(s) se adicionado o parâmetro \"nome\" e \"estado\"")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Município(s) encontrados e retornados com sucessos"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida", content = @Content)
    })
    public ResponseEntity<List<MunicipioDTO>> listarTodos(
            @Parameter(description = "Nome do município com acentuação", example = "Pingo-d'Água", required = false) String nome,
            @Parameter(description = "Nome ou sigla do estado", example = "MG ou Minas Gerais", required = false) String estado);

    @Operation(summary = "Busca um município pelo seu id", description = "Endpoint que retorna o município buscado pelo seu id" +
            "do IBGE")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Município encontrado e retornado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida", content = @Content),
            @ApiResponse(responseCode = "404", description = "Município não encontrado", content = @Content)
    })
    public ResponseEntity<MunicipioDTO> buscarPorId(
            @Parameter(description = "ID do município segundo o IBGE", example = "1100015", required = true) Long id);

    @Operation(summary = "Busca estado do município", description = "Endpoint que retorna o estado em que está localizado" +
            "o município atual")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estado retornado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida", content = @Content),
            @ApiResponse(responseCode = "404", description = "Municipio não encontrado", content = @Content)
    })
    public ResponseEntity<EstadoDTO> buscarEstadoDoMunicipio(
            @Parameter(description = "ID do município segundo o IBGE", example = "1100015", required = true) Long id);
}
