package io.pivotal.spring.cloud.config.java;

import io.pivotal.spring.cloud.service.gemfire.GemfireServiceConnectorConfig;
import org.apache.geode.cache.client.ClientCache;
import org.springframework.cloud.config.java.ServiceConnectionFactory;

public interface PivotalServiceConnectionFactory extends ServiceConnectionFactory {

	ClientCache gemfireClientCache();

	ClientCache gemfireClientCache(GemfireServiceConnectorConfig config);

	ClientCache gemfireClientCache(String serviceId);

	ClientCache gemfireClientCache(String serviceId, GemfireServiceConnectorConfig config);

}
