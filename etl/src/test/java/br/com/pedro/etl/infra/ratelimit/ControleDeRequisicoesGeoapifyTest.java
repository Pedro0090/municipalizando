package br.com.pedro.etl.infra.ratelimit;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ControleDeRequisicoesGeoapifyTest {

    @Test
    @DisplayName("Deve retornar um Intervalo válido quando não houver entidades salvas")
    void calculaLimiteDeRequisicoesDiario_ShouldReturnValidInterval_WhenNoSavedEntities() {

        ControleDeRequisicoesGeoapify controle = new ControleDeRequisicoesGeoapify();
        Intervalo intervaloCalculado = controle.calculaLimiteDeRequisicoesDiario(0, 800);
        Intervalo intervaloEsperado = new Intervalo(0, 800);

        assertThat(intervaloCalculado).usingRecursiveComparison().isEqualTo(intervaloEsperado);
    }

    @Test
    @DisplayName("Deve retornar um Intervalo que começa pela última entidade salva quando houver entidades salvas")
    void calculaLimiteDeRequisicoesDiario_ShouldStartFromSavedEntities_WhenSavedEntities() {

        ControleDeRequisicoesGeoapify controle = new ControleDeRequisicoesGeoapify();
        Intervalo intervaloCalculado = controle.calculaLimiteDeRequisicoesDiario(50, 3000);
        Intervalo intervaloEsperado = new Intervalo(50, 3000);

        assertThat(intervaloCalculado).usingRecursiveComparison().isEqualTo(intervaloEsperado);
    }

    @Test
    @DisplayName("Deve retornar um Intervalo limitado quando o tamanho da lista for maior que o limite")
    void calculaLimiteDeRequisicoesDiario_ShouldReturnLimitedInterval_WhenListLargerThanLimit() {

        ControleDeRequisicoesGeoapify controle = new ControleDeRequisicoesGeoapify();
        Intervalo intervaloCalculado = controle.calculaLimiteDeRequisicoesDiario(50, 5000);
        Intervalo intervaloEsperado = new Intervalo(50, 3049);

        assertThat(intervaloCalculado).usingRecursiveComparison().isEqualTo(intervaloEsperado);
    }

    @Test
    @DisplayName("Deve retornar um Intervalo limitado quando o tamanho da lista for maior que o limite e não houver entidades salvas")
    void calculaLimiteDeRequisicoesDiario_ShouldReturnLimitedInterval_WhenListLargerThanLimitAndNoSavedEntities() {

        ControleDeRequisicoesGeoapify controle = new ControleDeRequisicoesGeoapify();
        Intervalo intervaloCalculado = controle.calculaLimiteDeRequisicoesDiario(0, 5000);
        Intervalo intervaloEsperado = new Intervalo(0, 2999);

        assertThat(intervaloCalculado).usingRecursiveComparison().isEqualTo(intervaloEsperado);
    }

    @Test
    @DisplayName("Deve retornar um Intervalo limitado quando o tamanho da lista for menor que o total de entidades salvas")
    void calculaLimiteDeRequisicoesDiario_ShouldReturnLimited_WhenListSmallerThanSavedEntities() {

        ControleDeRequisicoesGeoapify controle = new ControleDeRequisicoesGeoapify();
        Intervalo intervaloCalculado = controle.calculaLimiteDeRequisicoesDiario(50, 25);
        Intervalo intervaloEsperado = new Intervalo(0, 25);

        assertThat(intervaloCalculado).usingRecursiveComparison().isEqualTo(intervaloEsperado);
    }

}