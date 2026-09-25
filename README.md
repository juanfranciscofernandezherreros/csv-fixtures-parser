![version](https://img.shields.io/badge/version-2.0.6-blue)
# csv-fixtures-parser

```text
file.ready.fixtures -> CSV parser -> fixtures.parsed
```

Consume eventos `FIXTURES`, lee CSV de 4 columnas (`match_id,event_time,home_team,away_team`) y obtiene `country` y `competition` del nombre `FIXTURES_<country>_<competition>.csv`.

Publica una fila Avro por fixture en `fixtures.parsed`. No contiene JPA, Flyway ni PostgreSQL.

El PR se fusiona automáticamente a `main` cuando pasan los checks y después se elimina la rama origen.


## Seguridad de rutas CSV

Antes de abrir un fichero recibido desde Kafka, el servicio valida que:

- la ruta sea absoluta;
- el fichero exista, sea regular y legible;
- la ruta real resuelta con `toRealPath()` permanezca dentro de `CSV_ALLOWED_ROOT`;
- un symlink no pueda escapar de la raíz permitida;
- el fichero tenga extensión `.csv`.

Configuración:

```text
CSV_ALLOWED_ROOT=/data/csv
```
