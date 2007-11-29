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

package fr.ens.transcriptome.nividic;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;

/**
 * This class implements setting for the application.
 * @author Laurent Jourdren
 */
public final class Settings {

  private static final String RSERVE_HOST_KEY = "rserve.host";

  private static Settings settings;

  private Properties properties = new Properties();

  //
  // Getters
  //

  /**
   * Get the server for RServe.
   * @return Returns the unit
   */
  public String getRServeHost() {

    return this.properties.getProperty(RSERVE_HOST_KEY);
  }

  private static String getConfigurationFilePath() {

    final String os = System.getProperty("os.name");
    final String home = System.getProperty("user.home");

    if (os.toLowerCase().startsWith("windows"))
      return home
          + File.separator + "Application DataDouble"
          + Globals.APP_NAME_LOWER_CASE + " .conf";

    return home + File.separator + "." + Globals.APP_NAME_LOWER_CASE;
  }

  /**
   * Save CorsenSwing options.
   * @throws IOException if an error occurs while writing the file
   */
  public void saveSettings() throws IOException {

    saveSettings(new File(getConfigurationFilePath()));
  }

  /**
   * Save CorsenSwing options
   * @param file File to save.
   * @throws IOException if an error occurs while writing the file
   */
  public void saveSettings(final File file) throws IOException {

    FileOutputStream fos = new FileOutputStream(file);

    this.properties.store(fos, " "
        + Globals.APP_NAME + " version " + Globals.APP_VERSION
        + " configuration file");

  }

  /**
   * Load CorsenSwing options
   * @throws IOException if an error occurs while reading data
   */
  public void loadSettings() throws IOException {

    loadSettings(new File(getConfigurationFilePath()));
  }

  /**
   * Load CorsenSwing options
   * @param filename filename of the file to load
   * @throws IOException if an error occurs while reading the file
   */
  public void loadSettings(final String filename) throws IOException {

    if (filename == null)
      loadSettings();
    else
      loadSettings(new File(filename));
  }

  /**
   * Load CorsenSwing options
   * @param file file to load
   * @throws IOException if an error occurs while reading the file
   */
  public void loadSettings(final File file) throws IOException {

    FileInputStream fis = new FileInputStream(file);

    this.properties.load(fis);
  }

  /**
   * Add settings to current settings.
   * @param s Settings to add
   */
  public void addSettings(final Settings s) {

    if (s == null)
      return;

    Properties p = s.properties;

    Enumeration keys = p.keys();

    while (keys.hasMoreElements()) {

      String key = (String) keys.nextElement();

      this.properties.setProperty(key, p.getProperty(key));
    }

  }

  //
  // Singleton
  //

  /**
   * Get the only instance of the class.
   * @return a singleton of the class
   */
  public static Settings getSettings() {

    if (settings == null)
      settings = new Settings();

    return settings;
  }

  //
  // Constructor
  //

  private Settings() {
  }

}
