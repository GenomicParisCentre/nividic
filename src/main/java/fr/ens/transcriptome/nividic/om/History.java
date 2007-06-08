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

import java.util.List;

/**
 * This interface defines a History of a BiologicalObject.
 * @author Laurent Jourdren
 */
public interface History {

  /**
   * Add an entry to the history.
   * @param entry Entry to add
   */
  void add(HistoryEntry entry);

  /**
   * Add a history log to the current history.
   * @param entry Entry to add
   */
  void add(History history);
  
  /**
   * Get the number of entries in the history.
   * @return The number of the entries in the history
   */
  int size();

  /**
   * Get an entry of the history
   * @param index Index of the entry to get
   * @return The request entry or null if not exists
   */
  HistoryEntry get(int index);

  /**
   * Get all the entries of the history.
   * @return a List of the entries
   */
  List<HistoryEntry> getEntries();

  /**
   * Clear the history.
   */
  void clear();

}
