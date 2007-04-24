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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.ens.transcriptome.nividic.NividicRuntimeException;
import fr.ens.transcriptome.nividic.om.Annotation;
import fr.ens.transcriptome.nividic.om.AnnotationFactory;
import fr.ens.transcriptome.nividic.om.BioAssay;
import fr.ens.transcriptome.nividic.om.Design;
import fr.ens.transcriptome.nividic.om.History;
import fr.ens.transcriptome.nividic.om.HistoryEntry;
import fr.ens.transcriptome.nividic.om.Slide;
import fr.ens.transcriptome.nividic.om.HistoryEntry.HistoryActionResult;
import fr.ens.transcriptome.nividic.om.HistoryEntry.HistoryActionType;
import fr.ens.transcriptome.nividic.om.datasources.DataSource;
import fr.ens.transcriptome.nividic.om.datasources.FileDataSource;
import fr.ens.transcriptome.nividic.om.filters.BiologicalFilter;
import fr.ens.transcriptome.nividic.om.filters.DesignFilter;
import fr.ens.transcriptome.nividic.om.io.BioAssayFormat;
import fr.ens.transcriptome.nividic.om.io.NividicIOException;
import fr.ens.transcriptome.nividic.om.samples.Samples;
import fr.ens.transcriptome.nividic.om.samples.SamplesFactory;

/**
 * This class decribe the experiment design.
 * @author Laurent Jourdren
 */
public class DesignImpl implements Design {

  private BiologicalName name = new BiologicalName(this);
  private HistoryImpl history = new HistoryImpl();
  private Annotation annotation = AnnotationFactory.createAnnotation();

  private Samples samples = SamplesFactory.createSamples();
  private List<String> slidesOrder = new ArrayList<String>();
  private Map<String, Integer> slides = new HashMap<String, Integer>();
  private Map<Integer, String> slidesReverse = new HashMap<Integer, String>();
  // private Map<Integer, SlideDescription> slidesDescriptions = new
  // HashMap<Integer, SlideDescription>();

  private Map<String, Integer> labels = new HashMap<String, Integer>();
  private List<String> labelsOrder = new ArrayList<String>();

  private Map<String, Integer> targets = new HashMap<String, Integer>();

  private Map<Integer, DataSource> sources = new HashMap<Integer, DataSource>();
  private Map<Integer, BioAssayFormat> formats = new HashMap<Integer, BioAssayFormat>();
  private Map<Integer, BioAssay> bioassays = new HashMap<Integer, BioAssay>();

  private Map<String, Integer> descriptionsFields = new HashMap<String, Integer>();
  private List<String> descriptionsOrder = new ArrayList<String>();
  private Map<String, String> descriptionData = new HashMap<String, String>();

  private int countSlides;
  private int countLabels;

  /**
   * Get the id of the biological Object
   * @return an Integer as biological id.
   */
  public int getBiologicalId() {
    return name.getBiologicalId();
  }

  /**
   * Get the name of the list.
   * @return the name of list
   */
  public String getName() {

    return this.name.getName();
  }

  /**
   * Set the name of the list.
   * @param name The name of the list
   */
  public void setName(final String name) {

    this.name.setName(name);
  }

  /**
   * Get the annotation of the object.
   * @return An annotation object
   */
  public Annotation getAnnotation() {
    return this.annotation;
  }

  //
  // Samples
  //

  /**
   * Get the samples.
   * @return the sample object related to the slides
   */
  public Samples getSamples() {

    return samples;
  }

  //
  // Labels
  //

  /**
   * Test if the label is already set.
   * @param name Name of the label to test
   * @return true if the label exists
   */
  public boolean isLabel(final String name) {

    return this.labels.containsKey(name);
  }

  /**
   * Add a label.
   * @param name Name of the label to add
   */
  public void addLabel(final String name) {

    if (name == null)
      throw new NividicRuntimeException("Label name can't be null");
    if (isLabel(name))
      throw new NividicRuntimeException("Label name already exists");

    this.labels.put(name, countLabels++);
    this.labelsOrder.add(name);
  }

  /**
   * Rename a label.
   * @param oldName Old name of the label
   * @param newName New name of the label
   */
  public void renameLabel(final String oldName, final String newName) {

    if (oldName == null)
      throw new NividicRuntimeException("oldName name can't be null");
    if (newName == null)
      throw new NividicRuntimeException("newName name can't be null");

    if (!isLabel(oldName))
      throw new NividicRuntimeException("the old label name don't exists");
    if (isLabel(newName))
      throw new NividicRuntimeException("the new label name already exists");

    int id = this.labels.get(oldName);
    this.labels.remove(oldName);
    this.labels.put(newName, id);

    final int index = Collections.binarySearch(this.labelsOrder, oldName);
    this.labelsOrder.set(index, newName);
  }

  /**
   * Get the names of the lables.
   * @return A List with the name of the labels
   */
  public List<String> getLabelsNames() {

    return Collections.unmodifiableList(this.labelsOrder);
  }

  /**
   * Remove a label.
   * @param name Name of the label to remove
   */
  public void removeLabel(final String name) {

    if (name == null)
      throw new NividicRuntimeException("Label name can't be null");

    if (!isLabel(name))
      throw new NividicRuntimeException("the label name doesn't exists");

    // Remove targets
    final String suffix = "-" + this.labels.get(name);

    for (String key : this.targets.keySet())
      if (key.endsWith(suffix))
        this.targets.remove(key);

    // Remove entry
    this.labels.remove(name);
    this.labelsOrder.remove(name);
  }

  /**
   * Get the number of labels
   * @return The number of labels
   */
  public int getLabelCount() {

    return this.labels.size();
  }

  //
  // Slides
  //

  /**
   * Test if a slide exists.
   * @param name Name of the slide to test
   * @return true if the slide exists
   */
  public boolean isSlide(final String name) {

    return this.slides.containsKey(name);
  }

  /**
   * Add a slide.
   * @param name Name of the slide to add
   */
  public void addSlide(final String name) {

    if (name == null)
      throw new NividicRuntimeException("Slide name can't be null");
    if (isSlide(name))
      throw new NividicRuntimeException("Slide name already exists");

    final int slideId = countSlides++;
    this.slides.put(name, slideId);
    this.slidesReverse.put(slideId, name);
    this.slidesOrder.add(name);
  }

  /**
   * Rename a slide.
   * @param oldName Old name of the slide
   * @param newName New name of the slide
   */
  public void renameSlide(final String oldName, final String newName) {

    if (oldName == null)
      throw new NividicRuntimeException("oldName name can't be null");
    if (newName == null)
      throw new NividicRuntimeException("newName name can't be null");

    if (!isSlide(oldName))
      throw new NividicRuntimeException("the old slide name don't exists");
    if (isSlide(newName))
      throw new NividicRuntimeException("the new slide name already exists");

    int slideId = this.slides.get(oldName);
    this.slides.remove(oldName);
    this.slides.put(newName, slideId);
    this.slidesReverse.put(slideId, newName);

    final int index = Collections.binarySearch(this.slidesOrder, oldName);
    this.slidesOrder.set(index, newName);
  }

  /**
   * Get the name of the slides
   * @return a Set with the name of the slides
   */
  public List<String> getSlidesNames() {

    return Collections.unmodifiableList(this.slidesOrder);
  }

  /**
   * Remove a slide.
   * @param name Name of the slide to remove
   */
  public void removeSlide(final String name) {

    if (name == null)
      throw new NividicRuntimeException("Slide name can't be null");

    if (!isSlide(name))
      throw new NividicRuntimeException("the slide name doesn't exists");

    // Remove targets
    final String prefixTarget = this.slides.get(name) + "-";

    for (String key : this.targets.keySet())
      if (key.startsWith(prefixTarget))
        this.targets.remove(key);

    // Remove descriptions
    final String prefixDescritpion = this.slides.get(name) + "-";

    for (String key : this.descriptionData.keySet())
      if (key.startsWith(prefixDescritpion))
        this.descriptionData.remove(key);

    // Remove formats and bioassays
    final int slideId = this.slides.get(name);
    this.formats.remove(slideId);
    this.bioassays.remove(slideId);

    // Remove entry
    this.slides.remove(name);
    this.slidesReverse.remove(slideId);
    this.slidesOrder.remove(name);
  }

  /**
   * Remove a slide.
   * @param slide The slideto remove
   */
  public void removeSlide(final Slide slide) {

    if (slide == null)
      throw new NullPointerException("Slide is null");

    removeSlide(slide.getName());
  }

  /**
   * Get the number of slides in the design.
   * @return The number of slides in the design
   */
  public int getSlideCount() {

    return this.slides.size();
  }

  /**
   * Get the slide name from the slide id.
   * @param slideId Slide identifier
   * @return the name of the slide or null if not exists
   */
  String getSlideName(final int slideId) {

    return this.slidesReverse.get(slideId);
  }

  /**
   * Extract a slide object from the design.
   * @param slideName The name of the slide to extract
   * @return a slide object
   */
  public Slide getSlide(final String slideName) {

    if (slideName == null)
      throw new NividicRuntimeException("The slide name can't be null");

    if (!isSlide(slideName))
      throw new NividicRuntimeException("The slide doesn't exists");

    final int slideId = this.slides.get(slideName);

    return new SlideImpl(this, slideId);
  }

  /**
   * Extract a slide object from the design.
   * @param index Index of the slide in the design
   * @return a slide object
   */
  public Slide getSlide(final int index) {

    String name = this.slidesOrder.get(index);
    if (name == null)
      throw new NividicRuntimeException("The slide index doesn't exists");

    return getSlide(name);
  }

  /**
   * Get a list of the slides of the design
   * @return a unmodifiable list of the slides
   */
  public List<Slide> getSlides() {

    List<Slide> result = new ArrayList<Slide>();

    List<String> names = getSlidesNames();

    for (String n : names)
      result.add(getSlide(n));

    return Collections.unmodifiableList(result);
  }

  //
  // Targets
  //

  private String createkeySlideFluo(final String slide, final String fluo) {

    final int slideId = this.slides.get(slide);
    final int fluoId = this.labels.get(fluo);

    return slideId + "-" + fluoId;
  }

  /**
   * Set a target for a slide.
   * @param slide Slide of the target
   * @param label Label of the target
   * @param sample Sample name of the target
   */
  public void setTarget(final String slide, final String label,
      final String sample) {

    if (slide == null)
      throw new NullPointerException("Slide name can't be null");
    if (label == null)
      throw new NullPointerException("Label name can't be null");
    if (sample == null)
      throw new NullPointerException("Sample name can't be null");

    if (!isSlide(slide))
      throw new NividicRuntimeException("The slide name doesn't exists");
    if (!isLabel(label))
      throw new NividicRuntimeException("The label name doesn't exists");
    if (!this.samples.isSample(sample))
      throw new NividicRuntimeException("The sample name doesn't exists");

    final int sampleId = this.samples.getSampleId(sample);

    this.targets.put(createkeySlideFluo(slide, label), sampleId);
  }

  /**
   * Get the sample of a target
   * @param slide Slide of the target
   * @param label Label of the target
   * @return The sample name of the target
   */
  public String getTarget(final String slide, final String label) {

    if (slide == null)
      throw new NullPointerException("Slide name can't be null");
    if (label == null)
      throw new NullPointerException("Label name can't be null");

    if (!isSlide(slide))
      throw new NividicRuntimeException("The slide name doesn't exists");
    if (!isLabel(label))
      throw new NividicRuntimeException("The label name doesn't exists");

    final int id = this.targets.get(createkeySlideFluo(slide, label));

    return this.samples.getSampleName(id);
  }

  //
  // Descriptions
  //

  private String createkeySlideDescriptionField(final String slide,
      final String descriptionField) {

    final int slideId = this.slides.get(slide);
    final int fieldId = this.descriptionsFields.get(descriptionField);

    return slideId + "-" + fieldId;
  }

  /**
   * Get the description of a slide.
   * @param name Name of the slide
   * @return a slide decription object
   */
  public SlideDescription getSlideDescription(final String name) {

    if (name == null)
      throw new NullPointerException("Slide name is null");

    if (!isSlide(name))
      throw new NividicRuntimeException("The slide doesn't exists");

    final int id = this.slides.get(name);

    return new SlideDescription(this, id);
  }

  /**
   * Test if the description field is already set.
   * @param name Name of the description field to test
   * @return true if the description exists
   */
  public boolean isDescriptionField(final String name) {

    return this.descriptionsFields.containsKey(name);
  }

  /**
   * Add a description field.
   * @param name Name of the label to add
   */
  public void addDescriptionField(final String name) {

    if (name == null)
      throw new NividicRuntimeException("The description field can't be null");
    if (isDescriptionField(name))
      throw new NividicRuntimeException(
          "The descriptionLabel name already exists");

    this.descriptionsFields.put(name, countLabels++);
    this.descriptionsOrder.add(name);
  }

  /**
   * Rename a description field.
   * @param oldName Old name of the description field
   * @param newName New name of the description field
   */
  public void renameDescriptionField(final String oldName, final String newName) {

    if (oldName == null)
      throw new NividicRuntimeException("oldName name can't be null");
    if (newName == null)
      throw new NividicRuntimeException("newName name can't be null");

    if (!isDescriptionField(oldName))
      throw new NividicRuntimeException("the old label name don't exists");
    if (isDescriptionField(newName))
      throw new NividicRuntimeException("the new label name already exists");

    int id = this.descriptionsFields.get(oldName);
    this.descriptionsFields.remove(oldName);
    this.descriptionsFields.put(newName, id);

    final int index = Collections.binarySearch(this.descriptionsOrder, oldName);
    this.descriptionsOrder.set(index, newName);
  }

  /**
   * Get the names of the descriptions fields.
   * @return A List with the name of the description fields
   */
  public List<String> getDescriptionFieldsNames() {

    return Collections.unmodifiableList(this.descriptionsOrder);
  }

  /**
   * Remove a description field.
   * @param name Name of the descritpion field to remove
   */
  public void removeDescriptionField(final String name) {

    if (name == null)
      throw new NividicRuntimeException(
          "The description field name can't be null");

    if (!isDescriptionField(name))
      throw new NividicRuntimeException(
          "The description field name doesn't exists");

    // Remove targets
    final String suffix = "-" + this.descriptionsFields.get(name);

    for (String key : this.descriptionData.keySet())
      if (key.endsWith(suffix))
        this.descriptionData.remove(key);

    // Remove entry
    this.descriptionsFields.remove(name);
    this.descriptionsOrder.remove(name);
  }

  /**
   * Get the number of description fields
   * @return The number of description fields
   */
  public int getDescriptionFieldCount() {

    return this.descriptionsOrder.size();
  }

  /**
   * Set a description for a slide.
   * @param slide Slide of the target
   * @param descriptionField Description field
   * @param value of the description to set
   */
  public void setDescription(final String slide, final String descriptionField,
      final String value) {

    if (slide == null)
      throw new NullPointerException("Slide name can't be null");
    if (descriptionField == null)
      throw new NullPointerException("Description field can't be null");
    if (value == null)
      throw new NullPointerException("value name can't be null");

    if (!isSlide(slide))
      throw new NividicRuntimeException("The slide name doesn't exists");
    if (!isDescriptionField(descriptionField))
      addDescriptionField(descriptionField);

    this.descriptionData.put(createkeySlideDescriptionField(slide,
        descriptionField), value);
  }

  /**
   * Get a description
   * @param slide Slide of the target
   * @param descriptionField The description field
   * @return The value of the description
   */
  public String getDescription(final String slide, final String descriptionField) {

    if (slide == null)
      throw new NullPointerException("Slide name can't be null");
    if (descriptionField == null)
      throw new NullPointerException("Label name can't be null");

    if (!isSlide(slide))
      throw new NividicRuntimeException("The slide name doesn't exists");
    if (!isDescriptionField(descriptionField))
      throw new NividicRuntimeException("The description name doesn't exists");

    return this.descriptionData.get(createkeySlideDescriptionField(slide,
        descriptionField));
  }

  //
  // Sources
  //

  /**
   * Set the data source of a slide.
   * @param slideName The name of the slide
   * @param source The source to set
   */
  public void setSource(final String slideName, final DataSource source) {

    if (slideName == null)
      throw new NullPointerException("Slide name is null");

    if (source == null)
      throw new NullPointerException("Slide source is null");

    if (!isSlide(slideName))
      throw new NividicRuntimeException("The slide doesn't exists");

    final int id = this.slides.get(slideName);

    this.sources.put(id, source);
  }

  /**
   * Set a filename as a source of a slide.
   * @param slideName The name of the slide
   * @param filename The filename to set
   */
  public void setSource(final String slideName, final String filename) {

    setSource(slideName, new FileDataSource(filename));
  }

  /**
   * Set the data format of a slide.
   * @param slideName The name of the slide
   * @param format The format to set
   */
  public void setSourceFormat(final String slideName,
      final BioAssayFormat format) {

    if (slideName == null)
      throw new NullPointerException("Slide name is null");

    if (!isSlide(slideName))
      throw new NividicRuntimeException("The slide doesn't exists");

    final int id = this.slides.get(slideName);

    if (format == null) {

      if (this.formats.containsKey(id))
        this.formats.remove(id);
    } else
      this.formats.put(id, format);
  }

  /**
   * Set the data format of a slide.
   * @param slideName The name of the slide
   * @param formatName The format to set
   */
  public void setSourceFormat(final String slideName, final String formatName) {

    if (formatName == null)
      setSourceFormat(slideName, (BioAssayFormat) null);
    else {
      BioAssayFormat format = BioAssayFormat.getBioAssayFormat(formatName);
      setSourceFormat(slideName, format);
    }
  }

  /**
   * Get the source of a slide.
   * @param slideName Name of the slide
   * @return a DataSource object
   */
  public DataSource getSource(final String slideName) {

    if (slideName == null)
      throw new NullPointerException("Slide name is null");

    if (!isSlide(slideName))
      throw new NividicRuntimeException("The slide doesn't exists");

    final int id = this.slides.get(slideName);

    return this.sources.get(id);
  }

  /**
   * Get information about the source of the slide.
   * @param slideName Name of the slide
   * @return information about the source of the slide
   */
  public String getSourceInfo(final String slideName) {

    if (slideName == null)
      throw new NullPointerException("Slide name is null");

    if (!isSlide(slideName))
      throw new NividicRuntimeException("The slide doesn't exists");

    final int id = this.slides.get(slideName);

    DataSource ds = this.sources.get(id);

    return ds == null ? "" : ds.getSourceInfo();
  }

  /**
   * Get the format of data source of a slide
   * @param slideName Name of the slide
   * @return the format of the data source
   */
  public BioAssayFormat getFormat(final String slideName) {

    if (slideName == null)
      throw new NullPointerException("Slide name is null");

    if (!isSlide(slideName))
      throw new NividicRuntimeException("The slide doesn't exists");

    final int id = this.slides.get(slideName);

    return this.formats.get(id);
  }

  //
  // BioAssays
  //

  /**
   * Set the bioassay of a slide.
   * @param slideName The name of the slide
   * @param bioassay The bioassay to set
   */
  public void setBioAssay(final String slideName, final BioAssay bioassay) {

    if (slideName == null)
      throw new NullPointerException("Slide name is null");

    if (bioassay == null)
      throw new NullPointerException("The biossay for the slide is null");

    if (!isSlide(slideName))
      throw new NividicRuntimeException("The slide doesn't exists");

    final int id = this.slides.get(slideName);

    this.bioassays.put(id, bioassay);
  }

  /**
   * Get the bioassay of a slide.
   * @param slideName Name of the slide
   * @return the bioassay object for the slide if exists
   */
  public BioAssay getBioAssay(final String slideName) {

    if (slideName == null)
      throw new NullPointerException("Slide name is null");

    if (!isSlide(slideName))
      throw new NividicRuntimeException("The slide doesn't exists");

    final int id = this.slides.get(slideName);

    return this.bioassays.get(id);
  }

  //
  // Other methods
  //

  /**
   * Load all source and set all the bioassays of a design.
   * @throws NividicIOException if an error occurs while reading data
   */
  public void loadAllSources() throws NividicIOException {

    final List<Slide> slides = getSlides();

    for (Slide slide : slides)
      slide.loadSource();
  }

  /**
   * Swap all the slides if the slides are a dye-swap.
   */
  public void swapAllSlides() {

    final List<Slide> slides = getSlides();

    for (Slide slide : slides)
      slide.swapSlide();
  }

  /**
   * Apply a filter on the design.
   * @param filter Filter to apply
   * @return a new design list
   */
  public Design filter(final BiologicalFilter filter) {

    if (filter == null)
      return null;

    if (!(filter instanceof DesignFilter))
      throw new NividicRuntimeException(
          "Only BiologicalListFilter can filter BiologicalList");

    return filter((DesignFilter) filter);
  }

  /**
   * Apply a filter on the design.
   * @param filter Filter to apply
   * @return a new design
   */
  public Design filter(final DesignFilter filter) {

    if (filter == null)
      return null;

    return filter.filter(this);
  }

  /**
   * Count the entries of the bioAssay that pass the filter.
   * @param filter Filter to apply
   * @return the number of entries that pass the filter
   */
  public int count(final BiologicalFilter filter) {

    if (filter == null)
      return 0;

    if (!(filter instanceof DesignFilter))
      throw new NividicRuntimeException(
          "Only BioAssayfilter can filter BioAssay");

    return count((DesignFilter) filter);
  }

  /**
   * Get the history of the biological object.
   * @return The history object of the object
   */
  public History getHistory() {

    return this.history;
  }

  /**
   * Copy the BioAssay Object.
   * @return a copy of the biological object
   */
  public BioAssay copy() {

    throw new NividicRuntimeException("copy() is not yet implemented.");
  }

  /**
   * Clear the biological object.
   */
  public void clear() {

    this.name.clear();
    this.annotation.clear();
    this.history.clear();
    this.samples.clear();

    this.slidesOrder.clear();
    this.slides.clear();
    this.slidesReverse.clear();
    this.labels.clear();
    this.labelsOrder.clear();
    this.targets.clear();
    this.sources.clear();
    this.formats.clear();
    this.bioassays.clear();
    this.descriptionsFields.clear();
    this.descriptionsOrder.clear();
    this.descriptionData.clear();
    this.countSlides = 0;
    this.countLabels = 0;
  }

  /**
   * Get the size of the biological object.
   * @return The size of the biological object
   */
  public int size() {

    return getSlideCount();
  }

  private void addConstructorHistoryEntry() {

    final HistoryEntry entry = new HistoryEntry("Create Design (#"
        + getBiologicalId() + ")", HistoryActionType.CREATE, "SlideNumbers="
        + getSlideCount() + ";LabelNumber=" + getLabelCount(),
        HistoryActionResult.PASS);

    getHistory().add(entry);
  }

  /**
   * Overides ToString
   * @return a String with the name of the design
   */
  public String toString() {

    return "Design: " + getName();
  }

  //
  // Constructor
  //

  /**
   * Public constructor.
   */
  public DesignImpl() {

    addConstructorHistoryEntry();
  }

}
