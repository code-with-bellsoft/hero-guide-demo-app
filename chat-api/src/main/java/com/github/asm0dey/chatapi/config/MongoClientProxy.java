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

import java.util.List;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("NullableProblems")
public class MongoClientProxy implements MongoClient {
    volatile MongoClient delegate;

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
