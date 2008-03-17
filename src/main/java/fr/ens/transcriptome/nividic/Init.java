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

import java.net.URL;
import java.net.URLStreamHandlerFactory;

/**
 * This class load additional module if exists.
 * @author Laurent Jourdren
 */
public class Init {

  public static void init() {

    // Load SGDB specific format
    try {

      Class sgdbBioFormatRegisteryInit =
          Class.forName("fr.ens.transcriptome.nividic.sgdb.io."
              + "SGDBBioAssayFormatRegisteryInit");

      if (sgdbBioFormatRegisteryInit != null)
        sgdbBioFormatRegisteryInit.newInstance();

    } catch (ClassNotFoundException e) {

    } catch (InstantiationException e) {

      e.printStackTrace();
    } catch (IllegalAccessException e) {

      e.printStackTrace();
    }

    // Add URL Handler for sgdb protocol
    try {

      Class sgdbProtocolFactory =
          Class.forName("fr.ens.transcriptome.nividic.sgdb.io."
              + "SGDBProtocolFactory");

      if (sgdbProtocolFactory != null)
        URL
            .setURLStreamHandlerFactory((URLStreamHandlerFactory) sgdbProtocolFactory
                .newInstance());

    } catch (ClassNotFoundException e) {

    } catch (InstantiationException e) {

      e.printStackTrace();
    } catch (IllegalAccessException e) {

      e.printStackTrace();
    }

  }

}
