package io.pivotal.spring.cloud.config.java;

import io.pivotal.spring.cloud.service.gemfire.GemfireServiceConnectorConfig;
import org.apache.geode.cache.client.ClientCache;
import org.springframework.cloud.Cloud;
import org.springframework.cloud.CloudException;
import org.springframework.cloud.config.java.CloudServiceConnectionFactory;

public class PivotalCloudServiceConnectionFactory extends CloudServiceConnectionFactory implements PivotalServiceConnectionFactory {

	private CloudConnectorsConfig cloudConnectorsConfig;

	public PivotalCloudServiceConnectionFactory(CloudConnectorsConfig cloudConnectorsConfig, Cloud cloud) {
		super(cloud);
		this.cloudConnectorsConfig = cloudConnectorsConfig;
	}

	/**
	 * Get the GemFire {@link ClientCache} object associated with the only GemFire service bound to the app.
	 *
	 * @return GemFire client
	 * @throws CloudException if there are either 0 or more than 1 GemFire services.
	 */
	@Override
	public ClientCache gemfireClientCache() {
		return gemfireClientCache((GemfireServiceConnectorConfig) null);
	}

	/**
	 * Get the GemFire {@link ClientCache} object associated with the only GemFire service bound to the app.
	 *
	 * @return GemFire client
	 * @param config configuration for the created {@link ClientCache}
	 * @throws CloudException if there are either 0 or more than 1 GemFire services.
	 */
	@Override
	public ClientCache gemfireClientCache(GemfireServiceConnectorConfig config) {
		return cloudConnectorsConfig.cloud().getSingletonServiceConnector(ClientCache.class, config);
	}

	/**
	 * Get the GemFire {@link ClientCache} object associated with the specified GemFire service.
	 *
	 * @param serviceId the name of the service
	 * @return GemFire client
	 * @throws CloudException if the specified service doesn't exist
	 */
	@Override
	public ClientCache gemfireClientCache(String serviceId) {
		return gemfireClientCache(serviceId, null);
	}

	/**
	 * Get the GemFire {@link ClientCache} object associated with the specified GemFire service.
	 *
	 * @param serviceId the name of the service
	 * @param config configuration for the created {@link ClientCache}
	 * @return GemFire client
	 * @throws CloudException if the specified service doesn't exist
	 */
	@Override
	public ClientCache gemfireClientCache(String serviceId, GemfireServiceConnectorConfig config) {
		return cloudConnectorsConfig.cloud().getServiceConnector(serviceId, ClientCache.class, config);
	}

}
