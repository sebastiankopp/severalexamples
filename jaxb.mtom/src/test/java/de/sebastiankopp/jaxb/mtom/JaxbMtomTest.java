package de.sebastiankopp.jaxb.mtom;

import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;
import java.util.Random;
import javax.activation.DataHandler;
import javax.xml.bind.JAXBElement;
import javax.xml.validation.Schema;

import de.sebastiankopp.jaxb.mtom.test.stubs.*;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.xml.sax.SAXParseException;


public class JaxbMtomTest {
	
	@BeforeClass
	public void init() {
		Locale.setDefault(Locale.ENGLISH);
	}
	
	@Test
	public void test1() throws Exception {
		ObjectFactory of = new ObjectFactory();
		ImgListCType lst = createTestData();
		JAXBElement<ImgListCType> imageList = of.createImageList(lst);
		JaxbMtomConverter jaxbMtomConverter = new JaxbMtomConverter();
		final Optional<Schema> optSchema = Optional.of("xsd/imagesWithMetainfos.xsd")
				.map(MtomValidator::schemaFromCpPath);
		JaxbMtomContainer container = jaxbMtomConverter.encodeDataFromJaxbElem(imageList, optSchema);
		System.out.println(container);
		jaxbMtomConverter.unmarshal(container, ImgListCType.class, optSchema);
	}
	
	// Note: Current Jakarta XML Bind Impl does not seem to keep message of nested validation exception
	@Test(expectedExceptions = SAXParseException.class /*, expectedExceptionsMessageRegExp = ".*length.*0.*not.*valid.*" */)
	public void testInvalid() throws Exception {
		ObjectFactory of = new ObjectFactory();
		ImgListCType lst = createTestData();
		lst.getImage().get(0).setId("");
		JAXBElement<ImgListCType> imageList = of.createImageList(lst);
		JaxbMtomConverter jaxbMtomConverter = new JaxbMtomConverter();
		final Optional<Schema> optSchema = Optional.of("xsd/imagesWithMetainfos.xsd").map(MtomValidator::schemaFromCpPath);
		JaxbMtomContainer container = jaxbMtomConverter.encodeDataFromJaxbElem(imageList, optSchema);
		jaxbMtomConverter.unmarshal(container, ImgListCType.class, optSchema);
	}
	
	@Test
	public void testInvalidWithoutValidation() throws Exception {
		ObjectFactory of = new ObjectFactory();
		ImgListCType lst = createTestData();
		lst.getImage().get(0).setId("");
		JAXBElement<ImgListCType> imageList = of.createImageList(lst);
		JaxbMtomConverter jaxbMtomConverter = new JaxbMtomConverter();
		final Optional<Schema> optSchema = Optional.empty();
		JaxbMtomContainer container = jaxbMtomConverter.encodeDataFromJaxbElem(imageList, optSchema);
		jaxbMtomConverter.unmarshal(container, ImgListCType.class, optSchema);
	}
	
	@Test(expectedExceptions = SAXParseException.class /*, expectedExceptionsMessageRegExp = ".*length.*0.*not.*valid.*"*/)
	public void testUmarshInvalid() throws Exception {
		ObjectFactory of = new ObjectFactory();
		ImgListCType lst = createTestData();
		lst.getImage().get(0).setId("");
		JAXBElement<ImgListCType> imageList = of.createImageList(lst);
		JaxbMtomConverter jaxbMtomConverter = new JaxbMtomConverter();
		final Optional<Schema> optSchema = Optional.of("xsd/imagesWithMetainfos.xsd")
				.map(MtomValidator::schemaFromCpPath);
		JaxbMtomContainer container = jaxbMtomConverter.encodeDataFromJaxbElem(imageList, Optional.empty());
		System.out.println("Marshalling succeessful without additional JAXB source validation");
		jaxbMtomConverter.unmarshal(container, ImgListCType.class, optSchema);
	}
	@Test
	public void testUniqueWOValidation() throws Exception {
		ObjectFactory of = new ObjectFactory();
		ImgListCType lst = createTestData();
		lst.getImage().forEach(x -> x.setId("IDID"));
		JAXBElement<ImgListCType> imageList = of.createImageList(lst);
		JaxbMtomConverter converter = new JaxbMtomConverter();
		JaxbMtomContainer container = converter.encodeDataFromJaxbElem(imageList, Optional.empty());
		converter.unmarshal(container, ImgListCType.class, Optional.empty());
	}
	
	@Test(expectedExceptions = SAXParseException.class /*, expectedExceptionsMessageRegExp = ".*duplicate unique value.*" */)
	public void testUniqueWithValidation() throws Exception {
		final Optional<Schema> schema = Optional.of("xsd/imagesWithMetainfos.xsd")
				.map(MtomValidator::schemaFromCpPath);
		ObjectFactory of = new ObjectFactory();
		ImgListCType lst = createTestData();
		lst.getImage().forEach(x -> x.setId("IDID"));
		JAXBElement<ImgListCType> imageList = of.createImageList(lst);
		JaxbMtomConverter converter = new JaxbMtomConverter();
		JaxbMtomContainer container = converter.encodeDataFromJaxbElem(imageList, schema);
		converter.unmarshal(container, ImgListCType.class, schema);
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
