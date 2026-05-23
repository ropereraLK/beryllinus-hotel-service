package com.beryllinus.hotel_service.config;

import org.springframework.context.ApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class DebugMongo {

//    @Autowired
//    private ApplicationContext context;
//
//    @PostConstruct
//    public void check() {
//        String[] beans = context.getBeanNamesForType(com.mongodb.client.MongoClient.class);
//        System.out.println("MongoClient beans:");
//        for (String bean : beans) {
//            System.out.println(bean + " -> " + context.getBean(bean));
//        }
//    }
}