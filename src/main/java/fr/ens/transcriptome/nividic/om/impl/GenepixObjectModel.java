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
import java.io.Serializable;

import netscape.javascript.JSObject;
import fr.ens.transcriptome.nividic.om.io.NividicIOException;

/**
 * This class is the binding between Nividic and the Genepix Object Model. To be
 * fonctionnal, the html page containning the call to the Java applet must
 * provides Javascript functions who really calls Genepix Object Models. This
 * class use javascript handling facilities of the Netscape.jar API. The
 * Netscape library is need for compiling this class but it seems to be already
 * shipped in Sun JRE plugin for Internet Explorer.
 * @author Laurent Jourdren TODO this class must be only visible for class of
 *               this package.
 */
public class GenepixObjectModel implements Serializable {

  /** serial version for serialization. */
  static final long serialVersionUID = -6474109031084968849L;

  private JSObject window;
  private int height;
  private int width;
  private String[] columnNames;

  //
  // Getters
  //

  /**
   * Get the applet object
   * @return The applet object
   */
  /*
   * private Applet getApplet() { return applet; }
   */

  /**
   * Get The length of the array of Genepix results
   * @return The length of the array of Genepix results
   */
  public int getHeight() {
    return height;
  }

  /**
   * Get The length of the array of Genepix results
   * @return The number of the fields
   */
  public int getWidth() {
    return this.width;
  }

  private JSObject getWindow() {
    return this.window;
  }

  //
  // Setters
  //

  /**
   * Set the applet Object
   * @param applet The applet
   */
  private void setApplet(final Applet applet) {
    this.window = JSObject.getWindow(applet);
  }

  /**
   * Set The height of the array of Genepix results
   * @param height The height of the array of Genepix results
   */
  private void setHeight(final int height) {
    this.height = height;
  }

  /**
   * Set The width of the array of Genepix results
   * @param width The height of the array of Genepix results
   */
  private void setWidth(final int width) {
    this.width = width;
  }

  //
  // Genepix bindings
  //

  /**
   * Get Returns the value at a given row and column
   * @param columnName The columnName
   * @param row The row
   * @return The value
   */
  private String readGenepixResultValueAt(final String columnName, final int row) {

    Object[] args = new Object[2];
    args[0] = columnName;
    args[1] = new Integer(row);

    Object result = getWindow().call("getValueAt", args);

    if (result == null)
      return null;

    return "" + result;
  }

  /**
   * Get the name of a column
   * @param column The column
   * @return The name of the column
   */
  private String readGenepixColumnName(final int column) {

    Object[] args = new Object[1];
    args[0] = new Integer(column);

    Object result = getWindow().call("getColumnName", args);

    if (result == null)
      return null;

    return "" + result;
  }

  /**
   * Get the integer value of a javascript variable
   * @param variable The variable
   * @return the int value of the javascript variable
   */
  private int readGenepixVariable(final String variable) {

    Object lavar = getWindow().getMember(variable);

    return Integer.parseInt("" + lavar);
  }

  /**
   * Get the headers of the data. The data is in GPR header format.
   * @return A string with the header.
   */
  public String getHeaders() {

    return "" + getWindow().call("getHeaders", new Object[0]);
  }

  //
  // Other methods
  //

  /**
   * Retrieve the dimension and the columns names of the genepix data result
   */
  private void setDimensions() {

    setHeight(readGenepixVariable("height"));
    setWidth(readGenepixVariable("width"));

    this.columnNames = new String[getWidth()];

    for (int i = 0; i < getWidth(); i++)
      this.columnNames[i] = readGenepixColumnName(i);

  }

  /**
   * Export a string column of the Genepix array result
   * @param column The column to export
   * @return The result column
   */
  public String[] exportToString(final String column) {

    if (!isColumn(column))
      return null;

    int len = getHeight();
    String[] result = new String[len];

    for (int i = 0; i < len; i++)
      result[i] = readGenepixResultValueAt(column, i);

    return result;
  }

  /**
   * Export an integer column of the Genepix array result
   * @param column The column to export
   * @return The result column
   */
  public int[] exportToInt(final String column) {

    String[] s = exportToString(column);
    if (s == null)
      return null;

    int[] result = new int[this.height];

    for (int i = 0; i < s.length; i++)
      result[i] = Integer.parseInt(s[i]);

    return result;
  }

  /**
   * Export a float column of the Genepix array result
   * @param column The column to export
   * @return The result column
   */
  public double[] exportToDouble(final String column) {

    String[] s = exportToString(column);
    if (s == null)
      return null;

    double[] result = new double[this.height];

    for (int i = 0; i < s.length; i++)
      result[i] = Double.parseDouble(s[i]);

    return result;
  }

  /**
   * Returns the field names of the array
   * @return The field names of the array
   */
  public String[] getColumnNames() {

    return this.columnNames;
  }

  /**
   * Returns the index of a column
   * @param columnName column to get index
   * @return The index of a column
   */
  public int getNumberColumn(final String columnName) {
    final String[] array = getColumnNames();
    if (columnName == null || array == null || array.length == 0)
      return -1;
    for (int i = 0; i < array.length; i++)
      if (columnName.equals(array[i]))
        return i;
    return -1;
  }

  /**
   * Test if a column exists
   * @param column Column to test
   * @return true if the column exits
   */
  public boolean isColumn(final String column) {

    if (column == null || this.columnNames == null)
      return false;

    for (int i = 0; i < this.columnNames.length; i++)
      if (column.equals(this.columnNames[i]))
        return true;

    return false;
  }

  //
  // constructors
  //
  private GenepixObjectModel() {
  }

  /**
   * Public constructor.
   * @param applet Applet use to find the data
   * @throws NividicIOException if applet is null
   */
  public GenepixObjectModel(final Applet applet) throws NividicIOException {

    if (applet == null)
      throw new NividicIOException("Applet is null");
    setApplet(applet);
    setDimensions();
  }

}