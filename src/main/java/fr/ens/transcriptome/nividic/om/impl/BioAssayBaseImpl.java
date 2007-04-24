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
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.collections.primitives.ArrayIntList;

import fr.ens.transcriptome.nividic.NividicRuntimeException;
import fr.ens.transcriptome.nividic.om.BioAssayBase;
import fr.ens.transcriptome.nividic.om.BioAssayRuntimeException;

/**
 * Implementation 100% java de BioAssayBase.
 * @author Laurent Jourdren
 * @author Vincent Frouin
 * @author Miriame Allouche
 */
class BioAssayBaseImpl implements BioAssayBase, Serializable {

  /** serial version for serialization. */
  static final long serialVersionUID = -7886253794891000605L;

  /*
   * TODO Forcer les champs a avoir tous la meme longueur. Creer un champ length
   * contenant la longueur du tableau. Si un champ d'une longueur differente est
   * ajouter doit jeter une exception. Si un des vecteur est modifié (dans sa
   * taille) doit jetter une exception. Ceci doit etre possible en utilisant le
   * mecanisme de Listener.
   */

  private Map<String, String[]> hashString = new HashMap<String, String[]>();
  private Map<String, int[]> hashInteger = new HashMap<String, int[]>();
  private Map<String, double[]> hashDouble = new HashMap<String, double[]>();
  private Map<String, Integer> hashDir = new HashMap<String, Integer>();
  private Map<Integer, Integer> indexLoc = new HashMap<Integer, Integer>();
  private Map<String, int[]> references = new HashMap<String, int[]>();
  private String referenceField;
  private int size = -1;

  //
  // Getters
  //

  /**
   * Return an integer array with all data from a field.
   * @param field The field to get
   * @return A vector containing the field's data
   */
  public int[] getDataFieldInt(final String field) {

    if (field == null)
      return null;

    return this.hashInteger.get(field);
  }

  /**
   * Return a doubel array with all data from a field.
   * @param field The field to get
   * @return A vector containing the field's data
   */
  public double[] getDataFieldDouble(final String field) {

    if (field == null)
      return null;
    return this.hashDouble.get(field);
  }

  /**
   * Return a string array with all data from a field.
   * @param field The field to get
   * @return A vector containing the field's data
   */
  public String[] getDataFieldString(final String field) {

    if (field == null)
      return null;
    return this.hashString.get(field);
  }

  /**
   * Return a array containing all the fields of the object.
   * @return A VectorString with all the fieldnames
   */
  public String[] getFields() {

    String[] result = new String[hashDir.size()];
    int i = 0;

    Iterator it = hashDir.keySet().iterator();
    while (it.hasNext())
      result[i++] = (String) it.next();

    return result;
  }

  /**
   * Get the number of fields in the bioAssay.
   * @return the count of fields in the bioAssay
   */
  public int getFieldCount() {

    return hashDir.size();
  }

  /**
   * Returns an array with the location of all spots.
   * @return All the locations
   */
  public int[] getLocations() {
    return getDataFieldInt(FIELD_NAME_LOCATION);
  }

  /**
   * Get the reference field.
   * @return The reference field if exists
   */
  public String getReferenceField() {
    return this.referenceField;

  }

  /**
   * Test if a reference field exists.
   * @return <b>true </b> if the reference field is set
   */
  public boolean isReference() {
    return this.referenceField == null;
  }

  /**
   * Test if this field exists.
   * @param field The fieldname
   * @return <b>true </b> if the field exists
   */
  public boolean isField(final String field) {

    if (field == null)
      return false;
    return this.hashDir.containsKey(field);
  }

  //
  // Setters
  //

  /**
   * Set the data for a field.
   * @param field The field to be set
   * @param value The data of the field
   * @throws BioAssayRuntimeException if value is null or if value size if wrong
   */
  public void setDataFieldInt(final String field, final int[] value)
      throws BioAssayRuntimeException {

    if (field == null || value == null)
      throw new BioAssayRuntimeException(NividicRuntimeException.NULL_POINTER,
          "field or value");

    if (this.size == -1)
      this.size = value.length;
    else if (this.size != value.length)
      throw new BioAssayRuntimeException(
          NividicRuntimeException.INVALID_ARGUMENT,
          "array length is different from BioAssay size");

    // if the same fieldname with another type already exists remove it
    if (isField(field) && getFieldType(field) != BioAssayBase.DATATYPE_INTEGER)
      removeField(field);

    // met à jour la map _dir à chaque fois qu'il y a un setData()
    this.hashInteger.put(field, value);
    this.hashDir.put(field, new Integer(BioAssayBase.DATATYPE_INTEGER));

  }

  /**
   * Set the data for a field.
   * @param field The field to be set
   * @param value The data of the field
   * @throws BioAssayRuntimeException if value is null or if value size if wrong
   */
  public void setDataFieldDouble(final String field, final double[] value)
      throws BioAssayRuntimeException {

    if (field == null || value == null)
      throw new BioAssayRuntimeException(NividicRuntimeException.NULL_POINTER,
          "field or value");

    if (this.size == -1)
      this.size = value.length;
    else if (this.size != value.length)
      throw new BioAssayRuntimeException(
          NividicRuntimeException.INVALID_ARGUMENT,
          "array length is different from BioAssay size");

    // if the same fieldname with another type already exists remove it
    if (isField(field) && getFieldType(field) != BioAssayBase.DATATYPE_DOUBLE)
      removeField(field);

    // met à jour la map _dir à chaque fois qu'il y a un setData()
    this.hashDouble.put(field, value);
    this.hashDir.put(field, new Integer(BioAssayBase.DATATYPE_DOUBLE));
  }

  /**
   * Set the data for a field.
   * @param field The field to be set
   * @param value The field data to be set
   * @throws BioAssayRuntimeException if value is null or if value size if wrong
   */
  public void setDataFieldString(final String field, final String[] value)
      throws BioAssayRuntimeException {

    if (field == null || value == null)
      throw new BioAssayRuntimeException(NividicRuntimeException.NULL_POINTER,
          "field or value");

    if (this.size == -1)
      this.size = value.length;
    else if (this.size != value.length)
      throw new BioAssayRuntimeException(
          NividicRuntimeException.INVALID_ARGUMENT,
          "array length is different from BioAssay size");

    // if the same fieldname with another type already exists remove it
    if (isField(field) && getFieldType(field) != BioAssayBase.DATATYPE_STRING)
      removeField(field);

    // met à jour la map _dir à chaque fois qu'il y a un setData()
    this.hashString.put(field, value);
    this.hashDir.put(field, new Integer(BioAssayBase.DATATYPE_STRING));

    if (isReference() && field.equals(getReferenceField()))
      makeReferences();

  }

  /**
   * Set locations for the BioAssay Object.
   * @param locations The locations
   */
  public void setLocations(final int[] locations) {

    if (locations == null)
      return;

    setDataFieldInt(FIELD_NAME_LOCATION, locations);

    // reconstruction de l'index
    this.indexLoc.clear();
    for (int i = 0; i < locations.length; i++)
      this.indexLoc.put(locations[i], i);

  }

  /**
   * Test if locations data exists.
   * @return <b>true </b> if location data exists
   */
  public boolean isLocations() {
    return this.isField(FIELD_NAME_LOCATION);
  }

  /**
   * remove the locations.
   * @return <b>true </b> if remove is ok
   */
  public boolean removeLocations() {
    return removeField(FIELD_NAME_LOCATION);
  }

  /**
   * Set the reference fieldname.
   * @param name The name of reference field
   * @param locations The locations
   */
  public void setReference(final String name, final int[] locations) {

    if (name == null)
      throw new BioAssayRuntimeException(NividicRuntimeException.NULL_POINTER,
          "name");

    if (this.size == -1)
      this.size = locations.length;
    else if (this.size != locations.length)
      throw new BioAssayRuntimeException(
          BioAssayRuntimeException.INVALID_ARGUMENT,
          "array length is different from BioAssay size");
    this.references.put(name, locations);
  }

  //
  // Other methods
  //

  /**
   * Remove a field.
   * @param field The field to be removed
   * @return <b>true </b> if remove is ok
   */
  public boolean removeField(final String field) {

    if (!isField(field))
      return false;

    switch (getFieldType(field)) {

    case BioAssayBase.DATATYPE_INTEGER:
      this.hashInteger.remove(field);
      this.hashDir.remove(field);
      if (field == FIELD_NAME_LOCATION)
        indexLoc.clear();
      break;

    case BioAssayBase.DATATYPE_DOUBLE:
      this.hashDouble.remove(field);
      this.hashDir.remove(field);
      break;

    case BioAssayBase.DATATYPE_STRING:
      this.hashString.remove(field);
      this.hashDir.remove(field);
      break;

    default:
      break;
    }
    return true;
  }

  /**
   * Get the type of a field.
   * @param field The fieldname
   * @return the type of the field name or -1 if the field doesn't exits
   */
  public int getFieldType(final String field) {

    if (!this.hashDir.containsKey(field))
      return -1;
    return this.hashDir.get(field);
  }

  /**
   * Return the array index of a location.
   * @param location The location
   * @return The index of the location
   */
  public int getIndexFromALocation(final int location) {

    Object o = indexLoc.get(new Integer(location));

    if (o == null)
      return -1;

    return ((Integer) o).intValue();
  }

  /**
   * Return the length of the array data.
   * @return The length of the array data
   */
  public int size() {

    if (this.size == -1)
      return 0;

    return this.size;
  }

  /**
   * Check if the BioAssayBase data is a true array (all the field must have the
   * same length).
   * @return <b>true </b> if the BioAssayBase data is a true array
   */
  public boolean checkArray() {

    int len = -1;

    Iterator is = this.hashString.keySet().iterator();
    while (is.hasNext()) {
      int newlen = this.hashString.get(is.next()).length;
      if (len == -1)
        len = newlen;
      else if (len != newlen)
        return false;
    }

    Iterator ii = this.hashInteger.keySet().iterator();
    while (is.hasNext()) {
      if (len != this.hashString.get(ii.next()).length)
        return false;
    }
    Iterator id = this.hashDouble.keySet().iterator();
    while (is.hasNext()) {
      if (len != (this.hashString.get(id.next()).length))
        return false;
    }

    return true;
  }

  /**
   * Set reference field,
   * @param field The name of the reference field
   */
  public void setReferenceField(final String field) {
    if (field == null)
      return;
    this.referenceField = field;
  }

  /**
   * Generate references links.
   */
  public void makeReferences() {

    if (this.referenceField == null || !isField(referenceField)
        || getFieldType(referenceField) != BioAssayBase.DATATYPE_STRING
        || !isLocations()
        || getLocations().length != getDataFieldString(referenceField).length)
      return;

    // clear the references hashmap
    this.references.clear();

    // For all key of reference field
    String[] refs = getDataFieldString(referenceField);
    for (int i = 0; i < refs.length; i++) {

      String id = refs[i];

      // ArrayIntList locs = (ArrayIntList) this.references.get(id);
      int[] locs = this.references.get(id);

      if (locs == null) {

        locs = new int[1];
        locs[0] = i;

        this.references.put(id, locs);
      } else {

        int[] newLocs = new int[locs.length + 1];
        System.arraycopy(locs, 0, newLocs, 0, locs.length);
        newLocs[newLocs.length - 1] = i;
        this.references.put(id, newLocs);
      }
    }

  }

  /**
   * Remove the reference field.
   * @return <b>true </b> if remove is ok
   */
  public boolean removeReferenceField() {
    this.referenceField = null;
    this.references.clear();
    return true;
  }

  /**
   * Return an array index vector from an indentifier reference.
   * @param id The identifier reference
   * @return A vector containing all the locations of a spot
   */
  public int[] getIndexesFromAReference(final String id) {

    if (id == null)
      return null;

    return this.references.get(id);
  }

  /**
   * Clear data in the object.
   */
  public void clear() {

    this.hashString.clear();
    this.hashInteger.clear();
    this.hashDouble.clear();
    this.hashDir.clear();
    this.indexLoc.clear();
    this.references.clear();
    this.referenceField = null;
    this.size = -1;
  }

  /**
   * Test if the size of the data equals bioAssaySize,
   * @param data Data to test
   * @return true if data is not null or its size is wrong
   */
  private boolean testSize(final int[] data) {

    if (data == null)
      return false;

    if (this.size == -1)
      return true;
    return data.length == this.size;

  }

  /**
   * Test if the size of the data equals bioAssaySize,
   * @param data Data to test
   * @return true if data is not null or its size is wrong
   */
  private boolean testSize(final double[] data) {

    if (data == null)
      return false;

    if (this.size == -1)
      return true;
    return data.length == this.size;
  }

  /**
   * Test if the size of the data equals bioAssaySize,
   * @param data Data to test
   * @return true if data is not null or its size is wrong
   */
  private boolean testSize(final String[] data) {

    if (data == null)
      return false;

    if (this.size == -1)
      return true;
    return data.length == this.size;
  }

  /**
   * Rename a field.
   * @param oldName Name of the field to rename
   * @param newName New name of the field
   */
  public void renameField(final String oldName, final String newName) {

    if (oldName == null || newName == null)
      return;

    if (!isField(oldName))
      throw new BioAssayRuntimeException("Unknown Field");
    if (isField(newName))
      throw new BioAssayRuntimeException(
          "A field with the new name of the field already exists");

    if (FIELD_NAME_LOCATION.equals(oldName)
        || FIELD_NAME_LOCATION.equals(newName))
      throw new BioAssayRuntimeException("You can't rename the locations field");

    final int fieldType = getFieldType(oldName);

    switch (fieldType) {

    case BioAssayBase.DATATYPE_INTEGER:

      int[] intValues = this.hashInteger.get(oldName);
      this.hashInteger.remove(oldName);
      this.hashInteger.put(newName, intValues);
      break;

    case BioAssayBase.DATATYPE_DOUBLE:

      double[] doubleValues = this.hashDouble.get(oldName);
      this.hashDouble.remove(oldName);
      this.hashDouble.put(newName, doubleValues);
      break;

    case BioAssayBase.DATATYPE_STRING:

      String[] stringValues = this.hashString.get(oldName);
      this.hashString.remove(oldName);
      this.hashString.put(newName, stringValues);
      break;

    default:
      break;
    }

    if (oldName.equals(this.referenceField))
      this.referenceField = newName;
  }

  /**
   * Swap two fields
   * @param fieldA First field to swap
   * @param fieldB Second field to swap
   */
  public void swapFields(final String fieldA, final String fieldB) {

    if (fieldA == null || fieldB == null)
      throw new NullPointerException("One or more fieldname is null");

    if (!isField(fieldA) || !isField(fieldB))
      new NividicRuntimeException("One ore more field doesn't exists");

    if (this.getFieldType(fieldA) != this.getFieldType(fieldB))
      throw new NividicRuntimeException(
          "The types of the two fields are not the same");

    switch (getFieldType(fieldA)) {

    case BioAssayBase.DATATYPE_DOUBLE:

      double[] da = getDataFieldDouble(fieldA);
      double[] db = getDataFieldDouble(fieldB);
      setDataFieldDouble(fieldA, db);
      setDataFieldDouble(fieldB, da);
      break;

    case BioAssayBase.DATATYPE_INTEGER:

      int[] ia = getDataFieldInt(fieldA);
      int[] ib = getDataFieldInt(fieldB);
      setDataFieldInt(fieldA, ib);
      setDataFieldInt(fieldB, ia);
      break;

    case BioAssayBase.DATATYPE_STRING:

      String[] sa = getDataFieldString(fieldA);
      String[] sb = getDataFieldString(fieldB);
      setDataFieldString(fieldA, sb);
      setDataFieldString(fieldB, sa);
      break;

    default:
      break;
    }

  }

  //
  // Constructors
  //

  /**
   * Create a BioAssay object. The object's name if ramdomly generated,
   */
  BioAssayBaseImpl() {

  }

}