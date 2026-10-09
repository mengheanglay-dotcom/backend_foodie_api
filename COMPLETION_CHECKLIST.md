# Foodie Backend Completion Checklist

## Included
- Teacher-style package structure
- Spring Boot REST API
- PostgreSQL + JPA
- JWT access/refresh authentication
- BCrypt password encryption
- USER / ADMIN roles
- Product CRUD
- Product search/category
- Favorites
- Delivery address
- Promo FOODIE10
- Cart-to-order request model
- Server-side price calculation
- Delivery fee
- Order history
- Order update/delete while processing
- Admin order status update
- Customer management
- Swagger/OpenAPI
- CORS for Flutter/Web
- Seed admin account
- Initial Foodie sample products
- ERD
- DFD Level 0
- DFD Level 1
- Project structure guide

## Before running
1. Install PostgreSQL.
2. Create `foodie_db`.
3. Check username/password in `src/main/resources/application.yaml`.
4. Run `gradlew.bat clean classes`.
5. Run `gradlew.bat bootRun`.
6. Open Swagger.

## If you want to use only your own food data
Edit `DataInitializer.java` and remove the sample `add(...)` product lines, or keep them and replace them with your own products.
