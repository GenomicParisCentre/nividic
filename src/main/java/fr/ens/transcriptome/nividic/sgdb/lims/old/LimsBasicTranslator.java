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

package fr.ens.transcriptome.nividic.sgdb.lims.old;

import javax.xml.rpc.ServiceException;

import fr.ens.transcriptome.nividic.NividicRuntimeException;
import fr.ens.transcriptome.nividic.om.translators.BasicTranslator;
import fr.ens.transcriptome.nividic.sgdb.lims.old.ws.LimsPort_PortType;

public abstract class LimsBasicTranslator extends BasicTranslator {

  private LimsConnection limsConnection;

  //
  // Getters
  //

  /**
   * Get the lims connection.
   * @return the lims connection
   */
  public LimsConnection getLimsConnection() {

    if (this.limsConnection == null)
      this.limsConnection = new LimsConnection();

    return limsConnection;
  }

  /**
   * Get the lims port.
   * @return the lims port
   * @throws NividicRuntimeException if an error occurs while creating the port
   */
  public LimsPort_PortType getPort() {

    LimsPort_PortType port;
    try {
      port = getLimsConnection().getPort();
    } catch (ServiceException e) {
      throw new NividicRuntimeException(
          "Error while creating the connection the lims");
    }

    return port;
  }

  //
  // Setters
  //

  /**
   * Set the lims connection.
   * @param limsConnection The connection to set
   */
  public void setLimsConnection(final LimsConnection limsConnection) {
    this.limsConnection = limsConnection;
  }

}
