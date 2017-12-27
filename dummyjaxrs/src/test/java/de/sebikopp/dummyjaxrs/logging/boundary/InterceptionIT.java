package de.sebikopp.dummyjaxrs.logging.boundary;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import org.hamcrest.Matchers;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.ByteArrayAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.sebikopp.dummyjaxrs.control.SlowService;
import de.sebikopp.dummyjaxrs.test.logging.TestScopeAppender;

@RunWith(Arquillian.class)
@FixMethodOrder
public class InterceptionIT {
	
	@Inject
	SlowService slowService;
	
	private TestScopeAppender appender;
	
	@Before
	public void before() throws IOException {
		appender = TestScopeAppender.byName("TScopeGlob").orElse(null);
		if (appender != null)  {
			appender.clear();
		} else {
			Assert.fail("Missing TestScopeAppender");
		}
	}
	
	@Deployment
	public static JavaArchive createArchive() {
		InputStream beansXml = InterceptionIT.class.getClassLoader().getResourceAsStream("beans.xml");
		return ShrinkWrap.create(JavaArchive.class)
				.addClasses(SlowService.class, Logged.class, CustomLoggerInterceptor.class)
				.addAsManifestResource(new ByteArrayAsset(beansXml), "beans.xml");
	}
	
	@Test
	public void t1() throws IOException, InterruptedException {
		int siz1 = appender.getEvents().size();
		slowService.giveMeSomeRandom();
		TimeUnit.SECONDS.sleep(1);
		int siz2 = appender.getEvents().size();
		Assert.assertThat(siz2, Matchers.greaterThan(siz1));
	}
	
	@After
	public void clearAll() throws IOException {
		appender.clear();
	}
	
	

}
