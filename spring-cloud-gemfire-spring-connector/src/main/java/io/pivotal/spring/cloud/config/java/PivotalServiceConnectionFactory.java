package io.pivotal.spring.cloud.config.java;

import org.apache.geode.cache.client.ClientCache;
import org.springframework.cloud.config.java.ServiceConnectionFactory;

import io.pivotal.spring.cloud.service.gemfire.GemfireServiceConnectorConfig;

public interface PivotalServiceConnectionFactory extends ServiceConnectionFactory {

	ClientCache gemfireClientCache();

	ClientCache gemfireClientCache(GemfireServiceConnectorConfig config);

	ClientCache gemfireClientCache(String serviceId);

	ClientCache gemfireClientCache(String serviceId, GemfireServiceConnectorConfig config);

}
