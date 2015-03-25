# An example project about various JPA helper libraries for Java EE

Plain JPA is not necessary the most developer friendly way to implement JPA based persistency. This is an example project that introduces SpringData, its plain CDI cousin DeltaSpike Data and QueryDSL, which can make working with JPA persistency much more efficient (than with the raw API). The example app contains three EJB implementations for simple phonebook CRUD and shows how the tools can help to implement them.

This example has been built to support an article series on the same topic, but this might in the future contain examples of other JPA related helper libraries as well. The UI layer is built using Vaadin.

To play around with the project, import it in to your favourite IDE and deploy to WilldFly using *mvn wildfly:run* (tested on Wildlfy , but should be easily portable).
