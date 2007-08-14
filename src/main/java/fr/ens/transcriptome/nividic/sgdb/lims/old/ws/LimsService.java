/**
 * LimsService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Jun 14, 2005 (09:15:57 EDT) WSDL2Java emitter.
 */

package fr.ens.transcriptome.nividic.sgdb.lims.old.ws;

public interface LimsService extends javax.xml.rpc.Service {
    public java.lang.String getLimsPortAddress();

    public fr.ens.transcriptome.nividic.sgdb.lims.old.ws.LimsPort_PortType getLimsPort() throws javax.xml.rpc.ServiceException;

    public fr.ens.transcriptome.nividic.sgdb.lims.old.ws.LimsPort_PortType getLimsPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
