/**
 * LimsService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package fr.ens.transcriptome.nividic.sgdb.lims.ws;

public interface LimsService extends javax.xml.rpc.Service {
    public java.lang.String getLimsPortAddress();

    public fr.ens.transcriptome.nividic.sgdb.lims.ws.LimsPort getLimsPort() throws javax.xml.rpc.ServiceException;

    public fr.ens.transcriptome.nividic.sgdb.lims.ws.LimsPort getLimsPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
