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

import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLFrameHyperlinkEvent;

/**
 * SimpleLinkListener.java A hyperlink listener for use with JEditorPane. This
 * listener changes the cursor over hyperlinks based on enter/exit events and
 * also loads a new page when a valid hyperlink is clicked. Most part of this
 * code come from O'Reilly Java Swing 2 book.
 * @author Laurent Jourdren
 * @author Marc Loy
 * @author Robert Ecksteing
 * @author Dave Wood
 * @author James Elliot
 * @author Brian Cole
 */
public class SimpleLinkListener implements HyperlinkListener {

  private BrowserWidget browser;

  //
  // Getters
  //

  /**
   * Get the browser
   * @return The browser
   */
  public BrowserWidget getBrowser() {
    return browser;
  }

  //
  // Setters
  //

  /**
   * Set the browser
   * @param browser The browser to set
   */
  public void setBrowser(final BrowserWidget browser) {
    this.browser = browser;
  }

  //
  // Other methods
  //

  /**
   * Called when a hypertext link is updated.
   * @param he the event responsible for the update
   */
  public void hyperlinkUpdate(final HyperlinkEvent he) {

    BrowserWidget browser = getBrowser();

    if (browser == null)
      return;

    HyperlinkEvent.EventType type = he.getEventType();

    /*
     * if (type == HyperlinkEvent.EventType.ENTERED) { } else if (type ==
     * HyperlinkEvent.EventType.EXITED) { } else
     */

    if (type == HyperlinkEvent.EventType.ACTIVATED) {
      // Jump event. Get the URL, and, if it's not null, switch to that
      // page in the main editor pane and update the "site url" label.
      if (he instanceof HTMLFrameHyperlinkEvent) {
        // Ahh, frame event; handle this separately.
        HTMLFrameHyperlinkEvent evt = (HTMLFrameHyperlinkEvent) he;
        HTMLDocument doc = browser.getDocument();
        doc.processHTMLFrameHyperlinkEvent(evt);
      } else {
        try {
          browser.setURL(he.getURL());

        } catch (FileNotFoundException fnfe) {
          browser.setText("Could not open file: <tt>" + he.getURL()
              + "</tt>.<hr>");
        } catch (IOException e) {
          browser.setText("Error while reading URL: <tt>" + he.getURL()
              + "</tt>.<hr>");
        }
      }
    }
  }

  //
  // Constructor
  //

  /**
   * Public constructor.
   * @param browser Browser to listen
   */
  public SimpleLinkListener(final BrowserWidget browser) {

    setBrowser(browser);
  }

}
