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

import javax.swing.JScrollPane;
import javax.swing.JTable;

import fr.ens.transcriptome.nividic.om.BioAssay;

public class BioAssayComponent extends JScrollPane {

  private BioAssayTableModel bioAssayModel = new BioAssayTableModel();
  private JTable table = new JTable();

  /**
   * @return Returns the bioAssay
   */
  public BioAssay getBioAssay() {
    return this.bioAssayModel.getBioAssay();
  }

  /**
   * @param bioAssay The bioAssay to set
   */
  public void setBioAssay(final BioAssay bioAssay) {
    this.bioAssayModel.setBioAssay(bioAssay);
  }

  //
  // Drawing
  //

  private void init() {

    System.out.println("init");

    // JLabel label = new JLabel("hello");

    JTable table = this.table;

    table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    setViewportView(table);
    this.add(table.getTableHeader());
    setMinimumSize(table.getPreferredSize());

  }

  //
  // Constructors
  //

  public BioAssayComponent() {
    this(null);
  }

  public BioAssayComponent(final BioAssay bioAssay) {
    // super(new BorderLayout(), true);
    setBioAssay(bioAssay);
    this.table.setModel(this.bioAssayModel);
    init();
  }

}
