package br.com.pedro.api.service;

import br.com.pedro.api.domain.DTO.EstadoDTO;
import br.com.pedro.api.domain.DTO.MunicipioDTO;
import br.com.pedro.api.domain.EstadoEntity;
import br.com.pedro.api.domain.MunicipioEntity;
import br.com.pedro.api.exception.MunicipioNaoEncontrado;
import br.com.pedro.api.mapper.EstadoMapper;
import br.com.pedro.api.mapper.MunicipioMapper;
import br.com.pedro.api.respository.MunicipioRepository;
import br.com.pedro.core.NormalizadorNome;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class MunicipioService {

    private final MunicipioRepository municipioRepository;
    private final MunicipioMapper municipioMapper;
    private final EstadoMapper estadoMapper;

    @Autowired
    public MunicipioService(MunicipioRepository municipioRepository, MunicipioMapper municipioMapper, EstadoMapper estadoMapper) {
        this.municipioRepository = municipioRepository;
        this.municipioMapper = municipioMapper;
        this.estadoMapper = estadoMapper;
    }

    public List<MunicipioDTO> listarTodos(String nome, String estado) {

        boolean isNomeInformado = nome != null && !nome.isBlank();
        boolean isEstadoInformado = estado != null && !estado.isBlank();
        boolean isEstadoSigla = isEstadoInformado && estado.length() == 2;

        String nomeMunicipioNormalizado = NormalizadorNome.normalizaNome(nome);

        if (isNomeInformado && !isEstadoInformado) {

            log.debug("Buscando municípios com filtro nome={}", nome);

            List<MunicipioEntity> municipios = municipioRepository.findByNomeNormalizado(nomeMunicipioNormalizado);

            return municipioMapper.paraListaDTO(municipios);
        }

        if (isNomeInformado) {

            log.debug("Buscando municípios com filtro nome={}, estado={}", nome, estado);

            List<MunicipioEntity> municipios;

            if (isEstadoSigla) {
                municipios = municipioRepository.findByNomeNormalizadoAndEstadoSigla(
                        nomeMunicipioNormalizado, estado.toUpperCase().trim());
            } else {
                 municipios = municipioRepository.findByNomeNormalizadoAndEstadoNomeNormalizado(
                        nomeMunicipioNormalizado, NormalizadorNome.normalizaNome(estado));
            }

            return municipioMapper.paraListaDTO(municipios);
        }

        if (isEstadoInformado) {

            log.warn("Parâmetro inválido: parâmetro estado foi informado sem nome. estado={}", estado);

            throw new IllegalArgumentException("Erro! Parâmetro 'estado' só pode ser usado junto com nome");
        }

        log.info("Buscando todos os municípios");

        List<MunicipioEntity> municipios = municipioRepository.findAll();
        return municipioMapper.paraListaDTO(municipios);
    }

    public MunicipioDTO buscarPorId(Long id) {

        log.debug("Buscando municipio por id={}", id);

        MunicipioEntity municipio = municipioRepository.findById(id)
                .orElseThrow(MunicipioNaoEncontrado::new);
        return municipioMapper.paraDTO(municipio);
    }

    public EstadoDTO buscarEstadoDoMunicipio(Long id) {

        log.debug("Buscando estado do municipio id={}", id);

        MunicipioEntity municipio = municipioRepository.findById(id).orElseThrow(MunicipioNaoEncontrado::new);

        EstadoEntity estado = municipio.getEstado();

        return estadoMapper.paraDTO(estado);
    }
}
