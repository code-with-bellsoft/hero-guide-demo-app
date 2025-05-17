/*
 * Copyright © 2025 BellSoft (info@bell-sw.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.asm0dey.chatapi.config;

import com.mongodb.client.*;
import org.crac.Context;
import org.crac.Core;
import org.crac.Resource;
import org.springframework.boot.autoconfigure.mongo.MongoConnectionDetails;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.event.ValidatingMongoEventListener;
import org.springframework.stereotype.Component;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

/**
 * MongoDB configuration for the Chat API.
 * Enables MongoDB auditing and validation.
 */
@Configuration
@EnableMongoAuditing
public class MongoConfig {

    /**
     * Configures MongoDB validation using Bean Validation API.
     * This allows using annotations like @NotNull, @Size, etc. on model classes.
     */
    @Bean
    public ValidatingMongoEventListener validatingMongoEventListener(
            LocalValidatorFactoryBean factory) {
        return new ValidatingMongoEventListener(factory);
    }

    @Bean
    public LocalValidatorFactoryBean validator() {
        return new LocalValidatorFactoryBean();
    }



    @Bean
    @Primary
    public MongoClient mongoClient(MongoConnectionDetails details) {
        MongoClient initialClient = MongoClients.create(details.getConnectionString());
        return new MongoClientProxy(initialClient);
    }

    @Bean
    public MongoTemplate mongoTemplate(MongoClient client) {
        return new MongoTemplate(client, "chat");
    }

    @Component
    static public class MongoClientResource implements Resource {

        private final MongoClientProxy mongoClientProxy;
        private final MongoConnectionDetails details;

        public MongoClientResource(MongoClient mongoClientProxy, MongoConnectionDetails details) {
            this.mongoClientProxy = (MongoClientProxy) mongoClientProxy;
            this.details = details;
            Core.getGlobalContext().register(this);
        }

        @Override
        public void beforeCheckpoint(Context<? extends Resource> context) {
            mongoClientProxy.delegate.close();
        }

        @Override
        public void afterRestore(Context<? extends Resource> context) {
            mongoClientProxy.delegate = MongoClients.create(details.getConnectionString());
        }
    }

}
