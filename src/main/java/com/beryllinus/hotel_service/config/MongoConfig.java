package com.beryllinus.hotel_service.config;

//import com.mongodb.ConnectionString;
//import com.mongodb.MongoClientSettings;
//import com.mongodb.client.MongoClient;
//import com.mongodb.client.MongoClients;
//import org.bson.UuidRepresentation;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.mongodb.MongoDatabaseFactory;
//import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;


public class MongoConfig {

//    @Bean
//    public MongoClient mongoClient(
//            @Value("${spring.data.mongodb.uri}") String uri
//    ) {
//        return MongoClients.create(
//                MongoClientSettings.builder()
//                        .applyConnectionString(new ConnectionString(uri))
//                        .uuidRepresentation(UuidRepresentation.STANDARD)
//                        .build()
//        );
//    }
//
//    @Bean
//    public MongoDatabaseFactory mongoDbFactory(MongoClient mongoClient) {
//        return new SimpleMongoClientDatabaseFactory(
//                mongoClient,
//                "hotel-service"
//        );
//    }
}