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

import fr.ens.transcriptome.nividic.om.PhysicalConstants;
import fr.ens.transcriptome.nividic.om.design.ScanLabelSettings;
import fr.ens.transcriptome.nividic.om.design.ScanLabelsSettings;

class ScanLabelsSettingsImpl implements ScanLabelsSettings {

  private DesignImpl design;
  private int slideId;

  public ScanLabelSettings getGreenSettings() {

    if (!this.design.isLabel(PhysicalConstants.GREEN_FIELD))
      return null;

    return new ScanLabelSettingsImpl(this.design, this.slideId,
        PhysicalConstants.GREEN_FIELD);
  }

  public ScanLabelSettings getRedSettings() {

    if (!this.design.isLabel(PhysicalConstants.RED_FIELD))
      return null;

    return new ScanLabelSettingsImpl(this.design, this.slideId,
        PhysicalConstants.RED_FIELD);
  }

  public ScanLabelSettings getSetting(final String labelName) {

    if (!this.design.isLabel(labelName))
      return null;

    return new ScanLabelSettingsImpl(this.design, this.slideId, labelName);
  }

  //
  // Constructor
  //

  ScanLabelsSettingsImpl(final DesignImpl design, final int slideId) {

    this.design = design;
    this.slideId = slideId;
  }

}
