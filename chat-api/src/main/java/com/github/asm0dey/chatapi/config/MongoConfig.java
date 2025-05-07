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

import com.mongodb.ClientSessionOptions;
import com.mongodb.ReadConcern;
import com.mongodb.ReadPreference;
import com.mongodb.WriteConcern;
import com.mongodb.client.*;
import com.mongodb.connection.ClusterDescription;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.conversions.Bson;
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

import java.util.List;
import java.util.concurrent.TimeUnit;

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

    @SuppressWarnings("NullableProblems")
    static public class MongoClientProxy implements MongoClient {
        private volatile MongoClient delegate;

        public MongoClientProxy(MongoClient initialClient) {
            this.delegate = initialClient;
        }


        @Override
        public void close() {
            delegate.close();
        }

        @Override
        public ClusterDescription getClusterDescription() {
            return delegate.getClusterDescription();
        }

        @Override
        public CodecRegistry getCodecRegistry() {
            return delegate.getCodecRegistry();
        }

        @Override
        public ReadPreference getReadPreference() {
            return delegate.getReadPreference();
        }

        @Override
        public WriteConcern getWriteConcern() {
            return delegate.getWriteConcern();
        }

        @Override
        public ReadConcern getReadConcern() {
            return delegate.getReadConcern();
        }

        @Override
        public Long getTimeout(TimeUnit timeUnit) {
            return delegate.getTimeout(timeUnit);
        }

        @Override
        public MongoCluster withCodecRegistry(CodecRegistry codecRegistry) {
            return delegate.withCodecRegistry(codecRegistry);
        }

        @Override
        public MongoCluster withReadPreference(ReadPreference readPreference) {
            return delegate.withReadPreference(readPreference);
        }

        @Override
        public MongoCluster withWriteConcern(WriteConcern writeConcern) {
            return delegate.withWriteConcern(writeConcern);
        }

        @Override
        public MongoCluster withReadConcern(ReadConcern readConcern) {
            return delegate.withReadConcern(readConcern);
        }

        @Override
        public MongoCluster withTimeout(long l, TimeUnit timeUnit) {
            return delegate.withTimeout(l, timeUnit);
        }

        @Override
        public MongoDatabase getDatabase(String s) {
            return delegate.getDatabase(s);
        }

        @Override
        public ClientSession startSession() {
            return delegate.startSession();
        }

        @Override
        public ClientSession startSession(ClientSessionOptions clientSessionOptions) {
            return delegate.startSession(clientSessionOptions);
        }

        @Override
        public MongoIterable<String> listDatabaseNames() {
            return delegate.listDatabaseNames();
        }

        @Override
        public MongoIterable<String> listDatabaseNames(ClientSession clientSession) {
            return delegate.listDatabaseNames(clientSession);
        }

        @Override
        public ListDatabasesIterable<Document> listDatabases() {
            return delegate.listDatabases();
        }

        @Override
        public ListDatabasesIterable<Document> listDatabases(ClientSession clientSession) {
            return delegate.listDatabases(clientSession);
        }

        @Override
        public <TResult> ListDatabasesIterable<TResult> listDatabases(Class<TResult> aClass) {
            return delegate.listDatabases(aClass);
        }

        @Override
        public <TResult> ListDatabasesIterable<TResult> listDatabases(ClientSession clientSession, Class<TResult> aClass) {
            return delegate.listDatabases(clientSession, aClass);
        }

        @Override
        public ChangeStreamIterable<Document> watch() {
            return delegate.watch();
        }

        @Override
        public <TResult> ChangeStreamIterable<TResult> watch(Class<TResult> aClass) {
            return delegate.watch(aClass);
        }

        @Override
        public ChangeStreamIterable<Document> watch(List<? extends Bson> list) {
            return delegate.watch(list);
        }

        @Override
        public <TResult> ChangeStreamIterable<TResult> watch(List<? extends Bson> list, Class<TResult> aClass) {
            return delegate.watch(list, aClass);
        }

        @Override
        public ChangeStreamIterable<Document> watch(ClientSession clientSession) {
            return delegate.watch(clientSession);
        }

        @Override
        public <TResult> ChangeStreamIterable<TResult> watch(ClientSession clientSession, Class<TResult> aClass) {
            return delegate.watch(clientSession, aClass);
        }

        @Override
        public ChangeStreamIterable<Document> watch(ClientSession clientSession, List<? extends Bson> list) {
            return delegate.watch(clientSession, list);
        }

        @Override
        public <TResult> ChangeStreamIterable<TResult> watch(ClientSession clientSession, List<? extends Bson> list, Class<TResult> aClass) {
            return delegate.watch(clientSession, list, aClass);
        }
    }

}
