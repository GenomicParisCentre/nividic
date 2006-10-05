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

package fr.ens.transcriptome.nividic.util.gui;

import java.io.IOException;
import java.net.URL;

import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import fr.ens.transcriptome.nividic.util.NividicUtils;
import fr.ens.transcriptome.nividic.util.WebBrowser;


public class ExternalBrowserLinkListener implements HyperlinkListener {

  private String alternativeBrowserPath;

  //
  // Getters
  //

  /**
   * Get alternative browser p ath.
   * @return The alternative browser path
   */
  public String getAlternativeBrowserPath() {
    return alternativeBrowserPath;
  }

  //
  // Setters
  //

  /**
   * Set alternative browser path.
   * @param alternativeBrowserPath The alternative browser path
   */
  public void setAlternativeBrowserPath(final String alternativeBrowserPath) {
    this.alternativeBrowserPath = alternativeBrowserPath;
  }

  //
  // Other methods
  //

  /**
   * Called when a hypertext link is updated.
   * @param he the event responsible for the update
   */
  public void hyperlinkUpdate(final HyperlinkEvent he) {

    HyperlinkEvent.EventType type = he.getEventType();

    if (type == HyperlinkEvent.EventType.ACTIVATED) {

      URL url = he.getURL();
      try {
        WebBrowser.launch(getAlternativeBrowserPath(), url);
      } catch (IOException e) {
        NividicUtils.nop();
      }

    }
  }

  //
  // Constructors
  //

  /**
   * Public constructor.
   */
  public ExternalBrowserLinkListener() {
  }

  /**
   * Public constructor.
   * @param alternativeBrowserPath
   */
  public ExternalBrowserLinkListener(final String alternativeBrowserPath) {
    setAlternativeBrowserPath(alternativeBrowserPath);
  }
}
