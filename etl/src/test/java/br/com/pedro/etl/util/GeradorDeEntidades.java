package br.com.pedro.etl.util;

import br.com.pedro.etl.domain.EstadoDTO;
import br.com.pedro.etl.domain.EstadoEntity;
import br.com.pedro.etl.domain.MunicipioDTO;
import br.com.pedro.etl.domain.MunicipioEntity;
import br.com.pedro.etl.geoapify.entidadesGeoapify.Feature;
import br.com.pedro.etl.geoapify.entidadesGeoapify.GeoapifyResponse;
import br.com.pedro.etl.geoapify.entidadesGeoapify.MunicipioGeoapifyDTO;
import br.com.pedro.etl.geoapify.entidadesGeoapify.Rank;
import br.com.pedro.etl.ibge.entidadesIBGE.EstadoIbgeDTO;
import br.com.pedro.etl.ibge.entidadesIBGE.MunicipioIbgeDTO;
import br.com.pedro.etl.ibge.entidadesIBGE.RegiaoImediata;
import br.com.pedro.etl.ibge.entidadesIBGE.RegiaoIntermediaria;

import java.util.Collections;
import java.util.List;

public class GeradorDeEntidades {
    
    public static MunicipioIbgeDTO retornaMunicipioIbgeDTO(){
        EstadoIbgeDTO estadoibgeDTO = retornaEstadoIbgeDTO();
        RegiaoIntermediaria regiaoIntermediaria = RegiaoIntermediaria.builder().estadoIbgeDTO(estadoibgeDTO).build();
        RegiaoImediata regiaoImediata = RegiaoImediata.builder().regiaoIntermediaria(regiaoIntermediaria).build();

        return MunicipioIbgeDTO.builder().id(1100015L).nome("Alta Floresta D'Oeste")
                .regiaoImediata(regiaoImediata).build();
    }
    

    public static MunicipioGeoapifyDTO retornaMunicipioGeoapifyDTO() {
        Rank rank = Rank.builder().importance(0.410586427023836).build();

        return MunicipioGeoapifyDTO.builder()
                .city("Alta Floresta D'Oeste")
                .lat(-11.9296917)
                .lon(-61.9961284)
                .state_code("RO")
                .rank(rank)
                .build();
    }

    public static EstadoIbgeDTO retornaEstadoIbgeDTO() {
        return EstadoIbgeDTO.builder().id(11L).nome("Rondônia").sigla("RO").build();
    }
    
    public static EstadoDTO retornaEstadoDTO() {
        return EstadoDTO.builder().id(11L).nome("Rondônia").sigla("RO").nomeNormalizado("rondonia").build();
    }

    
    public static MunicipioDTO retornaMunicipioDTO() {
        return MunicipioDTO.builder()
                .id(1100015L)
                .nome("Alta Floresta D'Oeste")
                .populacao(22787)
                .latitude(-11.9296917)
                .longitude(-61.9961284)
                .estadoDTO(retornaEstadoDTO())
                .nomeNormalizado("alta floresta d oeste")
                .build();
    }

    public static EstadoEntity retornaEstadoEntity() {
        return EstadoEntity.builder().id(11L).nome("Rondônia").sigla("RO").nomeNormalizado("rondonia").build();
    }

    public static MunicipioEntity retornaMunicipioEntity() {
        return MunicipioEntity.builder().id(1100015L)
                .nome("Alta Floresta D'Oeste")
                .populacao(22787)
                .latitude(-11.9296917)
                .longitude(-61.9961284)
                .estadoEntity(retornaEstadoEntity())
                .nomeNormalizado("alta floresta d oeste")
                .build();
    }

    public static List<GeoapifyResponse>  retornaListaGeoapifyResponse() {
        
        Rank rank = Rank.builder().importance(0.16001).build();

        MunicipioGeoapifyDTO municipio1 = retornaMunicipioGeoapifyDTO();
        Feature feature1 = Feature.builder().properties(municipio1).build();
        
        MunicipioGeoapifyDTO municipio2 = MunicipioGeoapifyDTO.builder()
                .city("Alta Floresta D'Oeste")
                .lat(-12.08587525).lon(-61.9795656838979)
                .state_code("RO").rank(rank).build();
        Feature feature2 = Feature.builder().properties(municipio2).build();

        List<Feature> features = List.of(feature1, feature2);

        List<GeoapifyResponse> geoapifyResponses = Collections.singletonList(GeoapifyResponse.builder().features(features).build());

        return geoapifyResponses;
    }
}
