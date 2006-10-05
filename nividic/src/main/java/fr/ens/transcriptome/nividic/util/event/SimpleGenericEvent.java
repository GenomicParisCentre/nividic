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
 * This class describe an generic event.
 * @author Laurent Jourdren
 */
public class SimpleGenericEvent implements GenericEvent {

  private int id;

  private Object source;
  private int valueType;
  private String description;
  private int intValue;
  private double doubleValue;
  private String stringValue;
  private Object objectValue;

  /**
   * Get the description of the event.
   * @return Returns the description
   */
  public String getDescription() {
    return description;
  }

  /**
   * Get the double value.
   * @return Returns the doubleValue
   */
  public double getDoubleValue() {
    return doubleValue;
  }

  /**
   * Get the event id.
   * @return Returns the id
   */
  public int getId() {
    return id;
  }

  /**
   * Get the int value.
   * @return Returns the intValue
   */
  public int getIntValue() {
    return intValue;
  }

  /**
   * Get the object value.
   * @return Returns the objectValue
   */
  public Object getObjectValue() {
    return objectValue;
  }

  /**
   * Get the source of the event.
   * @return Returns the source
   */
  public Object getObjectSource() {
    return source;
  }

  /**
   * Get the string value.
   * @return Returns the stringValue
   */
  public String getStringValue() {
    return stringValue;
  }

  /**
   * Get the type of the value.
   * @return Returns the valueType
   */
  public int getValueType() {
    return valueType;
  }

  //
  // Setters
  //

  /**
   * Set the description of the event.
   * @param description The description to set
   */
  protected void setDescription(final String description) {
    this.description = description;
  }

  /**
   * Set the double value.
   * @param doubleValue The doubleValue to set
   */
  protected void setDoubleValue(final double doubleValue) {
    this.doubleValue = doubleValue;
  }

  /**
   * Set the id of the event.
   * @param id The id to set
   */
  protected void setId(final int id) {
    this.id = id;
  }

  /**
   * Set the integer value of the event.
   * @param intValue The intValue to set
   */
  protected void setIntValue(final int intValue) {
    this.intValue = intValue;
  }

  /**
   * Set the object value of the event.
   * @param objectValue The objectValue to set
   */
  protected void setObjectValue(final Object objectValue) {
    this.objectValue = objectValue;
  }

  /**
   * Set the source of the event.
   * @param source The source to set
   */
  protected void setObjectSource(final Object source) {
    this.source = source;
  }

  /**
   * Set the string value of the event
   * @param stringValue The stringValue to set
   */
  protected void setStringValue(final String stringValue) {
    this.stringValue = stringValue;
  }

  /**
   * Set the type of the value of the event.
   * @param valueType The valueType to set
   */
  protected void setValueType(final int valueType) {
    this.valueType = valueType;
  }

}
