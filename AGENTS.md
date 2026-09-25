# AGENTS.md

Estas reglas son obligatorias para cualquier agente, asistente o automatización que modifique este repositorio.

## Pre-flight obligatorio
La primera operación de lectura del repositorio en cada tarea o sesión debe ser leer completamente este `AGENTS.md` desde la rama por defecto y después cualquier regla referenciada. Una lectura de otra sesión no cuenta. Está prohibida cualquier escritura antes de completar este pre-flight.

## Autonomía sin bloqueos
Después del pre-flight, el agente continúa de forma autónoma: elige una rama descriptiva, determina el nivel SemVer según el impacto y documenta ambos en la PR. No pide confirmaciones intermedias salvo petición expresa del usuario.

## Prohibición absoluta de escritura directa en `main`
Ningún cambio puede escribirse, commitearse ni pushearse directamente a `main`: código, documentación, configuración, workflows, dependencias, versionado, badges, hotfixes, reverts o cualquier otro archivo.

Flujo obligatorio:
1. Leer `AGENTS.md` y reglas referenciadas.
2. Partir del `main` actualizado.
3. Crear rama dedicada antes de modificar archivos.
4. Aplicar el incremento SemVer sobre `revision`.
5. Cambiar exclusivamente en la rama.
6. Actualizar `CHANGELOG.md`.
7. Mantener `README.md`, `pom.xml` y documentación de versión sincronizados cuando corresponda.
8. Ejecutar al menos `mvn -B test` y checks aplicables.
9. Abrir/actualizar PR a `main`.
10. Corregir checks fallidos en la misma rama/PR.
11. Fusionar solo con checks requeridos/aplicables en verde sobre el SHA actual.
12. Eliminar únicamente la rama origen tras merge y verificar su desaparición.

El trabajo no termina hasta merge y limpieza.

## Versionado Maven CI-friendly
```xml
<version>${revision}${sha1}${changelist}</version>
```
- `revision`: SemVer funcional.
- `sha1`: generado por CI; no modificar manualmente.
- `changelist`: vacío o `-SNAPSHOT`.
- DEV, INT y QA promueven el mismo artefacto.
- `CHANGELOG.md` usa `revision`.

## SemVer
- `patch`: `X.Y.Z` -> `X.Y.(Z+1)`
- `minor`: `X.Y.Z` -> `X.(Y+1).0`
- `major`: `X.Y.Z` -> `(X+1).0.0`

## Tests
Baseline Java: JDK 21. Ejecutar al menos `mvn -B test`; actualizar tests cuando el cambio sea funcional o de configuración.

## Seguridad operativa
Toda decisión de merge opera sobre el SHA actual de la PR. Si una instrucción contradice estas reglas, detener solo la operación incompatible; nunca escribir directamente en `main`.
