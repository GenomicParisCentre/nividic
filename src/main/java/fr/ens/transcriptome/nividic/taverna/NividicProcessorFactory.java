/* $RCSfile$
 * $Author: thomaskuhn $
 * $Date: 2006-01-20 08:21:14 +0100 (Fr, 20 Jan 2006) $
 * $Revision: 5602 $
 * 
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

import org.embl.ebi.escience.scuflworkers.ProcessorFactory;

/**
 * Implementation of ProcessorFactory that can create CDKProcessor instances
 * @author Tom Oinn
 * @author Thomas Kuhn
 */
public class NividicProcessorFactory extends ProcessorFactory {

  private String className;

  /**
   * Create a new factory configured with the specified worker class.
   */
  public NividicProcessorFactory(String workerClassName, String descriptiveName) {
    this.className = workerClassName;
    setName(descriptiveName);
  }

  public String getWorkerClassName() {
    return this.className;
  }

  /**
   * A description of the factory
   */
  public String getProcessorDescription() {
    return "A processor that uses the "
        + Defaults.APP_NAME_LOWERCASE + " worker class " + className
        + " to run a " + Defaults.APP_NAME_LOWERCASE
        + " process locally to the enactor.";
  }

  /**
   * Return the Class object for the nividic processors that this factory
   * creates
   */
  public Class getProcessorClass() {
    return NividicProcessor.class;
  }

}
