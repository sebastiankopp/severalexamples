package de.sebastiankopp.severalexamples.dummyjaxrs.boundary;

import de.sebastiankopp.severalexamples.bootysoap.crosscuttingtypes.CallID;
import de.sebastiankopp.severalexamples.bootysoap.wsdlx.digestwebservice.*;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.xml.ws.BindingProvider;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.UUID;

import static de.sebastiankopp.severalexamples.dummyjaxrs.test.CustomMatchers.urlStrReachable;
import static de.sebastiankopp.severalexamples.dummyjaxrs.test.TestNgAssume.assumeThat;

public class ITWsClient {
	
	private DigestWebservice proxy;
	private final String urlWsdl = "http://127.0.0.1:8080/dummyjaxrs/dig?wsdl";
	@BeforeMethod
	public void init() throws MalformedURLException {
		assumeThat(urlWsdl, urlStrReachable());
		DigestWebservice_Service service = new DigestWebservice_Service(new URL(urlWsdl));
		proxy = service.getPort(DigestWebservice.class);
		BindingProvider bp = (BindingProvider) proxy;
		bp.getRequestContext().put("javax.xml.ws.client.connectionTimeout", 10_000);
		bp.getRequestContext().put("schema-validation-enabled", "true");
		System.setProperty("org.apache.cxf.Logger", "org.apache.cxf.common.logging.Log4jLogger");
	}
	
	@Test
	public void test1() throws Exception {
		assumeThat(urlWsdl, urlStrReachable());
		CreateDigestRequest body = new CreateDigestRequest();
		final String algorithm = "SHA-256";
		body.setAlgorithm(algorithm);
		final byte[] payload = generatePayload();
		body.setPayload(payload);
		CreateDigestResponse digestResponse = proxy.getDigest(getCallId(), body);
		byte[] correctDigest = MessageDigest.getInstance(algorithm).digest(payload);	
		Assert.assertEquals(correctDigest, digestResponse.getPayloadDigest());
	}

	private byte[] generatePayload() {
		final byte[] payload = "Hallo Welt".getBytes(StandardCharsets.UTF_8);
		return payload;
	}
	
	@Test(timeOut=2_500)
	public void testCallAsync() {
		assumeThat(urlWsdl, urlStrReachable());
		PushPayloadRequest pushPayloadRequest = new PushPayloadRequest("Hallo Welt".getBytes(StandardCharsets.UTF_8));
		proxy.pushPayload(getCallId(), pushPayloadRequest);
	}

	@Test(expectedExceptions=CustomFault.class)
	public void testFault() throws Exception {
		assumeThat(urlWsdl, urlStrReachable());
		CreateDigestRequest request = new CreateDigestRequest();
		final String algorithm = "FOO-256";
		request.setAlgorithm(algorithm);
		request.setPayload(generatePayload());
		proxy.getDigest(getCallId(), request);
	}
	
	static CallID getCallId() {
		CallID callId = new CallID();
		callId.setMustUnderstand(Boolean.TRUE);
		callId.setValue(UUID.randomUUID().toString());
		return callId;
	}
	
}
