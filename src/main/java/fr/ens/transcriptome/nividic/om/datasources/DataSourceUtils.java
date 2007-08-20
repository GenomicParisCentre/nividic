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

package fr.ens.transcriptome.nividic.om.datasources;

import java.net.MalformedURLException;
import java.net.URL;

import fr.ens.transcriptome.nividic.om.io.BioAssayFormat;

/**
 * This class provides utility methods for DataSource.
 * @author Laurent Jourdren
 */
public final class DataSourceUtils {

  private static final String SGDB_URL_PREFIX = "sgdb://";

  /**
   * Identify the type of the DataSource from the source.
   * @param baseDir baseDir of the source if this a file
   * @param source source to identify
   * @return a new DataSource object
   */
  public static DataSource identifyDataSource(final String baseDir,
      final String source) {

    if (source == null)
      return null;

    if (source.startsWith(SGDB_URL_PREFIX))
      return new URLDataSource(source);

    boolean malformedURL = false;

    try {
      new URL(source);
    } catch (MalformedURLException e) {
      malformedURL = true;
    }

    if (malformedURL)
      return new URLDataSource(source);

    return new FileDataSource(baseDir, source);
  }

  /**
   * Identify the format of a DataSource.
   * @param source Source which format must be identify
   * @return the BioAssayFormat of the source if found
   */
  public static BioAssayFormat identifyBioAssayFormat(final DataSource source) {

    if (source == null)
      return null;

    final String sourceInfo = source.getSourceInfo();

    if (source == null)
      return null;

    if (source.getSourceInfo().startsWith(SGDB_URL_PREFIX))
      return BioAssayFormat.GPR;

    return BioAssayFormat.getBioAssayFormat(sourceInfo);
  }

  //
  // Constructor
  //

  private DataSourceUtils() {
  }

}
