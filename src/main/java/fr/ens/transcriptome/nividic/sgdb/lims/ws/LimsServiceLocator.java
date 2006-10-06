/**
 * LimsServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Jun 14, 2005 (09:15:57 EDT) WSDL2Java emitter.
 */

package fr.ens.transcriptome.nividic.sgdb.lims.ws;

public class LimsServiceLocator extends org.apache.axis.client.Service implements fr.ens.transcriptome.nividic.sgdb.lims.ws.LimsService {

    public LimsServiceLocator() {
    }


    public LimsServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public LimsServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for LimsPort
    private java.lang.String LimsPort_address = "http://www.genomic.ens.fr/lims/webservices/lims.php";

    public java.lang.String getLimsPortAddress() {
        return LimsPort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String LimsPortWSDDServiceName = "LimsPort";

    public java.lang.String getLimsPortWSDDServiceName() {
        return LimsPortWSDDServiceName;
    }

    public void setLimsPortWSDDServiceName(java.lang.String name) {
        LimsPortWSDDServiceName = name;
    }

    public fr.ens.transcriptome.nividic.sgdb.lims.ws.LimsPort_PortType getLimsPort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(LimsPort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getLimsPort(endpoint);
    }

    public fr.ens.transcriptome.nividic.sgdb.lims.ws.LimsPort_PortType getLimsPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            fr.ens.transcriptome.nividic.sgdb.lims.ws.LimsBindingStub _stub = new fr.ens.transcriptome.nividic.sgdb.lims.ws.LimsBindingStub(portAddress, this);
            _stub.setPortName(getLimsPortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setLimsPortEndpointAddress(java.lang.String address) {
        LimsPort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (fr.ens.transcriptome.nividic.sgdb.lims.ws.LimsPort_PortType.class.isAssignableFrom(serviceEndpointInterface)) {
                fr.ens.transcriptome.nividic.sgdb.lims.ws.LimsBindingStub _stub = new fr.ens.transcriptome.nividic.sgdb.lims.ws.LimsBindingStub(new java.net.URL(LimsPort_address), this);
                _stub.setPortName(getLimsPortWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("LimsPort".equals(inputPortName)) {
            return getLimsPort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("urn:Lims", "LimsService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("urn:Lims", "LimsPort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("LimsPort".equals(portName)) {
            setLimsPortEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
