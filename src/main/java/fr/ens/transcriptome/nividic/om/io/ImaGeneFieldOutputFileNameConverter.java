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
 * This class extends ConvertFieldNames for ImaGene Data.
 * @author Laurent Jourdren
 */
public class ImaGeneFieldOutputFileNameConverter extends FieldNameConverter {

  /**
   * Public constructor.
   */
  public ImaGeneFieldOutputFileNameConverter() {

    add(BioAssay.FIELD_NAME_ID, "Gene ID");
    add(BioAssay.FIELD_NAME_FLAG, "Flag");
    // add("Dia.", "Diameter");

    // add(BioAssay.FIELD_NAME_RED, "GeneID");
    // add(BioAssay.FIELD_NAME_GREEN, "GeneID");
    // add(BioAssay.FIELD_NAME_DESCRIPTION, "Annotation1");
  }

}
