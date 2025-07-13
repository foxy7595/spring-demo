// MongoDB initialization script
// This script runs when the MongoDB container starts for the first time

// Switch to the springdemo database
db = db.getSiblingDB('springdemo');

// Create collections
db.createCollection('users');
db.createCollection('calculations');
db.createCollection('audit_logs');

// Create indexes for better performance
db.users.createIndex({ "email": 1 }, { unique: true });
db.users.createIndex({ "username": 1 }, { unique: true });
db.calculations.createIndex({ "userId": 1 });
db.calculations.createIndex({ "createdAt": -1 });
db.audit_logs.createIndex({ "timestamp": -1 });
db.audit_logs.createIndex({ "userId": 1 });

// Insert sample data (optional)
db.users.insertOne({
    username: "admin",
    email: "admin@example.com",
    password: "$2a$10$rDmFN6ZqJ9K9ZqJ9K9ZqJ9K9ZqJ9K9ZqJ9K9ZqJ9K9ZqJ9K9ZqJ9K9ZqJ9", // This should be properly hashed
    role: "ADMIN",
    createdAt: new Date(),
    updatedAt: new Date()
});

print("MongoDB initialization completed successfully!"); 