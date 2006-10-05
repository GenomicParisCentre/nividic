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

package fr.ens.transcriptome.nividic.util.parameter;

/**
 * This interface define a container for Parameter Objects, in which none Parameter
 * can be added or removed.
 *
 * @author Laurent Jourdren
 */
public interface Parameters {

  /**
   * Get all the names of the parameters in the object.
   * @return A string array with the name of the paramters
   */
  String[] getParametersNames();

  /**
   * Set the value of a paramter
   * @param name Name of the parameter
   * @param value Value to set
   * @throws ParameterException if the value of parameter is bad or if the parameter doesn't exists
   */
  void setParameter(final String name, final String value)
    throws ParameterException;

  /**
   * Get a parameter.
   * @param name The name of the parameter to get
   * @return a parameter object
   */
  Parameter getParameter(final String name);

  /**
   * Returns the number of paramters.
   * @return The number of parameters
   */
  int size();

}
