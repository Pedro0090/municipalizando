package br.com.pedro;

import br.com.pedro.etl.EtlApplication;
import br.com.pedro.etl.config.TestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = EtlApplication.class)
@ActiveProfiles("test")
@Import(value = { TestConfig.class })
public class EtlApplicationTests {

    @Test
    void contextLoads() {

    }
}
