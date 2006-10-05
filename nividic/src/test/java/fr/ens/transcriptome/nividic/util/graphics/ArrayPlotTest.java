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

import java.awt.Image;
import java.io.InputStream;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import junit.framework.TestCase;
import fr.ens.transcriptome.nividic.om.BioAssay;
import fr.ens.transcriptome.nividic.om.GenepixArrayList;
import fr.ens.transcriptome.nividic.om.io.GPRReader;
import fr.ens.transcriptome.nividic.om.io.NividicIOException;

/**
 * @author Laurent Jourdren
 */
public class ArrayPlotTest extends TestCase {

  public void testArrayPlot() {
  }

  public static void main(String[] args) {

    try {
      InputStream is = ArrayPlotTest.class
          .getResourceAsStream("/files/testGAL10.gal");

      GPRReader gprr = new GPRReader(is);
      BioAssay b = gprr.read();

      GenepixArrayList arl = new GenepixArrayList(b);

      ArrayPlot ap = new ArrayPlot(arl.getBlocks(), 600, -1, 10, b, "toto");

      Image image = ap.getImage();

      JFrame frame = new JFrame();
      frame.show();
      //frame.setSize(new Dimension(image.getWidth() + 50, getHeight() + 50));

      frame.getContentPane().add(new JLabel(new ImageIcon(image)));
      frame.show();

    } catch (NividicIOException e) {
      e.printStackTrace();
    }

  }
}
