
package mw.pathclient;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the mw.pathclient package. 
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

    private final static QName _MWNoPathException_QNAME = new QName("http://path.mw/", "MWNoPathException");
    private final static QName _JAXBException_QNAME = new QName("http://path.mw/", "JAXBException");
    private final static QName _MWNoSuchKeyException_QNAME = new QName("http://path.mw/", "MWNoSuchKeyException");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: mw.pathclient
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link StringArray }
     * 
     */
    public StringArray createStringArray() {
        return new StringArray();
    }

    /**
     * Create an instance of {@link MWNoPathException }
     * 
     */
    public MWNoPathException createMWNoPathException() {
        return new MWNoPathException();
    }

    /**
     * Create an instance of {@link JAXBException }
     * 
     */
    public JAXBException createJAXBException() {
        return new JAXBException();
    }

    /**
     * Create an instance of {@link MWNoSuchKeyException }
     * 
     */
    public MWNoSuchKeyException createMWNoSuchKeyException() {
        return new MWNoSuchKeyException();
    }

    /**
     * Create an instance of {@link Throwable }
     * 
     */
    public Throwable createThrowable() {
        return new Throwable();
    }

    /**
     * Create an instance of {@link StackTraceElement }
     * 
     */
    public StackTraceElement createStackTraceElement() {
        return new StackTraceElement();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MWNoPathException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://path.mw/", name = "MWNoPathException")
    public JAXBElement<MWNoPathException> createMWNoPathException(MWNoPathException value) {
        return new JAXBElement<MWNoPathException>(_MWNoPathException_QNAME, MWNoPathException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link JAXBException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://path.mw/", name = "JAXBException")
    public JAXBElement<JAXBException> createJAXBException(JAXBException value) {
        return new JAXBElement<JAXBException>(_JAXBException_QNAME, JAXBException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MWNoSuchKeyException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://path.mw/", name = "MWNoSuchKeyException")
    public JAXBElement<MWNoSuchKeyException> createMWNoSuchKeyException(MWNoSuchKeyException value) {
        return new JAXBElement<MWNoSuchKeyException>(_MWNoSuchKeyException_QNAME, MWNoSuchKeyException.class, null, value);
    }

}
