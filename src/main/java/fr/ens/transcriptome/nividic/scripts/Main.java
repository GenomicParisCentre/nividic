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

package fr.ens.transcriptome.nividic.scripts;

import java.io.IOException;

import fr.ens.transcriptome.nividic.Init;
import fr.ens.transcriptome.nividic.Settings;
import fr.ens.transcriptome.nividic.js.JavascriptScriptEngine;
import fr.ens.transcriptome.nividic.util.StringUtils;

/**
 * Main class to launch scripts.
 * @author Laurent Jourdren
 */
public class Main {

  //
  // Main method
  //

  /**
   * Main class for launching interpreter.
   * @param args command line arguments
   */
  public static void main(final String[] args) {

    String scriptPath = null;
    String filetoExecute = null;
    String[] finalArgs = null;

    if (args != null && args.length > 0) {

      scriptPath = args[0];

      if (args.length > 1)
        filetoExecute = args[1];

      if (args.length > 2) {

        finalArgs = new String[args.length - 2];
        for (int i = 2; i < args.length; i++)
          finalArgs[i - 2] = args[i];
      }

    }

    /*
     * System.out.println("script path: " + scriptPath);
     * System.out.println("fileToExecute: " + filetoExecute);
     * System.out.println("Args: " + Arrays.toString(finalArgs));
     */

    try {
      Settings.getSettings().loadSettings();
    } catch (IOException e) {
      System.err.println("can't read configuration file. ("
          + e.getMessage() + ")");
    }

    final String extension = StringUtils.getFileExtension(filetoExecute);

    // Load additionnal modules if needed
    Init.init();

    final NividicScriptEngine engine;

    if (".js".equals(extension))
      engine = new JavascriptScriptEngine(scriptPath, filetoExecute, finalArgs);
    else if (".py".equals(extension))
      engine = new JythonScriptEngine(scriptPath, filetoExecute, finalArgs);
    else if (".rb".equals(extension))
      engine = new JRubyScriptEngine(scriptPath, filetoExecute, finalArgs);
    else
      // By default open Javascript shell
      new JavascriptScriptEngine().shell(System.in, System.out, System.err);
  }

}
