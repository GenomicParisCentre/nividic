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
 * This class implements a filter to remove identifiers from a bioAssay object.
 * @author Laurent Jourdren
 */
public class BioAssayRemoveIdentifiersFilter extends BioAssayIdentifiersFilter {

  /**
   * Test if found identifiers must be removed.
   * @return true if found identifiers must be removed
   */
  public boolean removeFoundId() {

    return true;
  }

  //
  // Constructors
  //

  /**
   * Public constructor.
   */
  public BioAssayRemoveIdentifiersFilter() {

    setFilterNull(true);
  }

  /**
   * Public constructor.
   * @param translator Translator to apply
   */
  public BioAssayRemoveIdentifiersFilter(final Translator translator) {

    this();
    setTranslator(translator);
  }

  /**
   * Public constructor.
   * @param identifier Identifier to remove
   */
  public BioAssayRemoveIdentifiersFilter(final String identifier) {

    this();
    addIdentifier(identifier);
  }

  /**
   * Public constructor.
   * @param identifier Identifier to remove
   * @param translator Translator to apply
   */
  public BioAssayRemoveIdentifiersFilter(final Translator translator,
      final String identifier) {

    this(identifier);
    setTranslator(translator);
  }

  /**
   * Public constructor.
   * @param identifiers Identifiers to remove
   */
  public BioAssayRemoveIdentifiersFilter(final String[] identifiers) {

    this();
    addIdentifers(identifiers);
  }

  /**
   * Public constructor.
   * @param identifiers Identifiers to remove
   * @param translator Translator to apply
   */
  public BioAssayRemoveIdentifiersFilter(final Translator translator,
      final String[] identifiers) {

    this(identifiers);
    setTranslator(translator);
  }

}
