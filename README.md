# An example project about various JPA helper libraries for Java EE

Plain JPA is not necessary the most developer friendly way to implement JPA based persistency. This is an example project that introduces three awesome helper libraries, [SpringData](http://projects.spring.io/spring-data-jpa/), its plain CDI cousin [DeltaSpike Data](https://deltaspike.apache.org/documentation/data.html) and [QueryDSL](http://www.querydsl.com), which can make working with JPA much more efficient (than with the raw API). The example app contains three EJB different implementations for simple phonebook CRUD and shows how the tools can help to implement them.

This example has been built to support an article series on the same topic, but this might in the future contain examples of other JPA related helper libraries as well. The UI layer is built using [Vaadin](https://vaadin.com/), but the actual meat is the EJB implementations.

To play around with the project, import it in to your favourite IDE and deploy to WilldFly using *mvn wildfly:run* (tested on Wildlfy , but should be easily portable).
