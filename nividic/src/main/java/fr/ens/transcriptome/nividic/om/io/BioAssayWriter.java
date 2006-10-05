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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import fr.ens.transcriptome.nividic.om.BioAssay;
import fr.ens.transcriptome.nividic.om.BioAssayRuntimeException;

/**
 * This abstract class defines how to write BioAssay Stream.
 * @author Laurent Jourdren
 */
public abstract class BioAssayWriter {

  private OutputStream outputStream;
  private BioAssay bioAssay;
  private Set fieldsToWrite = new HashSet();
  private boolean writeAllFields;

  private String[] bioAssayFieldsToWrite;
  private int[] bioAssayFieldsToWriteTypes;
  private String[] streamFieldsToWrite;
  private int[] metaColumn;
  private int[] metaRow;
  private int[] column;
  private int[] row;
  private int bioAssaySize;

  //
  // Getters
  //

  /**
   * Ge the bioAssay object to write.
   * @return Returns the bioAssay
   */
  public BioAssay getBioAssay() {
    return bioAssay;
  }

  /**
   * Get the outputStream.
   * @return Returns the outputStream
   */
  public OutputStream getOutputStream() {
    return outputStream;
  }

  /**
   * Test if all field must be writed.
   * @return Returns the writeAllFields
   */
  public boolean isWriteAllFields() {
    return writeAllFields;
  }

  //
  // Setters
  //

  /**
   * Set the bioasay object.
   * @param bioAssay The bioAssay to set
   */
  public void setBioAssay(final BioAssay bioAssay) {
    this.bioAssay = bioAssay;
  }

  /**
   * Set the output stream
   * @param outputStream The outputStream to set
   */
  public void setOutputStream(final OutputStream outputStream) {
    this.outputStream = outputStream;
  }

  /**
   * Set if all the fields must be writed.
   */
  public void addAllFieldsToWrite() {
    this.writeAllFields = true;
  }

  //
  // Abstract Methods
  //

  /**
   * Get the convert of fiednames
   * @return The converter of fieldnames
   */
  protected abstract FieldNameConverter getFieldNameConverter();

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
   * Get the fields names order.
   * @return A string array for fieldnames of the file
   */
  protected abstract String[] getFieldNamesOrder();

  protected abstract void writeHeaders() throws NividicIOException;

  protected abstract void writeData() throws NividicIOException;

  //
  // Other methods
  //

  /**
   * Add a field to read.
   * @param fieldName Fieldname to read
   */
  public void addFieldToWrite(final String fieldName) {

    if (fieldName == null || this.fieldsToWrite.contains(fieldName))
      return;
    this.fieldsToWrite.add(fieldName);
  }

  /**
   * Convert an array list to an array of string.
   * @param al ArrayList to convert
   * @return An array of String
   */
  private String[] convert(final ArrayList al) {

    if (al == null)
      return null;

    String[] result = new String[al.size()];
    for (int i = 0; i < al.size(); i++)
      result[i] = (String) al.get(i);

    return result;
  }

  /**
   * Create the array of field names used by the write methods
   */
  private void createFieldsArrays() {

    final String[] order = getFieldNamesOrder();
    // Map flagsFields = new HashMap();
    ArrayList alBioAssayFieldsToWrite = new ArrayList();
    ArrayList alStreamFieldsToWrite = new ArrayList();

    final FieldNameConverter converter = getFieldNameConverter();

    for (int i = 0; i < order.length; i++) {

      final String bioAssayFieldName;

      if (converter == null)
        bioAssayFieldName = order[i];
      else
        bioAssayFieldName = converter.getBioAssayFieldName(order[i]);

      final String otherFieldName = order[i];

      if (isWriteAllFields()) {

        String[] fields = getBioAssay().getFields();

        for (int j = 0; j < fields.length; j++)
          if (fields[j].equals(bioAssayFieldName)) {

            alBioAssayFieldsToWrite.add(bioAssayFieldName);
            alStreamFieldsToWrite.add(otherFieldName);

            break;
          }

      }

      if (this.fieldsToWrite.contains(bioAssayFieldName)
          && !alBioAssayFieldsToWrite.contains(bioAssayFieldName)) {

        alBioAssayFieldsToWrite.add(bioAssayFieldName);
        alStreamFieldsToWrite.add(otherFieldName);
      }
    }

    this.bioAssayFieldsToWrite = convert(alBioAssayFieldsToWrite);
    this.streamFieldsToWrite = convert(alStreamFieldsToWrite);

    // Cached variable to optimize the code

    this.bioAssayFieldsToWriteTypes = new int[bioAssayFieldsToWrite.length];
    for (int i = 0; i < this.bioAssayFieldsToWrite.length; i++) {
      this.bioAssayFieldsToWriteTypes[i] = getBioAssay().getFieldType(
          bioAssayFieldsToWrite[i]);
    }

    this.bioAssaySize = getBioAssay().size();

  }

  /**
   * Get the name of the field
   * @param column Column of the field
   * @return The name of the field of a column
   */
  protected final String getFieldName(final int column) {

    if (this.streamFieldsToWrite == null || column < 0
        || column >= this.streamFieldsToWrite.length)
      return null;

    return this.streamFieldsToWrite[column];
  }

  /**
   * Get the type of a field to write
   * @param column Column of the field
   * @return The of the field to write
   */
  protected final int getFieldType(final int column) {

    if (this.bioAssayFieldsToWrite == null || column < 0
        || column >= this.bioAssayFieldsToWrite.length)

      return -1;

    // return getBioAssay().getFieldType(this.bioAssayFieldsToWrite[column]);
    return this.bioAssayFieldsToWriteTypes[column];
  }

  /**
   * Get the number of the column of the array to write.
   * @return The number of the column of the array to write
   */
  protected final int getColumnCount() {
    if (this.streamFieldsToWrite == null)
      return 0;
    return this.streamFieldsToWrite.length;
  }

  /**
   * Get the number of the row of the array to write.
   * @return The number of the column of the array to write
   */
  protected final int getRowColumn() {
    if (getBioAssay() == null)
      return 0;
    return getBioAssay().size();
  }

  private void setLocationFields() throws BioAssayRuntimeException {

    final int size = getBioAssay().size();
    this.metaRow = new int[size];
    this.metaColumn = new int[size];
    this.row = new int[size];
    this.column = new int[size];

    getBioAssay().getLocations(this.metaRow, this.metaColumn, this.row,
        this.column);

  }

  /**
   * Get the data of a cell of the BioAssay data object
   * @param row Row of the cell
   * @param column Column of the cell
   * @return The value of the cell
   */
  protected final String getData(final int row, final int column) {

    if (column < 0 || column >= this.bioAssayFieldsToWrite.length || row < 0
        || row >= this.bioAssaySize)
      return null;

    final String field = this.bioAssayFieldsToWrite[column];

    try {

      if (field.equals(getMetaColumnField())) {
        if (this.metaColumn == null)
          setLocationFields();
        return "" + this.metaColumn[row];

      } else if (field.equals(getMetaRowField())) {
        if (this.metaRow == null)
          setLocationFields();
        return "" + this.metaRow[row];

      } else if (field.equals(getColumnField())) {
        if (this.column == null)
          setLocationFields();
        return "" + this.column[row];

      } else if (field.equals(getRowField())) {
        if (this.row == null)
          setLocationFields();
        return "" + this.row[row];
      }

    } catch (BioAssayRuntimeException e) {
      return null;
    }

    switch (this.bioAssayFieldsToWriteTypes[column]) {
    // switch (getBioAssay().getFieldType(field)) {
    case BioAssay.DATATYPE_INTEGER:
      return "" + getBioAssay().getDataFieldInt(field)[row];

    case BioAssay.DATATYPE_DOUBLE:
      return "" + getBioAssay().getDataFieldDouble(field)[row];

    case BioAssay.DATATYPE_STRING:
      return "" + getBioAssay().getDataFieldString(field)[row];

    default:
      break;
    }

    return null;
  }

  /**
   * Write data.
   * @param bioAssay BioAssay to write
   * @throws NividicIOException if an error occurs while writing data
   */
  public void write(final BioAssay bioAssay) throws NividicIOException {

    if (bioAssay == null)
      throw new NividicIOException("No bioassay to write");

    setBioAssay(bioAssay);
    addFieldToWrite(getMetaRowField());
    addFieldToWrite(getRowField());
    addFieldToWrite(getMetaColumnField());
    addFieldToWrite(getColumnField());

    createFieldsArrays();

    writeHeaders();
    writeData();

  }

  //
  // Constructor
  //

  /**
   * Public constructor.
   * @param file file to read
   * @throws NividicIOException if an error occurs while reading the file or if
   *           the file is null.
   */
  public BioAssayWriter(final File file) throws NividicIOException {

    if (file == null)
      throw new NividicIOException("No file to load");

    try {
      setOutputStream(new FileOutputStream(file));
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
  public BioAssayWriter(final OutputStream is) throws NividicIOException {

    setOutputStream(is);
  }

}