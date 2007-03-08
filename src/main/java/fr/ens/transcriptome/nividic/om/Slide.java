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

import fr.ens.transcriptome.nividic.om.datasources.DataSource;
import fr.ens.transcriptome.nividic.om.impl.SlideDescription;
import fr.ens.transcriptome.nividic.om.io.BioAssayFormat;
import fr.ens.transcriptome.nividic.om.io.NividicIOException;

public interface Slide {

  /**
   * Get the name of the slide.
   * @return The name of the slide
   */
  public abstract String getName();

  /**
   * Get the description of the slide.
   * @return a SlideDescription Object
   */
  public abstract SlideDescription getDescription();

  /**
   * Get the sample of a target
   * @param label Label of the target
   * @return The sample name of the target
   */
  public abstract String getTarget(final String label);

  /**
   * Get the source of a slide.
   * @return a DataSource object
   */
  public abstract DataSource getSource();

  /**
   * Get information about the source of the slide.
   * @return information about the source of the slide
   */
  public abstract String getSourceInfo();

  /**
   * Rename the slide.
   * @param newName The new name of the slide.
   */
  public abstract void setName(final String newName);

  /**
   * Set a target for a slide.
   * @param label Label of the target
   * @param sample Sample name of the target
   */
  public abstract void setTarget(final String label, final String sample);

  /**
   * Set the data source of a slide.
   * @param source The source to set
   */
  public abstract void setSource(final DataSource source);

  /**
   * Set a filename as a source of a slide.
   * @param filename The filename to set
   */
  public abstract void setSource(final String filename);

  /**
   * Set the bioassay of a slide.
   * @param bioassay The bioassay to set
   */
  public abstract void setBioAssay(final BioAssay bioassay);

  /**
   * Get the bioassay of a slide.
   * @return the bioassay object for the slide if exists
   */
  public abstract BioAssay getBioAssay();

  /**
   * Get the format of the source of the slide.
   * @return The format of the source of the slide
   */
  public abstract BioAssayFormat getFormat();

  /**
   * Set the format of the source fof the slide.
   * @param format The format to set
   */
  public abstract void setSourceFormat(final String format);

  /**
   * Set the format of the source fof the slide.
   * @param format The format to set
   */
  public abstract void setSourceFormat(final BioAssayFormat format);

  /**
   * Load a source data and set the bioAssay of a slide.
   * @throws NividicIOException if an error occurs while reading data
   */
  public abstract void loadSource() throws NividicIOException;

  /**
   * Swap the slide if the slide is a dye-swap.
   */
  public abstract void swapSlide();

}