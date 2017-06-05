package de.sebikopp.dummyjaxrs.logging.boundary;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

import de.sebikopp.dummyjaxrs.boundary.JsonCollectors;

@Provider
public class ContainerRequestLoggingFilter implements ContainerRequestFilter{

	@Inject
	Logger lggr;
	
	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		if (lggr.isEnabled(Level.INFO)) {
			String method = requestContext.getMethod();
			lggr.info("HTTP " + method + " on " + requestContext.getUriInfo().getAbsolutePath());
			final JsonObjectBuilder bui = Json.createObjectBuilder();
			requestContext.getHeaders().forEach((k, list) -> {
				JsonArray hdrValList = list.stream().collect(JsonCollectors.collectStrings());
				bui.add(k, hdrValList);
			});
			lggr.info("Header data: " + bui.build());
			if (requestContext.hasEntity() && lggr.isEnabled(Level.DEBUG)) {
				InputStream httpBody = requestContext.getEntityStream();
				byte[] data = extractInputStream(httpBody);
				requestContext.setEntityStream(new ByteArrayInputStream(data));
				lggr.debug("Content in textual representation");
				lggr.debug(new String(data, StandardCharsets.UTF_8));
				
			}
		}
	}
	
	static byte[] extractInputStream(InputStream instr) throws IOException {
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream()){
			byte [] buf = new byte[16384];
			int count;
			while ((count=instr.read(buf)) > 0) {
				baos.write(buf, 0, count);
			}
			
			return baos.toByteArray();
		}
	}

}
