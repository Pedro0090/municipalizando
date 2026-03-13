package br.com.pedro.api.service;

import br.com.pedro.api.domain.DTO.EstadoDTO;
import br.com.pedro.api.domain.DTO.MunicipioDTO;
import br.com.pedro.api.exception.EstadoNaoEcontrado;
import br.com.pedro.api.mapper.EstadoMapper;
import br.com.pedro.api.mapper.MunicipioMapper;
import br.com.pedro.api.respository.EstadoRepository;
import br.com.pedro.api.respository.MunicipioRepository;
import br.com.pedro.api.util.GeradorDeEntidades;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class EstadoServiceTest {

    @Mock
    private EstadoRepository estadoRepository;

    @Mock
    private EstadoMapper estadoMapper;

    @Mock
    private MunicipioRepository municipioRepository;

    @Mock
    private MunicipioMapper municipioMapper;

    @InjectMocks
    private EstadoService estadoService;

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {" ", ""})
    @DisplayName("Deve retornar todos os estados quando nenhum filtro foi indicado")
    void listarTodos_ShouldReturnAllEstados_WhenNoFiltersProvided(String nome) {
        when(estadoRepository.findAll()).thenReturn(List.of(GeradorDeEntidades.retornaEstadoEntity1(),
                GeradorDeEntidades.retornaEstadoEntity2()));
        when(estadoMapper.paraListaDTO(anyList())).thenReturn(List.of(GeradorDeEntidades.retornaEstadoDTO1(),
                GeradorDeEntidades.retornaEstadoDTO2()));

        List<EstadoDTO> estadosRetornados = estadoService.listarTodos(nome);
        List<EstadoDTO> estadosEsperados = List.of(GeradorDeEntidades.retornaEstadoDTO1(),
                GeradorDeEntidades.retornaEstadoDTO2());

        assertThat(estadosRetornados).hasSize(2);
        assertThat(estadosRetornados).usingRecursiveComparison().isEqualTo(estadosEsperados);

        verify(estadoRepository).findAll();
        verify(estadoRepository, never()).findByNomeNormalizado(anyString());
    }

    @Test
    @DisplayName("Deve retornar todos estados filtrados quando o nome for indicado")
    void listarTodos_ReturnFilteredEstados_WhenValidNameIsProvided() {
        when(estadoRepository.findByNomeNormalizado("rondonia")).thenReturn(List.of(GeradorDeEntidades.retornaEstadoEntity1()));

        when(estadoMapper.paraListaDTO(anyList())).thenReturn(List.of(GeradorDeEntidades.retornaEstadoDTO1()));

        List<EstadoDTO> estadoRetornado = estadoService.listarTodos("Rondônia");
        List<EstadoDTO> estadoEsperado = List.of(GeradorDeEntidades.retornaEstadoDTO1());

        assertThat(estadoRetornado).hasSize(1);
        assertThat(estadoRetornado).usingRecursiveComparison().isEqualTo(estadoEsperado);

        verify(estadoRepository).findByNomeNormalizado("rondonia");
        verify(estadoRepository, never()).findAll();
    }

    @Test
    @DisplayName("Deve retornar uma lista vazia quando o filtro nome não tiver resultados")
    void listarTodos_ShouldReturnEmptyList_WhenNameNoResultsFound() {
        when(estadoRepository.findByNomeNormalizado(anyString())).thenReturn(Collections.emptyList());

        assertThat(estadoService.listarTodos("eashudidshsui")).isEmpty();

        verify(estadoRepository).findByNomeNormalizado(anyString());
        verify(estadoRepository, never()).findAll();
    }

    @Test
    @DisplayName("Deve retonar um estado quando o estado existir")
    void buscarPorId_ShouldReturnEstado_WhenEstadoExists() {
        when(estadoRepository.findById(anyLong())).thenReturn(Optional.of(GeradorDeEntidades.retornaEstadoEntity1()));
        when(estadoMapper.paraDTO(any())).thenReturn(GeradorDeEntidades.retornaEstadoDTO1());

        EstadoDTO estadoRetornado = estadoService.buscarPorId(1L);
        EstadoDTO estadoEsperado = GeradorDeEntidades.retornaEstadoDTO1();

        assertThat(estadoRetornado).usingRecursiveComparison().isEqualTo(estadoEsperado);

        verify(estadoRepository).findById(1L);
        verify(estadoMapper).paraDTO(any());
    }

    @Test
    @DisplayName("Deve lançar exção de estado nao encontrado quando o estado não existir")
    void buscarPorId_ShouldThrowEstadoNaoEncontradoException_WhenEstadoDoesNotExist() {
        when(estadoRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> estadoService.buscarPorId(1L)).isInstanceOf(EstadoNaoEcontrado.class)
                        .hasMessage("Erro! Nenhum estado foi encontrado");
    }

    @Test
    @DisplayName("Deve retornar todos os municípios de um estado quando o estado existir")
    void buscarMunicipiosDoEstado_ShoudReturnAllMunicipiosOfEstado_WhenEstadoExists() {
        when(estadoRepository.findById(anyLong())).thenReturn(Optional.of(GeradorDeEntidades.retornaEstadoEntityComMunicipio1()));

        when(municipioMapper.paraListaDTO(anyList())).thenReturn(List.of(GeradorDeEntidades.retornaMunicipioDTO1()));

        List<MunicipioDTO> municipiosRetornados = estadoService.buscarMunicipiosDoEstado(1L);
        List<MunicipioDTO> municipiosEsperados = List.of(GeradorDeEntidades.retornaMunicipioDTO1());

        assertThat(municipiosRetornados).hasSize(1);
        assertThat(municipiosRetornados).usingRecursiveComparison().isEqualTo(municipiosEsperados);

        verify(estadoRepository).findById(1L);
        verify(municipioMapper).paraListaDTO(anyList());
    }

    @Test
    @DisplayName("Deve lancar exceção de estado nao encontrado quando o estado não existir")
    void buscarEstadosComMunicipio_ShouldReturnEstadoNaoEncontradoException_WhenEstadoDoesNotExist() {
        when(estadoRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> estadoService.buscarMunicipiosDoEstado(1L)).isInstanceOf(EstadoNaoEcontrado.class)
                .hasMessage("Erro! Nenhum estado foi encontrado");
    }

    @Test
    @DisplayName("Deve retornar uma lista vazia quando o estado não tiver municípios salvos")
    void buscarEstadosComMunicipio_ReturnEmptyList_WhenEstadoDoesNotHaveMunicipios() {
        when(estadoRepository.findById(anyLong())).thenReturn(Optional.of(GeradorDeEntidades.retornaEstadoEntityComMunicipio1()));
        when(municipioRepository.findByEstadoId(anyLong())).thenReturn(List.of());

        when(municipioMapper.paraListaDTO(anyList())).thenReturn(Collections.emptyList());

        List<MunicipioDTO> municipios = estadoService.buscarMunicipiosDoEstado(1L);

        assertThat(municipios).isEmpty();

        verify(estadoRepository).findById(1L);
    }
}