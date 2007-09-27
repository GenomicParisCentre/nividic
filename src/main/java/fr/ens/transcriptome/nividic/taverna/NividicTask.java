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

import java.util.Map;

import org.embl.ebi.escience.scufl.IProcessorTask;
import org.embl.ebi.escience.scufl.Processor;
import org.embl.ebi.escience.scuflworkers.ProcessorTaskWorker;

import uk.ac.soton.itinnovation.taverna.enactor.entities.TaskExecutionException;

/**
 * A task to invoke a Nividic Processor
 * @author Tom Oinn
 * @author Thomas Kuhn
 */
public class NividicTask implements ProcessorTaskWorker {

  private Processor proc;

  public NividicTask(Processor p) {
    this.proc = p;
  }

  public Map execute(Map inputMap, IProcessorTask parentTask)
      throws TaskExecutionException {
    NividicProcessor theProcessor = (NividicProcessor) proc;
    return theProcessor.getWorker().execute(inputMap);
  }

}
