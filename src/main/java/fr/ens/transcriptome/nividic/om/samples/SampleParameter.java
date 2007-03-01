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

package fr.ens.transcriptome.nividic.om.samples;

/**
 * This class define a sample parameter
 * @author Laurent Jourdren
 */
class SampleParameter {

  private static int count;

  private int id;
  private String name;
  private SampleParameterType type;

  //
  // Getters
  //

  int getId() {

    return id;
  }

  /**
   * Get the name of the parameter
   * @return Returns the name
   */
  public String getName() {

    return name;
  }

  /**
   * Get the type of the parameter
   * @return Returns the type
   */
  public SampleParameterType getType() {

    return type;
  }

  //
  // Setters
  //

  /**
   * Set the name of the parameter.
   * @param name The name to set
   */
  public void setName(final String name) {

    if (name == null)
      throw new NullPointerException("Parameter name can't be null");
    this.name = name;
  }

  /**
   * Set the type of the parameter.
   * @param type The type to set
   */
  public void setType(final SampleParameterType type) {
    this.type = type;
  }

  //
  // Constructor
  //

  /**
   * Public constructor
   * @param name Name of the parameter
   * @param type Type of the parameter
   */
  public SampleParameter(final String name, final SampleParameterType type) {

    this.id = count;
    count++;

    setName(name);
    setType(type);
  }

}
