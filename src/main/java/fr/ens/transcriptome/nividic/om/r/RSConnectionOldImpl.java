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
import java.awt.Toolkit;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.rosuda.JRclient.REXP;
import org.rosuda.JRclient.RFileInputStream;
import org.rosuda.JRclient.RSrvException;
import org.rosuda.JRclient.Rconnection;

/**
 * This class define an enhanced connection to RServe.
 * @author Laurent Jourdren
 * @author Marion Gaussen
 */
class RSConnectionOldImpl implements RSConnection {

  private String serverName;
  private Rconnection rConnection;
  private String sourceDirectory = "/tmp/rspsources";

  private static final int BUFFER_SIZE = 32 * 1024;

  //
  // Getters
  //

  /**
   * Get the R connection.
   * @return Returns the rConnection
   * @throws RSException
   */
  protected Rconnection getRConnection() throws RSException {

    if (this.rConnection == null)
      connect();

    return rConnection;
  }

  public String getSourceDirectory() {
    return sourceDirectory;
  }

  //
  // Setters
  //

  public void setSourceDirectory(final String sourceDirectory) {
    this.sourceDirectory = sourceDirectory;
  }

  //
  // Other methods
  //

  private void testAndCreateSourceDirectory() throws RSException {

    try {
      Rconnection c = getRConnection();
      if (c == null)
        throw new RSException("Connection is null");

      REXP exists = c.eval("file.exists(\"" + getSourceDirectory() + "\")");

      if (exists.asBool().isFALSE()) {

        c.voidEval("dir.create(\"" + getSourceDirectory() + "\")");
        exists = c.eval("file.exists(\"" + getSourceDirectory() + "\")");

        if (exists.asBool().isFALSE())
          throw new RSException("Enable to create source directory");
      }
    } catch (RSrvException e) {
      throw new RSException("RServe exception: " + e);
    }

  }

  public void writeStringAsFile(final String outputFilename, final String value)
      throws RSException {

    if (outputFilename == null)
      return;

    PrintWriter pw = new PrintWriter(getFileOutputStream(outputFilename));
    if (value != null)
      pw.write(value);
    pw.close();

  }

  public InputStream getFileInputStream(final String filename)
      throws RSException {

    Rconnection c = getRConnection();

    try {
      return c.openFile(filename);
    } catch (IOException e) {

      throw new RSException("Error: " + e.getMessage());
    }

  }

  public OutputStream getFileOutputStream(final String filename)
      throws RSException {

    Rconnection c = getRConnection();

    try {
      return c.createFile(filename);
    } catch (IOException e) {

      throw new RSException("Error: " + e.getMessage());
    }

  }

  public void putFile(final File inputFile, final String rServeFilename)
      throws RSException {

    try {

      InputStream is = new FileInputStream(inputFile);
      OutputStream os = getFileOutputStream(rServeFilename);

      byte[] buf = new byte[BUFFER_SIZE];
      int i = 0;

      while ((i = is.read(buf)) != -1)
        os.write(buf, 0, i);

      is.close();
      os.close();

    } catch (RSException e) {
      throw new RSException("Unable to get the normalized bioAssay");
    } catch (FileNotFoundException e) {
      throw new RSException("Unable to create report.");
    } catch (IOException e) {
      throw new RSException("Unable to create report.");
    }

  }

  public void getFile(final String rServeFilename, final File outputfile)
      throws RSException {

    try {
      InputStream is = getFileInputStream(rServeFilename);

      OutputStream fos = new FileOutputStream(outputfile);

      final byte[] buf = new byte[BUFFER_SIZE];
      int i = 0;

      while ((i = is.read(buf)) != -1)
        fos.write(buf, 0, i);

      is.close();
      fos.close();

    } catch (RSException e) {
      throw new RSException("Unable to get the file: " + rServeFilename);
    } catch (FileNotFoundException e) {
      throw new RSException("Unable to get the file: " + rServeFilename);
    } catch (IOException e) {
      throw new RSException("Unable to get the file: " + rServeFilename);
    }

  }

  public void getFilesIntoZip(final List<String> rServeFilenames,
      final File zipFile) throws RSException {

    try {
      ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFile));

      final byte[] buf = new byte[BUFFER_SIZE];

      for (String f : rServeFilenames) {
        final InputStream is = getFileInputStream(f);

        // Add Zip entry to output stream.
        out.putNextEntry(new ZipEntry(f));

        int i = 0;

        while ((i = is.read(buf)) != -1)
          out.write(buf, 0, i);

        // Complete the entry
        out.closeEntry();
        is.close();
      }

      // Complete the Zip file
      out.close();

    } catch (RSException e) {
      throw new RSException("Unable to get the normalized bioAssay");
    } catch (FileNotFoundException e) {
      throw new RSException("Unable to create report.");
    } catch (IOException e) {
      throw new RSException("Unable to create report.");
    }
  }

  public void removeFile(final String filename) throws RSException {

    try {
      // Test if the file exists
      Rconnection c = getRConnection();

      REXP exists = c.eval("file.exists(\"" + filename + "\")");
      if (exists.asBool().isTRUE())

        c.voidEval("file.remove(\"" + filename + "\")");

    } catch (RSrvException e) {
      throw new RSException("RServe exception: " + e);
    }
  }

  public void executeRCode(final String source) throws RSException {

    if (source == null)
      return;

    int hashCode = source.hashCode();
    String filename = getSourceDirectory() + "/" + hashCode + ".R";

    try {

      // Test if the file exists
      Rconnection c = getRConnection();
      REXP exists = c.eval("file.exists(\"" + filename + "\")");

      // Create the file on the server if doesn't exists
      // if (exists.asList().at(0).asBool().isFALSE()) {
      if (exists.asBool().isFALSE())
        writeStringAsFile(filename, source);

      // Execute the source
      c.voidEval("source(\"" + filename + "\")");

    } catch (RSrvException e) {
      throw new RSException("RServe exception: " + e);
    }

  }

  public Image loadImage(final String filename) throws RSException {

    if (filename == null)
      return null;

    final Rconnection connection = getRConnection();

    if (connection == null)
      throw new RSException("Connection is null");

    try {
      RFileInputStream is = connection.openFile(filename);
      ArrayList<byte[]> buffers = new ArrayList<byte[]>();
      int bufSize = 65536;
      byte[] buf = new byte[bufSize];
      int imgLength = 0;
      int n = 0;
      while (true) {
        n = is.read(buf);
        if (n == bufSize) {
          buffers.add(buf);
          buf = new byte[bufSize];
        }
        if (n > 0)
          imgLength += n;
        if (n < bufSize)
          break;
      }
      if (imgLength < 10) { // this shouldn't be the case actually,
        // because we did some error checking, but
        // for those paranoid ...
        throw new RSRuntimeException(
            "Cannot load image, check R output, probably R didn't produce anything.");

      }

      // now let's join all the chunks into one, big array ...
      byte[] imgCode = new byte[imgLength];
      int imgPos = 0;

      final int nbBuffers = buffers.size();

      for (int i = 0; i < nbBuffers; i++) {
        byte[] b = buffers.get(i);
        System.arraycopy(b, 0, imgCode, imgPos, bufSize);
        imgPos += bufSize;
      }
      if (n > 0)
        System.arraycopy(buf, 0, imgCode, imgPos, n);

      // ... and close the file ... and remove it - we have what we need :)
      is.close();
      connection.removeFile("test.jpg");

      // now this is pretty boring AWT stuff, nothing to do with R ...
      Image img = Toolkit.getDefaultToolkit().createImage(imgCode);

      return img;

    } catch (IOException e) {
      throw new RSException("Error while load image");
    } catch (RSrvException e) {
      throw new RSException("Error while removing image from server");
    }

  }

  public byte[] getFileAsArray(final String filename) throws RSException {

    final Rconnection connection = getRConnection();

    if (connection == null)
      throw new RSException("Connection is null");

    try {
      RFileInputStream is = connection.openFile(filename);
      ArrayList<byte[]> buffers = new ArrayList<byte[]>();

      int bufSize = 65536;
      byte[] buf = new byte[bufSize];

      int imgLength = 0;
      int n = 0;
      while (true) {
        n = is.read(buf);
        if (n == bufSize) {
          buffers.add(buf);
          buf = new byte[bufSize];
        }
        if (n > 0)
          imgLength += n;
        if (n < bufSize)
          break;
      }
      if (imgLength < 10) {
        throw new RSRuntimeException(
            "Cannot load image, check R output, probably R didn't produce anything.");

      }

      byte[] imgCode = new byte[imgLength];
      int imgPos = 0;

      final int nbBuffers = buffers.size();

      for (int i = 0; i < nbBuffers; i++) {
        byte[] b = buffers.get(i);
        System.arraycopy(b, 0, imgCode, imgPos, bufSize);
        imgPos += bufSize;
      }
      if (n > 0)
        System.arraycopy(buf, 0, imgCode, imgPos, n);

      is.close();

      return imgCode;

    } catch (IOException e) {
      throw new RSException("Error while load image");
    }

  }

  public static void showImage(final java.awt.Image image) {

    javax.swing.JFrame f = new javax.swing.JFrame("Test image");
    javax.swing.JLabel b =
        new javax.swing.JLabel(new javax.swing.ImageIcon(image));
    f.getContentPane().add(b);
    f.pack();
    f.setVisible(true);
  }

  private void connect() throws RSException {

    try {
      this.rConnection = new Rconnection(this.serverName);
    } catch (RSrvException e) {
      throw new RSException("Unable to connect to the server: "
          + e.getMessage());
    }

    testAndCreateSourceDirectory();
  }

  public void disConnect() {

    this.rConnection.close();
  }

  //
  // Constructor
  //

  /**
   * Default constructor. Connect to the localhost.
   */
  public RSConnectionOldImpl() {
    this(null);
  }

  /**
   * Public constructor.
   * @param serverName RServe server to use
   */
  public RSConnectionOldImpl(final String serverName) {

    this.serverName = serverName == null ? "127.0.0.1" : serverName.trim();
  }

}
