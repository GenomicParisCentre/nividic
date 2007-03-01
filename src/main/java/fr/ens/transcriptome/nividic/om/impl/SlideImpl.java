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

package fr.ens.transcriptome.nividic.om.impl;

import java.io.InputStream;

import fr.ens.transcriptome.nividic.NividicRuntimeException;
import fr.ens.transcriptome.nividic.datasources.DataSource;
import fr.ens.transcriptome.nividic.om.BioAssay;
import fr.ens.transcriptome.nividic.om.BioAssayUtils;
import fr.ens.transcriptome.nividic.om.Slide;
import fr.ens.transcriptome.nividic.om.io.BioAssayFormat;
import fr.ens.transcriptome.nividic.om.io.BioAssayReader;
import fr.ens.transcriptome.nividic.om.io.GPRReader;
import fr.ens.transcriptome.nividic.om.io.NividicIOException;

/**
 * This class implements a slide object.
 * @author Laurent Jourdren
 */
class SlideImpl implements Slide {

  private DesignImpl design;
  private int slideId;

  //
  // Getters
  // 

  /* (non-Javadoc)
   * @see fr.ens.transcriptome.nividic.om.exp.Slide#getName()
   */
  public String getName() {

    final String slideName = this.design.getSlideName(this.slideId);

    if (slideName == null)
      throw new NividicRuntimeException("The slide doesn't exists");

    return slideName;
  }

  /* (non-Javadoc)
   * @see fr.ens.transcriptome.nividic.om.exp.Slide#getDescription()
   */
  public SlideDescription getDescription() {

    final String slideName = this.design.getSlideName(this.slideId);

    if (slideName == null)
      throw new NividicRuntimeException("The slide doesn't exists");

    return this.design.getSlideDescription(slideName);
  }

  /* (non-Javadoc)
   * @see fr.ens.transcriptome.nividic.om.exp.Slide#getTarget(java.lang.String)
   */
  public String getTarget(final String label) {

    final String slideName = this.design.getSlideName(this.slideId);

    if (slideName == null)
      throw new NividicRuntimeException("The slide doesn't exists");

    return this.design.getTarget(slideName, label);
  }

  /* (non-Javadoc)
   * @see fr.ens.transcriptome.nividic.om.exp.Slide#getSource()
   */
  public DataSource getSource() {

    final String slideName = this.design.getSlideName(this.slideId);

    if (slideName == null)
      throw new NividicRuntimeException("The slide doesn't exists");

    return this.design.getSource(slideName);
  }

  /* (non-Javadoc)
   * @see fr.ens.transcriptome.nividic.om.exp.Slide#getSourceInfo()
   */
  public String getSourceInfo() {

    final String slideName = this.design.getSlideName(this.slideId);

    if (slideName == null)
      throw new NividicRuntimeException("The slide doesn't exists");

    return this.design.getSourceInfo(slideName);
  }

  //
  // Setters
  //

  /* (non-Javadoc)
   * @see fr.ens.transcriptome.nividic.om.exp.Slide#setName(java.lang.String)
   */
  public void setName(final String newName) {

    final String slideName = this.design.getSlideName(this.slideId);

    if (slideName == null)
      throw new NividicRuntimeException("The slide doesn't exists");

    this.design.renameSlide(slideName, newName);
  }

  /* (non-Javadoc)
   * @see fr.ens.transcriptome.nividic.om.exp.Slide#setTarget(java.lang.String, java.lang.String)
   */
  public void setTarget(final String label, final String sample) {

    final String slideName = this.design.getSlideName(this.slideId);

    if (slideName == null)
      throw new NividicRuntimeException("The slide doesn't exists");

    this.design.setTarget(slideName, label, sample);
  }

  /* (non-Javadoc)
   * @see fr.ens.transcriptome.nividic.om.exp.Slide#setSource(fr.ens.transcriptome.nividic.om.exp.DataSource)
   */
  public void setSource(final DataSource source) {

    final String slideName = this.design.getSlideName(this.slideId);

    if (slideName == null)
      throw new NividicRuntimeException("The slide doesn't exists");

    this.design.setSource(slideName, source);
  }

  /* (non-Javadoc)
   * @see fr.ens.transcriptome.nividic.om.exp.Slide#setSource(java.lang.String)
   */
  public void setSource(final String filename) {

    final String slideName = this.design.getSlideName(this.slideId);

    if (slideName == null)
      throw new NividicRuntimeException("The slide doesn't exists");

    this.design.setSource(slideName, filename);
  }

  /* (non-Javadoc)
   * @see fr.ens.transcriptome.nividic.om.exp.Slide#setBioAssay(fr.ens.transcriptome.nividic.om.BioAssay)
   */
  public void setBioAssay(final BioAssay bioassay) {

    final String slideName = this.design.getSlideName(this.slideId);

    if (slideName == null)
      throw new NividicRuntimeException("The slide doesn't exists");

    this.design.setBioAssay(slideName, bioassay);
  }

  /* (non-Javadoc)
   * @see fr.ens.transcriptome.nividic.om.exp.Slide#getBioAssay()
   */
  public BioAssay getBioAssay() {

    final String slideName = this.design.getSlideName(this.slideId);

    if (slideName == null)
      throw new NividicRuntimeException("The slide doesn't exists");

    return this.design.getBioAssay(slideName);
  }

  /* (non-Javadoc)
   * @see fr.ens.transcriptome.nividic.om.exp.Slide#getFormat()
   */
  public BioAssayFormat getFormat() {

    final String slideName = this.design.getSlideName(this.slideId);

    if (slideName == null)
      throw new NividicRuntimeException("The slide doesn't exists");

    return this.design.getFormat(slideName);
  }

  /* (non-Javadoc)
   * @see fr.ens.transcriptome.nividic.om.exp.Slide#setSourceFormat(java.lang.String)
   */
  public void setSourceFormat(final String format) {

    final String slideName = this.design.getSlideName(this.slideId);

    if (slideName == null)
      throw new NividicRuntimeException("The slide doesn't exists");

    this.design.setSourceFormat(slideName, format);
  }

  /* (non-Javadoc)
   * @see fr.ens.transcriptome.nividic.om.exp.Slide#setSourceFormat(fr.ens.transcriptome.nividic.om.io.BioAssayFormat)
   */
  public void setSourceFormat(final BioAssayFormat format) {

    final String slideName = this.design.getSlideName(this.slideId);

    if (slideName == null)
      throw new NividicRuntimeException("The slide doesn't exists");

    this.design.setSourceFormat(slideName, format);
  }

  //
  // Other methods
  //

  /* (non-Javadoc)
   * @see fr.ens.transcriptome.nividic.om.exp.Slide#loadSource()
   */
  public void loadSource() throws NividicIOException {

    final DataSource ds = getSource();

    if (ds == null)
      return;

    InputStream is = ds.getInputStream();

    final BioAssayFormat format = getFormat();

    BioAssayReader reader;

    switch (format) {

    case GPR:
    default:
      reader = new GPRReader(is);

      break;
    }

    BioAssay result = reader.read();
    result.setName(getName());

    setBioAssay(result);
  }

  /* (non-Javadoc)
   * @see fr.ens.transcriptome.nividic.om.exp.Slide#swapSlide()
   */
  public void swapSlide() {

    final BioAssay ba = getBioAssay();

    if (ba == null)
      throw new NullPointerException("bioAssay is null");

    if (getDescription().getSwap())
      BioAssayUtils.swap(ba);
  }

  //
  // Constructor
  //

  SlideImpl(final DesignImpl design, final int slideId) {

    this.design = design;
    this.slideId = slideId;
  }

}
