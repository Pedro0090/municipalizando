package br.com.pedro.etl.ibge.entidadesIBGE;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MunicipioIbgeDTO {

    private Long id;

    private String nome;

    @JsonProperty("regiao-imediata")
    private RegiaoImediata regiaoImediata;

    public EstadoIbgeDTO getEstadoIbgeDTO() {
        return this.getRegiaoImediata().getRegiaoIntermediaria().getEstadoIbgeDTO();
    }
}
