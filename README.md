# Banamex KidZania (Android)

Apps Android (Kotlin) que muestran a pantalla completa y en horizontal cada sección web de
Banamex en KidZania. Un mismo módulo genera **un APK por URL** mediante *product flavors*:

| Flavor | App | applicationId | URL | Cámara (QR) |
|---|---|---|---|---|
| `cajero` | Banamex Cajero | `tech.kidzania.banamex.cajero` | <https://banamex.kidzania.tech/cajero/> | no |
| `turnos` | Banamex Turnos | `tech.kidzania.banamex.turnos` | <https://banamex.kidzania.tech/turnos/> | no |
| `tarjeta` | Banamex Tarjeta | `tech.kidzania.banamex.tarjeta` | <https://banamex.kidzania.tech/tarjeta/> | sí |
| `bancadigital` | Banamex Banca Digital | `tech.kidzania.banamex.bancadigital` | <https://banamex.kidzania.tech/bancadigital/> | sí |

Como cada flavor tiene su propio `applicationId`, los cuatro pueden instalarse a la vez.

## Qué hace
- `WebView` a pantalla completa (modo inmersivo), orientación horizontal, pantalla siempre encendida.
- La web está diseñada para un escenario de 1920×1080 y se escala sola al tamaño del dispositivo.
- Solo HTTPS; los enlaces a otros dominios se abren en el navegador del sistema.
- Si no hay conexión, muestra una pantalla "Sin conexión" con botón **Reintentar** y recarga
  automáticamente al volver la red.
- Botón atrás: navega dentro de la web, nunca cierra la app por accidente.
- `tarjeta` y `bancadigital` piden el permiso de cámara (solo video) para escanear códigos QR; solo se
  concede a la web propia.

## Abrir en Android Studio
1. `File > Open` y selecciona esta carpeta.
2. Espera la sincronización de Gradle y ejecuta el módulo `app` (▶).

## Compilar por línea de comandos
```bash
./gradlew assembleDebug          # los cuatro APKs
./gradlew assembleTarjetaDebug   # solo uno (Cajero, Turnos, Tarjeta o BancaDigital)
```
Los APKs quedan en `app/build/outputs/apk/<flavor>/debug/app-<flavor>-debug.apk`.
En Android Studio, elige el flavor en *Build Variants*.

## Cambiar una URL o agregar una app
Edita (o agrega) el bloque correspondiente en `productFlavors` de `app/build.gradle.kts`
(`applicationId`, nombre, `SITE_URL`, `USES_CAMERA`) y, para el ícono, crea
`app/src/<flavor>/res/` con `values/colors.xml` (`launcher_background`) y
`drawable/ic_launcher_foreground.xml`.

## Datos del proyecto
- minSdk 26 · targetSdk/compileSdk 35 · Android Gradle Plugin 8.7.3 · Kotlin 2.0.21
