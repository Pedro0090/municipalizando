package br.com.pedro.api.util;

import br.com.pedro.api.domain.DTO.EstadoDTO;
import br.com.pedro.api.domain.DTO.MunicipioDTO;
import br.com.pedro.api.domain.EstadoEntity;
import br.com.pedro.api.domain.MunicipioEntity;

import java.util.List;

public class GeradorDeEntidades {

    public static EstadoDTO retornaEstadoDTO1() {
        return EstadoDTO.builder().id(11L).nome("Rondônia").sigla("RO").build();
    }

    public static EstadoDTO retornaEstadoDTO2() {
        return EstadoDTO.builder().id(35L).nome("São Paulo").sigla("SP").build();
    }

    public static EstadoEntity retornaEstadoEntity1() {
        return EstadoEntity.builder().id(11L).nome("Rondônia").sigla("RO").nomeNormalizado("rondonia").build();
    }

    public static EstadoEntity retornaEstadoEntity2() {
        return EstadoEntity.builder().id(35L).nome("São Paulo").sigla("SP").nomeNormalizado("sao paulo").build();
    }

    public static MunicipioDTO retornaMunicipioDTO1() {
        return MunicipioDTO.builder()
                .id(1100015L)
                .nome("Alta Floresta D'Oeste")
                .populacao(22787)
                .latitude(-11.9296917)
                .longitude(-61.9961284)
                .estado(retornaEstadoDTO1())
                .build();
    }

    public static MunicipioEntity retornaMunicipioEntity1() {
        return MunicipioEntity.builder().id(1100015L)
                .nome("Alta Floresta D'Oeste")
                .populacao(22787)
                .latitude(-11.9296917)
                .longitude(-61.9961284)
                .estado(retornaEstadoEntity1())
                .nomeNormalizado("alta floresta d oeste")
                .build();
    }

    public static MunicipioDTO retornaMunicipioDTO2() {
        return MunicipioDTO.builder()
                .id(3515202L)
                .nome("Estrela d'Oeste")
                .populacao(9696)
                .latitude(-20.28606920)
                .longitude(-50.39862160)
                .estado(retornaEstadoDTO2())
                .build();
    }

    public static MunicipioEntity retornaMunicipioEntity2() {
        return MunicipioEntity.builder()
                .id(3515202L)
                .nome("Estrela d'Oeste")
                .populacao(9696)
                .latitude(-20.28606920)
                .longitude(-50.39862160)
                .estado(retornaEstadoEntity2())
                .nomeNormalizado("estrela d oeste")
                .build();
    }

    public static EstadoEntity retornaEstadoEntityComMunicipio1() {

        EstadoEntity estado = EstadoEntity.builder()
                .id(11L)
                .nome("Rondônia")
                .sigla("RO")
                .build();

        MunicipioEntity municipio = MunicipioEntity.builder()
                .id(1100015L)
                .nome("Alta Floresta D'Oeste")
                .populacao(22787)
                .latitude(-11.9296917)
                .longitude(-61.9961284)
                .estado(estado)
                .build();

        estado.setMunicipios(List.of(municipio));

        return estado;
    }
}
