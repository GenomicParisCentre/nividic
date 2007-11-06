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

package fr.ens.transcriptome.nividic.om.translators;

import java.io.Serializable;

/**
 * This class define a translator that add commons links information.
 * @author Laurent Jourdren
 */
public class CommonLinksInfoTranslator extends BasicTranslator implements
    Serializable {

  static final long serialVersionUID = -6974613812819684107L;

  private Translator translator;

  /**
   * Get an ordered list of the translator fields
   * @return an ordered list of the translator fields.
   */
  public String[] getFields() {

    return this.translator.getFields();
  }

  /**
   * Get a translation for a feature
   * @param id Identifier of the feature
   * @param field the field to get
   * @return An array with the annotation of the Feature
   */
  public String translateField(final String id, final String field) {

    return this.translator.translateField(id, field);
  }

  /**
   * Test if the link information is available for the field
   * @param field Field to test
   * @return true if link information is available
   */
  public boolean isLinkInfo(final String field) {

    if (field == null)
      return false;

    return field.equals("TranscriptID")
        || field.equals("MGI") || field.equals("EntrezID")
        || field.equals("SGDID") || field.equals("Phatr2 Protein HyperLink");
  }

  /**
   * Get link information.
   * @param translatedId Translated id
   * @param field field of the id
   * @return a link for the translated id
   */
  public String getLinkInfo(final String translatedId, final String field) {

    if (translatedId == null || field == null)
      return null;

    if (field.equals("TranscriptID"))
      return "http://www.ensembl.org/Homo_sapiens/searchview?species=;idx=;q="
          + translatedId;

    if (field.equals("MGI") && translatedId.startsWith("MGI:")) {

      final String id = translatedId.substring(4, translatedId.length());

      return "http://www.informatics.jax.org/searches/accession_report.cgi?id=MGI%3A"
          + id;
    }

    if (field.equals("EntrezID"))
      return "http://www.ncbi.nlm.nih.gov/entrez/query.fcgi?"
          + "db=gene&cmd=Retrieve&dopt=Graphics&list_uids=" + translatedId;

    if (field.equals("SGDID")) {
      return "http://db.yeastgenome.org/cgi-bin/locus.pl?dbid=" + translatedId;
    }

    if (field.equals("Phatr2 Protein HyperLink")) {
      return "http://genome.jgi-psf.org/cgi-bin/dispGeneModel?db=Phatr2&tid="
          + translatedId;
    }

    return null;
  }

  /**
   * Get the available identfiers by the translator if possible.
   * @return a array of string with the identifiers
   */
  public String[] getIds() {

    return this.translator.getIds();
  }

  //
  // Constructor
  //

  /**
   * Public constructor.
   * @param translator Translator to use
   */
  public CommonLinksInfoTranslator(final Translator translator) {

    if (translator == null)
      throw new NullPointerException("Translator can't be null");

    this.translator = translator;
  }
}
