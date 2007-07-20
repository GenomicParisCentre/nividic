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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.NoSuchElementException;

import org.biojava.bio.BioException;
import org.biojava.bio.seq.Sequence;
import org.biojava.bio.seq.db.HashSequenceDB;
import org.biojava.bio.seq.db.SequenceDB;
import org.biojavax.bio.seq.RichSequence;
import org.biojavax.bio.seq.RichSequenceIterator;
import org.biojavax.bio.seq.RichSequence.IOTools;

import fr.ens.transcriptome.nividic.NividicRuntimeException;

/**
 * This class define a translator to handle annotation from fasta file.
 * @author Laurent Jourdren
 */
public class FastaTranslator extends BasicTranslator {

  private static final String DEFAULT_FIELD = "Fasta";

  private SequenceDB sequenceDB = new HashSequenceDB();

  /**
   * Get an ordered list of the translator fields
   * @return an ordered list of the translator fields.
   */
  public String[] getFields() {

    return new String[] {DEFAULT_FIELD};
  }

  /**
   * Get a translation for a feature
   * @param id Identifier of the feature
   * @param field the field to get
   * @return An array with the annotation of the Feature
   */
  public String translateField(final String id, final String field) {

    if (!DEFAULT_FIELD.equals(field))
      return null;

    Sequence seq;

    try {
      seq = sequenceDB.getSequence(id);
    } catch (BioException ex) {
      seq = null;
    }

    return seq == null ? null : seq.seqString();
  }

  //
  // Constructor
  //

  /**
   * Public constructor.
   * @param file Fasta file to read
   * @throws FileNotFoundException
   * @throws BioException
   * @throws NoSuchElementException
   */
  public FastaTranslator(final File file) {

    if (file == null)
      throw new NividicRuntimeException("Fasta file is null");

    try {
      BufferedReader reader =
          new BufferedReader(new InputStreamReader(new FileInputStream(file)));

      RichSequenceIterator iterator = IOTools.readFastaDNA(reader, null);

      while (iterator.hasNext()) {

        RichSequence rs = iterator.nextRichSequence();

        this.sequenceDB.addSequence(rs);
      }
    } catch (FileNotFoundException e) {

      throw new NividicRuntimeException("Fasta file not found: "
          + file.getName());

    } catch (NoSuchElementException e) {

      throw new NividicRuntimeException("Error reading fasta file: "
          + e.getMessage());
    } catch (BioException e) {

      throw new NividicRuntimeException("Error reading fasta file: "
          + e.getMessage());
    }

  }
}
