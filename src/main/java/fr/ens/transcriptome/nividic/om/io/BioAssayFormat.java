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

package fr.ens.transcriptome.nividic.om.io;

import java.io.InputStream;
import java.io.OutputStream;

import fr.ens.transcriptome.nividic.sgdb.io.TotalSummaryReader;
import fr.ens.transcriptome.nividic.sgdb.io.TotalSummaryWriter;

/**
 * Define an enum for the BioAssay files format.
 * @author Laurent Jourdren
 */
public enum BioAssayFormat {

  GPR("GPR", "Genepix Result File", ".gpr", true), GAL("GAL",
      "Genepix Array List File", ".gal", false), IDMA("IDMA",
      "Goulphar IDMA File", ".gal", true), TOTALSUMMARY("TOTALSUMMARY",
      "Goulphar Total Summary result File", ".txt", true), IMAGENE("IMAGENE",
      "Imagene Result File", ".txt", true), IMAGENE_ARRAYLIST(
      "IMAGENE_ARRAYLIST", "Imagene Array list File", ".txt", false);

  private String type;
  private String description;
  private String extension;
  private boolean result;

  //
  // Getters
  //

  /**
   * Get the description.
   * @return Returns the description
   */
  public String getDescription() {
    return description;
  }

  /**
   * Test if the format is a result file.
   * @return Returns the result
   */
  public boolean isResult() {
    return result;
  }

  /**
   * Get the short type.
   * @return Returns the type
   */
  public String getType() {
    return type;
  }

  /**
   * Get the extension of the file.
   * @return The extension of the file
   */
  public String getExtension() {
    return extension;
  }

  /**
   * Get the BioAssayReader for the format.
   * @param is InputStream to read
   * @return a BioAssayReader object to read the data
   * @throws NividicIOException if an error occurs while creating the reader
   */
  public BioAssayReader getBioAssayReader(final InputStream is)
      throws NividicIOException {

    switch (this) {

    case GAL:
      return new GALReader(is);

    case GPR:
      return new GPRReader(is);

    case IDMA:
      return new IDMAReader(is);

    case TOTALSUMMARY:
      return new TotalSummaryReader(is);

    case IMAGENE_ARRAYLIST:
      return new ImaGeneArrayListReader(is);

    default:
      return null;
    }
  }

  /**
   * Get the BioAssayWriter for the format.
   * @param os OutputStream to write
   * @return a BioAssayWriter object to write the data
   * @throws NividicIOException if an error occurs while creating the writer
   */
  public BioAssayWriter getBioAssayWriter(final OutputStream os)
      throws NividicIOException {

    switch (this) {

    case GAL:
      return new GALWriter(os);

    case GPR:
      return new GPRWriter(os);

    case IDMA:
      return new GPRWriter(os);

    case TOTALSUMMARY:
      return new TotalSummaryWriter(os);

    case IMAGENE_ARRAYLIST:
      return new ImaGeneArrayListWriter(os);

    default:
      return null;
    }
  }

  //
  // Other methods
  //

  /**
   * Get a BioAssayFormat from its type.
   * @param type Type of the BioAssayFormat
   * @return a BioAssayFormat enum
   */
  public static BioAssayFormat getBioAssayFormat(final String type) {

    if (type == null)
      return null;

    String s = type.trim();

    BioAssayFormat[] values = BioAssayFormat.values();

    for (int i = 0; i < values.length; i++)
      if (s.equalsIgnoreCase(values[i].getType()))
        return values[i];

    return null;
  }

  //
  // Constructor
  //

  BioAssayFormat(final String type, final String description,
      final String extension, final boolean result) {

    this.type = type;
    this.description = description;
    this.extension = extension;
    this.result = result;
  }

}
