package br.com.pedro.api.controller;

import br.com.pedro.api.domain.DTO.EstadoDTO;
import br.com.pedro.api.domain.DTO.MunicipioDTO;
import br.com.pedro.api.service.EstadoService;
import br.com.pedro.api.swagger.EstadoControllerSwagger;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/estados")
@Tag(name = "Estado")
public class EstadoController implements EstadoControllerSwagger {

    private final EstadoService estadoService;

    @Autowired
    public EstadoController(EstadoService estadoService) {
        this.estadoService = estadoService;
    }


    @GetMapping(path = {"", "/"})
    public ResponseEntity<List<EstadoDTO>> listarTodos(@RequestParam(value = "nome", required = false) String nome) {

        log.debug("Requisição para listar estados. Filtro nome={}", nome);

        List<EstadoDTO> estado = estadoService.listarTodos(nome);
        return ResponseEntity.ok(estado);
    }

    @GetMapping(path = {"/{id}", "/{id}/"})
    public ResponseEntity<EstadoDTO> buscarPorId(@PathVariable("id") Long id) {

        log.debug("Requisição para buscar estado por id={} ", id);

        EstadoDTO estado = estadoService.buscarPorId(id);
        return ResponseEntity.ok(estado);
    }

    @GetMapping(path = {"/{id}/municipios", "/{id}/municipios/"})
    public ResponseEntity<List<MunicipioDTO>> buscarMunicipiosDoEstado(@PathVariable("id") Long id) {

        log.debug("Requisição para buscar municípios do estado id={}", id);

        List<MunicipioDTO> municipios = estadoService.buscarMunicipiosDoEstado(id);
        return ResponseEntity.ok(municipios);
    }
}
