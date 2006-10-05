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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

import fr.ens.transcriptome.nividic.om.ArrayBlock;
import fr.ens.transcriptome.nividic.om.BioAssay;
import fr.ens.transcriptome.nividic.om.BioAssayRuntimeException;
import fr.ens.transcriptome.nividic.om.BioAssayUtils;
import fr.ens.transcriptome.nividic.om.Spot;

/**
 * this class implements
 * @author Laurent Jourdren
 */
public class ArrayBlockDrawer {

  private Graphics2D g;
  private ArrayBlock block;
  private double xFactor;
  private double yFactor;
  private double xSpottingOrigin;
  private double ySpottingOrigin;
  private double margin;
  private BioAssay gal;
  private String colorField;
  private Color defaultColor = Color.GRAY;

  //
  // Getters
  //

  /**
   * Set the block.
   * @return Returns the block
   */
  public ArrayBlock getBlock() {
    return block;
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
  public double getXSpottingOrigin() {
    return xSpottingOrigin;
  }

  /**
   * Get the y origin of the spotting
   * @return Returns the ySpottingOrigin
   */
  public double getYSpottingOrigin() {
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
   * Get the margin in pixels
   * @return Returns the margin
   */
  public double getMargin() {
    return margin;
  }

  /**
   * Get the color field.
   * @return The color field name
   */
  public String getColorField() {
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
   * @param block The block to set
   */
  public void setBlock(final ArrayBlock block) {
    this.block = block;
  }

  /**
   * Set the graphics interface
   * @param g The g to set
   */
  public void setG(final Graphics2D g) {
    this.g = g;
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
  public void setYfactor(final double yFactor) {
    this.yFactor = yFactor;
  }

  /**
   * Set the x origin of the spotting
   * @param origin The xSpottingOrigin to set
   */
  public void setXSpottingOrigin(final double origin) {
    xSpottingOrigin = origin;
  }

  /**
   * Set the y origin of spotting
   * @param origin The ySpottingOrigin to set
   */
  public void setYSpottingOrigin(final double origin) {
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
   * Set the margin in pixel.
   * @param margin The margin to set
   */
  public void setMargin(final double margin) {
    this.margin = margin;
  }

  /**
   * Set the color field name
   * @param colorField The name of the field to set
   */
  void setColorField(final String colorField) {
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

  /**
   * Draw the block
   */
  public void draw() {

    final ArrayBlock b = getBlock();
    final double xf = getXFactor();
    final double yf = getYFactor();

    final double xDrawOrigin = getMargin()
        + (b.getXOrigin() - getXSpottingOrigin()) * xf;
    final double yDrawOrigin = getMargin()
        + (b.getYOrigin() - getYSpottingOrigin()) * yf;

    Shape square = new Rectangle2D.Double(xDrawOrigin, yDrawOrigin, b
        .getWidth()
        * xf, b.getHeight() * yf);

    g.draw(square);

    // g.drawRect(xDrawOrigin, yDrawOrigin, (int) (b.getWidth() * xf), (int)
    // (b.getHeight() * yf));

    drawSpots(xDrawOrigin, yDrawOrigin);

  }

  private void drawSpots(final double xDrawOrigin, final double yDrawOrigin) {

    final int xSize = getBlock().getXFeatures();
    final int ySize = getBlock().getYFeatures();
    final int xSpacing = getBlock().getXSpacing();
    final int ySpacing = getBlock().getYSpacing();
    final double xf = getXFactor();
    final double yf = getYFactor();
    final double xDia = getBlock().getFeatureDiameter() * xf;
    final double yDia = getBlock().getFeatureDiameter() * yf;
    // final double margin = getMargin();

    Color c = g.getColor();
    Stroke st = g.getStroke();
    final String colorField = getColorField();

    // g.setStroke(new BasicStroke(1.0f,
    // BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND, 1.0f, new float[] {16.0f,
    // 20.0f}, 0.0f));

    final ArrayBlock block = getBlock();
    final int metaRow;
    final int metaColumn;

    if (getGal().getSpot(0).getMetaRow() == 0) {
      metaRow = 0;
      metaColumn = block.getId();
    } else {
      metaColumn = block.getMetaColumn();
      metaRow = block.getMetaRow();
    }

    for (int i = 0; i < ySize; i++) {

      double y = yDrawOrigin + i * ySpacing * yf + yDia / 2;

      for (int j = 0; j < xSize; j++) {

        double x = xDrawOrigin + j * xSpacing * xf + xDia / 2;

        Shape spot = new Ellipse2D.Double(x, y, xDia, yDia);

        final int location = BioAssayUtils.encodeLocation(metaRow, metaColumn,
            i + 1, j + 1);

        Spot spotBefore = getGal().getSpotLocation(location);

        Color newColor;

        if (colorField != null) {

          try {
            int rgbColor = spotBefore.getDataFieldInt(colorField);
            newColor = new Color(rgbColor);
          } catch (BioAssayRuntimeException e) {
            newColor = getDefaultColor();
          }

          // Spot spotAfter = getGalAfter().getSpotLocation(location);
        } else
          newColor = getDefaultColor();

        if (spotBefore == null || spotBefore.getIndex() == -1) {

          g.setColor(Color.LIGHT_GRAY);
          g.draw(spot);

        } else if (spotBefore.isEmpty()) {

          g.setColor(Color.LIGHT_GRAY);
          g.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_ROUND,
              BasicStroke.JOIN_ROUND, 1.0f, new float[] {1.0f, 1.0f}, 0.0f));
          g.draw(spot);

        } else {

          g.setColor(newColor);
          g.fill(spot);
        }

      }
    }
    g.setColor(c);
    g.setStroke(st);

  } // // Constructor //

  /**
   * Public constructor.
   * @param block Block to draw
   * @param g Graphics Interface
   * @param xFactor Zoom xFactor.
   * @param yFactor Zoom yFactor.
   * @param xSpottingOrigin X spotting origin
   * @param ySpottingOrigin Y spotting origin
   * @param margin the margin
   * @param gal Gal before
   * @param colorField The name of field for the color
   * @param defaultColor the default color of the field
   */
  public ArrayBlockDrawer(final ArrayBlock block, final Graphics2D g,
      final double xFactor, final double yFactor, final int xSpottingOrigin,
      final int ySpottingOrigin, final double margin, final BioAssay gal,
      final String colorField, final Color defaultColor) {

    setBlock(block);
    setG(g);
    setXFactor(xFactor);
    setYfactor(yFactor);
    setXSpottingOrigin(xSpottingOrigin);
    setYSpottingOrigin(ySpottingOrigin);
    setMargin(margin);
    setGal(gal);
    setColorField(colorField);
    setDefaultColor(defaultColor);
  }

}