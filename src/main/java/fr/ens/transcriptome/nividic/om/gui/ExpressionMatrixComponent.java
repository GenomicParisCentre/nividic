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

package fr.ens.transcriptome.nividic.om.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import fr.ens.transcriptome.nividic.om.ExpressionMatrix;
import fr.ens.transcriptome.nividic.om.ExpressionMatrixDimension;

public class ExpressionMatrixComponent extends JPanel implements ActionListener {

  private ExpressionMatrixDimensionTableModel dimensionModel =
      new ExpressionMatrixDimensionTableModel();
  private ExpressionMatrix matrix;

  private JTable table = new JTable();

  /**
   * @return Returns the bioAssay
   */
  public ExpressionMatrix getMatrix() {
    return this.matrix;
  }

  /**
   * @param bioAssay The bioAssay to set
   */
  public void setMatrix(final ExpressionMatrix matrix) {
    this.matrix = matrix;

    ExpressionMatrixDimension dimension =
        matrix == null ? null : matrix.getDefaultDimension();

    this.dimensionModel.setDimension(dimension);
  }

  //
  // Drawing
  //

  private void init() {

    System.out.println("init");

    // JLabel label = new JLabel("hello");

    JTable table = this.table;

    table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    JScrollPane centerPane = new JScrollPane(table);

    // setMinimumSize(table.getPreferredSize());

    String text;

    String[] dimensionNames =
        matrix == null ? null : matrix.getDimensionNames();

    JComboBox dimensionList = new JComboBox(dimensionNames);
    if (matrix != null)
      dimensionList.setSelectedItem(matrix.getDefaultDimension());
    dimensionList.addActionListener(this);

    if (this.matrix != null) {

      text =
          matrix.getRowCount()
              + " rows, " + matrix.getColumnCount() + " columns, "
              + matrix.getDimensionCount() + " dimensions";

    } else
      text = "";

    JLabel label = new JLabel(text);

    setLayout(new BorderLayout());
    add(dimensionList,BorderLayout.NORTH);
    add(centerPane, BorderLayout.CENTER);
    add(label, BorderLayout.SOUTH);
  }

  //
  // Constructors
  //

  public ExpressionMatrixComponent() {
    this(null);
  }

  public ExpressionMatrixComponent(final ExpressionMatrix matrix) {
    // super(new BorderLayout(), true);
    setMatrix(matrix);
    this.table.setModel(this.dimensionModel);
    init();
  }

  public void actionPerformed(ActionEvent e) {

    JComboBox cb = (JComboBox) e.getSource();
    String dimensionName = (String) cb.getSelectedItem();
    if (this.matrix != null)
      dimensionModel.setDimension(this.matrix.getDimension(dimensionName));
  }

}
