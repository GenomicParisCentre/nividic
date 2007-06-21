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

import java.util.Iterator;
import java.util.regex.Pattern;

import fr.ens.transcriptome.nividic.om.filters.BiologicalListFilter;

/**
 * This interface defines a biological list.
 * @author Laurent Jourdren
 */
public interface BiologicalList extends BiologicalObject {

  /**
   * Add an element.
   * @param element Element to add
   */
  void add(final String element);

  /**
   * Add elements in a string array.
   * @param list array of strings to add
   */
  void add(final String[] list);

  /**
   * Add elements of a list.
   * @param list List to add.
   */
  void add(final BiologicalList list);

  /**
   * Remove an element.
   * @param element Element to remove
   */
  void remove(final String element);

  /**
   * Remove an element.
   * @param list Elements to remove
   */
  void remove(final String[] list);

  /**
   * Remove an element.
   * @param list Elements to remove
   */
  void remove(final BiologicalList list);

  /**
   * Clear the list.
   */
  void clear();

  /**
   * Get an iterator over elements in the list.
   * @return A iterator over elements in the list
   */
  Iterator iterator();

  /**
   * Get the size of the list.
   * @return The size of the list
   */
  int size();

  /**
   * Get an array of the elements of the list.
   * @return An array of the elements of the list
   */
  String[] toArray();

  /**
   * Test if the list contains an element.
   * @param element Element to test
   * @return true if the list contains the element
   */
  boolean contains(String element);

  /**
   * Concat the current list and another list in a new list
   * @param list Other list to concat
   * @return an new list with the element of the two lists
   */
  BiologicalList concat(BiologicalList list);

  /**
   * Create an new list with the elements which are in the two lists.
   * @param list Second list
   * @return An an new list with the element which are in the two lists
   */
  BiologicalList include(BiologicalList list);

  /**
   * Create an new list with the elements which aren't in the argument list but
   * are in the object.
   * @param list Second list
   * @return An an new list with the element which are in the two lists
   */
  BiologicalList exclude(BiologicalList list);

  /**
   * Create an new list with the elements which aren't in the argument list but
   * are in the object.
   * @param list Second list
   * @return An an new list with the element which are in the two lists
   */
  BiologicalList excludeAllLists(BiologicalList list);

  /**
   * Apply a filter on the list.
   * @param filter Filter to apply
   * @return a new Biological list
   */
  BiologicalList filter(BiologicalListFilter filter);

  /**
   * Filter the member of the list with a regular expression.
   * @param regex Regex to use
   * @return a new Biological list
   */
  BiologicalList grep(String regex);

  /**
   * Filter the member of the list with a regular expression.
   * @param pattern Compiled regex to use
   * @return a new Biological list
   */
  BiologicalList grep(Pattern pattern);

  /**
   * Filter the member of the list with a defined prefix.
   * @param prefix Prefix to use
   * @return a new Biological list
   */
  BiologicalList startsWith(String prefix);

  /**
   * Filter the member of the list with a defined suffix.
   * @param suffix Suffix to use
   * @return a new Biological list
   */
  BiologicalList endsWith(String suffix);

  /**
   * Transform the members of the list to lower case.
   * @return a new Biological list
   */
  BiologicalList toLowerCase();

  /**
   * Transform the members of the list to upper case.
   * @return a new Biological list
   */
  BiologicalList toUpperCase();

  /**
   * Trim all the member of a list.
   * @return a new Biological list
   */
  BiologicalList trim();

  /**
   * Remove number of the identifers (e.g. toto#6 become toto).
   * @return a new Biological list
   */
  BiologicalList removeIdentifersNumbers();

  /**
   * Test if two list are equals.
   * @param list List to test
   * @return true if the two list are equals
   */
  boolean equals(BiologicalList list);
}