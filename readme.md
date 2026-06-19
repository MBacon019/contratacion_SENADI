# 📋 Sistema de Seguimiento de Contratación Pública — SENADI

Aplicativo web desarrollado como parte del programa de pasantías en el **Servicio Nacional de Derechos Intelectuales (SENADI)**, destinado al seguimiento y gestión de procesos de contratación pública institucional, en cumplimiento de la normativa del Sistema Nacional de Contratación Pública del Ecuador (SERCOP).

---

## 🎯 Descripción

El sistema permite a los funcionarios de la institución registrar, configurar y ejecutar procesos de contratación pública de forma organizada y trazable. Cada tipo de contrato cuenta con su propio flujo de pasos y subpasos que el usuario sigue secuencialmente, con validaciones, contadores de días laborables y notificaciones integradas.

---

## 🏗️ Tecnologías Utilizadas

| Capa | Tecnología |
|------|-----------|
| Backend | Java 11 · Spring Boot 2.7.18 |
| Persistencia | Spring Data JPA · Hibernate |
| Base de datos | PostgreSQL 14+ |
| Frontend | HTML5 · CSS3 · JavaScript (Vanilla) |
| Seguridad | jBCrypt |
| Datos CSV | OpenCSV 5.7.1 |
| Build | Apache Maven |

---

## 🗂️ Estructura del Proyecto

```
sistema-contratacion/
├── src/
│   └── main/
│       ├── java/com/example/sistema_contratacion/
│       │   ├── config/
│       │   │   ├── DataInitializer.java
│       │   │   └── DataLoader.java
│       │   ├── controller/
│       │   │   ├── AuthController.java
│       │   │   ├── UsuarioController.java
│       │   │   ├── TipoContratoController.java
│       │   │   ├── CpcController.java
│       │   │   ├── ItemController.java
│       │   │   └── ConfiguracionUmbralController.java
│       │   ├── entity/
│       │   │   ├── Usuario.java
│       │   │   ├── Role.java
│       │   │   ├── TipoContrato.java
│       │   │   ├── PasoContrato.java
│       │   │   ├── Cpc.java
│       │   │   ├── Item.java
│       │   │   └── ConfiguracionUmbral.java
│       │   ├── repository/
│       │   └── service/
│       └── resources/
│           └── static/
│               ├── index.html
│               ├── dashboard.html
│               ├── DashboardUser.html
│               ├── ejecutar-proceso.html
│               ├── detalles-proceso.html
│               ├── configurar-flujo.html
│               └── admin-cpc.html
└── pom.xml
```

---

## 📦 Módulos del Sistema

### 🔐 Autenticación y Gestión de Usuarios
El sistema cuenta con registro e inicio de sesión de usuarios, con contraseñas cifradas mediante BCrypt. Se gestionan dos roles: Administrador y Usuario, cada uno con acceso a vistas y funcionalidades diferenciadas.

### 🔄 Flujos de Contratación
Se implementaron cuatro tipos de contrato conforme a la normativa SERCOP:

- **Ínfima Cuantía**
- **Catálogo Electrónico**
- **Subasta Inversa Electrónica**
- **Régimen Especial**

Cada tipo de contrato tiene un flujo de pasos y subpasos configurables por el administrador, organizados por fases (Preparatoria, Precontractual, Contractual).

### 🗃️ Datos Maestros
Al iniciar el sistema se cargan automáticamente el catálogo CPC con umbrales VAE y el clasificador presupuestario institucional, disponibles para su consulta y uso dentro de los procesos.

### 🖥️ Panel de Administración
El administrador puede configurar los flujos de cada tipo de contrato, gestionar usuarios y monitorear los procesos activos en la institución.

---

## ⚙️ Requisitos

- Java 11 (JDK)
- Apache Maven 3.6 o superior
- PostgreSQL 14 o superior

---

## 🚀 Instalación y Ejecución

**1. Clonar el repositorio**
```bash
git clone <url-del-repositorio>
cd sistema-contratacion
```

**2. Crear la base de datos**
```sql
CREATE DATABASE contrataciones_db;
```

**3. Configurar la conexión**

Crear el archivo `src/main/resources/application.properties` a partir de la plantilla `application.properties.example` e ingresar las credenciales correspondientes.

**4. Ejecutar la aplicación**
```bash
# Linux / Mac
./mvnw spring-boot:run

# Windows
mvnw.cmd spring-boot:run
```

La aplicación estará disponible en: **http://localhost:8081**

---

## 👥 Equipo de Desarrollo

Proyecto desarrollado de forma colaborativa en el marco del programa de pasantías institucionales en SENADI.

| Desarrollador | Rama de trabajo |
|--------------|----------------|
| Marcelo Bacon | `programador-BaconJ` |
| Joel Pachar | `programador-Pachar` |
| Alexander Lopez | `programador-Lopez` |
| Integración general | `DEV-MEJORAS` |

---

## 🏛️ Institución

**SENADI** — Servicio Nacional de Derechos Intelectuales  
República del Ecuador  
Pasantías Institucionales · 2026
