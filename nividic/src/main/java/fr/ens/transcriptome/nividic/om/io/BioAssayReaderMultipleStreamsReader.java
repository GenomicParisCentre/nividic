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

package fr.ens.transcriptome.nividic.om.io;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

import fr.ens.transcriptome.nividic.om.BioAssay;
import fr.ens.transcriptome.nividic.om.BioAssayUtils;

/**
 * This abstract class allow to read data to create a BioAssay Data from
 * multiple stream (e.g. Imagene files).
 * @author Laurent Jourdren
 */
public abstract class BioAssayReaderMultipleStreamsReader extends
    BioAssayReader {

  private ArrayList streams = new ArrayList();

  //
  // Abstract methods
  //

  /**
   * This abstract method allow to transform a bioassay before merging (e.g.
   * change columns names).
   * @param ba BioAssay to transform
   * @param count The number of the bioinfo in the multiples streams
   */
  public abstract void tranformBioAssayBeforeMerging(final BioAssay ba,
      final int count);

  //
  // Other methods
  //

  /**
   * Add a stream to read.
   * @param stream Stream to read.
   * @throws NividicIOException if the stream is null.
   */
  public void addStream(final InputStream stream) throws NividicIOException {

    if (stream == null)
      throw new NividicIOException("Stream is null");

    this.streams.add(stream);

  }

  /**
   * Remove a stream from the list of the stream to read.
   * @param stream Stream to remove
   */
  public void removeStream(final InputStream stream) {

    if (stream == null)
      return;

    this.streams.remove(stream);
  }

  /**
   * Read all the streams.
   * @return A BioAssay object build from the multiple streams sources
   * @throws NividicIOException if an error occurs while reading the streams
   */
  public BioAssay read() throws NividicIOException {

    if (this.streams.size() == 0)
      return null;

    BioAssay result = null;

    for (int i = 0; i < this.streams.size(); i++) {

      if (i != 0)
        super.clear();

      InputStream is = (InputStream) this.streams.get(i);
      super.setInputStream(is);
      BioAssay ba = super.read();

      tranformBioAssayBeforeMerging(ba, i);

      result = BioAssayUtils.merge(result, ba, false);

    }

    return result;
  }

  //
  // Constructor
  //

  /**
   * Public Constructor.
   */
  public BioAssayReaderMultipleStreamsReader() {
  }

}
