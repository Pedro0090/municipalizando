package br.com.pedro.etl.service;

import br.com.pedro.etl.domain.EstadoDTO;
import br.com.pedro.etl.domain.EstadoEntity;
import br.com.pedro.etl.domain.MunicipioDTO;
import br.com.pedro.etl.domain.MunicipioEntity;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class EtlService {

    private final IbgeClient ibgeClient;
    private final MunicipioMapper municipioMapper;
    private final EstadoMapper estadoMapper;
    private final EstadoRepository estadoRepository;
    private final MunicipioRepository municipioRepository;
    private final ControleDeRequisicoesGeoapify controleDeRequisicoesGeoapify;
    private final MunicipioFactory municipioFactory;
    private final EstadoFactory estadoFactory;
    private final Limitador limitador;

    @Autowired
    public EtlService(IbgeClient ibgeClient, MunicipioMapper municipioMapper, EstadoMapper estadoMapper, EstadoRepository estadoRepository, MunicipioRepository municipioRepository, ControleDeRequisicoesGeoapify controleDeRequisicoesGeoapify, MunicipioFactory municipioFactory, EstadoFactory estadoFactory, Limitador limitador) {
        this.ibgeClient = ibgeClient;
        this.municipioMapper = municipioMapper;
        this.estadoMapper = estadoMapper;
        this.estadoRepository = estadoRepository;
        this.municipioRepository = municipioRepository;
        this.controleDeRequisicoesGeoapify = controleDeRequisicoesGeoapify;
        this.municipioFactory = municipioFactory;
        this.estadoFactory = estadoFactory;
        this.limitador = limitador;
    }

    private List<MunicipioDTO> listarMuncipiosDTO(Map<Long, EstadoDTO> map) {

        List<MunicipioIbgeDTO> listaMunicipiosIbge = ibgeClient.listarMunicipios();

        List<MunicipioDTO> listaMunicipiosDTO = new ArrayList<>();

        if (municipioRepository.count() == listaMunicipiosIbge.size()) {
            municipioRepository.deleteAll();
        }

        // Só use sequencialamente!!!
        Intervalo intervalo = controleDeRequisicoesGeoapify.calculaLimiteDeRequisicoesDiario(
                municipioRepository.count(), listaMunicipiosIbge.size());

        log.info("Construindo os DTOs dos municipios...");
        for (int inicio = intervalo.inicio(); inicio < intervalo.fim(); inicio++) {

            MunicipioIbgeDTO municipioIbgeDTO = listaMunicipiosIbge.get(inicio);
            EstadoDTO estadoDTO = map.get(municipioIbgeDTO.getEstadoIbgeDTO().getId());

            MunicipioDTO municipioDTO = municipioFactory.montarDTO(municipioIbgeDTO, estadoDTO);
            listaMunicipiosDTO.add(municipioDTO);
            log.debug("munipio numero {} adicionado na lista", inicio + 1);
        log.info("DTOs construídos!");

            limitador.aguardar();
        }
        return listaMunicipiosDTO;
    }

    private List<EstadoDTO> listarEstadosDTO() {
        List<EstadoIbgeDTO> listaEstadosDTO = ibgeClient.listarEstados();

        return listaEstadosDTO.stream().map(estadoFactory::montarDTO).toList();
    }

    public void executar() {

        log.info("Listando estados...");
        List<EstadoDTO> listaEstadosDTOProntos = listarEstadosDTO();

        Map<Long, EstadoDTO> mapEstadoDTO = listaEstadosDTOProntos.stream().collect(Collectors.
                toMap(EstadoDTO::getId, Function.identity()));

        if (estadoRepository.count() != listaEstadosDTOProntos.size()) {

            log.info("Listando estados prontos para salvar...");
            List<EstadoEntity> listaEstados = listaEstadosDTOProntos.stream().map(estadoMapper::toEntity).toList();

            log.info("Salvando estados...");
            estadoRepository.saveAll(listaEstados);
            log.info("{} estados salvos!",  listaEstados.size());
        }

        log.info("Listando municipios...");
        List<MunicipioDTO> listaMunicipiosDTOsProntos = listarMuncipiosDTO(mapEstadoDTO);

        log.info("Listando municipios prontos para salvar...");
        List<MunicipioEntity> listaMunicipios = listaMunicipiosDTOsProntos.stream().map(municipioMapper::toEntity).toList();

        log.info("Salvando municipios...");
        municipioRepository.saveAll(listaMunicipios);
        log.info("{} municipios salvos!", listaMunicipios.size());
    }
}
