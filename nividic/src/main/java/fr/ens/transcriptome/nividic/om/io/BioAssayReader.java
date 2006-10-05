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

package fr.ens.transcriptome.nividic.om.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.primitives.ArrayDoubleList;
import org.apache.commons.collections.primitives.ArrayIntList;

import fr.ens.transcriptome.nividic.om.Annotation;
import fr.ens.transcriptome.nividic.om.BioAssay;
import fr.ens.transcriptome.nividic.om.BioAssayFactory;
import fr.ens.transcriptome.nividic.om.BioAssayRuntimeException;
import fr.ens.transcriptome.nividic.util.StringUtils;

/**
 * This abstract class defines how to read BioAssay File. Warning: when reading
 * integer fields in <code>read</code>, non int values will be replaced by 0
 * @author Laurent Jourdren
 */
public abstract class BioAssayReader {

  private BioAssay bioAssay = BioAssayFactory.createBioAssay();;
  private Set fieldsToRead = new HashSet();
  private boolean readAllFields;
  // private String[] fields;
  private InputStream is;
  private BufferedReader bufferedReader;
  private Map data = new HashMap();

  private static final int INITIAL_CAPACITY = 1000;

  //
  // Getters
  //

  /**
   * Get the bioAssay object.
   * @return Returns the bioAssay
   */
  protected BioAssay getBioAssay() {
    return bioAssay;
  }

  /**
   * Test if all the fields must be read.
   * @return true if all fields must be read
   */
  private boolean isAllFieldsToRead() {
    return this.readAllFields;
  }

  /**
   * Get the input stream.
   * @return Returns the input stream
   */
  private InputStream getInputStream() {
    return is;
  }

  /**
   * Get the buffered reader of the stream.
   * @return Returns the bufferedReader
   */
  public BufferedReader getBufferedReader() {
    return bufferedReader;
  }

  //
  // Setters
  //

  /**
   * Add all the field to read.
   */
  public void addAllFieldsToRead() {
    this.readAllFields = true;
  }

  /**
   * Set the input stream.
   * @param is The input stream to set
   * @throws NividicIOException if the stream is null
   */
  protected void setInputStream(final InputStream is) throws NividicIOException {

    if (is == null)
      throw new NividicIOException("No stream to read");
    this.is = is;
  }

  /**
   * Set the buffered reader of the stream.
   * @param bufferedReader The bufferedReader to set
   */
  private void setBufferedReader(final BufferedReader bufferedReader) {
    this.bufferedReader = bufferedReader;
  }

  //
  // Abstract methods
  //

  /**
   * ReadHeaders of the file.
   * @throws NividicIOException if an error occurs while reading ATF stream
   */
  public abstract void readHeader() throws NividicIOException;

  /**
   * Get the names of the integer fields.
   * @return An string array of field names
   */
  protected abstract String[] getIntFieldNames();

  /**
   * Get the names of the double fields.
   * @return An string array of field names
   */
  protected abstract String[] getDoubleFieldNames();

  /**
   * Get the fields names order.
   * @return A string array for fieldnames of the file
   */
  protected abstract String[] getFieldNamesOrder();

  /**
   * Get the convert of fiednames
   * @return The converter of fieldnames
   */
  protected abstract FieldNameConverter getFieldNameConverter();

  /**
   * Get the version of the format of the file
   * @return The version of the file.
   */
  public abstract String getFormatVersion();

  /**
   * Get the meta row field name.
   * @return The name of the field for meta row
   */
  protected abstract String getMetaRowField();

  /**
   * Get the meta column field name.
   * @return The name of the field for meta column
   */
  protected abstract String getMetaColumnField();

  /**
   * Get the row field name.
   * @return The name of the field for row
   */
  protected abstract String getRowField();

  /**
   * Get the column field name.
   * @return The name of the field for column
   */
  protected abstract String getColumnField();

  /**
   * Get the separator field of the file.
   * @return The separator field of the file
   */
  protected abstract String getSeparatorField();

  /**
   * Test if the Strings quotes must be removed.
   * @return true if the quotes of the strings must be removed
   */
  protected abstract boolean isStringQuotesBeRemoved();

  /**
   * Get the end tag of the stream. Null is not exists.
   * @return the end tag of the stream. Null is not exists
   */
  protected abstract String getEndTag();

  //
  // Other methods
  //

  /**
   * Inititialize varibles each time an input stream is set.
   */
  public void clear() {

    this.bioAssay = BioAssayFactory.createBioAssay();
    // this.fieldsToRead = new HashSet();
    // this.readAllFields = false;
    // this.fields = null;
    this.data = new HashMap();
  }

  /**
   * Add an annotation.
   * @param key Key of the annotation
   * @param value Value of the annotation
   */
  public void addAnnotation(final String key, final String value) {

    Annotation annotations = getBioAssay().getAnnotation();

    /*
     * if (annotations == null) { annotations =
     * AnnotationFactory.createAnnotation();
     * getBioAssay().setAnnotation(annotations); }
     */

    annotations.setProperty(key, value);
  }

  /**
   * Add a field to read.
   * @param fieldName Fieldname to read
   */
  public void addFieldToRead(final String fieldName) {

    if (fieldName == null || this.fieldsToRead.contains(fieldName))
      return;

    String newFieldName = fieldName;

    if (getFieldNameConverter() != null)
      newFieldName = getFieldNameConverter().getOtherFieldName(fieldName);

    this.fieldsToRead.add(newFieldName);
  }

  /**
   * Get the number of field in the file.
   * @return The number of the fields
   */
  // public int getFieldsNumber() {
  // if (this.fields == null)
  // return 0;
  // return this.fields.length;
  // }
  /**
   * Create a boolean array of field to read.
   * @param fields Existing field in file
   * @return a boolean array
   */
  protected boolean[] createArrayOfFieldsToRead(final String[] fields) {

    if (fields == null)
      return null;

    boolean[] result = new boolean[fields.length];

    for (int i = 0; i < fields.length; i++)
      if (isAllFieldsToRead() || this.fieldsToRead.contains(fields[i]))
        result[i] = true;

    return result;
  }

  /**
   * Create a boolean array of int fields.
   * @param fields Existing field in file
   * @param typeFields A string array of field of a specified type (int,
   *          double...)
   * @return a boolean array
   */
  protected boolean[] createArrayOfTypeFields(final String[] fields,
      final String[] typeFields) {

    if (fields == null)
      return null;

    boolean[] result = new boolean[fields.length];
    if (typeFields == null)
      return result;

    for (int i = 0; i < typeFields.length; i++)
      searchFieldAndFlagIt(fields, typeFields[i], result);

    return result;
  }

  protected static void searchFieldAndFlagIt(final String[] fields,
      final String fieldToSearch, final boolean[] arrayResult) {

    if (fields == null || fieldToSearch == null || arrayResult == null)
      return;

    int pos = StringUtils.findStringInArrayString(fieldToSearch, fields);
    if (pos >= 0)
      arrayResult[pos] = true;

  }

  /**
   * Read data
   * @return A BioAssay object
   * @throws NividicIOException if an error occurs while reading the stream
   */
  public BioAssay read() throws NividicIOException {

    if (getInputStream() == null)
      throw new NividicIOException("No stream to read");

    addFieldToRead(getMetaRowField());
    addFieldToRead(getRowField());
    addFieldToRead(getMetaColumnField());
    addFieldToRead(getColumnField());

    setBufferedReader(new BufferedReader(
        new InputStreamReader(getInputStream())));

    readHeader();
    final String[] existingFields = getFieldNamesOrder();

    if (existingFields == null || existingFields.length == 0)
      return getBioAssay();

    final boolean[] arrayFieldsToRead = createArrayOfFieldsToRead(existingFields);
    final boolean[] arrayIntFields = createArrayOfTypeFields(existingFields,
        getIntFieldNames());
    final boolean[] arrayDoubleFields = createArrayOfTypeFields(existingFields,
        getDoubleFieldNames());

    // Flag location fields as integer fields
    searchFieldAndFlagIt(existingFields, getMetaRowField(), arrayIntFields);
    searchFieldAndFlagIt(existingFields, getRowField(), arrayIntFields);
    searchFieldAndFlagIt(existingFields, getMetaColumnField(), arrayIntFields);
    searchFieldAndFlagIt(existingFields, getColumnField(), arrayIntFields);

    BufferedReader br = getBufferedReader();
    final String separator = getSeparatorField();
    String line = null;

    final int columnCount = getFieldNamesOrder() == null ? 0
        : getFieldNamesOrder().length;

    final String endTag = getEndTag();

    try {
      while ((line = br.readLine()) != null) {

        if (endTag != null && line.startsWith(endTag))
          break;

        String[] data = line.split(separator);

        if (data.length != columnCount)
          continue;

        for (int i = 0; i < data.length; i++) {

          if (arrayFieldsToRead[i])
            if (arrayIntFields[i]) {
              int value;
              // integer values
              try {
                value = Integer.parseInt(data[i].trim());
              } catch (NumberFormatException e) {
                value = 0;
              }
              addDatafield(existingFields[i], value);

            } else if (arrayDoubleFields[i]) {
              double value;
              // Double values
              try {
                value = Double.parseDouble(data[i].trim());

              } catch (NumberFormatException e) {
                value = Double.NaN;
              }
              addDatafield(existingFields[i], value);

            } else {

              // String values
              String value = data[i].trim();
              if (isStringQuotesBeRemoved())
                value = new String(StringUtils.removeDoubleQuotes(value));

              addDatafield(existingFields[i], value);
            }

        }

      }
    } catch (IOException e) {
      // e.printStackTrace();
      throw new NividicIOException("Error while reading the file");
    }

    try {
      getBufferedReader().close();
    } catch (IOException e1) {
      throw new NividicIOException("Error while closing the file");
    }

    return settingReadedDataInBioAssay();
  }

  /**
   * Add an int value to the data.
   * @param field Field of the value to add
   * @param value Value to add
   */
  private void addDatafield(final String field, final int value) {

    if (field == null)
      return;

    ArrayIntList l = (ArrayIntList) this.data.get(field);
    if (l == null) {
      l = new ArrayIntList(INITIAL_CAPACITY);
      this.data.put(field, l);
    }
    l.add(value);
  }

  /**
   * Add an double value to the data.
   * @param field Field of the value to add
   * @param value Value to add
   */
  private void addDatafield(final String field, final double value) {

    if (field == null)
      return;

    ArrayDoubleList l = (ArrayDoubleList) this.data.get(field);
    if (l == null) {
      l = new ArrayDoubleList(INITIAL_CAPACITY);
      this.data.put(field, l);
    }
    l.add(value);
  }

  /**
   * Add a string value to the data.
   * @param field Field of the value to add
   * @param value Value to add
   */
  private void addDatafield(final String field, final String value) {

    if (field == null)
      return;

    ArrayList l = (ArrayList) this.data.get(field);
    if (l == null) {
      l = new ArrayList(INITIAL_CAPACITY);
      this.data.put(field, l);
    }
    l.add(value);
  }

  private BioAssay settingReadedDataInBioAssay() throws NividicIOException {

    int[] metaRow = null;
    int[] metaColumn = null;
    int[] row = null;
    int[] column = null;

    // Setting data in BioAssay object
    Iterator it = this.data.keySet().iterator();
    while (it.hasNext()) {

      String field = (String) it.next();
      if (StringUtils.findStringInArrayString(field, getIntFieldNames()) != -1) {

        // Setting integer fields
        int[] a = null;
        try {
          ArrayIntList l = (ArrayIntList) this.data.get(field);
          a = convert(l);
        } catch (ClassCastException e) {
          throw new NividicIOException("Invalid field type (integer) : "
              + field);
        }

        // if location field locations
        if (field.equals(getMetaRowField())) {
          metaRow = a;
          continue;
        } else if (field.equals(getMetaColumnField())) {
          metaColumn = a;
          continue;
        } else if (field.equals(getRowField())) {
          row = a;
          continue;
        } else if (field.equals(getColumnField())) {
          column = a;
          continue;
        }

        // other fields
        String newField = field;
        if (getFieldNameConverter() != null)
          newField = getFieldNameConverter().getBioAssayFieldName(newField);

        try {
          getBioAssay().setDataFieldInt(newField, a);
        } catch (BioAssayRuntimeException e) {
          throw new NividicIOException("Error while setting data in bioassay");
        }

      } else if (StringUtils.findStringInArrayString(field,
          getDoubleFieldNames()) != -1) {

        // Setting double fields
        ArrayDoubleList l = null;
        try {
          l = (ArrayDoubleList) this.data.get(field);
        } catch (ClassCastException e) {
          throw new NividicIOException("Invalid field type (integer) : "
              + field);
        }

        double[] a = new double[l.size()];
        for (int i = 0; i < a.length; i++)
          a[i] = l.get(i);

        String newField = field;
        if (getFieldNameConverter() != null)
          newField = getFieldNameConverter().getBioAssayFieldName(newField);

        try {

          getBioAssay().setDataFieldDouble(newField, a);
        } catch (BioAssayRuntimeException e) {
          throw new NividicIOException("Error while setting data in bioassay");
        }

      } else {

        ArrayList l = (ArrayList) this.data.get(field);
        String[] a = new String[l.size()];
        for (int i = 0; i < a.length; i++)
          a[i] = (String) l.get(i);

        String newField = field;
        if (getFieldNameConverter() != null)
          newField = getFieldNameConverter().getBioAssayFieldName(newField);

        try {
          getBioAssay().setDataFieldString(newField, a);
        } catch (BioAssayRuntimeException e) {

          throw new NividicIOException(
              "Error while setting data in bioassay : " + e.getMessage());
        }

      }

    }

    try {
      if (metaColumn != null && column != null && row != null)
        getBioAssay().setLocations(
            metaRow != null ? metaRow : new int[metaColumn.length], metaColumn,
            row, column);
    } catch (BioAssayRuntimeException e) {
      throw new NividicIOException("Unable to set location in BioAssay");
    }
    return getBioAssay();
  }

  /**
   * Convert an arraylist of int in an integer array.
   * @param list List to convert
   * @return An int array
   */
  private static int[] convert(final ArrayIntList list) {

    if (list == null)
      return null;

    int[] result = new int[list.size()];
    for (int i = 0; i < result.length; i++)
      result[i] = list.get(i);

    return result;
  }

  //
  // Public contrustors
  //

  /**
   * Public constructor.
   * @param file file to read
   * @throws NividicIOException if an error occurs while reading the file or if
   *           the file is null.
   */
  public BioAssayReader(final File file) throws NividicIOException {

    if (file == null)
      throw new NividicIOException("No file to load");

    try {
      setInputStream(new FileInputStream(file));
    } catch (FileNotFoundException e) {
      throw new NividicIOException("Error while reading file : "
          + file.getName());
    }

  }

  /**
   * Public constructor
   * @param is Input stream to read
   * @throws NividicIOException if the stream is null
   */
  public BioAssayReader(final InputStream is) throws NividicIOException {

    setInputStream(is);
  }

  /**
   * Public constructor.
   */
  protected BioAssayReader() {
  }

}