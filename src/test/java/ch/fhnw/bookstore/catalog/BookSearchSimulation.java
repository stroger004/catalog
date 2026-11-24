package ch.fhnw.bookstore.catalog;

import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import static io.gatling.javaapi.core.CoreDsl.atOnceUsers;
import static io.gatling.javaapi.core.CoreDsl.global;
import static io.gatling.javaapi.core.CoreDsl.scenario;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

/**
 * Lasttest mit Gatling: 100 Anfragen auf die Buchsuche.
 * <p>
 * Voraussetzung: Catalog läuft auf Port 8080.
 * Starten: Maven-Fenster → catalog → Plugins → gatling → gatling:test
 * (oder im Terminal: mvn gatling:test). Der Bericht liegt danach unter target/gatling.
 */
public class BookSearchSimulation extends Simulation {

    HttpProtocolBuilder httpProtocol = http
            .baseUrl("http://localhost:8080")
            .acceptHeader("application/json");

    ScenarioBuilder searchBooks = scenario("Buchsuche")
            .exec(http("GET /api/books?keywords=")
                    .get("/api/books?keywords=")
                    .check(status().is(200)));

    {
        // 100 User schicken gleichzeitig je eine Anfrage = 100 Anfragen
        setUp(searchBooks.injectOpen(atOnceUsers(100)))
                .protocols(httpProtocol)
                // Alle 100 Anfragen müssen erfolgreich sein (Status 200)
                .assertions(global().successfulRequests().percent().is(100.0));
    }
}
