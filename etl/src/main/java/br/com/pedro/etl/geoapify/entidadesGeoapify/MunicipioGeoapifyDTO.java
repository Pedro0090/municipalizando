package br.com.pedro.etl.geoapify.entidadesGeoapify;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MunicipioGeoapifyDTO {

    private String city;
    private Double lat;
    private Double lon;
    private String state_code;
    private Rank rank;
}
