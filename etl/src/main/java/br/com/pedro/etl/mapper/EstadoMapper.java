package br.com.pedro.etl.mapper;

import br.com.pedro.etl.domain.EstadoDTO;
import br.com.pedro.etl.domain.EstadoEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EstadoMapper {

    EstadoEntity toEntity(EstadoDTO estadoDTO);
}
