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

/**
 * This class implement a description of a block of features.
 * @author Laurent Jourdren
 */
public class ArrayBlock {

  private int id;
  private int metaRow;
  private int metaColumn;
  private int xOrigin;
  private int yOrigin;
  private int featureDiameter;
  private int xFeatures;
  private int yFeatures;
  private int xSpacing;
  private int ySpacing;

  private int width = -1;
  private int height = -1;

  //
  // Getters
  //

  /**
   * Get the id of the block.
   * @return Returns the id
   */
  public int getId() {
    return id;
  }

  /**
   * Get the diameter of the features (in micrometers).
   * @return Returns the featureDiameter
   */
  public int getFeatureDiameter() {
    return featureDiameter;
  }

  /**
   * Get the meta column of the block.
   * @return Returns the metaColumn
   */
  public int getMetaColumn() {
    return metaColumn;
  }

  /**
   * Get the meta row of the block.
   * @return Returns the metaRow
   */
  public int getMetaRow() {
    return metaRow;
  }

  /**
   * Get the number of column of features in the block.
   * @return Returns the xFeatures
   */
  public int getXFeatures() {
    return xFeatures;
  }

  /**
   * Get the x position of the center of top leftmost feature of current block
   * (in micormeters).
   * @return Returns the xOrigin
   */
  public int getXOrigin() {
    return xOrigin;
  }

  /**
   * Get the number of rows of features in the block.
   * @return Returns the yFeatures
   */
  public int getYFeatures() {
    return yFeatures;
  }

  /**
   * Get the y position of center of top leftmost feature of block.
   * @return Returns the yOrigin
   */
  public int getYOrigin() {
    return yOrigin;
  }

  /**
   * Get the column spacing of the block.
   * @return Returns the ySpacing
   */
  public int getYSpacing() {
    return ySpacing;
  }

  /**
   * Get the row spacing of the block.
   * @return Returns the xSpacing
   */
  public int getXSpacing() {
    return xSpacing;
  }

  //
  // Setters
  //

  /**
   * Set the id of the block
   * @param id The id to set
   */
  public void setId(final int id) {
    this.id = id;
  }

  /**
   * Set the diameter of the features of the block.
   * @param featureDiameter The featureDiameter to set
   */
  public void setFeatureDiameter(final int featureDiameter) {
    this.featureDiameter = featureDiameter;
  }

  /**
   * Set the meta column of the block.
   * @param metaColumn The metaColumn to set
   */
  public void setMetaColumn(final int metaColumn) {
    this.metaColumn = metaColumn;
  }

  /**
   * Set the meta row of the block.
   * @param metaRow The metaRow to set
   */
  public void setMetaRow(final int metaRow) {
    this.metaRow = metaRow;
  }

  /**
   * Set the number of columns of features in the block.
   * @param features The xFeatures to set
   */
  public void setXFeatures(final int features) {
    xFeatures = features;
  }

  /**
   * Set the x position (in micrometer) of center of the top leftmost feature of
   * the block.
   * @param origin The xOrigin to set
   */
  public void setXOrigin(final int origin) {
    xOrigin = origin;
  }

  /**
   * Set the number of rows of features in the block
   * @param features The yFeatures to set
   */
  public void setYFeatures(final int features) {
    yFeatures = features;
  }

  /**
   * Set the y position (in micrometer) of center of the top leftmost feature of
   * the block.
   * @param origin The yOrigin to set
   */
  public void setYOrigin(final int origin) {
    yOrigin = origin;
  }

  /**
   * Set the column spacing of current block (in micrometer).
   * @param spacing The ySpacing to set
   */
  public void setYSpacing(final int spacing) {
    ySpacing = spacing;
  }

  /**
   * Set the row spacing of current block (in micrometer).
   * @param spacing The xSpacing to set
   */
  public void setXSpacing(final int spacing) {
    xSpacing = spacing;
  }

  //
  // Other methods
  //

  /**
   * Get the width of the block
   * @return the width of the block
   */
  public int getWidth() {

    //if (this.width == -1) {
      //this.width = getXFeatures() * (getFeatureDiameter() + getXSpacing());
      this.width = getXFeatures() * getXSpacing();
    //}

    return this.width;
  }

  /**
   * Get the height of the block
   * @return The height of the block
   */
  public int getHeight() {

    //if (this.height == -1) {
      // this.height = getYFeatures() * (getFeatureDiameter() + getYSpacing());
      this.height = getYFeatures() * getYSpacing();
    //}

    return this.height;
  }

}