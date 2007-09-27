/*
 * Copyright (C) 2007 by Thomas Kuhn <thomaskuhn@users.sourceforge.net>
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 * All we ask is that proper credit is given for our work, which includes
 * - but is not limited to - adding the above copyright notice to the beginning
 * of your source code files, and to any copyright notice that you may distribute
 * with programs based on this work.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA
 * 
 * 
 * This file was used and modified from me Thomas Kuhn. The orignial file comment was:
 *
 * This file is a component of the Taverna project,
 * and is licensed under the GNU LGPL.
 * Copyright Tom Oinn, EMBL-EBI
 */
package fr.ens.transcriptome.nividic.taverna;

import org.embl.ebi.escience.scufl.DuplicateProcessorNameException;
import org.embl.ebi.escience.scufl.Processor;
import org.embl.ebi.escience.scufl.ProcessorCreationException;
import org.embl.ebi.escience.scufl.ScuflModel;
import org.embl.ebi.escience.scufl.XScufl;
import org.embl.ebi.escience.scufl.parser.XScuflFormatException;
import org.embl.ebi.escience.scuflworkers.ProcessorFactory;
import org.embl.ebi.escience.scuflworkers.XMLHandler;
import org.jdom.Element;
import org.jdom.Namespace;

/**
 * Handles XML store and load for the nividic local process processor
 * @author Tom Oinn
 * @author Thomas Kuhn
 */
public class NividicXMLHandler implements XMLHandler {

  private static final Namespace xscuflNamespace = XScufl.XScuflNS;

  public Element elementForProcessor(Processor p) {
    NividicProcessor lsp = (NividicProcessor) p;
    Element spec = new Element("nividic", xscuflNamespace);
    spec.setText(lsp.getWorkerClassName());
    if (lsp.getWorker() instanceof XMLExtensible) {
      spec.addContent(((XMLExtensible) lsp.getWorker()).provideXML());
    }
    return spec;
  }

  public Element elementForFactory(ProcessorFactory pf) {
    NividicProcessorFactory lspf = (NividicProcessorFactory) pf;
    Element spec = new Element("nividic", xscuflNamespace);
    spec.setText(lspf.getWorkerClassName());
    return spec;
  }

  public ProcessorFactory getFactory(Element specElement) {
    String workerClass = specElement.getTextTrim();
    // Use the class leaf name as the descriptive name, as we don't
    // have anything better
    String[] parts = workerClass.split("\\.");
    String descriptiveName = parts[parts.length - 1];
    return new NividicProcessorFactory(workerClass, descriptiveName);
  }

  public Processor loadProcessorFromXML(Element processorNode,
      ScuflModel model, String name) throws ProcessorCreationException,
      DuplicateProcessorNameException, XScuflFormatException {

    Element local = processorNode.getChild("nividic", xscuflNamespace);
    Element additionalInfo = local.getChild("extensions", xscuflNamespace);
    String workerClass = local.getTextTrim();
    if (additionalInfo == null) {
      return new NividicProcessor(model, name, workerClass);
    } else {
      return new NividicProcessor(model, name, workerClass, additionalInfo);
    }
  }
}
