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

package fr.ens.transcriptome.nividic.util;

import java.util.LinkedHashMap;
import java.util.Set;

/**
 * A Properties ordered implemetation.
 * @author Laurent Jourdren
 */

public class Properties {

  private LinkedHashMap<String, String> data = new LinkedHashMap<String, String>();

  /**
   * Set a property.
   * @param key Key set set
   * @param value Value of the key
   */
  public void setProperty(final String key, final String value) {
    this.data.put(key, value);
  }

  /**
   * Get a property
   * @param key Key of the property to get
   * @return The value of the proterty
   */
  public String getProperty(final String key) {
    return this.data.get(key);
  }

  /**
   * Remove a property.
   * @param key Key of the property to remove
   * @return The value of the property removed
   */
  public String remove(final String key) {
    return this.data.remove(key);
  }

  /**
   * Number of properties in the object.
   * @return The size of the object
   */
  public int size() {
    return this.data.size();
  }

  /**
   * Clear the properties.
   */
  public void clear() {
    this.data.clear();
  }

  /**
   * Get a set of the keys for the object.
   * @return A iterator
   */
  public Set<String> keySet() {
    return this.data.keySet();
  }

}
