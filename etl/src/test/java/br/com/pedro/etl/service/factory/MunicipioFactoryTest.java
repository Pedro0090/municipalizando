package br.com.pedro.etl.service.factory;

import br.com.pedro.etl.domain.EstadoDTO;
import br.com.pedro.etl.domain.MunicipioDTO;
import br.com.pedro.etl.geoapify.GeoapifyClient;
import br.com.pedro.etl.ibge.entidadesIBGE.MunicipioIbgeDTO;
import br.com.pedro.etl.pdf.LeitorPDF;
import br.com.pedro.etl.util.GeradorDeEntidades;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.BDDMockito.when;

@ExtendWith(MockitoExtension.class)
class MunicipioFactoryTest {

    @Mock
    private GeoapifyClient geoapifyClient;

    @Mock
    private LeitorPDF leitorPDF;

    @InjectMocks
    private MunicipioFactory municipioFactory;

    @Test
    @DisplayName("Deve retornar um MunicipioDTO completo quando todos os dados forem encontrados")
    void montarDTO_ShouldReturnCompleteMunicipioDTO_WhenAllDataIsAvailable() {
        MunicipioIbgeDTO municipioIbgeDTO = GeradorDeEntidades.retornaMunicipioIbgeDTO();
        EstadoDTO estadoDTO = GeradorDeEntidades.retornaEstadoDTO();

        when(geoapifyClient.buscarCoordenadas(municipioIbgeDTO)).thenReturn(GeradorDeEntidades.retornaMunicipioGeoapifyDTO());
        when(leitorPDF.buscarPopulacaoMunicipio(municipioIbgeDTO.getId())).thenReturn(22787);

        MunicipioDTO municipioRetornado = municipioFactory.montarDTO(municipioIbgeDTO, estadoDTO);
        MunicipioDTO municipioEsperado = GeradorDeEntidades.retornaMunicipioDTO();

        Assertions.assertThat(municipioRetornado).isNotNull();
        Assertions.assertThat(municipioRetornado).usingRecursiveComparison().isEqualTo(municipioEsperado);
    }
}