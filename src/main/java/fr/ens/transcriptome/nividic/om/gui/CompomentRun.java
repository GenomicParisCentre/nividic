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
import java.awt.Color;
import java.awt.Dimension;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import fr.ens.transcriptome.nividic.om.BioAssay;
import fr.ens.transcriptome.nividic.om.ExpressionMatrix;
import fr.ens.transcriptome.nividic.om.io.GPRReader;
import fr.ens.transcriptome.nividic.om.io.NividicIOException;
import fr.ens.transcriptome.nividic.om.io.SimpleExpressionMatrixReader;

public class CompomentRun {

  public static final void main1(String[] args) throws NividicIOException {

    GPRReader reader = new GPRReader(new File("/home/jourdren/workspace/nividic/src/test/java/files/testGPR3.gpr"));
    reader.addAllFieldsToRead();
    BioAssay ba = reader.read();

    JFrame frame = new JFrame("Test table");
    frame.setSize(300, 200);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    // BioAssayComponent bac = new BioAssayComponent(null);
    BioAssayComponent bac = new BioAssayComponent(ba);
    // JScrollPane jsp = new JScrollPane(bac);
    // frame.add(jsp);
    // frame.add(bac);
    // frame.add(new JLabel("ggl"));

    JPanel panel = new JPanel(new BorderLayout());

    JPanel leftPanel = new JPanel(new BorderLayout());
    panel.add(leftPanel, BorderLayout.CENTER);

    JScrollPane scrollPane = new JScrollPane(bac);
    scrollPane.getViewport().setBackground(Color.WHITE);
    leftPanel.add(scrollPane);

    //panel.setPreferredSize(new Dimension(200, 80));

    frame.add(panel);
    frame.setVisible(true);

  }
  
  public static final void main(String[] args) throws NividicIOException {

    SimpleExpressionMatrixReader reader = new SimpleExpressionMatrixReader(new File("/home/jourdren/workspace/nividic/src/test/java/files/huge_matrix.txt"));
   
    ExpressionMatrix matrix = reader.read();

    JFrame frame = new JFrame("Test table");
    frame.setSize(300, 200);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    // BioAssayComponent bac = new BioAssayComponent(null);
    ExpressionMatrixComponent bac = new ExpressionMatrixComponent(matrix);
    // JScrollPane jsp = new JScrollPane(bac);
    // frame.add(jsp);
    // frame.add(bac);
    // frame.add(new JLabel("ggl"));

    JPanel panel = new JPanel(new BorderLayout());

    JPanel leftPanel = new JPanel(new BorderLayout());
    panel.add(leftPanel, BorderLayout.CENTER);

    JScrollPane scrollPane = new JScrollPane(bac);
    scrollPane.getViewport().setBackground(Color.WHITE);
    leftPanel.add(scrollPane);

    //panel.setPreferredSize(new Dimension(200, 80));

    frame.add(panel);
    frame.setVisible(true);

  }

}
