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

import fr.ens.transcriptome.nividic.om.ExpressionMatrix;
import fr.ens.transcriptome.nividic.om.ExpressionMatrixDimension;
import fr.ens.transcriptome.nividic.util.event.SimpleGenericEvent;

/**
 * This class define a event for ExpressionMatrixDimension.
 * @author Laurent Jourdren
 */
public class ExpressionMatrixEvent extends SimpleGenericEvent {

  
  
  /** Workflow remove identifer event. */
  public static final int REMOVE_ROW_EVENT = 1;

  /** Workflow remove column event. */
  public static final int REMOVE_COLUMN_EVENT = 2;

  /** Workflow remove dimension event. */
  public static final int REMOVE_DIMENSION_EVENT = 3;

  /** Workflow rename identifer event. */
  public static final int RENAME_ROW_EVENT = 4;

  /** Workflow rename column event. */
  public static final int RENAME_COLUMN_EVENT = 5;

  /** Workflow rename dimension event. */
  public static final int RENAME_DIMENSION_EVENT = 6;

  /** Workflow add identifer event. */
  public static final int ADD_ROW_EVENT = 7;

  /** Workflow add column event. */
  public static final int ADD_COLUMN_EVENT = 8;
  
  /** Workflow add column event. */
  public static final int ADD_DIMENSION_EVENT = 9;

  /**
   * Get the source of the event.
   * @return Returns the source
   */
  public ExpressionMatrix getSource() {

    return (ExpressionMatrix) super.getObjectSource();
  }

  /**
   * Set the source of the event.
   * @param source The source to set
   */
  void setSource(final ExpressionMatrix source) {
    super.setObjectSource(source);
  }

  //
  // Constructors
  //

  /**
   * Public constructor.
   * @param source The source of the event
   * @param id The identifier of the event
   * @param value The value of the event
   * @param description The description of the event
   */
  public ExpressionMatrixEvent(final ExpressionMatrix source, final int id,
      final int value, final String description) {

    setSource(source);
    setId(id);
    setIntValue(value);
    setValueType(INTEGER_VALUE_TYPE);
    setDescription(description);
  }

  /**
   * Public constructor.
   * @param source The source of the event
   * @param id The identifier of the event
   * @param value The value of the event
   */
  public ExpressionMatrixEvent(final ExpressionMatrix source, final int id,
      final int value) {

    this(source, id, value, null);
  }

  /**
   * Public constructor.
   * @param source The source of the event
   * @param id The identifier of the event
   * @param value The value of the event
   * @param description The description of the event
   */
  public ExpressionMatrixEvent(final ExpressionMatrix source, final int id,
      final double value, final String description) {

    setSource(source);
    setId(id);
    setDoubleValue(value);
    setValueType(DOUBLE_VALUE_TYPE);
    setDescription(description);
  }

  /**
   * Public constructor.
   * @param source The source of the event
   * @param id The identifier of the event
   * @param value The value of the event
   */
  public ExpressionMatrixEvent(final ExpressionMatrix source, final int id,
      final double value) {

    this(source, id, value, null);
  }

  /**
   * Public constructor.
   * @param source The source of the event
   * @param id The identifier of the event
   * @param value The value of the event
   * @param description The description of the event
   */
  public ExpressionMatrixEvent(final ExpressionMatrix source, final int id,
      final String value, final String description) {

    setSource(source);
    setId(id);
    setStringValue(value);
    setValueType(STRING_VALUE_TYPE);
    setDescription(description);
  }

  /**
   * Public constructor.
   * @param source The source of the event
   * @param id The identifier of the event
   * @param value The value of the event
   */
  public ExpressionMatrixEvent(final ExpressionMatrix source, final int id,
      final String value) {

    this(source, id, value, null);
  }

  /**
   * Public constructor.
   * @param source The source of the event
   * @param id The identifier of the event
   * @param value The value of the event
   * @param description The description of the event
   */
  public ExpressionMatrixEvent(final ExpressionMatrix source, final int id,
      final Object value, final String description) {

    setSource(source);
    setId(id);
    setObjectValue(value);
    setValueType(OBJECT_VALUE_TYPE);
    setDescription(description);
  }

  /**
   * Public constructor.
   * @param source The source of the event
   * @param id The identifier of the event
   * @param value The value of the event
   */
  public ExpressionMatrixEvent(final ExpressionMatrix source, final int id,
      final Object value) {

    this(source, id, value, null);
  }

}