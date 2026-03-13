package br.com.pedro.api.domain.DTO;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder({"id", "nome", "sigla"})
@Schema(name = "Estado")
public class EstadoDTO {

    private Long id;
    private String nome;
    private String sigla;
}
