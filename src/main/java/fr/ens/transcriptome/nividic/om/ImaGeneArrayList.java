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

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 * A wrapper for a BioAssay object which provides methods for an easy access to
 * the object properties for real Imagene arraylist data.
 * @author Laurent Jourdren
 */
public class ImaGeneArrayList {

  /** The wrapped object. */
  private BioAssay bioAssay;

  /** Imagene Array list magic string. */
  public static final String IMAGENE_ARRAYLIST_MAGIC_STRING = "ImaGene Array List";
  private static final String IMAGENE_ARRAYLIST_HEADER_TYPE = "Type";

  //
  // Method for access to the wrapped object
  //

  /**
   * Return the wrapped object.
   * @return The BioAssay object wrapped in this object.
   */
  public BioAssay getBioAssay() {
    return bioAssay;
  }

  /**
   * Set the wrapped object.
   * @param bioAssay The object to be wrapped
   */
  private void setBioAssay(final BioAssay bioAssay) {
    this.bioAssay = bioAssay;
  }

  //
  // Getters
  //

  /**
   * Get the type of the file.
   * @return The type of the file
   */
  public String getType() {

    Annotation annotation = getBioAssay().getAnnotation();
    if (annotation == null)
      return null;

    return annotation.getProperty(IMAGENE_ARRAYLIST_HEADER_TYPE);
  }

  /**
   * Test if data is data from an imagene array list.
   * @return true if data cames from an imagene array list
   */
  public boolean isImageneArrayList() {

    String type = getType();

    if (type == null)
      return false;

    return type.startsWith(IMAGENE_ARRAYLIST_MAGIC_STRING);
  }

  //
  // Setters
  //

  /**
   * Set the type of Imagene result file.
   * @param type The type of Imagene result file to set
   */
  public void setType(final String type) {

    Annotation annotation = getBioAssay().getAnnotation();
    if (annotation == null)
      return;

    annotation.setProperty(IMAGENE_ARRAYLIST_HEADER_TYPE, type);
  }

  //
  // Other methods
  //

  /**
   * Get the array blocks of the chip.
   * @return The array blocks of the chip
   */
  public ArrayBlock[] getBlocks() {

    Map blocks = new TreeMap();

    SpotIterator si = getBioAssay().iterator();
    int count = 0;

    while (si.hasNext()) {
      si.next();

      final int mr = si.getMetaRow();
      final int mc = si.getMetaColumn();

      final Integer wmr = new Integer(mr);
      final Integer wmc = new Integer(mc);

      final Map ssBlocks;

      if (!blocks.containsKey(wmr)) {
        ssBlocks = new TreeMap();
        blocks.put(wmr, ssBlocks);
      } else
        ssBlocks = (Map) blocks.get(wmr);

      final ArrayBlock ab;

      if (!ssBlocks.containsKey(wmc)) {
        ab = new ArrayBlock();

        count++;
        ab.setId(count);
        ab.setMetaRow(mr);
        ab.setMetaColumn(mc);

        ssBlocks.put(wmc, ab);
      } else
        ab = (ArrayBlock) ssBlocks.get(wmc);

      int row = si.getRow();
      int col = si.getColumn();

      if (row > ab.getYFeatures())
        ab.setYFeatures(row);
      if (col > ab.getXFeatures())
        ab.setXFeatures(col);

    }

    ArrayBlock[] result = new ArrayBlock[count];

    count = 0;
    Iterator it1 = blocks.keySet().iterator();

    while (it1.hasNext()) {

      final Map ssBlocks = (Map) blocks.get(it1.next());

      Iterator it2 = ssBlocks.keySet().iterator();

      while (it2.hasNext())
        result[count++] = (ArrayBlock) ssBlocks.get(it2.next());

    }

    return result;
  }

  //
  // Constructor
  //

  /**
   * Standard constructor.
   * @param bioAssay BioAssay object to be wrapped, must be not null
   * @throws NullPointerException if parameter is null
   */
  public ImaGeneArrayList(final BioAssay bioAssay) throws NullPointerException {

    if (bioAssay == null)
      throw new NullPointerException("BioAssay parameter is null");
    setBioAssay(bioAssay);
  }

}
