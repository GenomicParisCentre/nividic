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

package fr.ens.transcriptome.nividic.sgdb.io;

import fr.ens.transcriptome.nividic.om.BioAssay;
import fr.ens.transcriptome.nividic.om.io.FieldNameConverter;

/**
 * This class extends ConvertFieldNames for total summary files.
 * @author Laurent Jourdren
 */
public class TotalSummaryConverterFieldNames extends FieldNameConverter {

  /**
   * Public constructor.
   */
  public TotalSummaryConverterFieldNames() {

    add(BioAssay.FIELD_NAME_ID, "Name");
    add(BioAssay.FIELD_NAME_A, "medianA");
    add(BioAssay.FIELD_NAME_M, "medianMnorm");
    add(BioAssay.FIELD_NAME_STD_DEV_M, "SDMnorm");
  }

}