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
public class EstadoControllerIT {

    @Autowired
    private MockMvcTester mockMvcTester;

    @Autowired
    private EstadoRepository estadoRepository;

    @Autowired
    private MunicipioRepository municipioRepository;

    @BeforeEach
    void setup() {
        estadoRepository.save(GeradorDeEntidades.retornaEstadoEntity1());
        estadoRepository.save(GeradorDeEntidades.retornaEstadoEntity2());
        municipioRepository.save(GeradorDeEntidades.retornaMunicipioEntity1());
    }

    @Test
    @DisplayName("Deve retornar todos os estados quando nenhum filtro filtro for indicado (IT)")
    void listarTodos_ShouldReturnAllEstados_WhenNoFiltersProvided() {
        var response = mockMvcTester.get().uri("/estados");

        assertThat(response).hasStatusOk().hasContentType(MediaType.APPLICATION_JSON_VALUE)
                .bodyJson().extractingPath("$").asArray().hasSize(2);

        assertThat(response).bodyJson().extractingPath("$[0].nome").isEqualTo("Rondônia");
    }

    @Test
    @DisplayName("Deve retornar todos os estados quando nenhum filtro filtro for indicado e o path termina com / (IT)")
    void listarTodos_ShouldReturnAllEstados_WhenNoFiltersProvidedAndBar() {
        var response = mockMvcTester.get().uri("/estados/");

        assertThat(response).hasStatusOk().hasContentType(MediaType.APPLICATION_JSON_VALUE)
                .bodyJson().extractingPath("$").asArray().hasSize(2);

        assertThat(response).bodyJson().extractingPath("$[0].nome").isEqualTo("Rondônia");
    }

    @Test
    @DisplayName("Deve retornar todos estados filtrados quando o nome for indicado (IT)")
    void listarTodos_ShouldReturnEstados_WhenValidNameIsProvided() {
        var response = mockMvcTester.get().uri("/estados?nome= Rondonia");

        assertThat(response).hasStatusOk().hasContentType(MediaType.APPLICATION_JSON_VALUE)
                .bodyJson().extractingPath("$").asArray().hasSize(1);

        assertThat(response).bodyJson().extractingPath("$[0].nome").isEqualTo("Rondônia");
    }

    @Test
    @DisplayName("Deve retornar todos os estados quando o filtro nome indicado for vazio (IT)")
    void listarTodos_ShouldReturnAllEstados_WhenEmptyNomeProvided() {
        var response = mockMvcTester.get().uri("/estados?nome= ");

        assertThat(response).hasStatusOk().hasContentType(MediaType.APPLICATION_JSON_VALUE)
                .bodyJson().extractingPath("$").asArray().hasSize(2);

        assertThat(response).bodyJson().extractingPath("$[0].nome").isEqualTo("Rondônia");
    }

    @Test
    @DisplayName("Deve retornar uma lista vazia quando o filtro nome não tiver resultados (IT)")
    void listarTodos_ShouldReturnEmptyList_WhenNameNoResultsFound() {
        assertThat(mockMvcTester.get().uri("/estados?nome=ahusdd")).hasStatus(200)
                .hasContentType(MediaType.APPLICATION_JSON_VALUE)
                .bodyJson().extractingPath("$").asArray().isEmpty();
    }

    @Test
    @DisplayName("Deve retonar um estado quando o estado existir (IT)")
    void buscarPorId_ShouldReturnEstado_WhenEstadosExists() {
        assertThat(mockMvcTester.get().uri("/estados/11")).hasStatusOk()
                .hasContentType(MediaType.APPLICATION_JSON_VALUE)
                .bodyJson()
                .extractingPath("$.nome").isEqualTo("Rondônia");
    }

    @Test
    @DisplayName("Deve lançar exceção de estado nao encontrado quando o estado não existir (IT)")
    void buscarPorId_ShouldThrowEstadoNaoEncontradoException_WhenEstadoDoesNotExist() {
        assertThat(mockMvcTester.get().uri("/estados/99")).hasStatus(404)
                .hasContentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE)
                .bodyJson()
                .extractingPath("$.detail").isEqualTo("Erro! Nenhum estado foi encontrado");
    }

    @Test
    @DisplayName("Deve lançar exceção de requisição inválida quando o tipo do argumento for inválido (IT)")
    void buscarPorId_ShouldThrowBadRequestException_WhenInvalidArgumentProvided() {
        assertThat(mockMvcTester.get().uri("/estados/abc")).hasStatus(400)
                .hasContentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE)
                .bodyJson()
                .extractingPath("$.detail").isEqualTo("Requisição inválida!");
    }

    @Test
    @DisplayName("Deve retornar todos os municípios de um estado quando o estado existir (IT)")
    void buscarMunicipiosDoEstado_ShouldReturnListOfMunicipio_WhenEstadosExists() {
        assertThat(mockMvcTester.get().uri("/estados/11/municipios")).hasStatusOk()
                .hasContentType(MediaType.APPLICATION_JSON_VALUE)
                .bodyJson()
                .extractingPath("$")
                .asArray().hasSize(1);
    }

    @Test
    @DisplayName("Deve retornar uma lista vazia quando o estado não tiver municípios salvos")
    void buscarMunicipiosDoEstado_ShouldReturnEmptyList_WhenEstadoDoesNotHaveMunicipios() {
        assertThat(mockMvcTester.get().uri("/estados/35/municipios")).hasStatusOk()
                .hasContentType(MediaType.APPLICATION_JSON_VALUE)
                .bodyJson()
                .extractingPath("$")
                .asArray().isEmpty();
    }
}
