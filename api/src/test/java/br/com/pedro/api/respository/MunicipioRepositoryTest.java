package br.com.pedro.api.respository;

import br.com.pedro.api.domain.EstadoEntity;
import br.com.pedro.api.domain.MunicipioEntity;
import br.com.pedro.api.util.GeradorDeEntidades;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class MunicipioRepositoryTest {

    @Autowired
    private MunicipioRepository municipioRepository;

    @Autowired
    private EstadoRepository estadoRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    EstadoEntity estadoSalvo;
    MunicipioEntity municipioSalvo;

    @BeforeEach
    void setUp() {
        estadoSalvo = estadoRepository.save(GeradorDeEntidades.retornaEstadoEntity1());

        municipioSalvo = GeradorDeEntidades.retornaMunicipioEntity1();
        municipioSalvo.setEstado(estadoSalvo);
        municipioSalvo = municipioRepository.save(municipioSalvo);

        testEntityManager.flush();
        testEntityManager.clear();
    }

    @Test
    @DisplayName("Deve retornar lista de MunicipioEntity quando o município existir")
    void findByNomeNormalizado_ShouldReturnListOfMunicipioEntity_WhenMunicipioExists() {
        List<MunicipioEntity> municipios = municipioRepository.findByNomeNormalizado("alta floresta d oeste");

        assertThat(municipios).hasSize(1);
        assertThat(municipios.getFirst().getNome()).isEqualTo("Alta Floresta D'Oeste");
    }

    @Test
    @DisplayName("Deve retornar lista de MunicipioEntity quando o município e a sigla do estado existirem")
    void findByNomeNormalizadoAndEstadoSigla_ShouldReturnMunicipioEntity_WhenNameAndEstadoSiglaExist() {
        List<MunicipioEntity> municipios = municipioRepository.
                findByNomeNormalizadoAndEstadoSigla("alta floresta d oeste", "RO");

        assertThat(municipios).hasSize(1);
        assertThat(municipios.getFirst().getNome()).isEqualTo("Alta Floresta D'Oeste");
    }

    @Test
    @DisplayName("Deve retornar lista de MunicipioEntity quando o município e o nome do estado existirem")
    void findByNomeNormalizadoAndEstadoNomeNormalizado_ShouldReturnMunicipioEntity_WhenMunicipioAndEstadoNameExist() {
        List<MunicipioEntity> municipios = municipioRepository.
                findByNomeNormalizadoAndEstadoNomeNormalizado
                        ("alta floresta d oeste", "rondonia");

        assertThat(municipios).hasSize(1);
        assertThat(municipios.getFirst().getNome()).isEqualTo("Alta Floresta D'Oeste");
    }

    @Test
    @DisplayName("Deve retornar MunicipioEntity quando o município existir")
    void findByEstadoId_ShouldReturnMunicipioEntity_WhenMunicipioExists() {
        List<MunicipioEntity> municipios =  municipioRepository.findByEstadoId(estadoSalvo.getId());

        assertThat(municipios).hasSize(1);
        assertThat(municipios.getFirst().getNome()).isEqualTo("Alta Floresta D'Oeste");
    }

    @Test
    @DisplayName("Deve retornar EstadoEntity quando o município existir")
    void deveBuscarMunicipioComEstado_ShouldReturnEstadoEntity_WhenMunicipioExists() {
        MunicipioEntity municipio = municipioRepository.findByNomeNormalizado("alta floresta d oeste").getFirst();
        EstadoEntity estado = municipio.getEstado();

        assertThat(estado).isNotNull();
        assertThat(estado.getNome()).isEqualTo("Rondônia");
    }
}