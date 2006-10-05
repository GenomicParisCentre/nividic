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

package fr.ens.transcriptome.nividic.util.event;

/**
 * Generic interface for events.
 * @author Laurent Jourdren
 */
public interface GenericEvent {

  /** Integer value type. */
  int INTEGER_VALUE_TYPE = 1;

  /** Double value type. */
  int DOUBLE_VALUE_TYPE = 2;

  /** String value type. */
  int STRING_VALUE_TYPE = 3;

  /** Object value type. */
  int OBJECT_VALUE_TYPE = 4;

  /**
   * Get the description of the event.
   * @return The description of the event
   */
  String getDescription();

  /**
   * Get the double value.
   * @return Returns the doubleValue
   */
  double getDoubleValue();

  /**
   * Get the event id.
   * @return Returns the id
   */
  int getId();

  /**
   * Get the int value.
   * @return Returns the intValue
   */
  int getIntValue();

  /**
   * Get the object value.
   * @return Returns the objectValue
   */
  Object getObjectValue();

  /**
   * Get the string value.
   * @return Returns the stringValue
   */
  String getStringValue();

  /**
   * Get the type of the value.
   * @return Returns the valueType
   */
  int getValueType();

}