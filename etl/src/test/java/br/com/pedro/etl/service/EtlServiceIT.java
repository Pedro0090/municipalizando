package br.com.pedro.etl.service;

import br.com.pedro.etl.geoapify.GeoapifyClient;
import br.com.pedro.etl.ibge.IbgeClient;
import br.com.pedro.etl.infra.ratelimit.ControleDeRequisicoesGeoapify;
import br.com.pedro.etl.mapper.EstadoMapper;
import br.com.pedro.etl.mapper.MunicipioMapper;
import br.com.pedro.etl.pdf.LeitorPDF;
import br.com.pedro.etl.respository.EstadoRepository;
import br.com.pedro.etl.respository.MunicipioRepository;
import br.com.pedro.etl.service.factory.EstadoFactory;
import br.com.pedro.etl.service.factory.MunicipioFactory;
import br.com.pedro.etl.service.helper.Limitador;
import br.com.pedro.etl.util.GeradorDeEntidades;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;

import static org.mockito.BDDMockito.*;

@SpringBootTest
@ActiveProfiles("test")
public class EtlServiceIT {

    @Autowired
    private EtlService etlService;

    @Autowired
    private MunicipioRepository municipioRepository;

    @Autowired
    private EstadoRepository estadoRepository;

    @Autowired
    private MunicipioMapper municipioMapper;

    @Autowired
    private EstadoMapper estadoMapper;

    @Autowired
    private MunicipioFactory municipioFactory;

    @Autowired
    private EstadoFactory estadoFactory;

    @Autowired
    private ControleDeRequisicoesGeoapify controleDeRequisicoesGeoapify;

    @MockitoBean
    private LeitorPDF leitorPDF;

    @MockitoBean
    private GeoapifyClient geoapifyClient;

    @MockitoBean
    private Limitador limitador;

    @MockitoBean
    private IbgeClient ibgeClient;

    @BeforeEach
    public void setUp(){
        when(ibgeClient.listarEstados()).thenReturn(List.of(GeradorDeEntidades.retornaEstadoIbgeDTO()));
        when(ibgeClient.listarMunicipios()).thenReturn(List.of(GeradorDeEntidades.retornaMunicipioIbgeDTO()));

        when(leitorPDF.buscarPopulacaoMunicipio(anyLong())).thenReturn(22787);
        when(geoapifyClient.buscarCoordenadas(any())).thenReturn(GeradorDeEntidades.retornaMunicipioGeoapifyDTO());
    }

    @Test
    @DisplayName("Deve salvar estado e municípios quando chamado")
    public void executar_ShouldSaveEstadosAndMunicipios_WhenCalled() {
        etlService.executar();

        Assertions.assertThat(estadoRepository.count()).isGreaterThan(0);
        Assertions.assertThat(municipioRepository.count()).isGreaterThan(0);
    }
}
