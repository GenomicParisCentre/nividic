package fr.ens.transcriptome.nividic.sgdb.lims.ws;

public class LimsPortProxy implements fr.ens.transcriptome.nividic.sgdb.lims.ws.LimsPort {
  private String _endpoint = null;
  private fr.ens.transcriptome.nividic.sgdb.lims.ws.LimsPort limsPort = null;
  
  public LimsPortProxy() {
    _initLimsPortProxy();
  }
  
  public LimsPortProxy(String endpoint) {
    _endpoint = endpoint;
    _initLimsPortProxy();
  }
  
  private void _initLimsPortProxy() {
    try {
      limsPort = (new fr.ens.transcriptome.nividic.sgdb.lims.ws.LimsServiceLocator()).getLimsPort();
      if (limsPort != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)limsPort)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)limsPort)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (limsPort != null)
      ((javax.xml.rpc.Stub)limsPort)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public fr.ens.transcriptome.nividic.sgdb.lims.ws.LimsPort getLimsPort() {
    if (limsPort == null)
      _initLimsPortProxy();
    return limsPort;
  }
  
  public fr.ens.transcriptome.nividic.sgdb.lims.ws.Hybridization getHybridization(java.lang.String slideSerialNumber) throws java.rmi.RemoteException{
    if (limsPort == null)
      _initLimsPortProxy();
    return limsPort.getHybridization(slideSerialNumber);
  }
  
  public fr.ens.transcriptome.nividic.sgdb.lims.ws.Target[] getTargets(int hybridizationId) throws java.rmi.RemoteException{
    if (limsPort == null)
      _initLimsPortProxy();
    return limsPort.getTargets(hybridizationId);
  }
  
  public fr.ens.transcriptome.nividic.sgdb.lims.ws.Scan[] getScans(int hybridizationId) throws java.rmi.RemoteException{
    if (limsPort == null)
      _initLimsPortProxy();
    return limsPort.getScans(hybridizationId);
  }
  
  
}