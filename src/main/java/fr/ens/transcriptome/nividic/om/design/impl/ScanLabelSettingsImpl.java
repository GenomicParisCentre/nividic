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

package fr.ens.transcriptome.nividic.om.design.impl;

import java.util.List;

import fr.ens.transcriptome.nividic.om.design.ScanLabelSettings;

class ScanLabelSettingsImpl implements ScanLabelSettings {

  private DesignImpl design;
  private int slideId;
  private String label;

  //
  // Setters
  //

  public int getLaserOnTime() {

    if (!this.design.isScanLabelSetting(LASER_ON_TIME_FIELD))
      return -1;

    final String slideName = this.design.getSlideName(this.slideId);

    return Integer.parseInt(this.design.getScanLabelSetting(slideName,
        this.label, LASER_ON_TIME_FIELD));
  }

  public double getLaserPower() {

    if (!this.design.isScanLabelSetting(LASER_POWER_FIELD))
      return Double.NaN;

    final String slideName = this.design.getSlideName(this.slideId);

    return Double.parseDouble(this.design.getScanLabelSetting(slideName,
        this.label, LASER_POWER_FIELD));
  }

  public int getPMTGain() {

    if (!this.design.isScanLabelSetting(PMT_GAIN_FIELD))
      return -1;

    final String slideName = this.design.getSlideName(this.slideId);

    return Integer.parseInt(this.design.getScanLabelSetting(slideName,
        this.label, PMT_GAIN_FIELD));
  }

  public int getScanPower() {

    if (!this.design.isScanLabelSetting(SCAN_POWER_FIELD))
      return -1;

    final String slideName = this.design.getSlideName(this.slideId);

    return Integer.parseInt(this.design.getScanLabelSetting(slideName,
        this.label, SCAN_POWER_FIELD));
  }

  public String getSetting(final String settingName) {

    if (!this.design.isScanLabelSetting(settingName))
      return null;

    final String slideName = this.design.getSlideName(this.slideId);

    return this.design.getScanLabelSetting(slideName, this.label, settingName);
  }

  public List<String> getSettingsNames() {

    return this.design.getScanLabelSettingsKeys();
  }

  public int getWaveLength() {

    if (!this.design.isScanLabelSetting(WAVELENGTH_FIELD))
      return -1;

    final String slideName = this.design.getSlideName(this.slideId);

    return Integer.parseInt(this.design.getScanLabelSetting(slideName,
        this.label, WAVELENGTH_FIELD));
  }

  //
  // Setters
  // 

  public void setLaserOnTime(final int value) {

    if (!this.design.isScanLabelSetting(LASER_ON_TIME_FIELD))
      this.design.addScanLabelSetting(LASER_ON_TIME_FIELD);

    final String slideName = this.design.getSlideName(this.slideId);

    this.design.setScanLabelSetting(slideName, this.label, LASER_ON_TIME_FIELD,
        "" + value);
  }

  public void setLaserPower(final double power) {

    if (!this.design.isScanLabelSetting(LASER_POWER_FIELD))
      this.design.addScanLabelSetting(LASER_POWER_FIELD);

    final String slideName = this.design.getSlideName(this.slideId);

    this.design.setScanLabelSetting(slideName, this.label, LASER_POWER_FIELD,
        "" + power);
  }

  public void setPMTGain(final int pmt) {

    if (!this.design.isScanLabelSetting(PMT_GAIN_FIELD))
      this.design.addScanLabelSetting(PMT_GAIN_FIELD);

    final String slideName = this.design.getSlideName(this.slideId);

    this.design.setScanLabelSetting(slideName, this.label, PMT_GAIN_FIELD, ""
        + pmt);
  }

  public void setScanPower(final int power) {

    if (!this.design.isScanLabelSetting(SCAN_POWER_FIELD))
      this.design.addScanLabelSetting(SCAN_POWER_FIELD);

    final String slideName = this.design.getSlideName(this.slideId);

    this.design.setScanLabelSetting(slideName, this.label, SCAN_POWER_FIELD, ""
        + power);
  }

  public void setSetting(final String settingName, final String value) {

    if (!this.design.isScanLabelSetting(settingName))
      this.design.addScanLabelSetting(settingName);

    final String slideName = this.design.getSlideName(this.slideId);

    this.design.setScanLabelSetting(slideName, this.label, settingName, value);

  }

  public void setWaveLength(final int waveLength) {

    if (!this.design.isScanLabelSetting(WAVELENGTH_FIELD))
      this.design.addScanLabelSetting(WAVELENGTH_FIELD);

    final String slideName = this.design.getSlideName(this.slideId);

    this.design.setScanLabelSetting(slideName, this.label, WAVELENGTH_FIELD, ""
        + waveLength);

  }

  //
  // Constructor
  //

  ScanLabelSettingsImpl(final DesignImpl design, final int slideId,
      final String label) {

    this.design = design;
    this.slideId = slideId;
    this.label = label;
  }

}
