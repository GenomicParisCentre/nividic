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

import java.util.Properties;

import org.apache.log4j.Logger;
import org.embl.ebi.escience.scufl.DuplicatePortNameException;
import org.embl.ebi.escience.scufl.DuplicateProcessorNameException;
import org.embl.ebi.escience.scufl.InputPort;
import org.embl.ebi.escience.scufl.OutputPort;
import org.embl.ebi.escience.scufl.PortCreationException;
import org.embl.ebi.escience.scufl.Processor;
import org.embl.ebi.escience.scufl.ProcessorCreationException;
import org.embl.ebi.escience.scufl.ScuflModel;
import org.embl.ebi.escience.scufl.SemanticMarkup;
import org.jdom.Element;

/**
 * A processor that runs the quick Java plugins defined by the LocalService
 * interface in this package.
 * @author Tom Oinn
 * @author Thomas Kuhn
 */
@SuppressWarnings("serial")
public class NividicProcessor extends Processor {

  private String workerClassName;

  private static Logger logger = Logger.getLogger(NividicProcessor.class);

  private NividicLocalWorker theImplementation;

  public String getWorkerClassName() {
    return this.workerClassName;
  }

  public NividicLocalWorker getWorker() {
    return this.theImplementation;
  }

  public int getMaximumWorkers() {
    return 100;
  }

  public NividicProcessor(ScuflModel model, String name,
      NividicLocalWorker worker) throws ProcessorCreationException,
      DuplicateProcessorNameException {
    super(model, name);
    this.workerClassName = worker.getClass().getName();
    this.theImplementation = worker;
    initialise(model, name);
  }

  public NividicProcessor(ScuflModel model, String name,
      String workerClassName, Element additionalXML)
      throws ProcessorCreationException, DuplicateProcessorNameException {
    super(model, name);
    this.workerClassName = workerClassName;
    try {
      // Get the instance of the worker
      Class theClass =
          NividicLocalWorkerRegistry.instance().findClassForName(
              workerClassName);
      theImplementation = (NividicLocalWorker) theClass.newInstance();

    } catch (Exception e) {
      ProcessorCreationException pce =
          new ProcessorCreationException(
              "Unable to instantiate processor for nividic local service class "
                  + workerClassName);
      e.printStackTrace();
      pce.initCause(e);
      throw pce;
    }
    if (this.theImplementation instanceof XMLExtensible) {
      ((XMLExtensible) this.theImplementation).consumeXML(additionalXML);
    } else {
      logger.warn(workerClassName
          + " has been provided with additional XML but is not XMLExtensible");
    }
    initialise(model, name);
  }

  public NividicProcessor(ScuflModel model, String name, String workerClassName)
      throws ProcessorCreationException, DuplicateProcessorNameException {
    super(model, name);
    this.workerClassName = workerClassName;
    try {
      // Get the instance of the worker
      Class theClass =
          NividicLocalWorkerRegistry.instance().findClassForName(
              workerClassName);
      theImplementation = (NividicLocalWorker) theClass.newInstance();

    } catch (Exception e) {
      ProcessorCreationException pce =
          new ProcessorCreationException("Unable to instantiate processor for "
              + Defaults.APP_NAME_LOWERCASE + " local service class "
              + workerClassName);
      e.printStackTrace();
      pce.initCause(e);
      throw pce;
    }
    initialise(model, name);
  }

  protected void initialise(ScuflModel model, String name)
      throws ProcessorCreationException, DuplicateProcessorNameException {
    try {

      if (theImplementation instanceof NividicLocalWorkerWithPorts) {

        for (InputPort port : ((NividicLocalWorkerWithPorts) theImplementation)
            .inputPorts(this)) {
          addPort(port);
        }
        for (OutputPort port : ((NividicLocalWorkerWithPorts) theImplementation)
            .outputPorts(this)) {
          addPort(port);
        }
      } else {
        for (int i = 0; i < theImplementation.inputNames().length; i++) {
          // Create input ports
          InputPort port =
              new InputPort(this, theImplementation.inputNames()[i]);
          port.setSyntacticType(theImplementation.inputTypes()[i]);
          addPort(port);
        }

        for (int i = 0; i < theImplementation.outputNames().length; i++) {
          // Create output ports
          OutputPort port =
              new OutputPort(this, theImplementation.outputNames()[i]);
          port.setSyntacticType(theImplementation.outputTypes()[i]);
          SemanticMarkup m = port.getMetadata();
          String[] mimeTypes =
              ((theImplementation.outputTypes()[i].split("\\'"))[1]).split(",");
          for (int j = 0; j < mimeTypes.length; j++) {
            logger.debug("Mime type " + mimeTypes[j]);
            m.addMIMEType(mimeTypes[j]);
          }
          addPort(port);
        }
      }

    } catch (DuplicatePortNameException dpne) {
      throw new ProcessorCreationException(
          "The supplied specification for the nividic local service processor '"
              + name + "' contained a duplicate port '" + dpne.getMessage()
              + "'");
    } catch (PortCreationException pce) {
      throw new ProcessorCreationException(
          "An error occured whilst generating ports for the nividic local service processor "
              + pce.getMessage());
    }

  }

  /**
   * Get the properties for this processor
   */
  public Properties getProperties() {
    Properties props = new Properties();
    props.put("WorkerClass", this.workerClassName);
    return props;
  }
}
