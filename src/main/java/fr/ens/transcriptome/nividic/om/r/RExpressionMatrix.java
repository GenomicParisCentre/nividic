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

package fr.ens.transcriptome.nividic.om.r;

import java.util.Vector;

import org.rosuda.JRclient.REXP;
import org.rosuda.JRclient.RSrvException;
import org.rosuda.JRclient.Rconnection;

import fr.ens.transcriptome.nividic.om.ExpressionMatrix;
import fr.ens.transcriptome.nividic.om.ExpressionMatrixDimension;
import fr.ens.transcriptome.nividic.om.ExpressionMatrixFactory;

/**
 * interface between ExpressionMatrixDimension and R
 * @author Lory Montout
 */
public class RExpressionMatrix implements RObject {

  private Rconnection connexion;
  private ExpressionMatrix matrix;

  //
  // Getters
  //

  /**
   * get the Rconnection
   * @return the Rconnection
   */
  public Rconnection getConnexion() {
    return connexion;
  }

  /**
   * get the expression matrix
   * @return an expression matrix
   */
  public ExpressionMatrix getMatrix() {
    return matrix;
  }

  //
  // Setters
  //

  /**
   * set an Rconnection
   * @param connexion the Rconnection to set in
   */
  public void setConnexion(final Rconnection connexion) {
    this.connexion = connexion;
  }

  /**
   * set an ExpressionMatrix
   * @param matrix the ExpressionMatrix to set in
   */
  public void setMatrix(final ExpressionMatrix matrix) {
    this.matrix = matrix;
  }

  //
  // Other methods
  //

  /**
   * put an ExpressionMatrixDimension in Rserve
   * @param rName the name of the matrix in RServe
   * @throws RSrvException when an error occurs when dialing with RServe
   */
  public void put(final String rName) throws RSrvException {

    final Rconnection con = getConnexion();

    if (con == null)
      return;

    final ExpressionMatrix matrix = getMatrix();
    final ExpressionMatrixDimension mDimension = matrix.getDefaultDimension();

    if (matrix == null)
      return;

    final String identifier = "tmp_" + System.currentTimeMillis() + "_";

    con.assign(identifier + "ids", new REXP(matrix.getRowNames()));

    final String[] colNames = matrix.getColumnNames();

    for (int i = 0; i < colNames.length; i++) {
      double[] val = mDimension.getColumnToArray(i);

      con.assign(identifier + colNames[i], val);
    }

    // REXP expr = con.eval("ls()");
    // Vector v = expr.asVector();

    StringBuffer sb = new StringBuffer();

    //
    // construct data.frame with the columns of the matrix
    //

    sb.append(rName);
    sb.append("=data.frame(");

    for (int i = 0; i < colNames.length; i++) {
      sb.append(identifier + colNames[i]);
      if (i != (colNames.length - 1))
        sb.append(",");
    }
    sb.append(")");

    System.out.println(sb);

    // sending the data.frame to the server
    con.voidEval(sb.toString());

    // cleaning the StringBuffer
    sb = new StringBuffer();

    //
    // rename the columns of the data.frame
    //

    sb.append("names(");
    sb.append(rName);
    sb.append(") = c(");

    for (int i = 0; i < colNames.length; i++) {
      sb.append('"' + colNames[i] + '"');
      if (i != (colNames.length - 1))
        sb.append(",");
    }
    sb.append(")");

    // sending the new names
    System.out.println(sb);
    con.voidEval(sb.toString());

    // cleaning the StringBuffer
    sb = new StringBuffer();

    //
    // Rename the rows of the matrix with the ids of the matrix
    //

    // final String[] ids = matrix.getIds();

    sb.append("row.names(");
    sb.append(rName);
    sb.append(") <- c(");
    sb.append(identifier);
    sb.append("ids");
    sb.append(")");

    System.out.println(sb);

    // sending the new names
    con.voidEval(sb.toString());

    // remove tmp object from the buffer
    con.voidEval("rm(" + identifier + "ids)");
    for (int i = 0; i < colNames.length; i++) {
      con.voidEval("rm(" + identifier + colNames[i] + ")");
    }
  }

  /**
   * get an ExpressionMatrixDimension from Rserve
   * @param rName the name of the matrix in RServe
   * @throws RSrvException when an error occurs when dialing with RServe
   */
  public void get(final String rName) throws RSrvException {

    final Rconnection con = getConnexion();

    setMatrix(null);

    if (con == null)
      return;

    ExpressionMatrix matrix = ExpressionMatrixFactory.createExpressionMatrix();
    ExpressionMatrixDimension mDimension = matrix.getDefaultDimension();

    if (matrix == null)
      return;

    REXP rexp = con.eval("row.names(" + rName + ")");

    Vector vectorIds = rexp.asVector();

    String[] ids = new String[vectorIds.size()];
    for (int i = 0; i < vectorIds.size(); i++) {
      ids[i] = ((REXP) vectorIds.get(i)).asString();
    }

    Vector vectorColumnNames = con.eval("names(" + rName + ")").asVector();
    String[] columnNames = new String[vectorColumnNames.size()];
    for (int i = 0; i < vectorColumnNames.size(); i++) {
      columnNames[i] = ((REXP) vectorColumnNames.get(i)).asString();
    }

    for (int i = 0; i < columnNames.length; i++) {

      final double[] values = con.eval(rName + "[," + (i + 1) + "]")
          .asDoubleArray();

      mDimension.addColumn(columnNames[i], ids, values);
    }

    setMatrix(matrix);

  }

  //
  // Constructor
  //

  /**
   * public constructor of the class RExpressionMatrix
   */
  public RExpressionMatrix() {
    super();
  }

}