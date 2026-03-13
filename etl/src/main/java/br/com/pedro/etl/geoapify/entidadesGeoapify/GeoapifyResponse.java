package br.com.pedro.etl.geoapify.entidadesGeoapify;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GeoapifyResponse {

    private List<Feature> features;
}
