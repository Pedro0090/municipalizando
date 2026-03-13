package br.com.pedro.etl.geoapify;

import br.com.pedro.etl.exception.MunicipioNaoBuscadoException;
import br.com.pedro.etl.geoapify.entidadesGeoapify.Feature;
import br.com.pedro.etl.geoapify.entidadesGeoapify.GeoapifyResponse;
import br.com.pedro.etl.geoapify.entidadesGeoapify.MunicipioGeoapifyDTO;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

@Component
public class ExtratorGeoapify {

    public MunicipioGeoapifyDTO extrairMunicipioGeoapifyDTO(List<GeoapifyResponse> responses) {

        return responses.stream()
                .flatMap(r -> r.getFeatures().stream())
                .map(Feature::getProperties)
                .filter(f -> f.getRank().getImportance() != null)
                .max(Comparator.comparingDouble(f -> f.getRank().getImportance()))
                .orElseThrow(() -> new MunicipioNaoBuscadoException("Erro! Município não encontrado"));
    }
}
