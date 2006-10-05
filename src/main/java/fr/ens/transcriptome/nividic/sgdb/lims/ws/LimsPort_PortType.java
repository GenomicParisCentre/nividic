/**
 * LimsPort_PortType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Jun 14, 2005 (09:15:57 EDT) WSDL2Java emitter.
 */

package fr.ens.transcriptome.nividic.sgdb.lims.ws;

public interface LimsPort_PortType extends java.rmi.Remote {
    public java.lang.String getSpotDescription(int spotId) throws java.rmi.RemoteException;
    public fr.ens.transcriptome.nividic.sgdb.lims.ws.SpotDescription[] getSpotsDescriptions(int[] spotsIds) throws java.rmi.RemoteException;
    public int getSpotProbe(int spotId) throws java.rmi.RemoteException;
    public fr.ens.transcriptome.nividic.sgdb.lims.ws.SpotProbe[] getSpotsProbes(int[] spotsIds) throws java.rmi.RemoteException;
    public fr.ens.transcriptome.nividic.sgdb.lims.ws.ProbeDescription getProbeDescription(int probeId) throws java.rmi.RemoteException;
    public fr.ens.transcriptome.nividic.sgdb.lims.ws.ProbeDescription[] getProbesDescriptions(int[] probesIds) throws java.rmi.RemoteException;
    public fr.ens.transcriptome.nividic.sgdb.lims.ws.SlideDescription getSlideDescription(java.lang.String serialNumber) throws java.rmi.RemoteException;
    public fr.ens.transcriptome.nividic.sgdb.lims.ws.SlideDescription[] getSlidesDescriptions(java.lang.String[] serialNumbers) throws java.rmi.RemoteException;
    public java.lang.String getArrayListFile(int listId) throws java.rmi.RemoteException;
    public java.lang.String getResultFile(int resultId) throws java.rmi.RemoteException;
}
