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

package fr.ens.transcriptome.nividic.om;

import java.applet.Applet;

import fr.ens.transcriptome.nividic.om.impl.BioAssayImpl;
import fr.ens.transcriptome.nividic.om.impl.GenepixBioAssayImpl;

/**
 * This allow to create instances of concrete classes of BioAssay interface.
 * @author Laurent Jourdren
 */
public final class BioAssayFactory {

  /**
   * Create a BioAssay Object.
   * @return A new BioAssay Object
   */
  public static BioAssay createBioAssay() {
    return new BioAssayImpl();
  }

  /**
   * Create a BioAssay Object.
   * @param applet Applet used to load Genepix data
   * @return A new BioAssay Object
   */
  public static BioAssay createBioAssay(final Applet applet) {
    return new GenepixBioAssayImpl(applet);
  }

  //
  // Constructor
  //

  /**
   * Private constructor.
   */
  private BioAssayFactory() {
  }

}