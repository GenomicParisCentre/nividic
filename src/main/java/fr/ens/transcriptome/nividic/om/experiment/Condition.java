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
 * This class define a condition of an experiement.
 * @author Laurent Jourdren
 */
public class Condition {

  private ExperimentDesign experimentDesign;
  private String name;
  private String description;

  private ArrayList listeners = new ArrayList();

  private static final int HASHCODE_ODD_NUMBER_1 = 4505;
  private static final int HASHCODE_ODD_NUMBER_2 = 1811;

  //
  // Getters
  //

  /**
   * Get the description of the condition.
   * @return Returns the description
   */
  public String getDescription() {
    return description;
  }

  /**
   * Get the name of the condition
   * @return Returns the name
   */
  public String getName() {
    return name;
  }

  /**
   * Set the experiment design of the condition.
   * @return Returns the experimentDesign
   */
  ExperimentDesign getExperimentDesign() {
    return experimentDesign;
  }

  //
  // Setters
  //

  /**
   * Set the description of the condition.
   * @param description The description to set
   */
  public void setDescription(final String description) {
    this.description = description;

    if (getExperimentDesign() != null)
      sendEvent(new SimpleExperimentDesignEvent(getExperimentDesign(),
          ExperimentDesignEvent.CONDITION_MODIFY_EVENT, this));
  }

  /**
   * Set the name of the description.
   * @param name The name to set
   * @throws NividicException if the name is null
   */
  public void setName(final String name) throws NividicException {

    if (name == null)
      throw new NividicException("Condition name is null");

    if (getExperimentDesign() != null
        && getExperimentDesign().isCondition(name))
      throw new NividicException("New name already exist");

    this.name = name;

    if (getExperimentDesign() != null)
      sendEvent(new SimpleExperimentDesignEvent(getExperimentDesign(),
          ExperimentDesignEvent.CONDITION_MODIFY_EVENT, this));
  }

  /**
   * Set the experiment design of the experiment.
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

  //
  // Other methods
  //

  /**
   * Test if two Condition objects are equals.
   * @param o Object to test
   * @return true if objects are equals
   */
  public boolean equals(final Object o) {

    if (o == null)
      return false;
    if (!(o instanceof Condition))
      return false;

    Condition c = (Condition) o;

    return new EqualsBuilder().appendSuper(super.equals(o))
        .append(name, c.name).append(description, c.description).append(
            experimentDesign, c.experimentDesign).isEquals();

  }

  /**
   * Returns a hash code value for the object.
   * @return a hash code value for this object
   */
  public int hashCode() {

    HashCodeBuilder hcb = new HashCodeBuilder(HASHCODE_ODD_NUMBER_1,
        HASHCODE_ODD_NUMBER_2);

    hcb.append(name).append(description).append(experimentDesign);

    return hcb.toHashCode();
  }

  //
  // Constructor
  //

  /**
   * Public constructor.
   * @param name The name of the condition
   * @param description The description of the condition
   * @throws NividicException if name if null
   */
  public Condition(final String name, final String description)
      throws NividicException {
    setName(name);
    setDescription(description);
  }

  /**
   * Public constructor.
   * @param name The name of the condition
   * @throws NividicException if name if null
   */
  public Condition(final String name) throws NividicException {
    this(name, null);
  }

}
