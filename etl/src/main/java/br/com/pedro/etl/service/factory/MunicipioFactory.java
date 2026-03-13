package br.com.pedro.etl.service.factory;

import br.com.pedro.core.NormalizadorNome;
import br.com.pedro.etl.domain.EstadoDTO;
import br.com.pedro.etl.domain.MunicipioDTO;
import br.com.pedro.etl.geoapify.GeoapifyClient;
import br.com.pedro.etl.geoapify.entidadesGeoapify.MunicipioGeoapifyDTO;
import br.com.pedro.etl.ibge.entidadesIBGE.MunicipioIbgeDTO;
import br.com.pedro.etl.pdf.LeitorPDF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MunicipioFactory {

    private final GeoapifyClient geoapifyClient;
    private final LeitorPDF leitorPDF;

    @Autowired
    public MunicipioFactory(GeoapifyClient geoapifyClient, LeitorPDF leitorPDF) {
        this.geoapifyClient = geoapifyClient;
        this.leitorPDF = leitorPDF;
    }

    public MunicipioDTO montarDTO(MunicipioIbgeDTO municipioIbgeDTO, EstadoDTO estadoDTO) {
        MunicipioGeoapifyDTO coordenadas = geoapifyClient.buscarCoordenadas(municipioIbgeDTO);
        Integer populacao = leitorPDF.buscarPopulacaoMunicipio(municipioIbgeDTO.getId());

        return MunicipioDTO.builder()
                .id(municipioIbgeDTO.getId())
                .nome(municipioIbgeDTO.getNome())
                .populacao(populacao).
                latitude(coordenadas.getLat())
                .longitude(coordenadas.getLon())
                .nomeNormalizado(NormalizadorNome.normalizaNome(municipioIbgeDTO.getNome()))
                .estadoDTO(estadoDTO)
                .build();
    }
}
