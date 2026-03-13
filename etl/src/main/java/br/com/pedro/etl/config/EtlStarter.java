package br.com.pedro.etl.config;

import br.com.pedro.etl.pdf.LeitorPDF;
import br.com.pedro.etl.service.EtlService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Profile("!test")
public class EtlStarter {

    private final EtlService etlService;
    private final ApplicationContext context;
    private final LeitorPDF leitorPDF;

    @Autowired
    public EtlStarter(EtlService etlService, ApplicationContext context, LeitorPDF leitorPDF) {
        this.etlService = etlService;
        this.context = context;
        this.leitorPDF = leitorPDF;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void executarEtl() {
        log.info("Buscando dados do PDF...");
        leitorPDF.buscarDadosDoPDF();
        log.info("Dados buscados!");

        log.info("Executando aplicacao...");
        etlService.executar();
        log.info("Aplicacao finalizada!");
        SpringApplication.exit(context, () -> 0);
    }
}
