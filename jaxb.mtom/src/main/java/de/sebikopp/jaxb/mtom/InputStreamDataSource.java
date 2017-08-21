package de.sebikopp.jaxb.mtom;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.activation.DataSource;

public class InputStreamDataSource implements DataSource {

	private static final String MIME_APPLICATION_OCTET_STREAM = "application/octet-stream";

	private final InputStream is;
	public InputStreamDataSource(final byte[] b) {
		this.is = new ByteArrayInputStream(b);
	}
	
	@Override
	public String getContentType() {
		return MIME_APPLICATION_OCTET_STREAM;
	}

	@Override
	public InputStream getInputStream() throws IOException {
		return is;
	}

	@Override
	public String getName() {
		return getClass().getSimpleName();
	}

	@Override
	public OutputStream getOutputStream() throws IOException {
		throw new UnsupportedOperationException("Dies ist eine INPUTStreamDataSource!!!");
	}

}
