package br.com.pedro.etl.infra.pdf;

import br.com.pedro.etl.exception.PdfNaoAcessadoException;
import br.com.pedro.etl.utils.DefineAno;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Component
public class AcessoPdf {

    private final int ano = DefineAno.defineAnoDoPdf();
    private final URI uri = URI.create(String.format("https://ftp.ibge.gov.br/Estimativas_de_Populacao/Estimativas_%d/estimativa_dou_%d.pdf", ano, ano));

    public byte[] acessaSiteDoPdf() {

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();

        byte[] pdf;

        try {
            pdf = client.send(request, HttpResponse.BodyHandlers.ofByteArray()).body();
        } catch (IOException | InterruptedException e) {
            throw new PdfNaoAcessadoException("Erro! Não foi possível acessar o PDF no site do IBGE. Causa: ", e);
        }
        return pdf;
    }
}
