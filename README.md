# AYDS25-SongInfo

Aplicación Android desarrollada en Kotlin para consultar, visualizar y gestionar información de canciones.

## Descripción

**AYDS25-SongInfo** es un proyecto para la materia **Análisis y Diseño de Sistemas (AYDS)**, del primer cuatrimestre de 2025.  
La aplicación está orientada a obtener información de canciones desde servicios externos, mostrar contenido enriquecido sobre las mismas (incluyendo imágenes) y mantener datos en almacenamiento local para mejorar la experiencia de uso.

## Tecnologías utilizadas

- **Kotlin** (100%)
- **Android SDK** (minSdk 26, targetSdk 34, compileSdk 34)
- **Gradle Kotlin DSL**
- **Retrofit** (consumo de APIs REST)
- **Gson** (serialización/deserialización JSON)
- **Picasso** (carga y renderizado de imágenes)
- **Room** + **KSP** (persistencia local y procesamiento de anotaciones)
- **AndroidX** y **Material Components**

## Estructura del proyecto

El repositorio está organizado en módulos:

- `:app`  
  Módulo principal de la aplicación Android. Contiene la configuración de la app, UI y lógica de integración con red/base local.
  
- `:observer`  
  Módulo de librería Android para encapsular comportamiento reutilizable relacionado con notificaciones internas.

## Requisitos

- **Android Studio** (versión más reciente)
- **JDK 8** o superior (el proyecto compila con target JVM 1.8)
- SDK de Android con API 34 instalado
- Gradle

## Cómo ejecutar

1. Clonar el repositorio:
   ```bash
   git clone https://github.com/nicolastradivarius/AYDS25-SongInfo.git
   cd AYDS25-SongInfo
   ```

2. Abrir el proyecto en Android Studio.

3. Sincronizar dependencias de Gradle.

4. Ejecutar el módulo `app` en un emulador o teléfono Android.
