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

import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * This class define a registery for BioAssayFormat objects.
 * @author Laurent Jourdren
 */
public final class BioAssayFormatRegistery {

  private static Map<String, BioAssayFormat> formats =
      new HashMap<String, BioAssayFormat>();

  private static final String MAGIC_IDMA = "ID\tName\tR\tRb\tG\tGb\tMnorm\tA";
  private static final String MAGIC_IDMA_QUOTES =
      "\"ID\"\t\"Name\"\t\"R\"\t\"Rb\"\t\"G\"\t\"Gb\"\t\"Mnorm\"\t\"A\"";
  private static final String MAGIC_IMAGENE = "Begin Header";
  private static final String MAGIC_AGILENT = "TYPE\t";

  private static final String MAGIC_ATF = "ATF";
  private static final String MAGIC_GPR = "Type=GenePix Results";
  private static final String MAGIC_GAL = "Type=GenePix ArrayList";

  /** GAL BioAssayFormat. */
  public static final BioAssayFormat GAL_BIOASSAY_FORMAT =
      new BioAssayFormat("GAL", "Genepix Array List File", ".gal", false) {

        @Override
        public InputStreamBioAssayReader getBioAssayReader(final InputStream is)
            throws NividicIOException {

          return new GALReader(is);
        }

        @Override
        public BioAssayWriter getBioAssayWriter(final OutputStream os)
            throws NividicIOException {

          return new GALWriter(os);

        }

        @Override
        public boolean testFormat(final String firstLines) {

          if (firstLines.startsWith(MAGIC_ATF)) {

            String[] lines = firstLines.split("\n");

            for (String l : lines)
              if (l.contains(MAGIC_GAL))
                return true;

          }
          return false;
        }
      };
  /** GPR BioAssayFormat. */
  public static final BioAssayFormat GPR_BIOASSAY_FORMAT =
      new BioAssayFormat("GPR", "Genepix Result File", ".gpr", true) {

        @Override
        public InputStreamBioAssayReader getBioAssayReader(final InputStream is)
            throws NividicIOException {

          return new GPRReader(is);
        }

        @Override
        public BioAssayWriter getBioAssayWriter(final OutputStream os)
            throws NividicIOException {

          return new GPRWriter(os);
        }

        @Override
        public boolean testFormat(final String firstLines) {

          if (firstLines.startsWith(MAGIC_ATF)) {

            String[] lines = firstLines.split("\n");

            for (String l : lines)
              if (l.contains(MAGIC_GPR))
                return true;
          }
          return false;
        }
      };
  /** IDMA BioAssayFormat. */
  public static final BioAssayFormat IDMA_BIOASSAY_FORMAT =
      new BioAssayFormat("IDMA", "Goulphar IDMA File", ".idma", true) {

        @Override
        public InputStreamBioAssayReader getBioAssayReader(final InputStream is)
            throws NividicIOException {

          return new IDMAReader(is);
        }

        @Override
        public BioAssayWriter getBioAssayWriter(final OutputStream os)
            throws NividicIOException {

          return new IDMAWriter(os);

        }

        @Override
        public boolean testFormat(final String firstLines) {

          return firstLines.startsWith(MAGIC_IDMA)
              || firstLines.startsWith(MAGIC_IDMA_QUOTES);
        }
      };

  /** ImaGene BioAssayFormat. */
  public static final BioAssayFormat IMAGENE_BIOASSAY_FORMAT =
      new BioAssayFormat("IMAGENE", "Imagene Result File", ".txt", true) {

        @Override
        public InputStreamBioAssayReader getBioAssayReader(final InputStream is)
            throws NividicIOException {

          return null;
        }

        @Override
        public BioAssayWriter getBioAssayWriter(final OutputStream os)
            throws NividicIOException {

          return null;
        }

        @Override
        public boolean testFormat(final String firstLines) {

          return firstLines.startsWith(MAGIC_IMAGENE);
        }
      };

  /** ImaGene array list BioAssayFormat. */
  public static final BioAssayFormat IMAGENE_ARRAYLIST_BIOASSAY_FORMAT =
      new BioAssayFormat("IMAGENE_ARRAYLIST", "Imagene Array list File",
          ".txt", false) {

        @Override
        public InputStreamBioAssayReader getBioAssayReader(final InputStream is)
            throws NividicIOException {

          return new ImaGeneArrayListReader(is);
        }

        @Override
        public BioAssayWriter getBioAssayWriter(final OutputStream os)
            throws NividicIOException {

          return new ImaGeneArrayListWriter(os);
        }

        @Override
        public boolean testFormat(final String firstLines) {

          return false;
        }
      };

  /** Agilent BioAssayFormat. */
  public static final BioAssayFormat AGILENT_BIOASSAY_FORMAT =
      new BioAssayFormat("AGILENT", "Agilent result file", ".txt", false) {

        @Override
        public InputStreamBioAssayReader getBioAssayReader(final InputStream is)
            throws NividicIOException {

          return new AgilentReader(is);
        }

        @Override
        public BioAssayWriter getBioAssayWriter(final OutputStream os)
            throws NividicIOException {

          return new AgilentWriter(os);

        }

        @Override
        public boolean testFormat(final String firstLines) {

          return firstLines.startsWith(MAGIC_AGILENT);
        }
      };

  static {

    addBioAssayFormat(GAL_BIOASSAY_FORMAT);
    addBioAssayFormat(GPR_BIOASSAY_FORMAT);
    addBioAssayFormat(IDMA_BIOASSAY_FORMAT);
    addBioAssayFormat(IMAGENE_BIOASSAY_FORMAT);
    addBioAssayFormat(IMAGENE_ARRAYLIST_BIOASSAY_FORMAT);
    addBioAssayFormat(AGILENT_BIOASSAY_FORMAT);

    // Load SGDB specific format

    try {

      Class sgdbBioFormatRegisteryInit =
          Class.forName("fr.ens.transcriptome.nividic.om.sgdb.io."
              + "SGDBBioAssayFormatRegisteryInit");

      if (sgdbBioFormatRegisteryInit != null)
        sgdbBioFormatRegisteryInit.newInstance();

    } catch (ClassNotFoundException e) {

    } catch (InstantiationException e) {

      e.printStackTrace();
    } catch (IllegalAccessException e) {

      e.printStackTrace();
    }

  }

  //
  // Static methods
  //

  /**
   * Add a BioAssay format.
   * @param format BioAssayFormat to add to th registery
   */
  public static void addBioAssayFormat(final BioAssayFormat format) {

    if (format == null)
      return;

    formats.put(format.getType(), format);
  }

  /**
   * Get a BioAssayFormat from its type.
   * @param type Type of the BioAssayFormat
   * @return a BioAssayFormat enum
   */
  public static BioAssayFormat getBioAssayFormat(final String type) {

    if (type == null)
      return null;

    final String s = type.trim();

    return formats.get(s);
  }

  /**
   * Get the BioAssayFormat from the first line of a stream.
   * @param firstLines first lines to test
   * @return the BioAssayFormat of the stream if it has been discovered
   */
  public static BioAssayFormat getBioAssayFormatFromFirstLines(
      final String firstLines) {

    if (firstLines == null)
      return null;

    for (BioAssayFormat format : formats.values()) {

      if (format.testFormat(firstLines))
        return format;

    }

    return null;
  }

  //
  // Constructor
  //

  private BioAssayFormatRegistery() {
  }

}
