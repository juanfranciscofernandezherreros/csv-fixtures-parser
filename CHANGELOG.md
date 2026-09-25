# Changelog

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
