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

import org.embl.ebi.escience.baclava.DataThing;

import uk.ac.soton.itinnovation.taverna.enactor.entities.TaskExecutionException;

/**
 * Implemented by classes acting as nividic services and which don't require the
 * full invocation infrastructure.
 * @author Laurent Jourdren
 * @author Tom Oinn
 * @author Thomas Kuhn
 */
public interface NividicLocalWorker {

  //
  // Primitive types
  //

  /** Type string for an Integer object */
  public static final String INT = "'value/Integer'";

  /** Type string for a boolean object */
  public static final String BOOLEAN = "'value/Boolean'";

  /** Type string for a string object */
  public static final String STRING = "'text/plain'";

  /** Type string for a Double object */
  public static final String DOUBLE = "'value/Double'";

  /** Type string for a document in HTML format */
  public static final String HTML = "'text/plain,text/html'";

  /** Type string for a PNG image */
  public static final String PNG_IMAGE = "'application/octet-stream,image/png'";

  /** Type string for arbitrary binary data */
  public static final String BINARY = "'application/octet-stream'";

  /** Type string for a single item of unknown type */
  public static final String UNTYPED = "''";

  //
  // List of Primitive types
  //

  /** Type string for a list Int object */
  public static final String ListOfINT = "l(" + INT + ")";

  /** Type string for a list Boolean object */
  public static final String ListOfBOOLEAN = "l(" + BOOLEAN + ")";

  /** Type string for a String list object */
  public static final String ListOfSTRING = "l(" + STRING + ")";

  /** Type string for a list Double object */
  public static final String ListOfDOUBLE = "l(" + DOUBLE + ")";

  /** Type string for an array of documents in HTML format */
  public static final String ListOfHTML = "l(" + HTML + ")";

  /** Type string for an array of PNG images */
  public static final String ListOfPNG_IMAGE = "l('" + PNG_IMAGE + "')";

  /** Type string for an array of arbitrary binary data blocks */
  public static final String ListOfBINARY = "l(" + BINARY + ")";

  /** Type string for an array of items of unknown type */
  public static final String ListOfUNTYPED = "l(" + UNTYPED + ")";

  //
  // Nividic Types
  //

  public static final String BIOASSAY =
      "'java/fr.ens.transcriptome.nividic.om.BioAssay'";

  public static final String BIOASSAY_FILTER =
      "'java/fr.ens.transcriptome.nividic.om.filters.BioAssayFilter'";

  public static final String MATRIX =
      "'java/fr.ens.transcriptome.nividic.om.ExpressionMatrix'";

  public static final String MATRIX_FILTER =
      "'java/fr.ens.transcriptome.nividic.om.filters.ExpressionMatrixFilter'";

  public static final String DIMENSION =
      "'java/fr.ens.transcriptome.nividic.om.ExpressionMatrixDimension'";

  public static final String TRANSLATOR =
      "'java/fr.ens.transcriptome.nividic.om.translators.Translator'";

  public static final String ANNOTATION =
      "'java/fr.ens.transcriptome.nividic.om.Annotation'";

  public static final String BIOLIST =
      "'java/fr.ens.transcriptome.nividic.om.BiologicalList'";

  public static final String BIOLIST_FILTER =
      "'java/fr.ens.transcriptome.nividic.om.filters.BiologicalListFilter'";

  public static final String DESIGN =
      "'java/fr.ens.transcriptome.nividic.design.Design'";

  //
  // Nividic types lists
  //

  public static final String ListOfDIMENSION = "l(" + DIMENSION + ")";
  public static final String ListOfBioAssay = "l(" + BIOASSAY + ")";

  //
  // Methods
  //
  /**
   * Given a Map of DataThing objects as input, invoke the underlying logic of
   * the task and return a map of DataThing objects as outputs, with the port
   * names acting as keys in the map in both cases.
   * @exception TaskExecutionException thrown if there is an error during
   *                invocation of the task.
   */
  public Map<String, DataThing> execute(Map<String, DataThing> inputMap)
      throws TaskExecutionException;

  /**
   * Get an array of the names of input ports for this processor
   */

  public String[] inputNames();

  /**
   * Get an array of the string types for the inputs defined by the inputNames()
   * method, these should probably use the constants defined in this interface
   * but may use any valid Baclava data type specifier.
   */
  public String[] inputTypes();

  /**
   * Names of the output ports
   */
  public String[] outputNames();

  /**
   * Types of the output ports
   */
  public String[] outputTypes();

}
