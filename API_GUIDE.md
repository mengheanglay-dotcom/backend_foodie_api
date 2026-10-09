# Foodie API Guide

Base URL: `http://localhost:8080/api/v1`

Swagger: `http://localhost:8080/swagger-ui/index.html`

## Authentication

### Register
`POST /auth/register`

```json
{
  "username": "Mengheang",
  "address": "Phnom Penh",
  "phone": "012345678",
  "email": "user@example.com",
  "password": "12345678",
  "confirm": "12345678"
}
```

### Login
`POST /auth/login`

```json
{
  "email": "user@example.com",
  "password": "12345678"
}
```

Use the returned access token:

`Authorization: Bearer <access-token>`

## Products

### Public product list
`GET /products?page=1&size=20`

Optional:
- `search=Burger`
- `category=Pizza`

### Admin create product
`POST /products`

```json
{
  "name": "Classic Burger",
  "subtitle": "Flame Grilled Beef",
  "price": 5.99,
  "rating": 4.8,
  "imageUrl": "assets/images/burger.png",
  "category": "Burger"
}
```

Admin can also `PUT /products/{id}` and `DELETE /products/{id}`.

## Favorites

- `GET /favorites`
- `GET /favorites/ids`
- `POST /favorites/{productId}/toggle`

## Delivery Address

`GET /user/delivery-address`

`PUT /user/delivery-address`

```json
{
  "address": "Street 271",
  "city": "Phnom Penh",
  "phone": "012345678"
}
```

## Promo

`POST /promos/validate`

```json
{
  "code": "FOODIE10",
  "subtotal": 20.00
}
```

## Create Order

`POST /orders`

```json
{
  "orderType": "Delivery",
  "paymentMethod": "QR Payment",
  "deliveryAddress": "Street 271, Phnom Penh",
  "promoCode": "FOODIE10",
  "items": [
    {
      "productId": 1,
      "quantity": 2,
      "selectedSize": 1,
      "cheese": true,
      "bacon": false
    }
  ]
}
```

The backend calculates the final total.

## Orders

- `GET /orders`
- `GET /orders/{id}`
- `POST /orders`
- `PUT /orders/{id}` while status is `processing`
- `DELETE /orders/{id}` while status is `processing`

### Admin status
`PUT /admin/orders/{id}/status`

```json
{
  "status": "purchased"
}
```

Allowed status values:
- `processing`
- `purchased`
- `cancelled`

## Admin account

```text
Email: admin@foodie.com
Password: Admin@12345
```
