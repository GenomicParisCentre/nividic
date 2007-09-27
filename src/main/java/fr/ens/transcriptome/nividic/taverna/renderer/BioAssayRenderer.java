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

package fr.ens.transcriptome.nividic.taverna.renderer;

import java.awt.BorderLayout;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JPanel;

import org.embl.ebi.escience.baclava.DataThing;
import org.embl.ebi.escience.scuflui.renderers.AbstractRenderer;
import org.embl.ebi.escience.scuflui.renderers.RendererException;
import org.embl.ebi.escience.scuflui.renderers.RendererRegistry;

import fr.ens.transcriptome.nividic.om.BioAssay;
import fr.ens.transcriptome.nividic.om.gui.BioAssayComponent;

public class BioAssayRenderer extends AbstractRenderer.ByMimeType {

  //
  // Methods from AbstractRenderer.ByMimeType
  //

  /**
   * Make a decision about each mime type presented.
   * @param renderers the sibling renderers
   * @param userObject the user data object to render
   * @param mimeType one of the mime types associated with it
   * @return true if we can handle this object with this mime type
   */
  @Override
  protected boolean canHandle(RendererRegistry renderers, Object userObject,
      String mimeType) {

    /*
     * return LocalWorkerNividic.BIOASSAY.equals(mimeType) && userObject
     * instanceof fr.ens.transcriptome.nividic.om.BioAssay;
     */
    return userObject instanceof fr.ens.transcriptome.nividic.om.BioAssay;
  }

  /**
   * Return a JComponent that renders this object that proports to have a
   * particular mime type. If canHandle() returns true, then getComponent() must
   * not return null.
   * @param renderers the MimeTypeRendereRegistry to look up sibling renderers
   * @param dataThing the object to render
   */
  public JComponent getComponent(RendererRegistry renderers, DataThing dataThing)
      throws RendererException {

    final BioAssay bioAssay = (BioAssay) dataThing.getDataObject();

    JPanel itemPanel = new JPanel(new BorderLayout());
    itemPanel.add(new BioAssayComponent(bioAssay), BorderLayout.CENTER);

    return itemPanel;
  }

  /**
   * Discover if this is a terminal renderer. A renderer is terminal if it
   * renders the given DataThing. It is not terminal if it first calculates some
   * property of that DataThing that may potentially lead to some other
   * non-terminal Renderer being used.
   * @return true if this is a terminal renderer, false otherwise
   */
  public boolean isTerminal() {

    // TODO Auto-generated method stub
    return true;
  }

  //
  // Constructor
  //

  /**
   * The constructor.
   */
  public BioAssayRenderer() {

    super("BioAssay", new ImageIcon(BioAssayRenderer.class.getClassLoader()
        .getResource("org/embl/ebi/escience/baclava/icons/text.png")));
  }

}
