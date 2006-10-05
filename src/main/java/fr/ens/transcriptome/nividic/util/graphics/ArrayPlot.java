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

package fr.ens.transcriptome.nividic.util.graphics;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import org.apache.commons.collections.primitives.ArrayIntList;

import fr.ens.transcriptome.nividic.om.ArrayBlock;
import fr.ens.transcriptome.nividic.om.BioAssay;
import fr.ens.transcriptome.nividic.om.Spot;
import fr.ens.transcriptome.nividic.om.SpotIterator;

/**
 * This define a class used to plot ArrayDesign
 * @author Laurent Jourdren
 */
public class ArrayPlot {

  /** Column name for red data. */
  public static final String FIELD_NAME_RGB_COLOR = "spotRGBColor";

  private static final int DEFAULT_FEATURE_DIAMETER = 110;
  private static final int DEFAULT_X_SPACING = 400;
  private static final int DEFAULT_Y_SPACING = 400;

  private BufferedImage image;
  private int xSpottingOrigin;
  private int ySpottingOrigin;
  private int width;
  private int height;
  private double xFactor;
  private double yFactor;
  private BioAssay gal;
  private int margin;
  private String colorField = FIELD_NAME_RGB_COLOR;
  private Color defaultColor = Color.GRAY;

  private ArrayBlock[] blocks;

  //
  // Getters
  //

  /**
   * Get the blocks
   * @return Returns the blocks
   */
  public ArrayBlock[] getBlocks() {
    return blocks;
  }

  /**
   * Get the height of the image
   * @return Returns the height
   */
  public int getHeight() {
    return height;
  }

  /**
   * Get the image.
   * @return Returns the image
   */
  public BufferedImage getImage() {
    return image;
  }

  /**
   * Get the width.
   * @return Returns the width
   */
  public int getWidth() {
    return width;
  }

  /**
   * Get the xFactor.
   * @return Returns the xFactor
   */
  public double getXFactor() {
    return xFactor;
  }

  /**
   * Set the y factor.
   * @return Returns the yfactor
   */
  public double getYFactor() {
    return yFactor;
  }

  /**
   * Set the x origin of the spotting
   * @return Returns the xSpottingOrigin
   */
  public int getXSpottingOrigin() {
    return xSpottingOrigin;
  }

  /**
   * Get the y origin of the spotting
   * @return Returns the ySpottingOrigin
   */
  public int getYSpottingOrigin() {
    return ySpottingOrigin;
  }

  /**
   * Get the original Gal.
   * @return Returns the gal
   */
  public BioAssay getGal() {
    return gal;
  }

  /**
   * Get the margin size in pixels.
   * @return Returns the margin size
   */
  public int getMargin() {
    return margin;
  }

  /**
   * Get the name of the color field
   * @return The name of the color field
   */
  String getColorField() {
    return colorField;
  }

  /**
   * Get the defaultColor.
   * @return The default color
   */
  public Color getDefaultColor() {
    return defaultColor;
  }

  //
  // Setters
  //

  /**
   * Set the blocks.
   * @param blocks The blocks to set
   */
  public void setBlocks(final ArrayBlock[] blocks) {
    this.blocks = blocks;
  }

  /**
   * Set the height.
   * @param height The height to set
   */
  private void setHeight(final int height) {
    this.height = height;
  }

  /**
   * Set the image
   * @param image The image to set
   */
  private void setImage(final BufferedImage image) {
    this.image = image;
  }

  /**
   * Set the width of the image
   * @param width The width to set
   */
  public void setWidth(final int width) {
    this.width = width;
  }

  /**
   * Set the xFactor.
   * @param xFactor The xFactor to set
   */
  public void setXFactor(final double xFactor) {
    this.xFactor = xFactor;
  }

  /**
   * Set the y factor.
   * @param yFactor The yfactor to set
   */
  public void setYFactor(final double yFactor) {
    this.yFactor = yFactor;
  }

  /**
   * Set the x origin of the spotting
   * @param origin The xSpottingOrigin to set
   */
  public void setXSpottingOrigin(final int origin) {
    xSpottingOrigin = origin;
  }

  /**
   * Set the y origin of spotting
   * @param origin The ySpottingOrigin to set
   */
  public void setYSpottingOrigin(final int origin) {
    ySpottingOrigin = origin;
  }

  /**
   * Get the original Gal.
   * @param gal The gal to set
   */
  public void setGal(final BioAssay gal) {
    this.gal = gal;
  }

  /**
   * Set the margin size in pixel.
   * @param margin The margin to set
   */
  public void setMargin(final int margin) {
    this.margin = margin;
  }

  /**
   * Set the name of the color field.
   * @param colorField The name of the color field to set
   */
  public void setColorField(final String colorField) {
    this.colorField = colorField;
  }

  /**
   * Set the default color.
   * @param defaultColor Default color to set
   */
  public void setDefaultColor(final Color defaultColor) {
    this.defaultColor = defaultColor;
  }

  //
  // Other methods
  //

  private void init() {

    setHeight((int) searchFactors(getWidth(), getHeight()));

    setImage(new BufferedImage(getWidth(), getHeight(),
        BufferedImage.TYPE_USHORT_555_RGB));

    Graphics2D g = this.image.createGraphics();

    // Enable antialiasing
    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
        RenderingHints.VALUE_ANTIALIAS_ON);
    g.setBackground(Color.BLACK);
    g.clearRect(0, 0, getWidth(), getHeight());
    g.setColor(Color.WHITE);

    for (int i = 0; i < this.blocks.length; i++) {

      ArrayBlockDrawer abd = new ArrayBlockDrawer(this.blocks[i], g,
          getXFactor(), getYFactor(), getXSpottingOrigin(),
          getYSpottingOrigin(), getMargin(), getGal(), getColorField(),
          getDefaultColor());
      abd.draw();

    }

  }

  private void searchFactorsWithoutHeaders() {

    if (this.blocks.length == 0 || this.blocks[0] == null
        || this.blocks[0].getFeatureDiameter() != 0)
      return;

    for (int i = 0; i < this.blocks.length; i++) {

      this.blocks[i].setFeatureDiameter(DEFAULT_FEATURE_DIAMETER);
      this.blocks[i].setXSpacing(DEFAULT_X_SPACING);
      this.blocks[i].setYSpacing(DEFAULT_Y_SPACING);

    }

    ArrayList allBlocks = new ArrayList();

    for (int i = 0; i < this.blocks.length; i++) {

      final int mr = this.blocks[i].getMetaRow();
      final int mc = this.blocks[i].getMetaColumn();

      ArrayList rowBlocks;

      if (allBlocks.size() < mr) {
        rowBlocks = new ArrayList();

        while (allBlocks.size() < mr)
          allBlocks.add("");

        allBlocks.set(mr - 1, rowBlocks);
      } else {
        rowBlocks = (ArrayList) allBlocks.get(mr - 1);
      }

      while (rowBlocks.size() < mc)
        rowBlocks.add("");
      rowBlocks.set(mc - 1, blocks[i]);
    }

    int originY = 0;

    for (int i = 0; i < allBlocks.size(); i++) {

      ArrayList rowBlocks = (ArrayList) allBlocks.get(i);

      int originX = 0;

      for (int j = 0; j < rowBlocks.size(); j++) {

        ArrayBlock ab = (ArrayBlock) rowBlocks.get(j);
        ab.setXOrigin(originX);
        ab.setYOrigin(originY);
        originX += ab.getWidth() + DEFAULT_X_SPACING;

      }

      ArrayBlock ab = (ArrayBlock) rowBlocks.get(0);

      originY += ab.getHeight() + DEFAULT_Y_SPACING;

    }

  }

  private double searchFactors(final int width, final int height) {

    int maxWidth = -1;
    int maxHeight = -1;
    int minXOrigin = Integer.MAX_VALUE;
    int minYOrigin = Integer.MAX_VALUE;
    // int newHeight = height;
    int margin = getMargin();

    searchFactorsWithoutHeaders();

    for (int i = 0; i < this.blocks.length; i++) {

      int maxX = this.blocks[i].getWidth() + this.blocks[i].getXOrigin();
      int maxY = this.blocks[i].getHeight() + this.blocks[i].getYOrigin();

      if (maxX > maxWidth)
        maxWidth = maxX;
      if (maxY > maxHeight)
        maxHeight = maxY;
      if (this.blocks[i].getXOrigin() < minXOrigin)
        minXOrigin = this.blocks[i].getXOrigin();
      if (this.blocks[i].getYOrigin() < minYOrigin)
        minYOrigin = this.blocks[i].getYOrigin();

    }

    /*if (minXOrigin == Integer.MAX_VALUE)
      minXOrigin = 0;
    if (minYOrigin == Integer.MAX_VALUE)
      minYOrigin = 0; */

    setXSpottingOrigin(minXOrigin);
    setYSpottingOrigin(minYOrigin);

    setXFactor((double) (width - margin * 2 - 1)
        / (double) (maxWidth - minXOrigin));

    if (height == -1) {
      setYFactor(getXFactor());
      return getXFactor() * (maxHeight - minYOrigin - margin * 2) + margin * 2
          + 1;
    }

    setYFactor((double) (height - margin * 2 - 1)
        / (double) (maxHeight - minYOrigin));
    return height;
  }

  //
  // constructor
  //

  /**
   * Public constructor.
   * @param blocks Blocks to set
   * @param width Width to set
   * @param height Height to set
   * @param margin the value of the margin
   * @param gal the bioAssay to draw
   */
  public ArrayPlot(final ArrayBlock[] blocks, final int width,
      final int height, final int margin, final BioAssay gal) {
    this(blocks, width, height, margin, gal, null);
  }

  /**
   * Public constructor.
   * @param blocks Blocks to set
   * @param width Width to set
   * @param height Height to set
   * @param margin the value of the margin
   * @param gal the bioAssay to draw
   * @param colorField the field name for the color of the spots
   */
  public ArrayPlot(final ArrayBlock[] blocks, final int width,
      final int height, final int margin, final BioAssay gal,
      final String colorField) {
    setBlocks(blocks);
    setWidth(width);
    setHeight(height);
    setMargin(margin);
    setGal(gal);
    if (colorField == null)
      setColorField(FIELD_NAME_RGB_COLOR);
    init();
  }
}