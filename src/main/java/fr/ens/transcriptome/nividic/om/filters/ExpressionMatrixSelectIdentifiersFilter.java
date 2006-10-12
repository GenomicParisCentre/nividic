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

package fr.ens.transcriptome.nividic.om.filters;

import fr.ens.transcriptome.nividic.om.translators.Translator;

/**
 * This class implements a filter using rows identifier as a filter.
 * @author Laurent Jourdren
 */
public class ExpressionMatrixSelectIdentifiersFilter extends
    ExpressionMatrixIdentifersFilter {

  /**
   * Test if filtered identifiers must be removed.
   * @return true if filtered row must be removed
   */
  public boolean removeFoundId() {

    return false;
  }

  //
  // Constructor
  //

  /**
   * Default constructor.
   */
  public ExpressionMatrixSelectIdentifiersFilter() {

    setFilterNull(true);
  }

  /**
   * Default constructor.
   * @param translator to use
   */
  public ExpressionMatrixSelectIdentifiersFilter(final Translator translator) {

    this();
    setTranslator(translator);
  }

  /**
   * Default constructor.
   * @param id Identifier to add to filter list
   * @param translator Translator
   */
  public ExpressionMatrixSelectIdentifiersFilter(final Translator translator,
      final String id) {

    this(translator);
    this.add(id);
  }

  /**
   * Default constructor.
   * @param id Identifier to add to filter list
   */
  public ExpressionMatrixSelectIdentifiersFilter(final String id) {

    this();
    this.add(id);
  }

  /**
   * Default constructor.
   * @param ids Identifiers to add to filter list
   * @param translator Translator
   */
  public ExpressionMatrixSelectIdentifiersFilter(final Translator translator,
      final String[] ids) {

    this(translator);
    this.add(ids);
  }

  /**
   * Default constructor.
   * @param ids Identifiers to add to filter list
   */
  public ExpressionMatrixSelectIdentifiersFilter(final String[] ids) {

    this();
    this.add(ids);
  }
}
