package com.jugglinhats.hexagonal.storefront;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.data.r2dbc.AutoConfigureDataR2dbc;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.http.MediaType;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.StreamUtils;

@SpringBootTest
@AutoConfigureWebTestClient
@AutoConfigureDataR2dbc
class StorefrontApplicationTests {

    @Autowired
    WebTestClient testClient;

    @Autowired
    R2dbcEntityTemplate template;
    DatabaseClient dbClient;

    @Value("classpath:/schema.sql")
    Resource schemaSql;
    @Value("classpath:/test-data.sql")
    Resource testDataSql;

    @BeforeEach
    void setup() throws IOException {
        dbClient = template.getDatabaseClient();
        executeSqlLoadedFrom(schemaSql);
        executeSqlLoadedFrom(testDataSql);
    }

    void executeSqlLoadedFrom(Resource schemaSql) throws IOException {
        dbClient.sql(loadSqlFrom(schemaSql))
                .fetch()
                .rowsUpdated()
                .block(Duration.ofSeconds(1));
    }

    String loadSqlFrom(Resource sql) throws IOException {
        return StreamUtils.copyToString(sql.getInputStream(), StandardCharsets.UTF_8);
    }

    @Test
    void returnsListOfProductsMatchingATag() {
        //language=JSON
        var homeStudioProducts = """
                {
                    "tag_query": "home studio",
                    "products": [
                        {
                            "id": "2535d495-33e7-47d1-806f-e0617986d222",
                            "name": "Sterling by Music Man StingRay",
                            "description": "The Classic, Active, “SLO Special” Styled StingRay Bass."
                        },
                        {
                            "id": "6b8210a4-8f36-484e-800e-77dbab73ccba",
                            "name": "Universal Audio OX Reactive Amp Attenuator",
                            "description": "A More Flexible Way to Use Your Tube Amplifier"
                        },
                        {
                            "id": "f5073ebc-aebc-47ac-b4b2-095e3eb5bffe",
                            "name": "Peavey Classic 30 II 30-watt Combo Amp",
                            "description": "One Seriously Giggable Amplifier!"
                        },
                        {
                            "id": "48c1ccf1-ee72-4b90-82f1-e07390578c96",
                            "name": "Squier Classic Vibe Stratocaster",
                            "description": "The Stratocaster Never Goes Out of Style"
                        }
                    ]
                }
                """;

        testClient.get().uri("/products?tag={tag}", "home studio")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody().json(homeStudioProducts);
    }

    @Test
    void returnsProductDetails() {
        //language=JSON
        var kawaiPiano = """
                {
                  "id": "83085b91-5d7d-4301-9119-719f150e879a",
                  "name": "Kawai ES920 88-key Digital Piano",
                  "description": "Kawai Tone and Touch, Plus Unbeatable Value",
                  "dateAdded": "02/11/2021"
                }
                """;

        testClient.get().uri("/products/{id}", "83085b91-5d7d-4301-9119-719f150e879a")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody().json(kawaiPiano);
    }

    @Test
    void returnsInventoryAvailabilityAlongWithProductDetails() {
        //language=JSON
        var kawaiPiano = """
                {
                  "id": "83085b91-5d7d-4301-9119-719f150e879a",
                  "availability": "OUT_OF_STOCK"
                }
                """;

        testClient.get().uri("/products/{id}", "83085b91-5d7d-4301-9119-719f150e879a")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody().json(kawaiPiano);

        //language=JSON
        var peaveyAmp = """
                {
                  "id": "f5073ebc-aebc-47ac-b4b2-095e3eb5bffe",
                  "availability": "IN_STOCK"
                }
                """;

        testClient.get().uri("/products/{id}", "f5073ebc-aebc-47ac-b4b2-095e3eb5bffe")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody().json(peaveyAmp);
    }
}
