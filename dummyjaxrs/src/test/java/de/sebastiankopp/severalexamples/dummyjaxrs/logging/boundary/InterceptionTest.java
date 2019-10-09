package de.sebastiankopp.severalexamples.dummyjaxrs.logging.boundary;

import de.sebastiankopp.severalexamples.dummyjaxrs.control.SlowService;
import de.sebastiankopp.severalexamples.dummyjaxrs.test.logging.TestScopeAppender;
import org.hamcrest.Matchers;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.jboss.shrinkwrap.descriptor.api.beans11.BeansDescriptor;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.inject.Inject;
import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.testng.Assert.fail;

public class InterceptionTest extends Arquillian {
	
	@Inject
	SlowService slowService;
	
	private TestScopeAppender appender;
	
	@BeforeMethod
	public void before() throws IOException {
		appender = TestScopeAppender.byName("TScope").orElse(null);
		if (appender != null)  {
			appender.clear();
		} else {
			fail("Missing TestScopeAppender");
		}
	}
	
	@Deployment
	public static JavaArchive createArchive() {
		String beansXml = Descriptors.create(BeansDescriptor.class)
				.beanDiscoveryMode("all")
				.exportAsString();
		System.out.println("beans.xml: " + beansXml);
		return ShrinkWrap.create(JavaArchive.class)
				.addClasses(SlowService.class, Logged.class, CustomLoggerInterceptor.class)
				.addAsManifestResource(new StringAsset(beansXml), "beans.xml");
	}
	
	@Test
	public void t1() {
		int siz1 = appender.getEvents().size();
		slowService.giveMeSomeRandom();
		int siz2 = appender.getEvents().size();
		assertThat(siz2, Matchers.greaterThan(siz1));
	}
	
	@AfterMethod
	public void clearAll() throws IOException {
		appender.clear();
	}
}
