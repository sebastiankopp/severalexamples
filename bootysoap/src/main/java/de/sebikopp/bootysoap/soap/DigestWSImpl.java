package de.sebikopp.bootysoap.soap;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.WebServiceContext;

import org.apache.cxf.annotations.SchemaValidation;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.sebikopp.bootysoap.crosscuttingtypes.CallID;
import de.sebikopp.bootysoap.crosscuttingtypes.FaultContent;
import de.sebikopp.bootysoap.wsdlx.digestwebservice.CreateDigestRequest;
import de.sebikopp.bootysoap.wsdlx.digestwebservice.CreateDigestResponse;
import de.sebikopp.bootysoap.wsdlx.digestwebservice.CustomFault;
import de.sebikopp.bootysoap.wsdlx.digestwebservice.DigestWebservice;
import de.sebikopp.bootysoap.wsdlx.digestwebservice.PushPayloadRequest;

@WebService(targetNamespace="http://sebikopp.de/bootysoap/wsdlx/DigestWebservice", serviceName="DigestWebservice", name="DigestWebservice",
			portName="DigestWebserviceSOAP", wsdlLocation="wsdl/DigestWebservice.wsdl")
@Service
@SchemaValidation
public class DigestWSImpl implements DigestWebservice{

	@Autowired
	Logger logger;
	
	@Resource
	WebServiceContext webServiceContext;
	
	static final String COMP_INJECT_NAME_DIGEST_WS = "DigestWebservice";
	
	@Override
	public CreateDigestResponse getDigest(CallID header1, CreateDigestRequest body) throws CustomFault {
		final String algorithm = body.getAlgorithm();
		try {
			logger.debug("Msg ctx: {}", webServiceContext.getMessageContext());
			byte[] digest = MessageDigest.getInstance(algorithm).digest(body.getPayload());
			CreateDigestResponse response = new CreateDigestResponse();
			response.setPayloadDigest(digest);
			return response;
		} catch (NoSuchAlgorithmException e) {
			final String msg = "Unsupported hash algorithm: " + algorithm;
			throw new CustomFault(msg, createFaultContent(e, msg, header1.getValue()), null);
		} catch (Exception e) {
			final String msg = "A fatal error occured while processing: " + e.getMessage();
			throw new CustomFault(msg, createFaultContent(e, msg, header1.getValue()), e);
		}
	}

	@Override
	public void pushPayload(CallID hdx, PushPayloadRequest body) {
		try {
			logger.debug("Msg ctx: {}", webServiceContext.getMessageContext());
			logger.debug("Starting to sleep ...");
			TimeUnit.SECONDS.sleep(42);
			logger.debug("Finished with sleeping ...");
		} catch (InterruptedException e) {
			throw new IllegalStateException("interrupted", e);
		}
	}

	
	private FaultContent createFaultContent(Exception e, String msg, String callId) {
		FaultContent rc = new FaultContent();
		rc.setCallID(callId);
		rc.setFaultID(UUID.randomUUID().toString());
		rc.setMessage(msg);
		if (e != null) {
			try (final StringWriter sw = new StringWriter(); final PrintWriter pw = new PrintWriter(sw)) {
				e.printStackTrace(pw);
				rc.setStackTrace(sw.toString());
			} catch (IOException e1) {
				System.err.println("IO---");
			}
		}
		return rc;
	}
}
