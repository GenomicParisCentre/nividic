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

/**
 * This interface is the parent interface of BioAssay interface. This code is
 * based on previous work in c++ developped at the Service de Génomique
 * Fonctionelle of The Commissariat à l'Énergie Atomique (CEA) by Laurent
 * Jourdren, Miramme Allouche and Vincent Frouin.
 * @author Laurent Jourdren
 * @author Vincent Frouin
 * @author Miriame Allouche
 */
public interface BioAssayBase {

  /*
   * TODO Use generic with java 1.5 ( x3 setData ans getData methods) TODO
   * Replace Vector with more adapted dynamic array
   */
  /** Field name for locations, */
  String FIELD_NAME_LOCATION = "location";

  //
  // Datatypes
  //

  /* TODO use enum with java 1.5 */

  /** Integer data type. */
  int DATATYPE_INTEGER = 1;
  /** Double data type. */
  int DATATYPE_DOUBLE = 2;
  /** String data type. */
  int DATATYPE_STRING = 3;

  /*
   * TODO find the bests vector and hashtable
   */

  //
  // Getters
  //
  /**
   * Return an integer array with all data from a field.
   * @param field The field to get
   * @return A vector containing the field's data
   */
  int[] getDataFieldInt(String field);

  /**
   * Return a doubel array with all data from a field.
   * @param field The field to get
   * @return A vector containing the field's data
   */
  double[] getDataFieldDouble(String field);

  /**
   * Return a string array with all data from a field.
   * @param field The field to get
   * @return A vector containing the field's data
   */
  String[] getDataFieldString(String field);

  /**
   * Return a array containing all the fields of the object.
   * @return A VectorString with all the fieldnames
   */
  String[] getFields();

  /**
   * Returns an array with the location of all spots.
   * @return All the locations
   */
  int[] getLocations();

  /**
   * Test if a reference field exists.
   * @return <b>true </b> if the reference field is set
   */
  boolean isReference();

  /**
   * Get the reference field.
   * @return The reference field if exists
   */
  String getReferenceField();

  /**
   * Test if this field exists.
   * @param field The fieldname
   * @return <b>true </b> if the field exists
   */
  boolean isField(String field);

  //
  // Setters
  //

  /**
   * Set the data for a field.
   * @param field The field to be set
   * @param value The data of the field
   * @throws BioAssayRuntimeException if value is null or if value size if wrong
   */
  void setDataFieldInt(String field, int[] value)
      throws BioAssayRuntimeException;

  /**
   * Set the data for a field.
   * @param field The field to be set
   * @param value The data of the field
   * @throws BioAssayRuntimeException if value is null or if value size if wrong
   */
  void setDataFieldDouble(String field, double[] value)
      throws BioAssayRuntimeException;

  /**
   * Set the data for a field.
   * @param field The field to be set
   * @param value The field data to be set
   * @throws BioAssayRuntimeException if value is null or if value size if wrong
   */
  void setDataFieldString(String field, String[] value)
      throws BioAssayRuntimeException;

  /**
   * Set locations for the BioAssay Object.
   * @param locs The locations
   */
  void setLocations(int[] locs);

  /**
   * remove the locations.
   * @return <b>true </b> if remove is ok
   */
  boolean removeLocations();

  /**
   * Set the reference fieldname.
   * @param field The reference field
   */
  void setReferenceField(String field);

  /**
   * Set the reference fieldname.
   * @param name The name of reference field
   * @param locations The locations
   * @throws BioAssayRuntimeException if value is null or if value size if wrong
   */
  void setReference(final String name, final int[] locations)
      throws BioAssayRuntimeException;

  //
  // Other methods
  //

  /**
   * Remove a field.
   * @param field The field to be removed
   * @return <b>true </b> if remove is ok
   */
  boolean removeField(String field);

  /**
   * Remove the reference field.
   * @return <b>true </b> if remove is ok
   */
  boolean removeReferenceField();

  /**
   * Get the type of a field.
   * @param field The fieldname
   * @return the type of the field name
   */
  int getFieldType(String field);

  /**
   * Test if locations data exists.
   * @return <b>true </b> if location data exists
   */
  boolean isLocations();

  /**
   * Return the array index of a location.
   * @param location The location
   * @return The index of the location
   */
  int getIndexFromALocation(int location);

  /**
   * Generate references links.
   */
  void makeReferences();

  /**
   * Return an array index vector from an indentifier reference.
   * @param id The identifier reference
   * @return A vector containing all the locations of a spot
   */
  int[] getIndexesFromAReference(String id);

  /**
   * Return the length of the array data.
   * @return The length of the array data
   */
  int size();

  /**
   * Check if the BioAssayBase data is a true array (all the field must have the
   * same length).
   * @return <b>true </b> if the BioAssayBase data is a true array
   */
  boolean checkArray();

  /**
   * Clear data in the object
   */
  void clear();

  /**
   * Rename a field.
   * @param oldName The old name of the field
   * @param newName The new name of the field
   */
  void renameField(String oldName, String newName);

  /**
   * Swap two fields
   * @param fieldA First field to swap
   * @param fieldB Second field to swap
   */
  void swapFields(String fieldA, String fieldB);

}