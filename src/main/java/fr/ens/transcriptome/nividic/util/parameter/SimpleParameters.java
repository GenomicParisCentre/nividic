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

import java.util.LinkedHashMap;
import java.util.Map;

import fr.ens.transcriptome.nividic.util.NividicUtils;

/**
 * This class defines an object to contain parameters.
 * @author Laurent Jourdren
 */
class SimpleParameters implements Parameters {

  private Map<String, Parameter> params =
      new LinkedHashMap<String, Parameter>();
  private boolean fixedParameters;

  /**
   * Add a paramter.
   * @param p Parameter to add
   */
  public void addParameter(final Parameter p) {
    if (p == null)
      return;
    this.params.put(p.getName(), p);
  }

  /**
   * Get all the names of the parameters in the object.
   * @return A string array with the name of the paramters
   */
  public String[] getParametersNames() {

    return NividicUtils.toArray(this.params.keySet());
  }

  /**
   * Set the value of a paramter
   * @param name Name of the parameter
   * @param value Value to set
   * @throws ParameterException if the value of paramter is bad or if the
   *             parameter doesn't exits
   */
  public void setParameter(final String name, final String value)
      throws ParameterException {

    if (isFixedParameters() && !this.params.containsKey(name))
      throw new ParameterException("This parameter doesn't exists : " + name);

    // if the paramter not exists create it
    if (!isFixedParameters())
      addParameter(new ParameterBuilder().withName(name).getParameter());

    ((Parameter) this.params.get(name)).setValue(value);
  }

  /**
   * Get a parameter.
   * @param name The name of the parameter to get
   * @return a parameter object
   */
  public Parameter getParameter(final String name) {

    if (!this.params.containsKey(name))
      return null;
    return (Parameter) this.params.get(name);
  }

  /**
   * Returns the number of paramters.
   * @return The number of parameters
   */
  public int size() {
    return this.params.size();
  }

  /**
   * Test if the parameters are fixed.
   * @return if the parameters are fixed
   */
  public boolean isFixedParameters() {
    return fixedParameters;
  }

  /**
   * Set if the parameters are fixed.
   * @param fixed true if the paramters must be fixed
   */
  public void setFixedParameters(final boolean fixed) {
    fixedParameters = fixed;
  }

  //
  // Constructor
  //

  /**
   * Constructor. Set if the parameters are fixed.
   * @param fixed true if the paramters must be fixed
   */
  public SimpleParameters(final boolean fixed) {
    setFixedParameters(fixed);
  }

}