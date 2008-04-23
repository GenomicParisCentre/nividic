/*
 * This file contains utility methods.
 *
 * @author Laurent Jourdren
 */

/*
 * String methods
 */

/**
   * Split a String object. There is a bug with some regex in javascript. This
   * method fix it.
   * @param s String to split
   * @param regex Regex
   * @return an array of String
   */
function split(s, regex) {

  var nividicNames = JavaImporter();
  nividicNames.importPackage(Packages.fr.ens.transcriptome.nividic.js);

  with (nividicNames)  {
  
    return Util.split(s, regex);
  }

}