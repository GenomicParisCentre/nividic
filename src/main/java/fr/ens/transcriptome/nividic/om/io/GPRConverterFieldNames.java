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

package fr.ens.transcriptome.nividic.om.io;

import fr.ens.transcriptome.nividic.om.BioAssay;

/**
 * This class extends ConvertFieldNames for GPR Data.
 * @author Laurent Jourdren
 */
public class GPRConverterFieldNames extends FieldNameConverter {

  /**
   * Public constructor.
   */
  public GPRConverterFieldNames() {

    add(BioAssay.FIELD_NAME_ID, "ID");
    add(BioAssay.FIELD_NAME_DESCRIPTION, "Name");
    add(BioAssay.FIELD_NAME_GREEN, "F532 Median");
    add(BioAssay.FIELD_NAME_RED, "F635 Median");
    add(BioAssay.FIELD_NAME_FLAG, "Flags");
  }

}