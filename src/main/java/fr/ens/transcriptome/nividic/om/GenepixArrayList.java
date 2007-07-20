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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.StringTokenizer;

import fr.ens.transcriptome.nividic.NividicRuntimeException;

/**
 * A wrapper for a BioAssay object which provides methods for an easy access to
 * the object properties for GAL data.
 * @author Laurent Jourdren
 */
public class GenepixArrayList {

  /** The wrapped object. */
  private BioAssay bioAssay;

  private static final String GAL_MAGIC_STRING = "GenePix ArrayList";

  private static final String GAL_HEADER_TYPE = "Type";
  private static final String GAL_HEADER_BLOCKCOUNT = "BlockCount";
  private static final String GAL_HEADER_BLOCKTYPE = "BlockType";
  private static final String GAL_HEADER_URL = "URL";

  private static final String GAL_HEADER_SUPPLIER = "Supplier";
  private static final String GAL_HEADER_ARRAYERSOFTWARENAME =
      "ArrayerSoftwareName";
  private static final String GAL_HEADER_ARRAYERSOFTWAREVERSION =
      "ArrayerSoftwareVersion";
  private static final String GAL_HEADER_ARRAYNAME = "ArrayName";
  private static final String GAL_HEADER_ARRAYREVISON = "ArrayRevison";
  private static final String GAL_HEADER_SLIDEBARCODE = "SlideBarCode";
  private static final String GAL_HEADER_MANUFACTURER = "Manufacturer";

  private static final String GAL_HEADER_BLOCK_PREFIX = "Block";

  /** Block type rectangular. */
  public static final int BLOCK_TYPE_RECTANGULAR = 0;
  /** Orange packing #1. */
  public static final int BLOCK_TYPE_ORANGE_PACKING_1 = 1;
  /** Orange packing #2. */
  public static final int BLOCK_TYPE_ORANGE_PACKING_2 = 2;
  /** GAL Type version 1.0. */
  public static final String GAL_TYPE_1_0 = "GenePix Array List v1.0";

  //
  // Getters
  //

  /**
   * Get the name of the arrayer software.
   * @return Returns the arrayerSoftwareName
   */
  public String getArrayerSoftwareName() {

    Annotation annotation = getBioAssay().getAnnotation();
    if (annotation == null)
      return null;

    return annotation.getProperty(GAL_HEADER_ARRAYERSOFTWARENAME);
  }

  /**
   * Get the version of the arrayer software.
   * @return Returns the arrayerSoftwareVersion
   */
  public String getArrayerSoftwareVersion() {

    Annotation annotation = getBioAssay().getAnnotation();
    if (annotation == null)
      return null;

    return annotation.getProperty(GAL_HEADER_ARRAYERSOFTWAREVERSION);
  }

  /**
   * Get the name of the array.
   * @return Returns the arrayName
   */
  public String getArrayName() {

    Annotation annotation = getBioAssay().getAnnotation();
    if (annotation == null)
      return null;

    return annotation.getProperty(GAL_HEADER_ARRAYNAME);
  }

  /**
   * Get the version of the array.
   * @return Returns the arrayRevision
   */
  public String getArrayRevision() {

    Annotation annotation = getBioAssay().getAnnotation();
    if (annotation == null)
      return null;

    return annotation.getProperty(GAL_HEADER_ARRAYREVISON);
  }

  /**
   * Get the number of block described in the file.
   * @return Returns the blockCount
   */
  public int getBlockCount() {

    Annotation annotation = getBioAssay().getAnnotation();
    if (annotation == null)
      return 0;

    String s = annotation.getProperty(GAL_HEADER_BLOCKCOUNT);
    if (s == null) {

      // TODO Remove this (BUGFIX of SGDB lims gal generator)
      s = annotation.getProperty("Blockcount");
      if (s == null)
        return -1;
    }

    try {
      return Integer.parseInt(s);
    } catch (NumberFormatException e) {
      return -1;
    }

  }

  /**
   * @return Returns the blockType
   */
  public int getBlockType() {

    Annotation annotation = getBioAssay().getAnnotation();
    if (annotation == null)
      return -1;

    String s = annotation.getProperty(GAL_HEADER_BLOCKTYPE);

    if (s == null)
      return -1;
    try {
      return Integer.parseInt(s);
    } catch (NumberFormatException e) {
      return -1;
    }
  }

  /**
   * @return Returns the slideBarCode
   */
  public String getSlideBarCode() {

    Annotation annotation = getBioAssay().getAnnotation();
    if (annotation == null)
      return null;

    return annotation.getProperty(GAL_HEADER_SLIDEBARCODE);
  }

  /**
   * @return Returns the supplier
   */
  public String getSupplier() {

    Annotation annotation = getBioAssay().getAnnotation();
    if (annotation == null)
      return null;

    return annotation.getProperty(GAL_HEADER_SUPPLIER);
  }

  /**
   * @return Returns the type
   */
  public String getType() {

    Annotation annotation = getBioAssay().getAnnotation();
    if (annotation == null)
      return null;

    return annotation.getProperty(GAL_HEADER_TYPE);
  }

  /**
   * @return Returns the url
   */
  public String getUrl() {

    Annotation annotation = getBioAssay().getAnnotation();
    if (annotation == null)
      return null;

    return annotation.getProperty(GAL_HEADER_URL);
  }

  /**
   * Get the manufacturer of the array.
   * @return The manufacturer of the array
   */
  public String getManufacturer() {

    Annotation annotation = getBioAssay().getAnnotation();
    if (annotation == null)
      return null;

    return annotation.getProperty(GAL_HEADER_MANUFACTURER);
  }

  //
  // Setters
  //

  /**
   * Set the name of the arrayer software.
   * @param arrayerSoftwareName The arrayerSoftwareName to set
   */
  public void setArrayerSoftwareName(final String arrayerSoftwareName) {

    Annotation annotation = getBioAssay().getAnnotation();
    if (annotation == null)
      return;

    annotation.setProperty(GAL_HEADER_ARRAYERSOFTWARENAME, arrayerSoftwareName);
  }

  /**
   * Set the version of the arrayer software.
   * @param arrayerSoftwareVersion The arrayerSoftwareVersion to set
   */
  public void setArrayerSoftwareVersion(final String arrayerSoftwareVersion) {

    Annotation annotation = getBioAssay().getAnnotation();
    if (annotation == null)
      return;

    annotation.setProperty(GAL_HEADER_ARRAYERSOFTWAREVERSION,
        arrayerSoftwareVersion);
  }

  /**
   * Set the name of the array.
   * @param arrayName The arrayName to set
   */
  public void setArrayName(final String arrayName) {

    Annotation annotation = getBioAssay().getAnnotation();
    if (annotation == null)
      return;

    annotation.setProperty(GAL_HEADER_ARRAYNAME, arrayName);
  }

  /**
   * Set the version of the array.
   * @param arrayRevision The arrayRevision to set
   */
  public void setArrayRevision(final String arrayRevision) {

    Annotation annotation = getBioAssay().getAnnotation();
    if (annotation == null)
      return;

    annotation.setProperty(GAL_HEADER_ARRAYREVISON, arrayRevision);
  }

  /**
   * Set the number of block described.
   * @param blockCount The blockCount to set
   */
  public void setBlockCount(final int blockCount) {

    Annotation annotation = getBioAssay().getAnnotation();
    if (annotation == null)
      return;

    annotation.setProperty(GAL_HEADER_BLOCKCOUNT, "" + blockCount);
  }

  /**
   * Set the type of the block described.
   * @param blockType The blockType to set
   */
  public void setBlockType(final int blockType) {

    Annotation annotation = getBioAssay().getAnnotation();
    if (annotation == null)
      return;

    annotation.setProperty(GAL_HEADER_BLOCKTYPE, "" + blockType);
  }

  /**
   * Set the barcodes supported by the GAL.
   * @param slideBarCode The slideBarCode to set
   */
  public void setSlideBarCode(final String slideBarCode) {

    Annotation annotation = getBioAssay().getAnnotation();
    if (annotation == null)
      return;

    annotation.setProperty(GAL_HEADER_SLIDEBARCODE, slideBarCode);
  }

  /**
   * Set the name of the supplier.
   * @param supplier The supplier to set
   */
  public void setSupplier(final String supplier) {

    Annotation annotation = getBioAssay().getAnnotation();
    if (annotation == null)
      return;

    annotation.setProperty(GAL_HEADER_SUPPLIER, supplier);
  }

  /**
   * Set the type of the of the GAL file.
   * @param type The type to set
   */
  public void setType(final String type) {

    Annotation annotation = getBioAssay().getAnnotation();
    if (annotation == null)
      return;

    annotation.setProperty(GAL_HEADER_TYPE, type);
  }

  /**
   * Set the URL for the Go To the Web command.
   * @param url The url to set
   */
  public void setUrl(final String url) {

    Annotation annotation = getBioAssay().getAnnotation();
    if (annotation == null)
      return;

    annotation.setProperty(GAL_HEADER_URL, url);
  }

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

  /**
   * Set the manufacturer of the array.
   * @param manufacturer The manufacturer of the array
   */
  public void setManufacturer(final String manufacturer) {

    Annotation annotation = getBioAssay().getAnnotation();
    if (annotation == null)
      return;

    annotation.setProperty(GAL_HEADER_MANUFACTURER, manufacturer);
  }

  //
  // Other method
  //

  /**
   * Create an url to get information about a feature.
   * @param url URL to modify
   * @param id Identifier of the feature
   * @return An url to the feature information
   */
  public static URL getURLForAnId(final String url, final String id) {

    if (url == null || id == null)
      return null;

    String s = url.replaceAll("\\[id\\]", id);

    try {
      return new URL(s);
    } catch (MalformedURLException e) {
      return null;
    }

  }

  /**
   * Create an url to get information about a feature.
   * @param id Identifier of the feature
   * @return An url to the feature information
   */
  public URL getURLForAnId(final String id) {
    return getURLForAnId(getUrl(), id);
  }

  /**
   * Set a block in the ArrayList
   * @param block Block to set
   */
  public void setBlock(final ArrayBlock block) {

    if (block == null)
      return;

    Annotation annotation = getBioAssay().getAnnotation();
    if (annotation == null)
      return;

    String key = GAL_HEADER_BLOCK_PREFIX + block.getId();
    StringBuffer sb = new StringBuffer();
    sb.append(block.getXOrigin());
    sb.append(" ,");
    sb.append(block.getYOrigin());
    sb.append(" ,");
    sb.append(block.getFeatureDiameter());
    sb.append(" ,");
    sb.append(block.getXFeatures());
    sb.append(" ,");
    sb.append(block.getXSpacing());
    sb.append(" ,");
    sb.append(block.getYFeatures());
    sb.append(" ,");
    sb.append(block.getYSpacing());

    annotation.setProperty(key, sb.toString());
  }

  /**
   * Set a array of block and update the blockcount.
   * @param blocks Blocks to set
   */
  public void setBlocks(final ArrayBlock[] blocks) {

    if (blocks == null)
      return;

    for (int i = 0; i < blocks.length; i++) {
      setBlock(blocks[i]);
    }

    setBlockCount(blocks.length);
  }

  /**
   * Get a block
   * @param id Identifier of the block
   * @return A ArrayBlock object if the identifier exists
   */
  public ArrayBlock getBlock(final int id) {

    Annotation annotation = getBioAssay().getAnnotation();
    if (annotation == null)
      return null;

    final String key = GAL_HEADER_BLOCK_PREFIX + id;

    String value = annotation.getProperty(key);
    if (value == null)
      return null;

    ArrayBlock ab = new ArrayBlock();
    ab.setId(id);
    StringTokenizer st = new StringTokenizer(value, ",");

    try {
      if (st.hasMoreTokens())
        ab.setXOrigin(Double.parseDouble(st.nextToken().trim()));
      else
        return ab;

      if (st.hasMoreTokens())
        ab.setYOrigin(Double.parseDouble(st.nextToken().trim()));
      else
        return ab;

      if (st.hasMoreTokens())
        ab.setFeatureDiameter(Double.parseDouble(st.nextToken().trim()));
      else
        return ab;

      if (st.hasMoreTokens())
        ab.setXFeatures(Integer.parseInt(st.nextToken().trim()));
      else
        return ab;

      if (st.hasMoreTokens())
        ab.setXSpacing(Double.parseDouble(st.nextToken().trim()));
      else
        return ab;

      if (st.hasMoreTokens())
        ab.setYFeatures(Integer.parseInt(st.nextToken().trim()));
      else
        return ab;

      if (st.hasMoreTokens())
        ab.setYSpacing(Double.parseDouble(st.nextToken().trim()));
      else
        return ab;

      return ab;
    } catch (NumberFormatException e) {
      e.printStackTrace();
      System.out.println(e.getMessage());
      return null;
    }
  }

  /**
   * Get all block of the data.
   * @return An array of ArrayBlock
   */
  public ArrayBlock[] getBlocks() {

    int size = getBlockCount();

    ArrayBlock[] result = new ArrayBlock[size];

    double lastXOri = -1;
    int metaRow = 1;
    int metaColumn = 0;

    for (int i = 1; i <= size; i++) {

      final ArrayBlock r = getBlock(i);

      if (r == null)
        throw new NividicRuntimeException("Invalid block definition");

      if (r.getXOrigin() < lastXOri) {
        metaRow++;
        metaColumn = 1;
      } else
        metaColumn++;

      r.setMetaRow(metaRow);
      r.setMetaColumn(metaColumn);

      lastXOri = r.getXOrigin();

      result[i - 1] = r;
    }

    return result;
  }

  /**
   * Test if data is data from a GPR
   * @return true if data cames from a GPR
   */
  public boolean isGALData() {

    String type = getType();

    if (type == null)
      return false;

    return type.startsWith(GAL_MAGIC_STRING);
  }

  //
  // Constructor
  //

  /**
   * Standard constructor.
   * @param bioAssay BioAssay object to be wrapped, must be not null
   * @throws NividicRuntimeException if parameter is null
   */
  public GenepixArrayList(final BioAssay bioAssay)
      throws NividicRuntimeException {

    if (bioAssay == null)
      throw new NividicRuntimeException(NividicRuntimeException.NULL_POINTER,
          "BioAssay");
    setBioAssay(bioAssay);
  }

}