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

package fr.ens.transcriptome.nividic.om;

import org.apache.commons.collections.OrderedMapIterator;

/**
 * This interface define annotations
 * @author Laurent Jourdren
 */
public interface Annotation {

  /**
   * Set a annotation.
   * @param key Key set set
   * @param value Value of the key
   */
  void setProperty(final String key, final String value);

  /**
   * Add an annotation. This method use a counter to create the key of the
   * annotation.
   * @param value Value to set
   */
  void addProperty(final String value);

  /**
   * Add all the annotations in a previous annotation object to the current
   * object.
   * @param annotations Annotations to set
   */
  void addProperties(final Annotation annotations);

  /**
   * Get a property
   * @param key Key of the property to get
   * @return The value of the proterty
   */
  String getProperty(final String key);

  /**
   * Remove a property.
   * @param key Key of the property to remove
   * @return The value of the property removed
   */
  String removeProperty(final String key);

  /**
   * Number of properties in the object.
   * @return The size of the object
   */
  int size();

  /**
   * Clear the properties.
   */
  void clear();

  /**
   * Get an iterator for the object.
   * @return A iterator
   */
  OrderedMapIterator iterator();

  /**
   * Returns the annotations keys.
   * @return All the annotations
   */
  String[] getPropertiesKeys();

  /**
   * Test if the annotation contains a property.
   * @param key Key to test
   * @return true if the property exists
   */
  boolean containsProperty(final String key);

}