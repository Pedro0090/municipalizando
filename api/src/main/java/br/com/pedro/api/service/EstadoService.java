package br.com.pedro.api.service;

import br.com.pedro.api.domain.DTO.EstadoDTO;
import br.com.pedro.api.domain.DTO.MunicipioDTO;
import br.com.pedro.api.domain.EstadoEntity;
import br.com.pedro.api.domain.MunicipioEntity;
import br.com.pedro.api.exception.EstadoNaoEcontrado;
import br.com.pedro.api.mapper.EstadoMapper;
import br.com.pedro.api.mapper.MunicipioMapper;
import br.com.pedro.api.respository.EstadoRepository;
import br.com.pedro.api.respository.MunicipioRepository;
import br.com.pedro.core.NormalizadorNome;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class EstadoService {

    private final EstadoRepository estadoRepository;
    private final EstadoMapper estadoMapper;
    private final MunicipioRepository municipioRepository;
    private final MunicipioMapper municipioMapper;

    @Autowired
    public EstadoService(EstadoRepository estadoRepository, EstadoMapper estadoMapper, MunicipioRepository municipioRepository, MunicipioMapper municipioMapper) {
        this.estadoRepository = estadoRepository;
        this.estadoMapper = estadoMapper;
        this.municipioRepository = municipioRepository;
        this.municipioMapper = municipioMapper;
    }

    public List<EstadoDTO> listarTodos(String nome) {
        if (nome != null && !nome.isBlank()) {

            log.debug("Buscando estados com filtro nome={}", nome);

            List<EstadoEntity> estados = estadoRepository.findByNomeNormalizado(NormalizadorNome.normalizaNome(nome));

            return estadoMapper.paraListaDTO(estados);
        }

        log.debug("Buscando todos os estados");

        List<EstadoEntity> estados = estadoRepository.findAll();
        return estadoMapper.paraListaDTO(estados);
    }

    public EstadoDTO buscarPorId(Long id) {

        log.debug("Buscando estado por id={}", id);

        EstadoEntity estado = estadoRepository.findById(id).orElseThrow(EstadoNaoEcontrado::new);
        return estadoMapper.paraDTO(estado);
    }

    public List<MunicipioDTO> buscarMunicipiosDoEstado(Long id) {
        log.debug("Buscando municipios do estado id={}", id);

        estadoRepository.findById(id).orElseThrow(EstadoNaoEcontrado::new);

        List<MunicipioEntity> municipios = municipioRepository.findByEstadoId(id);

        return municipioMapper.paraListaDTO(municipios);
    }
}
