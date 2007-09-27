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
 * no comment
 */

package fr.ens.transcriptome.nividic.taverna;

import org.jdom.Element;

/**
 * Inteface that indicates that a LocalWorker XML description can be extended
 * within an addional <extensions/> element, and that the LocalWorker can
 * generate and consume this XML.
 */

public interface XMLExtensible {
  public void consumeXML(Element element);

  public Element provideXML();
}
