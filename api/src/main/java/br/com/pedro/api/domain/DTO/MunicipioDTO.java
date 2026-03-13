package br.com.pedro.api.domain.DTO;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder({"id", "nome", "populacao", "latitude", "longitude", "estado"})
@Schema(name = "Municipio")
public class MunicipioDTO {

    private Long id;
    private String nome;
    private Integer populacao;
    private Double latitude;
    private Double longitude;
    private EstadoDTO estado;
}
