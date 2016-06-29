//
// 此文件是由 JavaTM Architecture for XML Binding (JAXB) 引用实现 v2.2.8-b130911.1802 生成的
// 请访问 <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// 在重新编译源模式时, 对此文件的所有修改都将丢失。
// 生成时间: 2016.06.02 时间 01:04:10 PM CEST 
//


package mw.cache.generated;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the mw.cache.generated package. 
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

    private final static QName _MWCacheRequest_QNAME = new QName("", "MWCacheRequest");
    private final static QName _MWCacheReply_QNAME = new QName("", "MWCacheReply");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: mw.cache.generated
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link MWMessage }
     * 
     */
    public MWMessage createMWMessage() {
        return new MWMessage();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MWMessage }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "MWCacheRequest")
    public JAXBElement<MWMessage> createMWCacheRequest(MWMessage value) {
        return new JAXBElement<MWMessage>(_MWCacheRequest_QNAME, MWMessage.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MWMessage }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "MWCacheReply")
    public JAXBElement<MWMessage> createMWCacheReply(MWMessage value) {
        return new JAXBElement<MWMessage>(_MWCacheReply_QNAME, MWMessage.class, null, value);
    }

}
