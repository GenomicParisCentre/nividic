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

package fr.ens.transcriptome.nividic.om.impl.storage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.Serializable;
import java.nio.DoubleBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class FileMatrixStore extends AbstractMatrixStore implements
    Serializable {

  private static int MAX_ROW_COUNT_INIT = 20000;
  // public static int MAX_FILE_SIZE = 4 * (1000 * 1000);

  private Map<String, ColumnStore> columns = new HashMap<String, ColumnStore>();
  private int rowCount;
  private int maxRowCount = MAX_ROW_COUNT_INIT;

  private class ColumnStore implements Serializable {

    private File file;
    private transient FileChannel fc;
    private transient MappedByteBuffer bb;
    private transient DoubleBuffer buffer;

    public void remove() throws IOException {

      fc.close();
      file.delete();
    }

    public ColumnStore(int limit) throws IOException {

      this.file = File.createTempFile("ms_", ".dat");
      this.file.deleteOnExit();
      resize(limit);
    }

    public void resize(int newLimit) throws IOException {

      if (this.fc != null) {
        this.fc.close();
      }

      this.fc = new RandomAccessFile(file, "rw").getChannel();

      this.bb = fc.map(FileChannel.MapMode.READ_WRITE, 0, newLimit * 8); // MAX_FILE_SIZE);
      this.buffer = bb.asDoubleBuffer();
    }

    public void finalize() throws Throwable {
      super.finalize();
      this.fc.close();
      this.file.delete();
    }
  }

  public boolean isColumn(final String columnName) {

    return this.columns.containsKey(columnName);
  }

  public int getRowCount() {

    return this.rowCount;
  }

  /*
   * (non-Javadoc)
   * @see fr.ens.transcriptome.nividic.om.impl.storage.MatrixStore#addColumn(java.lang.String)
   */
  public void addColumn(final String columnName) throws MatrixStoreException {

    if (columnName == null)
      return;

    try {
      final ColumnStore cs = new ColumnStore(this.maxRowCount);
      this.columns.put(columnName, cs);
    } catch (IOException e) {
      throw new MatrixStoreException(e.getMessage());
    }

  }

  /*
   * (non-Javadoc)
   * @see fr.ens.transcriptome.nividic.om.impl.storage.MatrixStore#removeColumn(java.lang.String)
   */
  public void removeColumn(final String columnName) throws MatrixStoreException {

    final ColumnStore cs = this.columns.get(columnName);
    if (cs == null)
      return;

    try {
      cs.remove();
      this.columns.remove(columnName);
    } catch (IOException e) {
      throw new MatrixStoreException("The column doesn't exists");
    }
  }

  public void renameColumn(final String oldName, final String newName)
      throws MatrixStoreException {

    if (!this.columns.containsKey(oldName))
      throw new MatrixStoreException("The column doesn't exists");
    if (this.columns.containsKey(newName))
      throw new MatrixStoreException("The new column name already exists");

    final ColumnStore cs = this.columns.get(oldName);
    this.columns.remove(oldName);
    this.columns.put(newName, cs);
  }

  public void addRow(int count) throws MatrixStoreException {

    int c = count < 1 ? 1 : count;

    this.rowCount += c;

    if (this.rowCount > this.maxRowCount) {

      while (this.rowCount > this.maxRowCount)
        this.maxRowCount += getNewSize(this.maxRowCount);

      try {
        for (Map.Entry<String, ColumnStore> entry : this.columns.entrySet())
          entry.getValue().resize(this.maxRowCount);
      } catch (IOException e) {
        throw new MatrixStoreException("Unable to resize the matrix");
      }

    }

  }

  /*
   * (non-Javadoc)
   * @see fr.ens.transcriptome.nividic.om.impl.storage.MatrixStore#addRow()
   */
  public void addRow() throws MatrixStoreException {

    addRow(1);
  }

  public int getColumnCount() {

    return this.columns.size();
  }

  /*
   * (non-Javadoc)
   * @see fr.ens.transcriptome.nividic.om.impl.storage.MatrixStore#set(int,
   *      java.lang.String, double)
   */
  public void set(final int rowIndex, final String columnName,
      final double value) throws MatrixStoreException {

    if (rowIndex < 0 || rowIndex >= this.rowCount)
      throw new MatrixStoreException("The row doesn't exist");

    final ColumnStore cs = this.columns.get(columnName);
    if (cs == null)
      throw new MatrixStoreException("The column doesn't exist");

    cs.buffer.put(rowIndex, value);
  }

  /*
   * (non-Javadoc)
   * @see fr.ens.transcriptome.nividic.om.impl.storage.MatrixStore#get(int,
   *      java.lang.String, double)
   */
  public double get(final int rowIndex, final String columnName)
      throws MatrixStoreException {

    if (rowIndex < 0 || rowIndex >= this.rowCount)
      throw new MatrixStoreException("The row doesn't exist");

    final ColumnStore cs = this.columns.get(columnName);
    if (cs == null)
      throw new MatrixStoreException("The column doesn't exist");

    return cs.buffer.get(rowIndex);
  }

  private int getNewSize(int currentSize) {

    final int max_exp = 1000000;

    if (currentSize > max_exp)
      return currentSize += max_exp / 5;

    return currentSize * 2;

  }

  public void fill(final String columnName, final double value)
      throws MatrixStoreException {

    final ColumnStore cs = this.columns.get(columnName);
    if (cs == null)
      throw new MatrixStoreException("The column doesn't exist");

    final int limit = cs.buffer.limit();
    for (int i = 0; i < limit; i++)
      cs.buffer.put(i, value);

  }

  //
  // Test methods
  //

  private static DoubleBuffer createBuffer(String filename) throws IOException {

    File file = new File("/tmp/data");

    final int length = 0x8FFFFFF; // Integer.MAX_VALUE;

    FileChannel fc = new RandomAccessFile(file, "rw").getChannel();

    MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_WRITE, 0, fc.size());

    return bb.asDoubleBuffer();
  }

  private static DoubleBuffer createBuffer2(String filename) throws IOException {

    // File file = File.createTempFile("ms_", ".dat");
    File file = new File("/tmp/data");
    final int length = 0x8FFFFFF;
    // length = fc.size();
    FileChannel fc = new RandomAccessFile(file, "rw").getChannel();

    final MappedByteBuffer bb =
        fc.map(FileChannel.MapMode.READ_WRITE, 0, length);
    return bb.asDoubleBuffer();
  }

  private static void test() throws IOException {

    int max = 18 * 1000 * 1000;
    int nbBuffer = 10;

    DoubleBuffer[] buffers = new DoubleBuffer[nbBuffer];

    for (int i = 0; i < buffers.length; i++) {

      buffers[i] = createBuffer2("/tmp/data" + i);
      final DoubleBuffer db = buffers[i];

      for (int j = 0; j < max; j++)
        db.put(j, 1000 - j);

      for (int j = 0; j < 10; j++)
        System.out.println(db.get(j));

      for (int j = 0; j < 10; j++) {
        System.out.println(db.get(max - j));
      }

    }

    System.out.println(max);

  }

  public static void main(String[] args) throws IOException {

    test();

  }

}
