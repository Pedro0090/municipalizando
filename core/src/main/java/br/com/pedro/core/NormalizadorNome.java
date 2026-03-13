package br.com.pedro.core;

import java.text.Normalizer;

public class NormalizadorNome {

    public static String normalizaNome(String nome) {

        if (nome == null || nome.isBlank()) {
            return nome;
        }

        String nomeNormalizado = Normalizer.normalize(nome.trim(), Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        nomeNormalizado = nomeNormalizado.replaceAll("['-]", " ");
        nomeNormalizado = nomeNormalizado.replaceAll("\\s+", " ").trim();

        return nomeNormalizado.toLowerCase();
    }
}
