package io.pivotal.spring.cloud;

import java.util.Collections;

import io.pivotal.spring.cloud.service.common.GemfireServiceInfo;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.cloud.CloudConnector;
import org.springframework.cloud.CloudFactory;
import org.springframework.cloud.CloudTestUtil;
import org.springframework.cloud.service.ServiceInfo;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

abstract public class StubCloudConnectorTest {
	private static final String MOCK_CLOUD_BEAN_NAME = "mockCloud";

	protected ApplicationContext getTestApplicationContext(Class<?> configClass, ServiceInfo... serviceInfos) {
		final CloudConnector stubCloudConnector = CloudTestUtil.getTestCloudConnector(serviceInfos);

		return new AnnotationConfigApplicationContext(configClass) {
			@Override
			protected void prepareBeanFactory(ConfigurableListableBeanFactory beanFactory) {
				CloudFactory cloudFactory = new CloudFactory();
				cloudFactory.registerCloudConnector(stubCloudConnector);
				getBeanFactory().registerSingleton(MOCK_CLOUD_BEAN_NAME, cloudFactory);
				super.prepareBeanFactory(beanFactory);
			}
		};
	}

	protected GemfireServiceInfo createGemfireService(String id) {
		return new GemfireServiceInfo(id, Collections.singletonList("10.0.0.1[1044]"));
	}
}