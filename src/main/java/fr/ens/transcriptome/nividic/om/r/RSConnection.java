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
 * of the �cole Normale Sup�rieure and the individual authors.
 * These should be listed in @author doc comments.
 *
 * For more information on the Nividic project and its aims,
 * or to join the Nividic mailing list, visit the home page
 * at:
 *
 *      http://www.transcriptome.ens.fr/nividic
 *
 */

package fr.ens.transcriptome.nividic.om.r;

import java.awt.Image;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import org.rosuda.JRclient.REXP;
import org.rosuda.JRclient.RSrvException;

public interface RSConnection {

  /**
   * Get the source directory
   * @return Returns the sourceDirectory
   */
  String getSourceDirectory();

  /**
   * Set the source directory
   * @param sourceDirectory The sourceDirectory to set
   */
  void setSourceDirectory(final String sourceDirectory);

  /**
   * Write a file to the RServer
   * @param outputFilename the filename
   * @param value The content of the file
   * @throws RSException if an error occurs while writing the file
   */
  void writeStringAsFile(final String outputFilename, final String value)
      throws RSException;

  /**
   * Create an inputStream on a file on RServer.
   * @param filename Name of the file on RServer to load
   * @return an inputStream
   * @throws RSException if an exception occurs while reading file
   */
  public InputStream getFileInputStream(final String filename)
      throws RSException;

  /**
   * Create an outputStream on a file on RServer.
   * @param filename Name of the file on RServer to write
   * @return an outputStream
   * @throws RSException if an exception occurs while reading file
   */
  OutputStream getFileOutputStream(final String filename) throws RSException;

  /**
   * Put a file from the RServer.
   * @param rServeFilename filename of the file to put
   * @param outputfile output file of the file to put
   * @throws RSException if an error occurs while downloading the file
   */
  void putFile(final File inputFile, final String rServeFilename)
      throws RSException;

  /**
   * Get a file from the RServer.
   * @param rServeFilename filename of the file to retrieve
   * @param outputfile output file of the file to retrieve
   * @throws RSException if an error occurs while downloading the file
   */
  void getFile(final String rServeFilename, final File outputfile)
      throws RSException;

  /**
   * Get a list of files from the RServer.
   * @param rServeFilenames list of filenames of the files to retrieve
   * @param zipFile zip output file for the files to retrieve
   * @throws RSException if an error occurs while downloading the file
   */
  void getFilesIntoZip(final List<String> rServeFilenames, final File zipFile)
      throws RSException;

  /**
   * Remove a file on the RServer
   * @param filename File to remove
   */
  void removeFile(final String filename) throws RSException;

  /**
   * Execute a R code.
   * @param source code to execute
   * @throws RSException if an error while executing the code
   */
  void executeRCode(final String source) throws RSException;

  /**
   * Load an image from RServe
   * @param filename file to load
   * @return an image object
   * @throws RSException if an error occurs while loading the image
   */
  Image loadImage(final String filename) throws RSException;

  /**
   * Get file from RServe
   * @param filename file to load
   * @return an byte[] object
   * @throws RSException if an error occurs while loading the image
   */
  byte[] getFileAsArray(final String filename) throws RSException;

  /**
   * Destroy the connection to the Rserve server
   * @throws RSException if an error occurs while deleting to Rserve
   */
  void disConnect();

}