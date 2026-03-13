package br.com.pedro.api.service;

import br.com.pedro.api.domain.DTO.EstadoDTO;
import br.com.pedro.api.domain.DTO.MunicipioDTO;
import br.com.pedro.api.exception.MunicipioNaoEncontrado;
import br.com.pedro.api.mapper.EstadoMapper;
import br.com.pedro.api.mapper.MunicipioMapper;
import br.com.pedro.api.respository.EstadoRepository;
import br.com.pedro.api.respository.MunicipioRepository;
import br.com.pedro.api.util.GeradorDeEntidades;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MunicipioServiceTest {

    @Mock
    private MunicipioRepository municipioRepository;

    @Mock
    private MunicipioMapper municipioMapper;

    @Mock
    private EstadoRepository estadoRepository;

    @Mock
    private EstadoMapper estadoMapper;

    @InjectMocks
    private MunicipioService municipioService;


    static Stream<Arguments> noNameProvider() {
        return Stream.of(
                Arguments.of("", ""),
                Arguments.of("", " "),
                Arguments.of(null, ""),
                Arguments.of(null, " "),
                Arguments.of(null, null),
                Arguments.of("", null),
                Arguments.of(" ", null)
        );
    }

    @ParameterizedTest
    @MethodSource("noNameProvider")
    @DisplayName("Deve retornar todos os municípios quando nenhum filtro foi indicado")
    void listarTodos_ShouldReturnAllMunicipios_WhenNoFiltersProvided(String nome, String estado) {
        when(municipioRepository.findAll()).thenReturn(List.of(GeradorDeEntidades.retornaMunicipioEntity1(),
                GeradorDeEntidades.retornaMunicipioEntity2()));
        when(municipioMapper.paraListaDTO(anyList())).thenReturn(List.of(GeradorDeEntidades.retornaMunicipioDTO1(),
                GeradorDeEntidades.retornaMunicipioDTO2()));

        List<MunicipioDTO> municipiosRetornados = municipioService.listarTodos(nome, estado);
        List<MunicipioDTO> municipiosEsperados = List.of(GeradorDeEntidades.retornaMunicipioDTO1(),
                GeradorDeEntidades.retornaMunicipioDTO2());

        assertThat(municipiosRetornados).hasSize(2);
        assertThat(municipiosRetornados).usingRecursiveComparison().isEqualTo(municipiosEsperados);

        verify(municipioRepository).findAll();
        verifyNoMoreInteractions(municipioRepository);
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", " "})
    @DisplayName("Deve retornar os municípios filtrados quando apenas o nome foi indicado")
    void listarTodos_ShouldReturnFilteredMunicipios_WhenOnlyNameIsProvided(String estado) {
        when(municipioRepository.findByNomeNormalizado("alta floresta d oeste")).thenReturn
                (List.of(GeradorDeEntidades.retornaMunicipioEntity1()));
        when(municipioMapper.paraListaDTO(anyList())).thenReturn(List.of(GeradorDeEntidades.retornaMunicipioDTO1()));

        List<MunicipioDTO> municipioRetornado = municipioService.listarTodos("Alta Floresta D'Oeste", estado);
        List<MunicipioDTO> municipioEsperado = List.of(GeradorDeEntidades.retornaMunicipioDTO1());

        assertThat(municipioRetornado).hasSize(1);
        assertThat(municipioRetornado).usingRecursiveComparison().isEqualTo(municipioEsperado);

        verify(municipioRepository).findByNomeNormalizado("alta floresta d oeste");
        verify(municipioRepository, never()).findAll();
    }

    @Test
    @DisplayName("Deve retornar municípios filtrados quando o nome e a sigla dos estados forem indicados")
    void listarTodos_ShouldReturnFilteredMunicipios_WhenNameAndEstadoSiglaAreProvided() {
        when(municipioRepository.findByNomeNormalizadoAndEstadoSigla("alta floresta d oeste", "RO"))
                .thenReturn(List.of(GeradorDeEntidades.retornaMunicipioEntity1()));
        when(municipioMapper.paraListaDTO(anyList())).thenReturn(List.of(GeradorDeEntidades.retornaMunicipioDTO1()));

        List<MunicipioDTO> municipioRetornado = municipioService.listarTodos("Alta Floresta D'Oeste", "ro");
        List<MunicipioDTO> municipioEsperado = List.of(GeradorDeEntidades.retornaMunicipioDTO1());

        assertThat(municipioRetornado).hasSize(1);
        assertThat(municipioRetornado).usingRecursiveComparison().isEqualTo(municipioEsperado);

        verify(municipioRepository).findByNomeNormalizadoAndEstadoSigla("alta floresta d oeste", "RO");
        verifyNoMoreInteractions(municipioRepository);
    }

    @Test
    @DisplayName("Deve retornar municípios filtrados quando o nome e o estados forem indicados")
    void listarTodos_ShouldReturnFilteredMunicipios_WhenNameAndEstadoAreProvided() {
        when(municipioRepository.findByNomeNormalizadoAndEstadoNomeNormalizado
                ("alta floresta d oeste", "rondonia")).thenReturn(List.of(
                GeradorDeEntidades.retornaMunicipioEntity1()));
        when(municipioMapper.paraListaDTO(anyList())).thenReturn(List.of(GeradorDeEntidades.retornaMunicipioDTO1()));

        List<MunicipioDTO> municipioRetornado = municipioService.listarTodos("Alta Floresta D'Oeste", "Rondônia");
        List<MunicipioDTO> municipioEsperado = List.of(GeradorDeEntidades.retornaMunicipioDTO1());

        assertThat(municipioRetornado).hasSize(1);
        assertThat(municipioRetornado).usingRecursiveComparison().isEqualTo(municipioEsperado);

        verify(municipioRepository).findByNomeNormalizadoAndEstadoNomeNormalizado
                ("alta floresta d oeste", "rondonia");
        verifyNoMoreInteractions(municipioRepository);
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", " "})
    @DisplayName("Deve retornar uma lista vazia quando o filtro nome não tiver resultados")
    void listarTodos_ShouldReturnEmptyList_WhenOnlyNameProvidedAndNoResultsFound(String estado) {
        when(municipioRepository.findByNomeNormalizado(anyString())).thenReturn(Collections.emptyList());

        assertThat(municipioService.listarTodos("ah-se'éT", estado)).isEmpty();

        verify(municipioRepository).findByNomeNormalizado("ah se et");
        verifyNoMoreInteractions(municipioRepository);
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", " "})
    @DisplayName("Deve lançar exceção de argumento ilegal quando apenas o filtro estado foi indicado")
    void listarTodos_ShouldThrowIllegalArgumentException_WhenOnlyEstadoProvided(String nome) {
        assertThatThrownBy(() -> municipioService.listarTodos(nome, "RO"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Erro! Parâmetro 'estado' só pode ser usado junto com nome");

        verifyNoInteractions(municipioRepository);
    }

    @Test
    @DisplayName("Deve retornar uma lista vazia quando apenas a sigla do estado não tiver resultados")
    void listarTodos_ShouldReturnEmptyList_WhenNameProvidedAndEstadoSiglaNoResultsFound() {
        when(municipioRepository.findByNomeNormalizadoAndEstadoSigla(anyString(), anyString()))
                .thenReturn(Collections.emptyList());

        assertThat(municipioService.listarTodos("Alta Floresta D'Oeste", "as")).isEmpty();

        verify(municipioRepository).findByNomeNormalizadoAndEstadoSigla("alta floresta d oeste", "AS");
        verifyNoMoreInteractions(municipioRepository);
    }

    @Test
    @DisplayName("Deve lançar exceção de município nao encontrado quando apenas o filtro estado não tem resultados")
    void listarTodos_ShouldReturnEmptyList_WhenNameProvidedAndEstadoNoResultsFound() {
        when(municipioRepository.findByNomeNormalizadoAndEstadoNomeNormalizado(anyString(), anyString()))
                .thenReturn(Collections.emptyList());

        assertThat(municipioService.listarTodos("Alta Floresta d oeste", "asfasfsfsafa")).isEmpty();

        verify(municipioRepository).findByNomeNormalizadoAndEstadoNomeNormalizado
                ("alta floresta d oeste", "asfasfsfsafa");
        verifyNoMoreInteractions(municipioRepository);
    }


    @Test
    @DisplayName("Deve retornar um município quando o município existir")
    void buscarPorId_ShouldReturnMunicipio_WhenMunicipioExists() {
        when(municipioRepository.findById(anyLong())).thenReturn(Optional.of(GeradorDeEntidades.retornaMunicipioEntity1()));
        when(municipioMapper.paraDTO(any())).thenReturn(GeradorDeEntidades.retornaMunicipioDTO1());

        MunicipioDTO municipioRetornado = municipioService.buscarPorId(1L);
        MunicipioDTO municipioEsperado = GeradorDeEntidades.retornaMunicipioDTO1();

        assertThat(municipioRetornado).usingRecursiveComparison().isEqualTo(municipioEsperado);

        verify(municipioRepository).findById(1L);
        verify(municipioMapper).paraDTO(any());
    }

    @Test
    @DisplayName("Deve laçar exceção de município não encontrdo quando o município não existir")
    void buscarPorId_ShouldThrowMunicipioNaoEncontradoException_WhenDoesNotExist() {
        when(municipioRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> municipioService.buscarPorId(1L)).isInstanceOf(MunicipioNaoEncontrado.class)
                .hasMessage("Erro! Nenhum município foi encontrado");
    }

    @Test
    @DisplayName("Deve retornar o estado de um município quando o município existir")
    void buscarEstadoDoMunicipio_ShouldReturnEstado_WhenMunicipioExists() {
        when(municipioRepository.findById(anyLong())).thenReturn(Optional.of(GeradorDeEntidades.retornaMunicipioEntity1()));

        when(estadoMapper.paraDTO(any())).thenReturn(GeradorDeEntidades.retornaEstadoDTO1());

        EstadoDTO estadoRetornado = municipioService.buscarEstadoDoMunicipio(1L);
        EstadoDTO estadoEsperado = GeradorDeEntidades.retornaEstadoDTO1();

        assertThat(estadoRetornado).usingRecursiveComparison().isEqualTo(estadoEsperado);

        verify(municipioRepository).findById(1L);
        verify(estadoMapper).paraDTO(any());
    }

    @Test
    @DisplayName("Deve lançar exceção de município não encontrado quando o município não existir")
    void buscarEstadoDoMunicipio_ShouldThrowMunicipioNaoEncontradoException_WhenMunicipioDoesNotExist() {
        when(municipioRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> municipioService.buscarEstadoDoMunicipio(1L)).isInstanceOf(MunicipioNaoEncontrado.class)
                .hasMessage("Erro! Nenhum município foi encontrado");
    }
}