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

package fr.ens.transcriptome.nividic.sgdb.agilent;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class AnnotationUpdater {

  public static final String ENSEMBL_ANNOT_FILE = "/home/jourdren/tmp/entrez/res.csv";
  public static final String ENTREZ_ANNOT_FILE = "/home/jourdren/tmp/entrez/gene_info";
  public static final String OUTPUT_FILE = "/home/jourdren/tmp/entrez/toto.csv";

  private Map<String, Set<AnnotationEntry>> annotRead = new HashMap<String, Set<AnnotationEntry>>();
  private Map<String, AnnotationEntry> annotWrite = new HashMap<String, AnnotationEntry>();
  private Map<String, String> symbols = new HashMap<String, String>();

  public void read() throws IOException {

    BufferedReader in = new BufferedReader(new InputStreamReader(
        new FileInputStream(ENSEMBL_ANNOT_FILE)));

    String line;

    boolean first = true;

    while ((line = in.readLine()) != null) {

      if (first) {
        first = false;
        continue;
      }

      final AnnotationEntry ae = new AnnotationEntry(line);
      final String id = ae.getProbeId();

      Set<AnnotationEntry> annots;

      if (this.annotRead.containsKey(id))
        annots = annotRead.get(id);
      else {

        annots = new HashSet<AnnotationEntry>();
        annotRead.put(id, annots);
      }
      annots.add(ae);

    }

    in.close();

  }

  public void select() {

    for (String id : this.annotRead.keySet()) {

      Set<AnnotationEntry> annots = this.annotRead.get(id);

      AnnotationEntry best = null;

      for (AnnotationEntry a : annots) {

        if (best == null) {
          best = a;
          continue;
        }

        if (a.score() > best.score())
          best = a;

      }

      // if (best.score()!=11 && best.score()>2) System.out.println(best);
      this.annotWrite.put(best.getProbeId(), best);
    }

  }

  public void write() throws FileNotFoundException {

    PrintWriter pw = new PrintWriter(new FileOutputStream(OUTPUT_FILE));

    pw
        .write("#ProbeID\tGeneSymbol\tTranscriptID\tMGI\tDescription\tChromosome\tEntrezID\tEntrezSymbol\n");

    for (String id : this.annotWrite.keySet()) {

      AnnotationEntry ae = this.annotWrite.get(id);
      pw.write(ae.toString());
      pw.write("\t");

      String symbol;

      if (ae.isEntrezId())
        symbol = this.symbols.get(ae.getEntrezId());
      else
        symbol = "";

      if (symbol == null)
        symbol = "";

      /*
       * if (!symbol.equals(ae.getGeneSymbol())) { if (!("".equals(symbol) &&
       * ae.getGeneSymbol() == null)) System.out.println("mgi: " +
       * ae.getGeneSymbol() + "\tentrez: " + symbol); }
       */

      pw.write(symbol);
      pw.write("\n");

    }

    pw.close();
  }

  public void readSymbols() throws IOException {

    BufferedReader in = new BufferedReader(new InputStreamReader(
        new FileInputStream(ENTREZ_ANNOT_FILE)));

    String line;

    while ((line = in.readLine()) != null) {

      if (!line.startsWith("10090\t"))
        continue;

      String[] fields = line.split("\t");

      this.symbols.put(fields[1], fields[2]);

    }

    in.close();

  }

  public static void main(String[] args) throws IOException {

    AnnotationUpdater au = new AnnotationUpdater();

    au.read();
    au.select();
    au.readSymbols();
    au.write();
  }

}
