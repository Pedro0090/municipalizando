package br.com.pedro.etl.geoapify.entidadesGeoapify;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Feature {

    private MunicipioGeoapifyDTO properties;
}
