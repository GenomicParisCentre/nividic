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

package fr.ens.transcriptome.nividic.om.impl;

import java.util.Set;

/**
 * This interface defines how manage listeners for ExpressionMatrixDimension
 * @author Laurent Jourdren
 */
public interface ExpressionMatrixListenerHandler {

  /**
   * add a listener in a ExpressionMatrixObject
   * @param listener the listener to add
   */
  void addListener(ExpressionMatrixListener listener);

  /**
   * remove a listener in a ExpressionMatrixObject
   * @param listener the listener to remove
   */
  void removeListener(ExpressionMatrixListener listener);

  /**
   * get all the listeners from an ExpressionMatrixDimension object
   * @return a set of all the listeners of the ExpressionMatrixDimension object
   */
  Set getListeners();

}