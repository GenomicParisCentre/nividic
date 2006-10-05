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

package fr.ens.transcriptome.nividic.om.r;

import org.rosuda.JRclient.RSrvException;

/**
 * @author montout TODO To change the template for this generated type comment
 *         go to Window - Preferences - Java - Code Style - Code Templates
 */
public interface RObject {

  /**
   * Put java data in a R Object.
   * @param rName Name of the R object
   * @throws RSrvException if an error occurs
   */
  void put(String rName) throws RSrvException;

  /**
   * Get R data in the java Object.
   * @param rName Name of the R object
   * @throws RSrvException if an error occurs
   */
  void get(String rName) throws RSrvException;

}