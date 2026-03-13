package br.com.pedro.etl.infra.ratelimit;

import org.springframework.stereotype.Component;

@Component
public class ControleDeRequisicoesGeoapify {

    private static final int LIMITE_DE_REQUISICOES_DIARIO_GEOAPIFY = 2999;

    public Intervalo calculaLimiteDeRequisicoesDiario(long entidadesSalvas, int tamanhoLista) {

        int inicio = 0;

        if (entidadesSalvas > 0 && entidadesSalvas < tamanhoLista) {
            inicio = (int) entidadesSalvas;
        }
        int fim = inicio + LIMITE_DE_REQUISICOES_DIARIO_GEOAPIFY;

        return new Intervalo(inicio, Math.min(fim, tamanhoLista));
    }
}
