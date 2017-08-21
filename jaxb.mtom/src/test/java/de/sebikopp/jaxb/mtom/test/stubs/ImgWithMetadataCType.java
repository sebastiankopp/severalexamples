//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2017.08.19 um 10:13:54 PM CEST 
//


package de.sebikopp.jaxb.mtom.test.stubs;

import javax.activation.DataHandler;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlMimeType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für ImgWithMetadataCType complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="ImgWithMetadataCType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Content" type="{http://www.w3.org/2001/XMLSchema}base64Binary"/>
 *         &lt;element name="Metadata" type="{}MetadataSetCType"/>
 *       &lt;/sequence>
 *       &lt;attribute name="id" use="required" type="{}NonEmptyStringSType" />
 *       &lt;attribute name="fileType" use="required" type="{}SupportedImgFiletypeSType" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ImgWithMetadataCType", propOrder = {
    "content",
    "metadata"
})
public class ImgWithMetadataCType {

    @XmlElement(name = "Content", required = true)
    @XmlMimeType("application/octet-stream")
    protected DataHandler content;
    @XmlElement(name = "Metadata", required = true)
    protected MetadataSetCType metadata;
    @XmlAttribute(name = "id", required = true)
    protected String id;
    @XmlAttribute(name = "fileType", required = true)
    protected SupportedImgFiletypeSType fileType;

    /**
     * Ruft den Wert der content-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link DataHandler }
     *     
     */
    public DataHandler getContent() {
        return content;
    }

    /**
     * Legt den Wert der content-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link DataHandler }
     *     
     */
    public void setContent(DataHandler value) {
        this.content = value;
    }

    /**
     * Ruft den Wert der metadata-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link MetadataSetCType }
     *     
     */
    public MetadataSetCType getMetadata() {
        return metadata;
    }

    /**
     * Legt den Wert der metadata-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link MetadataSetCType }
     *     
     */
    public void setMetadata(MetadataSetCType value) {
        this.metadata = value;
    }

    /**
     * Ruft den Wert der id-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * Legt den Wert der id-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
    }

    /**
     * Ruft den Wert der fileType-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link SupportedImgFiletypeSType }
     *     
     */
    public SupportedImgFiletypeSType getFileType() {
        return fileType;
    }

    /**
     * Legt den Wert der fileType-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link SupportedImgFiletypeSType }
     *     
     */
    public void setFileType(SupportedImgFiletypeSType value) {
        this.fileType = value;
    }

}
