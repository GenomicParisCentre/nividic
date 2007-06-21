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

import java.util.HashMap;
import java.util.Map;

import fr.ens.transcriptome.nividic.NividicRuntimeException;
import fr.ens.transcriptome.nividic.om.HistoryEntry.HistoryActionResult;
import fr.ens.transcriptome.nividic.om.HistoryEntry.HistoryActionType;
import fr.ens.transcriptome.nividic.om.filters.BioAssayMerger;
import fr.ens.transcriptome.nividic.om.translators.Translator;
import fr.ens.transcriptome.nividic.util.MathUtils;
import fr.ens.transcriptome.nividic.util.NividicUtils;

/**
 * Utility class for handling BioAssayObject
 * @author Laurent Jourdren
 * @author Lory Montout
 */
public final class BioAssayUtils {

  /** 6 bits mask. */
  private static final int MASK_6BITS = 0x3f;
  /** 10 bits mask. */
  private static final int MASK_10BITS = 0x3ff;
  /** Meta row mask. */
  private static final int MASK_META_ROW = 0xffffffc0;
  /** Meta column mask. */
  private static final int MASK_META_COLUMN = 0xfffff03f;
  /** Row mask. */
  private static final int MASK_ROW = 0xffc00fff;
  /** Column mask. */
  private static final int MASK_COL = 0x3fffff;
  /** Shift meta row. */
  private static final int SHIFT_META_ROW = 0;
  /** Shift meta colummn. */
  private static final int SHIFT_META_COLUMN = 6;
  /** Shift row. */
  private static final int SHIFT_ROW = 12;
  /** Shift column. */
  private static final int SHIFT_COLUMN = 22;
  /** Max value for meta row. */
  private static final int MAX_META_ROW = 64;
  /** Max value for meta column. */
  private static final int MAX_META_COLUMN = 64;
  /** Max value for row. */
  private static final int MAX_ROW = 1024;
  /** Max value for column. */
  private static final int MAX_COLUMN = 1024;

  /** Constant for GPR BioAssay type. */
  public static final int GPR_BIOASSAY_TYPE = 1;
  /** Constant for GAL BioAssay type. */
  public static final int GAL_BIOASSAY_TYPE = 2;
  /** Constant for ImaGene results BioAssay type. */
  public static final int IMAGENE_RESULTS_BIOASSAY_TYPE = 3;
  /** Constant for ImaGene array list BioAssay type. */
  public static final int IMAGENE_ARRAYLIST_BIOASSAY_TYPE = 4;

  /**
   * Remove some rows of a bioassay object.
   * @param bioAssay BioAssay
   * @param rowsToRemove Array of the number of the rows to remove
   * @return A new BioAssay object without the deleted rows
   * @throws BioAssayRuntimeException if an error occurs while creating the new
   *           bioassay object
   */
  public static BioAssay removeRowsFromBioAssay(final BioAssay bioAssay,
      final int[] rowsToRemove) throws BioAssayRuntimeException {

    if (bioAssay == null)
      return null;
    if (rowsToRemove == null)
      return bioAssay;

    int size = bioAssay.size();
    Map mapFieldsToRemove = new HashMap();

    for (int i = 0; i < rowsToRemove.length; i++) {
      final int value = rowsToRemove[i];
      if (value < 0 || value >= size)
        throw new BioAssayRuntimeException("Row doesn't exits : " + value);
      mapFieldsToRemove.put("" + value, "");

    }

    int newSize = (size - mapFieldsToRemove.size());
    BioAssay result = BioAssayFactory.createBioAssay();

    String[] fields = bioAssay.getFields();

    for (int i = 0; i < fields.length; i++) {

      switch (bioAssay.getFieldType(fields[i])) {

      case BioAssay.DATATYPE_INTEGER:

        int[] dataInt = bioAssay.getDataFieldInt(fields[i]);
        int[] newDataInt = new int[newSize];
        int count = 0;

        for (int j = 0; j < dataInt.length; j++)
          if (!mapFieldsToRemove.containsKey("" + j))
            newDataInt[count++] = dataInt[j];

        result.setDataFieldInt(fields[i], newDataInt);

        break;

      case BioAssay.DATATYPE_DOUBLE:

        double[] dataDouble = bioAssay.getDataFieldDouble(fields[i]);
        double[] newDataDouble = new double[newSize];
        count = 0;

        for (int j = 0; j < dataDouble.length; j++)
          if (!mapFieldsToRemove.containsKey("" + j))
            newDataDouble[count++] = dataDouble[j];

        result.setDataFieldDouble(fields[i], newDataDouble);

        break;

      case BioAssay.DATATYPE_STRING:

        String[] dataString = bioAssay.getDataFieldString(fields[i]);
        String[] newDataString = new String[newSize];
        count = 0;

        for (int j = 0; j < dataString.length; j++)
          if (!mapFieldsToRemove.containsKey("" + j))
            newDataString[count++] = dataString[j];

        result.setDataFieldString(fields[i], newDataString);

        break;

      default:
        break;
      }

    }

    Annotation annotations = bioAssay.getAnnotation();
    result.getAnnotation().addProperties(annotations);

    return result;
  }

  /**
   * Test if an identifier of a spot is equals to one of the members of list of
   * identifiers
   * @param id Identifier to test
   * @param spotsIds Array of identifiers
   * @return true if the id is in the list of identifiers
   */
  public static boolean isIdEquals(final String id, final String[] spotsIds) {

    if (spotsIds == null || id == null)
      return false;

    for (int j = 0; j < spotsIds.length; j++) {
      String member = spotsIds[j];
      if (id.equals(member))
        return true;
    }

    return false;
  }

  /**
   * Count the number of empty spot (e. g. spot with "empty" id) in a bioassay
   * object.
   * @param bioassay Bioassay object to count
   * @return the number of empty identifier in a bioassay object
   */
  public static int countEmptySpots(final BioAssay bioassay) {

    if (bioassay == null)
      return 0;

    int count = 0;

    SpotIterator si = bioassay.iterator();

    while (si.hasNext()) {
      si.next();
      if (si.isEmpty())
        count++;
    }

    return count;
  }

  /**
   * Extract the meta row from a coded location.
   * @param location The coded location
   * @return The meta row from the coded location
   */
  public static int getMetaRow(final int location) {

    return ((location & (~MASK_META_ROW)) >> SHIFT_META_ROW) & MASK_6BITS;
  }

  /**
   * Extract the meta column from a coded location.
   * @param location The coded location
   * @return The meta column from the coded location
   */
  public static int getMetaColumn(final int location) {

    return ((location & (~MASK_META_COLUMN)) >> SHIFT_META_COLUMN) & MASK_6BITS;
  }

  /**
   * Extract the row from a coded location.
   * @param location The coded location
   * @return The row from the coded location
   */
  public static int getRow(final int location) {

    return ((location & (~MASK_ROW)) >> SHIFT_ROW) & MASK_10BITS;
  }

  /**
   * Extract the column from a coded location.
   * @param location The coded location
   * @return The column from the coded location
   */
  public static int getColumn(final int location) {

    return ((location & (~MASK_COL)) >> SHIFT_COLUMN) & MASK_10BITS;
  }

  /**
   * Set the meta line in coded location.
   * @param location The coded location
   * @param metaRow The value of meta line to set
   * @return The new coded location
   */
  public static int setMetaRow(final int location, final int metaRow) {

    return (location & MASK_META_ROW)
        | ((metaRow & MASK_6BITS) << SHIFT_META_ROW);
  }

  /**
   * Set the meta column in coded location.
   * @param location The coded location
   * @param metaColumn The value of meta column to set
   * @return The new coded location
   */
  public static int setMetaColumn(final int location, final int metaColumn) {

    return (location & MASK_META_COLUMN)
        | ((metaColumn & MASK_6BITS) << SHIFT_META_COLUMN);
  }

  /**
   * Set the line in coded location.
   * @param location The coded location
   * @param row The value of line to set
   * @return The new coded location
   */
  public static int setRow(final int location, final int row) {

    return (location & MASK_ROW) | ((row & MASK_10BITS) << SHIFT_ROW);
  }

  /**
   * Set the column in coded location.
   * @param location The coded location
   * @param column The value of column to set
   * @return The new coded location
   */
  public static int setColumn(final int location, final int column) {

    return (location & MASK_COL) | ((column & MASK_10BITS) << SHIFT_COLUMN);
  }

  /**
   * Get the maximum value of a meta row coordinate.
   * @return The maximum value of a meta row coordinate
   */
  public static int getMaxMetaRowValue() {
    return MAX_META_ROW;
  }

  /**
   * Get the maximum value of a meta column coordinate.
   * @return The maximum value of a meta column coordinate
   */
  public static int getMaxMetaColumnValue() {
    return MAX_META_COLUMN;
  }

  /**
   * Get the maximum value of a row coordinate.
   * @return The maximum value of a row coordinate
   */
  public static int getMaxRowValue() {
    return MAX_ROW;
  }

  /**
   * Get the maximum value of a column coordinate.
   * @return The maximum value of a column coordinate
   */
  public static int getMaxColumnValue() {
    return MAX_COLUMN;
  }

  /**
   * Encode a location.
   * @param metaRow MetaRow to set
   * @param metaColumn metaColumn to set
   * @param row Row to set
   * @param column Column to set
   * @return An encoded location
   */
  public static int encodeLocation(final int metaRow, final int metaColumn,
      final int row, final int column) {

    return setMetaRow(setMetaColumn(setRow(setColumn(0, column), row),
        metaColumn), metaRow);
  }

  /**
   * Encode a location.
   * @param metaRow MetaRow to set
   * @param metaColumn metaColumn to set
   * @param row Row to set
   * @param column Column to set
   * @return An array of encoded locations
   */
  public static int[] encodeLocation(final int[] metaRow,
      final int[] metaColumn, final int[] row, final int[] column) {

    if (metaRow == null || metaColumn == null || row == null || column == null
        || metaRow.length != metaColumn.length
        || metaColumn.length != row.length || row.length != column.length)
      return null;

    int[] result = new int[metaRow.length];

    for (int i = 0; i < result.length; i++)
      result[i] = encodeLocation(metaRow[i], metaColumn[i], row[i], column[i]);

    return result;
  }

  /**
   * Calculate M and A values for bioassay object
   * @param bioAssay object which we want calculate M and A
   */
  public static void calcMA(final BioAssay bioAssay) {

    if (bioAssay == null)
      throw new NividicRuntimeException(NividicRuntimeException.NULL_POINTER,
          "bioassay");

    int[] red = bioAssay.getReds();
    int[] green = bioAssay.getGreens();

    if (red == null || green == null)
      throw new NividicRuntimeException(NividicRuntimeException.NULL_POINTER,
          "one or more intensity is null");

    final int size = red.length;

    double[] m = new double[size];
    double[] a = new double[size];

    for (int i = 0; i < green.length; i++) {
      a[i] = calcA(red[i], green[i]);
      m[i] = calcM(red[i], green[i]);
    }

    bioAssay.setAs(a);
    bioAssay.setMs(m);

    final HistoryEntry entry = new HistoryEntry("calc MA",
        HistoryActionType.MODIFY, null, HistoryActionResult.PASS);

    bioAssay.getHistory().add(entry);
  }

  /**
   * Swap M values.
   * @param bioAssay BioAssay object to process
   */
  public static void swap(final BioAssay bioAssay) {

    if (bioAssay == null)
      throw new NividicRuntimeException(NividicRuntimeException.NULL_POINTER,
          "bioassay");

    if (!bioAssay.isMs())
      throw new NividicRuntimeException(
          "The M field of the bioAssay is not set");

    double[] ms = bioAssay.getMs();

    for (int i = 0; i < ms.length; i++) {
      ms[i] = -ms[i];
    }

    bioAssay.setMs(ms);

    final HistoryEntry entry = new HistoryEntry("swap",
        HistoryActionType.MODIFY, null, HistoryActionResult.PASS);

    bioAssay.getHistory().add(entry);
  }

  /**
   * Calculate ratio value for bioassay object
   * @param bioAssay object which we want calculate the ratio
   */
  public static void calcRatio(final BioAssay bioAssay) {

    if (bioAssay == null)
      throw new NividicRuntimeException(NividicRuntimeException.NULL_POINTER,
          "bioassay");

    int[] red = bioAssay.getReds();
    int[] green = bioAssay.getGreens();

    if (red == null || green == null)
      throw new NividicRuntimeException(NividicRuntimeException.NULL_POINTER,
          "one or more intensity is null");

    final int size = red.length;

    double[] ratio = new double[size];

    for (int i = 0; i < green.length; i++) {
      ratio[i] = red[i] / green[i];
    }

    final HistoryEntry entry = new HistoryEntry("calc rotios",
        HistoryActionType.MODIFY, null, HistoryActionResult.PASS);

    bioAssay.getHistory().add(entry);
  }

  /**
   * calculate A value from intensity values of a spot
   * @param red value of the spot intensity
   * @param green value of the spot intensity
   * @return the A value
   */
  public static double calcA(final int red, final int green) {
    return MathUtils.log2(Math.sqrt(green * red));
  }

  /**
   * calculate M value from intensity values of a spot
   * @param red value of the spot intensity
   * @param green value of the spot intensity
   * @return the M value
   */
  public static double calcM(final int red, final int green) {
    return MathUtils.log2(red / green);
  }

  /**
   * Test if the bioAssay b contains all the location of the bioAssay b.
   * @param a BioAssay to test
   * @param b BioAssay to test
   * @return true if BioAssay b contains all the location of the BioAssay a
   */
  public static boolean containLocations(final BioAssay a, final BioAssay b) {

    if (a == null || a == null)
      return false;

    // Test if the gal file is compatible with the gpr data
    SpotIterator si = b.iterator();
    while (si.hasNext()) {
      si.next();
      if (a.getIndexFromALocation(si.getLocation()) == -1)
        return false;
    }

    return true;
  }

  /**
   * Replace Ids of a BioAssay by the id from an feature annoatation
   * @param bioAssay Bioassay to modify
   * @param annotation Feature annotation to use
   * @param annotationField field of the annotation to use
   * @param unknowId Value for empty annotations.
   */
  public static void replaceIdsByAnnotation(final BioAssay bioAssay,
      final Translator annotation, final String annotationField,
      final String unknowId) {

    if (bioAssay == null || annotation == null || annotationField == null)
      return;

    String[] ids = bioAssay.getIds();

    if (ids == null)
      return;

    String[] newIds = annotation.translateField(ids, annotationField);

    if (unknowId != null)
      for (int i = 0; i < newIds.length; i++)
        if (newIds[i] == null)
          newIds[i] = unknowId;

    bioAssay.setIds(newIds);
  }

  /**
   * Test if the identifiers and the locations (and in the same order) of two
   * bioassay objects are equals.
   * @param a First BioAssay object to test
   * @param b Second BioAssat object to test.
   * @return true if the identifiers and the locations of two bioassay objects
   *         are equals
   */
  public static boolean equalsIdsAndLocations(final BioAssay a, final BioAssay b) {

    if (a == null && b == null)
      return true;
    if (a == null || b == null)
      return false;

    String[] idsA = a.getIds();
    String[] idsB = b.getIds();

    if (!NividicUtils.stringsEquals(idsA, idsB))
      return false;

    int[] locsA = a.getLocations();
    int[] locsB = b.getLocations();

    if (!NividicUtils.intsEquals(locsA, locsB))
      return false;

    return true;
  }

  private static void addFieldToBioAssay(final BioAssay result, final BioAssay a) {

    if (result == null || a == null)
      return;

    String[] fields = a.getFields();

    for (int i = 0; i < fields.length; i++) {

      final String f = fields[i];

      switch (a.getFieldType(f)) {
      case BioAssay.DATATYPE_DOUBLE:
        result.setDataFieldDouble(f, a.getDataFieldDouble(f));
        break;
      case BioAssay.DATATYPE_INTEGER:
        result.setDataFieldInt(f, a.getDataFieldInt(f));
        break;
      case BioAssay.DATATYPE_STRING:
        result.setDataFieldString(f, a.getDataFieldString(f));
        break;
      default:
        break;
      }

    }

  }

  /**
   * Merge two bioAssay to a BioAssay
   * @param a First BioAssay to merge
   * @param b First BioAssay to merge
   * @param newBioAssay if a new BioAssay must be created
   * @return a new BioAssay
   */
  public static BioAssay merge(final BioAssay a, final BioAssay b,
      final boolean newBioAssay) {

    if (a == null)
      return b;
    if (b == null)
      return a;

    if (!equalsIdsAndLocations(a, b))
      throw new BioAssayRuntimeException(
          "The identifiers and locations are not the same in the two BioAssay objects");

    BioAssay result;

    if (newBioAssay) {
      result = BioAssayFactory.createBioAssay();
      Annotation annot = result.getAnnotation();
      Annotation annotA = a.getAnnotation();
      Annotation annotB = a.getAnnotation();
      annot.addProperties(annotA);
      annot.addProperties(annotB);
      addFieldToBioAssay(result, a);
    } else {
      result = a;
      a.getAnnotation().addProperties(b.getAnnotation());
    }

    addFieldToBioAssay(result, b);

    final HistoryEntry entry = new HistoryEntry("merge bioAssay",
        HistoryActionType.MODIFY, a.getName() + " " + b.getName(),
        HistoryActionResult.PASS);

    result.getHistory().add(entry);

    return result;
  }

  /**
   * Merge two bioAssay to a new BioAssay
   * @param a First BioAssay to merge
   * @param b First BioAssay to merge
   * @return a new BioAssay
   */
  public static BioAssay merge(final BioAssay a, final BioAssay b) {

    return merge(a, b, true);
  }

  /**
   * Test if a BioAssay is a GPR BioAssay.
   * @param ba BioAssay to test
   * @return true if the BioAssay is a GPR BioAssay
   */
  public static boolean isGPRBioAssay(final BioAssay ba) {

    if (ba == null)
      return false;

    GenepixResults gr = new GenepixResults(ba);

    return gr.isGPRData();
  }

  /**
   * Test if a BioAssay is a GAL BioAssay.
   * @param ba BioAssay to test
   * @return true if the BioAssay is a GPR BioAssay
   */
  public static boolean isGALBioAssay(final BioAssay ba) {

    if (ba == null)
      return false;

    GenepixArrayList gal = new GenepixArrayList(ba);

    return gal.isGALData();
  }

  /**
   * Test if a BioAssay is an Imagene Result data.
   * @param ba BioAssay to test
   * @return true if the BioAssay is an Imagene Result data
   */
  public static boolean isImageneResultBioAssay(final BioAssay ba) {

    if (ba == null)
      return false;

    ImaGeneResult igr = new ImaGeneResult(ba);

    return igr.isImageneResultData();
  }

  /**
   * Test if a BioAssay is an Imagene array list.
   * @param ba BioAssay to test
   * @return true if the BioAssay is an Imagene array list
   */
  public static boolean isImageneArrayListBioAssay(final BioAssay ba) {

    if (ba == null)
      return false;

    ImaGeneArrayList igal = new ImaGeneArrayList(ba);

    return igal.isImageneArrayList();
  }

  /**
   * Get the type of the BioAssay.
   * @param ba The bioAssay to test
   * @return the type of the bioAssay or -1 if the type is unknown
   */
  public static int getBioAssayType(final BioAssay ba) {

    if (ba == null)
      return -1;

    if (isGPRBioAssay(ba))
      return GPR_BIOASSAY_TYPE;
    if (isGALBioAssay(ba))
      return GAL_BIOASSAY_TYPE;
    if (isImageneResultBioAssay(ba))
      return IMAGENE_RESULTS_BIOASSAY_TYPE;
    if (isImageneArrayListBioAssay(ba))
      return IMAGENE_ARRAYLIST_BIOASSAY_TYPE;

    return -1;
  }

  /**
   * Print a BioAssay object.
   * @param bioAssay BioAssay to print
   */
  public static void printBioAssay(final BioAssay bioAssay) {

    if (bioAssay == null)
      return;

    String[] fields = bioAssay.getFields();
    String[] ids = bioAssay.getIds();

    for (int i = 0; i < fields.length; i++) {
      if (i > 0)
        System.out.print("\t");
      System.out.print(fields[i]);
    }
    System.out.println();

    for (int i = 0; i < ids.length; i++) {

      for (int j = 0; j < fields.length; j++) {

        switch (bioAssay.getFieldType(fields[j])) {
        case BioAssayBase.DATATYPE_DOUBLE:
          System.out.print(bioAssay.getDataFieldDouble(fields[j])[i]);
          break;

        case BioAssayBase.DATATYPE_INTEGER:
          System.out.print(bioAssay.getDataFieldInt(fields[j])[i]);
          break;

        case BioAssayBase.DATATYPE_STRING:
          System.out.print(bioAssay.getDataFieldString(fields[j])[i]);
          break;

        default:
          break;
        }

        System.out.print("\t");

      }
      System.out.println();

    }

  }

  /**
   * Merge all the rows with the same identifiers.
   * @param bioAssay BioAssay to merge
   * @return a new bioAssay
   */
  public static BioAssay mergeInnerIdsReplicates(final BioAssay bioAssay) {

    if (bioAssay == null)
      return null;

    BioAssayMerger bam = new BioAssayMerger(bioAssay);
    bam.mergeAllIds();
    BioAssay result = bam.getBioAssay();

    final HistoryEntry entry = new HistoryEntry("merge ids",
        HistoryActionType.MODIFY, null, HistoryActionResult.PASS);

    result.getHistory().add(entry);

    return result;
  }

  /**
   * Merge all the rows with the same identifiers.
   * @param bioAssay BioAssay to merge
   * @return a new bioAssay
   */
  public static BioAssay mergeInnerDescriptionReplicates(final BioAssay bioAssay) {

    if (bioAssay == null)
      return null;

    BioAssayMerger bam = new BioAssayMerger(bioAssay);
    bam.mergeAllDescription();
    BioAssay result = bam.getBioAssay();

    final HistoryEntry entry = new HistoryEntry("merge descriptions",
        HistoryActionType.MODIFY, null, HistoryActionResult.PASS);

    result.getHistory().add(entry);

    return result;
  }

  /**
   * Rename the identifiers of the bioAssay with uniques Indentifers
   * @param bioAssay BioAssay to modify
   */
  public static void renameIdsWithUniqueIdentifiers(final BioAssay bioAssay) {

    if (bioAssay == null)
      return;

    final String[] ids = bioAssay.getIds();

    if (ids == null)
      return;

    Map<String, Integer> idsCount = new HashMap<String, Integer>();

    for (int i = 0; i < ids.length; i++) {

      final String id = ids[i];

      if (id == null)
        continue;

      int count;

      if (!idsCount.containsKey(id))
        count = 0;
      else
        count = idsCount.get(id);

      count++;

      if (count > 1)
        ids[i] = id + "#" + count;

      idsCount.put(id, count);
    }

    final HistoryEntry entry = new HistoryEntry("create unique identifers",
        HistoryActionType.MODIFY, null, HistoryActionResult.PASS);

    bioAssay.getHistory().add(entry);
  }

  //
  // Constructor
  //

  /**
   * Private constructor.
   */
  private BioAssayUtils() {
  }

}