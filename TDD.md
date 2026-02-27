The document contains a **Parking Lot System design (LLD)** with domain classes such as `ParkingSpot`, `ParkingSpotType`, and logic for maintaining counters like `freeHandicappedSpotCount`, `freeMotorbikeSpotCount`, and `freeElectricSpotCount` .

This clearly indicates:

- Multiple **spot types** (HANDICAPPED, MOTORBIKE, ELECTRIC, etc.)
- Spot allocation & release logic
- Counter management
- Domain-driven object interactions (Vehicle ↔ ParkingSpot ↔ ParkingFloor ↔ ParkingLot)

Below is the complete **Spring Initializr configuration + TDD (Technical Design Document)** for implementing this as a production-ready REST API.

---

# ✅ SPRING INITIALIZR PROJECT DETAILS

Go to **[https://start.spring.io](https://start.spring.io)**

### Project Metadata

| Field        | Value                         |
| ------------ | ----------------------------- |
| Project      | Maven                         |
| Language     | Java                          |
| Spring Boot  | **3.5.11**                    |
| Group        | com.yuvraj                    |
| Artifact     | parking-lot                   |
| Name         | parking-lot                   |
| Description  | Parking Lot Management System |
| Package Name | com.yuvraj.parking            |
| Packaging    | Jar                           |
| Java         | **17**                        |

---

### Dependencies to Add

- Spring Web
- Spring Data JPA
- PostgreSQL Driver
- Validation
- Lombok
- Spring Boot DevTools
- Springdoc OpenAPI (Swagger UI)

After generation, manually ensure this dependency:

```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.5.0</version>
</dependency>
```

---

# 🧠 SYSTEM ANALYSIS (From the File)

The HTML file contains:

- `ParkingSpot`
- `ParkingSpotType`
- `Vehicle`
- Spot freeing logic
- Spot count tracking
- Display board updates
- Type-based allocation logic

This implies a **stateful parking management domain**.

---

# 📘 TECHNICAL DESIGN DOCUMENT (TDD)

---

# 1️⃣ SYSTEM OVERVIEW

A REST-based Parking Lot Management System that:

- Registers vehicles
- Allocates parking spots
- Frees spots
- Tracks availability by type
- Provides real-time availability
- Maintains parking history

---

# 2️⃣ ARCHITECTURE

Layered Architecture:

```
Controller → Service → Repository → PostgreSQL
```

Using:

- JPA/Hibernate
- PostgreSQL
- OpenAPI (Swagger)
- DTO pattern
- Exception handling
- Validation layer

---

# 3️⃣ DOMAIN MODEL

## 🔹 ParkingLot

- id
- name
- address

## 🔹 ParkingFloor

- id
- floorNumber
- parkingLot (ManyToOne)

## 🔹 ParkingSpot

- id
- spotNumber
- type (ENUM)
- isOccupied
- floor (ManyToOne)

## 🔹 Vehicle

- id
- licensePlate
- vehicleType (ENUM)
- entryTime
- exitTime
- parkingSpot (OneToOne)

## 🔹 ENUMS

```java
ParkingSpotType {
    HANDICAPPED,
    COMPACT,
    LARGE,
    MOTORBIKE,
    ELECTRIC
}
```

---

# 4️⃣ DATABASE DESIGN (PostgreSQL)

### Tables

### parking_lot

- id (PK)
- name
- address

### parking_floor

- id (PK)
- floor_number
- parking_lot_id (FK)

### parking_spot

- id (PK)
- spot_number
- type
- is_occupied
- floor_id (FK)

### vehicle

- id (PK)
- license_plate (unique)
- vehicle_type
- entry_time
- exit_time
- parking_spot_id (FK)

---

# 5️⃣ CORE BUSINESS RULES

### Allocation Rules

1. Vehicle can only park in compatible spot type
2. Spot must not be occupied
3. Update free spot counters
4. Record entry time

### Release Rules

1. Mark spot unoccupied
2. Update counter
3. Record exit time

The file clearly shows counter increment logic per type .

---

# 6️⃣ REST API DESIGN

## Parking APIs

### Create Parking Lot

```
POST /api/parking-lots
```

### Add Floor

```
POST /api/floors
```

### Add Spot

```
POST /api/spots
```

### Park Vehicle

```
POST /api/vehicles/park
```

### Exit Vehicle

```
POST /api/vehicles/exit/{licensePlate}
```

### Get Availability

```
GET /api/spots/available?type=ELECTRIC
```

---

# 7️⃣ SERVICE LAYER DESIGN

## ParkingService

```java
Vehicle parkVehicle(ParkRequest request);
void exitVehicle(String licensePlate);
Map<ParkingSpotType, Long> getAvailableSpots();
```

---

# 8️⃣ EXCEPTION DESIGN

- SpotNotAvailableException
- VehicleAlreadyParkedException
- VehicleNotFoundException
- InvalidSpotTypeException

Global handler via:

```java
@RestControllerAdvice
```

---

# 9️⃣ VALIDATION RULES

- License plate required
- Spot type required
- No duplicate vehicle entry
- Cannot exit non-existent vehicle

---

# 🔟 SWAGGER CONFIGURATION

After startup:

```
http://localhost:8080/swagger-ui.html
```

OpenAPI auto-generates from annotations.

Use:

```java
@Operation(summary = "Park a vehicle")
```

---

# 1️⃣1️⃣ TEST STRATEGY

### Unit Tests

- Service layer
- Allocation logic
- Counter increment/decrement
- Edge cases

### Integration Tests

- @SpringBootTest
- Testcontainers (PostgreSQL)

### Test Cases Examples

- Park vehicle successfully
- Park when no spot available
- Exit vehicle successfully
- Exit non-existing vehicle
- Get availability grouped by type

---

# 1️⃣2️⃣ APPLICATION.PROPERTIES

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/parkingdb
spring.datasource.username=postgres
spring.datasource.password=postgres

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

---

# 1️⃣3️⃣ PACKAGE STRUCTURE

```
com.yuvraj.parking
 ├── controller
 ├── service
 ├── repository
 ├── entity
 ├── dto
 ├── exception
 ├── config
```

---

# 1️⃣4️⃣ FUTURE EXTENSIONS

- Ticket pricing calculation
- Payment integration
- Multi-tenant parking lots
- Admin dashboard
- JWT authentication
- Redis caching for availability

---

# 🚀 FINAL OUTPUT SUMMARY

You will generate a:

- Spring Boot 3.5.11
- Maven project
- Java 17
- PostgreSQL-backed
- RESTful Parking Lot System
- Swagger-documented
- Production-structured layered backend
