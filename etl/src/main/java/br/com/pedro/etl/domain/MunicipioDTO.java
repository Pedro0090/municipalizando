package br.com.pedro.etl.domain;

import lombok.*;

@Getter
@Setter
@Builder
public class MunicipioDTO {

    private Long id;
    private String nome;
    private Integer populacao;
    private Double latitude;
    private Double longitude;
    private String nomeNormalizado;
    private EstadoDTO estadoDTO;
}
