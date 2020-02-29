package com.usermanagement.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;

@Configuration
@EnableAutoConfiguration
@ComponentScan(value ="com.usermanagement")
@EnableMongoRepositories(basePackages="com.usermanagement.user.repository")
public class MongoAppConfig extends AbstractMongoConfiguration {

    private MongoConfigProperties mongoConfigProperties;

    @Autowired
    public MongoAppConfig(MongoConfigProperties mongoConfigProperties){
        this.mongoConfigProperties = mongoConfigProperties;
    }

    @Override
    public MongoClient mongoClient() {
        return new MongoClient(new ServerAddress(mongoConfigProperties.getHost(), mongoConfigProperties.getPort()));
    }

    @Override
    protected String getDatabaseName() {
        return mongoConfigProperties.getUserManagementDatabaseName();
    }
}
