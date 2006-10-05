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

import java.util.ArrayList;
import java.util.Iterator;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import fr.ens.transcriptome.nividic.NividicException;

/**
 * This class define a experiment parameter.
 * @author Laurent Jourdren
 */
public class ExperimentParameter {

  private ExperimentDesign experimentDesign;
  private String name;
  private ExperimentParameterType type;
  private String unit;
  private ArrayList listeners = new ArrayList();

  private static final int HASHCODE_ODD_NUMBER_1 = 4505;
  private static final int HASHCODE_ODD_NUMBER_2 = 1815;

  //
  // Getters
  //

  /**
   * Get the name of the parameter.
   * @return Returns the name
   */
  public String getName() {
    return name;
  }

  /**
   * Get the type of the parameter.
   * @return Returns the type
   */
  public ExperimentParameterType getType() {
    return type;
  }

  /**
   * Get the unit of the parameter.
   * @return Returns the unit
   */
  public String getUnit() {
    return unit;
  }

  /**
   * Get the experiment design.
   * @return Returns the experimentDesign
   */
  ExperimentDesign getExperimentDesign() {
    return experimentDesign;
  }

  //
  // Setters
  //

  /**
   * Set the name of the parameter.
   * @param name The name to set
   * @throws NividicException if name if null
   */
  public void setName(final String name) throws NividicException {

    if (name == null)
      throw new NividicException("Experiement parameter name can't be null");

    if (getExperimentDesign() != null
        && getExperimentDesign().isParameter(name))
      throw new NividicException("New name already exist");

    this.name = name;
    if (getExperimentDesign() != null)
      sendEvent(new SimpleExperimentDesignEvent(getExperimentDesign(),
          ExperimentDesignEvent.PARAMETER_MODIFY_EVENT, this));
  }

  /**
   * Set the type of the parameter.
   * @param type The type to set
   */
  public void setType(final ExperimentParameterType type) {

    this.type = type;
    if (getExperimentDesign() != null)
      sendEvent(new SimpleExperimentDesignEvent(getExperimentDesign(),
          ExperimentDesignEvent.PARAMETER_MODIFY_EVENT, this));
  }

  /**
   * Set the unit of the parameter.
   * @param unit The unit to set
   */
  public void setUnit(final String unit) {

    this.unit = unit;
    if (getExperimentDesign() != null)
      sendEvent(new SimpleExperimentDesignEvent(getExperimentDesign(),
          ExperimentDesignEvent.PARAMETER_MODIFY_EVENT, this));
  }

  /**
   * Set the experiment design
   * @param experimentDesign The experimentDesign to set
   */
  void setExperimentDesign(final ExperimentDesign experimentDesign) {
    this.experimentDesign = experimentDesign;
  }

  //
  // Listener management
  //

  /**
   * Add a listener.
   * @param listener Listener to add.
   */
  public void addListener(final ExperimentDesignListener listener) {

    if (listener == null || this.listeners.contains(listener))
      return;
    this.listeners.add(listener);
  }

  /**
   * Remove a listener.
   * @param listener Listener to remove
   */
  public void removeListener(final ExperimentDesignListener listener) {
    if (listener == null || !this.listeners.contains(listener))
      return;

    this.listeners.remove(listener);
  }

  /**
   * Send event to all the listeners.
   * @param event a AlgorithmEvent object
   */
  private void sendEvent(final ExperimentDesignEvent event) {

    if (event == null)
      return;

    Iterator it = this.listeners.iterator();
    while (it.hasNext())
      ((ExperimentDesignListener) it.next())
          .experimentDesignStateChanged(event);
  }

  /**
   * Test if two Condition objects are equals.
   * @param o Object to test
   * @return true if objects are equals
   */
  public boolean equals(final Object o) {

    if (o == null)
      return false;
    if (!(o instanceof ExperimentParameter))
      return false;

    ExperimentParameter p = (ExperimentParameter) o;

    return new EqualsBuilder().appendSuper(super.equals(p))
        .append(name, p.name).append(type, p.type).append(experimentDesign,
            p.experimentDesign).append(unit, p.unit).isEquals();

  }

  /**
   * Returns a hash code value for the object.
   * @return a hash code value for this object
   */
  public int hashCode() {

    HashCodeBuilder hcb = new HashCodeBuilder(HASHCODE_ODD_NUMBER_1,
        HASHCODE_ODD_NUMBER_2);

    hcb.append(name).append(type).append(unit).append(experimentDesign);

    return hcb.toHashCode();
  }

  //
  // Constructor
  //

  /**
   * Public constructor.
   * @throws NividicException if name if null
   */
  public ExperimentParameter() throws NividicException {
    this(null);
  }

  /**
   * Public constructor.
   * @param name Name of the parameter
   * @throws NividicException if name if null
   */
  public ExperimentParameter(final String name) throws NividicException {
    this(name, new SimpleExperimentParameterType());
  }

  /**
   * Public constructor.
   * @param name Name of the parameter
   * @param type Experiment parameter type
   * @throws NividicException if name if null
   */
  public ExperimentParameter(final String name,
      final ExperimentParameterType type) throws NividicException {
    setName(name);
    setType(type);
  }

}