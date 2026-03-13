package br.com.pedro.etl.pdf;

import br.com.pedro.etl.exception.PdfNaoAcessadoException;
import br.com.pedro.etl.infra.pdf.AcessoPdf;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@Component
public class LeitorPDF {

    @Autowired
    private final AcessoPdf acessoPdf;

    public LeitorPDF(AcessoPdf acessoPdf) {
        this.acessoPdf = acessoPdf;
    }

    private final HashMap<Long, MunicipioPdfDTO> hashmapMunicipiosComPopuacao = new HashMap<>();

    public void buscarDadosDoPDF() {

        byte[] pdf = acessoPdf.acessaSiteDoPdf();

        try (PDDocument document = Loader.loadPDF(pdf)) {

            PDFTextStripper striper = new PDFTextStripper();
            striper.setSortByPosition(true);
            striper.setStartPage(2);
            striper.setEndPage(120);
            List<String> linhasDoPdf = List.of(striper.getText(document).split("\n"));

            for (String linha : linhasDoPdf) {

                String dadosNumericos = linha.replaceAll("\\D+", "");
                if (dadosNumericos.length() > 7) {
                    Long id = Long.parseLong(dadosNumericos.substring(0, 7));
                    Integer populacao = Integer.parseInt(dadosNumericos.substring(7));
                    MunicipioPdfDTO muncipio = MunicipioPdfDTO.builder()
                            .id(id)
                            .populacao(populacao).build();
                    hashmapMunicipiosComPopuacao.put(muncipio.getId(), muncipio);
                }
            }

        } catch (IOException e) {
            throw new PdfNaoAcessadoException("Erro! Não foi possível acessar o PDF carregado do site. Causa: ", e);
        }
    }

    public Integer buscarPopulacaoMunicipio(Long id) {
        return hashmapMunicipiosComPopuacao.get(id).getPopulacao();
    }
}