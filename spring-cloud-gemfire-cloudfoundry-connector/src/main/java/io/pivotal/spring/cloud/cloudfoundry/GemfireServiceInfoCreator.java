package io.pivotal.spring.cloud.cloudfoundry;

import java.util.List;
import java.util.Map;

import org.springframework.cloud.cloudfoundry.CloudFoundryServiceInfoCreator;
import org.springframework.cloud.cloudfoundry.Tags;
import io.pivotal.spring.cloud.service.common.GemfireServiceInfo;

/**
 *
 * Service info creator for GemFire services
 *
 * @author Vinicius Carvalho
 */
public class GemfireServiceInfoCreator extends CloudFoundryServiceInfoCreator<GemfireServiceInfo> {

	public GemfireServiceInfoCreator() {
		super(new Tags("gemfire"));
	}

	@Override
	@SuppressWarnings("unchecked")
	public GemfireServiceInfo createServiceInfo(Map<String, Object> serviceData) {
		String id = (String) serviceData.get("name");
		String username = "",password = "";
		Map<String, Object> credentials = getCredentials(serviceData);
		List<Map<String, Object>> users = (List<Map<String,Object>>) credentials.get("users");
		for (Map<String, Object> user: users) {
			if (isClusterOperator(user)){
				username = (String) user.get("username");
				password = (String) user.get("password");
			}
		}
		List<String> locators = (List<String>) credentials.get("locators");
		String restURL = getStringFromCredentials(credentials, "rest_url");

		return new GemfireServiceInfo.Builder(id, locators).usernamePassword(username, password).restUrl(restURL).build();
	}

	@Override
	public boolean accept(Map<String, Object> serviceData) {
		return containsLocators(serviceData) || super.accept(serviceData);
	}

	protected boolean containsLocators(Map<String,Object> serviceData){
		Object locators = getCredentials(serviceData).get("locators");
		return locators != null;
	}

	private boolean isClusterOperator(Map<String, Object> user) {
		return user.get("roles") != null && ((List<String>)user.get("roles")).contains("cluster_operator")
				|| user.get("username").equals("cluster_operator");
	}

}
