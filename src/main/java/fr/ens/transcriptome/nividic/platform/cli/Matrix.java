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

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Properties;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.log4j.PropertyConfigurator;

import fr.ens.transcriptome.nividic.Globals;
import fr.ens.transcriptome.nividic.om.io.NividicIOException;
import fr.ens.transcriptome.nividic.sgdb.lims.LimsConnection;

public final class Matrix {

  /**
   * Create the Options object for the command line.
   * @return The Option object
   */
  private static Options makeOptions() {

    Option help = new Option("help", "show this message");
    Option licence = new Option("licence",
        "show information about software licence");
    Option about = new Option("about", "show information this software");
    Option withA = new Option("intensities",
        "add intensities to the output matrix");
    Option stdout = new Option("stdout", "print the output matrix on stdout");
    Option descriptions = new Option("descriptions",
        "add descriptions to the row of the matrix");
    Option mergeReplicate = new Option("mergeReplicate",
        "merge replicate for each file");
    Option probeids = new Option("probeIds", "replace all spotIds by probeIds");
    Option emptyIdentifiers = OptionBuilder.withArgName("emptyIdentifiers")
        .hasArg().withDescription("use given file for log").create(
            "emptyIdentifiers");

    Options options = new Options();
    options.addOption(help);
    options.addOption(licence);
    options.addOption(about);
    options.addOption(withA);
    options.addOption(stdout);
    options.addOption(descriptions);
    options.addOption(mergeReplicate);
    options.addOption(probeids);
    options.addOption(emptyIdentifiers);

    return options;
  }

  /**
   * Show information about this application.
   */
  private static void about() {
    System.out.println(Globals.APP_NAME + " version " + Globals.VERSION + " : "
        + Globals.APP_DESCRITPION);
    System.exit(0);
  }

  /**
   * Show information about the software licence.
   */
  private static void licence() {
    System.out.println("This software is developed under " + Globals.LICENCE);

    BufferedReader br = new BufferedReader(new InputStreamReader(Main.class
        .getClassLoader().getResourceAsStream("META-INF/LICENCE.txt")));
    String line = null;
    try {
      while ((line = br.readLine()) != null)
        System.out.println(line);
      
      br.close();
    } catch (IOException e) {
      System.err.println("Error while reading the README.txt resource");
    }
    System.out.println();
    System.exit(0);
  }

  private static void printHelp(final Options options) {
    HelpFormatter formatter = new HelpFormatter();
    formatter.printHelp("matrix inputfiles outputfile", options);
  }

  private static void initLog() {
    Properties conf = new Properties();
    conf.put("log4j.rootLogger", "ERROR" + ", R");
    conf.put("log4j.appender.R", "org.apache.log4j.varia.NullAppender");

    System.out.println("Before configurator");
    PropertyConfigurator.configure(conf);

  }

  //
  // Main method.
  //

  /**
   * Main method.
   * @param args Command line arguments
   */
  public static void main(final String[] args) {

    boolean stdout = false;
    MatrixCreator mc = new MatrixCreator();

    mc.setLimsConnection(new LimsConnection(
        "http://genomic.ens.fr/lims/webservices/lims.php"));

    Options options = makeOptions();
    CommandLineParser parser = new GnuParser();
    try {
      // parse the command line arguments
      CommandLine line = parser.parse(options, args);
      if (line.hasOption("help")) { // Show help message
        printHelp(options);
        System.exit(0);
      }

      initLog();

      if (line.hasOption("about"))
        about();
      if (line.hasOption("licence"))
        licence();

      if (line.hasOption("intensities"))
        mc.setWithIntensities(true);

      if (line.hasOption("descriptions"))
        mc.setWithDescriptions(true);

      if (line.hasOption("stdout"))
        stdout = true;

      if (line.hasOption("mergeReplicate"))
        mc.setMergeReplicate(true);

      if (line.hasOption("probeIds"))
        mc.setReplaceSpotIdsByProbeIds(true);

      if (line.hasOption("emptyIdentifier")) {

        String s = line.getOptionValue("emptyIdentifier");
        if (s != null) {
          String[] ids = s.split(",");
          for (int i = 0; i < ids.length; i++)
            mc.addEmptyIdentifier(ids[i].trim());
        }
      }

      String[] filenames = line.getArgs();

      OutputStream os;

      if (stdout) {

        if (filenames == null || filenames.length == 0) {
          System.err.println("Error: no files.");
          printHelp(options);

          System.exit(1);
        }

        for (int i = 0; i < filenames.length; i++)
          mc.addGPR(filenames[i]);

        os = System.out;
      } else {

        if (filenames == null || filenames.length < 2) {
          System.err.println("Error: no files.");
          printHelp(options);

          System.exit(1);
        }

        for (int i = 0; i < filenames.length - 1; i++)
          mc.addGPR(filenames[i]);

        os = new FileOutputStream(filenames[filenames.length - 1]);
      }

      mc.createMatrix(os);

    } catch (ParseException e) {
      System.err.println("Error analysing command line: " + e);
      System.exit(1);
    } catch (FileNotFoundException e) {
      System.err.println("File not found: " + e.getMessage());
      System.exit(1);
    } catch (NividicIOException e) {
      System.err.println("Error while creating the matrix: " + e.getMessage());
      System.exit(1);
    } catch (IOException e) {
      System.err.println("Error while witring the matrix: " + e.getMessage());
      System.exit(1);
    }

  }

  //
  // Constructor
  //

  /**
   * Private constructor.
   */
  private Matrix() {
  }

}
