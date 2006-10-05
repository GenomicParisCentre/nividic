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
 * This class allow to build Paramater objects.
 * @author Laurent Jourdren
 */
public class ParameterBuilder {

  private ParameterImpl p = new ParameterImpl();

  /**
   * Set the name of a parameter.
   * @param name The name to set
   * @return The ParameterBuilder Object
   */
  public ParameterBuilder withName(final String name) {
    this.p.setName(name);
    return this;
  }

  /**
   * Set the long name of a parameter.
   * @param longName The long name to set
   * @return The ParameterBuilder Object
   */
  public ParameterBuilder withLongName(final String longName) {
    this.p.setLongName(longName);
    return this;
  }

  /**
   * Set the type of the parameter
   * @param type The type of the parameter
   * @return The ParameterBuilder Object
   * @throws ParameterException if an error occurs while setting the type
   */
  public ParameterBuilder withType(final int type) throws ParameterException {
    this.p.setType(type);
    return this;
  }

  /**
   * Set the description of the parameter
   * @param description The description to set
   * @return The ParameterBuilder Object
   */
  public ParameterBuilder withDescription(final String description) {
    this.p.setDescription(description);
    return this;
  }

  /**
   * Set the long description of the parameter.
   * @param longDescription The long description of the parameter
   * @return The ParameterBuilder Object
   */
  public ParameterBuilder withLongDescription(final String longDescription) {
    this.p.setLongDescription(longDescription);
    return this;
  }

  /**
   * Add a range value for the parameter.
   * @param min Minimum value of the parameter
   * @param max Maximum value of the parameter
   * @return The ParameterBuilder Object
   */
  public ParameterBuilder withRangeValue(final double min, final double max) {

    this.p.addTester(p.makeRangeTest(min, max));
    return this;
  }

  /**
   * Add Minimum value for the parameter.
   * @param threshold The minimum value of the parameter
   * @return The ParameterBuilder Object
   */
  public ParameterBuilder withGreaterThanValue(final double threshold) {

    this.p.addTester(p.makeGreaterThanTest(threshold));
    return this;
  }

  /**
   * Add a maximum value for the parameter,
   * @param threshold The maximum value for the parameter
   * @return The ParameterBuilder Object
   */
  public ParameterBuilder withLowerThanValue(final double threshold) {

    this.p.addTester(p.makeLowerThanTest(threshold));
    return this;
  }

  /**
   * Add a value for the choice of the parameter
   * @param value The value to set
   * @return The ParameterBuilder Object
   */
  public ParameterBuilder withEqualsValue(final String value) {

    this.p.addTester(p.makeEqualsTest(value));
    return this;
  }

  /**
   * Set the choices for the value of the parameter.
   * @param choices The choices to set
   * @return The ParameterBuilder Object
   */
  public ParameterBuilder withChoices(final String[] choices) {
    this.p.setChoices(choices);
    return this;
  }

  /**
   * Set the default value of the parameter.
   * @param value The value to set
   * @return The ParameterBuilder Object
   */
  public ParameterBuilder withDefaultValue(final String value) {
    this.p.setDefaultValue(value);
    return this;
  }

  /**
   * Set the unit of the parameter.
   * @param unit The unit to set
   * @return The ParameterBuilder Object
   */
  public ParameterBuilder withUnit(final String unit) {
    this.p.setUnit(unit);
    return this;
  }

  /**
   * Set the type of equality of the parameter.
   * @param equality The equality type to set
   * @return The ParameterBuilder Object
   */
  public ParameterBuilder withEqualityType(final String equality) {
    this.p.setEqualityType(equality);
    return this;
  }

  /**
   * Get the Parameter object build with the builder.
   * @return A new Parameter object
   * @throws ParameterException if the paramter has no name or if the parameter
   *                 type is not set
   */
  public Parameter getParameter() throws ParameterException {

    if (p.getName() == null || p.getName().equals(""))
      throw new ParameterException("The parameter must have a name");
    if (p.getType() == 0)
      throw new ParameterException("The parameter must have a type");
    return p;
  }

}