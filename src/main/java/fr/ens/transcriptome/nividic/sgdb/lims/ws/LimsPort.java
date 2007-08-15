/**
 * LimsPort.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package fr.ens.transcriptome.nividic.sgdb.lims.ws;

public interface LimsPort extends java.rmi.Remote {
    public fr.ens.transcriptome.nividic.sgdb.lims.ws.Hybridization getHybridization(java.lang.String slideSerialNumber) throws java.rmi.RemoteException;
    public fr.ens.transcriptome.nividic.sgdb.lims.ws.Target[] getTargets(int hybridizationId) throws java.rmi.RemoteException;
    public fr.ens.transcriptome.nividic.sgdb.lims.ws.Scan[] getScans(int hybridizationId) throws java.rmi.RemoteException;
}
