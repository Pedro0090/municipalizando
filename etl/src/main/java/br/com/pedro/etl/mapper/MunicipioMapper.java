package br.com.pedro.etl.mapper;

import br.com.pedro.etl.domain.MunicipioDTO;
import br.com.pedro.etl.domain.MunicipioEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = EstadoMapper.class)
public interface MunicipioMapper {

    @Mapping(target = "estadoEntity", source = "estadoDTO")
    MunicipioEntity toEntity(MunicipioDTO municipioDTO);
}
