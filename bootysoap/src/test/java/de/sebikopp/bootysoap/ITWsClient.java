package de.sebikopp.bootysoap;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.UUID;
import javax.xml.ws.BindingProvider;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import de.sebikopp.bootysoap.crosscuttingtypes.CallID;
import de.sebikopp.bootysoap.wsdlx.digestwebservice.CreateDigestRequest;
import de.sebikopp.bootysoap.wsdlx.digestwebservice.CreateDigestResponse;
import de.sebikopp.bootysoap.wsdlx.digestwebservice.CustomFault;
import de.sebikopp.bootysoap.wsdlx.digestwebservice.DigestWebservice;
import de.sebikopp.bootysoap.wsdlx.digestwebservice.DigestWebservice_Service;
import de.sebikopp.bootysoap.wsdlx.digestwebservice.PushPayloadRequest;

public class ITWsClient {
	
	private DigestWebservice proxy;
	private final String urlWsdl = "http://127.0.0.1:9080/cxf/dig?wsdl";
	@Before
	public void init() throws MalformedURLException {
		DigestWebservice_Service service = new DigestWebservice_Service(new URL(urlWsdl));
		proxy = service.getPort(DigestWebservice.class);
		BindingProvider bp = (BindingProvider) proxy;
		bp.getRequestContext().put("javax.xml.ws.client.connectionTimeout", 10_000);
		bp.getRequestContext().put("schema-validation-enabled", "true");
		System.setProperty("org.apache.cxf.Logger", "org.apache.cxf.common.logging.Log4jLogger");
	}
	
	@Test
	public void test1() throws Exception {
		CreateDigestRequest body = new CreateDigestRequest();
		final String algorithm = "SHA-256";
		body.setAlgorithm(algorithm);
		final byte[] payload = generatePayload();
		body.setPayload(payload);
		CreateDigestResponse digestResponse = proxy.getDigest(getCallId(), body);
		byte[] correctDigest = MessageDigest.getInstance(algorithm).digest(payload);	
		Assert.assertArrayEquals(correctDigest, digestResponse.getPayloadDigest());
	}

	private byte[] generatePayload() {
		final byte[] payload = "Hallo Welt".getBytes(StandardCharsets.UTF_8);
		return payload;
	}
	
	@Test(timeout=2_500)
	public void testCallAsync() {
		PushPayloadRequest pushPayloadRequest = new PushPayloadRequest("Hallo Welt".getBytes(StandardCharsets.UTF_8));
		proxy.pushPayload(getCallId(), pushPayloadRequest);
	}

	@Test(expected=CustomFault.class)
	public void testFault() throws Exception {
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
