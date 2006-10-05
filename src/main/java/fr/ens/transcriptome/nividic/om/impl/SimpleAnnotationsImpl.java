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

import java.util.Iterator;

import org.apache.commons.collections.OrderedMapIterator;
import org.apache.commons.collections.map.LinkedMap;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import fr.ens.transcriptome.nividic.om.Annotation;

/**
 * This class define annotations for biological objects
 * @author Laurent Jourdren
 */
public class SimpleAnnotationsImpl implements Annotation {

  private LinkedMap data = new LinkedMap();

  private static final int HASHCODE_ODD_NUMBER_1 = 4505;
  private static final int HASHCODE_ODD_NUMBER_2 = 1807;

  /**
   * Set a property.
   * @param key Key set set
   * @param value Value of the key
   */
  public void setProperty(final String key, final String value) {
    this.data.put(key, value);
  }

  /**
   * Add all the annotations in a previous annotation object to the current
   * object.
   * @param annotations Annotations to set
   */
  public void addProperties(final Annotation annotations) {

    if (annotations == null)
      return;

    Iterator it = annotations.iterator();
    while (it.hasNext()) {
      String key = (String) it.next();
      setProperty(key, annotations.getProperty(key));
    }
  }

  /**
   * Add an property. This method use a counter to create the key of the
   * property.
   * @param value Value to set
   */
  public void addProperty(final String value) {
    if (value != null) {
      setProperty("#" + (size() + 1), value);
    }
  }

  /**
   * Get a property
   * @param key Key of the property to get
   * @return The value of the proterty
   */
  public String getProperty(final String key) {
    return (String) this.data.get(key);
  }

  /**
   * Remove a property.
   * @param key Key of the property to remove
   * @return The value of the property removed
   */
  public String removeProperty(final String key) {
    return (String) this.data.remove(key);
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
    this.data = new LinkedMap();
  }

  /**
   * Get an iterator for the object.
   * @return A iterator
   */
  public OrderedMapIterator iterator() {
    return this.data.orderedMapIterator();
  }

  /**
   * Returns the properties keys.
   * @return All the annotations
   */
  public String[] getPropertiesKeys() {

    String[] result = new String[size()];
    OrderedMapIterator omi = this.data.orderedMapIterator();

    int i = 0;
    while (omi.hasNext())
      result[i++] = (String) omi.next();

    return result;
  }

  /**
   * Test if the annotation contains a property.
   * @param key Key to test
   * @return true if the property exists
   */
  public boolean containsProperty(final String key) {
    return data.containsKey(key);
  }

  /**
   * Test if two annotation objects object are equals.
   * @param o Object to test
   * @return true if the two object are equals
   */
  public boolean equals(final Object o) {

    if (o == null || !(o instanceof Annotation))
      return false;

    Annotation a = (Annotation) o;

    EqualsBuilder eb = new EqualsBuilder().appendSuper(super.equals(o)).append(
        size(), a.size());

    Iterator it = iterator();

    while (it.hasNext()) {

      String key = (String) it.next();
      eb.append(getProperty(key), a.getProperty(key));
    }

    return eb.isEquals();
  }

  /**
   * Returns a hash code value for the object.
   * @return a hash code value for this object
   */
  public int hashCode() {

    HashCodeBuilder hcb = new HashCodeBuilder(HASHCODE_ODD_NUMBER_1,
        HASHCODE_ODD_NUMBER_2);

    hcb.append(data);

    return hcb.toHashCode();
  }

}
