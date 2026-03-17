# Municipalizando



[![online](https://img.shields.io/badge/API-Online-brightgreen)](https://municipalizando.onrender.com)

API para consulta de dados de municípios e estados brasileiros. Já está em produção e pode ser consumida via HTTP/JSON.

## Código-fonte

Este projeto é open source.

Repositório:
https://github.com/Pedro0090/municipalizando

## Descrição

A Municipalizando API fornece dados de estados e municípios brasileiros
por meio de uma interface HTTP/JSON simples.

Os dados são obtidos a partir de fontes públicas como o IBGE e enriquecidos
durante um processo de ETL que também coleta coordenadas geográficas via Geoapify.

O projeto foi desenvolvido utilizando Java e Spring Boot com o objetivo de
explorar arquitetura modular, pipelines de ETL e integração com APIs externas.

## Documentação da API

A documentação interativa da API está disponível em:

https://municipalizando.onrender.com/swagger-ui/index.html

## Funcionalidades

- Fornece dados de estados e municípios brasileiros
- Disponibiliza informações como população e coordenadas geográficas dos municípios
- Retorna dados em JSON
- Permite consulta de estados e municípios por diferentes critérios de busca
- Popula o banco via ETL com dados provenientes de APIs externas
- Expõe um modelo de dados simplificado para fácil consumo de informações


## Tecnologias utilizadas

- [![Java][Java-badge]][Java-url]
- [![Spring][Spring-badge]][Spring-url]
- [![Postgres][Postgres-badge]][Postgres-url]
- [![Flyway][Flyway-badge]][Flyway-url]
- [![Docker][Docker-badge]][Docker-url]

## Como executar
### Pré-requisitos

    Docker instalado
    

### 1 - Clonar repositório

    git clone https://github.com/Pedro0090/municipalizando
    cd municipalizando

### 2 - Iniciar a aplicação

    docker compose up

Após isso, a aplicação será iniciada em containers. Para preencher o banco de dados, execute o módulo ETL:
    
    docker compose run --rm etl

Esse comando executa o container do ETL e remove automaticamente após a conclusão.

> **Observação:** devido a limitações de requisições das APIs externas utilizadas, é necessário executar o módulo ETL duas vezes com intervalo de 24 horas para que toda a base de dados seja preenchida.

### 3 - Testar Endpoints

Após iniciar a API, acesse:

    http://localhost:8080

Os principais endpoints GET disponíveis são:

- `/municipios`
- `/estados`

Outros endpoints e filtros estão detalhados na [documentação da API](http://localhost:8080/swagger-ui/index.html).

Você também pode acessar a API em produção: 

https://municipalizando.onrender.com

## Estrutura do Projeto

    project-root/
    │
    ├─ api/                # Módulo responsável por expor os endpoints REST
    │
    ├─ config/             # Configurações compartilhadas das aplicações
    │
    ├─ core/               # Código compartilhado entre módulos
    │
    ├─ etl/                # Módulo responsável por consumir APIs externas e popular o banco
    │
    ├─ pom.xml             # Projeto Maven multi-módulo
    ├─ docker-compose.yml  # Define e orquestra os serviços da aplicação via containers
    └─ README.md

## Fontes de Dados e Atribuições

Esta API utiliza dados públicos provenientes das seguintes fontes:

- **IBGE (Instituto Brasileiro de Geografia e Estatística)**  
  https://www.ibge.gov.br

- **Geoapify** — serviço utilizado para geocodificação das coordenadas geográficas  
  https://www.geoapify.com

Os dados geográficos utilizados pelo Geoapify são baseados em:

© [OpenStreetMap](https://www.openstreetmap.org/copyright) contributors

As atribuições completas também podem ser consultadas no endpoint `/info` da API.

## Licença

Esse projeto está licenciado sob a [MIT License](./LICENSE)


[Java-badge]: https://img.shields.io/badge/Java-007396?style=for-the-badge&logo=openjdk&logoColor=white
[Java-url]: https://adoptium.net/

[Spring-badge]: https://img.shields.io/badge/Spring_Boot-DB33F?style=for-the-badge&logo=springboot&logoColor=white
[Spring-url]: https://spring.io/projects/spring-boot

[Postgres-badge]: https://img.shields.io/badge/PostgreSQL-316192?style=for-the-badge&logo=postgresql&logoColor=white
[Postgres-url]: https://www.postgresql.org/

[Flyway-badge]: https://img.shields.io/badge/Flyway-CC0200?style=for-the-badge&logo=flyway&logoColor=white
[Flyway-url]: https://flywaydb.org/

[Docker-badge]: https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white
[Docker-url]: https://www.docker.com/
