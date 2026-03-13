package br.com.pedro.etl.repository;

import br.com.pedro.etl.domain.MunicipioEntity;
import br.com.pedro.etl.respository.EstadoRepository;
import br.com.pedro.etl.respository.MunicipioRepository;
import br.com.pedro.etl.util.GeradorDeEntidades;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

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

    @BeforeEach
    public void salvaEstado() {
        estadoRepository.save(GeradorDeEntidades.retornaEstadoEntity());
    }

    @Test
    @DisplayName("Deve retornar um estado quando municipio existe")
    void findById_ShouldReturnEstadoEntity_WhenMunicipioExists(){
        MunicipioEntity municipio = GeradorDeEntidades.retornaMunicipioEntity();
        municipioRepository.save(municipio);

        testEntityManager.flush();
        testEntityManager.clear();

        MunicipioEntity municipioDoBanco = municipioRepository.findById(municipio.getId()).orElseThrow();

        assertThat(municipioDoBanco.getEstadoEntity()).isNotNull();
        assertThat(municipioDoBanco.getEstadoEntity().getSigla()).isEqualTo("RO");
    }
}
