A basic implementation of a localconfig connector for gemfire, to support local development.

Basically parses a url following this pattern:

    gemfire://<host>:<port>/

..to locator reference:

    <host>[port]

The spring cloud localconfig connector documentation quick start, at:
http://cloud.spring.io/spring-cloud-connectors/spring-cloud-connectors.html#_local_configuration_connector

..shows an example of configuring a local mysql backing service using a spring-cloud properties file:

    spring.cloud.appId:    myApp
    ; spring.cloud.{serviceId}:   URI
    spring.cloud.database: mysql://user:pass@host:1234/dbname

With the gemfire localconfig connector, you can do the same for gemfire, like so:

    spring.cloud.appId:   myApp
    spring.cloud.my-gemfire-service: gemfire://localhost:10334/

Then, assuming you have a gemfire cluster running locally at `localhost[10334]`, then you'll be able to test your application locally without any code changes.  When deploying your app to PCF, the `ClientCache` will be constructed and provided by the cloudfoundry connector instead.

Here is one way to construct a `ClientCache` bean in your spring boot Application class:

    @SpringBootApplication
    @EnableGemfireRepositories(basePackages = 'com.eitan')
    class ContactApplication extends AbstractCloudConfig {
      ...
      @Bean
      ClientCache myClientCache() {
        return cloud().getServiceConnector("my-gemfire-service", ClientCache.class, null);
      }
      ..
    }

The other mechanism, assuming you have a single bound service of type "gemfire", is to use:

    cloud().getSingletonServiceConnector(ClientCache.class);

