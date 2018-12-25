package de.sebastiankopp.jaxb.mtom;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import javax.activation.DataHandler;
import javax.xml.bind.attachment.AttachmentMarshaller;

public final class MtomAttachmentMarshaller extends AttachmentMarshaller {
	private final Map<String,byte[]> attachData = new HashMap<>();
	@Override
	public String addMtomAttachment(DataHandler data, String elementNamespace, String elementLocalName) {
		return addSwaRefAttachment(data);
	}

	@Override
	public String addMtomAttachment(byte[] data, int offset, int length, String mimeType, String elementNamespace,
			String elementLocalName) {
		final byte[] sizedData = Arrays.copyOfRange(data, offset, length);
		final String uri = "urn:uuid:" + UUID.randomUUID().toString();
		attachData.put(uri, sizedData);
		return uri;
		
	}

	@Override
	public String addSwaRefAttachment(DataHandler data) {
		try {
			final String uri = "urn:uuid:" + UUID.randomUUID().toString();
			attachData.put(uri, readInstr(data.getInputStream()));
			return uri;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public boolean isXOPPackage() {
		return true;
	}
	
	public Map<String,byte[]> getContainedData() {
		return Collections.unmodifiableMap(attachData);
	}
	
	private static byte[] readInstr(final InputStream is) throws IOException {
		byte[] buf = new byte[1024];
		int len;
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream()){
			while ((len = is.read(buf)) > 0) {
				baos.write(buf, 0, len);
			}
			return baos.toByteArray();
		}
	}
}
