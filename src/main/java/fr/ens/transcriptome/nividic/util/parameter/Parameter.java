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
 * This interface define methods to handle a parameter. TODO implements short,
 * float
 * @author Laurent Jourdren
 */
public interface Parameter {

  /** Integer data type. */
  int DATATYPE_INTEGER = 1;
  /** Double data type. */
  int DATATYPE_DOUBLE = 2;
  /** String data type. */
  int DATATYPE_STRING = 3;
  /** Boolean data type */
  int DATATYPE_BOOLEAN = 4;
  /** Yes/No data type */
  int DATATYPE_YESNO = 5;
  /** Array integer type */
  int DATATYPE_ARRAY_INTEGER = 6;
  /** Array double type */
  int DATATYPE_ARRAY_DOUBLE = 7;
  /** Array string type */
  int DATATYPE_ARRAY_STRING = 8;
  /** Array boolean type */
  int DATATYPE_ARRAY_BOOLEAN = 9;
  /** Array yes/no type */
  int DATATYPE_ARRAY_YESNO = 10;

  /**
   * Get the name of the parameter.
   * @return The name of the parameter
   */
  String getName();

  /**
   * Get the long name of the parameter.
   * @return The long name of the parameter
   */
  String getLongName();

  /**
   * Get a description of the parameter.
   * @return the description of the parameter.
   */
  String getDescription();

  /**
   * Get a long description.
   * @return the long description of the parameter.
   */
  String getLongDescription();

  /**
   * Get the type of the parameter.
   * @return The type of the parameter
   */
  int getType();

  /**
   * Get the unit of the parameter if exists.
   * @return A string with the unit of the parameter.
   */
  String getUnit();

  /**
   * Get the type of equality between the parameter and this value. For example:
   * "=", " <", ">", " <=", ">=", "!="... Default value is "=".
   * @return the type of equality of the parameter.
   */
  String getEqualityType();

  /**
   * Set the value of a parameter.
   * @param value The value to set.
   * @throws ParameterException if the format value is bad
   */
  void setValue(String value) throws ParameterException;

  /**
   * Get the value of the parameter in a string.
   * @return A String value
   * @throws ParameterException if parameter is'nt a boolean
   */
  String getValue() throws ParameterException;

  /**
   * Get the value of the parameter if is a boolean.
   * @return A boolean value
   * @throws ParameterException if parameter is'nt a boolean
   */
  boolean getBooleanValue() throws ParameterException;

  /**
   * Get the value of the parameter if is an integer.
   * @return A integer value
   * @throws ParameterException if parameter is'nt an integer
   */
  int getIntValue() throws ParameterException;

  /**
   * Get the value of the parameter if is a string.
   * @return A string value
   * @throws ParameterException if parameter is'nt a string
   */
  String getStringValue() throws ParameterException;

  /**
   * Get the value of the parameter if is a string.
   * @return A double value
   * @throws ParameterException if parameter is'nt a double
   */
  double getDoubleValue() throws ParameterException;

  /**
   * Get the value of the parameter if is an array of strings.
   * @return A double value
   * @throws ParameterException if parameter is'nt a double
   */
  int[] getArrayIntegerValues() throws ParameterException;

  /**
   * Get the value of the parameter if is an array of strings.
   * @return A double value
   * @throws ParameterException if parameter is'nt a double
   */
  double[] getArrayDoubleValues() throws ParameterException;

  /**
   * Get the value of the parameter if is an array of strings.
   * @return A double value
   * @throws ParameterException if parameter is'nt a double
   */
  String[] getArrayStringValues() throws ParameterException;

  /**
   * Get the value of the parameter if is an array of strings.
   * @return A double value
   * @throws ParameterException if parameter is'nt a double
   */
  boolean[] getArrayBooleanValues() throws ParameterException;

  /**
   * Get the value of the parameter if is an array of strings.
   * @return A double value
   * @throws ParameterException if parameter is'nt a double
   */
  boolean[] getArrayYesNoValues() throws ParameterException;

  /**
   * Get the default value if exits.
   * @return The default value
   */
  String getDefaultValue();

  /**
   * Get all existing choices for the parameter.
   * @return A string array with all choices
   */
  String[] getChoices();

}