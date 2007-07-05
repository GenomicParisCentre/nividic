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

package fr.ens.transcriptome.nividic.om.translators;

import java.util.HashMap;
import java.util.Map;

import fr.ens.transcriptome.nividic.NividicRuntimeException;

/**
 * This class implements a translator for multicolumn annotation. The first
 * column is the identifier.
 * @author Laurent Jourdren
 */
public class MultiColumnTranslator extends BasicTranslator {

  private Map<String, Map<String, String>> annotations =
      new HashMap<String, Map<String, String>>();
  private String[] fieldNames;

  /**
   * Add data to the translator
   * @param rowData data to add
   */
  public void addRow(final String[] rowData) {

    if (rowData == null || rowData.length == 1)
      return;

    final String[] dataArray = arrayWithFirstElement(rowData);

    Map<String, String> dataMap = new HashMap<String, String>();

    final int sizeData = dataArray.length;
    final int sizeFields = this.fieldNames.length;

    final int size = Math.min(sizeData, sizeFields);

    for (int i = 0; i < size; i++)
      dataMap.put(fieldNames[i], dataArray[i]);

    this.annotations.put(rowData[0], dataMap);
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

  private String[] arrayWithFirstElement(final String[] data) {

    if (data == null)
      return null;

    final int size = data.length;

    String[] result = new String[size - 1];

    System.arraycopy(data, 1, result, 0, size - 1);

    return result;
  }

  //
  // Constructor
  //

  /**
   * Public constructor.
   * @param fieldNames Field names of the annotation
   */
  public MultiColumnTranslator(final String[] fieldNames) {

    if (fieldNames == null)
      throw new NullPointerException("fieldnames is null");

    if (fieldNames.length < 2)
      throw new NividicRuntimeException(
          "fieldNames must have at least 2 fields");

    this.fieldNames = arrayWithFirstElement(fieldNames);
    setDefaultField(fieldNames[1]);
  }

}
