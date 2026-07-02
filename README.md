# Gestión de Empresa Circense

Aplicación de escritorio desarrollada en Java 23 para la gestión integral de una empresa circense. Proyecto basado en Spring Boot y JavaFX, aplicando arquitectura MVC y múltiples sistemas de persistencia.

## Descripción

La aplicación permite gestionar artistas, coordinadores, espectáculos, números circenses, incidencias e historiales de operaciones mediante distintos sistemas de bases de datos especializados.

El proyecto integra persistencia relacional, orientada a objetos, documental y XML nativa, centralizando toda la lógica de negocio mediante Spring Boot.

## Arquitectura

La aplicación sigue una arquitectura MVC en capas:

Controller -> Service -> Repository

## Sistemas de Persistencia

### MySQL + Hibernate/JPA

Gestión principal de:

* Personas
* Artistas
* Coordinadores
* Espectáculos
* Números circenses
* Credenciales

### DB4O

Almacenamiento de:

* Historial de operaciones
* Auditorías
* Registros de modificaciones

### ObjectDB

Gestión de:

* Incidencias
* Resoluciones
* Transacciones orientadas a objetos

### eXistDB

Persistencia XML de:

* Informes completos de espectáculos
* Exportación estructurada XML

### MongoDB

Gestión documental de:

* Dossiers artísticos
* Trayectorias profesionales
* Evaluaciones y observaciones

## Funcionalidades

* Gestión completa de artistas y coordinadores
* Administración de espectáculos y números circenses
* Asignación de artistas a espectáculos
* Registro de incidencias
* Historial de operaciones con filtros
* Exportación de informes XML
* Gestión documental en MongoDB
* Sistema de autenticación y control de sesiones

## Perfiles de Usuario

### Administrador

* Acceso completo al sistema
* Gestión total de datos y configuraciones

### Coordinación

* Gestión de artistas
* Gestión de espectáculos
* Gestión de incidencias

### Artista

* Consulta de información personal
* Registro de incidencias

## Tecnologías Utilizadas

* Java 23
* Spring Boot 3.4
* JavaFX 23
* Maven
* Hibernate 6.6
* MySQL
* MongoDB
* DB4O
* ObjectDB
* eXistDB

## Configuración

Los parámetros de conexión se encuentran en:

src/main/resources/application.properties

## Requisitos Previos

Antes de ejecutar la aplicación es necesario tener en funcionamiento:

* MySQL
* MongoDB
* ObjectDB Server
* eXistDB Server
* JDK 23
* JavaFX 23


