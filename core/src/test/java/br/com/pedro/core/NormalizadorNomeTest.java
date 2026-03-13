package br.com.pedro.core;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class NormalizadorNomeTest {

    @Test
    @DisplayName("Deve retornar uma string normalizada quando o nome contém caracteres especiais")
    void normalizaNome_ShouldReturnNormalizedString_WhenNameContainsSpecialCharacters() {
        String nomeMunicipio = "Pingo-d'Água";
        String nomeNormalizado = NormalizadorNome.normalizaNome(nomeMunicipio);
        String nomeEsperado = "pingo d agua";

        assertThat(nomeNormalizado).isEqualTo(nomeEsperado);
    }

    @Test
    @DisplayName("Deve retornar uma string vazia quando o nome é vazio")
    void normalizaNome_ShouldReturnEmptyString_WhenNameIsBlank() {
        String nomeInvalido = NormalizadorNome.normalizaNome("");

        assertThat(nomeInvalido).isBlank();
    }

    @Test
    @DisplayName("Deve retornar nulo quando o nome é nulo")
    void normalizaNome_ShouldReturnNull_WhenNameIsNull() {
        assertThat(NormalizadorNome.normalizaNome(null)).isNull();
    }

    @Test
    @DisplayName("Deve retornar uma string normalizada quando o nome contém espaços extras")
    void normalizaNome_ShouldReturnNormalizedString_WhenNameContainsExtraSpaces() {
        String nomeMunicipio = "  São  Paulo ";
        String nomeNormalizado = NormalizadorNome.normalizaNome(nomeMunicipio);
        String nomeEsperado = "sao paulo";

        assertThat(nomeNormalizado).isEqualTo(nomeEsperado);
    }

    @Test
    @DisplayName("Deve retornar uma string vazia quando o nome contém apenas espaços")
    void normalizaNome_ShouldReturnEmptyString_WhenNameContainsOnlySpaces() {
        String nomeInvalido = "    ";
        String nomeNormalizado = NormalizadorNome.normalizaNome(nomeInvalido);

        assertThat(nomeNormalizado).isBlank();
    }

}
