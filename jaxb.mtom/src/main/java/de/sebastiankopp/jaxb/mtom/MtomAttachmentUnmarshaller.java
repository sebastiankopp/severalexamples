package de.sebastiankopp.jaxb.mtom;


import jakarta.activation.DataHandler;
import jakarta.xml.bind.attachment.AttachmentUnmarshaller;

public class MtomAttachmentUnmarshaller extends AttachmentUnmarshaller {
	private final JaxbMtomContainer cont;
	public MtomAttachmentUnmarshaller(JaxbMtomContainer cont) {
		this.cont = cont;
	}
	
	@Override
	public byte[] getAttachmentAsByteArray(String arg0) {
		System.out.println("Unmarshalling as byte array");
		return cont.getAttachments().get(arg0);
	}

	@Override
	public DataHandler getAttachmentAsDataHandler(String arg0) {
		return new DataHandler(new InputStreamDataSource(getAttachmentAsByteArray(arg0)));
	}
	@Override
	public boolean isXOPPackage() {
		return true;
	}
}
