package br.com.pedro.etl.service.factory;

import br.com.pedro.core.NormalizadorNome;
import br.com.pedro.etl.domain.EstadoDTO;
import br.com.pedro.etl.ibge.entidadesIBGE.EstadoIbgeDTO;
import org.springframework.stereotype.Component;

@Component
public class EstadoFactory {

    public EstadoDTO montarDTO(EstadoIbgeDTO estadoIbgeDTO) {

        return EstadoDTO.builder().id(estadoIbgeDTO.getId())
                .nome(estadoIbgeDTO.getNome())
                .sigla(estadoIbgeDTO.getSigla())
                .nomeNormalizado(NormalizadorNome.normalizaNome(estadoIbgeDTO.getNome())).build();
    }
}
