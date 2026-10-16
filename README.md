# Foodie Backend API - Teacher Structure

Spring Boot REST API for the Flutter Foodie application.

## Architecture

rotha

controller -> service -> repository -> PostgreSQL

Security:

Flutter -> Bearer access token -> JwtAuthFilter -> Spring Security -> Controller

The project keeps the teacher's package style:

- common
- config
- controller
- model/entity
- model/request
- model/response
- repository
- security
- service
- service/impl
- util

## Database

Create PostgreSQL database:

```sql
CREATE DATABASE foodie_db;
```

Default development settings:

- host: localhost
- port: 5432
- database: foodie_db
- username: postgres
- password: 123

You can override them with DB_USERNAME and DB_PASSWORD.

## Run

```bash
./gradlew bootRun
```

Windows:

```bat
gradlew.bat bootRun
```

Swagger:

```text
http://localhost:8080/swagger-ui/index.html
```

## Default admin

Email: admin@foodie.com
Password: Admin@12345

Change this password for real deployment.

## Main API

### Auth - public

POST /api/v1/auth/register
POST /api/v1/auth/login
POST /api/v1/auth/refresh

### Products

GET /api/v1/products - public
GET /api/v1/products/{id} - public
POST /api/v1/products - ADMIN
PUT /api/v1/products/{id} - ADMIN
DELETE /api/v1/products/{id} - ADMIN

### User

GET /api/v1/user/current - authenticated
PUT /api/v1/user - authenticated
POST /api/v1/user/logout - authenticated

### Orders

GET /api/v1/orders - USER gets own orders; ADMIN gets all
GET /api/v1/orders/{id} - owner or ADMIN
POST /api/v1/orders - authenticated user
PUT /api/v1/orders/{id} - owner or ADMIN, pending only
DELETE /api/v1/orders/{id} - owner or ADMIN, pending only

### Admin orders

PUT /api/v1/admin/orders/{id}/status - ADMIN only

## Important Foodie rules

1. Registration always creates USER. The client cannot register itself as ADMIN.
2. Product price is read from the database when an order is created.
3. Size and extras are calculated by the backend:
   - large size: +$2.00
   - cheese: +$1.00
   - bacon: +$1.50
4. Delivery fee is calculated by the backend: $2.00 for DELIVERY, otherwise $0.
5. Users can only access their own orders.
6. Refresh tokens are rotated and old refresh tokens are invalidated.
