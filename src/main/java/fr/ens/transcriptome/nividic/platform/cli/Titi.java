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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import fr.ens.transcriptome.nividic.om.BioAssay;
import fr.ens.transcriptome.nividic.om.BiologicalList;
import fr.ens.transcriptome.nividic.om.BiologicalListFactory;
import fr.ens.transcriptome.nividic.om.ExpressionMatrix;
import fr.ens.transcriptome.nividic.om.ExpressionMatrixFactory;
import fr.ens.transcriptome.nividic.om.SpotEmptyTester;
import fr.ens.transcriptome.nividic.om.filters.BioAssayEmptySpotFilter;
import fr.ens.transcriptome.nividic.om.filters.BioAssayFilter;
import fr.ens.transcriptome.nividic.om.filters.BioAssayReplicateFilter;
import fr.ens.transcriptome.nividic.om.filters.ExpressionMatrixFilter;
import fr.ens.transcriptome.nividic.om.filters.ExpressionMatrixRemoveIdentifiersFilter;
import fr.ens.transcriptome.nividic.om.io.IDMAReader;
import fr.ens.transcriptome.nividic.om.io.NividicIOException;
import fr.ens.transcriptome.nividic.om.translators.Translator;
import fr.ens.transcriptome.nividic.sgdb.SGDBYeastBioAssayTranslator;
import fr.ens.transcriptome.nividic.sgdb.SGDBYeastSpotEmptyTester;

public class Titi {

  public static void main(String[] args) throws NividicIOException, IOException {

    String[] emptyIndentifier = {"", "Spotting Buffer Corning",
        "Corning Pronto Universal Spotting Solution"};

    SpotEmptyTester emptySpotTester = new SGDBYeastSpotEmptyTester();

    String[][] lames = {
        /*{"lame05",
            "/home/jourdren/analyses/bertolloti/exp2/15min/normalised_chip1.txt"},
        {"lame06",
            "/home/jourdren/analyses/bertolloti/exp2/15min/normalised_chip2.txt"},
        {"lame04",
            "/home/jourdren/analyses/bertolloti/exp2/30min/normalised_chip1.txt"},
        {"lame07",
            "/home/jourdren/analyses/bertolloti/exp2/30min/normalised_chip2.txt"}, */

        /*
         * {"lame03",
         * "/home/jourdren/analyses/bertolloti/exp2/120min/normalised_chip1.txt"},
         * {"lame08",
         * "/home/jourdren/analyses/bertolloti/exp2/120min/normalised_chip2.txt.gpr"},
         */

        {"lame09",
            "/home/jourdren/analyses/bertolloti/exp3/phase2/15min/normalised_chip1.txt"},
        {"lame10",
            "/home/jourdren/analyses/bertolloti/exp3/phase2/15min/normalised_chip2.txt"},
        {"lame11",
            "/home/jourdren/analyses/bertolloti/exp3/phase2/30min/normalised_chip1.txt"},
        {"lame12",
            "/home/jourdren/analyses/bertolloti/exp3/phase2/30min/normalised_chip2.txt"},
        {"lame13",
            "/home/jourdren/analyses/bertolloti/exp3/phase2/60min/normalised_chip1.txt"},
        {"lame14",
            "/home/jourdren/analyses/bertolloti/exp3/phase2/60min/normalised_chip2.txt"}};

    ExpressionMatrix matrix = ExpressionMatrixFactory
        .createExpressionMatrix("myMatrix");
    matrix.addDimension(BioAssay.FIELD_NAME_A);

    Translator translator = null;
    BioAssayFilter emptySpotFilter = new BioAssayEmptySpotFilter();
    BioAssayFilter replicateFilter = null;

    String ids[] = null;

    for (int i = 0; i < lames.length; i++) {

      String name = lames[i][0];
      File file = new File(lames[i][1]);

      IDMAReader reader = new IDMAReader(file);
      BioAssay bioAssay = reader.read();
      bioAssay.setSpotEmptyTester(emptySpotTester);
      
      String [] descriptions = bioAssay.getDescriptions();
      
      for (int j = 0; j < descriptions.length; j++) {
        
        String desc = descriptions[j];
        
        if (desc==null) continue;
        desc=desc.trim();
        if (!("".equals(desc)) && (desc.indexOf('|')==-1)) System.out.println("find bad desc: \""+desc+"\"");
        
        
      }
      

      if (translator == null) {
        translator = new SGDBYeastBioAssayTranslator(bioAssay);
        replicateFilter = new BioAssayReplicateFilter(translator);
        // emptySpotFilter = new BioAssayRemoveIdentifiersFilter(translator,
        // emptyIndentifier);
      }

      System.out.println("BioAssay: " + bioAssay.getName() + "\t["
          + bioAssay.size() + " rows]");

      BiologicalList bl = BiologicalListFactory.createBiologicalList();
      bl.add(translator.translateField(bioAssay.getIds()));
      System.out.println("biological list: " + bl.size());
      
      bioAssay = bioAssay.filter(emptySpotFilter);

      System.out.println("BioAssay: " + bioAssay.getName() + "\t["
          + bioAssay.size() + " rows]");

      bioAssay = bioAssay.filter(replicateFilter);

      System.out.println("BioAssay: " + bioAssay.getName() + "\t["
          + bioAssay.size() + " rows]");

      ids = bioAssay.getIds();

      matrix.addBioAssay(bioAssay);

      bioAssay.setName(name);
    }

    OutputStream os = new FileOutputStream("/home/jourdren/list.txt");
    PrintWriter pw = new PrintWriter(os);

    for (int i = 0; i < ids.length; i++) {
      pw.println(translator.translateField(ids[i]));
    }
    pw.close();

    ExpressionMatrixFilter idsRemoveFilter = new ExpressionMatrixRemoveIdentifiersFilter(
        translator, emptyIndentifier);

    ExpressionMatrix filteredMatrix = matrix.filter(idsRemoveFilter);

    /*
     * String[] ids = filteredMatrix.getRowIds(); String[] orfs =
     * translator.translateField(ids); for (int i = 0; i < ids.length; i++)
     * System.out.println(ids[i] + "\t" + orfs[i]);
     */
  }
}
