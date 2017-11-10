package io.pivotal.spring.cloud.cloudfoundry;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.pivotal.spring.cloud.service.common.GemfireServiceInfo;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.cloud.cloudfoundry.AbstractCloudFoundryConnectorTest;

import java.util.List;
import java.util.Map;

public class GemfireServiceInfoCreatorTest extends AbstractCloudFoundryConnectorTest {

	private ObjectMapper mapper = new ObjectMapper();

	@Test
	public void testInfoCreator() throws Exception {
		GemfireServiceInfoCreator creator = new GemfireServiceInfoCreator();
		Map services = readServiceData("test-gemfire-service.json");
		Map<String, Object> serviceData = getServiceData(services, "p-gemfire");

		GemfireServiceInfo info = creator.createServiceInfo(serviceData);
		Assert.assertNotNull(info);
		Assert.assertNotNull(info.getLocators());
		Assert.assertEquals("some-cluster-operator", info.getUsername());
		Assert.assertEquals("3q1XFDobsp8S2wJZ8ajTQ", info.getPassword());
		Assert.assertNull(info.getRestURL());
	}

	@Test
	public void itUsesUsernamesAsRoles_whenServiceInfoHasNoRoles() throws Exception {
		GemfireServiceInfoCreator creator = new GemfireServiceInfoCreator();
		Map services = readServiceData("test-gemfire-service-no-roles.json");
		Map<String, Object> serviceData = getServiceData(services, "p-gemfire");

		GemfireServiceInfo info = creator.createServiceInfo(serviceData);
		Assert.assertNotNull(info);
		Assert.assertNotNull(info.getLocators());
		Assert.assertEquals("cluster_operator", info.getUsername());
		Assert.assertEquals("3q1XFDobsp8S2wJZ8ajTQ", info.getPassword());
		Assert.assertNull(info.getRestURL());
	}

	@Test
	public void testAcceptService() throws Exception {
		GemfireServiceInfoCreator creator = new GemfireServiceInfoCreator();
		Map services = readServiceData("test-gemfire-service.json");
		Map<String, Object> serviceData = getServiceData(services, "p-gemfire");
		boolean accepts = creator.accept(serviceData);
		Assert.assertEquals(true, accepts);
	}

	@Test
	public void testInfoCreatorCups() throws Exception {
		GemfireServiceInfoCreator creator = new GemfireServiceInfoCreator();
		Map services = readServiceData("test-gemfire-userprovided.json");
		Map<String, Object> serviceData = getServiceData(services, "user-provided");

		GemfireServiceInfo info = creator.createServiceInfo(serviceData);
		Assert.assertNotNull(info);
	}

	@Test
	public void testAcceptCups() throws Exception {
		GemfireServiceInfoCreator creator = new GemfireServiceInfoCreator();
		Map services = readServiceData("test-gemfire-userprovided.json");
		Map<String, Object> serviceData = getServiceData(services, "user-provided");
		boolean accepts = creator.accept(serviceData);
		Assert.assertEquals(true, accepts);
	}

	@Test
	public void testRestUrl() throws Exception {
		GemfireServiceInfoCreator creator = new GemfireServiceInfoCreator();
		Map services = readServiceData("test-gemfire-rest-service.json");
		Map<String, Object> serviceData = getServiceData(services, "p-gemfire");
		GemfireServiceInfo info = creator.createServiceInfo(serviceData);
		Assert.assertEquals("gemfire-si.foo.bar.com", info.getRestURL());
	}

	@Test
	public void testCloudCache() throws Exception{
		GemfireServiceInfoCreator creator = new GemfireServiceInfoCreator();
		Map servicesJsonMap = readServiceData("cloudcache-service.json");
		Map<String, Object> serviceData = getServiceData(servicesJsonMap, "p-cloudcache");
		GemfireServiceInfo info = creator.createServiceInfo(serviceData);
		Assert.assertThat(info.getLocators()[0].toString(), Matchers.containsString("locator://10.244.0.4:55221"));
		Assert.assertThat(info.getLocators()[1].toString(), Matchers.containsString("locator://10.244.1.2:55221"));
		Assert.assertThat(info.getLocators()[2].toString(), Matchers.containsString("locator://10.244.0.130:55221"));
		Assert.assertThat(info.getUsername(), Matchers.equalTo("cluster_operator"));
		Assert.assertThat(info.getPassword(), Matchers.equalTo("some_password"));
	}

	private Map readServiceData(String resource) throws java.io.IOException {
		return mapper.readValue(readTestDataFile(resource), Map.class);
	}

	@SuppressWarnings("unchecked")
	private Map<String, Object> getServiceData(Map services, String name) {
		return (Map<String, Object>) ((List) services.get(name)).get(0);
	}
}
