# Parking Lot Management System

[![Java](https://img.shields.io/badge/Java-17-blue?logo=openjdk&logoColor=white)](https://openjdk.org/projects/jdk/17/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.11-brightgreen?logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-17-blue?logo=postgresql&logoColor=white)](https://www.postgresql.org/)
[![Maven](https://img.shields.io/badge/Maven-3-C71A36?logo=apachemaven&logoColor=white)](https://maven.apache.org/)
[![Swagger](https://img.shields.io/badge/Swagger-OpenAPI%203-85EA2D?logo=swagger&logoColor=black)](https://swagger.io/)
[![Docker](https://img.shields.io/badge/Docker-Ready-2496ED?logo=docker&logoColor=white)](https://www.docker.com/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

A production-ready REST API for managing parking lots, floors, spots, and vehicle parking operations. Built with Spring Boot and PostgreSQL following a layered architecture pattern.

---

## Table of Contents

- [Parking Lot Management System](#parking-lot-management-system)
  - [Table of Contents](#table-of-contents)
  - [Features](#features)
  - [Tech Stack](#tech-stack)
  - [Architecture](#architecture)
  - [Getting Started](#getting-started)
    - [Prerequisites](#prerequisites)
    - [Database Setup](#database-setup)
    - [Configuration](#configuration)
    - [Build and Run](#build-and-run)
    - [Swagger UI](#swagger-ui)
  - [API Endpoints](#api-endpoints)
    - [Parking Lot](#parking-lot)
    - [Parking Floor](#parking-floor)
    - [Parking Spot](#parking-spot)
    - [Vehicle Parking](#vehicle-parking)
    - [Sample Requests](#sample-requests)
  - [Domain Model](#domain-model)
    - [Enums](#enums)
    - [Entity Relationships](#entity-relationships)
  - [Error Handling](#error-handling)
  - [Testing](#testing)
    - [Test Coverage](#test-coverage)
    - [Test Cases](#test-cases)
  - [Project Structure](#project-structure)
  - [License](#license)

---

## Features

- Create and manage parking lots with multiple floors
- Add parking spots of various types (Handicapped, Compact, Large, Motorbike, Electric)
- Park and exit vehicles with automatic spot allocation
- Real-time availability tracking grouped by spot type
- Input validation and meaningful error responses
- Interactive API documentation via Swagger UI

---

## Tech Stack

| Component     | Technology                  |
| ------------- | --------------------------- |
| Language      | Java 17                     |
| Framework     | Spring Boot 3.5.11          |
| Database      | PostgreSQL                  |
| ORM           | Spring Data JPA / Hibernate |
| Validation    | Jakarta Bean Validation     |
| Documentation | Springdoc OpenAPI (Swagger) |
| Build Tool    | Maven                       |
| Utilities     | Lombok                      |

---

## Architecture

```
Controller  -->  Service  -->  Repository  -->  PostgreSQL
    |                |
    v                v
   DTO          Entity / Domain
```

The project follows a standard layered architecture:

- **Controller** - REST endpoints, request validation, HTTP status mapping
- **Service** - Business logic, spot allocation, counter management
- **Repository** - Data access via Spring Data JPA
- **DTO** - Decouples API contracts from internal entities
- **Exception** - Centralized error handling with `@RestControllerAdvice`

---

## Getting Started

### Prerequisites

- Java 17+
- Maven 3.9+
- PostgreSQL 15+

### Database Setup

```sql
CREATE DATABASE parkingdb;
```

### Configuration

1. Copy the example environment file:

```bash
cp .env.example .env
```

2. Edit `.env` with your PostgreSQL credentials:

```env
DB_URL=jdbc:postgresql://localhost:5432/parkingdb
DB_USERNAME=postgres
DB_PASSWORD=your_password_here
```

The application reads these environment variables at startup. Default values are provided as fallbacks in `application.properties`.

### Build and Run

```bash
# Clone the repository
git clone https://github.com/your-username/parking-lot.git
cd parking-lot

# Build
./mvnw clean install

# Run
./mvnw spring-boot:run
```

The application starts at `http://localhost:8080`.

### Swagger UI

Open `http://localhost:8080/swagger-ui.html` to explore and test all APIs interactively.

---

## API Endpoints

### Parking Lot

| Method | Endpoint                 | Description           |
| ------ | ------------------------ | --------------------- |
| POST   | `/api/parking-lots`      | Create a parking lot  |
| GET    | `/api/parking-lots`      | List all parking lots |
| GET    | `/api/parking-lots/{id}` | Get lot by ID         |

### Parking Floor

| Method | Endpoint                          | Description                  |
| ------ | --------------------------------- | ---------------------------- |
| POST   | `/api/floors`                     | Add a floor to a parking lot |
| GET    | `/api/floors/{id}`                | Get floor by ID              |
| GET    | `/api/floors/parking-lot/{lotId}` | List floors by parking lot   |

### Parking Spot

| Method | Endpoint                      | Description             |
| ------ | ----------------------------- | ----------------------- |
| POST   | `/api/spots`                  | Add a spot to a floor   |
| GET    | `/api/spots/{id}`             | Get spot by ID          |
| GET    | `/api/spots/available?type=X` | Available spots by type |
| GET    | `/api/spots/floor/{floorId}`  | List spots on a floor   |

### Vehicle Parking

| Method | Endpoint                            | Description            |
| ------ | ----------------------------------- | ---------------------- |
| POST   | `/api/vehicles/park`                | Park a vehicle         |
| POST   | `/api/vehicles/exit/{licensePlate}` | Exit a vehicle         |
| GET    | `/api/vehicles/availability`        | Real-time availability |

### Sample Requests

**Create Parking Lot**

```json
POST /api/parking-lots
{
  "name": "City Mall Parking",
  "address": "100 Main Street"
}
```

**Add Floor**

```json
POST /api/floors
{
  "floorNumber": 1,
  "parkingLotId": 1
}
```

**Add Spot**

```json
POST /api/spots
{
  "spotNumber": "C-1",
  "type": "COMPACT",
  "floorId": 1
}
```

**Park Vehicle**

```json
POST /api/vehicles/park
{
  "licensePlate": "DL-01-AB-1234",
  "vehicleType": "CAR",
  "spotType": "COMPACT"
}
```

**Exit Vehicle**

```
POST /api/vehicles/exit/DL-01-AB-1234
```

**Check Availability**

```
GET /api/vehicles/availability
```

```json
{
  "availableSpots": {
    "HANDICAPPED": 1,
    "COMPACT": 2,
    "LARGE": 1,
    "MOTORBIKE": 1,
    "ELECTRIC": 1
  },
  "totalAvailable": 6
}
```

---

## Domain Model

### Enums

**ParkingSpotType**: `HANDICAPPED`, `COMPACT`, `LARGE`, `MOTORBIKE`, `ELECTRIC`

**VehicleType**: `CAR`, `TRUCK`, `VAN`, `MOTORBIKE`, `ELECTRIC`

### Entity Relationships

```
ParkingLot  1 --- *  ParkingFloor  1 --- *  ParkingSpot  1 --- 1  Vehicle
```

| Entity       | Key Fields                                                           |
| ------------ | -------------------------------------------------------------------- |
| ParkingLot   | id, name, address                                                    |
| ParkingFloor | id, floorNumber, parkingLot (FK)                                     |
| ParkingSpot  | id, spotNumber, type, isOccupied, floor (FK)                         |
| Vehicle      | id, licensePlate, vehicleType, entryTime, exitTime, parkingSpot (FK) |

---

## Error Handling

All errors return a consistent JSON structure:

```json
{
  "status": 409,
  "message": "Vehicle with license plate DL-01-AB-1234 is already parked",
  "timestamp": "2026-02-27T17:20:00"
}
```

| Exception                     | HTTP Status | Trigger                           |
| ----------------------------- | ----------- | --------------------------------- |
| SpotNotAvailableException     | 409         | No free spot of requested type    |
| VehicleAlreadyParkedException | 409         | Duplicate license plate parking   |
| VehicleNotFoundException      | 404         | Exit with unknown license plate   |
| ResourceNotFoundException     | 404         | Invalid lot, floor, or spot ID    |
| InvalidSpotTypeException      | 400         | Invalid spot type value           |
| Validation errors             | 400         | Missing or invalid request fields |

---

## Testing

The project includes unit tests and integration tests using H2 in-memory database.

```bash
# Run all tests
./mvnw test
```

### Test Coverage

| Test Class                       | Scope                                |
| -------------------------------- | ------------------------------------ |
| ParkingServiceTest               | Park, exit, availability, edge cases |
| ParkingLotServiceTest            | CRUD operations for parking lots     |
| VehicleControllerIntegrationTest | End-to-end API tests with MockMvc    |

### Test Cases

- Park vehicle successfully
- Park when vehicle is already parked (409)
- Park when no spot available (409)
- Exit vehicle successfully
- Exit non-existing vehicle (404)
- Availability count accuracy
- Counter decrement after parking
- Counter increment after exit
- Request validation (400)

---

## Project Structure

```
src/main/java/com/yuvraj/parking_lot/
|-- config/
|   |-- OpenApiConfig.java
|-- controller/
|   |-- ParkingLotController.java
|   |-- ParkingFloorController.java
|   |-- ParkingSpotController.java
|   |-- VehicleController.java
|-- dto/
|   |-- ParkingLotRequest.java
|   |-- ParkingLotResponse.java
|   |-- ParkingFloorRequest.java
|   |-- ParkingFloorResponse.java
|   |-- ParkingSpotRequest.java
|   |-- ParkingSpotResponse.java
|   |-- ParkRequest.java
|   |-- VehicleResponse.java
|   |-- AvailabilityResponse.java
|   |-- ErrorResponse.java
|-- entity/
|   |-- ParkingLot.java
|   |-- ParkingFloor.java
|   |-- ParkingSpot.java
|   |-- Vehicle.java
|   |-- enums/
|       |-- ParkingSpotType.java
|       |-- VehicleType.java
|-- exception/
|   |-- GlobalExceptionHandler.java
|   |-- SpotNotAvailableException.java
|   |-- VehicleAlreadyParkedException.java
|   |-- VehicleNotFoundException.java
|   |-- InvalidSpotTypeException.java
|   |-- ResourceNotFoundException.java
|-- repository/
|   |-- ParkingLotRepository.java
|   |-- ParkingFloorRepository.java
|   |-- ParkingSpotRepository.java
|   |-- VehicleRepository.java
|-- service/
|   |-- ParkingLotService.java
|   |-- ParkingFloorService.java
|   |-- ParkingSpotService.java
|   |-- ParkingService.java
|   |-- impl/
|       |-- ParkingLotServiceImpl.java
|       |-- ParkingFloorServiceImpl.java
|       |-- ParkingSpotServiceImpl.java
|       |-- ParkingServiceImpl.java
|-- ParkingLotApplication.java
```

---

## License

This project is open source and available under the [MIT License](LICENSE).
