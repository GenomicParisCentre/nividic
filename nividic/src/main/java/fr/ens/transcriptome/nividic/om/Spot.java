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
 * This interface defines a Spot. This code is based on previous work in c++
 * developped at the Service de Génomique Fonctionelle of The Commissariat à
 * l'Énergie Atomique (CEA) by Laurent Jourdren, Miramme Allouche and Vincent
 * Frouin.
 * @author Laurent Jourdren
 * @author Miramme Allouche
 */
public interface Spot {

  /**
   * Get the bioAssay of the spot.
   * @return The bioAssay of the spot
   */
  BioAssay getBioAssay();

  /**
   * Get the index of the spot in the bioAssay object.
   * @return Returns the index
   */
  int getIndex();

  /**
   * Get the red value.
   * @return An int vector containing the data of the field
   */
  int getRed();

  /**
   * Get the green value.
   * @return An int vector containing the data of the field
   */
  int getGreen();

  /**
   * Get the identifier value.
   * @return An int vector containing the data of the field
   */
  String getId();

  /**
   * Get the flag value.
   * @return <b>true </b> if the field exists.
   */
  int getFlag();

  /**
   * Get the description value.
   * @return An array of string of the description.
   */
  String getDescription();

  /**
   * Get the value of a coordinates of a MA plot.
   * @return a coordinate of a MA plot.
   */
  double getA();

  /**
   * Get the value of m coordinates of a MA plot.
   * @return m coordinate of a MA plot.
   */
  double getM();

  /**
   * Get the value of the standard deviation for A values.
   * @return the value of the standard deviation for A values
   */
  double getStdDevA();

  /**
   * Get the value of the standard deviation for M values.
   * @return the value of the standard deviation for M values
   */
  double getStdDevM();

  /**
   * Test if the green field exits.
   * @return <b>true </b> if the field exists.
   */
  boolean isGreen();

  /**
   * Test if the red field exits.
   * @return <b>true </b> if the field exists.
   */
  boolean isRed();

  /**
   * Test if the ratio field exits.
   * @return <b>true </b> if the field exists.
   */
  boolean isRatio();

  /**
   * Test if the flags field exits.
   * @return <b>true </b> if the field exists.
   */
  boolean isFlag();

  /**
   * Get the brights value.
   * @return the bright value
   */
  double getBright();

  /**
   * Test if the bright field exits.
   * @return <b>true </b> if the field exists.
   */
  boolean isBright();

  /**
   * Test if the identifier field exits.
   * @return <b>true </b> if the field exists.
   */
  boolean isId();

  /**
   * Test if the description field is set.
   * @return true if the description field is set
   */
  boolean isDescription();

  /**
   * Test if the a field is set.
   * @return true if the a field is set
   */
  boolean isA();

  /**
   * Test if the m field is set.
   * @return true if the m field is set
   */
  boolean isM();

  /**
   * Test if the the standard deviation for a values field is set.
   * @return true if the field set
   */
  boolean isStdDevA();

  /**
   * Test if the the standard deviation for m values field is set.
   * @return true if the field set
   */
  boolean isStdDevM();

  /**
   * Test if the location field is set.
   * @return true if the location field is set
   */
  boolean isLocation();

  /**
   * Get the ratios data.
   * @return An int vector containing the data of the field
   */
  double getRatio();

  /**
   * Get the location of the spot.
   * @return The location of the spot
   */
  int getLocation();

  /**
   * Get the meta row of the spot.
   * @return The meta row of the spot
   */
  int getMetaRow();

  /**
   * Get the meta column of the spot.
   * @return The meta column of the spot
   */
  int getMetaColumn();

  /**
   * Get the row of the spot.
   * @return The row of the spot
   */
  int getRow();

  /**
   * Get the column of the spot.
   * @return The column of the spot
   */
  int getColumn();

  /**
   * Test if the spot is labeled as empty. This method use default "empty"
   * identifers.
   * @return true if the spot is labeled as empty
   */
  boolean isEmpty();

  /**
   * Test if the flag of the spot is "bad".
   * @return true if the flag of the spot is "bad"
   */
  boolean isBadFlag();

  /**
   * Test if the flag of the spot is "abscent".
   * @return true if the flag of the spot is "abscent"
   */
  boolean isFlagAbscent();

  /**
   * Test if the flag of the spot is "not found".
   * @return true if the flag of the spot is "not found"
   */
  boolean isFlagNotFound();

  /**
   * Test if the flag of the spot is "unflagged".
   * @return true if the flag of the spot is "unflagged"
   */
  boolean isFlagUnflagged();

  /**
   * Test if the flag of the spot is "good".
   * @return true if the flag of the spot is "good"
   */
  boolean isFlagGood();

  /**
   * Test if the flag of the spot is "normalized".
   * @return true if the flag of the spot is "normalized"
   */
  boolean isFlagNormalized();

  /**
   * Set the index of the spot.
   * @param index The index to set
   */
  void setIndex(final int index);

  /**
   * Set the flag value.
   * @param flag The flag value to set
   * @throws BioAssayRuntimeException if flags is null or if flags size if wrong
   */
  void setFlag(final int flag) throws BioAssayRuntimeException;

  /**
   * Set the green value.
   * @param green The green data to set
   * @throws BioAssayRuntimeException if greens is null or if greens size if
   *           wrong
   */
  void setGreen(final int green) throws BioAssayRuntimeException;

  /**
   * Set the red value.
   * @param red The red data to set
   * @throws BioAssayRuntimeException if reds is null or if reds size if wrong
   */
  void setRed(final int red) throws BioAssayRuntimeException;

  /**
   * Set the ratio value.
   * @param ratio The ratios value to set
   * @throws BioAssayRuntimeException if ratios is null or if ratios size if
   *           wrong
   */
  void setRatio(final double ratio) throws BioAssayRuntimeException;

  /**
   * Set the identifier value.
   * @param id The identifiers data to set
   * @throws BioAssayRuntimeException if ids is null or if ids size if wrong
   */
  void setId(final String id) throws BioAssayRuntimeException;

  /**
   * Set the bright value.
   * @param bright The brights data to set
   * @throws BioAssayRuntimeException if brights is null or if brights size if
   *           wrong
   */
  void setBright(final double bright) throws BioAssayRuntimeException;

  /**
   * Set the description field.
   * @param description Array of descriptions to set
   * @throws BioAssayRuntimeException if descriptions is null or if descriptions
   *           size if wrong
   */
  void setDescription(final String description) throws BioAssayRuntimeException;

  /**
   * Set the a value.
   * @param a The a coordinate to set
   * @throws BioAssayRuntimeException if as is null or if as size if wrong
   */
  void setA(final double a) throws BioAssayRuntimeException;

  /**
   * Set the m field.
   * @param m The m coordinate to set
   * @throws BioAssayRuntimeException if ms is null or if ms size if wrong
   */
  void setM(final double m) throws BioAssayRuntimeException;

  /**
   * Set the standard deviation for a value.
   * @param stdDevA The standard deviation for a value to set
   * @throws BioAssayRuntimeException if as is null or if as size if wrong
   */
  void setStdDevA(final double stdDevA) throws BioAssayRuntimeException;

  /**
   * Set the standard deviation for m value.
   * @param stdDevM The standard deviation for m value to set
   * @throws BioAssayRuntimeException if ms is null or if ms size if wrong
   */
  void setStdDevM(final double stdDevM) throws BioAssayRuntimeException;

  /**
   * Set the location of the spot.
   * @param location The location to set
   * @throws BioAssayRuntimeException if location field is null
   */
  void setLocation(final int location) throws BioAssayRuntimeException;

  /**
   * Set meta row of the spot.
   * @param metaRow The meta row to set
   * @throws BioAssayRuntimeException if location field is null
   */
  void setMetaRow(final int metaRow) throws BioAssayRuntimeException;

  /**
   * Set meta column of the spot.
   * @param metaColumn The meta column to set
   * @throws BioAssayRuntimeException if location field is null
   */
  void setMetaColumn(final int metaColumn) throws BioAssayRuntimeException;

  /**
   * Set the row of the spot.
   * @param row The row to set
   * @throws BioAssayRuntimeException if location field is null
   */
  void setRow(final int row) throws BioAssayRuntimeException;

  /**
   * Set the column of the spot .
   * @param column The column to set
   * @throws BioAssayRuntimeException if location field is null
   */
  void setColumn(final int column) throws BioAssayRuntimeException;

  /**
   * Set an integer value in a field
   * @param field Field to set
   * @param value Value to set
   * @throws BioAssayRuntimeException if the field doesn exists
   */
  void setDataFieldInt(final String field, final int value)
      throws BioAssayRuntimeException;

  /**
   * Set an double value in a field
   * @param field Field to set
   * @param value Value to set
   * @throws BioAssayRuntimeException if the field doesn exists
   */
  void setDataFieldDouble(final String field, final double value)
      throws BioAssayRuntimeException;

  /**
   * Set an string value in a field
   * @param field Field to set
   * @param value Value to set
   * @throws BioAssayRuntimeException if the field doesn exists
   */
  void setDataFieldString(final String field, final String value)
      throws BioAssayRuntimeException;

  /**
   * Get the integer value of a field.
   * @param field The field
   * @return the integer value of the spot
   */
  int getDataFieldInt(final String field);

  /**
   * Get the double value of a field.
   * @param field The field
   * @return the double value of the spot
   */
  double getDataFieldDouble(final String field);

  /**
   * Get the string value of a field.
   * @param field The field
   * @return the string value of the spot
   */
  String getDataFieldString(final String field);

  /**
   * Test if the field exits.
   * @param field Field to test
   * @return true if the field exists
   */
  boolean isField(final String field);

}