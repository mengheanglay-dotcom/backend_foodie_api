package com.api.v1.bearerauth.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@Order(1)
@RequiredArgsConstructor
public class ProductCategoryMigration implements CommandLineRunner {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... args) {
        if (!columnExists("product", "category")) {
            return;
        }

        jdbcTemplate.execute("ALTER TABLE product ADD COLUMN IF NOT EXISTS category_id BIGINT");
        jdbcTemplate.execute("""
                INSERT INTO category (name)
                SELECT DISTINCT TRIM(category)
                FROM product
                WHERE category IS NOT NULL AND TRIM(category) <> ''
                ON CONFLICT (name) DO NOTHING
                """);
        jdbcTemplate.execute("""
                UPDATE product p
                SET category_id = c.category_id
                FROM category c
                WHERE p.category_id IS NULL
                  AND LOWER(TRIM(p.category)) = LOWER(c.name)
                """);
        if (!constraintExists("product_category_fk")) {
            jdbcTemplate.execute("""
                    ALTER TABLE product
                    ADD CONSTRAINT product_category_fk
                    FOREIGN KEY (category_id) REFERENCES category(category_id)
                    """);
        }
        jdbcTemplate.execute("ALTER TABLE product ALTER COLUMN category_id SET NOT NULL");
    }

    private boolean columnExists(String table, String column) {
        Integer count = jdbcTemplate.queryForObject("""
                SELECT COUNT(*)
                FROM information_schema.columns
                WHERE table_schema = current_schema()
                  AND table_name = ?
                  AND column_name = ?
                """, Integer.class, table, column);
        return count != null && count > 0;
    }

    private boolean constraintExists(String constraint) {
        Integer count = jdbcTemplate.queryForObject("""
                SELECT COUNT(*)
                FROM information_schema.table_constraints
                WHERE table_schema = current_schema()
                  AND constraint_name = ?
                """, Integer.class, constraint);
        return count != null && count > 0;
    }
}
