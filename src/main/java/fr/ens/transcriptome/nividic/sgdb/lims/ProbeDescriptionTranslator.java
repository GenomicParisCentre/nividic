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

import fr.ens.transcriptome.nividic.NividicRuntimeException;
import fr.ens.transcriptome.nividic.sgdb.lims.ws.LimsPort_PortType;
import fr.ens.transcriptome.nividic.sgdb.lims.ws.ProbeDescription;
import fr.ens.transcriptome.nividic.util.StringUtils;

/**
 * This class define how to retrieve probe id from the spot id.
 * @author Laurent Jourdren
 */
public class ProbeDescriptionTranslator extends
    LimsBasicTranslator {

  /** Probe name field. */
  public static final String PROBE_NAME_FIELD = "ProbeName";
  /** Probe description field. */
  public static final String PROBE_DESCRIPTION_FIELD = "ProbeDescription";
  /** Organism identifer field. */
  public static final String ORGANISM_ID_FIELD = "OrganismId";
  /** Control field. */
  public static final String CONTROL_FIELD = "Control";

  //
  // Methods from Translator
  //

  /**
   * Get an ordered list of the annotations fields
   * @return an ordered list of the annotations fields.
   */
  public String[] getFields() {

    return new String[] {PROBE_NAME_FIELD, PROBE_DESCRIPTION_FIELD,
        ORGANISM_ID_FIELD, CONTROL_FIELD};
  }

  /**
   * Get all the annotation for an feature
   * @param probeId Identifier of the feature
   * @return An array with the annotation of the Feature
   */
  public String[] translate(final String probeId) {

    if (probeId == null)
      return null;

    return probeDescriptionToStrings(retrieveProbeDescription(probeId));
  }

  /**
   * Get an annotation for an feature
   * @param probeId Identifier of the feature
   * @param field the field to get
   * @return An array with the annotation of the Feature
   */
  public String translateField(final String probeId, final String field) {

    if (probeId == null || !isField(field))
      return null;

    return probeDescriptionToString(retrieveProbeDescription(probeId), field);
  }

  /**
   * Get annotations for features
   * @param ids Identifiers of the features
   * @param field the field to get
   * @return An array with the annotation of the Feature
   */
  public String[] getAnnotations(final String[] ids, final String field) {

    if (ids == null || !isField(field))
      return null;

    ProbeDescription[] descs = retrieveProbeDescription(ids);

    String[] result = new String[ids.length];

    for (int i = 0; i < result.length; i++)
      result[i] = probeDescriptionToString(descs[i], field);

    return result;
  }

  /**
   * Get all the annotations for features
   * @param ids Identifiers of the features
   * @return An array with the annotation of the Feature
   */
  public String[][] translate(final String[] ids) {

    if (ids == null)
      return null;

    ProbeDescription[] descs = retrieveProbeDescription(ids);

    String[][] result = new String[ids.length][];

    for (int i = 0; i < result.length; i++)
      result[i] = probeDescriptionToStrings(descs[i]);

    return result;
  }

  //
  // Other methods
  //

  /**
   * Retrieve the data.
   */
  private ProbeDescription retrieveProbeDescription(final String spotId) {

    if (spotId == null)
      return null;

    int id = Integer.parseInt(spotId.trim());

    try {
      LimsPort_PortType port = getPort();

      return port.getProbeDescription(id);

    } catch (RemoteException e) {
      throw new NividicRuntimeException("Error while connection to the lims");
    }

  }

  /**
   * Retrieve the data.
   */
  private ProbeDescription[] retrieveProbeDescription(final String[] probeIds) {

    int[] ids = StringUtils.arrayStringToArrayInt(probeIds);

    if (ids == null)
      return null;

    try {
      LimsPort_PortType port = getPort();

      return port.getProbesDescriptions(ids);

    } catch (RemoteException e) {
      throw new NividicRuntimeException("Error while connection to the lims");
    }
  }

  private static String probeDescriptionToString(final ProbeDescription desc,
      final String field) {

    if (desc == null)
      return null;

    if (PROBE_NAME_FIELD.equals(field))
      return desc.getProbeName();
    if (PROBE_DESCRIPTION_FIELD.equals(field))
      return desc.getProbeDescription();
    if (ORGANISM_ID_FIELD.equals(field))
      return "" + desc.getOrganismId();
    if (CONTROL_FIELD.equals(field))
      return "" + desc.isControl();

    return null;
  }

  private static String[] probeDescriptionToStrings(final ProbeDescription desc) {

    if (desc == null)
      return null;

    return new String[] {desc.getProbeName(), desc.getProbeDescription(),
        "" + desc.getOrganismId(), "" + desc.isControl()};
  }

  //
  // Constructor
  //

  /**
   * Public constructor.
   * @param limsConnection connection to the lims
   */
  public ProbeDescriptionTranslator(final LimsConnection limsConnection) {
    setLimsConnection(limsConnection);
  }

  /**
   * Public constructor.
   */
  public ProbeDescriptionTranslator() {
    this(null);
  }

}
