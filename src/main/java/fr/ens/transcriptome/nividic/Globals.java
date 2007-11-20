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

package fr.ens.transcriptome.nividic;

/**
 * PhysicalConstants of the project.
 * @author Laurent Jourdren
 */
public final class Globals {

  // Debug constants

  /** Debug mode. */
  public static final boolean DEBUG = true;
  /** Default test suite list file. */
  public static final String TEST_SUITE_LIST_FILE = "tsl.xml";

  // Generals constants

  /** Name of the application. */
  public static final String APP_NAME = "Nividic";
  /** Name of the application. */
  public static final String APP_DESCRITPION =
      "A framework for DNA chips analysis";
  /** Version of the application. */
  public static final String VERSION = "0.6";
  /** Copyright. */
  // public static final String COPYRIGHT = "Copyright (c) 2003-2004 CEA - ENS";
  public static final String COPYRIGHT =
      "Copyright (c) 2004-2007 ENS Microarray platform";
  /** Authors */
  // public static final String[] AUTHORS = {"Laurent Jourdren", "Vincent
  // Frouin", "Miriame Allouche"};
  public static final String[] AUTHORS = {"Laurent Jourdren"};
  /** Web site. */
  public static final String WEBSITE = "http://transcritpome.ens.fr";
  /** Licence. */
  public static final String LICENCE = "General Public Licence";

  // Property names

  /** System property for library directory. */
  public static final String NIVIDIC_LIB_DIRECTORY_PROPERTY = "nividic.lib.dir";
  /** System property for repository directory. */
  public static final String NIVIDIC_REPOSITORY_DIRECTORY_PROPERTY =
      "nividic.repository.dir";
  /** System property for configuration directory. */
  public static final String NIVIDIC_CONFIGURATION_DIRECTORY_PROPERTY =
      "nividic.conf.dir";
  /** System property for log directory. */
  public static final String NIVIDIC_LOG_DIRECTORY_PROPERTY = "nividic.log.dir";
  /** System property for cli main class. */
  public static final String NIVIDIC_CLI_MAIN_CLASS_PROPEERTY =
      "nividic.cli.main";

  // CLI

  /** CLI default main class. */
  public static final String CLI_MAIN_CLASS =
      "fr.ens.transcriptome.nividic.platform.cli.MainCLI";
  /** CLI default library directory. */
  public static final String CLI_LIB_DIR = "lib";
  /** CLI default log directory. */
  public static final String CLI_LOG_DIR = "log";
  /** CLI default configration directory. */
  public static final String CLI_CONF_DIR = "conf";
  /** CLI default repository directory. */
  public static final String CLI_REPOSITORY_DIR = "repository";

  // Webapp

  //
  // Modules
  //

  /** Internals modules. */
  public static final Class[] INTERNALS_MODULES = {};

  /** Module user directory. */
  public static final String MODULES_USER_DIRECTORY =
      "/home/jourdren/data/modules";

  /** Module global directory. */
  public static final String MODULES_GLOBAL_DIRECTORY =
      "/usr/local/nividic/modules";

  //
  // Log system
  //

  /** Name of the default log file. */
  public static final String LOG_FILE = "nividic.log";
  /** Default log level. */
  public static final String LOG_LEVEL = "debug";

  /** Default Text file encoding. */
  public static final String DEFAULT_FILE_ENCODING = "ISO-8859-1";

  //
  // Constructor
  //

  private Globals() {
  }

}
