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

import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.html.HTMLDocument;

/**
 * This class implements an HTML browser
 * @author Laurent Jourdren
 */
public class BrowserWidget {

  private JEditorPane jep = new JEditorPane();
  private HyperlinkListener hyperLinkListener;
  private static final String DEFAULT_CONTENT_TYPE = "text/html";
  private URL url;

  //
  // Getters
  //

  /**
   * Get the URL of the document.
   * @return the url of the document
   */
  public URL getURL() {

    return url;
  }

  /**
   * Get the HTML code of the document.
   * @return The HTML code of the document
   */
  public String getText() {

    return this.jep.getDocument().toString();
  }

  /**
   * Get hyper link listener
   * @return The hyper link listener
   */
  public HyperlinkListener getHyperLinkListener() {
    return hyperLinkListener;
  }

  /**
   * Get the internal document.
   * @return The internal document
   */
  public HTMLDocument getDocument() {
    return (HTMLDocument) this.jep.getDocument();
  }

  /**
   * Get content type.
   * @return The content type of the document.
   */
  public String getContentType() {
    return this.jep.getContentType();
  }

  /**
   * Get the graphical component.
   * @return The graphical component
   */
  public JComponent getComponent() {
    return this.jep;
  }

  //
  // Setters
  //

  /**
   * Set the URL of the editorPane
   * @param url URL to set
   * @throws IOException if an error occur while loading the page
   */
  public void setURL(final URL url) throws IOException {

    if (url == null)
      return;
    this.url = url;
    jep.setPage(url);
  }

  /**
   * Set the html of the page
   * @param text HTML code to set
   */
  public void setText(final String text) {

    this.url = null;

    this.jep.setText(text);
  }

  /**
   * Set the hyperlink listener
   * @param hyperLinkListener Hyper link listener to set
   */
  public void setHyperLinkListener(final HyperlinkListener hyperLinkListener) {

    if (this.hyperLinkListener != null)
      this.jep.removeHyperlinkListener(this.hyperLinkListener);

    this.hyperLinkListener = hyperLinkListener;
    this.jep.addHyperlinkListener(hyperLinkListener);
  }

  /**
   * Set the content type of the document.
   * @param contentType The content type to set
   */
  public void setContentType(final String contentType) {
    this.jep.setContentType(contentType);
  }

  //
  // Other methods
  //

  private void init() {

    this.jep.setContentType(DEFAULT_CONTENT_TYPE);
    this.jep.setEditable(false);

  }

  //
  // Constructors
  //

  /**
   * Public constructor.
   */
  public BrowserWidget() {
    init();
  }

  /**
   * Public constructor.
   * @param url URL to set
   * @throws IOException if an error occur while loading the page
   */
  public BrowserWidget(final URL url) throws IOException {
    this();
    setURL(url);
  }

  /**
   * Public constructor.
   * @param html HTML code to set
   */
  public BrowserWidget(final String html) {
    this();
    setText(html);
  }

}
