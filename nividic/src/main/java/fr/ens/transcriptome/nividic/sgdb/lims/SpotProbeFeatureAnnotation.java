/*
 *                      Nividic development code
 *
 * This code may be freely distributed and modified under the
 * terms of the GNU Lesser General Public Licence.  This should
 * be distributed with the code.  If you do not have a copy,
 * see:
 *
 *      http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright for this code is held jointly by the microarray platform
 * of the École Normale Supérieure and the individual authors.
 * These should be listed in @author doc comments.
 *
 * For more information on the Nividic project and its aims,
 * or to join the Nividic mailing list, visit the home page
 * at:
 *
 *      http://www.transcriptome.ens.fr/nividic
 *
 */

package fr.ens.transcriptome.nividic.sgdb.lims;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.primitives.ArrayIntList;
import org.apache.commons.collections.primitives.IntList;

import fr.ens.transcriptome.nividic.NividicRuntimeException;
import fr.ens.transcriptome.nividic.sgdb.lims.ws.LimsPort_PortType;
import fr.ens.transcriptome.nividic.sgdb.lims.ws.SpotProbe;
import fr.ens.transcriptome.nividic.util.StringUtils;

/**
 * This class define how to retrieve probe id from the spot id.
 * @author Laurent Jourdren
 */
public class SpotProbeFeatureAnnotation extends LimsBasicFeatureAnnotation {

  /** Prode Id field annotation. */
  public static final String PROBE_ID_FIELD = "ProbeId";

  //
  // Methods from Translator
  //

  /**
   * Get an ordered list of the annotations fields
   * @return an ordered list of the annotations fields.
   */
  public String[] getFields() {

    return new String[] {PROBE_ID_FIELD};
  }

  /**
   * Get all the annotation for an feature
   * @param id Identifier of the feature
   * @return An array with the annotation of the Feature
   */
  public String[] translate(final String id) {

    if (id == null)
      return null;

    String result = retrieveProbeId(id);

    if (result == null)
      return null;

    return new String[] {result};
  }

  /**
   * Get an annotation for an feature
   * @param id Identifier of the feature
   * @param field the field to get
   * @return An array with the annotation of the Feature
   */
  public String translateField(final String id, final String field) {

    if (id == null || !PROBE_ID_FIELD.equals(field))
      return null;

    return retrieveProbeId(id);
  }

  /**
   * Get annotations for features
   * @param ids Identifiers of the features
   * @param field the field to get
   * @return An array with the annotation of the Feature
   */
  public String[] getAnnotations(final String[] ids, final String field) {

    if (ids == null || !PROBE_ID_FIELD.equals(field))
      return null;

    return retrieveProbeIds(ids);
  }

  /**
   * Get all the annotations for features
   * @param ids Identifiers of the features
   * @return An array with the annotation of the Feature
   */
  public String[][] translate(final String[] ids) {

    if (ids == null)
      return null;

    String[] annots = retrieveProbeIds(ids);

    String[][] result = new String[ids.length][];

    for (int i = 0; i < result.length; i++)
      result[i] = new String[] {annots[i]};

    return null;
  }

  //
  // Other methods
  //

  /**
   * Retrieve the data.
   */
  private String retrieveProbeId(final String spotId) {

    if (spotId == null)
      return null;

    int id = Integer.parseInt(spotId.trim());

    try {
      LimsPort_PortType port = getPort();

      return "" + port.getSpotProbe(id);

    } catch (RemoteException e) {
      throw new NividicRuntimeException("Error while connection to the lims");
    }

  }

  /**
   * Retrieve the data.
   */
  private String[] retrieveProbeIds(final String[] spotIds) {

    int[] ids = StringUtils.arrayStringToArrayInt(spotIds);

    if (ids == null)
      return null;

    String[] result;

    try {
      LimsPort_PortType port = getPort();

      SpotProbe[] probes = port.getSpotsProbes(ids);

      System.out.println("probes: " + probes);

      if (probes == null)
        return null;

      Map map = new HashMap();

      for (int i = 0; i < probes.length; i++)
        map.put("" + probes[i].getSpotId(), "" + probes[i].getProbeId());

      result = new String[spotIds.length];
      for (int i = 0; i < spotIds.length; i++)
        result[i] = (String) map.get(spotIds[i]);

    } catch (RemoteException e) {
      System.err.println(e.getMessage());
      e.printStackTrace();

      throw new NividicRuntimeException("Error while connection to the lims");
    }

    return result;
  }

  private static SpotProbe getSpotProbes(int[] spotIds) {

    if (spotIds == null)
      return null;
    
    ArrayIntList ail = new ArrayIntList();
    ArrayList allProbes = new ArrayList();

    for (int i = 0; i < spotIds.length; i++)
      ail.add(spotIds[i]);

    for (int i = 0; i < spotIds.length; i = i + 500) {
      IntList subList = ail.subList(i, i + 500);

      //SpotProbe [] probes =  port.getSpotsProbes(subList.toArray());
      
      //
      // ERROR
      //
      
    }

      return null;
  }

  //
  // Constructor
  //

  /**
   * Public constructor.
   * @param limsConnection connection to the lims
   */
  public SpotProbeFeatureAnnotation(final LimsConnection limsConnection) {
    setLimsConnection(limsConnection);
  }

  /**
   * Public constructor.
   */
  public SpotProbeFeatureAnnotation() {
    this(null);
  }

}
