package de.sebikopp.dummyjaxrs.boundary;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

public class JaxRsXmlValidationIT {

	private final String host = "localhost";
	private final int port = 8080;
	private WebTarget target = null;
	
	
	@Test
	public void testWithValidXml() {
		initJaxRsClient();
		Response rsp = target.request()
				.buildPost(loadResourceAsEntity("xml/validXml.xml"))
				.invoke();
		int status = rsp.getStatus();
		Assert.assertThat(status, Matchers.allOf(Matchers.greaterThanOrEqualTo(200), Matchers.lessThan(300)));
	}

	
	@Test
	public void testWithNonValidXml() {
		initJaxRsClient();
		Response rsp = target.request()
				.buildPost(loadResourceAsEntity("xml/nonValidXml.xml"))
				.invoke();
		int status = rsp.getStatus();
		Assert.assertEquals(status, Status.BAD_REQUEST.getStatusCode());
	}
	
	private void initJaxRsClient() {
		try {
			target = ClientBuilder.newClient()
					.target("http://" + host + ":" +port)
					.path("dummyjaxrs")
					.path("resources")
					.path("peoplexml");
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("Could not run tests due to an exception");
		}
	}
	
	private Entity<String> loadResourceAsEntity(String p2res) {
		String xml = loadXml(p2res);
		return Entity.entity(xml, MediaType.APPLICATION_XML);
	}
	private String loadXml(String p2Resource) {
		ClassLoader cl = JaxRsXmlValidationIT.class.getClassLoader();
		InputStream input = cl.getResourceAsStream(p2Resource);
		try (final BufferedReader buffer = new BufferedReader(new InputStreamReader(input))) {
            return buffer.lines().collect(Collectors.joining(System.lineSeparator()));
        } catch (Exception e) {
			throw new RuntimeException("Test cannot be finished due to an Exception", e);
		}
		
	}
}
