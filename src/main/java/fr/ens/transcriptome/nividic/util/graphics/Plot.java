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
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import fr.ens.transcriptome.nividic.util.MathUtils;

/**
 * Draw a plot.
 * @author Laurent Jourdren
 */
public class Plot {

  private int width;
  private int height;
  private int xPlotZone;
  private int yPlotZone;
  private int widthPlotZone;
  private int heightPlotZone;
  private Graphics2D graphics;
  private BufferedImage image;
  private Data data;
  private double minX = Double.NaN;
  private double maxX = Double.NaN;
  private double minY = Double.NaN;
  private double maxY = Double.NaN;
  private double xRange;
  private double yRange;
  private double histRange;
  private boolean initDone;
  private boolean logMode;
  private String title;
  private String xAxisUnit;
  private String yAxisUnit;
  private String xPlotDescription;
  private boolean legendXPlot;
  private double xFactor;
  private double yFactor;

  // private static final double MARGIN_FACTOR = 0.12;
  private static final double X_MARGIN_FACTOR = 0.05;
  private static final double Y_MARGIN_FACTOR = 0.12;

  private static final Font FONT_AXIS = new Font("Lucinda Sans Unicode",
      Font.PLAIN, 9);
  private static final Font FONT_TITLE = new Font("Lucinda BRIGHT Unicode",
      Font.ITALIC, 14);

  //
  // Getters
  //

  /**
   * Get the height of the plot.
   * @return Returns the height
   */
  public int getHeight() {
    return height;
  }

  /**
   * Get the heigth of the plot zone.
   * @return Returns the heigthPlotZone
   */
  private int getHeightPlotZone() {
    return heightPlotZone;
  }

  /**
   * Get the width of the plot.
   * @return Returns the width
   */
  public int getWidth() {
    return width;
  }

  /**
   * Get the width of the plot zone
   * @return Returns the widthPlotZone
   */
  private int getWidthPlotZone() {
    return widthPlotZone;
  }

  /**
   * Get the x coordinate of the origin of the plot zone.
   * @return Returns the xPlotZone
   */
  private int getXPlotZone() {
    return xPlotZone;
  }

  /**
   * Get the y coordinate of the origin of the plot zone.
   * @return Returns the yPlotZone
   */
  private int getYPlotZone() {
    return yPlotZone;
  }

  /**
   * Get the graphics.
   * @return the graphics.
   */
  private Graphics2D getGraphics() {
    return graphics;
  }

  /**
   * Get plot data.
   * @return Plot data
   */
  public Data getData() {
    return data;
  }

  /**
   * Get the maximun display value of X.
   * @return Returns the maxX
   */
  public double getMaxX() {
    return maxX;
  }

  /**
   * Get the maximun display value of Y.
   * @return Returns the maxY
   */
  public double getMaxY() {
    return maxY;
  }

  /**
   * Get the minimum display value of X.
   * @return Returns the minX
   */
  public double getMinX() {
    return minX;
  }

  /**
   * Get the minimum display value of Y.
   * @return Returns the minY
   */
  public double getMinY() {
    return minY;
  }

  /**
   * Get the X axis range.
   * @return Returns the rangeX
   */
  public double getXRange() {
    return xRange;
  }

  /**
   * Get the Y axis range.
   * @return Returns the rangeY
   */
  public double getYRange() {
    return yRange;
  }

  /**
   * Get the hist range.
   * @return Returns the histRange
   */
  public double getHistRange() {
    return histRange;
  }

  /**
   * Test if is in log mode.
   * @return Returns the logMode
   */
  public boolean isLogMode() {
    return logMode;
  }

  /**
   * Get the x axis unit.
   * @return Returns the xUnit
   */
  public String getXAxisUnit() {
    return xAxisUnit;
  }

  /**
   * Get the y axis unit.
   * @return Returns the yUnit
   */
  public String getYAxisUnit() {
    return yAxisUnit;
  }

  /**
   * Get the description of the X plots
   * @return Returns the xPlotDescription
   */
  public String getXPlotDescription() {
    return xPlotDescription;
  }

  /**
   * Get the xFactor
   * @return Returns the xFactor
   */
  private double getXFactor() {
    return xFactor;
  }

  /**
   * Get the y factor
   * @return Returns the yFactor
   */
  private double getYFactor() {
    return yFactor;
  }

  //
  // Setters
  //

  /**
   * Set the height of the plot
   * @param height The height to set
   */
  public void setHeight(final int height) {
    this.height = height;
  }

  /**
   * Set the heigth of the plot zone.
   * @param heightPlotZone The heigthPlotZone to set
   */
  private void setHeightPlotZone(final int heightPlotZone) {
    this.heightPlotZone = heightPlotZone;
  }

  /**
   * Set the width of the plot.
   * @param width The width to set
   */
  public void setWidth(final int width) {
    this.width = width;
  }

  /**
   * Get the width of the plot zone.
   * @param widthPlotZone The widthPlotZone to set
   */
  private void setWidthPlotZone(final int widthPlotZone) {
    this.widthPlotZone = widthPlotZone;
  }

  /**
   * Set the x coordinate of the origin of the plot zone.
   * @param plotZone The xPlotZone to set
   */
  private void setXPlotZone(final int plotZone) {
    xPlotZone = plotZone;
  }

  /**
   * Set the y coordinate of the plot zone.
   * @param plotZone The yPlotZone to set
   */
  private void setYPlotZone(final int plotZone) {
    yPlotZone = plotZone;
  }

  /**
   * Set the graphics.
   * @param graphics The graphics to set
   */
  public void setGraphics(final Graphics2D graphics) {
    this.graphics = graphics;
  }

  /**
   * Set plot data.
   * @param data Plot data
   */
  public void setData(final Data data) {
    this.data = data;
  }

  /**
   * Set the maximun display value of X.
   * @param maxX The maxX to set
   */
  public void setMaxX(final double maxX) {
    this.maxX = maxX;
  }

  /**
   * Get the maximun display value of Y.
   * @param maxY The maxY to set
   */
  public void setMaxY(final double maxY) {
    this.maxY = maxY;
  }

  /**
   * Set the minimum display value of X.
   * @param minX The minX to set
   */
  public void setMinX(final double minX) {
    this.minX = minX;
  }

  /**
   * Set the minimum display value of X.
   * @param minY The minY to set
   */
  public void setMinY(final double minY) {
    this.minY = minY;
  }

  /**
   * Set the X axis range.
   * @param rangeX The rangeX to set
   */
  public void setXRange(final double rangeX) {
    this.xRange = rangeX;
  }

  /**
   * Set the Y axis range.
   * @param rangeY The rangeY to set
   */
  public void setYRange(final double rangeY) {
    this.yRange = rangeY;
  }

  /**
   * Set the hist range.
   * @param histRange The histRange to set
   */
  public void setHistRange(final double histRange) {
    this.histRange = histRange;
  }

  /**
   * Set the log mode.
   * @param logMode The logMode to set
   */
  public void setLogMode(final boolean logMode) {
    this.logMode = logMode;
  }

  /**
   * Set the x axis unit.
   * @param unit The xUnit to set
   */
  public void setXAxisUnit(final String unit) {
    xAxisUnit = unit;
  }

  /**
   * Set the y axis unit
   * @param unit The yUnit to set
   */
  public void setYAxisUnit(final String unit) {
    yAxisUnit = unit;
  }

  /**
   * Set the description of the X plots
   * @param plotDescription The xPlotDescription to set
   */
  public void setXPlotDescription(final String plotDescription) {
    xPlotDescription = plotDescription;
  }

  /**
   * Set the x factor
   * @param factor The xFactor to set
   */
  public void setXFactor(double factor) {
    xFactor = factor;
  }

  /**
   * Set the y factor
   * @param factor The yFactor to set
   */
  public void setYFactor(double factor) {
    yFactor = factor;
  }

  //
  // Other methods
  //

  private void computePlotZone() {

    final Graphics2D g = getGraphics();
    FontRenderContext frc = g.getFontRenderContext();
    g.setFont(FONT_AXIS);
    Font f = g.getFont();

    int sizeXLeftUnit = 0;
    int sizeXRightUnit = 0;

    if (getXAxisUnit() != null) {

      Rectangle2D bounds1 = f.getStringBounds(getXAxisUnit(), frc);
      Rectangle2D bounds2 = f.getStringBounds(
          getXPlotDescription() == null ? "" : getXPlotDescription(), frc);

      if (bounds1.getWidth() > (bounds2.getWidth() + 20))
        sizeXRightUnit = (int) bounds1.getWidth();
      else
        sizeXRightUnit = (int) bounds2.getWidth() + 20;

    }
    if (getYAxisUnit() != null) {
      Rectangle2D bounds = f.getStringBounds(getYAxisUnit(), frc);
      sizeXLeftUnit = (int) bounds.getWidth();
    }

    int xLeftMargin = (int) (X_MARGIN_FACTOR * getWidth()) + sizeXLeftUnit;
    int xRightMargin = (int) (X_MARGIN_FACTOR * getWidth()) + sizeXRightUnit;
    int yMargin = (int) (Y_MARGIN_FACTOR * getHeight());

    // setXPlotZone(xMargin);
    setXPlotZone(xLeftMargin);
    setYPlotZone(yMargin);
    setWidthPlotZone(getWidth() - (xLeftMargin + xRightMargin));
    setHeightPlotZone(getHeight() - yMargin * 2);
  }

  private void drawXPlotsDescription() {

    if (this.legendXPlot == true)
      return;
    this.legendXPlot = true;

    if (getXPlotDescription() == null)
      return;

    double y = getHeight() / (double) 2;
    double x = (getXPlotZone() + getWidthPlotZone()) * 1.05;

    final Graphics2D g = getGraphics();
    FontRenderContext frc = g.getFontRenderContext();
    g.setFont(FONT_AXIS);
    Font f = g.getFont();

    g.setColor(Color.RED);
    g.drawLine((int) x, (int) y, (int) x + 10, (int) y);
    g.setColor(Color.BLACK);

    Rectangle2D bounds = f.getStringBounds(getXPlotDescription(), frc);

    g.drawString(getXPlotDescription(), (int) x + 15, (int) (y + bounds
        .getHeight() / 3));
    // bounds .getMaxY()
  }

  private void drawXAxis(final double min, final double max, final double range) {

    final Graphics2D g = getGraphics();

    // Axis line
    g.drawLine(getXPlotZone() - 1, getYPlotZone() + getHeightPlotZone() + 1,
        getXPlotZone() + getWidthPlotZone() + 7, getYPlotZone()
            + getHeightPlotZone() + 1);

    // Arrow
    g.drawLine(getXPlotZone() + getWidthPlotZone() + 5, getYPlotZone()
        + getHeightPlotZone() - 1, getXPlotZone() + getWidthPlotZone() + 7,
        getYPlotZone() + getHeightPlotZone() + 1);

    g.drawLine(getXPlotZone() + getWidthPlotZone() + 5, getYPlotZone()
        + getHeightPlotZone() + 3, getXPlotZone() + getWidthPlotZone() + 7,
        getYPlotZone() + getHeightPlotZone() + 1);

    // int windows = (int) ((max - min) / range) + 1;
    // int step = (int) (getWidthPlotZone() / windows);

    g.setFont(FONT_AXIS);

    // double xFactor = getWidthPlotZone() / (max - min);

    for (double d = min; d <= max; d += range) {

      int xpos = getXPlotZone() + (int) (d * getXFactor());

      g.drawLine(xpos, getYPlotZone() + getHeightPlotZone() + 1, xpos,
          getYPlotZone() + getHeightPlotZone() + 5);
      g.drawString("" + (int) (d), xpos, getYPlotZone() + getHeightPlotZone()
          + 20);
    }

    // X Axis unit
    if (getXAxisUnit() != null)
      g.drawString(getXAxisUnit(), getXPlotZone() + getWidthPlotZone() + 15,
          getYPlotZone() + getHeightPlotZone() + 20);

  }

  private void drawYAxis(double min, final double max, final double range) {

    final Graphics2D g = getGraphics();

    // Axis line
    g.drawLine(getXPlotZone() - 1, getYPlotZone() + getHeightPlotZone() + 1,
        getXPlotZone() - 1, getYPlotZone() - 5);

    // Arrow
    g.drawLine(getXPlotZone() - 3, getYPlotZone() - 5, getXPlotZone() - 1,
        getYPlotZone() - 7);
    g.drawLine(getXPlotZone() + 1, getYPlotZone() - 5, getXPlotZone() - 1,
        getYPlotZone() - 7);

    int windows;

    if (isLogMode())
      windows = (int) (MathUtils.log10(max - min));
    else
      windows = (int) ((max - min) / range) + 1;

    // int step = (int) (getHeightPlotZone() / windows);

    g.setFont(FONT_AXIS);
    Font f = g.getFont();
    FontRenderContext frc = g.getFontRenderContext();

    double yFactor;
    if (isLogMode())
      yFactor = ((double) getHeightPlotZone())
          / ((double) MathUtils.log10(max));
    else
      yFactor = ((double) getHeightPlotZone()) / ((double) max);

    if (isLogMode() && min < 1)
      min = 1;
    for (double d = min; d <= max;) {

      int ypos;
      String message = "" + (int) d;
      int y;
      if (isLogMode()) {
        y = (int) (MathUtils.log10(d) * yFactor);
        d = 10 * d;
      } else {
        y = (int) (d * yFactor);
        d += range;
      }

      ypos = getYPlotZone() + getHeightPlotZone() - y;

      g.drawLine(getXPlotZone() - 5, ypos, getXPlotZone() - 1, ypos);
      Rectangle2D bounds = f.getStringBounds(message, frc);
      g.drawString(message, (int) (getXPlotZone() - bounds.getWidth() - 8),
          ypos);
    }

    if (getYAxisUnit() != null) {
      final String unit = getYAxisUnit();
      Rectangle2D bounds = f.getStringBounds(unit, frc);

      g.drawString(unit, (int) (getXPlotZone() - bounds.getWidth() - 8),
          getYPlotZone() - 10);
    }

  }

  private void searchFactors(final boolean hist) {

    if (Double.isNaN(getMinX()))
      // minX = getData().getMinX();
      setMinX(0);

    else
      setMinX(getMinX());

    if (Double.isNaN(getMaxX())) {
      double maxX = getData().getMaxX();

      if (hist) {
        // The last column must be on the axis
        int n = (int) (maxX / getHistRange());
        maxX = (n + 1) * getHistRange();
      }
      setMaxX(maxX);
    }

    else
      setMaxX(getMaxX());

    setXFactor(getWidthPlotZone() / (getMaxX() - getMinX()));
  }

  public void plotXLimit(double limit) {

    if (data != null)
      if (getData() == null || getData().size() == 0)
        return;

    // double minX;
    // double maxX;
    /*
     * double range = getHistRange(); if (Double.isNaN(getMinX())) //minX =
     * getData().getMinX(); minX = 0; else minX = getMinX(); if
     * (Double.isNaN(getMaxX())) maxX = getData().getMaxX(); else maxX =
     * getMaxX();
     */

    int x = (int) ((limit - getMinX()) * getXFactor());

    final Graphics2D g = getGraphics();
    g.setColor(Color.RED);
    g.drawLine(getXPlotZone() + x, getYPlotZone() + getHeightPlotZone() + 1,
        getXPlotZone() + x, getYPlotZone() - 5);

    g.setColor(Color.BLACK);

    drawXPlotsDescription();
  }

  public void plotData() {
    if (!this.initDone)
      init();

    searchFactors(false);

    if (getData() == null || getData().size() == 0)
      return;

    double minX;
    double minY;
    double maxX;
    double maxY;

    if (Double.isNaN(getMinX()))
      minX = getData().getMinX();

    else
      minX = getMinX();

    if (Double.isNaN(getMaxX()))
      maxX = getData().getMaxX();
    else
      maxX = getMaxX();

    if (Double.isNaN(getMinY()))
      minY = getData().getMinY();
    else
      minY = getMinY();

    if (Double.isNaN(getMaxY()))
      maxY = getData().getMaxY();
    else
      maxY = getMaxY();

    // double xFactor = getWidthPlotZone() / (maxX - minX);
    // double yFactor = getHeightPlotZone() / (maxY - minY);

    final int size = getData().size();
    final ArrayList alX = getData().getXs();
    final ArrayList alY = getData().getYs();
    final Graphics2D g = getGraphics();

    for (int i = 0; i < size; i++) {
      final double xValue = ((Double) alX.get(i)).doubleValue();
      final double yValue = ((Double) alY.get(i)).doubleValue();

      if (xValue < minX || yValue < minY || xValue > maxX || yValue > maxY)
        continue;

      int x = (int) ((xValue - minX) * getXFactor());
      int y = (int) ((yValue - minY) * getYFactor());
      g.drawOval(getXPlotZone() + x, getYPlotZone() + getHeightPlotZone() - y,
          0, 0);

    }
    drawXAxis(minX, maxX, getXRange());
    drawYAxis(minY, maxY, getYRange());

  }

  public void plotHistData() {

    if (!this.initDone)
      init();
    searchFactors(true);

    if (data != null)
      if (getData() == null || getData().size() == 0)
        return;

    // double minX;
    // double maxX;
    final double range = getHistRange();

    int windows = (int) ((getMaxX() - getMinX()) / range) + 1;

    int[] hist = new int[windows];

    final int size = getData().size();
    final ArrayList alX = getData().getXs();

    for (int i = 0; i < size; i++) {
      final double xValue = ((Double) alX.get(i)).doubleValue();
      if (xValue < getMinX() || xValue > getMaxX())
        continue;

      hist[(int) ((xValue - getMinX()) / range)]++;
    }

    final Graphics2D g = getGraphics();
    // double yFactor;

    if (isLogMode())
      setYFactor(((double) getHeightPlotZone())
          / ((double) MathUtils.log10(getMax(hist))));
    else
      setYFactor(((double) getHeightPlotZone()) / ((double) getMax(hist)));

    // double xFactor = getWidthPlotZone() / (getMaxX() - getMinX());

    boolean b = false;
    for (int i = 0; i < windows; i++) {

      int xStart = (int) ((i * getHistRange() - getMinX()) * getXFactor());
      int length = (int) ((((i + 1) * getHistRange() - getMinX()) * getXFactor()) - 1)
          - xStart;

      int y;
      if (isLogMode())
        y = (int) (MathUtils.log10(hist[i]) * getYFactor());
      else
        y = (int) (hist[i] * getYFactor());

      if (b) {
        g.setColor(Color.LIGHT_GRAY);
        b = false;
      } else {
        g.setColor(Color.GRAY);
        b = true;
      }

      g.fillRect(getXPlotZone() + xStart, getYPlotZone() + getHeightPlotZone()
          - y, length, y);

    }
    g.setColor(Color.BLACK);

    drawXAxis(getMinX(), getMaxX(), getXRange());
    drawYAxis(0, getMax(hist), getYRange());

  }

  private int getMax(int[] array) {

    if (array == null || array.length == 0)
      return 0;

    int result = Integer.MIN_VALUE;

    for (int i = 0; i < array.length; i++) {
      final int v = array[i];
      if (v > result)
        result = v;
    }
    return result;
  }

  public void setTitle(final String title) {
    this.title = title;
  }

  private void drawTitle(final String title) {

    final Graphics2D g = getGraphics();

    g.setFont(FONT_TITLE);

    Font f = g.getFont();
    FontRenderContext frc = g.getFontRenderContext();
    Rectangle2D bounds = f.getStringBounds(title, frc);

    g.drawString(title, (float) (getWidth() - bounds.getWidth()) / 2,
        (float) 20);

  }

  private void init() {

    this.image = new BufferedImage(getWidth(), getHeight(),
        BufferedImage.TYPE_INT_RGB);

    Graphics2D g = this.image.createGraphics();
    setGraphics(g);
    g.setBackground(Color.WHITE);
    g.clearRect(0, 0, getWidth(), getHeight());
    g.setColor(Color.BLACK);

    g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
    // g.drawRect(getXPlotZone(),getYPlotZone(),
    // getWidthPlotZone(),getHeightPlotZone());

    computePlotZone();

    drawTitle(this.title);
    this.initDone = true;
  }

  public Image getImage() {

    return this.image;
  }

  private void show() {
    JFrame frame = new JFrame();
    frame.show();
    frame.setSize(new Dimension(getWidth() + 50, getHeight() + 50));
    frame.getContentPane().add(new JLabel(new ImageIcon(getImage())));
    frame.show();
  }

  //
  // Constructors
  //

  public Plot() {
    // this(800, 600);
    // this(200,200);
  }

  public Plot(final int width, final int height) {
    setWidth(width);
    setHeight(height);

  }

  // Main

  public static void main(String[] args) {

    Random generator = new Random(System.currentTimeMillis());
    Data data = new Data();

    for (int i = 0; i < 500; i++)
      data.add(generator.nextInt(199), generator.nextInt(500));

    Plot p = new Plot();

    p.setTitle("Diameter distribution");
    p.setData(data);
    p.setWidth(400);
    p.setHeight(300);
    p.setXRange(20);
    p.setYRange(1000);
    p.setHistRange(10);
    p.setXAxisUnit("Pixels");
    p.setYAxisUnit("# Spots");
    p.setXPlotDescription("Threshold");
    p.plotHistData();
    p.plotXLimit(140);

    p.show();
  }

}