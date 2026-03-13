package br.com.pedro.etl.pdf;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class MunicipioPdfDTO {

    private Long id;
    private Integer populacao;
}
