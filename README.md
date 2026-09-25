Current version: **2.0.2**

# csv-fixtures-parser

```text
file.ready.fixtures -> CSV parser -> fixtures.parsed
```

Consume eventos `FIXTURES`, lee CSV de 4 columnas (`match_id,event_time,home_team,away_team`) y obtiene `country` y `competition` del nombre `FIXTURES_<country>_<competition>.csv`.

Publica una fila Avro por fixture en `fixtures.parsed`. No contiene JPA, Flyway ni PostgreSQL.

El PR se fusiona automáticamente a `main` cuando pasan los checks y después se elimina la rama origen.
