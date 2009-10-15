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
 * of the �cole Normale Sup�rieure and the individual authors.
 * These should be listed in @author doc comments.
 *
 * For more information on the Nividic project and its aims,
 * or to join the Nividic mailing list, visit the home page
 * at:
 *
 *      http://www.transcriptome.ens.fr/nividic
 *
 */

package fr.ens.transcriptome.nividic.om.design;

import fr.ens.transcriptome.nividic.om.BioAssay;
import fr.ens.transcriptome.nividic.om.datasources.DataSource;
import fr.ens.transcriptome.nividic.om.io.BioAssayFormat;
import fr.ens.transcriptome.nividic.om.io.NividicIOException;

/**
 * This interface define a slide.
 * @author Laurent Jourdren
 */
public interface Slide {

  //
  // Getters
  //

  /**
   * Get the name of the slide.
   * @return The name of the slide
   */
  String getName();

  /**
   * Get the description of the slide.
   * @return a SlideDescriptionImpl Object
   */
  SlideDescription getDescription();

  /**
   * Get the sample of a target
   * @param label Label of the target
   * @return The sample name of the target
   */
  String getTarget(final String label);

  /**
   * Get the scan labels settings.
   * @return a scan labels settings for the object
   */
  ScanLabelsSettings getScanLabelsSettings();

  /**
   * Get the source of a slide.
   * @return a DataSource object
   */
  DataSource getSource();

  /**
   * Get information about the source of the slide.
   * @return information about the source of the slide
   */
  String getSourceInfo();

  //
  // Setters
  //

  /**
   * Rename the slide.
   * @param newName The new name of the slide.
   */
  void setName(final String newName);

  /**
   * Set a target for a slide.
   * @param label Label of the target
   * @param sample Sample name of the target
   */
  void setTarget(final String label, final String sample);

  /**
   * Set the data source of a slide.
   * @param source The source to set
   */
  void setSource(final DataSource source);

  /**
   * Set a filename as a source of a slide.
   * @param filename The filename to set
   */
  void setSource(final String filename);

  /**
   * Set the bioassay of a slide.
   * @param bioassay The bioassay to set
   */
  void setBioAssay(final BioAssay bioassay);

  /**
   * Get the bioassay of a slide.
   * @return the bioassay object for the slide if exists
   */
  BioAssay getBioAssay();

  /**
   * Get the format of the source of the slide.
   * @return The format of the source of the slide
   */
  BioAssayFormat getFormat();

  /**
   * Set the format of the source fof the slide.
   * @param format The format to set
   */
  void setSourceFormat(final String format);

  /**
   * Set the format of the source fof the slide.
   * @param format The format to set
   */
  void setSourceFormat(final BioAssayFormat format);

  //
  // Setters
  //

  /**
   * Load a source data and set the bioAssay of a slide.
   * @throws NividicIOException if an error occurs while reading data
   */
  void loadSource() throws NividicIOException;
  
  /**
   * Load a source data and set the bioAssay of a slide.
   * @param readAllFields true if all the fields must be read
   * @throws NividicIOException if an error occurs while reading data
   */
  void loadSource(boolean readAllFields) throws NividicIOException;
  
  /**
   * Load a source data and set the bioAssay of a slide.
   * @param readAllFields true if all the fields must be read
   * @param additionalfieldsToRead List of additional fields to read
   * @throws NividicIOException if an error occurs while reading data
   */
  void loadSource(boolean readAllFields, String [] additionalfieldsToRead) throws NividicIOException;

  /**
   * Swap the slide if the slide is a dye-swap.
   */
  void swapSlide();

}