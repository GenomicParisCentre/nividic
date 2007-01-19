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
import java.util.Comparator;
import java.util.Hashtable;

import fr.ens.transcriptome.nividic.NividicRuntimeException;
import fr.ens.transcriptome.nividic.om.BioAssay;
import fr.ens.transcriptome.nividic.om.BioAssayFactory;
import fr.ens.transcriptome.nividic.om.BioAssayRuntimeException;
import fr.ens.transcriptome.nividic.om.BioAssayUtils;
import fr.ens.transcriptome.nividic.om.DefaultSpotEmptyTester;
import fr.ens.transcriptome.nividic.om.Spot;
import fr.ens.transcriptome.nividic.om.SpotEmptyTester;
import fr.ens.transcriptome.nividic.om.SpotIterator;
import fr.ens.transcriptome.nividic.om.filters.BioAssayFilter;
import fr.ens.transcriptome.nividic.om.filters.SpotComparator;

/**
 * @author Laurent Jourdren
 * @author Vincent Frouin
 * @author Miriame Allouche
 */
public class BioAssayImpl extends BioAssayBaseImpl implements BioAssay,
    Serializable {

  /** serial version for serialization. */
  static final long serialVersionUID = 8059012751969392959L;

  /** Vector index for location (loc,index). */
  private Hashtable indexLoc = new Hashtable();

  private SpotEmptyTester spotEmptyTester;

  /**
   * Return some int vectors containing coordinates of the array spot.
   * @param metaRow Vector for meta lines, must be not null.
   * @param metaColumn Vector for meta columns, must be not null.
   * @param row Vector for lines, must be not null.
   * @param column Vector for columns, must be not null.
   * @throws BioAssayRuntimeException if there is no location, if one or more
   *           parameter is null, or if size of one or more parameter is
   *           invalid.
   */
  public final void getLocations(final int[] metaRow, final int[] metaColumn,
      final int[] row, final int[] column) throws BioAssayRuntimeException {

    if (!isLocations())
      throw new BioAssayRuntimeException("No locations found");

    if (metaRow == null || metaColumn == null || row == null || column == null
        || !isLocations()) {
      throw new BioAssayRuntimeException(BioAssayRuntimeException.NULL_POINTER,
          "One or more parameter is null");
    }
    // Must throw an exception

    int[] array = getLocations();
    // recupere le vecteur qui a les localisations codées

    // int size = array.length;
    if ((array.length != metaRow.length) || (array.length != metaColumn.length)
        || (array.length != row.length) || (array.length != column.length))
      throw new BioAssayRuntimeException(
          BioAssayRuntimeException.INVALID_ARGUMENT,
          "One or more parameter size is bad");

    for (int i = 0; i < array.length; i++) {

      int loc = array[i];

      metaRow[i] = BioAssayUtils.getMetaRow(loc);
      metaColumn[i] = BioAssayUtils.getMetaColumn(loc);
      row[i] = BioAssayUtils.getRow(loc);
      column[i] = BioAssayUtils.getColumn(loc);
    }

  }

  /**
   * Set the locations of the array with one vector for each coordinate.
   * @param metaRow Vector for meta lines
   * @param metaColumn Vector for meta columns
   * @param row Vector for lines
   * @param column Vector for columns
   * @return <b>true </b> if ok
   * @throws BioAssayRuntimeException if there is no location or if one or more
   *           parameter is null.
   */
  public boolean setLocations(final int[] metaRow, final int[] metaColumn,
      final int[] row, final int[] column) throws BioAssayRuntimeException {

    if (metaRow == null || metaColumn == null || row == null || column == null)
      throw new BioAssayRuntimeException(BioAssayRuntimeException.NULL_POINTER,
          "Locations");

    if (!(metaRow.length == metaColumn.length && metaRow.length == row.length && metaRow.length == column.length))
      throw new BioAssayRuntimeException("Different locations sizes");

    // impossible de fixer les positions, les vecteurs n'ont pas la même taille

    if (!(testLocationValue(metaRow, 0, BioAssayUtils.getMaxMetaRowValue())
        && testLocationValue(metaColumn, 0, BioAssayUtils
            .getMaxMetaColumnValue())
        && testLocationValue(row, 0, BioAssayUtils.getMaxRowValue()) && testLocationValue(
        column, 0, BioAssayUtils.getMaxColumnValue())))
      throw new BioAssayRuntimeException("Locations out of range");

    int max = metaRow.length;
    int[] resultat = new int[max];
    for (int i = 0; i < max; i++) {

      int loc = 0;
      loc = BioAssayUtils.setMetaRow(loc, metaRow[i]);
      loc = BioAssayUtils.setMetaColumn(loc, metaColumn[i]);
      loc = BioAssayUtils.setRow(loc, row[i]);
      loc = BioAssayUtils.setColumn(loc, column[i]);
      // Construction du vecteur des localisations codees
      resultat[i] = loc;
      // Construction de la map d'index des localisations
      this.indexLoc.put(new Integer(loc), new Integer(i));
    } // Ajout du vecteur des localisations a la classe
    setLocations(resultat);
    return true;
  }

  /**
   * Get the reds data.
   * @return An int vector containing the data of the field
   */
  public int[] getReds() {
    return this.getDataFieldInt(FIELD_NAME_RED);
  }

  /**
   * Test if the red field exits.
   * @return <b>true </b> if the field exists.
   */
  public boolean isReds() {
    return this.isField(FIELD_NAME_RED);
  }

  /**
   * Set the red data.
   * @param reds The reds data to set
   */
  public void setReds(final int[] reds) {
    this.setDataFieldInt(FIELD_NAME_RED, reds);
  }

  /**
   * Get the green data.
   * @return An int vector containing the data of the field
   */
  public int[] getGreens() {
    return this.getDataFieldInt(FIELD_NAME_GREEN);
  }

  /**
   * Test if the green field exits.
   * @return <b>true </b> if the field exists.
   */
  public boolean isGreens() {
    return this.isField(FIELD_NAME_GREEN);
  }

  /**
   * Set the green data.
   * @param greens The reds data to set *
   */
  public void setGreens(final int[] greens) {
    this.setDataFieldInt(FIELD_NAME_GREEN, greens);
  }

  /**
   * Get the identifiers data.
   * @return An int vector containing the data of the field
   */
  public String[] getIds() {
    return this.getDataFieldString(FIELD_NAME_ID);
  }

  /**
   * Test if the identifier field exits.
   * @return <b>true </b> if the field exists.
   */
  public boolean isIds() {
    return this.isField(FIELD_NAME_ID);
  }

  /**
   * Set the identifiers data.
   * @param ids The identifiers data to set
   */
  public void setIds(final String[] ids) {
    setDataFieldString(FIELD_NAME_ID, ids);
  }

  /**
   * Get the ratios data.
   * @return An int vector containing the data of the field
   */
  public double[] getRatios() {
    return this.getDataFieldDouble(FIELD_NAME_RATIO);
  }

  /**
   * Test if the ratio field exits.
   * @return <b>true </b> if the field exists.
   */
  public boolean isRatios() {
    return this.isField(FIELD_NAME_RATIO);
  }

  /**
   * Set the ratios data.
   * @param ratios The ratios data to set
   */
  public void setRatios(final double[] ratios) {
    this.setDataFieldDouble(FIELD_NAME_RATIO, ratios);
  }

  /**
   * Get the brights data.
   * @return An int vector containing the data of the field
   */
  public double[] getBrights() {
    return this.getDataFieldDouble(FIELD_NAME_BRIGHT);
  }

  /**
   * Test if the bright field exits.
   * @return <b>true </b> if the field exists.
   */
  public boolean isBrights() {
    return this.isField(FIELD_NAME_BRIGHT);
  }

  /**
   * Set the brights data.
   * @param brights The brights data to set
   */
  public void setBrights(final double[] brights) {
    this.setDataFieldDouble(FIELD_NAME_BRIGHT, brights);
  }

  /**
   * Get the flags data.
   * @return An int vector containing the data of the field
   */
  public int[] getFlags() {
    return this.getDataFieldInt(FIELD_NAME_FLAG);
  }

  /**
   * Test if the flags field exits.
   * @return <b>true </b> if the field exists.
   */
  public boolean isFlags() {
    return this.isField(FIELD_NAME_FLAG);
  }

  /**
   * Set the flags data.
   * @param flags The flags data to set
   */
  public void setFlags(final int[] flags) {
    this.setDataFieldInt(FIELD_NAME_FLAG, flags);
  }

  /**
   * Get the array of description of the spots.
   * @return An array of string of the description.
   */
  public String[] getDescriptions() {
    return this.getDataFieldString(FIELD_NAME_DESCRIPTION);
  }

  /**
   * Test if the description field is set.
   * @return true if the description field is set
   */
  public boolean isDescriptions() {
    return this.isField(FIELD_NAME_DESCRIPTION);
  }

  /**
   * Set the description field.
   * @param descriptions Array of descriptions to set
   */
  public void setDescriptions(final String[] descriptions) {
    this.setDataFieldString(FIELD_NAME_DESCRIPTION, descriptions);
  }

  /**
   * Get the array of the value of a coordinates of a MA plot.
   * @return An double array of a coordinate of a MA plot.
   */
  public double[] getAs() {
    return this.getDataFieldDouble(FIELD_NAME_A);
  }

  /**
   * Test if the a field is set.
   * @return true if the a field is set
   */
  public boolean isAs() {
    return this.isField(FIELD_NAME_A);
  }

  /**
   * Set the a field.
   * @param as Array of a coordinate to set
   */
  public void setAs(final double[] as) {
    this.setDataFieldDouble(FIELD_NAME_A, as);
  }

  /**
   * Get the array of the value of m coordinates of a MA plot.
   * @return An double array of m coordinate of a MA plot.
   */
  public double[] getMs() {
    return this.getDataFieldDouble(FIELD_NAME_M);
  }

  /**
   * Test if the m field is set.
   * @return true if the m field is set
   */
  public boolean isMs() {
    return this.isField(FIELD_NAME_M);
  }

  /**
   * Set the m field.
   * @param ms Array of m coordinate to set
   */
  public void setMs(final double[] ms) {
    this.setDataFieldDouble(FIELD_NAME_M, ms);
  }

  /**
   * Get the array of the value of the standard deviations of A values.
   * @return An double array of the standard deviations of A values.
   */
  public double[] getStdDevAs() {
    return this.getDataFieldDouble(FIELD_NAME_STD_DEV_A);
  }

  /**
   * Get the array of the value of the standard deviations of M values.
   * @return An double array of the standard deviations of M values.
   */
  public double[] getStdDevMs() {
    return this.getDataFieldDouble(FIELD_NAME_STD_DEV_M);
  }

  /**
   * Test if the standard deviation for a values field is set.
   * @return true if the field is set
   */
  public boolean isStdDevAs() {
    return this.isField(FIELD_NAME_STD_DEV_A);
  }

  /**
   * Set the standard deviation a field.
   * @param stdDevAs Array of standard deviation for field m to set
   */
  public void setStdDevAs(final double[] stdDevAs) {
    this.setDataFieldDouble(FIELD_NAME_STD_DEV_A, stdDevAs);
  }

  /**
   * Test if the standard deviation for a values field is set.
   * @return true if the m field is set
   */
  public boolean isStdDevMs() {
    return this.isField(FIELD_NAME_STD_DEV_M);
  }

  /**
   * Set the standard deviation m field.
   * @param stdDevMs Array of standard deviation for field m to set
   */
  public void setStdDevMs(final double[] stdDevMs) {
    this.setDataFieldDouble(FIELD_NAME_STD_DEV_M, stdDevMs);
  }

  /**
   * Test if all the valeur of an int vector are in a range.
   * @param v Int vector to test
   * @param min Min value of the range (inclusive)
   * @param max Max value of the range (exclusive)
   * @return <b>true </b> if all the value are in the range
   */
  private static boolean testLocationValue(final int[] v, final int min,
      final int max) {

    if (v == null)
      return false;
    for (int i = 0; i < v.length; i++) {
      int value = v[i];
      if (value < min || value >= max)
        return false;
    }

    return true;
  }

  /**
   * Remove flags data.
   */
  public void removeFlags() {
    removeField(FIELD_NAME_FLAG);
  }

  /**
   * Remove greens data.
   */
  public void removeGreens() {
    removeField(FIELD_NAME_GREEN);
  }

  /**
   * Remove reds data.
   */
  public void removeReds() {
    removeField(FIELD_NAME_RED);
  }

  /**
   * Remove ratios.
   */
  public void removeRatios() {
    removeField(FIELD_NAME_RATIO);
  }

  /**
   * Remove identifiers.
   */
  public void removeIds() {
    removeField(FIELD_NAME_ID);
  }

  /**
   * Remove brights.
   */
  public void removeBrights() {
    removeField(FIELD_NAME_BRIGHT);
  }

  /**
   * Remove descriptions.
   */
  public void removeDescriptions() {
    removeField(FIELD_NAME_DESCRIPTION);
  }

  /**
   * Remove a coordinate of MA plot.
   */
  public void removeAs() {
    removeField(FIELD_NAME_A);
  }

  /**
   * Remove m coordinate of MA plot.
   */
  public void removeMs() {
    removeField(FIELD_NAME_M);
  }

  /**
   * Get a spot iterator.
   * @return A spot Iterator Object.
   */
  public SpotIterator iterator() {
    return new SpotIteratorImpl(this);
  }

  /**
   * Get the spot at the index.
   * @param index The index of the spot
   * @return A spot object
   */
  public Spot getSpot(final int index) {

    if (index < 0 && index >= size())
      return null;

    return new SpotImpl(this, index);
  }

  /**
   * Get the spot at the location.
   * @param location The location of the spot
   * @return A spot object
   */
  public Spot getSpotLocation(final int location) {

    return getSpot(getIndexFromALocation(location));
  }

  /**
   * Test if the spot is labeled as empty. If no SpotEmptyTester is set, use the
   * DefaultSpotEmptyTester.
   * @param index of the spot to test
   * @return true if the spot is labeled as empty
   */
  public boolean isEmpty(final int index) {
    return getSpotEmptyTester().isEmpty(getSpot(index));
  }

  /**
   * Set the SpotEmptyTester.
   * @param spotEmptyTester SpotEmptyTester to set
   */
  public void setSpotEmptyTester(final SpotEmptyTester spotEmptyTester) {
    this.spotEmptyTester = spotEmptyTester;
  }

  /**
   * Get the SpotEmptyTester.
   * @return the SpotEmptyTester for the bioAssay
   */
  public SpotEmptyTester getSpotEmptyTester() {
    if (this.spotEmptyTester == null)
      this.spotEmptyTester = new DefaultSpotEmptyTester();
    return this.spotEmptyTester;
  }

  /**
   * Filter the bioAssay
   * @param filter Filter to apply
   * @return a new bioAssay filtered
   */
  public BioAssay filter(final BioAssayFilter filter) {

    if (filter == null)
      return null;

    return filter.filter(this);
  }

  /**
   * Sort the bioAssay.
   * @param comparator Object used to do the sort
   * @return a new bioAssay sorted
   */
  public BioAssay sorter(final SpotComparator comparator) {

    if (comparator == null)
      throw new NividicRuntimeException("Comparator is null");

    final int n = size();

    Integer order[] = new Integer[n];

    for (int i = 0; i < order.length; i++)
      order[i] = new Integer(i);

    // Sort the order of the rows
    Arrays.sort(order, new Comparator() {

      public int compare(Object arg0, Object arg1) {

        final int i0 = ((Integer) arg0).intValue();
        final int i1 = ((Integer) arg1).intValue();

        return comparator.compare(getSpot(i0), getSpot(i1));
      }
    });

    BioAssay ba = BioAssayFactory.createBioAssay();
    ba.getAnnotation().addProperties(getAnnotation());
    final String[] fields = getFields();

    for (int i = 0; i < fields.length; i++) {

      final String fieldName = fields[i];

      switch (getFieldType(fieldName)) {

      case BioAssay.DATATYPE_DOUBLE:

        final double[] fieldDouble = getDataFieldDouble(fieldName);
        final double[] newFieldDouble = new double[n];
        for (int j = 0; j < n; j++)
          newFieldDouble[j] = fieldDouble[order[j].intValue()];

        ba.setDataFieldDouble(fieldName, newFieldDouble);
        break;

      case BioAssay.DATATYPE_INTEGER:

        final int[] fieldInt = getDataFieldInt(fieldName);
        final int[] newFieldInt = new int[n];
        for (int j = 0; j < n; j++)
          newFieldInt[j] = fieldInt[order[j].intValue()];

        ba.setDataFieldInt(fieldName, newFieldInt);
        break;

      case BioAssay.DATATYPE_STRING:

        final String[] fieldString = getDataFieldString(fieldName);
        final String[] newFieldString = new String[n];
        for (int j = 0; j < n; j++)
          newFieldString[j] = fieldString[order[j].intValue()];

        ba.setDataFieldString(fieldName, newFieldString);
        break;

      default:
        throw new NividicRuntimeException("Invalid BioAssay Type");
      }

    }

    ba.setReferenceField(getReferenceField());
    ba.setSpotEmptyTester(getSpotEmptyTester());

    return ba;
  }

  //
  // Constructor
  //

  /**
   * Public constructor.
   */
  public BioAssayImpl() {
  }

}