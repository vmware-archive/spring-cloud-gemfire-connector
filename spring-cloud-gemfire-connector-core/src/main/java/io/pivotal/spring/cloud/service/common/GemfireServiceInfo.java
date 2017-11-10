package io.pivotal.spring.cloud.service.common;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.cloud.service.BaseServiceInfo;
import org.springframework.cloud.service.ServiceInfo.ServiceLabel;

/**
 * 
 * Information to access Gemfire services
 * 
 * @author Vinicius Carvalho
 *
 */
@ServiceLabel("gemfire")
public class GemfireServiceInfo extends BaseServiceInfo {

	private URI[] locators;
	private final String username;
	private final String password;
	private String restURL;

	public GemfireServiceInfo(Builder builder){
		super(builder.id);

		this.locators = builder.locators;
		this.username = builder.username;
		this.password = builder.password;
		this.restURL = builder.restURL;
	}

	public static class Builder{
		private final Pattern p = Pattern.compile("(.*)\\[(\\d*)\\]");

		private URI[] locators;
		private String id;

		//optional
		private String restURL;
		private String username;
		private String password;

		public Builder(String id, List<String> locators){
			ArrayList<URI> uris = new ArrayList<URI>(locators.size());
			for (String locator : locators) {
				uris.add(parseLocator(locator));
			}
			this.locators = uris.toArray(new URI[uris.size()]);
			this.id = id;
		}

		public  Builder restUrl(String restURL){
			this.restURL = restURL;
			return this;
		}

		public Builder usernamePassword(String username, String password){
			this.username = username;
			this.password = password;
			return this;
		}

		public GemfireServiceInfo build(){
			return new GemfireServiceInfo(this);
		}

		private URI parseLocator(String locator) throws IllegalArgumentException {
			Matcher m = p.matcher(locator);
			if (!m.find()) {
				throw new IllegalArgumentException("Could not parse locator url. Expected format host[port], received: " + locator);
			} else {
				if (m.groupCount() != 2) {
					throw new IllegalArgumentException("Could not parse locator url. Expected format host[port], received: " + locator);
				}
				try {
					return new URI("locator://" + m.group(1) + ":" + m.group(2));
				} catch (URISyntaxException e) {
					throw new IllegalArgumentException("Malformed URL " + locator);
				}
			}
		}
	}



	public URI[] getLocators() {
		return locators;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String getRestURL() {
		return restURL;
	}
}
