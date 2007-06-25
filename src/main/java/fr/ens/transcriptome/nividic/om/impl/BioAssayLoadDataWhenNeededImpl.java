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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import fr.ens.transcriptome.nividic.om.BioAssay;
import fr.ens.transcriptome.nividic.om.io.FieldNameConverter;

/**
 * This abstract class allow to load data in BioAssay objects only when neeed
 * @author Laurent Jourdren
 */
public abstract class BioAssayLoadDataWhenNeededImpl extends BioAssayImpl
    implements Serializable {

  /** serial version for serialization. */
  static final long serialVersionUID = 6766133047639912494L;

  // Load fields
  private Map<String, Boolean> fieldnames = new HashMap<String, Boolean>();
  private FieldNameConverter converter;

  //
  // Loads methods
  //

  /**
   * Get the converter of field names.
   * @return Returns the converter
   */
  public FieldNameConverter getConverter() {
    return converter;
  }

  /**
   * Set the converter of field names.
   * @param converter The converter to set
   */
  public void setConverter(final FieldNameConverter converter) {
    this.converter = converter;
  }

  //
  // abstract methods
  //

  /**
   * Test if data must be loaded.
   * @return true if data must be loaded
   */
  public abstract boolean isLoader();

  /**
   * Test if this field exists.
   * @param field The fieldname
   * @return <b>true </b> if the field exists
   */
  public boolean isField(final String field) {

    if (isLoader()) {

      if (BioAssay.FIELD_NAME_LOCATION.equals(field))
        return super.isField(BioAssay.FIELD_NAME_LOCATION);

      String newField = field;

      if (getConverter() != null)
        newField = getConverter().getOtherFieldName(field);

      if (!isFieldLoaded(newField))
        return isFieldExists(newField);
    }

    return true;
  }

  /**
   * Test if this field exists.
   * @param field The fieldname
   * @return <b>true </b> if the field exists
   */
  public abstract boolean isFieldExists(final String field);

  /**
   * load the annotation of the bioAssay.
   */
  public abstract void loadAnnotations();

  /**
   * load the existing field names of the bioassay.
   * @return An array of field names.
   */
  public abstract String[] loadFieldNames();

  /**
   * Return an integer array with all data from a field.
   * @param field The field to get
   * @return A vector containing the field data
   */
  public abstract int[] loadDataFieldInt(final String field);

  /**
   * Return a double array with all data from a field.
   * @param field The field to get
   * @return A vector containing the field data
   */
  public abstract double[] loadDataFieldDouble(final String field);

  /**
   * Return a string array with all data from a field.
   * @param field The field to get
   * @return A vector containing the field data
   */
  public abstract String[] loadDataFieldString(final String field);

  /**
   * Return an integer array with encoded data location.
   */
  public abstract void setLocationField();

  //
  // Overrided methods
  //

  /**
   * Return an integer array with all data from a field.
   * @param field The field to get
   * @return A vector containing the field's data
   */
  public int[] getDataFieldInt(final String field) {

    if (isLoader()) {

      if (BioAssay.FIELD_NAME_LOCATION.equals(field))
        return super.getDataFieldInt(BioAssay.FIELD_NAME_LOCATION);

      String newField = field;

      if (getConverter() != null)
        newField = getConverter().getOtherFieldName(field);

      if (!isFieldLoaded(newField)) {

        int[] result = loadDataFieldInt(newField);

        setDataFieldInt(field, result);

        setFieldLoadedStatus(newField, true);
        return result;
      }
    }

    return super.getDataFieldInt(field);
  }

  /**
   * Return a doubel array with all data from a field.
   * @param field The field to get
   * @return A vector containing the field's data
   */
  public double[] getDataFieldDouble(final String field) {

    if (isLoader()) {

      String newField = field;

      if (getConverter() != null)
        newField = getConverter().getOtherFieldName(field);

      if (!isFieldLoaded(newField)) {

        double[] result = loadDataFieldDouble(newField);

        setDataFieldDouble(field, result);

        setFieldLoadedStatus(newField, true);
        return result;
      }
    }

    return super.getDataFieldDouble(field);
  }

  /**
   * Return a string array with all data from a field.
   * @param field The field to get
   * @return A vector containing the field's data
   */
  public String[] getDataFieldString(final String field) {

    if (isLoader()) {

      String newField = field;

      if (getConverter() != null)
        newField = getConverter().getOtherFieldName(field);

      if (!isFieldLoaded(newField)) {

        String[] result = loadDataFieldString(newField);

        setDataFieldString(field, result);

        setFieldLoadedStatus(newField, true);
        return result;
      }
    }

    return super.getDataFieldString(field);
  }

  /**
   * Return a array containing all the fields of the object.
   * @return A VectorString with all the fieldnames
   */
  public String[] getFields() {

    if (!isLoader())
      return super.getFields();

    String[] fields = super.getFields();

    Map<String, String> m = new HashMap<String, String>();
    for (int i = 0; i < fields.length; i++)
      m.put(fields[i], "");

    Iterator<String> it = this.fieldnames.keySet().iterator();
    while (it.hasNext())
      m.put(it.next(), "");

    String[] result = new String[m.size()];

    it = m.keySet().iterator();
    int i = 0;
    while (it.hasNext())
      result[i++] = it.next();

    return result;
  }

  /**
   * Load Annotations and field names.
   */
  protected void loadAnnotationsAndFieldNames() {

    loadAnnotations();
    String[] fdn = loadFieldNames();

    if (fdn != null)
      for (int i = 0; i < fdn.length; i++)
        this.fieldnames.put(fdn[i], Boolean.FALSE);
  }

  /**
   * Test if a field is loaded.
   * @param field Field to test
   * @return true if the field is already loaded
   */
  private boolean isFieldLoaded(final String field) {

    return Boolean.TRUE.equals(this.fieldnames.get(field));
  }

  /**
   * Set the loaded status of a field.
   * @param field Field to set
   * @param loaded Status of the field
   */
  private void setFieldLoadedStatus(final String field, final boolean loaded) {
    this.fieldnames.put(field, loaded);
  }

}