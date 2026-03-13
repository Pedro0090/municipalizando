package br.com.pedro.etl.ibge.entidadesIBGE;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EstadoIbgeDTO {

    private Long id;
    private String nome;
    private String sigla;
}
