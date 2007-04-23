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

package fr.ens.transcriptome.nividic.js;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.Arrays;
import java.util.regex.Pattern;

/**
 * This class implements filesystem API for javascript
 * @author Laurent Jourdren
 */
public class FileSystem {

  private File currentDir = new File(".");

  /**
   * Change the current dir.
   * @param directory The new directory
   * @throws IOException if an error occurs while changing the directory
   */
  public void chdir(final String directory) throws IOException {

    chdir(sf(directory));
  }

  /**
   * Change the current dir.
   * @param dir The new directory
   * @throws IOException if an error occurs while changing the directory
   */
  public void chdir(final File dir) throws IOException {

    if (dir == null)
      throw new NullPointerException("dir is null");
    if (!dir.exists() || !dir.isDirectory())
      throw new IOException("dir ("+ dir.getAbsolutePath() +") is not a valid directory");

    this.currentDir = dir;
  }

  /**
   * Get the current directory.
   * @return The current directory
   */
  public File cwd() {

    return currentDir;
  }

  /**
   * Get all the files of the current directory.
   * @return an array of file
   */
  public File[] ls() {

    return ls((String) null);
  }

  private Pattern createLsPattern(final String s) {

    StringBuffer sb = new StringBuffer();

    for (int i = 0; i < s.length(); i++) {

      char c = s.charAt(i);

      switch (c) {
      case '\\':
      case '$':
      case '^':

      case '+':
      case '.':
      case '[':
      case ']':
        sb.append('\\');
        sb.append(c);
        break;

      case '*':
        sb.append(".*");
        break;
      case '?':
        sb.append(".{1}");
        break;

      default:
        sb.append(c);
      }

    }
    // System.out.println("Pattern: " + sb);
    return Pattern.compile(sb.toString());
  }

  /**
   * Get all the files of the current directory.
   * @param path path to show
   * @return an array of file
   */
  public File[] ls(final String path) {

    if (path == null)
      return ls("*");

    File f = new File(path);

    if (f.isDirectory())
      return lsInternal(f, createLsPattern("*"));

    File parent = f.getParentFile();
    String s = f.getName();

    final Pattern p = createLsPattern(s);

    return lsInternal(parent == null ? cwd() : parent, p);

  }

  private File[] lsInternal(final File parent, final Pattern p) {

    return parent.listFiles(new FileFilter() {

      public boolean accept(File arg0) {

        return p.matcher(arg0.getName()).find();
      }
    });
  }

  public File sf(String filename) {

    File f = new File(filename);

    if (f.exists())
      return f;

    return new File(currentDir, filename);
  }

  public void mkdir(String path) {

    mkdir(sf(path));
  }

  public void mkdir(File dir) {

    if (dir == null)
      throw new NullPointerException("dir is null");
    dir.mkdir();
  }

  public void mkdir(final String[] filenames) throws IOException {

    if (filenames == null)
      throw new NullPointerException("filenames is null");

    for (int i = 0; i < filenames.length; i++)
      mkdir(filenames[i]);
  }

  public void mkdir(final File[] files) throws IOException {

    if (files == null)
      throw new NullPointerException("files is null");

    for (int i = 0; i < files.length; i++)
      mkdir(files[i]);
  }

  public void rmdir(String path) throws IOException {

    rmdir(sf(path));
  }

  public void rmdir(File dir) throws IOException {

    if (dir == null)
      throw new NullPointerException("dir is null");

    if (!dir.exists() || !dir.isDirectory())
      throw new IOException("dir is not a valid directory");

    dir.delete();
  }

  public void rmdir(final String[] filenames) throws IOException {

    if (filenames == null)
      throw new NullPointerException("filenames is null");

    for (int i = 0; i < filenames.length; i++)
      rmdir(filenames[i]);
  }

  public void rmdir(final File[] files) throws IOException {

    if (files == null)
      throw new NullPointerException("files is null");

    for (int i = 0; i < files.length; i++)
      rmdir(files[i]);
  }

  public void unlink(final String filename) throws IOException {

    unlink(sf(filename));
  }

  public void unlink(final File file) throws IOException {

    if (file == null)
      throw new NullPointerException("file is null");

    if (!file.exists())
      throw new IOException("file is not a valid file");

    file.delete();
  }

  public void unlink(final String[] filenames) throws IOException {

    if (filenames == null)
      throw new NullPointerException("filenames is null");

    for (int i = 0; i < filenames.length; i++)
      unlink(filenames[i]);
  }

  public void unlink(final File[] files) throws IOException {

    if (files == null)
      throw new NullPointerException("files is null");

    for (int i = 0; i < files.length; i++)
      unlink(files[i]);
  }

  public void move(final String a, final String b) throws IOException {

    move(sf(a), sf(b));
  }

  public void move(final File a, final File b) throws IOException {

    if (a == null || b == null)
      throw new NullPointerException("one ore more argument is null");

    if (!a.exists())
      throw new IOException(a.getName() + " not exists");

    a.renameTo(b);
  }

  public static void main(String[] args) throws IOException {

    FileSystem fs = new FileSystem();

    System.out.println("cwd:" + fs.cwd());
    System.out.println(Arrays.deepToString(fs.ls("/home/*en")));
    fs.chdir("/home");
    System.out.println("cwd:" + fs.cwd());
    System.out.println(Arrays.deepToString(fs.ls()));

    boolean bs[] = {true, false, true};

  }

}
