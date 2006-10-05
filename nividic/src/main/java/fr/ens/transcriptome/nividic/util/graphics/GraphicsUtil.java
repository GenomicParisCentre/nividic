/*
 *                      Romde development code
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
 * For more information on the Romde project and its aims,
 * or to join the Romde mailing list, visit the home page
 * at:
 *
 *      http://www.transcriptome.ens.fr/romde
 *
 */

package fr.ens.transcriptome.nividic.util.graphics;

import java.awt.Image;
import java.awt.image.BufferedImage;

/**
 *  This class provides utility methods for graphics
 * @author Laurent Jourdren
 */
public final class GraphicsUtil {

  /**
   * Merge two image, one of the top of the other.
   * @param image1 First image to merge
   * @param image2 Second image to merge
   * @return a new image
   */
  public static Image mergeImages(final Image image1, final Image image2) {

    if (image1 == null || image2 == null || !(image1 instanceof BufferedImage)
        || !(image2 instanceof BufferedImage))
      return null;

    BufferedImage i1 = (BufferedImage) image1;
    BufferedImage i2 = (BufferedImage) image2;

    Image result = new BufferedImage(i1.getWidth(), i1.getHeight()
        + i2.getHeight(), BufferedImage.TYPE_INT_RGB);

    result.getGraphics().drawImage(i1, 0, 0, i1.getWidth(), i1.getHeight(),
        null);
    result.getGraphics().drawImage(i2, 0, i1.getHeight(), i2.getWidth(),
        i2.getHeight(), null);

    return result;
  }
  
}
