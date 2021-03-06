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

package fr.ens.transcriptome.nividic.om.impl;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.lang.builder.HashCodeBuilder;

import fr.ens.transcriptome.nividic.NividicRuntimeException;
import fr.ens.transcriptome.nividic.om.Annotation;
import fr.ens.transcriptome.nividic.om.AnnotationFactory;
import fr.ens.transcriptome.nividic.om.BiologicalList;
import fr.ens.transcriptome.nividic.om.History;
import fr.ens.transcriptome.nividic.om.HistoryEntry;
import fr.ens.transcriptome.nividic.om.HistoryEntry.HistoryActionResult;
import fr.ens.transcriptome.nividic.om.HistoryEntry.HistoryActionType;
import fr.ens.transcriptome.nividic.om.filters.BiologicalFilter;
import fr.ens.transcriptome.nividic.om.filters.BiologicalListEndsWithFilter;
import fr.ens.transcriptome.nividic.om.filters.BiologicalListFilter;
import fr.ens.transcriptome.nividic.om.filters.BiologicalListGrepFilter;
import fr.ens.transcriptome.nividic.om.filters.BiologicalListIdentifierNumberRemoverFilter;
import fr.ens.transcriptome.nividic.om.filters.BiologicalListStartsWithFilter;
import fr.ens.transcriptome.nividic.om.filters.BiologicalListToLowerCaseFilter;
import fr.ens.transcriptome.nividic.om.filters.BiologicalListTrimFilter;

/**
 * This class implement a biological list.
 * @author Laurent Jourdren
 */
public class BiologicalListImpl implements BiologicalList, Serializable {

  /** serial version for serialization. */
  static final long serialVersionUID = -8974757475594863469L;

  private static final int HASHCODE_ODD_NUMBER_1 = 812203;
  private static final int HASHCODE_ODD_NUMBER_2 = 1235;

  private BiologicalName name = new BiologicalName(this);
  private Set<String> list = new HashSet<String>();
  private Annotation annotation = AnnotationFactory.createAnnotation();
  private HistoryImpl history = new HistoryImpl();

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
   * Add an element.
   * @param element Element to add
   */
  public void add(final String element) {

    if (element == null)
      return;
    if (!this.list.contains(element))
      this.list.add(element);
  }

  /**
   * Add elements in a string array.
   * @param list array of strings to add
   */
  public void add(final String[] list) {

    if (list == null)
      return;
    for (int i = 0; i < list.length; i++)
      add(list[i]);
  }

  /**
   * Add elements of a list.
   * @param list List to add.
   */
  public void add(final BiologicalList list) {

    if (list == null)
      return;
    add(list.toArray());
  }

  /**
   * Remove an element.
   * @param element Element to remove
   */
  public void remove(final String element) {

    if (element == null)
      return;
    this.list.remove(element);
  }

  /**
   * Remove an element.
   * @param list Elements to remove
   */
  public void remove(final String[] list) {

    if (list == null)
      return;
    for (int i = 0; i < list.length; i++)
      remove(list[i]);
  }

  /**
   * Remove an element.
   * @param list Elements to remove
   */
  public void remove(final BiologicalList list) {
    if (list == null)
      return;
    remove(list.toArray());
  }

  /**
   * Clear the list.
   */
  public void clear() {
    this.list.clear();
  }

  /**
   * Get an iterator over elements in the list.
   * @return A iterator over elements in the list
   */
  public Iterator<String> iterator() {
    return this.list.iterator();
  }

  /**
   * Get the size of the list.
   * @return The size of the list
   */
  public int size() {
    return this.list.size();
  }

  /**
   * Get an array of the elements of the list.
   * @return An array of the elements of the list
   */
  public String[] toArray() {

    String[] result = new String[size()];
    this.list.toArray(result);

    return result;
  }

  /**
   * Test if the list contains an element.
   * @param element Element to test
   * @return true if the list contains the element
   */
  public boolean contains(final String element) {
    return this.list.contains(element);
  }

  /**
   * Concat the current list and another list in a new list
   * @param list Other list to concat
   * @return an new list with the element of the two lists
   */
  public BiologicalList concat(final BiologicalList list) {

    BiologicalListImpl result = new BiologicalListImpl();
    result.add(this);
    if (list != null)
      result.add(list);

    return result;
  }

  /**
   * Create an new list with the elements which are in the two lists.
   * @param list Second list
   * @return An an new list with the element which are in the two lists
   */
  public BiologicalList include(final BiologicalList list) {

    BiologicalListImpl result = new BiologicalListImpl();

    if (list == null) {
      result.add(this);
      return result;
    }

    for (final String element : this)
      if (list.contains(element))
        result.add(element);

    return result;
  }

  /**
   * Create an new list with the elements which aren't in the argument list but
   * are in the object.
   * @param list Second list
   * @return An an new list with the element which are in the two lists
   */
  public BiologicalList exclude(final BiologicalList list) {

    BiologicalListImpl result = new BiologicalListImpl();

    if (list == null) {
      result.add(this);
      return result;
    }

    for (final String element : this)
      if (!list.contains(element))
        result.add(element);

    return result;
  }

  /**
   * Create an new list with the elements which aren't in the argument list but
   * are in the object.
   * @param list Second list
   * @return An an new list with the element which are in the two lists
   */
  public BiologicalList excludeAllLists(final BiologicalList list) {

    BiologicalList result = exclude(list);

    if (list == null) {
      return result;
    }

    for (final String element : list)
      if (!list.contains(element))
        result.add(element);

    return result;
  }

  /**
   * Test if two list are equals.
   * @param list List to test
   * @return true if the two list are equals
   */
  public boolean equals(final BiologicalList list) {

    if (list == null)
      return false;

    if (list == this)
      return true;

    if (size() != list.size())
      return false;

    for (final String element : list)
      if (!contains(element))
        return false;

    return true;
  }

  /**
   * Get the annotation of the object.
   * @return An annotation object
   */
  public Annotation getAnnotation() {
    return this.annotation;
  }

  /**
   * Returns a hash code value for the list.
   * @return a hash code value for this list
   */
  public int hashCode() {

    HashCodeBuilder hcb =
        new HashCodeBuilder(HASHCODE_ODD_NUMBER_1, HASHCODE_ODD_NUMBER_2);

    for (final String element : list)
      hcb.append(element);

    return hcb.toHashCode();
  }

  /**
   * Apply a filter on the list.
   * @param filter Filter to apply
   * @return a new Biological list
   */
  public BiologicalList filter(final BiologicalFilter filter) {

    if (filter == null)
      return null;

    if (!(filter instanceof BiologicalListFilter))
      throw new NividicRuntimeException(
          "Only BiologicalListFilter can filter BiologicalList");

    return filter((BiologicalListFilter) filter);
  }

  /**
   * Apply a filter on the list.
   * @param filter Filter to apply
   * @return a new Biological list
   */
  public BiologicalList filter(final BiologicalListFilter filter) {

    if (filter == null)
      return null;

    BiologicalList result = filter.filter(this);

    if (result != null) {

      HistoryEntry entry =
          new HistoryEntry(filter.getClass().getSimpleName(),
              HistoryEntry.HistoryActionType.FILTER, filter.getParameterInfo(),
              HistoryEntry.HistoryActionResult.PASS);

      result.getHistory().add(entry);
    }

    return result;
  }

  /**
   * Count the entries of the bioAssay that pass the filter.
   * @param filter Filter to apply
   * @return the number of entries that pass the filter
   */
  public int count(final BiologicalFilter filter) {

    if (filter == null)
      return 0;

    if (!(filter instanceof BiologicalListFilter))
      throw new NividicRuntimeException(
          "Only BioAssayfilter can filter BioAssay");

    return count((BiologicalListFilter) filter);
  }

  /**
   * Count the entries of the bioAssay that pass the filter.
   * @param filter Filter to apply
   * @return the number of entries that pass the filter
   */
  public int count(final BiologicalListFilter filter) {

    if (filter == null)
      return 0;

    return filter.count(this);
  }

  /**
   * Filter the member of the list with a regular expression.
   * @param regex Regex to use
   * @return a new Biological list
   */
  public BiologicalList grep(final String regex) {

    final BiologicalListFilter filter = new BiologicalListGrepFilter(regex);

    return filter.filter(this);
  }

  /**
   * Filter the member of the list with a regular expression.
   * @param pattern Compiled regex to use
   * @return a new Biological list
   */
  public BiologicalList grep(final Pattern pattern) {

    final BiologicalListFilter filter = new BiologicalListGrepFilter(pattern);

    return filter.filter(this);
  }

  /**
   * Filter the member of the list with a defined prefix.
   * @param prefix Prefix to use
   * @return a new Biological list
   */
  public BiologicalList startsWith(final String prefix) {

    final BiologicalListFilter filter =
        new BiologicalListStartsWithFilter(prefix);

    return filter.filter(this);
  }

  /**
   * Filter the member of the list with a defined suffix.
   * @param suffix Suffix to use
   * @return a new Biological list
   */
  public BiologicalList endsWith(final String suffix) {

    final BiologicalListFilter filter =
        new BiologicalListEndsWithFilter(suffix);

    return filter.filter(this);
  }

  /**
   * Transform the members of the list to lower case.
   * @return a new Biological list
   */
  public BiologicalList toLowerCase() {

    final BiologicalListFilter filter = new BiologicalListToLowerCaseFilter();

    return filter.filter(this);
  }

  /**
   * Transform the members of the list to upper case.
   * @return a new Biological list
   */
  public BiologicalList toUpperCase() {

    final BiologicalListFilter filter = new BiologicalListToLowerCaseFilter();

    return filter.filter(this);
  }

  /**
   * Trim all the member of a list.
   * @return a new Biological list
   */
  public BiologicalList trim() {

    final BiologicalListFilter filter = new BiologicalListTrimFilter();

    return filter.filter(this);
  }

  /**
   * Remove number of the identifers (e.g. toto#6 become toto).
   * @return a new Biological list
   */
  public BiologicalList removeIdentifersNumbers() {

    final BiologicalListFilter filter =
        new BiologicalListIdentifierNumberRemoverFilter();

    return filter.filter(this);
  }

  /**
   * Get the history of the biological object.
   * @return The history object of the object
   */
  public History getHistory() {

    return this.history;
  }

  /**
   * Copy the BiologicalList Object.
   * @return a copy of the biological object
   */
  public BiologicalList copy() {

    final BiologicalList result = new BiologicalListImpl(this);

    final HistoryEntry entry =
        new HistoryEntry("Create Biological List (#"
            + result.getBiologicalId() + "), copy of #" + getBiologicalId(),
            HistoryActionType.CREATE, "Size=" + size(),
            HistoryActionResult.PASS);

    getHistory().add(entry);

    return result;
  }

  private void addConstructorHistoryEntry() {

    final HistoryEntry entry =
        new HistoryEntry("Create Biological List(#" + getBiologicalId() + ")",
            HistoryActionType.CREATE, "Size=" + size(),
            HistoryActionResult.PASS);

    getHistory().add(entry);
  }

  //
  // Constructor
  //

  private BiologicalListImpl(final BiologicalList list, final boolean addHistory) {

    add(list);
    if (addHistory)
      addConstructorHistoryEntry();
  }

  /**
   * Public constructor.
   */
  public BiologicalListImpl() {

    addConstructorHistoryEntry();
  }

  /**
   * Public constructor
   * @param list Biological list to add to the new object
   */
  public BiologicalListImpl(final BiologicalList list) {

    this(list, true);
  }

}