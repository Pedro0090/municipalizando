package br.com.pedro.etl.service;

import br.com.pedro.etl.ibge.IbgeClient;
import br.com.pedro.etl.ibge.entidadesIBGE.EstadoIbgeDTO;
import br.com.pedro.etl.ibge.entidadesIBGE.MunicipioIbgeDTO;
import br.com.pedro.etl.infra.ratelimit.ControleDeRequisicoesGeoapify;
import br.com.pedro.etl.infra.ratelimit.Intervalo;
import br.com.pedro.etl.mapper.EstadoMapper;
import br.com.pedro.etl.mapper.MunicipioMapper;
import br.com.pedro.etl.respository.EstadoRepository;
import br.com.pedro.etl.respository.MunicipioRepository;
import br.com.pedro.etl.service.factory.EstadoFactory;
import br.com.pedro.etl.service.factory.MunicipioFactory;
import br.com.pedro.etl.service.helper.Limitador;
import br.com.pedro.etl.util.GeradorDeEntidades;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class EtlServiceTest {

    @Mock
    private IbgeClient ibgeClient;

    @Mock
    private MunicipioMapper municipioMapper;

    @Mock
    private EstadoMapper estadoMapper;

    @Mock
    private EstadoRepository estadoRepository;

    @Mock
    private MunicipioRepository municipioRepository;

    @Mock
    private ControleDeRequisicoesGeoapify controleDeRequisicoesGeoapify;

    @Mock
    private MunicipioFactory municipioFactory;

    @Mock
    private EstadoFactory estadoFactory;

    @Mock
    private Limitador limitador;

    @InjectMocks
    private EtlService etlService;

    @BeforeEach
    void setUp() {
        List<EstadoIbgeDTO> estadosIbge = List.of(GeradorDeEntidades.retornaEstadoIbgeDTO());
        List<MunicipioIbgeDTO> municipiosIbge = List.of(GeradorDeEntidades.retornaMunicipioIbgeDTO());

        when(estadoFactory.montarDTO(any())).thenReturn(GeradorDeEntidades.retornaEstadoDTO());

        when(ibgeClient.listarMunicipios()).thenReturn(municipiosIbge);
        when(ibgeClient.listarEstados()).thenReturn(estadosIbge);
        when(controleDeRequisicoesGeoapify.calculaLimiteDeRequisicoesDiario(anyLong(), anyInt()))
                .thenReturn(new Intervalo(0, 1));
        when(municipioFactory.montarDTO(any(), any())).thenReturn(GeradorDeEntidades.retornaMunicipioDTO());
        when(municipioMapper.toEntity(any())).thenReturn(GeradorDeEntidades.retornaMunicipioEntity());

        doNothing().when(limitador).aguardar();
    }

    @Test
    @DisplayName("Deve salvar estados e municípios quando o banco de dados estiver vazio")
    void executar_ShouldSaveEstadosAndMunicipios_WhenDatabaseIsEmpty() {
        when(estadoMapper.toEntity(any())).thenReturn(GeradorDeEntidades.retornaEstadoEntity());
        when(estadoRepository.count()).thenReturn(0L);

        etlService.executar();

        verify(estadoRepository, atLeastOnce()).saveAll(any());
        verify(municipioRepository, atLeastOnce()).saveAll(any());
    }

    @Test
    @DisplayName("Não deve salvar estados quando os estados já existem")
    void executar_ShouldNotSaveEstadoss_WhenEstadosAlreadyExist() {
        when(estadoRepository.count()).thenReturn(1L);

        etlService.executar();

        verify(estadoRepository, never()).saveAll(any());
        verify(municipioRepository, times(1)).saveAll(any());
    }

}