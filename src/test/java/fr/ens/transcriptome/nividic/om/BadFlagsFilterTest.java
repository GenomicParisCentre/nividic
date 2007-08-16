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

package fr.ens.transcriptome.nividic.om;

import java.io.InputStream;

import junit.framework.TestCase;
import fr.ens.transcriptome.nividic.om.BioAssay;
import fr.ens.transcriptome.nividic.om.BioAssayRuntimeException;
import fr.ens.transcriptome.nividic.om.filters.BioAssayBadFlagsFilter;
import fr.ens.transcriptome.nividic.om.filters.BioAssayFilter;
import fr.ens.transcriptome.nividic.om.io.InputStreamBioAssayReader;
import fr.ens.transcriptome.nividic.om.io.GPRReader;
import fr.ens.transcriptome.nividic.om.io.NividicIOException;

/**
 * @author Laurent Jourdren
 */
public class BadFlagsFilterTest extends TestCase {

  public void test() throws NividicIOException, BioAssayRuntimeException {

    String file1 = "/files/testGPR3" + ".gpr";

    InputStream is = this.getClass().getResourceAsStream(file1);
    assertNotNull(is);

    InputStreamBioAssayReader bar1 = new GPRReader(is);
    bar1.addAllFieldsToRead();

    BioAssay b = bar1.read();

    BioAssayFilter baf = new BioAssayBadFlagsFilter();
    BioAssay b2 = baf.filter(b);

    int[] flags = b2.getFlags();

    for (int i = 0; i < flags.length; i++) {
      if (flags[i] < 0)
        assertTrue(false);
    }

  }

}