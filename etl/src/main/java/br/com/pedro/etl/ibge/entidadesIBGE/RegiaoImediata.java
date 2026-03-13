package br.com.pedro.etl.ibge.entidadesIBGE;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegiaoImediata {

    @JsonProperty("regiao-intermediaria")
    private RegiaoIntermediaria regiaoIntermediaria;
}
