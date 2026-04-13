#  Sistema de Gestión de Inventario - Microservicios

Este proyecto es una solución integral para la gestión de productos e inventarios, construida con una arquitectura de microservicios, un frontend moderno y despliegue automatizado mediante Docker.

##  Tecnologías Utilizadas

### **Backend (Microservicios)**
* **Java 17** & **Spring Boot 3.x**
* **Spring Data JPA**: Gestión de persistencia.
* **MySQL 8.0**: Base de datos relacional.
* **Maven**: Gestión de dependencias.
* **JUnit & Mockito**: Pruebas unitarias y de integracion.

### **Frontend**
* **Vue.js 3** (Composition API)
* **Vite**: Tooling de ultima generacion.
* **Pinia**: Gestion de estado global.
* **Vue Router**: Navegación SPA.
* **Axios**: Consumo de APIs REST.

### **Infraestructura**
* **Docker & Docker Compose**: Contenerizacion y orquestacion de servicios.

---

##  Arquitectura
El sistema se divide en tres componentes principales:
1. **Product Service**: Gestión del catálogo de productos (Puerto 8080).
2. **Inventory Service**: Control de stock y existencias (Puerto 8081).
3. **Frontend App**: Interfaz de usuario intuitiva (Puerto 5173).

---

##  Instalación y Despliegue

Para levantar todo el ecosistema (Base de datos + Backends + Frontend), solo necesitas tener instalado **Docker** y **Docker Compose**.

1. **Clonar el repositorio:**
   ```bash
   git clone [https://github.com/DavidGuz1399/Linktic.git](https://github.com/DavidGuz1399/Linktic.git)
   cd Linktic

2. **Levantar los servicios:**
    docker-compose up --build

3. **Acceder a la aplicacion**:
* **Frontend**: http://localhost:5173
* **API Products**: http://localhost:8080/api/products
* **API Inventory**: http://localhost:8081/api/inventory

## Pruebas

Los servicios cuentan con cobertura de pruebas unitarias. Para ejecutarlas localmente:

mvn test

## Notas de implementacion

* **Variables de Entorno**: Configuración flexible inyectada vía Docker para conexión a base de datos.
* **Persistencia**: Se utilizó un volumen de Docker para asegurar que los datos no se pierdan al apagar los contenedores..
* **Comunicacion**: El frontend está configurado para consumir los microservicios de forma asíncrona mediante Axios..