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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import fr.ens.transcriptome.nividic.om.History;
import fr.ens.transcriptome.nividic.om.HistoryEntry;

/**
 * This class implements a History
 * @author Laurent Jourdren
 */
public class HistoryImpl implements History, Serializable {

  private List<HistoryEntry> entries = new ArrayList<HistoryEntry>();

  /**
   * Add an entry to the history.
   * @param entry Entry to add
   */
  public void add(final HistoryEntry entry) {

    if (entry != null)
      this.entries.add(entry);
  }

  /**
   * Add a history log to the current history.
   * @param entry Entry to add
   */
  public void add(final History history) {

    if (history == null)
      return;

    for (HistoryEntry entry : history.getEntries())
      add(entry);
  }

  /**
   * Get an entry of the history
   * @param index Index of the entry to get
   * @return The request entry or null if not exists
   */
  public HistoryEntry get(final int index) {

    return this.entries.get(index);
  }

  /**
   * Get all the entries of the history.
   * @return a List of the entries
   */
  public List<HistoryEntry> getEntries() {

    return Collections.unmodifiableList(entries);
  }

  /**
   * Get the number of entries in the history.
   * @return The number of the entries in the history
   */
  public int size() {

    return this.entries.size();
  }

  /**
   * Clear the history.
   */
  public void clear() {

    this.entries.clear();
  }

  //
  // Constructor
  //

  public HistoryImpl() {
  }

  HistoryImpl(final History history) {

    for (HistoryEntry entry : history.getEntries())
      this.entries.add(new HistoryEntry(entry));
  }

}
