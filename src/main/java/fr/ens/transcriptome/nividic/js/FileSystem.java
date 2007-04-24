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
import java.util.regex.Pattern;

import fr.ens.transcriptome.nividic.NividicRuntimeException;

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
      throw new IOException("dir (" + dir.getAbsolutePath()
          + ") is not a valid directory");

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

      public boolean accept(final File arg0) {

        return p.matcher(arg0.getName()).find();
      }
    });
  }

  /**
   * Select a file.
   * @param filename File to select
   * @return a file Object
   */
  public File sf(final String filename) {

    if (filename == null || "..".equals(filename.trim()))
      return new File(currentDir, "..");

    File f = new File(filename);

    if (f.exists())
      return f;

    return new File(currentDir, filename);
  }

  /**
   * Create a directory.
   * @param path Path of the directory to create
   */
  public void mkdir(final String path) {

    mkdir(sf(path));
  }

  /**
   * Create a directory.
   * @param dir File of the directory to create
   */
  public void mkdir(final File dir) {

    if (dir == null)
      throw new NullPointerException("dir is null");
    dir.mkdir();
  }

  /**
   * Create directories.
   * @param filenames Filenames of the directories to create
   */
  public void mkdir(final String[] filenames) {

    if (filenames == null)
      throw new NullPointerException("filenames is null");

    for (int i = 0; i < filenames.length; i++)
      mkdir(filenames[i]);
  }

  /**
   * Create directories.
   * @param files Files of the directories to create
   */
  public void mkdir(final File[] files) {

    if (files == null)
      throw new NullPointerException("files is null");

    for (int i = 0; i < files.length; i++)
      mkdir(files[i]);
  }

  /**
   * Remove directory.
   * @param path Path of the directory to remove
   */
  public void rmdir(final String path) {

    rmdir(sf(path));
  }

  /**
   * Remove directory.
   * @param dir File of the directory to remove
   */
  public void rmdir(final File dir) {

    if (dir == null)
      throw new NullPointerException("dir is null");

    if (!dir.exists() || !dir.isDirectory())
      throw new NividicRuntimeException("dir is not a valid directory");

    dir.delete();
  }

  /**
   * Remove directory.
   * @param directories Path of the directory to remove
   */
  public void rmdir(final String[] directories) {

    if (directories == null)
      throw new NullPointerException("filenames is null");

    for (int i = 0; i < directories.length; i++)
      rmdir(directories[i]);
  }

  /**
   * Remove directories.
   * @param files Files of the directories to remove
   */
  public void rmdir(final File[] files) {

    if (files == null)
      throw new NullPointerException("files is null");

    for (int i = 0; i < files.length; i++)
      rmdir(files[i]);
  }

  /**
   * Remove a file.
   * @param filename Filename of the file to remove
   */
  public void unlink(final String filename) {

    unlink(sf(filename));
  }

  /**
   * Remove a file.
   * @param file File to remove
   */
  public void unlink(final File file) {

    if (file == null)
      throw new NullPointerException("file is null");

    if (!file.exists())
      throw new NividicRuntimeException("file is not a valid file");

    file.delete();
  }

  /**
   * Remove files.
   * @param filenames Filenames of the files to remove
   */
  public void unlink(final String[] filenames) {

    if (filenames == null)
      throw new NullPointerException("filenames is null");

    for (int i = 0; i < filenames.length; i++)
      unlink(filenames[i]);
  }

  /**
   * Remove files.
   * @param files files to remove
   */
  public void unlink(final File[] files) {

    if (files == null)
      throw new NullPointerException("files is null");

    for (int i = 0; i < files.length; i++)
      unlink(files[i]);
  }

  /**
   * Move or rename a file.
   * @param oldName Filename of the old name
   * @param newName Filename of the new name
   */
  public void move(final String oldName, final String newName) {

    move(sf(oldName), sf(newName));
  }

  /**
   * Move or rename a file.
   * @param oldFile Old file object
   * @param newFile New file object
   */
  public void move(final File oldFile, final File newFile) {

    if (oldFile == null || newFile == null)
      throw new NullPointerException("one ore more argument is null");

    if (!oldFile.exists())
      throw new NividicRuntimeException(oldFile.getName() + " not exists");

    oldFile.renameTo(newFile);
  }

}
