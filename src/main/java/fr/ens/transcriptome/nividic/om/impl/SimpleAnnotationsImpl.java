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
 * of the �cole Normale Sup�rieure and the individual authors.
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
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import fr.ens.transcriptome.nividic.om.Annotation;
import fr.ens.transcriptome.nividic.util.NividicUtils;

/**
 * This class define annotations for biological objects
 * @author Laurent Jourdren
 */
public class SimpleAnnotationsImpl implements Annotation, Serializable {

  static final long serialVersionUID = -3706417043759109770L;

  private Map<String, String> data = new LinkedHashMap<String, String>();

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

    for (String key : annotations.keySet())
      setProperty(key, annotations.getProperty(key));

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
    return this.data.get(key);
  }

  /**
   * Remove a property.
   * @param key Key of the property to remove
   * @return The value of the property removed
   */
  public String removeProperty(final String key) {
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

  /**
   * Returns the properties keys.
   * @return All the annotations
   */
  public String[] getPropertiesKeys() {

    return NividicUtils.toArray(this.data.keySet());
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

    EqualsBuilder eb =
        new EqualsBuilder().appendSuper(super.equals(o)).append(size(),
            a.size());

    for (String key : keySet()) {
      eb.append(getProperty(key), a.getProperty(key));
    }

    return eb.isEquals();
  }

  /**
   * Returns a hash code value for the object.
   * @return a hash code value for this object
   */
  public int hashCode() {

    HashCodeBuilder hcb =
        new HashCodeBuilder(HASHCODE_ODD_NUMBER_1, HASHCODE_ODD_NUMBER_2);

    hcb.append(data);

    return hcb.toHashCode();
  }

}
