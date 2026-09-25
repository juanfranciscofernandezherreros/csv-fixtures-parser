# AGENTS.md

Estas reglas son obligatorias para cualquier agente, asistente o automatización que modifique este repositorio.

## Confirmación obligatoria antes de empezar

Antes de realizar cualquier cambio, preguntar al usuario y esperar respuesta explícita sobre:
1. **Nombre de la rama**, proponiendo uno por defecto.
2. **Tipo SemVer**: `major`, `minor` o `patch`.

No modificar archivos, crear commits ni abrir PR hasta tener ambas respuestas.

## Flujo obligatorio

1. Partir de `main` actualizado.
2. Confirmar rama y SemVer.
3. Crear rama dedicada; nunca trabajar directamente sobre `main`.
4. Aplicar el incremento sobre `revision`.
5. Realizar el cambio y actualizar `CHANGELOG.md`.
6. Actualizar `README.md` cuando corresponda o lo exija el repositorio.
7. Ejecutar tests/checks.
8. Abrir PR hacia `main`.
9. No hacer merge salvo autorización explícita.
10. Con autorización de merge automático, fusionar solo con checks verdes.

## Maven CI-friendly

`<version>${revision}${sha1}${changelist}</version>`

- `revision`: SemVer funcional.
- `sha1`: `-<short-sha>` generado por CI; no persistirlo manualmente.
- `changelist`: vacío o `-SNAPSHOT`.
- DEV, INT y QA promocionan exactamente el mismo artefacto.

## SemVer

- patch: `X.Y.Z -> X.Y.(Z+1)`
- minor: `X.Y.Z -> X.(Y+1).0`
- major: `X.Y.Z -> (X+1).0.0`

El CHANGELOG usa `revision`, nunca el SHA de build.

## Tests

Baseline JDK 21. Ejecutar como mínimo `mvn -B test`.
