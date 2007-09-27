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

import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import javax.swing.tree.DefaultMutableTreeNode;

import org.apache.log4j.Logger;
import org.embl.ebi.escience.scuflui.workbench.Scavenger;
import org.embl.ebi.escience.scuflui.workbench.ScavengerCreationException;

/**
 * A scavenger that can create new NividicServiceProcessor nodes. Local Services
 * are discovered using the Nividic LocalWorker SPI pattern, with the processor
 * description and category held in nividic.properties mapped to the classname.
 * @author Laurent Jourdren
 * @author Tom Oinn
 * @author Matthew Pocock
 * @author Stuart Owen
 * @author Thomas Kuhn
 */
public class NividicScavenger extends Scavenger {

  private static final long serialVersionUID = -901978754221092069L;

  private static Logger logger = Logger.getLogger(NividicScavenger.class);

  private static final String CONF_FILENAME =
      Defaults.APP_NAME_LOWERCASE + ".properties";

  private static Map<String, Scavenger> workerList =
      new TreeMap<String, Scavenger>();

  static {
    try {
      Properties properties = new Properties();
      List<NividicLocalWorker> workers =
          NividicLocalWorkerRegistry.instance().getLocalWorkers();
      for (NividicLocalWorker worker : workers) {
        String description =
            properties.getProperty(worker.getClass().getName());
        if (description == null) {
          // use the workers classloader to find the properties file
          // (because of Raven).
          Enumeration en =
              worker.getClass().getClassLoader().getResources(CONF_FILENAME);
          while (en.hasMoreElements()) {
            URL resURL = (URL) en.nextElement();
            properties.load(resURL.openStream());
          }
          description = properties.getProperty(worker.getClass().getName());
        }

        if (description == null) {
          logger.warn("No description in "
              + CONF_FILENAME + " file found for: "
              + worker.getClass().getName());
        } else {
          String[] split = description.split(":");
          String shortDescription = description;
          String category = "";
          if (split.length == 2) {
            shortDescription = split[1];
            category = split[0];

          }
          if (!category.equalsIgnoreCase("hidden")) {
            workerList.put(description, new Scavenger(
                new NividicProcessorFactory(worker.getClass().getName(),
                    shortDescription)));
          }
        }
      }
    } catch (Exception e) {
      logger.error(
          "Failure in initialization of LocalWorker nividic scavenger", e);
    }
  }

  /**
   * Create a new nividic local service scavenger
   */
  public NividicScavenger() throws ScavengerCreationException {
    super(Defaults.APP_NAME + " - " + Defaults.APP_DESCRIPTION);
    // Get all the categories and create nodes
    Map<String, DefaultMutableTreeNode> nodeMap =
        new TreeMap<String, DefaultMutableTreeNode>();
    for (Iterator i = workerList.keySet().iterator(); i.hasNext();) {
      String key = (String) i.next();
      Scavenger s = workerList.get(key);
      String category = "default";
      if (key.split(":").length == 2) {
        category = key.split(":")[0];
      }
      // If the category doesn't exist create it
      DefaultMutableTreeNode categoryNode;
      if (nodeMap.containsKey(category)) {
        categoryNode = nodeMap.get(category);
      } else {
        categoryNode = new DefaultMutableTreeNode(category);
        nodeMap.put(category, categoryNode);
      }
      categoryNode.add(s);
    }
    // for all available local widgets, add them as
    // children to this scavenger.
    for (Iterator i = nodeMap.values().iterator(); i.hasNext();) {
      add((DefaultMutableTreeNode) i.next());
    }
  }

}