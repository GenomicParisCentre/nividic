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

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JDialog;

/**
 * Show an image.
 *
 * @author Laurent Jourdren
 */
public class ShowImage extends JDialog {

  private class ImageCanvas extends Canvas {

    private Image img;

    /**
     * Show an image.
     * @param image Image to show
     */
    public ImageCanvas(final Image image) {
      this.img = image;
      MediaTracker mediaTracker = new MediaTracker(this);
      mediaTracker.addImage(img, 0);
      try {
        mediaTracker.waitForID(0);
      } catch (InterruptedException ie) {
        System.err.println(ie);
        System.exit(1);
      }
      setSize(img.getWidth(null), img.getHeight(null));
    }

    /**
     * Paint the image.
     * @param g The graphics
     */
    public void paint(final Graphics g) {
      g.drawImage(img, 0, 0, null);
    }
  }

  /**
   * Public constructor.
   *
   * @param image Image to show
   */
  public ShowImage(final Image image) {

    if (image == null)
      return;

    setName("Test image");

    getContentPane().add(new ImageCanvas(image));
    addWindowListener(new WindowAdapter() { // just do we can close the window
      public void windowClosing(final WindowEvent e) {
        System.exit(0);
      }
    });
  }

}
