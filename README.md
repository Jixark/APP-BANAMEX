# Banamex Cajero (Android)

App Android (Kotlin) que muestra a pantalla completa y en horizontal la aplicación web
<https://banamex.kidzania.tech/cajero/> (conteo de kidZos para Banamex en KidZania).

## Qué hace
- `WebView` a pantalla completa (modo inmersivo), orientación horizontal, pantalla siempre encendida.
- La web está diseñada para un escenario de 1920×1080 y se escala sola al tamaño del dispositivo.
- Solo HTTPS; los enlaces a otros dominios se abren en el navegador del sistema.
- Si no hay conexión, muestra una pantalla "Sin conexión" con botón **Reintentar** y recarga
  automáticamente al volver la red.
- Botón atrás: navega dentro de la web, nunca cierra la app por accidente.

## Abrir en Android Studio
1. `File > Open` y selecciona esta carpeta.
2. Espera la sincronización de Gradle y ejecuta el módulo `app` (▶).

## Compilar por línea de comandos
```bash
./gradlew assembleDebug
```
El APK queda en `app/build/outputs/apk/debug/app-debug.apk`.

## Cambiar la URL
Edita `CAJERO_URL` en `app/build.gradle.kts`.

## Datos del proyecto
- `applicationId`: `tech.kidzania.banamex.cajero`
- minSdk 26 · targetSdk/compileSdk 35 · Android Gradle Plugin 8.7.3 · Kotlin 2.0.21
