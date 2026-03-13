package br.com.pedro.etl.service.factory;

import br.com.pedro.etl.domain.EstadoDTO;
import br.com.pedro.etl.ibge.entidadesIBGE.EstadoIbgeDTO;
import br.com.pedro.etl.util.GeradorDeEntidades;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class EstadoFactoryTest {

    @InjectMocks
    private EstadoFactory estadoFactory;

    @Test
    @DisplayName("Deve retornar um EstadoDTO completo quando todos os dados forem encontrados")
    void montarDTO_ShouldReturnCompleteEstadoDTO_WhenAllDataIsAvailable() {

        EstadoIbgeDTO estadoIbgeDTO = GeradorDeEntidades.retornaEstadoIbgeDTO();

        EstadoDTO estadoRetornado = estadoFactory.montarDTO(estadoIbgeDTO);
        EstadoDTO estadoEsperado = GeradorDeEntidades.retornaEstadoDTO();

        assertThat(estadoEsperado).usingRecursiveComparison().isEqualTo(estadoRetornado);
    }
}

