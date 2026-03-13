package br.com.pedro.api.controller;

import br.com.pedro.api.domain.Info;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/info")
public class InfoController {

    @GetMapping(path = {"", "/"})
    public Info mostrarInfo() {
        return Info.builder().atribuicoes(Map.of(
                "coordenadas",
                "Powered by Geoapify (https://www.geoapify.com/) | © OpenStreetMap contributors",
                "municipios",
                "IBGE (https://servicodados.ibge.gov.br)")).build();
    }
}
