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

import java.applet.Applet;

import fr.ens.transcriptome.nividic.om.Annotation;
import fr.ens.transcriptome.nividic.om.io.GPRConverterFieldNames;
import fr.ens.transcriptome.nividic.om.io.NividicIOException;
import fr.ens.transcriptome.nividic.util.NividicUtils;

/**
 * This class allow to load in memory Genepix dat only when the user request it.
 * @author Laurent Jourdren
 */
public class GenepixBioAssayImpl extends BioAssayLoadDataWhenNeededImpl {

  /** serial version for serialization. */
  static final long serialVersionUID = 3951209739865591061L;

  private GenepixObjectModel om;

  //
  // Getters
  //

  /**
   * Get the Genepix object model used to load data.
   * @return The Genepix object model used to load data
   */
  private GenepixObjectModel getObjectModel() {
    return om;
  }

  //
  // Implementation of abstract methods
  //

  /**
   * Test if data must be loaded.
   * @return true if data must be loaded
   */
  public boolean isLoader() {
    return true;
  }

  /**
   * Test if this field exists.
   * @param field The fieldname
   * @return <b>true </b> if the field exists
   */
  public boolean isFieldExists(final String field) {

    if (getObjectModel() == null)
      return false;

    return getObjectModel().isColumn(field);
  }

  /**
   * Load annotation to the bioassay object.
   */
  public void loadAnnotations() {

    if (getObjectModel() == null)
      return;

    String ch = getObjectModel().getHeaders();
    String[] lines = ch.split("\n");

    if (lines == null)
      return;

    //if (getAnnotation() == null)
    //  setAnnotation(AnnotationFactory.createAnnotation());
    Annotation annotations = getAnnotation();

    for (int i = 0; i < lines.length; i++) {
      String[] words = lines[i].split("=");
      if (words != null && words.length == 2)
        annotations.setProperty(words[0], words[1]);
    }
  }

  /**
   * Load the fieldnames.
   * @return an array of string containing the names of the fields
   */
  public String[] loadFieldNames() {

    if (getObjectModel() == null)
      return null;

    return getObjectModel().getColumnNames();
  }

  /**
   * Return an integer array with all data from a field.
   * @param field The field to get
   * @return A vector containing the field's data
   */
  public int[] loadDataFieldInt(final String field) {

    if (getObjectModel() == null)
      return null;

    return getObjectModel().exportToInt(field);
  }

  /**
   * Return a doubel array with all data from a field.
   * @param field The field to get
   * @return A vector containing the field's data
   */
  public double[] loadDataFieldDouble(final String field) {

    if (getObjectModel() == null)
      return null;

    return getObjectModel().exportToDouble(field);
  }

  /**
   * Return an integer array with all data from a field.
   * @param field The field to get
   * @return A vector containing the field's data
   */
  public String[] loadDataFieldString(final String field) {

    if (getObjectModel() == null)
      return null;

    return getObjectModel().exportToString(field);
  }

  /**
   * Set location field with encoded data location.
   */
  public void setLocationField() {

    int[] block = loadDataFieldInt("Block");
    int[] column = loadDataFieldInt("Column");
    int[] row = loadDataFieldInt("Row");

    if (block != null && column != null && row != null)
      setLocations(new int[block.length], block, row, column);
  }

  //
  // Constructor
  //

  /**
   * Public constructor.
   * @param applet Applet used to load data
   */
  public GenepixBioAssayImpl(final Applet applet) {

    try {
      this.om = new GenepixObjectModel(applet);
    } catch (NividicIOException e) {
      NividicUtils.nop();
    }

    loadAnnotationsAndFieldNames();
    setLocationField();
    setConverter(new GPRConverterFieldNames());

  }

}