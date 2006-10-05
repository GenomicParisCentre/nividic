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

package fr.ens.transcriptome.nividic.platform.cli;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

import fr.ens.transcriptome.nividic.om.BioAssay;
import fr.ens.transcriptome.nividic.om.io.BioAssayReader;
import fr.ens.transcriptome.nividic.om.io.BioAssayWriter;
import fr.ens.transcriptome.nividic.om.io.GPRReader;
import fr.ens.transcriptome.nividic.om.io.GPRWriter;
import fr.ens.transcriptome.nividic.om.io.NividicIOException;

public class Toto {

  public static void main(String[] args) throws NividicIOException, IOException {

    String[] filesToModify = {
        "/home/jourdren/analyses/bertolloti/exp2/15min/lame 5 scan 700.650.gpr",
        "/home/jourdren/analyses/bertolloti/exp2/15min/lame 6 scan 750.650.gpr",
        "/home/jourdren/analyses/bertolloti/exp2/30min/lame 4 scan 750.650.gpr",
        "/home/jourdren/analyses/bertolloti/exp2/30min/lame 7 scan 750.650.gpr",
        "/home/jourdren/analyses/bertolloti/exp2/2h/lame 3 scan 700.650.gpr",
        "/home/jourdren/analyses/bertolloti/exp2/2h/lame 8 scan 750.650.gpr"};

    String pseudoGalFile = "/home/jourdren/analyses/bertolloti/exp3/phase2/15min/lame 10 scan 680.620.gpr";

    InputStream is = new FileInputStream(pseudoGalFile);

    BioAssayReader bar = new GPRReader(is);
    BioAssay pseudoGal = bar.read();

    for (int i = 0; i < filesToModify.length; i++) {
      doIt(filesToModify[i], pseudoGal);
    }

  }

  private static void doIt(final String file, final BioAssay pseudoGal)
      throws NividicIOException, IOException {

    if (file == null || pseudoGal == null)
      return;

    System.out.println("file: " + file);

    InputStream is = new FileInputStream(file);

    BioAssayReader bar = new GPRReader(is);
    bar.addAllFieldsToRead();
    BioAssay bioAssay = bar.read();
    is.close();

    if (!Arrays.equals(bioAssay.getLocations(), pseudoGal.getLocations()))
      return;

    String outputFile = file.substring(0, file.length() - 4) + "-bis.gpr";
    System.out.println("output: " + outputFile);

    bioAssay.setIds(pseudoGal.getIds());
    bioAssay.setDescriptions(pseudoGal.getDescriptions());

    OutputStream os = new FileOutputStream(outputFile);

    BioAssayWriter baw = new GPRWriter(os);
    baw.addAllFieldsToWrite();
    baw.write(bioAssay);
    os.close();

  }

}
