package com.learning.ai.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;

@Configuration
public class MongoConfig extends AbstractMongoClientConfiguration {



    @Value("${database.name}")
    private String databaseName;

    @Override
    protected String getDatabaseName() {
        return this.databaseName;
    }

    @Override
    @Bean
    public MongoClient mongoClient() {
        String username = System.getenv("MONGO_USERNAME");
        String password = System.getenv("MONGO_PASSWORD");
        String host = System.getenv("MONGO_HOST");

        if (StringUtils.isAnyBlank(username, password, host)) {
            System.out.println("MongoDB connection details are not set in environment variables.");
            throw new IllegalStateException("MongoDB connection details are not set.");
        }

        String connectionString = String.format(
                "mongodb+srv://%s:%s@%s/%s?retryWrites=true&w=majority",
                username, password, host, this.databaseName
        );
        System.out.println("Connecting to MongoDB: " + connectionString);
        return MongoClients.create(connectionString);
    }
}
