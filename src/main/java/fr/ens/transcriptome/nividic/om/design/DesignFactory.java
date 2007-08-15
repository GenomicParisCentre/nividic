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
 * of the �cole Normale Sup�rieure and the individual authors.
 * These should be listed in @author doc comments.
 *
 * For more information on the Nividic project and its aims,
 * or to join the Nividic mailing list, visit the home page
 * at:
 *
 *      http://www.transcriptome.ens.fr/nividic
 *
 */

package fr.ens.transcriptome.nividic.om.design;

import fr.ens.transcriptome.nividic.om.PhysicalConstants;
import fr.ens.transcriptome.nividic.om.design.impl.DesignImpl;

/**
 * This class is a factory for experimental design.
 * @author Laurent Jourdren
 */
public final class DesignFactory {

  /**
   * Create a standard design for 2 colors experiments
   * @return a new DesignImpl
   */
  public static Design create2ColorsDesign() {

    final Design d = new DesignImpl();
    d.addLabel(PhysicalConstants.CY3_COLOR);
    d.addLabel(PhysicalConstants.CY5_COLOR);

    return d;
  }

  /**
   * Create a design without targets.
   * @return a new DesignImpl
   */
  public static Design createEmptyDesign() {

    return new DesignImpl();
  }

  //
  // Constructor
  //

  private DesignFactory() {
  }

}
