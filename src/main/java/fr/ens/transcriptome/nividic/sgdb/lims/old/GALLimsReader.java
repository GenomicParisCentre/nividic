/*
 *                      Romde development code
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
 * For more information on the Romde project and its aims,
 * or to join the Romde mailing list, visit the home page
 * at:
 *
 *      http://www.transcriptome.ens.fr/romde
 *
 */

package fr.ens.transcriptome.nividic.sgdb.lims.old;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.rmi.RemoteException;
import java.util.zip.GZIPInputStream;

import javax.xml.rpc.ServiceException;

import org.apache.commons.codec.binary.Base64;

import fr.ens.transcriptome.nividic.om.io.GALReader;
import fr.ens.transcriptome.nividic.om.io.NividicIOException;
import fr.ens.transcriptome.nividic.sgdb.lims.old.ws.LimsPort_PortType;
import fr.ens.transcriptome.nividic.sgdb.lims.old.ws.LimsService;
import fr.ens.transcriptome.nividic.sgdb.lims.old.ws.LimsServiceLocator;

public class GALLimsReader extends GALReader {

  private static InputStream getArrayListLimsInputStream(final int listId)
      throws NividicIOException {

    // Make a service
    LimsService service = new LimsServiceLocator();

    // Now use the service to get a stub which implements the SDI.
    try {
      LimsPort_PortType port = service.getLimsPort();

      final String stringData = port.getArrayListFile(listId);

      if (stringData == null)
        throw new NividicIOException("No data to read");

      final byte[] byteData = Base64.decodeBase64(stringData
          .getBytes("US-ASCII"));

      return new GZIPInputStream(new ByteArrayInputStream(byteData));

    } catch (ServiceException e) {
      throw new NividicIOException("Can't reach the sgdb lims server");
    } catch (RemoteException e) {
      throw new NividicIOException("Error during the connection to the server");
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return null;
  }

  public GALLimsReader(int listId) throws NividicIOException {
    super(getArrayListLimsInputStream(listId));
  }

}
