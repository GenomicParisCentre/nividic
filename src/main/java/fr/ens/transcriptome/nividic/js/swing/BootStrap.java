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

package fr.ens.transcriptome.nividic.js.swing;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;

import fr.ens.transcriptome.nividic.util.SystemUtils;

public class BootStrap {

  private static void writeFile(File file, String s) throws IOException {

    FileOutputStream fos = new FileOutputStream(file);

    Writer out = new OutputStreamWriter(fos);
    out.write(s);
    out.close();

  }

  private static void jarPath(final String baseDir, final String jarName,
      final String version, final StringBuffer result) {

    result.append(baseDir);
    result.append("/RM");
    result.append(jarName);
    result.append("-");
    result.append(version);
    result.append(".jar");
  }

  private static String getWebstartCache(final URL url) {

    StringBuffer sb = new StringBuffer();
    sb.append(System.getProperty("user.home"));
    sb.append(File.separatorChar);

    if (SystemUtils.isMacOsX())
      sb.append("Library/Caches/Java/cache/javaws/");

    else if (SystemUtils.isWindowsSystem()) {
      sb.append("Application Data\\Sun\\Java\\Deployment\\cache\\javaws\\");
    } else
      sb.append(".java/deployment/cache/javaws/");

    sb.append(url.getProtocol());
    sb.append(File.separatorChar);

    sb.append("D");
    sb.append(url.getHost());
    sb.append(File.separatorChar);

    sb.append("P");
    sb.append(url.getPort());

    String path = url.getPath();
    String[] dirs = path.split("/");
    for (int i = 1; i < dirs.length; i++) {
      sb.append(File.separatorChar);
      sb.append("DM");
      sb.append(dirs[i]);
    }

    return sb.toString();
  }

  public static void bootstrap() {

    Runtime rt = Runtime.getRuntime();

    String cmd = null;

    final String qtJambiVersion = "1.0.0-beta";
    final String qtJambiJar = "qtjambi";
    final String qtJambiLinuxJar = "qtjambi-linux";
    final String qtJambiWin32Jar = "qtjambi-win32";
    final String qtJambiMacJar = "qtjambi-mac";
    final String qtJambiLauncherJar = "qtjambi-launcher";
    final String mainClass = "com.trolltech.launcher.Launcher";

    URL urNividicWebstart = null;
    URL urlQtJambiWebstart = null;

    try {
      urNividicWebstart = new URL("http://hestia.ens.fr:80/leburon/nividic/webstart");
      urlQtJambiWebstart = new URL("http://hestia.ens.fr:80/leburon/qtjambi");
    } catch (MalformedURLException e1) {
      // TODO Auto-generated catch block
      e1.printStackTrace();
    }

    final String webstartNividicCacheDir = getWebstartCache(urNividicWebstart);
    final String webstartQtJambiCacheDir = getWebstartCache(urlQtJambiWebstart);

    if (SystemUtils.isMacOsX()) {

      final String cacheVar = "CACHE";
      final String cacheVarDollar = "$" + cacheVar;

      final String tmpMacDir = System.getProperty("user.home")
          + "/Library/Caches/NividicWebstart/" + qtJambiVersion + "/";
      final String shellScript = tmpMacDir + "nividic.sh";
      // final String libraryPath = tmpMacDir + qtJambiVersion;
      final String qtJambiMacTar = qtJambiMacJar + "-" + qtJambiVersion
          + ".tar";

      StringBuffer sb = new StringBuffer();

      sb.append("#!/bin/bash\n");
      sb.append("export DYLD_LIBRARY_PATH=");
      sb.append(tmpMacDir);
      sb.append("\nexport ");
      sb.append(cacheVar);
      sb.append("=");
      sb.append(webstartQtJambiCacheDir);

      sb
          .append("\nmkdir -p $DYLD_LIBRARY_PATH\ncd $DYLD_LIBRARY_PATH\nunzip -n ");
      jarPath(cacheVarDollar, qtJambiMacJar, qtJambiVersion, sb);
      sb.append("\ntar xkf ");
      sb.append(qtJambiMacTar);

      sb.append("\njava -XstartOnFirstThread -cp ");
      jarPath(cacheVarDollar, qtJambiJar, qtJambiVersion, sb);
      sb.append(File.pathSeparatorChar);
      jarPath(cacheVarDollar, qtJambiMacJar, qtJambiVersion, sb);
      sb.append(File.pathSeparatorChar);
      jarPath(cacheVarDollar, qtJambiLauncherJar, qtJambiVersion, sb);
      sb.append(" ");
      sb.append(mainClass);
      sb.append("\n");

      File f = new File(shellScript);
      f.getParentFile().mkdirs();

      try {

        writeFile(f, sb.toString());
      } catch (IOException e) {

        e.printStackTrace();
      }

      cmd = "bash " + shellScript;
    }

    else {

      // C:\Documents and Settings\jourdren\Application
      // Data\Sun\Java\Deployment\cache\javaws\http

      StringBuffer sb = new StringBuffer();
      sb.append("java -cp ");
      jarPath(webstartQtJambiCacheDir, qtJambiJar, qtJambiVersion, sb);
      sb.append(File.pathSeparatorChar);
      
      if (SystemUtils.isWindowsSystem())
        jarPath(webstartQtJambiCacheDir, qtJambiWin32Jar, qtJambiVersion, sb);
      else
        jarPath(webstartQtJambiCacheDir, qtJambiLinuxJar, qtJambiVersion, sb);

      sb.append(File.pathSeparatorChar);
      jarPath(webstartNividicCacheDir, qtJambiLauncherJar, qtJambiVersion, sb);
      sb.append(" ");
      sb.append(mainClass);

      cmd = sb.toString();
    }

    System.out.println("################");
    System.out.println(cmd);

    try {
      rt.exec(cmd);
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    ;

  }
}
