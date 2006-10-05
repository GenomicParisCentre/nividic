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

package fr.ens.transcriptome.nividic.util.parameter;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Iterator;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

/**
 * A helper class that supports XML-like processing for parameters objects.
 * @author Laurent Jourdren
 */
public class ParametersDocument {

  /** Stores the configuration object this object operates on. */
  private Parameters parameters;

  private Document document;

  //
  // Getters
  //

  /**
   * Return the parameters object for this document.
   * @return the parameter object
   */
  public Parameters getParameters() {
    return parameters;
  }

  /**
   * Get the document.
   * @return The document object
   */
  public Document getDocument() {
    return document;
  }

  //
  // Setters
  //

  /**
   * Set the parameter object this documnet operates on.
   * @param parameters the parameter object
   */
  public void setParameters(final Parameters parameters) {
    this.parameters = parameters;
  }

  /**
   * Set the document.
   * @param document Document to set
   */
  public void setDocument(final Document document) {
    this.document = document;
  }

  //
  // Constructor
  //

  /**
   * Creates a new instance of ParametersXMLDocument and sets the paramter
   * object to be processed.
   * @param parameters the parameter object
   */
  public ParametersDocument(final Parameters parameters) {
    setParameters(parameters);
  }

  /**
   * Create the xml document
   * @return the xml document
   * @throws ParameterException if an error occurs while creating the document
   */
  public Document createDocument() throws ParameterException {

    final Document document = DocumentHelper.createDocument();
    final Element root = document.addElement("parameters");

    final Parameters ps = getParameters();

    if (ps != null) {

      String[] names = ps.getParametersNames();

      for (int i = 0; i < names.length; i++) {

        final Parameter p = ps.getParameter(names[i]);

        final Element param = root.addElement("parameter");
        param.addElement("name").addText(p.getName());

        final Element value = param.addElement("value");

        switch (p.getType()) {
        case Parameter.DATATYPE_INTEGER:
        case Parameter.DATATYPE_BOOLEAN:
        case Parameter.DATATYPE_DOUBLE:
        case Parameter.DATATYPE_STRING:
        case Parameter.DATATYPE_YESNO:
        case Parameter.DATATYPE_ARRAY_INTEGER:
        case Parameter.DATATYPE_ARRAY_BOOLEAN:
        case Parameter.DATATYPE_ARRAY_DOUBLE:
        case Parameter.DATATYPE_ARRAY_STRING:
        case Parameter.DATATYPE_ARRAY_YESNO:
          value.addText("" + p.getValue());
          break;

        default:
          break;
        }

      } // for
    } // parameters is null

    setDocument(document);
    return document;
  }

  //
  // Writer
  //

  /**
   * Writes a configuration (or parts of it) to the given writer.
   * @param out the output writer
   * @param prefix the prefix of the subset to write; if <b>null </b>, the whole
   *                 configuration is written
   * @param root the name of the root element of the resulting document; <b>null
   *                 </b> for a default name
   * @param pretty flag for the pretty print mode
   * @throws IOException if an IO error occurs
   * @throws DocumentException if there is an error during processing
   */
  public void write(final Writer out, final String prefix, final String root,
      final boolean pretty) throws IOException, DocumentException {
    OutputFormat format = pretty ? OutputFormat.createPrettyPrint()
        : OutputFormat.createCompactFormat();

    XMLWriter writer = new XMLWriter(out, format);
    writer.write(getDocument());
  }

  /**
   * Writes a configuration (or parts of it) to the given writer. This
   * overloaded version always uses pretty print mode.
   * @param out the output writer
   * @param prefix the prefix of the subset to write; if <b>null </b>, the whole
   *                 configuration is written
   * @param root the name of the root element of the resulting document; <b>null
   *                 </b> for a default name
   * @throws IOException if an IO error occurs
   * @throws DocumentException if there is an error during processing
   */
  public void write(final Writer out, final String prefix, final String root)
      throws IOException, DocumentException {
    write(out, prefix, root, true);
  }

  /**
   * Writes a configuration (or parts of it) to the given writer. The resulting
   * document's root element will be given a default name.
   * @param out the output writer
   * @param prefix the prefix of the subset to write; if <b>null </b>, the whole
   *                 configuration is written
   * @param pretty flag for the pretty print mode
   * @throws IOException if an IO error occurs
   * @throws DocumentException if there is an error during processing
   */
  public void write(final Writer out, final String prefix, final boolean pretty)
      throws IOException, DocumentException {
    write(out, prefix, null, pretty);
  }

  /**
   * Writes a configuration (or parts of it) to the given writer. The resulting
   * document's root element will be given a default name. This overloaded
   * version always uses pretty print mode.
   * @param out the output writer
   * @param prefix the prefix of the subset to write; if <b>null </b>, the whole
   *                 configuration is written
   * @throws IOException if an IO error occurs
   * @throws DocumentException if there is an error during processing
   */
  public void write(final Writer out, final String prefix) throws IOException,
      DocumentException {
    write(out, prefix, true);
  }

  /**
   * Writes the wrapped configuration to the given writer. The resulting
   * document's root element will be given a default name.
   * @param out the output writer
   * @param pretty flag for the pretty print mode
   * @throws IOException if an IO error occurs
   * @throws DocumentException if there is an error during processing
   */
  public void write(final Writer out, final boolean pretty) throws IOException,
      DocumentException {
    write(out, null, null, pretty);
  }

  /**
   * Writes the wrapped configuration to the given writer. The resulting
   * document's root element will be given a default name. This overloaded
   * version always uses pretty print mode.
   * @param out the output writer
   * @throws IOException if an IO error occurs
   * @throws DocumentException if there is an error during processing
   */
  public void write(final Writer out) throws IOException, DocumentException {
    write(out, true);
  }

  //
  // Read XML
  //

  /**
   * Parse a writer object.
   * @param reader Object to read
   * @return a dom4j document
   * @throws DocumentException if error occurs while reading the document
   */
  public static Document parse(final Reader reader) throws DocumentException {

    SAXReader saxReader = new SAXReader();
    Document document = saxReader.read(reader);
    return document;
  }

  /**
   * Parse a dom4j document to set a Parameters object.
   * @param document dom4j document to read
   * @param parameters Parameters object to set
   * @return The parameter Parameters
   * @throws DocumentException if an error occurs while reading the document
   * @throws ParameterException if an error occurs while setting the parameters
   *                 values
   */
  public static Parameters parse(final Document document,
      final Parameters parameters) throws DocumentException, ParameterException {

    final Element root = document.getRootElement();
    return parse(root, parameters);
  }

  /**
   * Parse a dom4j document to set a Parameters object.
   * @param element Elemt to parse
   * @param parameters Parameters object to set
   * @return The parameter Parameters
   * @throws DocumentException if an error occurs while reading the document
   * @throws ParameterException if an error occurs while setting the parameters
   *                 values
   */
  public static Parameters parse(final Element element,
      final Parameters parameters) throws DocumentException, ParameterException {

    for (Iterator i1 = element.elementIterator("parameter"); i1.hasNext();) {
      final Element parameter = (Element) i1.next();

      String name = "";
      String value = "";

      for (Iterator i2 = parameter.elementIterator("name"); i2.hasNext();) {
        final Element nameElement = (Element) i2.next();
        name = nameElement.getText();
      }

      for (Iterator i3 = parameter.elementIterator("value"); i3.hasNext();) {
        final Element valueElement = (Element) i3.next();
        value = valueElement.getText();
      }

      parameters.setParameter(name, value);
    }

    return parameters;
  }

}