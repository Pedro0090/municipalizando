package br.com.pedro.api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Value("${api.title}")
    private String title;

    @Value("${api.version}")
    private String version;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI().info(
                new Info().title(title)
                        .version(version)
                        .description("""
                                A API Municipalizando disponibiliza dados básicos sobre estados e municípios do Brasil,
                                incluindo informações administrativas, coordenadas geográficas e população.
    
                                Os dados são obtidos a partir de fontes públicas e complementados durante o processo de ETL.
    
                                Fontes de dados:
                                - IBGE — dados administrativos e demográficos de estados e municípios (https://www.ibge.gov.br)
                                - Geoapify — geocodificação das coordenadas geográficas (https://www.geoapify.com/)
                                - OpenStreetMap — base de dados geográficos utilizada pelo Geoapify (https://www.openstreetmap.org/)
    
                                Para informações mais detalhadas, recomenda-se consultar diretamente as bases públicas do IBGE.
                                Para atribuições e detalhes sobre as fontes utilizadas pela API, consulte o endpoint GET /info.""")
                        .license(
                                new License()
                                        .name("MIT")
                                        .url("https://opensource.org/licenses/MIT"))
        ).tags(List.of(
                new Tag().name("Município").description("Operações referentes a municípios brasileiros"),
                new Tag().name("Estado").description("Operações referentes a estados brasileiros"))
        ).servers(List.of(
                        new Server().url("").description("Produção"),
                        new Server().url("http://localhost:8080").description("Ambiente Local")));
    }
}
