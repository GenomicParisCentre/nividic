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
 * * Copyright (C) 2003 The University of Manchester 
 *
 * Modifications to the initial code base are copyright of their
 * respective authors, or their employers as appropriate.  Authorship
 * of the modifications may be determined from the ChangeLog placed at
 * the end of this file.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307
 * USA.
 */

package fr.ens.transcriptome.nividic.taverna;

import java.util.List;

import org.embl.ebi.escience.utils.TavernaSPIRegistry;

/**
 * SPI discovery class for finding classes that implement CDK worker
 * @author Stuart Owen
 * @author Thomas Kuhn
 */

public class NividicLocalWorkerRegistry extends
    TavernaSPIRegistry<NividicLocalWorker> {

  private static NividicLocalWorkerRegistry instance =
      new NividicLocalWorkerRegistry();

  private NividicLocalWorkerRegistry() {
    super(NividicLocalWorker.class);
  }

  /**
   * Return a static instance of the registry loaded with all available
   * instances of the ProcessorActionSPI
   */
  public static synchronized NividicLocalWorkerRegistry instance() {

    return instance;
  }

  public List<NividicLocalWorker> getLocalWorkers() {
    List<NividicLocalWorker> result = findComponents();
    return result;
  }

  public Class findClassForName(String classname) throws ClassNotFoundException {
    for (Class c : getSpiRegistry().getClasses()) {
      if (c.getName().equalsIgnoreCase(classname)) {
        return c;
      }
    }
    throw new ClassNotFoundException();
  }
}
