package com.api.v1.bearerauth.config;

import com.api.v1.bearerauth.model.entity.AppUser;
import com.api.v1.bearerauth.model.entity.Product;
import com.api.v1.bearerauth.model.enumerate.Roles;
import com.api.v1.bearerauth.repository.AppUserRepository;
import com.api.v1.bearerauth.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {
    @Bean
    CommandLineRunner seedData(AppUserRepository users, ProductRepository products, PasswordEncoder encoder) {
        return args -> {
            if (users.findByEmail("admin@foodie.com").isEmpty()) {
                AppUser admin = new AppUser();
                admin.setUsername("Foodie Admin");
                admin.setEmail("admin@foodie.com");
                admin.setPassword(encoder.encode("Admin@12345"));
                admin.setRole(Roles.ADMIN);
                admin.setAddress("Phnom Penh");
                admin.setPhone("012345678");
                users.save(admin);
            }
            if (products.count() == 0) {
                add(products, "Classic Cheeseburger", "Flame Grilled Beef", 5.99, 4.8, "assets/images/burger.png", "Burger");
                add(products, "Bacon Whopper", "Crispy Bacon & Cheddar", 6.80, 4.9, "assets/images/burger2.png", "Burger");
                add(products, "Mushroom Swiss Burger", "Sautéed Mushrooms", 6.25, 4.6, "assets/images/burger3.png", "Burger");
                add(products, "Spicy Zinger Burger", "Crispy Fried Chicken", 5.50, 4.7, "assets/images/burger4.png", "Burger");
                add(products, "Double Smash Burger", "Caramelized Onions", 7.50, 4.9, "assets/images/burger5.png", "Burger");
                add(products, "Pepperoni Supreme", "Mozzarella & Oregano", 11.99, 4.9, "assets/images/pizza.png", "Pizza");
                add(products, "Margherita Delight", "Fresh Basil & Tomato", 9.50, 4.5, "assets/images/pizza2.jpg", "Pizza");
                add(products, "BBQ Chicken Pizza", "Smoky BBQ & Red Onion", 12.50, 4.8, "assets/images/pizza3.jpg", "Pizza");
                add(products, "Four Cheese Feast", "Parmesan, Cheddar, Mozz", 10.99, 4.7, "assets/images/pizza4.jpg", "Pizza");
                add(products, "Hawaiian Special", "Ham & Sweet Pineapple", 10.50, 4.3, "assets/images/pizza5.jpg", "Pizza");
                add(products, "Family Feast Combo", "2 Burgers, 2 Drinks, Fries", 18.99, 4.9, "assets/images/combo1.jpg", "Combos");
                add(products, "Solo Meal Deal", "1 Burger, Fries & Soda", 8.99, 4.7, "assets/images/combo2.jpg", "Combos");
                add(products, "Pizza & Wings Combo", "Medium Pizza + 6 Wings", 16.50, 4.8, "assets/images/combo3.jpg", "Combos");
                add(products, "Double Trouble Pack", "2 Smash Burgers & Shake", 14.20, 4.6, "assets/images/combo4.jpg", "Combos");
                add(products, "Iced Vanilla Latte", "Cold Brewed Coffee", 3.50, 4.6, "assets/images/drink1.jpg", "Drinks");
                add(products, "Fresh Mango Smoothie", "100% Real Fruit", 4.25, 4.8, "assets/images/drink2.jpg", "Drinks");
                add(products, "Chocolate Milkshake", "Rich Creamy Fudge", 4.50, 4.9, "assets/images/drink3.jpg", "Drinks");
                add(products, "Classic Lemonade", "Freshly Squeezed", 2.99, 4.4, "assets/images/drink4.jpg", "Drinks");
            }
        };
    }

    private void add(ProductRepository repository, String name, String description, double price, double rating, String image, String category) {
        Product p = new Product();
        p.setProductName(name);
        p.setDescription(description);
        p.setUnitPrice(price);
        p.setRating(rating);
        p.setImageUrl(image);
        p.setCategory(category);
        repository.save(p);
    }
}
