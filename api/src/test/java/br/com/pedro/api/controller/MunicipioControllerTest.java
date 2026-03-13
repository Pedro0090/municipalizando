package br.com.pedro.api.controller;

import br.com.pedro.api.exception.MunicipioNaoEncontrado;
import br.com.pedro.api.service.MunicipioService;
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
import static org.mockito.BDDMockito.*;

@WebMvcTest(MunicipioController.class)
class MunicipioControllerTest {

    @Autowired
    private MockMvcTester mockMvcTester;

    @MockitoBean
    private MunicipioService municipioService;

    @Test
    @DisplayName("Deve retornar todos os municípios quando nenhum filtro filtro for indicado (unit)")
    public void listarTodos_ShouldReturnAllMunicipios_WhenNoFiltersProvided() {
        when(municipioService.listarTodos(null, null)).thenReturn(List.of(
                GeradorDeEntidades.retornaMunicipioDTO1(), GeradorDeEntidades.retornaMunicipioDTO2()));

        var response = mockMvcTester.get().uri("/municipios");

        assertThat(response).hasStatusOk()
                .hasContentType(MediaType.APPLICATION_JSON_VALUE)
                .bodyJson()
                .extractingPath("$").asArray().hasSize(2);

        assertThat(response).bodyJson()
                .extractingPath("$[0].nome").isEqualTo("Alta Floresta D'Oeste");
    }

    @Test
    @DisplayName("Deve retornar todos os municípios quando nenhum filtro filtro foi indicado e o path termina com / (unit)")
    public void listarTodos_ShouldReturnMunicipios_WhenNoFiltersProvidedAndBar() {
        when(municipioService.listarTodos(null, null)).thenReturn(List.of(
                GeradorDeEntidades.retornaMunicipioDTO1(), GeradorDeEntidades.retornaMunicipioDTO2()));

        var response = mockMvcTester.get().uri("/municipios/");

        assertThat(response).hasStatusOk()
                .hasContentType(MediaType.APPLICATION_JSON_VALUE)
                .bodyJson()
                .extractingPath("$").asArray().hasSize(2);

        assertThat(response).bodyJson()
                .extractingPath("$[0].nome").isEqualTo("Alta Floresta D'Oeste");
    }

    @Test
    @DisplayName("Deve retornar todos municípios filtrados quando apenas o nome for indicado (unit)")
    void listarTodos_ShouldReturnMunicipios_WhenValidNameIsProvided() {
        when(municipioService.listarTodos("Alta Floresta D'Oeste", null))
                .thenReturn(List.of(GeradorDeEntidades.retornaMunicipioDTO1()));

        var response = mockMvcTester.get().uri("/municipios?nome=Alta Floresta D'Oeste");

        assertThat(response)
                .hasStatusOk()
                .hasContentType(MediaType.APPLICATION_JSON_VALUE)
                .bodyJson()
                .extractingPath("$")
                .asArray()
                .hasSize(1);

        assertThat(response).bodyJson()
                .extractingPath("$[0].nome").isEqualTo("Alta Floresta D'Oeste");
    }

    @Test
    @DisplayName("Deve retornar todos municípios filtrados quando apenas o nome e a sigla do estado forem indicados (unit)")
    void listarTodos_ShouldReturnMunicipios_WhenNameAndEstadoSiglaAreProvided() {
        when(municipioService.listarTodos("Alta Floresta D'Oeste", "RO"))
                .thenReturn(List.of(GeradorDeEntidades.retornaMunicipioDTO1()));

        var response = mockMvcTester.get().uri("/municipios?nome=Alta Floresta D'Oeste&estado=RO");

        assertThat(response)
                .hasStatusOk()
                .hasContentType(MediaType.APPLICATION_JSON_VALUE)
                .bodyJson()
                .extractingPath("$")
                .asArray()
                .hasSize(1);

        assertThat(response)
                .bodyJson()
                .extractingPath("$[0].nome").isEqualTo("Alta Floresta D'Oeste");
    }

    @Test
    @DisplayName("Deve retornar todos os municípios quando o filtro nome indicado for vazio (unit)")
    void listarTodos_ShouldReturnAllMunicipios_WhenEmptyNomeProvided() {
        when(municipioService.listarTodos("", null)).thenReturn(List.of(GeradorDeEntidades.retornaMunicipioDTO1(),
                GeradorDeEntidades.retornaMunicipioDTO2()));

        var response = mockMvcTester.get().uri("/municipios?nome=");

        assertThat(response).hasStatusOk()
                .hasContentType(MediaType.APPLICATION_JSON_VALUE)
                .bodyJson()
                .extractingPath("$")
                .asArray()
                .hasSize(2);

        assertThat(response).bodyJson()
                .extractingPath("$[0].nome").isEqualTo("Alta Floresta D'Oeste");
    }

    @Test
    @DisplayName("Deve retornar uma lista vazia quando o filtro nome não tiver resultados (unit)")
    void listarTodos_ShouldReturnEmptyList_WhenNameNoResultsFound() {
        when(municipioService.listarTodos(anyString(), any())).thenReturn(Collections.emptyList());

        assertThat(mockMvcTester.get().uri("/municipios?nome=qquwerwhfas")).hasStatus(200)
                .hasContentType(MediaType.APPLICATION_JSON_VALUE)
                .bodyJson()
                .extractingPath("$")
                .asArray()
                .isEmpty();
    }

    @Test
    @DisplayName("Deve retornar uma lista vazia quando apenas o filtro estado não tiver resultados (unit)")
    void listarTodos_ShouldReturnEmptyList_WhenOnlyEstadoNoResultsFound() {
        when(municipioService.listarTodos(anyString(), anyString())).thenReturn(Collections.emptyList());

        assertThat(mockMvcTester.get().uri("/municipios?nome=Alta Floresta D'Oeste&estado=ei")).hasStatus(200)
                .hasContentType(MediaType.APPLICATION_JSON_VALUE)
                .bodyJson()
                .extractingPath("$")
                .asArray()
                .isEmpty();
    }

    @Test
    @DisplayName("Deve lançar exceção de má requisição quando o apenas o filtro estado foi indicado (unit)")
    void listarTodos_ShouldThrowIllegalArgumentException_WhenOnlyEstadoProvided() {
        when(municipioService.listarTodos(any(), anyString())).thenThrow(
                new IllegalArgumentException("Erro! Parâmetro 'estado' só pode ser usado junto com nome"));

        assertThat(mockMvcTester.get().uri("/municipios?estado=ro")).hasStatus(400)
                .hasContentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE)
                .bodyJson()
                .extractingPath("detail")
                .isEqualTo("Erro! Parâmetro 'estado' só pode ser usado junto com nome");
    }

    @Test
    @DisplayName("Deve retornar um municipio quando o municipio existir (unit)")
    void buscarPorId_ReturnMunicipio_WhenMunicipioExists() {
        when(municipioService.buscarPorId(11L)).thenReturn(GeradorDeEntidades.retornaMunicipioDTO1());

        assertThat(mockMvcTester.get().uri("/municipios/11")).hasStatusOk()
                .hasContentType(MediaType.APPLICATION_JSON_VALUE)
                .bodyJson()
                .extractingPath("nome")
                .isEqualTo("Alta Floresta D'Oeste");
    }

    @Test
    @DisplayName("Deve lançar exceção de municipio não encontrado quando o municipio não existir (unit)")
    void buscarPorId_ShouldThrowMunicipioNaoEncontrado_WhenMunicipioDoesNotExist() {
        when(municipioService.buscarPorId(anyLong())).thenThrow(new MunicipioNaoEncontrado());

        assertThat(mockMvcTester.get().uri("/municipios/999")).hasStatus(404)
                .hasContentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE)
                .bodyJson()
                .extractingPath("detail")
                .isEqualTo("Erro! Nenhum município foi encontrado");
    }

    @Test
    @DisplayName("Deve lançar exceção de requisição inválida quando o tipo do argumento for inválido (unit)")
    void buscarPorId_ShouldThrowBadRequestException_WhenInvalidArgumentProvided() {
        assertThat(mockMvcTester.get().uri("/municipios/abc")).hasStatus(400)
                .hasContentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE).bodyJson()
                .extractingPath("$.detail").isEqualTo("Requisição inválida!");
    }

    @Test
    @DisplayName("Deve retornar o estado de um município quando o município existir (unit)")
    void buscarEstadoDoMunicipio_ShouldReturnEstado_WhenMunicipioExists() {
        when(municipioService.buscarEstadoDoMunicipio(110015L)).thenReturn
                (GeradorDeEntidades.retornaEstadoDTO1());

        assertThat(mockMvcTester.get().uri("/municipios/110015/estado")).hasStatusOk()
                .hasContentType(MediaType.APPLICATION_JSON_VALUE)
                .bodyJson()
                .extractingPath("$.nome")
                .isEqualTo("Rondônia");
    }

    @Test
    @DisplayName("Deve lancar exceção de município nao encontrado quando o município não existir (unit)")
    void buscarEstadoDoMunicipio_ShouldThrowMunicipioNaoEncontradoException_WhenMunicipioDoesNotExist() {
        when(municipioService.buscarEstadoDoMunicipio(anyLong())).thenThrow(new MunicipioNaoEncontrado());

        assertThat(mockMvcTester.get().uri("/municipios/56/estado")).hasStatus(404)
                .hasContentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE)
                .bodyJson()
                .extractingPath("detail")
                .isEqualTo("Erro! Nenhum município foi encontrado");
    }

    @Test
    @DisplayName("Deve laçar exceção de erro interno quando erro inesperado ocorrer")
    void deveRetornarErro500_ShouldThrowInternalServerError_WhenUnexpectedError() {
        when(municipioService.listarTodos(any(), any())).thenThrow(new RuntimeException());

        assertThat(mockMvcTester.get().uri("/municipios")).hasStatus(500)
                .hasContentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE)
                .bodyJson()
                .extractingPath("$.detail")
                .isEqualTo("Ocorreu um erro inesperado! Tente novamente mais tarde");

    }
}