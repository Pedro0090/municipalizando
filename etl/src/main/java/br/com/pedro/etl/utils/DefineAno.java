package br.com.pedro.etl.utils;

import java.time.LocalDate;
import java.time.Month;

public class DefineAno {

    public static Integer defineAnoDoPdf() {

        LocalDate anoAtual = LocalDate.now();
        boolean isPdfAtualizado = anoAtual.isAfter(LocalDate.of(anoAtual.getYear(), Month.JULY, 2));
        return isPdfAtualizado ? anoAtual.getYear() : anoAtual.getYear() - 1;
    }
}
