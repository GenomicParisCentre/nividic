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
 * This interface defines methods that are share by ExpressionMatrix and
 * ExpressionMatrixDimension.
 * @author Laurent Jourdren
 */
public interface ExpressionMatrixSizes {

  /**
   * gives the second dimention of the matrix, the number of columns
   * @return an int , the number of columns of the matrix
   */
  int getColumnCount();

  /**
   * get the index of a column if it exists
   * @param columnName the name of the column which you want the index
   * @return the index of the column
   */
  int getColumnIndex(String columnName);

  /**
   * get the name of a column if it exists
   * @param columnNumber the index of the column which you want the name
   * @return the name of the column
   */
  String getColumnName(int columnNumber);

  /**
   * gives the first dimention of the matrix, the number of rows
   * @return an int , the number of rows of the matrix
   */
  int getRowCount();

  /**
   * Get the names of the columns
   * @return return the names of the columns in an array of strings
   */
  String[] getColumnNames();

  /**
   * Get the identifiers of the rows
   * @return return the names of the rows in an array of strings
   */
  String[] getRowIds();

  /**
   * Checks is the ExpressionMatrixDimension is empty or not
   * @return return true if the matrix is empty, false else
   */
  boolean isNoRow();

  /**
   * Allows us to know if the ExpressionMatrixDimension contains a given column
   * @param columnNumber the index of the column in reference ArrayList
   * @return true if this column exists, false if not
   */
  boolean containsColumn(int columnNumber);

  /**
   * Allows us to know if the ExpressionMatrixDimension contains a given column
   * @param columnName the column name that we want to check
   * @return true if the column exists, false if not
   */
  boolean containsColumn(String columnName);

  /**
   * Allows us to know if the ExpressionMatrixDimension contains a given rowId
   * @param rowId the row name that we want to check
   * @return true if the rowId exists, false if not
   */
  boolean containsRowId(String rowId);

  /**
   * Test if the data inside the object is the same data as another object.
   * @param o Object to test
   * @return true if the 2 objects are equals
   */
  boolean dataEquals(Object o);

}
