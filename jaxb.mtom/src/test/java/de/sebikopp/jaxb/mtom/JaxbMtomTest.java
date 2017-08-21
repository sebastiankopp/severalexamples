package de.sebikopp.jaxb.mtom;

import java.util.Arrays;
import java.util.Optional;
import java.util.Random;

import javax.activation.DataHandler;
import javax.xml.bind.JAXBElement;
import org.junit.Test;
import org.xml.sax.SAXParseException;

import de.sebikopp.jaxb.mtom.test.stubs.ImgListCType;
import de.sebikopp.jaxb.mtom.test.stubs.ImgWithMetadataCType;
import de.sebikopp.jaxb.mtom.test.stubs.MetadataSetCType;
import de.sebikopp.jaxb.mtom.test.stubs.ObjectFactory;
import de.sebikopp.jaxb.mtom.test.stubs.SupportedImgFiletypeSType;

public class JaxbMtomTest {
	@Test
	public void test1() throws Exception {
		ObjectFactory of = new ObjectFactory();
		ImgListCType lst = createTestData();
		JAXBElement<ImgListCType> imageList = of.createImageList(lst);
		JaxbMtomConverter jaxbMtomConverter = new JaxbMtomConverter();
		final Optional<String> optP2schema = Optional.of("xsd/imagesWithMetainfos.xsd");
		JaxbMtomContainer container = jaxbMtomConverter.encodeDataFromJaxbElem(imageList, optP2schema);
		System.out.println(container);
		jaxbMtomConverter.unmarshal(container, ImgListCType.class, optP2schema);
	}
	@Test(expected=SAXParseException.class)
	public void testInvalid() throws Exception {
		ObjectFactory of = new ObjectFactory();
		ImgListCType lst = createTestData();
		lst.getImage().get(0).setId("");
		JAXBElement<ImgListCType> imageList = of.createImageList(lst);
		JaxbMtomConverter jaxbMtomConverter = new JaxbMtomConverter();
		final Optional<String> optP2schema = Optional.of("xsd/imagesWithMetainfos.xsd");
		JaxbMtomContainer container = jaxbMtomConverter.encodeDataFromJaxbElem(imageList, optP2schema);
		jaxbMtomConverter.unmarshal(container, ImgListCType.class, optP2schema);
	}
	@Test(expected=SAXParseException.class)
	public void testUmarshInvalid() throws Exception {
		ObjectFactory of = new ObjectFactory();
		ImgListCType lst = createTestData();
		lst.getImage().get(0).setId("");
		JAXBElement<ImgListCType> imageList = of.createImageList(lst);
		JaxbMtomConverter jaxbMtomConverter = new JaxbMtomConverter();
		final Optional<String> optP2schema = Optional.of("xsd/imagesWithMetainfos.xsd");
		JaxbMtomContainer container = jaxbMtomConverter.encodeDataFromJaxbElem(imageList, Optional.empty());
		jaxbMtomConverter.unmarshal(container, ImgListCType.class, optP2schema);
	}
	
	private ImgListCType createTestData() {
		final byte[] img1 = createRandomData(4096);
		final byte[] img2 = createRandomData(54545);
		ImgListCType lst = new ImgListCType();
		ImgWithMetadataCType bild1 = new ImgWithMetadataCType();
		bild1.setContent(new DataHandler(new InputStreamDataSource(img1)));
		bild1.setId("b1");
		bild1.setFileType(SupportedImgFiletypeSType.JPEG);
		bild1.setMetadata(new MetadataSetCType());
		ImgWithMetadataCType bild2 = new ImgWithMetadataCType();
		bild2.setContent(new DataHandler(new InputStreamDataSource(img2)));
		bild2.setId("b2");
		bild2.setMetadata(new MetadataSetCType());
		bild2.setFileType(SupportedImgFiletypeSType.PNG);
		lst.getImage().addAll(Arrays.asList(bild1, bild2));
		return lst;
	}
	
	private byte[] createRandomData(final int size) {
		byte [] rc = new byte[size];
		new Random().nextBytes(rc);
		return rc;
	}
}
