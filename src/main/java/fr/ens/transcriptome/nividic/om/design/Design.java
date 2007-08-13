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

import java.util.List;

import fr.ens.transcriptome.nividic.om.BioAssay;
import fr.ens.transcriptome.nividic.om.BiologicalObject;
import fr.ens.transcriptome.nividic.om.datasources.DataSource;
import fr.ens.transcriptome.nividic.om.io.BioAssayFormat;
import fr.ens.transcriptome.nividic.om.io.NividicIOException;
import fr.ens.transcriptome.nividic.om.samples.Samples;

/**
 * This interface define a Design.
 * @author Laurent Jourdren
 */
public interface Design extends BiologicalObject {

  /**
   * Get the samples.
   * @return the sample object related to the slides
   */
  Samples getSamples();

  /**
   * Test if the label is already set.
   * @param name Name of the label to test
   * @return true if the label exists
   */
  boolean isLabel(final String name);

  /**
   * Add a label.
   * @param name Name of the label to add
   */
  void addLabel(final String name);

  /**
   * Rename a label.
   * @param oldName Old name of the label
   * @param newName New name of the label
   */
  void renameLabel(final String oldName, final String newName);

  /**
   * Get the names of the lables.
   * @return A List with the name of the labels
   */
  List<String> getLabelsNames();

  /**
   * Remove a label.
   * @param name Name of the label to remove
   */
  void removeLabel(final String name);

  /**
   * Get the number of labels
   * @return The number of labels
   */
  int getLabelCount();

  /**
   * Test if a slide exists.
   * @param name Name of the slide to test
   * @return true if the slide exists
   */
  boolean isSlide(final String name);

  /**
   * Add a slide.
   * @param name Name of the slide to add
   */
  void addSlide(final String name);

  /**
   * Rename a slide.
   * @param oldName Old name of the slide
   * @param newName New name of the slide
   */
  void renameSlide(final String oldName, final String newName);

  /**
   * Get the name of the slides
   * @return a Set with the name of the slides
   */
  List<String> getSlidesNames();

  /**
   * Remove a slide.
   * @param name Name of the slide to remove
   */
  void removeSlide(final String name);

  /**
   * Get the number of slides in the design.
   * @return The number of slides in the design
   */
  int getSlideCount();

  /**
   * Set a target for a slide.
   * @param slide Slide of the target
   * @param label Label of the target
   * @param sample Sample name of the target
   */
  void setTarget(String slide, String label, String sample);

  /**
   * Get the sample of a target
   * @param slide Slide of the target
   * @param label Label of the target
   * @return The sample name of the target
   */
  String getTarget(final String slide, final String label);

  /**
   * Get the description of a slide.
   * @param name Name of the slide
   * @return a slide decription object
   */
  SlideDescription getSlideDescription(final String name);

  /**
   * Set the data source of a slide.
   * @param slideName The name of the slide
   * @param source The source to set
   */
  void setSource(final String slideName, final DataSource source);

  /**
   * Set a filename as a source of a slide.
   * @param slideName The name of the slide
   * @param filename The filename to set
   */
  void setSource(final String slideName, final String filename);

  /**
   * Set the data format of a slide.
   * @param slideName The name of the slide
   * @param format The format to set
   */
  void setSourceFormat(final String slideName, final BioAssayFormat format);

  /**
   * Set the data format of a slide.
   * @param slideName The name of the slide
   * @param formatName The format to set
   */
  void setSourceFormat(final String slideName, final String formatName);

  /**
   * Get the source of a slide.
   * @param slideName Name of the slide
   * @return a DataSource object
   */
  DataSource getSource(final String slideName);

  /**
   * Get information about the source of the slide.
   * @param slideName Name of the slide
   * @return information about the source of the slide
   */
  String getSourceInfo(final String slideName);

  /**
   * Get the format of data source of a slide
   * @param slideName Name of the slide
   * @return the format of the data source
   */
  BioAssayFormat getFormat(final String slideName);

  /**
   * Extract a slide object from the design.
   * @param index Index of the slide in the design
   * @return a slide object
   */
  Slide getSlide(final int index);

  /**
   * Extract a slide object from the design.
   * @param slideName The name of the slide to extract
   * @return a slide object
   */
  Slide getSlide(final String slideName);

  /**
   * Get a list of the slides of the design
   * @return a unmodifiable list of the slides
   */
  List<Slide> getSlides();

  /**
   * Set the bioassay of a slide.
   * @param slideName The name of the slide
   * @param bioassay The bioassay to set
   */
  void setBioAssay(final String slideName, final BioAssay bioassay);

  /**
   * Get the bioassay of a slide.
   * @param slideName Name of the slide
   * @return the bioassay object for the slide if exists
   */
  BioAssay getBioAssay(final String slideName);

  /**
   * Test if the description field is already set.
   * @param name Name of the description field to test
   * @return true if the description exists
   */
  boolean isDescriptionField(final String name);

  /**
   * Add a description field.
   * @param name Name of the label to add
   */
  void addDescriptionField(final String name);

  /**
   * Rename a description field.
   * @param oldName Old name of the description field
   * @param newName New name of the description field
   */
  void renameDescriptionField(final String oldName, final String newName);

  /**
   * Get the names of the descriptions fields.
   * @return A List with the name of the description fields
   */
  List<String> getDescriptionFieldsNames();

  /**
   * Remove a description field.
   * @param name Name of the descritpion field to remove
   */
  void removeDescriptionField(final String name);

  /**
   * Get the number of description fields
   * @return The number of description fields
   */
  int getDescriptionFieldCount();

  /**
   * Set a description for a slide.
   * @param slide Slide of the target
   * @param descriptionField Description field
   * @param value of the description to set
   */
  void setDescription(final String slide, final String descriptionField,
      final String value);

  /**
   * Get a description
   * @param slide Slide of the target
   * @param descriptionField The description field
   * @return The value of the description
   */
  String getDescription(final String slide, final String descriptionField);

  /**
   * Get setting about the scan of a label.
   * @param slide Slide of the target
   * @param label Name of the label
   * @param key key of the setting
   * @return the value of the setting
   */
  String getScanLabelSetting(final String slide, final String label,
      final String key);

  /**
   * Set setting about the scan of a label.
   * @param slide Slide of the target
   * @param label Name of the label
   * @param setting key of the setting
   * @param value value of the setting
   */
  void setScanLabelSetting(final String slide, final String label,
      final String setting, final String value);

  /**
   * Test if the scan label setting is already set.
   * @param setting Name of the description field to test
   * @return true if the description exists
   */
  boolean isScanLabelSetting(final String setting);

  /**
   * Add a scan label setting.
   * @param setting Name of the setting to add
   */
  void addScanLabelSetting(final String setting);

  /**
   * Rename a scan label setting.
   * @param oldName Old name of the scan label setting
   * @param newName New name of the scan label setting
   */
  void renameScanLabelSetting(final String oldName, final String newName);

  /**
   * Get the names of the scan label settings.
   * @return A List with the scan label settings
   */
  List<String> getScanLabelSettingsKeys();

  /**
   * Remove a scan label setting.
   * @param setting Setting name to remove
   */
  void removeScanLabelSetting(final String setting);

  //
  // Other methods
  //

  /**
   * Load all source and set all the bioassays of a design.
   * @throws NividicIOException if an error occurs while reading data
   */
  void loadAllSources() throws NividicIOException;

  /**
   * Swap all the slides if the slides are a dye-swap.
   */
  void swapAllSlides();

}