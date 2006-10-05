package fr.ens.transcriptome.nividic.sgdb.lims;

import org.apache.axis.message.SOAPHeaderElement;
import org.apache.log4j.BasicConfigurator;


import fr.ens.transcriptome.nividic.sgdb.lims.ws.LimsBindingStub;
import fr.ens.transcriptome.nividic.sgdb.lims.ws.LimsService;
import fr.ens.transcriptome.nividic.sgdb.lims.ws.LimsServiceLocator;
import fr.ens.transcriptome.nividic.sgdb.lims.ws.ProbeDescription;
import fr.ens.transcriptome.nividic.sgdb.lims.ws.SpotProbe;

class Test {
  public static void main(String[] args) throws Exception {

    BasicConfigurator.configure();

    /*
     * try { // Make a service LimsService service = new LimsServiceLocator(); //
     * Now use the service to get a stub which implements the SDI.
     * LimsPort_PortType port = service.getLimsPort(); String data =
     * port.getArrayListFile(11); System.out.println("File: ");
     * System.out.println(data); } catch (SOAPFaultException e) {
     * System.err.println(e.getMessage()); }
     */

    /*
     * GPRLimsReader glr = new GPRLimsReader(5); //GALLimsReader glr = new
     * GALLimsReader(5); BioAssay ba = glr.read(); Annotation annot =
     * ba.getAnnotation(); Iterator it = annot.iterator(); while (it.hasNext()) {
     * String key = (String) it.next(); System.out.println(key + ": " +
     * annot.getProperty(key)); }
     */

    // Make a service
    LimsService service = new LimsServiceLocator();

    // Now use the service to get a stub which implements the SDI.

    LimsBindingStub port = (LimsBindingStub) service.getLimsPort();

    // port.setUsername("ljourdre");
    // port.setPassword("ey42pecQ");

    /*
     * final ProbeDescription probe = port.getProbeDescription(7777);
     * System.out.println("probe: " + probe.getProbeId() + " (" +
     * probe.getProbeDescription() + ")");
     */

    
    
    SpotProbeFeatureAnnotation pfa = new SpotProbeFeatureAnnotation();

    // System.out.println(pfa.getAnnotation("7777",
    // pfa.getAnnotationFields()[0]));

    String[] results = pfa.getAnnotations(new String[] {"7777"}, pfa
        .getFields()[0]);

    // SpotProbe [] results = port.getSpotsProbes(new int[] {7777, -1, 8888});

    if (results != null)
      for (int i = 0; i < results.length; i++) {
        System.out.println("i: " + results[i]);
      }

    System.out.println("end.");
  }

}
