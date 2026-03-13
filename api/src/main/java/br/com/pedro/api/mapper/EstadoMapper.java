package br.com.pedro.api.mapper;

import br.com.pedro.api.domain.DTO.EstadoDTO;
import br.com.pedro.api.domain.EstadoEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EstadoMapper {

    EstadoDTO paraDTO(EstadoEntity estadoEntity);

    List<EstadoDTO> paraListaDTO(List<EstadoEntity> estados);
}
