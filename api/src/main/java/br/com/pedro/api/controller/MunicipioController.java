package br.com.pedro.api.controller;

import br.com.pedro.api.domain.DTO.EstadoDTO;
import br.com.pedro.api.domain.DTO.MunicipioDTO;
import br.com.pedro.api.service.MunicipioService;
import br.com.pedro.api.swagger.MunicipioControllerSwagger;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/municipios")
@Tag(name = "Município")
public class MunicipioController implements MunicipioControllerSwagger {

    private final MunicipioService municipioService;

    @Autowired
    public MunicipioController(MunicipioService municipioService) {
        this.municipioService = municipioService;
    }

    @GetMapping(path = {"", "/"})
    public ResponseEntity<List<MunicipioDTO>> listarTodos(@RequestParam(value = "nome", required = false) String nome,
                                                          @RequestParam(value = "estado", required = false) String estado) {

        log.debug("Requisição para listar municípios. Filtro nome={}, filtro estado={}", nome, estado);

        List<MunicipioDTO> municipios = municipioService.listarTodos(nome, estado);
        return ResponseEntity.ok(municipios);
    }

    @GetMapping(path = {"/{id}", "/{id}/"})
    public ResponseEntity<MunicipioDTO> buscarPorId(@PathVariable("id") Long id) {

        log.debug("Requisição para buscar município por id={}", id);

        MunicipioDTO municipio = municipioService.buscarPorId(id);
        return ResponseEntity.ok(municipio);
    }

    @GetMapping(path = {"/{id}/estado", "/{id}/estado/"})
    public ResponseEntity<EstadoDTO> buscarEstadoDoMunicipio(@PathVariable("id") Long id) {

        log.debug("Requisição para buscar estado do município id={}", id);

        EstadoDTO estado = municipioService.buscarEstadoDoMunicipio(id);
        return ResponseEntity.ok(estado);
    }
}
