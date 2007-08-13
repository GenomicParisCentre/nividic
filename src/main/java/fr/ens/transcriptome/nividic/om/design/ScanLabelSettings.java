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

package fr.ens.transcriptome.nividic.om.design;

import java.util.List;

/**
 * This interface define a scan labels settings.
 * @author Laurent Jourdren
 */
public interface ScanLabelSettings {

  /** Wave length field. */
  String WAVELENGTH_FIELD = "wavelength";
  /** PMT Gain field. */
  String PMT_GAIN_FIELD = "pmt";
  /** Scan power field. */
  String SCAN_POWER_FIELD = "scanpower";
  /** Laser power field. */
  String LASER_POWER_FIELD = "laserpower";
  /** Laser on time field. */
  String LASER_ON_TIME_FIELD = "laserontime";

  /**
   * Get a setting.
   * @param settingName Field of the setting to get
   * @return a String with the setting value
   */
  String getSetting(final String settingName);

  /**
   * Get the wavelength for the label.
   * @return the wavelength for the label in nanometers
   */
  int getWaveLength();

  /**
   * Get the PMT Gain for the scan for this label.
   * @return The PMT Gain
   */
  int getPMTGain();

  /**
   * Get the scan power.
   * @return the scan power
   */
  int getScanPower();

  /**
   * Get the laser power.
   * @return the laser power
   */
  double getLaserPower();

  /**
   * Get the laser on time.
   * @return The laser on time
   */
  int getLaserOnTime();

  /**
   * Set the wavelength for the label.
   * @param waveLength the wavelength for the label in nanometers
   */
  void setWaveLength(int waveLength);

  /**
   * Set the PMT Gain for the scan for this label.
   * @param pmt The PMT Gain
   */
  void setPMTGain(int pmt);

  /**
   * Set the scan power.
   * @param power the scan power
   */
  void setScanPower(int power);

  /**
   * Set the laser power.
   * @param power the laser power
   */
  void setLaserPower(double power);

  /**
   * Set the laser on time.
   * @param value The laser on time
   */
  void setLaserOnTime(int value);

  /**
   * Get the name of the settings.
   * @return a list of strings with the setting names
   */
  List<String> getSettingsNames();

  /**
   * Set a field of the description.
   * @param settingName Field to set
   * @param value value to set
   */
  void setSetting(final String settingName, final String value);
}
