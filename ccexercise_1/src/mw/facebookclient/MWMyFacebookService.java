
package mw.facebookclient;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.Action;
import javax.xml.ws.FaultAction;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.9-b130926.1035
 * Generated source version: 2.2
 * 
 */
@WebService(name = "MWMyFacebookService", targetNamespace = "http://facebook.mw/")
@SOAPBinding(style = SOAPBinding.Style.RPC)
@XmlSeeAlso({
    ObjectFactory.class
})
public interface MWMyFacebookService {


    /**
     * 
     * @param arg0
     * @return
     *     returns java.lang.String
     * @throws MWUnknownIDException_Exception
     */
    @WebMethod
    @WebResult(partName = "return")
    @Action(input = "http://facebook.mw/MWMyFacebookService/getNameRequest", output = "http://facebook.mw/MWMyFacebookService/getNameResponse", fault = {
        @FaultAction(className = MWUnknownIDException_Exception.class, value = "http://facebook.mw/MWMyFacebookService/getName/Fault/MWUnknownIDException")
    })
    public String getName(
        @WebParam(name = "arg0", partName = "arg0")
        String arg0)
        throws MWUnknownIDException_Exception
    ;

    /**
     * 
     * @param arg0
     * @return
     *     returns mw.client.StringArray
     * @throws MWUnknownIDException_Exception
     */
    @WebMethod
    @WebResult(partName = "return")
    @Action(input = "http://facebook.mw/MWMyFacebookService/getFriendsRequest", output = "http://facebook.mw/MWMyFacebookService/getFriendsResponse", fault = {
        @FaultAction(className = MWUnknownIDException_Exception.class, value = "http://facebook.mw/MWMyFacebookService/getFriends/Fault/MWUnknownIDException")
    })
    public StringArray getFriends(
        @WebParam(name = "arg0", partName = "arg0")
        String arg0)
        throws MWUnknownIDException_Exception
    ;

    /**
     * 
     * @param arg0
     * @return
     *     returns mw.client.StringArrayArray
     * @throws MWUnknownIDException_Exception
     */
    @WebMethod
    @WebResult(partName = "return")
    @Action(input = "http://facebook.mw/MWMyFacebookService/getFriendsBatchRequest", output = "http://facebook.mw/MWMyFacebookService/getFriendsBatchResponse", fault = {
        @FaultAction(className = MWUnknownIDException_Exception.class, value = "http://facebook.mw/MWMyFacebookService/getFriendsBatch/Fault/MWUnknownIDException")
    })
    public StringArrayArray getFriendsBatch(
        @WebParam(name = "arg0", partName = "arg0")
        StringArray arg0)
        throws MWUnknownIDException_Exception
    ;

    /**
     * 
     * @param arg0
     * @return
     *     returns mw.client.StringArray
     */
    @WebMethod
    @WebResult(partName = "return")
    @Action(input = "http://facebook.mw/MWMyFacebookService/searchIDsRequest", output = "http://facebook.mw/MWMyFacebookService/searchIDsResponse")
    public StringArray searchIDs(
        @WebParam(name = "arg0", partName = "arg0")
        String arg0);

    /**
     * 
     * @param arg0
     * @return
     *     returns int
     */
    @WebMethod
    @WebResult(partName = "return")
    @Action(input = "http://facebook.mw/MWMyFacebookService/getServerStatusRequest", output = "http://facebook.mw/MWMyFacebookService/getServerStatusResponse")
    public int getServerStatus(
        @WebParam(name = "arg0", partName = "arg0")
        int arg0);

}
