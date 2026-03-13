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
class EstadoRepositoryTest {

    @Autowired
    private EstadoRepository estadoRepository;

    @Autowired
    private MunicipioRepository municipioRepository;

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
    @DisplayName("Deve retornar EstadoEntity quando o estado existir")
    void findByNomeNormalizado_ShouldReturnEstadoEntity_WhenEstadoExists() {
        List<EstadoEntity> estados = estadoRepository.findByNomeNormalizado("rondonia");

        assertThat(estados).hasSize(1);
        assertThat(estados.getFirst().getNome()).isEqualTo("Rondônia");
    }

    @Test
    @DisplayName("Deve retornar lista de MunicipioEntity quando o estado existir e possuir municípios")
    void deveBuscarMunicipioPorEstado_ShouldReturnListOfMunicipioEntity_WhenEstadoExistsAndHasMunicipios() {
        List<EstadoEntity> estados = estadoRepository.findByNomeNormalizado("rondonia");
        List<MunicipioEntity> municipios = estados.getFirst().getMunicipios();

        assertThat(municipios).hasSize(1);
        assertThat(municipios.getFirst().getNomeNormalizado()).isEqualTo("alta floresta d oeste");
    }
}