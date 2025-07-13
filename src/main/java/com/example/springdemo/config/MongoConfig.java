package com.example.springdemo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.stereotype.Component;

@Component
public class MongoConfig implements CommandLineRunner {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void run(String... args) throws Exception {
        // Create indexes manually to avoid conflicts
        try {
            // Create unique index for username
            mongoTemplate.indexOps("users")
                .ensureIndex(new Index().on("username", org.springframework.data.domain.Sort.Direction.ASC).unique());

            // Create unique index for email
            mongoTemplate.indexOps("users")
                .ensureIndex(new Index().on("email", org.springframework.data.domain.Sort.Direction.ASC).unique());

            System.out.println("MongoDB indexes created successfully");
        } catch (Exception e) {
            System.out.println("Index creation skipped (may already exist): " + e.getMessage());
        }
    }
} 