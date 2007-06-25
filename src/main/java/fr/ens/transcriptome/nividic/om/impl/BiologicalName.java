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

import java.io.Serializable;

/**
 * This class define a class to handle a biological name.
 * @author Laurent Jourdren
 */
class BiologicalName implements Serializable {

  private static int currentId;

  private int id = currentId++;
  private String name;

  /**
   * Get the id of the biological Object
   * @return an Integer as biological id.
   */
  public int getBiologicalId() {

    return id;
  }

  /**
   * Get the name of the biologicalObject
   * @return The name of the biologicalObject
   */
  public String getName() {

    return name;
  }

  /**
   * Set the name of the biological Object
   * @param name The new name of the biological object
   */
  public void setName(final String name) {

    this.name = name;
  }

  /**
   * Clear the name.
   */
  public void clear() {

    this.name = null;
  }

  //
  // Constructor
  //

  /**
   * Public constructor
   * @param o instance of the class
   */
  BiologicalName(final Object o) {

    this(o.getClass());
  }

  /**
   * Public constructor
   * @param clazz Class that need to have a name
   */
  BiologicalName(final Class clazz) {

    final String s = clazz == null ? "Unknow" : clazz.getSimpleName();

    setName(s + "-" + this.id);
  }

}
