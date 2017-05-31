This repository is meant to illustrate a WAS 9.0.0.3 issue when using JAX-RS 2.0 with Apapche CXF 3.1.11.
The issue consists in raising the following exception:
[31/05/17 11:14:31:120 CEST] 000000a1 ServletWrappe E com.ibm.ws.webcontainer.servlet.ServletWrapper init SRVE0271E: Uncaught init() exception created by servlet [fr.simplex_software.tests.App] in application [test-jaxrs-was_war]: org.apache.cxf.service.factory.ServiceConstructionException
	at org.apache.cxf.jaxrs.JAXRSServerFactoryBean.create(JAXRSServerFactoryBean.java:219)
	at com.ibm.ws.jaxrs20.server.JaxRsWebEndpointImpl.init(JaxRsWebEndpointImpl.java:69)
	at com.ibm.websphere.jaxrs.server.IBMRestServlet.init(IBMRestServlet.java:82)
	at com.ibm.ws.webcontainer.servlet.ServletWrapper.init(ServletWrapper.java:342)
	at com.ibm.ws.webcontainer.servlet.ServletWrapperImpl.init(ServletWrapperImpl.java:168)
	at com.ibm.ws.webcontainer.servlet.ServletWrapper.load(ServletWrapper.java:1385)
	at com.ibm.ws.webcontainer.filter.WebAppFilterManager.invokeFilters(WebAppFilterManager.java:1029)
	at com.ibm.ws.webcontainer.webapp.WebApp.handleRequest(WebApp.java:4144)
	at com.ibm.ws.webcontainer.webapp.WebAppImpl.handleRequest(WebAppImpl.java:2208)
	at com.ibm.ws.webcontainer.webapp.WebGroup.handleRequest(WebGroup.java:304)
	at com.ibm.ws.webcontainer.WebContainer.handleRequest(WebContainer.java:1030)
	at com.ibm.ws.webcontainer.WSWebContainer.handleRequest(WSWebContainer.java:1817)
	at com.ibm.ws.webcontainer.channel.WCChannelLink.ready(WCChannelLink.java:382)
	at com.ibm.ws.http.channel.inbound.impl.HttpInboundLink.handleDiscrimination(HttpInboundLink.java:465)
	at com.ibm.ws.http.channel.inbound.impl.HttpInboundLink.handleNewRequest(HttpInboundLink.java:532)
	at com.ibm.ws.http.channel.inbound.impl.HttpInboundLink.processRequest(HttpInboundLink.java:318)
	at com.ibm.ws.http.channel.inbound.impl.HttpInboundLink.ready(HttpInboundLink.java:289)
	at com.ibm.ws.tcp.channel.impl.NewConnectionInitialReadCallback.sendToDiscriminators(NewConnectionInitialReadCallback.java:214)
	at com.ibm.ws.tcp.channel.impl.NewConnectionInitialReadCallback.complete(NewConnectionInitialReadCallback.java:113)
	at com.ibm.ws.tcp.channel.impl.AioReadCompletionListener.futureCompleted(AioReadCompletionListener.java:175)
	at com.ibm.io.async.AbstractAsyncFuture.invokeCallback(AbstractAsyncFuture.java:217)
	at com.ibm.io.async.AsyncChannelFuture.fireCompletionActions(AsyncChannelFuture.java:161)
	at com.ibm.io.async.AsyncFuture.completed(AsyncFuture.java:138)
	at com.ibm.io.async.ResultHandler.complete(ResultHandler.java:204)
	at com.ibm.io.async.ResultHandler.runEventProcessingLoop(ResultHandler.java:775)
	at com.ibm.io.async.ResultHandler$2.run(ResultHandler.java:905)
	at com.ibm.ws.util.ThreadPool$Worker.run(ThreadPool.java:1892)

while bootstraping the resources in the following code:

@ApplicationPath("services")
public class App extends Application
{
}

Interesting enough, if web.xml is used instead of annotations, as in the example below:

  <servlet>
    <servlet-name>CXFServlet</servlet-name>
    <servlet-class>org.apache.cxf.jaxrs.servlet.CXFNonSpringJaxrsServlet</servlet-class>
    <init-param>
      <param-name>javax.ws.rs.Application</param-name>
      <param-value>fr.simplex_software.tests.TestServiceImpl</param-value>
    </init-param>
    <load-on-startup>1</load-on-startup>
  </servlet>
  <servlet-mapping>
    <servlet-name>CXFServlet</servlet-name>
    <url-pattern>/services/*</url-pattern>
  </servlet-mapping>
  
 then everything works as expected. 
The included WAR contains a very simple REST service with only one endpoint. In order to experience the issue perform the following operations:
  1. Clone the repo (git clone ....)
  2. Build the WAR (mvn -DskipTests clean package)
  deploy the created WAR on WAS 9.0 and start the application
  3. Run integration tests (mvn failsafe:integration-test)
  
The following assertion failure will be observed:
java.lang.AssertionError: expected:<200> but was:<500>
        at fr.simplex_software.tests.ITest123.testMultipart2(ITest123.java:33)

Now, to confirm that using web.xml instead of annotations works, do the following:
  1. Switch on the develop branch (git checkout develop)
  2. Perform the remaining operations as above
  
One detail to be noted is that the WAR is including Apache CXF 3.1.11 artifacts. Then the WAS parent-last class-loading strategy should be used.  
However, during the tests it appeared that using the parent-first or the parent-last strategy doesn't change anything. 
The same as far as the variable com.ibm.websphere.jaxrs.server.DisableIBMJAXRSEngine is concerned.

Another interesting point is that it appears that using the Apahce CXF provided with WAS 9.0, which is 3.0.3, and consequently not including the libraries in the WAR, works as expected as well.
