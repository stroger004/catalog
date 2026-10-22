# catalog

Spring-Boot-Service für den **Catalog** Bounded Context des Online-Bookstores (Sprint 1 – Buchsuche).

## Starten

```bash
mvn spring-boot:run
```

- Suche: `GET http://localhost:8080/books?keywords=tolkien ring`
- Alle Bücher: `GET http://localhost:8080/books`
- H2-Konsole: `http://localhost:8080/h2-console` (JDBC-URL `jdbc:h2:mem:catalog`, User `sa`, kein Passwort)

## Tests

```bash
mvn test
```

Die Teststrategie steht in [docs/Teststrategie-Buchsuche.md](docs/Teststrategie-Buchsuche.md).

## Aufbau

| Klasse | Aufgabe |
|---|---|
| `Book` | JPA-Entity mit ISBN, Titel, Beschreibung, Autor |
| `BookRepository` | Suchmethode `search(String)` – alle Schlüsselwörter müssen in Titel, Beschreibung oder Autor vorkommen, case-insensitive |
| `BookRestController` | `GET /books?keywords=...` liefert die Treffer als JSON |
| `data.sql` | 10 Beispielbücher |
