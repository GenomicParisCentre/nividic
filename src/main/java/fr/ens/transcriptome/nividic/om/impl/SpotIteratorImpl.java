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

import fr.ens.transcriptome.nividic.om.BioAssay;
import fr.ens.transcriptome.nividic.om.BioAssayRuntimeException;
import fr.ens.transcriptome.nividic.om.SpotIterator;

/**
 * This class define the standard implemenation of a spor iterator.
 * @author Laurent Jourdren
 */
public class SpotIteratorImpl extends SpotImpl implements SpotIterator {

  /**
   * Test the index
   * @param index index to test
   * @return true if the spot for the index exists
   */
  private boolean testIndex(final int index) {
    return index >= 0 && index < getBioAssay().size();
  }

  /**
   * Test if the bioAssay has one more spot.
   * @return true if the bioAssay object has one more spot.
   */
  public boolean hasNext() {
    return testIndex(getIndex() + 1);
  }

  /**
   * Set the Spot iterator to the next spot.
   */
  public void next() {
    if (hasNext())
      setIndex(getIndex() + 1);
    else
      throw new BioAssayRuntimeException("Out of bounds");
  }

  /**
   * Test if the bioAssay has previous spot.
   * @return true if the bioAssay object has previous spot.
   */
  public boolean hasPrevious() {
    return testIndex(getIndex() - 1);
  }

  /**
   * Set the Spot iterator to the previous spot.
   */
  public void previous() {

    if (hasPrevious())
      setIndex(getIndex() - 1);
    else
      throw new BioAssayRuntimeException("Out of bounds");
  }

  /**
   * Set the Spot iterator to the first spot of the bioAssay.
   */
  public void first() {
    if (testIndex(0))
      setIndex(0);
    else
      throw new BioAssayRuntimeException("Out of bounds");
  }

  /**
   * Set the Spot iterator to the last spot of the bioAssay.
   */
  public void last() {
    final int last = getBioAssay().size() - 1;
    if (testIndex(last))
      setIndex(last);
    else
      throw new BioAssayRuntimeException("Out of bounds");
  }

  /**
   * Test if there is elements in bioAssay.
   * @return true if there is element in the bioAssay
   */
  public boolean isEmptyIterator() {
    return getBioAssay().size() == 0;
  }

  //
  // Constructor
  //

  SpotIteratorImpl(final BioAssay bioAssay) {
    super(bioAssay);
    setIndex(-1);
  }

}