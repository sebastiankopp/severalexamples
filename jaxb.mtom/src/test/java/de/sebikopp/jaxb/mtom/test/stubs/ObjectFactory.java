//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2017.08.19 um 10:13:54 PM CEST 
//


package de.sebikopp.jaxb.mtom.test.stubs;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the de.sebikopp.jaxb.mtom.test.stubs package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _ImageList_QNAME = new QName("", "ImageList");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: de.sebikopp.jaxb.mtom.test.stubs
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ImgListCType }
     * 
     */
    public ImgListCType createImgListCType() {
        return new ImgListCType();
    }

    /**
     * Create an instance of {@link KeyValuePairCType }
     * 
     */
    public KeyValuePairCType createKeyValuePairCType() {
        return new KeyValuePairCType();
    }

    /**
     * Create an instance of {@link MetadataSetCType }
     * 
     */
    public MetadataSetCType createMetadataSetCType() {
        return new MetadataSetCType();
    }

    /**
     * Create an instance of {@link ImgWithMetadataCType }
     * 
     */
    public ImgWithMetadataCType createImgWithMetadataCType() {
        return new ImgWithMetadataCType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ImgListCType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "ImageList")
    public JAXBElement<ImgListCType> createImageList(ImgListCType value) {
        return new JAXBElement<ImgListCType>(_ImageList_QNAME, ImgListCType.class, null, value);
    }

}
