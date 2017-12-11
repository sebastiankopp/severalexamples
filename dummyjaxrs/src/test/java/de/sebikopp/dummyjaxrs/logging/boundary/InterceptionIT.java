package de.sebikopp.dummyjaxrs.logging.boundary;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import org.hamcrest.Matchers;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.Asset;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.jboss.shrinkwrap.descriptor.api.beans11.BeansDescriptor;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.runner.RunWith;

import de.sebikopp.dummyjaxrs.control.SlowService;

@RunWith(Arquillian.class)
public class InterceptionIT {
	
	private static final String LOGFILENAME = "foobar.log";

	private static final String LOGDIR = "target/test-logs";

	@Inject
	SlowService slowService;
	
	private Path logDir;
	private Path logfilePath;
	
	@Before
	public void before() throws IOException {
		this.logDir = Paths.get(LOGDIR);
		this.logfilePath = this.logDir.resolve(LOGFILENAME);
		Files.deleteIfExists(logfilePath);
		Files.createDirectories(logDir);
		Files.createFile(logfilePath);
	}
	
//	@Deployment
	public static WebArchive createArchive() {
		BeansDescriptor beansDescriptor = Descriptors.create(BeansDescriptor.class);
		beansDescriptor.beanDiscoveryMode("all");
		Asset beansAsset = new StringAsset(beansDescriptor.exportAsString());
		return ShrinkWrap.create(WebArchive.class)
				.addClasses(SlowService.class, Logged.class, CustomLoggerInterceptor.class)
//				.addAsManifestResource(new File("src/main/webapp/WEB-INF/beans.xml"), "beans.xml");
				.addAsManifestResource(beansAsset, "META-INF/beans.xml");
	}
	
//	@Test
	public void t1() throws IOException, InterruptedException {
		long size0 = Files.size(logfilePath);
		slowService.giveMeSomeRandom();
		TimeUnit.SECONDS.sleep(1);
		long size1 = Files.size(logfilePath);
		Assert.assertThat(size1, Matchers.greaterThan(size0));
	}
	
	@After
	public void clearAll() throws IOException {
//		Files.deleteIfExists(logfilePath);
	}
	
	

}
