package br.com.pedro.api.controller;

import br.com.pedro.api.respository.EstadoRepository;
import br.com.pedro.api.respository.MunicipioRepository;
import br.com.pedro.api.util.GeradorDeEntidades;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
public class MunicipioControllerIT {

    @Autowired
    private MockMvcTester mockMvcTester;

    @Autowired
    private MunicipioRepository municipioRepository;

    @Autowired
    private EstadoRepository estadoRepository;

    @BeforeEach
    void setUp() {
        estadoRepository.save(GeradorDeEntidades.retornaEstadoEntity1());
        estadoRepository.save(GeradorDeEntidades.retornaEstadoEntity2());
        municipioRepository.save(GeradorDeEntidades.retornaMunicipioEntity1());
        municipioRepository.save(GeradorDeEntidades.retornaMunicipioEntity2());
    }

    @Test
    @DisplayName("Deve retornar todos os municípios quando nenhum filtro filtro for indicado (IT)")
    public void listarTodos_ShouldReturnAllMunicipios_WhenNoFiltersProvided() {
        var response = mockMvcTester.get().uri("/municipios");

        assertThat(response).hasStatusOk().hasContentType(MediaType.APPLICATION_JSON_VALUE).bodyJson().extractingPath("$")
                .asArray().hasSize(2);

        assertThat(response).bodyJson().extractingPath("$[0].nome").isEqualTo("Alta Floresta D'Oeste");
    }

    @Test
    @DisplayName("Deve retornar todos os municípios quando nenhum filtro filtro for indicado e o path termina com / (IT)")
    public void listarTodos_ShouldReturnMunicipios_WhenNoFiltersProvidedAndBar() {
        var response = mockMvcTester.get().uri("/municipios/");

        assertThat(response).hasStatusOk().hasContentType(MediaType.APPLICATION_JSON_VALUE).bodyJson().extractingPath("$")
                .asArray().hasSize(2);

        assertThat(response).bodyJson().extractingPath("$[0].nome").isEqualTo("Alta Floresta D'Oeste");
    }

    @Test
    @DisplayName("Deve retornar todos municípios filtrados quando apenas o nome for indicado (IT)")
    void listarTodos_ShouldReturnMunicipios_WhenValidNameIsProvided() {
        var response = mockMvcTester.get().uri("/municipios?nome=Alta Floresta D Oeste");

        assertThat(response).hasStatusOk().hasContentType(MediaType.APPLICATION_JSON_VALUE).bodyJson().extractingPath("$")
                .asArray().hasSize(1);

        assertThat(response).bodyJson().extractingPath("$[0].nome").isEqualTo("Alta Floresta D'Oeste");
    }

    @Test
    @DisplayName("Deve retornar todos municípios filtrados quando apenas o nome e a sigla do estado forem indicados (IT)")
    void listarTodos_ShouldReturnMunicipios_WhenNameAndEstadoSiglaAreProvided() {
        var response = mockMvcTester.get().uri("/municipios?nome=Alta Floresta D Oeste&estado=ro");

        assertThat(response).hasStatusOk().hasContentType(MediaType.APPLICATION_JSON_VALUE).bodyJson().extractingPath("$")
                .asArray().hasSize(1);

        assertThat(response).bodyJson().extractingPath("$[0].nome").isEqualTo("Alta Floresta D'Oeste");
    }

    @Test
    @DisplayName("Deve retornar todos os municípios quando o filtro nome indicado for vazio (IT)")
    void listarTodos_ShouldReturnAllMunicipios_WhenEmptyNomeProvided() {
        var response = mockMvcTester.get().uri("/municipios?nome= ");

        assertThat(response).hasStatusOk().hasContentType(MediaType.APPLICATION_JSON_VALUE).bodyJson().extractingPath("$")
                .asArray().hasSize(2);

        assertThat(response).bodyJson().extractingPath("$[0].nome").isEqualTo("Alta Floresta D'Oeste");
    }

    @Test
    @DisplayName("Deve retornar uma lista vazia quando o filtro nome não tiver resultados (IT)")
    void listarTodos_ShouldReturnEmptyList_WhenNameNoResultsFound() {

        assertThat(mockMvcTester.get().uri("/municipios?nome=suufasueife")).hasStatus(200)
                .hasContentType(MediaType.APPLICATION_JSON_VALUE).bodyJson().extractingPath("$")
                .asArray().isEmpty();
    }

    @Test
    @DisplayName("Deve retornar uma lista vazia quando apenas o filtro estado não tiver resultados (IT)")
    void listarTodos_ReturnEmptyList_WhenOnlyEstadoNoResultsFound() {

        assertThat(mockMvcTester.get().uri("/municipios?nome=alta floresta d oeste &estado=ei"))
                .hasStatus(200).hasContentType(MediaType.APPLICATION_JSON_VALUE).bodyJson().extractingPath("$")
                .asArray().isEmpty();
    }

    @Test
    @DisplayName("Deve lançar exceção de má requisição quando o apenas o filtro estado foi indicado (IT)")
    void listarTodos_ShouldThrowIllegalArgumentException_WhenOnlyEstadoProvided() {

        assertThat(mockMvcTester.get().uri("/municipios?estado=rondonia")).hasStatus(400)
                .hasContentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE).bodyJson().extractingPath("$.detail")
                .isEqualTo("Erro! Parâmetro 'estado' só pode ser usado junto com nome");
    }

    @Test
    @DisplayName("Deve retornar um municipio quando o municipio existir (IT)")
    void buscarPorId_ReturnMunicipio_WhenMunicipioExists() {

        assertThat(mockMvcTester.get().uri("/municipios/1100015")).hasStatusOk()
                .hasContentType(MediaType.APPLICATION_JSON_VALUE).bodyJson().extractingPath("$.nome")
                .isEqualTo("Alta Floresta D'Oeste");
    }

    @Test
    @DisplayName("Deve lançar exceção de municipio não encontrado quando o municipio não existir (IT)")
    void buscarPorId_ShouldThrowMunicipioNaoEncontrado_WhenMunicipioDoesNotExist() {

        assertThat(mockMvcTester.get().uri("/municipios/0")).hasStatus(404)
                .hasContentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE).bodyJson().extractingPath("$.detail")
                .isEqualTo("Erro! Nenhum município foi encontrado");
    }

    @Test
    @DisplayName("Deve lançar exceção de requisição inválida quando o tipo do argumento for inválido (IT)")
    void buscarPorId_ShouldThrowBadRequestException_WhenInvalidArgumentProvided() {

        assertThat(mockMvcTester.get().uri("/municipios/abc")).hasStatus(400)
                .hasContentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE).bodyJson().extractingPath("$.detail")
                .isEqualTo("Requisição inválida!");
    }

    @Test
    @DisplayName("Deve retornar o estado de um município quando o município existir (IT)")
    void buscarEstadoDoMunicipio_ShouldReturnEstado_WhenMunicipioExists() {
        assertThat(mockMvcTester.get().uri("/municipios/1100015/estado")).hasStatusOk()
                .hasContentType(MediaType.APPLICATION_JSON_VALUE).bodyJson().extractingPath("$.nome")
                .isEqualTo("Rondônia");
    }

    @Test
    @DisplayName("Deve lancar exceção de município nao encontrado quando o município não existir (IT)")
    void buscarEstadoDoMunicipio_ShouldThrowMunicipioNaoEncontradoException_WhenMunicipioDoesNotExist() {
        assertThat(mockMvcTester.get().uri("/municipios/0/estado")).hasStatus(404)
                .hasContentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE).bodyJson().extractingPath("$.detail")
                .isEqualTo("Erro! Nenhum município foi encontrado");
    }
}
