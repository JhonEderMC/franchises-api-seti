package co.com.bancolombia.driven.adapter.mongo.config;

import com.mongodb.reactivestreams.client.MongoClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class MongoConfigTest {

    private MongoConfig mongoConfig;

    @BeforeEach
    void setUp() {
        mongoConfig = new MongoConfig();
        ReflectionTestUtils.setField(mongoConfig, "database", "testdb");
        ReflectionTestUtils.setField(mongoConfig, "username", "user");
        ReflectionTestUtils.setField(mongoConfig, "password", "pass");
        ReflectionTestUtils.setField(mongoConfig, "clusterUrl", "cluster.mongodb.net");
    }

    @Test
    void shouldReturnDatabaseName() {
        assertEquals("testdb", mongoConfig.getDatabaseName());
    }

    @Test
    void shouldCreateReactiveMongoClient() {
        MongoClient client = mongoConfig.reactiveMongoClient();
        assertNotNull(client);
    }

}