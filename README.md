#Spring Cloud GemFire Connector

Spring Cloud Connector for using GemFire service on Cloud Foundry

### Java Applications

Applications can use this connector to access the information in `VCAP_SERVICES`
environment variable, necessary to connect to a GemFire cluster.

```
CloudFactory cloudFactory = new CloudFactory();
Cloud cloud = cloudFactory.getCloud();
GemfireServiceInfo myService = (GemfireServiceInfo) cloud.getServiceInfo("MyService");
myService.getUsername();
myService.getLocators();
```

### Spring Applications

Spring Application can use this connector to auto inject a GemFire ClientCache
which enables the application to talk to the GemFire cluster.

### Examples

Simple example apps are at https://github.com/gemfire/cf-gemfire-connector-examples
