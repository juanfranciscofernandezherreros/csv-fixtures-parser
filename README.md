![version](https://img.shields.io/badge/version-2.1.1-blue)
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


## Contratos Avro compartidos

`FileEventKey`, `FileEventValue`, `FixtureKey` y `FixtureValue` se consumen desde:

```text
com.fernandez.basketball:basketball-event-contracts:1.0.2
```

Este repositorio ya no mantiene copias locales de esos schemas.


## Estrategia de errores Kafka

KAN-104 aplica la política común de KAN-18 al consumidor de `file.ready.fixtures`.

- errores de ruta, formato o contenido CSV: non-retryable;
- fallos transitorios de Kafka: retryable;
- intentos agotados: publicación del registro original en `file.ready.fixtures.DLT`;
- retries y backoff configurables con `KAFKA_RETRY_MAX_ATTEMPTS` y `KAFKA_RETRY_BACKOFF_MS`;
- DLT configurable con `KAFKA_FIXTURES_PARSER_DLT_TOPIC`.

Spring Kafka añade a la publicación DLT los headers de excepción y contexto del registro original.


### Deserialización y DLT

Los deserializadores Avro están envueltos con `ErrorHandlingDeserializer`. Un payload corrupto o incompatible entra así en el flujo normal de recuperación de Spring Kafka.

La DLT `file.ready.fixtures.DLT`:
- acepta tanto objetos Avro como `byte[]` originales;
- conserva los headers de diagnóstico;
- deja que Kafka seleccione una partición válida;
- propaga cualquier fallo al publicar en la DLT para evitar pérdida silenciosa.
