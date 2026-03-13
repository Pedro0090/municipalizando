package br.com.pedro.api.mapper;

import br.com.pedro.api.domain.DTO.MunicipioDTO;
import br.com.pedro.api.domain.MunicipioEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = EstadoMapper.class)
public interface MunicipioMapper {

    MunicipioDTO paraDTO(MunicipioEntity municipio);

    List<MunicipioDTO> paraListaDTO(List<MunicipioEntity> municipios);
}
