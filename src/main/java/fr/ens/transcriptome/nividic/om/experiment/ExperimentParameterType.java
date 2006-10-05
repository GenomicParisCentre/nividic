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

package fr.ens.transcriptome.nividic.om.experiment;

/**
 * This interface defines an experiement parameter type.
 * @author Laurent Jourdren
 */
public interface ExperimentParameterType {

  /**
   * Get the name of the experiment parameter type
   * @return Returns the name of the experiement parameter
   */
  String getName();

  /**
   * Get the default value of the parameter.
   * @return The default value of the parameter
   */
  String getDefaultValue();

  /**
   * Get the type (integer, double, string) of the value.
   * @return The type of the value
   */
  Class getValueType();

  /**
   * Test if the value of parameter is correct.
   * @param value Value to test
   * @return true if the value of the parameter is correct
   */
  boolean isCorrectValue(String value);

}