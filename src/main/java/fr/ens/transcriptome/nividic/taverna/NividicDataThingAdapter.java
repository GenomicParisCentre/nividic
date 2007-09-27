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

package fr.ens.transcriptome.nividic.taverna;

import java.awt.Image;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.embl.ebi.escience.baclava.DataThing;
import org.embl.ebi.escience.baclava.factory.DataThingFactory;

import fr.ens.transcriptome.nividic.om.Annotation;
import fr.ens.transcriptome.nividic.om.BioAssay;
import fr.ens.transcriptome.nividic.om.BiologicalList;
import fr.ens.transcriptome.nividic.om.ExpressionMatrix;
import fr.ens.transcriptome.nividic.om.ExpressionMatrixDimension;
import fr.ens.transcriptome.nividic.om.design.Design;
import fr.ens.transcriptome.nividic.om.filters.BioAssayFilter;
import fr.ens.transcriptome.nividic.om.filters.BiologicalListFilter;
import fr.ens.transcriptome.nividic.om.filters.ExpressionMatrixFilter;
import fr.ens.transcriptome.nividic.om.translators.Translator;
import fr.ens.transcriptome.nividic.util.NividicUtils;

/**
 * This class is designed as a wrapper for inputmaps and output maps. Values
 * that are typically stored in these maps are usually stored as DataThings. In
 * order to make the code cleaner, this wrapper, insures that values inserted
 * into these maps are inserted as DataThings. Last edited by $Author: sowen70 $
 * @author Mark
 */
public class NividicDataThingAdapter {

  private Map<String, DataThing> map;

  public Map<String, DataThing> getMap() {

    return this.map;
  }

  public BioAssay getBioAssay(final String key) {

    DataThing val = map.get(key);
    if (val == null) {
      return null;
    }

    return (BioAssay) val.getDataObject();
  }

  public void putBioAssay(String key, BioAssay bioAssay) {

    final DataThing val = new DataThing(bioAssay);
    map.put(key, val);
  }

  public List<BioAssay> getBioAssayList(final String key) {

    final DataThing val = map.get(key);

    if (val == null)
      return null;

    return (List<BioAssay>) val.getDataObject();
  }

  public BioAssay[] getArrayBioAssay(final String key) {

    DataThing val = map.get(key);
    if (val == null) {
      return null;
    }

    final List<BioAssay> list = (List<BioAssay>) val.getDataObject();

    return list.toArray(new BioAssay[list.size()]);
  }

  public ExpressionMatrix getExpressionMatrix(final String key) {

    DataThing val = map.get(key);
    if (val == null) {
      return null;
    }

    return (ExpressionMatrix) val.getDataObject();
  }

  public void putExpressionMatrix(String key, ExpressionMatrix matrix) {

    DataThing val = new DataThing(matrix);
    map.put(key, val);
  }

  public ExpressionMatrixDimension getExpressionMatrixDimension(final String key) {

    final DataThing val = map.get(key);
    if (val == null)
      return null;

    return (ExpressionMatrixDimension) val.getDataObject();
  }

  public void putExpressionMatrixDimension(String key,
      ExpressionMatrixDimension dimension) {

    final DataThing val = new DataThing(dimension);
    map.put(key, val);
  }

  public Annotation getAnnotation(final String key) {

    final DataThing val = map.get(key);
    if (val == null)
      return null;

    return (Annotation) val.getDataObject();
  }

  public void putAnnotation(String key, Annotation annotation) {

    DataThing val = new DataThing(annotation);
    map.put(key, val);
  }

  public BiologicalList getBiologicalList(final String key) {

    final DataThing val = map.get(key);
    if (val == null)
      return null;

    return (BiologicalList) val.getDataObject();
  }

  public void putBiologicalList(String key, BiologicalList list) {

    final DataThing val = new DataThing(list);
    map.put(key, val);
  }

  public Design getDesign(final String key) {

    final DataThing val = map.get(key);
    if (val == null)
      return null;

    return (Design) val.getDataObject();
  }

  public void putDesign(String key, Design design) {

    DataThing val = new DataThing(design);
    map.put(key, val);
  }

  public Translator getTranslator(final String key) {

    final DataThing val = map.get(key);
    if (val == null)
      return null;

    return (Translator) val.getDataObject();
  }

  public void putTranslator(String key, Translator translator) {

    DataThing val = new DataThing(translator);
    map.put(key, val);
  }

  //
  // Older methods
  //

  /**
   * This method gets a value out of the map and returns it as a string. It
   * performs the DataThing conversion for you.
   * @param key
   * @return
   */
  public String getString(String key) {

    final DataThing val = map.get(key);

    if (val == null)
      return null;

    return (String) val.getDataObject();
  }

  public List<String> getStringList(String key) {

    final DataThing val = map.get(key);

    if (val == null)
      return null;

    return (List<String>) val.getDataObject();
  }

  /**
   * This method gets an array of objects as a string array.
   * @param key
   * @return
   */
  public String[] getStringArray(String key) {

    return NividicUtils.toArray(getStringList(key));
  }

  /**
   * This method wraps the string value in a DataThing before storing it in the
   * hashmap.
   * @param key The key for the value.
   * @param value The value to be stored.
   */
  public void putString(String key, String value) {

    final DataThing val = new DataThing(value);
    this.map.put(key, val);
  }

  /**
   * This method puts a boolean value into the map.
   * @param key
   * @param value
   */
  public void putBoolean(String key, boolean value) {

    final DataThing val = new DataThing(String.valueOf(value));
    this.map.put(key, val);
  }

  /**
   * This method gets a boolean value from the hashmap
   * @param key
   * @return False if null, otherwise returns the value indicated by the key.
   */
  public boolean getBoolean(String key) {

    final DataThing val = map.get(key);

    if (val == null)
      return false;

    final String strVal = (String) val.getDataObject();

    return Boolean.valueOf(strVal).booleanValue();
  }

  /**
   * This method puts a string array into the DataThing map.
   * @param key The key for the value.
   * @param value The values to be stored.
   */
  public void putStringArray(String key, String[] values) {
    // ArrayList valueArray = new ArrayList();
    // valueArray = (ArrayList)Arrays.asList(values);
    DataThing val = new DataThing(Arrays.asList(values));
    this.map.put(key, val);
  }

  /**
   * This method puts a string array into the DataThing map.
   * @param key The key for the value.
   * @param value The values to be stored.
   */
  public void putStringList(String key, List<String> values) {
    // ArrayList valueArray = new ArrayList();
    // valueArray = (ArrayList)Arrays.asList(values);
    DataThing val = new DataThing(values);
    this.map.put(key, val);
  }

  /**
   * This method gets an integer value from the DataThing map
   * @param key The key for the value.
   * @return
   */
  public int getInt(String key) {

    final String strVal = getString(key);

    if (strVal == null)
      return 0;

    return Integer.parseInt(strVal);
  }

  /**
   * This method puts an integer value into the DataThing map
   * @param key The key for the value.
   * @param value The values to be stored.
   */
  public void putInt(String key, int value) {
    putString(key, String.valueOf(value));
  }

  /**
   * This method gets a two-dimensional array out of the DataThing map.
   * @param key
   * @return
   */
  public String[][] getArrayArray(String key) {
    String[][] values = null;
    DataThing val = map.get(key);

    return values;
  }

  /**
   * This method puts a two-dimensional array into the DataThing map.
   * @param key
   * @param value
   */
  public void putArrayArray(String key, String[][] value) {
    map.put(key, DataThingFactory.bake(value));
  }

  /**
   * This metod gets an ArrayList from the DataThing map.
   * @param key
   * @return
   */
  public ArrayList getArrayList(String key) {
    DataThing val = map.get(key);
    return (ArrayList) val.getDataObject();
  }

  /**
   * This method puts an ArrayList into the DataThing map
   * @param key
   * @param list
   */
  public void putArrayList(String key, ArrayList list) {

    DataThing val = new DataThing(list);
    map.put(key, val);
  }

  /**
   * This method puts a Serializable object into the map.
   * @param key
   * @param obj
   */
  public void putSerializable(String key, Serializable obj) {

    DataThing val = new DataThing(obj);
    map.put(key, val);
  }

  /**
   * This method gets a Serializable object from the map.
   * @param key
   * @return
   */
  public Serializable getSerializable(String key) {
    DataThing val = map.get(key);
    return (Serializable) val.getDataObject();
  }

  public Image getImage(String key) {
    DataThing val = map.get(key);
    return (Image) val.getDataObject();
  }

  public void putImage(String key, Image img) {

    final DataThing val = new DataThing(img);
    map.put(key, val);
  }

  public void putDouble(String key, double db) {

    final DataThing val = new DataThing(String.valueOf(db));
    map.put(key, val);
  }

  public double getDouble(String key) {

    final String sv = getString(key);
    if (sv == null)
      return Double.NaN;

    return Double.valueOf(sv);
  }

  public void putDoubleArray(String key, double[] db) {

    DataThing val = new DataThing(NividicUtils.toList(db));
    map.put(key, val);
  }

  public double[] getDoubleArray(String key) {

    return NividicUtils.toArray(getDoubleList(key));
  }

  public List<Double> getDoubleList(String key) {

    DataThing val = map.get(key);

    if (val == null)
      return null;

    return (List<Double>) val.getDataObject();
  }

  public void putIntArray(String key, int[] db) {

    DataThing val = new DataThing(NividicUtils.toList(db));
    map.put(key, val);
  }

  public List<Integer> getIntList(String key) {

    DataThing val = map.get(key);

    if (val == null)
      return null;

    return (List<Integer>) val.getDataObject();
  }

  public int[] getIntArray(String key) {

    return NividicUtils.toArray(getIntList(key));
  }

  public void putBioAssayFilter(String key, BioAssayFilter filter) {

    DataThing val = new DataThing(filter);
    map.put(key, val);
  }

  public BioAssayFilter getBioAssayFilter(String key) {
    DataThing val = map.get(key);
    return (BioAssayFilter) val.getDataObject();
  }

  public void putExpressionMatrixFilter(String key,
      ExpressionMatrixFilter filter) {

    DataThing val = new DataThing(filter);
    map.put(key, val);
  }

  public ExpressionMatrixFilter getExpressionMatrixFilter(String key) {
    DataThing val = map.get(key);
    return (ExpressionMatrixFilter) val.getDataObject();
  }

  public void putBiologicalListFilter(String key, BiologicalListFilter filter) {

    DataThing val = new DataThing(filter);
    map.put(key, val);
  }

  public ExpressionMatrixDimension[] getExpressionMatrixDimensionArray(
      String key) {

    List<ExpressionMatrixDimension> values =
        getExpressionMatrixDimensionList(key);

    if (values == null)
      return null;

    ExpressionMatrixDimension[] result =
        new ExpressionMatrixDimension[values.size()];

    for (int i = 0; i < result.length; i++)
      result[i] = values.get(i);

    return result;
  }

  public List<ExpressionMatrixDimension> getExpressionMatrixDimensionList(
      String key) {

    DataThing val = map.get(key);
    if (val == null)
      return null;

    return (List<ExpressionMatrixDimension>) val.getDataObject();
  }

  public void putExpressionMatrixDimensionArray(String key,
      ExpressionMatrixDimension[] dimensions) {

    DataThing val = new DataThing(dimensions);
    map.put(key, val);
  }

  public BiologicalListFilter getBiologicalListFilter(String key) {
    DataThing val = map.get(key);
    return (BiologicalListFilter) val.getDataObject();
  }

  //
  // Constructor
  //

  public NividicDataThingAdapter(Map<String, DataThing> map) {
    this.map = map;
  }

  public NividicDataThingAdapter() {
    this.map = new HashMap<String, DataThing>();
  }

}
