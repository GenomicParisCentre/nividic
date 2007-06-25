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

import fr.ens.transcriptome.nividic.om.BioAssay;
import fr.ens.transcriptome.nividic.om.BioAssayRuntimeException;
import fr.ens.transcriptome.nividic.om.BioAssayUtils;
import fr.ens.transcriptome.nividic.om.Spot;

/**
 * @author Laurent Jourdren
 * @author Vincent Frouin
 * @author Miriame Allouche
 */
public class SpotImpl implements Spot, Serializable {

  /** serial version for serialization. */
  static final long serialVersionUID = 8549481801283788203L;

  private BioAssay bioAssay;
  private int index;

  private double[] as;
  private double[] brights;
  private int[] locations;
  private String[] descriptions;
  private int[] flags;
  private int[] greens;
  private String[] ids;
  private double[] ms;
  private double[] ratios;
  private int[] reds;
  private double[] stdDevA;
  private double[] stdDevM;
  private int bioAssaySize;
  private boolean indexChange = true;

  //
  // Getters
  //

  /**
   * Set the bioAssay object.
   * @return Returns the bioAssay
   */
  public final BioAssay getBioAssay() {
    if (bioAssay == null)
      throw new BioAssayRuntimeException(BioAssayRuntimeException.NULL_POINTER,
          "No bioAssay exists");
    return bioAssay;
  }

  /**
   * Get the index of the spot in the bioAssay object.
   * @return Returns the index
   */
  public final int getIndex() {
    return index;
  }

  /**
   * Get the red value.
   * @return An int vector containing the data of the field
   */
  public int getRed() {

    testIndex();
    if (this.reds == null)
      throw new BioAssayRuntimeException(BioAssayRuntimeException.NULL_POINTER,
          "this field doesn't exist in this bioAssay");
    return this.reds[this.index];
  }

  /**
   * Get the green value.
   * @return An int vector containing the data of the field
   */
  public int getGreen() {

    testIndex();
    if (this.greens == null)
      throw new BioAssayRuntimeException(BioAssayRuntimeException.NULL_POINTER,
          "this field doesn't exist in this bioAssay");
    return this.greens[this.index];
  }

  /**
   * Get the identifier value.
   * @return An int vector containing the data of the field
   */
  public String getId() {

    testIndex();
    if (this.ids == null)
      throw new BioAssayRuntimeException(BioAssayRuntimeException.NULL_POINTER,
          "this field doesn't exist in this bioAssay");
    return this.ids[this.index];
  }

  /**
   * Get the flag value.
   * @return <b>true </b> if the field exists.
   */
  public int getFlag() {

    testIndex();
    if (this.flags == null)
      throw new BioAssayRuntimeException(BioAssayRuntimeException.NULL_POINTER,
          "this field doesn't exist in this bioAssay");
    return this.flags[this.index];
  }

  /**
   * Get the description value.
   * @return An array of string of the description.
   */
  public String getDescription() {

    testIndex();
    if (this.descriptions == null)
      throw new BioAssayRuntimeException(BioAssayRuntimeException.NULL_POINTER,
          "this field doesn't exist in this bioAssay");
    return this.descriptions[this.index];
  }

  /**
   * Get the value of a coordinates of a MA plot.
   * @return a coordinate of a MA plot.
   */
  public double getA() {

    testIndex();
    if (this.as == null)
      throw new BioAssayRuntimeException(BioAssayRuntimeException.NULL_POINTER,
          "this field doesn't exist in this bioAssay");
    return this.as[this.index];
  }

  /**
   * Get the value of m coordinates of a MA plot.
   * @return m coordinate of a MA plot.
   */
  public double getM() {

    testIndex();
    if (this.ms == null)
      throw new BioAssayRuntimeException(BioAssayRuntimeException.NULL_POINTER,
          "this field doesn't exist in this bioAssay");
    return this.ms[this.index];
  }

  /**
   * Get the value of the standard deviation for A values.
   * @return the value of the standard deviation for A values
   */
  public double getStdDevA() {

    testIndex();
    if (this.stdDevA == null)
      throw new BioAssayRuntimeException(BioAssayRuntimeException.NULL_POINTER,
          "this field doesn't exist in this bioAssay");
    return this.stdDevA[this.index];
  }

  /**
   * Get the value of the standard deviation for M values.
   * @return the value of the standard deviation for M values
   */
  public double getStdDevM() {

    testIndex();
    if (this.stdDevM == null)
      throw new BioAssayRuntimeException(BioAssayRuntimeException.NULL_POINTER,
          "this field doesn't exist in this bioAssay");
    return this.stdDevM[this.index];
  }

  /**
   * Test if the green field exits.
   * @return <b>true </b> if the field exists.
   */
  public boolean isGreen() {
    return this.bioAssay.isGreens();
  }

  /**
   * Test if the red field exits.
   * @return <b>true </b> if the field exists.
   */
  public boolean isRed() {
    return this.bioAssay.isReds();
  }

  /**
   * Test if the ratio field exits.
   * @return <b>true </b> if the field exists.
   */
  public boolean isRatio() {
    return this.bioAssay.isRatios();
  }

  /**
   * Test if the flags field exits.
   * @return <b>true </b> if the field exists.
   */
  public boolean isFlag() {
    return this.bioAssay.isFlags();
  }

  /**
   * Get the brights value.
   * @return the bright value
   */
  public double getBright() {

    testIndex();
    if (this.brights == null)
      throw new BioAssayRuntimeException(BioAssayRuntimeException.NULL_POINTER,
          "this field doesn't exist in this bioAssay");
    return this.brights[this.index];
  }

  /**
   * Test if the bright field exits.
   * @return <b>true </b> if the field exists.
   */
  public boolean isBright() {
    return this.bioAssay.isBrights();
  }

  /**
   * Test if the identifier field exits.
   * @return <b>true </b> if the field exists.
   */
  public boolean isId() {
    return this.bioAssay.isIds();
  }

  /**
   * Test if the description field is set.
   * @return true if the description field is set
   */
  public boolean isDescription() {
    return this.bioAssay.isDescriptions();
  }

  /**
   * Test if the a field is set.
   * @return true if the a field is set
   */
  public boolean isA() {
    return this.bioAssay.isAs();
  }

  /**
   * Test if the m field is set.
   * @return true if the m field is set
   */
  public boolean isM() {
    return this.bioAssay.isMs();
  }

  /**
   * Test if the the standard deviation for a values field is set.
   * @return true if the field set
   */
  public boolean isStdDevA() {
    return this.bioAssay.isStdDevAs();
  }

  /**
   * Test if the the standard deviation for m values field is set.
   * @return true if the field set
   */
  public boolean isStdDevM() {
    return this.bioAssay.isStdDevMs();
  }

  /**
   * Test if the location field is set.
   * @return true if the m field is set
   */
  public boolean isLocation() {
    return this.bioAssay.isLocations();
  }

  /**
   * Get the ratios data.
   * @return An int vector containing the data of the field
   */
  public double getRatio() {

    testIndex();
    if (this.ratios == null)
      throw new BioAssayRuntimeException(BioAssayRuntimeException.NULL_POINTER,
          "this field doesn't exist in this bioAssay");
    return this.ratios[this.index];
  }

  /**
   * Get the location of the spot.
   * @return The location of the spot
   */
  public int getLocation() {

    testIndex();
    if (this.locations == null)
      throw new BioAssayRuntimeException(BioAssayRuntimeException.NULL_POINTER,
          "this field doesn't exist in this bioAssay");
    return this.locations[this.index];
  }

  /**
   * Get the meta row of the spot.
   * @return The meta row of the spot
   */
  public int getMetaRow() {
    return BioAssayUtils.getMetaRow(getLocation());
  }

  /**
   * Get the meta column of the spot.
   * @return The meta column of the spot
   */
  public int getMetaColumn() {
    return BioAssayUtils.getMetaColumn(getLocation());
  }

  /**
   * Get the row of the spot.
   * @return The row of the spot
   */
  public int getRow() {
    return BioAssayUtils.getRow(getLocation());
  }

  /**
   * Get the column of the spot.
   * @return The column of the spot
   */
  public int getColumn() {
    return BioAssayUtils.getColumn(getLocation());
  }

  /**
   * Test if the spot is labeled as empty. This method use default "empty"
   * identifers.
   * @return true if the spot is labeled as empty
   */
  public boolean isEmpty() {
    return this.bioAssay.getSpotEmptyTester().isEmpty(this);
  }

  /**
   * Test if the flag of the spot is "bad".
   * @return true if the flag of the spot is "bad"
   */
  public boolean isBadFlag() {
    return getFlag() == BioAssay.FLAG_BAD;
  }

  /**
   * Test if the flag of the spot is "abscent".
   * @return true if the flag of the spot is "abscent"
   */
  public boolean isFlagAbscent() {
    return getFlag() == BioAssay.FLAG_ABSCENT;
  }

  /**
   * Test if the flag of the spot is "not found".
   * @return true if the flag of the spot is "not found"
   */
  public boolean isFlagNotFound() {
    return getFlag() == BioAssay.FLAG_NOT_FOUND;
  }

  /**
   * Test if the flag of the spot is "unflagged".
   * @return true if the flag of the spot is "unflagged"
   */
  public boolean isFlagUnflagged() {
    return getFlag() == BioAssay.FLAG_UNFLAGGED;
  }

  /**
   * Test if the flag of the spot is "good".
   * @return true if the flag of the spot is "good"
   */
  public boolean isFlagGood() {
    return getFlag() == BioAssay.FLAG_GOOD;
  }

  /**
   * Test if the flag of the spot is "normalized".
   * @return true if the flag of the spot is "normalized"
   */
  public boolean isFlagNormalized() {
    return getFlag() == BioAssay.FLAG_NORMALIZED;
  }

  /**
   * Test if the field exits.
   * @param field Field to test
   * @return true if the field exists
   */
  public boolean isField(final String field) {
    return this.bioAssay.isField(field);
  }

  /**
   * Get the integer value of a field.
   * @param field The field
   * @return the integer value of the spot
   * @throws BioAssayRuntimeException if the field doesn't exists
   */
  public int getDataFieldInt(final String field)
      throws BioAssayRuntimeException {

    testIndex();
    final int[] data = this.bioAssay.getDataFieldInt(field);

    if (data == null)
      throw new BioAssayRuntimeException("The field doesn't exists");

    return data[this.index];
  }

  /**
   * Get the double value of a field.
   * @param field The field
   * @return the double value of the spot
   * @throws BioAssayRuntimeException if the field doesn't exists
   */
  public double getDataFieldDouble(final String field)
      throws BioAssayRuntimeException {

    testIndex();
    final double[] data = this.bioAssay.getDataFieldDouble(field);

    if (data == null)
      throw new BioAssayRuntimeException("The field doesn't exists");

    return data[this.index];
  }

  /**
   * Get the string value of a field.
   * @param field The field
   * @return the string value of the spot
   * @throws BioAssayRuntimeException if the field doesn't exists
   */
  public String getDataFieldString(final String field)
      throws BioAssayRuntimeException {

    testIndex();
    final String[] data = this.bioAssay.getDataFieldString(field);

    if (data == null)
      throw new BioAssayRuntimeException("The field doesn't exists");

    return data[this.index];
  }

  //
  // Setters
  //

  /**
   * Set the bioAssay Object
   * @param bioAssay The bioAssay to set
   */
  void setBioAssay(final BioAssay bioAssay) {

    if (bioAssay == null)
      throw new BioAssayRuntimeException(BioAssayRuntimeException.NULL_POINTER,
          "BioAssay is null");

    this.bioAssay = bioAssay;
    this.bioAssaySize = bioAssay.size();

    if (bioAssay.isAs())
      this.as = bioAssay.getAs();
    if (bioAssay.isBrights())
      this.brights = bioAssay.getBrights();
    if (bioAssay.isLocations())
      this.locations = bioAssay.getLocations();
    if (bioAssay.isDescriptions())
      this.descriptions = bioAssay.getDescriptions();
    if (bioAssay.isFlags())
      this.flags = bioAssay.getFlags();
    if (bioAssay.isGreens())
      this.greens = bioAssay.getGreens();
    if (bioAssay.isIds())
      this.ids = bioAssay.getIds();
    if (bioAssay.isMs())
      this.ms = bioAssay.getMs();
    if (bioAssay.isRatios())
      this.ratios = bioAssay.getRatios();
    if (bioAssay.isReds())
      this.reds = bioAssay.getReds();
    if (bioAssay.isStdDevAs())
      this.stdDevA = bioAssay.getStdDevAs();
    if (bioAssay.isStdDevMs())
      this.stdDevM = bioAssay.getStdDevMs();

  }

  /**
   * Set the index of the spot.
   * @param index The index to set
   */
  public void setIndex(final int index) {
    this.index = index;
    this.indexChange = true;
  }

  /**
   * Set the flag value.
   * @param flag The flag value to set
   * @throws BioAssayRuntimeException if flags is null or if flags size if wrong
   */
  public void setFlag(final int flag) throws BioAssayRuntimeException {

    testIndex();
    if (this.flags == null)
      throw new BioAssayRuntimeException(BioAssayRuntimeException.NULL_POINTER,
          "this field doesn't exist in this bioAssay");
    this.flags[this.index] = flag;
  }

  /**
   * Set the green value.
   * @param green The green data to set
   * @throws BioAssayRuntimeException if greens is null or if greens size if
   *           wrong
   */
  public void setGreen(final int green) throws BioAssayRuntimeException {

    testIndex();
    if (this.greens == null)
      throw new BioAssayRuntimeException(BioAssayRuntimeException.NULL_POINTER,
          "this field doesn't exist in this bioAssay");
    this.greens[this.index] = green;
  }

  /**
   * Set the red value.
   * @param red The red data to set
   * @throws BioAssayRuntimeException if reds is null or if reds size if wrong
   */
  public void setRed(final int red) throws BioAssayRuntimeException {

    testIndex();
    if (this.reds == null)
      throw new BioAssayRuntimeException(BioAssayRuntimeException.NULL_POINTER,
          "this field doesn't exist in this bioAssay");
    this.reds[this.index] = red;
  }

  /**
   * Set the ratio value.
   * @param ratio The ratios value to set
   * @throws BioAssayRuntimeException if ratios is null or if ratios size if
   *           wrong
   */
  public void setRatio(final double ratio) throws BioAssayRuntimeException {

    testIndex();
    if (this.ratios == null)
      throw new BioAssayRuntimeException(BioAssayRuntimeException.NULL_POINTER,
          "this field doesn't exist in this bioAssay");
    this.ratios[this.index] = ratio;
  }

  /**
   * Set the identifier value.
   * @param id The identifiers data to set
   * @throws BioAssayRuntimeException if ids is null or if ids size if wrong
   */
  public void setId(final String id) throws BioAssayRuntimeException {

    testIndex();
    if (this.ids == null)
      throw new BioAssayRuntimeException(BioAssayRuntimeException.NULL_POINTER,
          "this field doesn't exist in this bioAssay");
    this.ids[this.index] = id;
  }

  /**
   * Set the bright value.
   * @param bright The brights data to set
   * @throws BioAssayRuntimeException if brights is null or if brights size if
   *           wrong
   */
  public void setBright(final double bright) throws BioAssayRuntimeException {

    testIndex();
    if (this.brights == null)
      throw new BioAssayRuntimeException(BioAssayRuntimeException.NULL_POINTER,
          "this field doesn't exist in this bioAssay");
    this.brights[this.index] = bright;
  }

  /**
   * Set the description field.
   * @param description Array of descriptions to set
   * @throws BioAssayRuntimeException if descriptions is null or if descriptions
   *           size if wrong
   */
  public void setDescription(final String description)
      throws BioAssayRuntimeException {

    testIndex();
    if (this.descriptions == null)
      throw new BioAssayRuntimeException(BioAssayRuntimeException.NULL_POINTER,
          "this field doesn't exist in this bioAssay");
    this.descriptions[this.index] = description;
  }

  /**
   * Set the a value.
   * @param a The a coordinate to set
   * @throws BioAssayRuntimeException if as is null or if as size if wrong
   */
  public void setA(final double a) throws BioAssayRuntimeException {

    testIndex();
    if (this.as == null)
      throw new BioAssayRuntimeException(BioAssayRuntimeException.NULL_POINTER,
          "this field doesn't exist in this bioAssay");
    this.as[this.index] = a;
  }

  /**
   * Set the m field.
   * @param m The m coordinate to set
   * @throws BioAssayRuntimeException if ms is null or if ms size if wrong
   */
  public void setM(final double m) throws BioAssayRuntimeException {

    testIndex();
    if (this.ms == null)
      throw new BioAssayRuntimeException(BioAssayRuntimeException.NULL_POINTER,
          "this field doesn't exist in this bioAssay");
    this.ms[this.index] = m;
  }

  /**
   * Set the standard deviation for a value.
   * @param stdDevA The standard deviation for a value to set
   * @throws BioAssayRuntimeException if as is null or if as size if wrong
   */
  public void setStdDevA(final double stdDevA) throws BioAssayRuntimeException {

    testIndex();
    if (this.stdDevA == null)
      throw new BioAssayRuntimeException(BioAssayRuntimeException.NULL_POINTER,
          "this field doesn't exist in this bioAssay");
    this.stdDevA[this.index] = stdDevA;
  }

  /**
   * Set the standard deviation for m value.
   * @param stdDevM The standard deviation for m value to set
   * @throws BioAssayRuntimeException if ms is null or if ms size if wrong
   */
  public void setStdDevM(final double stdDevM) throws BioAssayRuntimeException {

    testIndex();
    if (this.stdDevM == null)
      throw new BioAssayRuntimeException(BioAssayRuntimeException.NULL_POINTER,
          "this field doesn't exist in this bioAssay");
    this.stdDevM[this.index] = stdDevM;
  }

  /**
   * Set the location of the spot.
   * @param location The location to set
   * @throws BioAssayRuntimeException if location field is null
   */
  public void setLocation(final int location) throws BioAssayRuntimeException {

    testIndex();
    if (this.locations == null)
      throw new BioAssayRuntimeException(BioAssayRuntimeException.NULL_POINTER,
          "this field doesn't exist in this bioAssay");
    this.locations[this.index] = location;
  }

  /**
   * Set meta row of the spot.
   * @param metaRow The meta row to set
   * @throws BioAssayRuntimeException if location field is null
   */
  public void setMetaRow(final int metaRow) throws BioAssayRuntimeException {

    testIndex();
    setLocation(BioAssayUtils.setMetaRow(getLocation(), metaRow));
  }

  /**
   * Set meta column of the spot.
   * @param metaColumn The meta column to set
   * @throws BioAssayRuntimeException if location field is null
   */
  public void setMetaColumn(final int metaColumn)
      throws BioAssayRuntimeException {

    testIndex();
    setLocation(BioAssayUtils.setMetaRow(getLocation(), metaColumn));
  }

  /**
   * Set the row of the spot.
   * @param row The row to set
   * @throws BioAssayRuntimeException if location field is null
   */
  public void setRow(final int row) throws BioAssayRuntimeException {

    testIndex();

    if (this.reds == null)
      throw new BioAssayRuntimeException(BioAssayRuntimeException.NULL_POINTER,
          "this field doesn't exist in this bioAssay");
    setLocation(BioAssayUtils.setMetaRow(getLocation(), row));
  }

  /**
   * Set the column of the spot .
   * @param column The column to set
   * @throws BioAssayRuntimeException if location field is null
   */
  public void setColumn(final int column) throws BioAssayRuntimeException {

    testIndex();
    setLocation(BioAssayUtils.setMetaRow(getLocation(), column));
  }

  /**
   * Set an integer value in a field
   * @param field Field to set
   * @param value Value to set
   * @throws BioAssayRuntimeException if the field doesn exists
   */
  public void setDataFieldInt(final String field, final int value)
      throws BioAssayRuntimeException {

    testIndex();
    final int[] data = this.bioAssay.getDataFieldInt(field);
    data[this.index] = value;
  }

  /**
   * Set an doubke value in a field
   * @param field Field to set
   * @param value Value to set
   * @throws BioAssayRuntimeException if the field doesn exists
   */
  public void setDataFieldDouble(final String field, final double value)
      throws BioAssayRuntimeException {

    testIndex();
    final double[] data = this.bioAssay.getDataFieldDouble(field);
    data[this.index] = value;
  }

  /**
   * Set an string value in a field
   * @param field Field to set
   * @param value Value to set
   * @throws BioAssayRuntimeException if the field doesn exists
   */
  public void setDataFieldString(final String field, final String value)
      throws BioAssayRuntimeException {

    testIndex();
    final String[] data = this.bioAssay.getDataFieldString(field);
    data[this.index] = value;
  }

  private void testIndex() {

    if (!this.indexChange)
      return;

    if (this.index < 0 || this.index >= this.bioAssaySize)
      throw new BioAssayRuntimeException(
          BioAssayRuntimeException.INVALID_INDEX, "" + this.index);

    this.indexChange = false;
  }

  //
  // Constructor
  //

  /**
   * Package constructor.
   * @param bioAssay of the spot
   */
  SpotImpl(final BioAssay bioAssay) {
    setBioAssay(bioAssay);
  }

  SpotImpl(final BioAssay bioAssay, final int index) {
    this(bioAssay);
    setIndex(index);
  }

}