package br.com.pedro.etl.domain;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EstadoDTO {

    private Long id;
    private String nome;
    private String sigla;
    private String nomeNormalizado;
}
