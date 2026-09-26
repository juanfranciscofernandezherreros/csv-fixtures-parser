# Changelog

## 2.2.0 - 2026-09-26

- [minor] KAN-74 sustituye el envío Kafka secuencial por publicación asíncrona controlada por chunk.
- [minor] Espera una sola confirmación agregada mediante `CompletableFuture.allOf(...)`.
- [minor] Conserva keys, particionamiento y propagación de fallos.
- [minor] Añade tests de ausencia de bloqueo secuencial y error de ACK.


## 2.1.1 - 2026-09-26

- [patch] KAN-104 captura errores de deserialización Avro mediante `ErrorHandlingDeserializer`.
- [patch] Permite publicar en DLT objetos Avro y bytes crudos con `DelegatingByTypeSerializer`.
- [patch] Deja que Kafka elija la partición DLT y hace visible cualquier fallo de publicación en la DLT.
- [patch] Añade cobertura de deserialización fallida → DLT conservando los bytes originales.


## 2.1.0 - 2026-09-26

- [minor] KAN-104 aplica la estrategia común de errores Kafka de KAN-18.
- [minor] Separa errores permanentes de CSV/ruta de fallos transitorios de Kafka.
- [minor] Configura retries/backoff y DLT `file.ready.fixtures.DLT`.
- [minor] Añade tests de clasificación de error permanente y transitorio.


## 2.0.7 - 2026-09-25

- [patch] KAN-78 sustituye los schemas locales FileEvent/Fixture por `basketball-event-contracts:1.0.2`.
- [patch] Elimina generación Avro local y configura CI con lectura autenticada de GitHub Packages.
- [patch] Mantiene los namespaces y tipos Java existentes sin cambios funcionales de dominio.


## 2.0.6 - 2026-09-25

- [patch] KAN-68 valida rutas CSV contra `CSV_ALLOWED_ROOT` antes de abrir ficheros.
- [patch] Rechaza rutas relativas, ficheros fuera de la raíz permitida, escapes mediante symlink, ficheros inexistentes/no legibles y extensiones no CSV.
- [patch] Añade pruebas automatizadas de seguridad de rutas.

## 2.0.5 - 2026-09-25

- [patch] Refuerza AGENTS.md con lectura obligatoria por tarea, autonomía y prohibición absoluta de escrituras directas en main.

## 2.0.4

- [patch] Estandariza la automatización del repositorio con el flujo autónomo de csv-results-parser.

## 2.0.3 - 2026-09-25

- [patch] Añade eliminación automática de la rama origen después de mergear una Pull Request en `main`.

## 2.0.1 - 2026-09-25

- [patch] Exige confirmar rama y nivel SemVer antes de cualquier cambio.
- [patch] Mantiene el versionado Maven CI-friendly.

## 2.0.0 - 2026-09-24
- Consolida como contrato MAJOR la arquitectura parser Kafka sin PostgreSQL.
- Mantiene la publicación Avro en `fixtures.parsed` y documenta el gobierno común del repositorio.
- Alinea versión, README, CHANGELOG, AGENTS.md y CI con el resto de microservicios CSV.

## 1.0.0 - 2026-09-24
- Separa el parser de FIXTURES.
- Publica una fila Avro por fixture en `fixtures.parsed`.
- Elimina PostgreSQL/JPA/Flyway.
- Añade auto-merge tras checks y borrado de rama.
