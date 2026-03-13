package br.com.pedro.api.controller;

import br.com.pedro.api.exception.EstadoNaoEcontrado;
import br.com.pedro.api.service.EstadoService;
import br.com.pedro.api.util.GeradorDeEntidades;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.when;

@WebMvcTest(EstadoController.class)
class EstadoControllerTest {

    @Autowired
    private MockMvcTester mockMvcTester;

    @MockitoBean
    private EstadoService estadoService;

    @Test
    @DisplayName("Deve retornar todos os estados quando nenhum filtro filtro for indicado (unit)")
    void listarTodos_ShouldReturnAllEstados_WhenNoFiltersProvided() {
        when(estadoService.listarTodos(null)).thenReturn(List.of(GeradorDeEntidades.retornaEstadoDTO1(),
                GeradorDeEntidades.retornaEstadoDTO2()));

        var response = mockMvcTester.get().uri("/estados");

        assertThat(response).hasStatusOk()
                .hasContentType(MediaType.APPLICATION_JSON_VALUE)
                .bodyJson()
                .extractingPath("$")
                .asArray()
                .hasSize(2);

        assertThat(response).bodyJson()
                .extractingPath("$[0].nome").isEqualTo("Rondônia");
    }

    @Test
    @DisplayName("Deve retornar todos os estados quando nenhum filtro filtro for indicado e o path termina com / (unit)")
    void listarTodos_ShouldReturnAllEstados_WhenNoFiltersProvidedAndBar() {
        when(estadoService.listarTodos(null)).thenReturn(List.of(GeradorDeEntidades.retornaEstadoDTO1(),
                GeradorDeEntidades.retornaEstadoDTO2()));

        var response = mockMvcTester.get().uri("/estados/");

        assertThat(response).hasStatusOk()
                .hasContentType(MediaType.APPLICATION_JSON_VALUE)
                .bodyJson()
                .extractingPath("$")
                .asArray()
                .hasSize(2);

        assertThat(response).bodyJson()
                .extractingPath("$[0].nome").isEqualTo("Rondônia");
    }

    @Test
    @DisplayName("Deve retornar todos estados filtrados quando o nome for indicado (unit)")
    void listarTodos_ShouldReturnEstados_WhenValidNameIsProvided() {
        when(estadoService.listarTodos("Rondônia")).thenReturn(List.of(GeradorDeEntidades.retornaEstadoDTO1()));

        var response = mockMvcTester.get().uri("/estados?nome=Rondônia");

        assertThat(response).hasStatusOk()
                .hasContentType(MediaType.APPLICATION_JSON_VALUE)
                .bodyJson()
                .extractingPath("$")
                .asArray()
                .hasSize(1);

        assertThat(response).bodyJson()
                .extractingPath("$[0].nome").isEqualTo("Rondônia");
    }

    @Test
    @DisplayName("Deve retornar todos os estados quando o filtro nome indicado for vazio (unit)")
    void listarTodos_ShouldReturnAllEstados_WhenEmptyNomeProvided() {
        when(estadoService.listarTodos("")).thenReturn(List.of(GeradorDeEntidades.retornaEstadoDTO1(),
                GeradorDeEntidades.retornaEstadoDTO2()));

        var response = mockMvcTester.get().uri("/estados?nome=");

        assertThat(response).hasStatusOk()
                .hasContentType(MediaType.APPLICATION_JSON_VALUE)
                .bodyJson()
                .extractingPath("$")
                .asArray()
                .hasSize(2);

        assertThat(response).bodyJson()
                .extractingPath("$[0].nome").isEqualTo("Rondônia");
    }

    @Test
    @DisplayName("Deve retornar uma lista vazia quando o filtro nome não tiver resultados (unit)")
    void listarTodos_ShouldReturnEmptyList_WhenNameNoResultsFound() {
        when(estadoService.listarTodos(anyString())).thenReturn(Collections.emptyList());

        assertThat(mockMvcTester.get().uri("/estados?nome=qquwerwhfas")).hasStatus(200)
                .hasContentType(MediaType.APPLICATION_JSON_VALUE)
                .bodyJson()
                .extractingPath("$")
                .asArray()
                .isEmpty();
    }

    @Test
    @DisplayName("Deve retonar um estado quando o estado existir (unit)")
    void buscarPorId_ShouldReturnEstado_WhenEstadosExists() {
        when(estadoService.buscarPorId(11L)).thenReturn(GeradorDeEntidades.retornaEstadoDTO1());

        assertThat(mockMvcTester.get().uri("/estados/11")).hasStatusOk()
                .hasContentType(MediaType.APPLICATION_JSON_VALUE)
                .bodyJson()
                .extractingPath("nome")
                .isEqualTo("Rondônia");
    }

    @Test
    @DisplayName("Deve lançar exceção de estado nao encontrado quando o estado não existir (unit)")
    void buscarPorId_ShouldThrowEstadoNaoEncontradoException_WhenEstadoDoesNotExist() {
        when(estadoService.buscarPorId(anyLong())).thenThrow(new EstadoNaoEcontrado());

        assertThat(mockMvcTester.get().uri("/estados/999")).hasStatus(404)
                .hasContentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE)
                .bodyJson()
                .extractingPath("detail")
                .isEqualTo("Erro! Nenhum estado foi encontrado");
    }

    @Test
    @DisplayName("Deve lançar exceção de requisição inválida quando o tipo do argumento for inválido (unit)")
    void buscarPorId_ShouldThrowIllegalArgumentException_WhenInvalidArgumentProvided() {
        assertThat(mockMvcTester.get().uri("/estados/abc")).hasStatus(400)
                .hasContentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE).bodyJson()
                .extractingPath("$.detail").isEqualTo("Requisição inválida!");
    }

    @Test
    @DisplayName("Deve retornar todos os municípios de um estado quando o estado existir (unit)")
    void buscarMunicipiosDoEstado_ShouldReturnListOfMunicipio_WhenEstadosExists() {
        when(estadoService.buscarMunicipiosDoEstado(11L)).thenReturn(List.of(GeradorDeEntidades.retornaMunicipioDTO1()));

        assertThat(mockMvcTester.get().uri("/estados/11/municipios")).hasStatusOk()
                .hasContentType(MediaType.APPLICATION_JSON_VALUE)
                .bodyJson()
                .extractingPath("$")
                .asArray()
                .hasSize(1);
    }

    @Test
    @DisplayName("Deve lancar exceção de estado nao encontrado quando o estado não existir (unit)")
    void buscarMunicipiosDoEstado_ShouldThrowEstadoNaoEncontradoException_WhenEstadoDoesNotExist() {
        when(estadoService.buscarMunicipiosDoEstado(anyLong())).thenThrow(new EstadoNaoEcontrado());

        assertThat(mockMvcTester.get().uri("/estados/56/municipios")).hasStatus(404)
                .hasContentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE)
                .bodyJson()
                .extractingPath("detail")
                .isEqualTo("Erro! Nenhum estado foi encontrado");
    }

    @Test
    @DisplayName("Deve retornar uma lista vazia quando o estado não tiver municípios salvos (unit)")
    void buscarMunicipiosDoEstado_ShouldReturnEmptyList_WhenEstadoDoesNotHaveMunicipios() {
        when(estadoService.buscarMunicipiosDoEstado(anyLong())).thenReturn(Collections.emptyList());

        assertThat(mockMvcTester.get().uri("/estados/56/municipios")).hasStatusOk()
                .hasContentType(MediaType.APPLICATION_JSON_VALUE)
                .bodyJson()
                .extractingPath("$")
                .asArray()
                .isEmpty();
    }

    @Test
    @DisplayName("Deve laçar exceção de erro interno quando erro inesperado ocorrer")
    void deveRetornarErro500_ShouldThrowInternalServerError_WhenUnexpectedError() {
        when(estadoService.listarTodos(any())).thenThrow(new RuntimeException());

        assertThat(mockMvcTester.get().uri("/estados")).hasStatus(500)
                .hasContentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE)
                .bodyJson()
                .extractingPath("$.detail")
                .isEqualTo("Ocorreu um erro inesperado! Tente novamente mais tarde");

    }
}