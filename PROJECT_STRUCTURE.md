# Foodie Backend API — Project Structure Guide

This backend follows the teacher's Bearer Authentication Spring Boot structure and adapts it to the Foodie mobile ordering application.

## 1. Main structure

```text
src/main/java/com/api/v1/bearerauth/
├── common/
│   ├── constant/       -> common messages/constants
│   ├── exception/      -> custom exceptions + global error handling
│   └── response/       -> standard API response wrappers
├── config/             -> Spring configuration, security, database seed, Swagger
├── controller/         -> REST API endpoints
├── model/
│   ├── entity/         -> JPA entities / database tables
│   ├── enumerate/      -> enums such as role, order status and payment method
│   ├── request/        -> JSON objects received from Flutter/API clients
│   └── response/       -> JSON objects returned to Flutter/API clients
├── repository/         -> database access using Spring Data JPA
├── security/           -> JWT authentication and current-user handling
├── service/            -> business logic interfaces
│   └── impl/           -> business logic implementations
└── util/               -> small reusable API helper functions
```

## 2. What each layer handles

### controller/
Controller is the API entrance. It receives HTTP requests, validates input, calls a service, and returns the API response.

Examples:
- `AuthController` -> register, login, refresh token
- `ProductController` -> food list/detail and admin food CRUD
- `OrderController` -> customer order operations
- `FavoriteController` -> favorite food operations
- `DeliveryAddressController` -> saved delivery address
- `PromoController` -> promo validation
- `AdminOrderController` -> admin changes order status

### service/
Service contains the business rules. Controllers should not contain calculations or database logic.

Examples:
- `OrderService` calculates order totals and handles order rules.
- `ProductService` manages food products.
- `FavoriteService` handles favorite logic.
- `AuthService` handles login/refresh flow.

### repository/
Repository talks to PostgreSQL through JPA.

Examples:
- `ProductRepository` -> product table
- `OrderTbRepository` -> order table
- `ProductOrderRepository` -> order-item table
- `FavoriteRepository` -> favorite table
- `AppUserRepository` -> user table

### model/entity/
These classes represent database tables.

- `AppUser` -> application account
- `Customer` -> customer profile
- `Product` -> Foodie menu item
- `Favorite` -> saved favorite food
- `OrderTb` -> order header
- `ProductOrder` -> food item inside an order
- `AppUserToken` -> refresh token

### model/request/
These are request bodies sent to the API.

For example, creating an order sends `OrderRequest`, which contains `OrderItemRequest` objects.

### model/response/
These are clean API responses. They hide database details and return fields that the Flutter application needs.

### security/
Handles Bearer JWT authentication.

Flow:

```text
Login
  ↓
Access Token + Refresh Token
  ↓
Flutter sends Authorization: Bearer <access-token>
  ↓
JwtAuthFilter
  ↓
Current User
  ↓
Controller / Service
```

### config/
Contains application setup.

- `SecurityConfig` -> Spring Security and CORS
- `PasswordEncoderConfig` -> BCrypt password encryption
- `DataInitializer` -> default admin and initial Foodie products
- `OpenApiConfig` -> Swagger/OpenAPI
- `MapperConfig` -> ModelMapper
- `AppProperty` -> JWT settings

## 3. Foodie database flow

```text
APP_USER
   │ 1:1
   ▼
CUSTOMER
   │ 1:M
   ▼
ORDER_TB
   │ 1:M
   ▼
PRODUCT_ORDER
   ▲
   │ M:1
PRODUCT
```

Favorites are connected separately:

```text
APP_USER 1:M FAVORITE M:1 PRODUCT
```

## 4. API groups

| Group | Main endpoints | Purpose |
|---|---|---|
| Auth | `/api/v1/auth/*` | Register/login/refresh |
| User | `/api/v1/user/*` | Current profile/logout |
| Products | `/api/v1/products/*` | Browse/search/manage food |
| Favorites | `/api/v1/favorites/*` | Favorite food |
| Orders | `/api/v1/orders/*` | Create/view/update orders |
| Address | `/api/v1/user/delivery-address` | Delivery address |
| Promo | `/api/v1/promos/validate` | Validate FOODIE10 |
| Admin Orders | `/api/v1/admin/orders/*` | Change order status |
| Customers | `/api/v1/customers/*` | Admin customer management |

## 5. What you need to add/change yourself

You do not need to change the architecture.

### Food data
You can manage food using admin product endpoints:
- POST product
- PUT product
- DELETE product
- GET product

Fields:
- name
- subtitle
- price
- rating
- imageUrl
- category

### Database settings
Edit only `src/main/resources/application.yaml` if your PostgreSQL username/password is different.

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/foodie_db
    username: postgres
    password: 123
```

## 6. Default admin

```text
Email:    admin@foodie.com
Password: Admin@12345
Role:     ADMIN
```

Change the password before using the project outside a classroom environment.

## 7. Database

Database name:

```text
foodie_db
```

Hibernate uses:

```text
ddl-auto: update
```

So the tables are created/updated automatically when the backend starts.

## 8. Run the project

```cmd
gradlew.bat clean classes
gradlew.bat bootRun
```

Swagger:

```text
http://localhost:8080/swagger-ui/index.html
```

## 9. Recommended testing order

1. Create PostgreSQL database `foodie_db`.
2. Start Spring Boot.
3. Register a normal user.
4. Login and copy the access token.
5. Test GET products.
6. Test favorites.
7. Test delivery address.
8. Test promo `FOODIE10`.
9. Create an order.
10. Login as admin.
11. Change order status.
12. Test product CRUD as admin.

## 10. Important business rules

- Normal registration always creates `USER`.
- Admin role cannot be selected during public registration.
- Passwords are stored encrypted with BCrypt.
- Protected endpoints require a Bearer access token.
- Backend calculates food extras and delivery fee.
- Large size adds `$2.00`.
- Cheese adds `$1.00`.
- Bacon adds `$1.50`.
- Delivery adds `$2.00`.
- `FOODIE10` gives 10% off the food subtotal.
- Users can only read/update/delete their own orders.
- Admin can view all orders and change their status.
- Product management is admin-only.
