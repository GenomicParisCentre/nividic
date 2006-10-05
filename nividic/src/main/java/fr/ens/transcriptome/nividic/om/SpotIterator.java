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
 * This interface defines an interator over a bioAssay object.
 * @author Laurent Jourdren
 */
public interface SpotIterator extends Spot {

  /**
   * Test if the bioAssay has one more spot.
   * @return true if the bioAssay object has one more spot.
   */
  boolean hasNext();

  /**
   * Set the Spot iterator to the next spot.
   */
  void next();

  /**
   * Test if the bioAssay has previous spot.
   * @return true if the bioAssay object has previous spot.
   */
  boolean hasPrevious();

  /**
   * Set the Spot iterator to the previous spot.
   */
  void previous();

  /**
   * Set the Spot iterator to the first spot of the bioAssay.
   */
  void first();

  /**
   * Set the Spot iterator to the last spot of the bioAssay.
   */
  void last();

  /**
   * Test if there is elements in bioAssay.
   * @return true if there is element in the bioAssay
   */
  boolean isEmptyIterator();

}