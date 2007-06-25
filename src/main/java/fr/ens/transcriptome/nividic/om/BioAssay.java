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

import fr.ens.transcriptome.nividic.om.filters.BioAssayFilter;
import fr.ens.transcriptome.nividic.om.filters.SpotComparator;

/**
 * This interface discribe a bioassay object. This code is based on previous
 * work in c++ developped at the Service de Génomique Fonctionelle of The
 * Commissariat à l'Énergie Atomique (CEA) by Laurent Jourdren, Miramme Allouche
 * and Vincent Frouin.
 * @author Laurent Jourdren
 * @author Vincent Frouin
 * @author Miriame Allouche
 */
public interface BioAssay extends BioAssayBase, BiologicalObject {

  /** Column name for red data. */
  String FIELD_NAME_RED = "red";
  /** Column name for green data. */
  String FIELD_NAME_GREEN = "green";
  /** Column name for flags data. */
  String FIELD_NAME_FLAG = "flags";
  /** Column name for name data. */
  String FIELD_NAME_ID = "id";
  /** Column name for ratio data. */
  String FIELD_NAME_RATIO = "ratio";
  /** Column name for bright data. */
  String FIELD_NAME_BRIGHT = "bright";
  /** Column name for description data. */
  String FIELD_NAME_DESCRIPTION = "description";
  /** Column name for a coordinate of a MA plot. */
  String FIELD_NAME_A = "a";
  /** Column name for m coordinate of a MA plot. */
  String FIELD_NAME_M = "m";
  /** Column name for the standard deviation of a values. */
  String FIELD_NAME_STD_DEV_A = "stddeva";
  /** Column name for the standard deviation of m values. */
  String FIELD_NAME_STD_DEV_M = "stddevm";

  /** Flag bad. */
  int FLAG_BAD = -100;
  /** Flag abscent. */
  int FLAG_ABSCENT = -75;
  /** Flag not found. */
  int FLAG_NOT_FOUND = -50;
  /** Flag unflagged. */
  int FLAG_UNFLAGGED = 0;
  /** Flag normalized. */
  int FLAG_NORMALIZED = 1;
  /** Flag good. */
  int FLAG_GOOD = 100;

  /** Idenfiers of the empty spots. */
  String[] EMPTY_SPOTS_IDENTIFIERS = {"", "empty"};

  //
  // Getters
  //

  /**
   * Get the annotations.
   * @return An annotations object
   */
  Annotation getAnnotation();

  /**
   * Get the reds data.
   * @return An int vector containing the data of the field
   */
  int[] getReds();

  /**
   * Get the green data.
   * @return An int vector containing the data of the field
   */
  int[] getGreens();

  /**
   * Get the identifiers data.
   * @return An int vector containing the data of the field
   */
  String[] getIds();

  /**
   * Test if the flags field exits.
   * @return <b>true </b> if the field exists.
   */
  int[] getFlags();

  /**
   * Get the array of description of the spots.
   * @return An array of string of the description.
   */
  String[] getDescriptions();

  /**
   * Get the array of the value of a coordinates of a MA plot.
   * @return An double array of a coordinate of a MA plot.
   */
  double[] getAs();

  /**
   * Get the array of the value of m coordinates of a MA plot.
   * @return An double array of m coordinate of a MA plot.
   */
  double[] getMs();

  /**
   * Get the array of the value of the standard deviations of A values.
   * @return An double array of the standard deviations of A values.
   */
  double[] getStdDevAs();

  /**
   * Get the array of the value of the standard deviations of M values.
   * @return An double array of the standard deviations of M values.
   */
  double[] getStdDevMs();

  /**
   * Test if the green field exits.
   * @return <b>true </b> if the field exists.
   */
  boolean isGreens();

  /**
   * Test if the red field exits.
   * @return <b>true </b> if the field exists.
   */
  boolean isReds();

  /**
   * Test if the ratio field exits.
   * @return <b>true </b> if the field exists.
   */
  boolean isRatios();

  /**
   * Test if the flags field exits.
   * @return <b>true </b> if the field exists.
   */
  boolean isFlags();

  /**
   * Get the brights data.
   * @return An int vector containing the data of the field
   */
  double[] getBrights();

  /**
   * Test if the bright field exits.
   * @return <b>true </b> if the field exists.
   */
  boolean isBrights();

  /**
   * Test if the identifier field exits.
   * @return <b>true </b> if the field exists.
   */
  boolean isIds();

  /**
   * Test if the description field is set.
   * @return true if the description field is set
   */
  boolean isDescriptions();

  /**
   * Test if the a field is set.
   * @return true if the a field is set
   */
  boolean isAs();

  /**
   * Test if the m field is set.
   * @return true if the m field is set
   */
  boolean isMs();

  /**
   * Test if the standard deviation for a values field is set.
   * @return true if the field is set
   */
  boolean isStdDevAs();

  /**
   * Test if the standard deviation for a values field is set.
   * @return true if the m field is set
   */
  boolean isStdDevMs();

  /**
   * Get the ratios data.
   * @return An int vector containing the data of the field
   */
  double[] getRatios();

  /**
   * Return some int vectors containing coordinates of the array spot.
   * @param metaRow Vector for meta lines
   * @param metaColumn Vector for meta columns
   * @param row Vector for lines
   * @param column Vector for columns
   * @throws BioAssayRuntimeException if there is no location or if one or more
   *           parameter is null.
   */
  void getLocations(int[] metaRow, int[] metaColumn, int[] row, int[] column)
      throws BioAssayRuntimeException;

  //
  // Setters
  //

  /**
   * Set the flags data.
   * @param flags The flags data to set
   * @throws BioAssayRuntimeException if flags is null or if flags size if wrong
   */
  void setFlags(int[] flags) throws BioAssayRuntimeException;

  /**
   * Set the green data.
   * @param greens The reds data to set *
   * @throws BioAssayRuntimeException if greens is null or if greens size if
   *           wrong
   */
  void setGreens(int[] greens) throws BioAssayRuntimeException;

  /**
   * Set the red data.
   * @param reds The reds data to set
   * @throws BioAssayRuntimeException if reds is null or if reds size if wrong
   */
  void setReds(int[] reds) throws BioAssayRuntimeException;

  /**
   * Set the ratios data.
   * @param ratios The ratios data to set
   * @throws BioAssayRuntimeException if ratios is null or if ratios size if
   *           wrong
   */
  void setRatios(double[] ratios) throws BioAssayRuntimeException;

  /**
   * Set the identifiers data.
   * @param ids The identifiers data to set
   * @throws BioAssayRuntimeException if ids is null or if ids size if wrong
   */
  void setIds(String[] ids) throws BioAssayRuntimeException;

  /**
   * Set the brights data.
   * @param brights The brights data to set
   * @throws BioAssayRuntimeException if brights is null or if brights size if
   *           wrong
   */
  void setBrights(double[] brights) throws BioAssayRuntimeException;

  /**
   * Set the description field.
   * @param descriptions Array of descriptions to set
   * @throws BioAssayRuntimeException if descriptions is null or if descriptions
   *           size if wrong
   */
  void setDescriptions(String[] descriptions) throws BioAssayRuntimeException;

  /**
   * Set the a field.
   * @param as Array of a coordinate to set
   * @throws BioAssayRuntimeException if as is null or if as size if wrong
   */
  void setAs(double[] as) throws BioAssayRuntimeException;

  /**
   * Set the m field.
   * @param ms Array of m coordinate to set
   * @throws BioAssayRuntimeException if ms is null or if ms size if wrong
   */
  void setMs(double[] ms) throws BioAssayRuntimeException;

  /**
   * Set the standard deviation a field.
   * @param stdDevAs Array of standard deviation for field m to set
   * @throws BioAssayRuntimeException if as is null or if as size if wrong
   */
  void setStdDevAs(double[] stdDevAs) throws BioAssayRuntimeException;

  /**
   * Set the standard deviation m field.
   * @param stdDevMs Array of standard deviation for field m to set
   * @throws BioAssayRuntimeException if ms is null or if ms size if wrong
   */
  void setStdDevMs(double[] stdDevMs) throws BioAssayRuntimeException;

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
  boolean setLocations(int[] metaRow, int[] metaColumn, int[] row, int[] column)
      throws BioAssayRuntimeException;

  //
  // Others methods
  //

  /**
   * Remove flags data.
   */
  void removeFlags();

  /**
   * Remove greens data.
   */
  void removeGreens();

  /**
   * remove reds data.
   */
  void removeReds();

  /**
   * remove ratios.
   */
  void removeRatios();

  /**
   * Remove identifiers.
   */
  void removeIds();

  /**
   * Remove brights.
   */
  void removeBrights();

  /**
   * Remove descriptions.
   */
  void removeDescriptions();

  /**
   * Remove a coordinate of MA plot.
   */
  void removeAs();

  /**
   * Remove m coordinate of MA plot.
   */
  void removeMs();

  /**
   * Get a spot iterator.
   * @return A spot Iterator Object.
   */
  SpotIterator iterator();

  /**
   * Get the spot at the index.
   * @param index The index of the spot
   * @return A spot object
   */
  Spot getSpot(int index);

  /**
   * Get the spot at the location.
   * @param location The location of the spot
   * @return A spot object
   */
  Spot getSpotLocation(int location);

  /**
   * Test if the spot is labeled as empty. If no SpotEmptyTester is set, use the
   * DefaultSpotEmptyTester.
   * @param index of the spot to test
   * @return true if the spot is labeled as empty
   */
  boolean isEmpty(int index);

  /**
   * Set the SpotEmptyTester.
   * @param spotEmptyTester SpotEmptyTester to set
   */
  void setSpotEmptyTester(SpotEmptyTester spotEmptyTester);

  /**
   * Get the SpotEmptyTester.
   * @return the SpotEmptyTester for the bioAssay
   */
  SpotEmptyTester getSpotEmptyTester();

  /**
   * Filter the bioAssay.
   * @param filter Filter to apply
   * @return a new bioAssay filtered
   */
  BioAssay filter(BioAssayFilter filter);

  /**
   * Count the entries of the bioAssay that pass the filter.
   * @param filter Filter to apply
   * @return a new bioAssay filtered
   */
  int count(BioAssayFilter filter);

  /**
   * Sort the bioAssay.
   * @param comparator Object used to do the sort
   * @return a new bioAssay sorted
   */
  BioAssay sort(SpotComparator comparator);

  /**
   * Swap Identifiers and Description columns
   */
  void swapIdentifiersAndDescriptions();

}