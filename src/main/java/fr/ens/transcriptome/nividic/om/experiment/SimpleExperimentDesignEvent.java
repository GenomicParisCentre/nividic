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

import fr.ens.transcriptome.nividic.util.event.SimpleGenericEvent;

/**
 * This method implements a simple experiment design event.
 * @author Laurent Jourdren
 */
public class SimpleExperimentDesignEvent extends SimpleGenericEvent implements
    ExperimentDesignEvent {

  /**
   * Get the source of the event.
   * @return Returns the source
   */
  public ExperimentDesign getSource() {

    return (ExperimentDesign) super.getObjectSource();
  }

  /**
   * Set the source of the event.
   * @param source The source to set
   */
  void setSource(final ExperimentDesign source) {
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
  public SimpleExperimentDesignEvent(final ExperimentDesign source,
      final int id, final int value, final String description) {

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
  public SimpleExperimentDesignEvent(final ExperimentDesign source,
      final int id, final int value) {

    this(source, id, value, null);
  }

  /**
   * Public constructor.
   * @param source The source of the event
   * @param id The identifier of the event
   * @param value The value of the event
   * @param description The description of the event
   */
  public SimpleExperimentDesignEvent(final ExperimentDesign source,
      final int id, final double value, final String description) {

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
  public SimpleExperimentDesignEvent(final ExperimentDesign source,
      final int id, final double value) {

    this(source, id, value, null);
  }

  /**
   * Public constructor.
   * @param source The source of the event
   * @param id The identifier of the event
   * @param value The value of the event
   * @param description The description of the event
   */
  public SimpleExperimentDesignEvent(final ExperimentDesign source,
      final int id, final String value, final String description) {

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
  public SimpleExperimentDesignEvent(final ExperimentDesign source,
      final int id, final String value) {

    this(source, id, value, null);
  }

  /**
   * Public constructor.
   * @param source The source of the event
   * @param id The identifier of the event
   * @param value The value of the event
   * @param description The description of the event
   */
  public SimpleExperimentDesignEvent(final ExperimentDesign source,
      final int id, final Object value, final String description) {

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
  public SimpleExperimentDesignEvent(final ExperimentDesign source,
      final int id, final Object value) {

    this(source, id, value, null);
  }

}
