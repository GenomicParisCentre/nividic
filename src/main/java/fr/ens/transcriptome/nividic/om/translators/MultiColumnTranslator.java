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
 * of the �cole Normale Sup�rieure and the individual authors.
 * These should be listed in @author doc comments.
 *
 * For more information on the Nividic project and its aims,
 * or to join the Nividic mailing list, visit the home page
 * at:
 *
 *      http://www.transcriptome.ens.fr/nividic
 *
 */

package fr.ens.transcriptome.nividic.om.translators;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import fr.ens.transcriptome.nividic.NividicRuntimeException;
import fr.ens.transcriptome.nividic.util.NividicUtils;

/**
 * This class implements a translator for multicolumn annotation. The first
 * column is the identifier.
 * @author Laurent Jourdren
 */
public class MultiColumnTranslator extends BasicTranslator implements
    Serializable {

  static final long serialVersionUID = -6434086257744017469L;

  private Map<String, Map<String, String>> annotations =
      new HashMap<String, Map<String, String>>();
  private String[] fieldNames;

  /**
   * Add data to the translator. The first value of the array data is the unique
   * id for the traslator.
   * @param rowData data to add
   */
  public void addRow(final String[] rowData) {

    if (rowData == null || rowData.length == 0 || rowData.length == 1)
      return;

    final String[] dataArray = arrayWithoutFirstElement(rowData);

    addRow(rowData[0], dataArray);
  }

  /**
   * Add data to the translator.
   * @param id id for the traslator.
   * @param rowData data to add
   */
  public void addRow(final String id, final String[] rowData) {

    if (id == null || rowData == null)
      return;

    Map<String, String> dataMap = new HashMap<String, String>();

    final int sizeData = rowData.length;
    final int sizeFields = this.fieldNames.length;

    final int size = Math.min(sizeData, sizeFields);

    for (int i = 0; i < size; i++)
      dataMap.put(fieldNames[i], rowData[i]);

    this.annotations.put(id, dataMap);
  }

  //
  // Method for the Translator
  //

  /**
   * Get an ordered list of the annotations fields
   * @return an ordered list of the annotations fields.
   */
  public String[] getFields() {

    if (this.fieldNames == null)
      return null;

    final String[] result = this.fieldNames.clone();

    return result;
  }

  /**
   * Get an annotation for an feature
   * @param id Identifier of the feature
   * @param fieldName Field to get
   * @return A String with the request annotation of the Feature
   */
  public String translateField(final String id, final String fieldName) {

    if (id == null)
      return null;

    final String field;

    if (fieldName == null)
      field = getDefaultField();
    else
      field = fieldName;

    final Map<String, String> map = this.annotations.get(id);
    if (map == null)
      return null;

    return map.get(field);
  }

  /**
   * Clear the descriptions of the features.
   */
  public void clear() {

    this.annotations.clear();
  }

  private String[] arrayWithoutFirstElement(final String[] data) {

    if (data == null)
      return null;

    final int size = data.length;

    String[] result = new String[size - 1];

    System.arraycopy(data, 1, result, 0, size - 1);

    return result;
  }

  /**
   * Get the available identfiers by the translator if possible.
   * @return a array of string with the identifiers
   */
  public String[] getIds() {

    return NividicUtils.toArray(this.annotations.keySet());
  }

  //
  // Constructor
  //

  /**
   * Public constructor.
   * @param fieldNames Field names of the annotation
   */
  public MultiColumnTranslator(final String[] fieldNames) {

    this(fieldNames, true);
  }

  /**
   * Public constructor.
   * @param fieldNames Field names of the annotation
   * @param fieldNamesWithId false if the first element of the fieldname array
   *            is the key for the translator (must be ignored)
   */
  public MultiColumnTranslator(final String[] fieldNames,
      final boolean fieldNamesWithId) {

    if (fieldNames == null)
      throw new NullPointerException("fieldnames is null");

    if (fieldNamesWithId && fieldNames.length < 2)
      throw new NividicRuntimeException(
          "fieldNames must have at least 2 fields");

    if (!fieldNamesWithId && fieldNames.length < 1)
      throw new NividicRuntimeException(
          "fieldNames must have at least one fields");

    if (fieldNamesWithId) {
      this.fieldNames = arrayWithoutFirstElement(fieldNames);
      setDefaultField(fieldNames[1]);
    } else {
      this.fieldNames = fieldNames;
      setDefaultField(fieldNames[0]);
    }
  }

}
