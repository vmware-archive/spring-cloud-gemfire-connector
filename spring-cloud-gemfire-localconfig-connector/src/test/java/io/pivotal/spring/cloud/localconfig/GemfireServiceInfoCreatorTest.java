package io.pivotal.spring.cloud.localconfig;

import io.pivotal.spring.cloud.service.common.GemfireServiceInfo;
import org.junit.Test;
import org.springframework.cloud.service.UriBasedServiceData;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by esuez on 1/17/16.
 */
public class GemfireServiceInfoCreatorTest {
  @Test
  public void shouldRegisterGemfireAsScheme() {
    GemfireServiceInfoCreator serviceCreator = new GemfireServiceInfoCreator();

    assertTrue(serviceCreator.accept(new UriBasedServiceData("some-service", "gemfire://localhost:10334/")));
  }

  @Test
  public void shouldProduceLocatorStringFromUri() {
    assertEquals("localhost[10334]", GemfireServiceInfoCreator.extractLocatorFromUri("gemfire://localhost:10334"));
  }

  @Test
  public void shouldProduceLocatorStringFromUriWithTrailingSlash() {
    assertEquals("localhost[10334]", GemfireServiceInfoCreator.extractLocatorFromUri("gemfire://localhost:10334/"));
  }

  @Test
  public void shouldCreateServiceInfoForIdAndUri() {
    GemfireServiceInfoCreator serviceInfoCreator = new GemfireServiceInfoCreator();
    GemfireServiceInfo serviceInfo = serviceInfoCreator.createServiceInfo("my-gemfire-service", "gemfire://localhost:10334/");

    assertNotNull(serviceInfo);
    assertEquals(1, serviceInfo.getLocators().length);
    assertEquals("localhost", serviceInfo.getLocators()[0].getHost());
    assertEquals(10334, serviceInfo.getLocators()[0].getPort());
  }
}
