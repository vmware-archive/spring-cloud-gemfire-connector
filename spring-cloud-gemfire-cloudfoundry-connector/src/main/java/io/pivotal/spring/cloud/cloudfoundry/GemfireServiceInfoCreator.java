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
		String devUsername = "", devPassword = "";
		Map<String, Object> credentials = getCredentials(serviceData);
		List<Map<String, String>> users = (List<Map<String,String>>) credentials.get("users");
		for (Map<String, String> user: users) {
			if (user.get("username").equalsIgnoreCase("developer")){
				devUsername = user.get("username");
				devPassword = user.get("password");
			}
		}
//		String username = users.get(1).get("username");
//		String password = getStringFromCredentials(credentials, "password");
		List<String> locators = (List<String>) credentials.get("locators");
		String restURL = getStringFromCredentials(credentials, "rest_url");

		return new GemfireServiceInfo.Builder(id, locators).usernamePassword(devUsername, devPassword).restUrl(restURL).build();
	}

	@Override
	public boolean accept(Map<String, Object> serviceData) {
		return containsLocators(serviceData) || super.accept(serviceData);
	}

	protected boolean containsLocators(Map<String,Object> serviceData){
		Object locators = getCredentials(serviceData).get("locators");
		return locators != null;
	}

}
