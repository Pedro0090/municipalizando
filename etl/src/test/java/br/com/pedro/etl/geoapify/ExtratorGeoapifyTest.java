package br.com.pedro.etl.geoapify;

import br.com.pedro.etl.exception.MunicipioNaoBuscadoException;
import br.com.pedro.etl.geoapify.entidadesGeoapify.MunicipioGeoapifyDTO;
import br.com.pedro.etl.util.GeradorDeEntidades;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;

class ExtratorGeoapifyTest {

    @Test
    @DisplayName("Deve retornar entidade válida quando tiver sucesso")
    void extrairMunicipioGeoapifyDTO_ShloudReturnValidEntity_WhenSuccessful() {
        ExtratorGeoapify extratorGeoapify = new ExtratorGeoapify();

        MunicipioGeoapifyDTO municipioRetornado = extratorGeoapify.extrairMunicipioGeoapifyDTO(
                GeradorDeEntidades.retornaListaGeoapifyResponse());
        MunicipioGeoapifyDTO municipioEsperado = GeradorDeEntidades.retornaMunicipioGeoapifyDTO();

        Assertions.assertThat(municipioRetornado).isNotNull();
        Assertions.assertThat(municipioRetornado).usingRecursiveComparison().isEqualTo(municipioEsperado);
    }

    @Test
    @DisplayName("Deve retornar exceção quando método retorna lista vazia")
    void extrairMunicipioGeoapifyDTO_ShouldThrowMunicipioNaoBuscadoException_WhenEmptyList() {
        ExtratorGeoapify extratorGeoapify = new ExtratorGeoapify();

        Assertions.assertThatThrownBy(() -> extratorGeoapify.extrairMunicipioGeoapifyDTO(Collections.emptyList()))
                .isInstanceOf(MunicipioNaoBuscadoException.class);
    }
}